package com.congxiaoyao.location.cache;

import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.congxiaoyao.location.pojo.GpsSampleOuterClass.GpsSample;

/**
 * Created by congxiaoyao on 2017/2/9.
 */
@Component("gpsSampleCache")
public class GpsSampleCache implements IGpsSampleCache{

    private RTree rTree;
    private ConcurrentMap<Integer, PointSet> latestData;
    private ReentrantLock lock;

    private int expired;        //过期时间 毫秒

    public GpsSampleCache() {
        init(5000);
    }

    public GpsSampleCache(int expired) {
        if(expired <= 0) throw new RuntimeException("过期时间不能小于0");
        init(expired);
    }

    private void init(int expired) {
        this.expired = expired;

        rTree = new RTree();
        rTree.init(null);
        latestData = new ConcurrentHashMap<>();
        lock = new ReentrantLock(false);
    }

    /**
     * 将这个Gps采样点保存至cache中
     *
     * @param point
     */
    @Override
    public void put(GpsSample point) {
        //暂不支持过大的carId
        checkCarIdAndThrow(point.getCarId());
        int carId = (int) point.getCarId();
        //创建或获取PointSet
        PointSet pointSet = latestData.get(carId);
        if (pointSet == null) {
            lock.lock();
            pointSet = latestData.get(carId);
            //双重检查
            if (pointSet == null) {
                //这里的分支意味着是添加点
                pointSet = new PointSet();
                //添加point至PointSet
                pointSet.addPoint(point);
                //添加point至rtree
                Rectangle rectangle = pointSet.getRectangle();
                rTree.add(rectangle, carId);
                latestData.put( carId, pointSet);
                lock.unlock();
                return;
            }
            lock.unlock();
        }
        //这里意味着之前存在这个车辆上传的数据 需要更新
        //1.获取添加point之前的rectangle
        Rectangle oldRect = pointSet.getRectangle();
        //2.添加point至PointSet
        pointSet.addPoint(point);
        //3.获取需要更新的rectangle
        Rectangle rectangle = pointSet.getRectangle();
        lock.lock();
        //4.删除
        rTree.delete(oldRect, carId);
        //5.更新
        rTree.add(rectangle, carId);
        lock.unlock();
    }

    @Override
    public void clearExpired() {

    }

    /**
     * 清除某个car的缓存轨迹
     *
     * @param carId
     */
    @Override
    public void clear(long carId) {
        checkCarIdAndThrow(carId);
        int intCarID = (int) carId;
        PointSet pointSet = latestData.get(intCarID);
        latestData.remove(intCarID);
        if (pointSet != null) {
            lock.lock();
            rTree.delete(pointSet.getRectangle(), intCarID);
            lock.unlock();
        }
    }

    /**
     * 给定中心点坐标 查询d范围内离中心点最近的前n个PointSet 并将其中保存的轨迹以数组的形式获取
     * 最终返回关于轨迹数据的List
     *
     * @param lat 中心点的纬度
     * @param lng 中心点的经度
     * @param n   距离中心点最近的n个点
     * @param d   在半径为d的范围内
     * @return 关于符合条件的PointSet的List 也就是关于GPSSample[]的List
     */
    @Override
    public List<GpsSample[]> nearestN(double lng, double lat, int n, double d) {
        lock.lock();
        List<GpsSample[]> list = new ArrayList<>(n);
        rTree.nearestNUnsorted(new Point((float) lng, (float) lat), carId -> {
            PointSet pointSet = latestData.get(carId);
            if (pointSet != null) {
                list.add(pointSet.getPath());
            }
            return true;
        }, n, (float) d);
        lock.unlock();
        return list;
    }

    @Override
    public void setExpired(int expired) {
        this.expired = expired;
    }

    @Override
    public int getExpired() {
        return expired;
    }

    private void checkCarIdAndThrow(long carId) {
        if (carId > Integer.MAX_VALUE) {
            throw new RuntimeException("carId 过大!!! 真有这么多车吗?!");
        }
    }


    /**
     * 对于每一辆车 最近{@link GpsSampleCache#expired}时间内的点都会存放在PointSet中
     * 通过PointSet 可以完成如下操作:
     * <ul>
     * <li>添加点到点集 {@link PointSet#addPoint(GpsSample)}</li>
     * <li>获取点集中所有的点 {@link PointSet#getPath()}</li>
     * <li>获取点集中所有的点的最小外接矩形 {@link PointSet#getRectangle()}</li>
     * </ul>
     */
    class PointSet {

        private LinkedList<GpsSample> points;
        private ReadWriteLock lock;
        private Rectangle rectangle;

        PointSet() {
            points = new LinkedList<>();
            lock = new ReentrantReadWriteLock(true);
            rectangle = new Rectangle();
        }

        /**
         * 向点集中添加一个GpsSample对象 并根据{@link GpsSampleCache}中的过期时间自动更新相关数据
         *
         * @param point
         */
        public void addPoint(GpsSample point) {
            lock.writeLock().lock();

            //删除过期节点
            while (removeHeadPointIfExpired(point.getTime())) ;
            //添加新节点至队列
            addNewPoint(point);
            //更新rectangle
            updateRectangleByPoints();
            //TODO debug method can remove
            beforeUnLockWrite(point);

            lock.writeLock().unlock();
        }

        /**
         * 检查时间序列并将GPSSample对象加入点集
         *
         * @param point
         */
        private void addNewPoint(GpsSample point) {
            GpsSample last = null;
            if (points.size() != 0) last = points.getLast();
            points.addLast(point);
            if (last == null) return;
            if (last.getTime() > point.getTime()) throw new RuntimeException("时间序列错误");
        }

        /**
         * 检查points中的队首元素是否过期 如果过期便将其移除
         * 否则不做操作 过期是指队首元素的时间比参考时间早expired或更早
         * 注意，此方法为单线程版 多线程使用请自行加锁
         *
         * @param refTime 参考时间，判断是否过期的时间依据
         * @return 成功移除返回true 否则false
         */
        private boolean removeHeadPointIfExpired(long refTime) {
            if (points.size() == 0) return false;
            GpsSample point = points.peekFirst();
            //队首元素过期
            if (refTime - point.getTime() >= expired) {
                points.removeFirst();
                return true;
            }
            return false;
        }

        /**
         * 根据点集更新rectangle 使得rectangle成为可以覆盖points的最小矩形
         */
        private void updateRectangleByPoints() {
            if (points.size() == 0) throw new RuntimeException("外接矩形更新错误");
            //寻找最大最小经纬度并更新
            float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE,
                    maxX = -1, maxY = -1;
            //点集中只有一个点 用当前点和匀直运动一秒后的点数据构成rect
            if (points.size() == 1) {
                GpsSample point = points.getFirst();
                minX = (float) point.getLng();
                maxX = (float) (point.getLng() + point.getVlng());
                minY = (float) point.getLat();
                maxY = (float) (point.getLat() + point.getVlat());
                if (minX == maxX) maxX += 0.00006f;
                if (minY == maxY) maxY += 0.00006f;
                //min max大小不用在意 set方法可以处理
                rectangle.set(minX, minY, maxX, maxY);
                return;
            }
            //否则从点集中寻找
            for (GpsSample point : points) {
                float x = (float) point.getLng();
                float y = (float) point.getLat();
                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
            if (minX == maxX) maxX += 0.00006f;
            if (minY == maxY) maxY += 0.00006f;
            rectangle.minX = minX;
            rectangle.minY = minY;
            rectangle.maxX = maxX;
            rectangle.maxY = maxY;
        }

        /**
         * 获取可以覆盖点集中所有点的最小矩形 因为在修改数据的时候(addPoint)就完成了对矩形的更新
         * 所以这里只是简单地做边界判断和线程同步
         *
         * @return 可以覆盖点集中所有点的最小矩形
         */
        public Rectangle getRectangle() {
            lock.readLock().lock();
            if (isRectModified()) {
                lock.readLock().unlock();
                throw new RuntimeException("获取矩形时发生错误，请确认PointSet中包含数据");
            }
            //TODO 关于rectangle的回收池？
            Rectangle result = rectangle.copy();
            //TODO debug method can remove
            beforeUnlockRead(result);
            lock.readLock().unlock();
            return result;
        }

        /**
         * 获取点集中所有的点 以数组的方式呈现
         * @return
         */
        public GpsSample[] getPath() {
            lock.readLock().lock();
            int size = points.size();
            if(size == 0) {
                lock.readLock().unlock();
                throw new RuntimeException("PointSet中没有数据");
            }
            GpsSample[] result = points.toArray(new GpsSample[points.size()]);
            lock.readLock().unlock();
            return result;
        }

        private boolean isRectModified() {
            return rectangle.minX == Float.MAX_VALUE &&
                    rectangle.minY == Float.MAX_VALUE &&
                    rectangle.maxX == -Float.MAX_VALUE &&
                    rectangle.maxY == -Float.MAX_VALUE;
        }


        protected void beforeUnLockWrite(GpsSample point) {

        }

        protected void beforeUnlockRead(Rectangle result) {

        }
    }
}
