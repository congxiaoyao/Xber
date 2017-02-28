package com.congxiaoyao.location.cache;

import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.congxiaoyao.location.pojo.GpsSampleOuterClass.GpsSample;

/**
 * 对于司机端上传的热数据 可以通过GpsSampleCache进行缓存 以便提高查询效率
 * cache内部使用rtree作为二位数据的索引 提供按范围查找 按车辆查找等功能
 * 使用规则见方法注释
 * <p>
 * Created by congxiaoyao on 2017/2/9.
 */
@Component("gpsSampleCache")
public class GpsSampleCache implements IGpsSampleCache{

    private RTree rTree;
    private ConcurrentMap<Integer, PointSet> latestData;
    private ReadWriteLock readWriteLock;
    private ReadWriteLock removeLock;

    private int expired;        //过期时间 毫秒

    public GpsSampleCache(int expired) {
        if (expired <= 0) throw new RuntimeException("过期时间不能小于0");
    }

    public GpsSampleCache() {
        init(5000);
    }

    private void init(int expired) {
        this.expired = expired;

        rTree = new RTree();
        rTree.init(null);
        latestData = new ConcurrentHashMap<>();

        readWriteLock = new ReentrantReadWriteLock(false);
        removeLock = new ReentrantReadWriteLock(false);
    }

    /**
     * 将这个Gps采样点保存至cache中
     *
     * @param point
     */
    public void put(GpsSample point) {
        if (point == null) return;

        //暂不支持过大的carId
        checkCarIdAndThrow(point.getCarId());
        int carId = (int) point.getCarId();

        //不允许删除和写入同时进行
        removeLock.readLock().lock();

        //创建或获取PointSet
        PointSet pointSet = latestData.get(carId);
        if (pointSet == null) {
            readWriteLock.writeLock().lock();
            pointSet = latestData.get(carId);
            //双重检查
            if (pointSet == null) {
                try {
                    //这里的分支意味着是添加点
                    pointSet = new PointSet();
                    //添加point至PointSet
                    pointSet.addPoint(point);
                    //添加point至rtree
                    Rectangle rectangle = pointSet.getRectangle();
                    if (rectangle != null) {
                        rTree.add(rectangle, carId);
                        latestData.put(carId, pointSet);
                    }
                } finally {
                    readWriteLock.writeLock().unlock();
                    removeLock.readLock().unlock();
                }
                return;
            }
            readWriteLock.writeLock().unlock();
        }

        //这里意味着之前存在这个车辆上传的数据 需要更新
        pointSet.writeLock().lock();
        try {
            //1.获取添加point之前的rectangle
            Rectangle oldRect = pointSet.getRectangle();
            //2.添加point至PointSet
            pointSet.addPoint(point);
            //3.获取需要更新的rectangle
            Rectangle newRect = pointSet.getRectangle();
            readWriteLock.writeLock().lock();
            //4.更新
            try {
                if (oldRect != null) rTree.delete(oldRect, carId);
                if (newRect != null) rTree.add(newRect, carId);
            } finally {
                readWriteLock.writeLock().unlock();
            }
        } finally {
            pointSet.writeLock().unlock();
        }

        removeLock.readLock().unlock();
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
    public List<GpsSample[]> nearestN(double lng, double lat, int n, double d) {
        removeLock.readLock().lock();
        List<GpsSample[]> list = new ArrayList<>(n);
        try {
            List<PointSet> pointSets = new ArrayList<>(n);
            readWriteLock.readLock().lock();
            try {
                rTree.nearestNUnsorted(new Point((float) lng, (float) lat), carId -> {
                    PointSet pointSet = latestData.get(carId);
                    if (pointSet != null) pointSets.add(pointSet);
                    return true;
                }, n, (float) d);
            } finally {
                readWriteLock.readLock().unlock();
            }
            pointSets.stream().forEach(pointSet -> {
                pointSet.readLock().lock();
                GpsSample[] path = null;
                try {
                    path = pointSet.getPath();
                } finally {
                    pointSet.readLock().unlock();
                    if (path != null) list.add(path);
                }
            });
        } finally {
            removeLock.readLock().unlock();
        }
        return list;
    }

    /**
     * 清除某个car的缓存轨迹
     *
     * @param carId
     */
    public void clear(long carId) {
        checkCarIdAndThrow(carId);
        //stop the world!
        removeLock.writeLock().lock();
        try {
            int intCarID = (int) carId;
            PointSet pointSet = latestData.get(intCarID);
            latestData.remove(intCarID);
            if (pointSet != null) {
                Rectangle rectangle = pointSet.getRectangle();
                if (rectangle != null) rTree.delete(rectangle, intCarID);
            }
        } finally {
            removeLock.writeLock().unlock();
        }
    }

    /**
     * 给定参考时间 删除比参考时间早expired或更早的点或点集
     *
     * @param refTime
     * @see GpsSampleCache#clearExpired()
     */
    public void clearExpired(final long refTime) {
        //stop the world!
        removeLock.writeLock().lock();
        try {
            latestData.forEach((carId, pointSet) -> {
                Rectangle oldRect = pointSet.getRectangle();
                //这是个异常啊朋友 炒鸡古怪
                if (oldRect == null) {
                    latestData.remove(carId);
                    return;
                }
                //删除点集中的过期位置
                boolean hasRemoved = pointSet.removeExpiredPoint(refTime) != 0;
                if (!hasRemoved) return;
                //删掉rtree中的所以以及map中的引用
                if (pointSet.points.size() == 0) {
                    rTree.delete(oldRect, carId);
                    latestData.remove(carId);
                    return;
                }
                //删除了这辆车的一部分点 只需要更新rtree
                Rectangle nowRect = pointSet.getRectangle();
                rTree.delete(oldRect, carId);
                rTree.add(nowRect, carId);
            });
        } finally {
            removeLock.writeLock().unlock();
        }
    }

    /**
     * 删除比当前系统时间早expired或更早的点或点集
     *
     * @see GpsSampleCache#clearExpired(long)
     */
    public void clearExpired() {
        clearExpired(System.currentTimeMillis());
    }

    /**
     * 根据carIds查询对应车辆的轨迹
     *
     * @param carIds
     * @return
     */
    public List<GpsSample[]> getTraceByCarIds(List<Long> carIds) {
        removeLock.readLock().lock();
        List<GpsSample[]> result = new ArrayList<>(carIds.size());
        try {
            carIds.forEach(carId -> {
                if (carId > Integer.MAX_VALUE) return;
                PointSet pointSet = latestData.get((int) ((long) carId));
                if (pointSet == null) return;
                pointSet.readLock().lock();
                try {
                    GpsSample[] path = pointSet.getPath();
                    if (path != null) result.add(path);
                } finally {
                    pointSet.readLock().unlock();
                }
            });
        } finally {
            removeLock.readLock().unlock();
        }
        return result;
    }

    /**
     * 设置过期时间
     *
     * @param expired
     */
    public void setExpired(int expired) {
        this.expired = expired;
    }

    /**
     * @return 过期时间
     */
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

        private TreeSet<GpsSample> points;
        private ReadWriteLock lock;
        private Rectangle rectangle = null;

        PointSet() {
            points = new TreeSet<>((sample1, sample2) ->
                    (int) (sample1.getTime() - sample2.getTime()));
            lock = new ReentrantReadWriteLock(false);
        }

        /**
         * 向点集中添加一个GpsSample对象 并根据{@link GpsSampleCache}中的过期时间自动更新相关数据
         *
         * @param point
         */
        public void addPoint(GpsSample point) {
            //添加新节点至队列
            points.add(point);
            //删除过期节点
            while (removeHeadPointIfExpired(point.getTime())) ;
            //更新rectangle
            updateRectangleByPoints();
        }

        public int removeExpiredPoint(long refTime) {
            int size = 0;
            while (removeHeadPointIfExpired(refTime)) size++;
            return size;
        }

        /**
         * 检查points中的队首元素是否过期 如果过期便将其移除
         * 否则不做操作 过期是指队首元素的时间比参考时间早expired或更早
         *
         * @param refTime 参考时间，判断是否过期的时间依据
         * @return 成功移除返回true 否则false
         */
        private boolean removeHeadPointIfExpired(long refTime) {
            if (points.size() == 0) return false;
            GpsSample point = points.first();
            //队首元素过期
            if (refTime - point.getTime() >= expired) {
                points.pollFirst();
                return true;
            }
            return false;
        }

        /**
         * 根据点集更新rectangle 使得rectangle成为可以覆盖points的最小矩形
         */
        private void updateRectangleByPoints() {
            if (points.size() == 0) {
                rectangle = null;
                return;
            }
            //寻找最大最小经纬度并更新
            float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE,
                    maxX = -1, maxY = -1;
            if (rectangle == null) rectangle = new Rectangle();

            //点集中只有一个点 用当前点和匀直运动一秒后的点数据构成rect
            if (points.size() == 1) {
                GpsSample point = points.first();
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
         * 获取可以覆盖点集中所有点的最小矩形
         * 因为在修改数据的时候{@link PointSet#addPoint(GpsSample)}就完成了对矩形的更新
         * 所以这里只是
         *
         * @return 可以覆盖点集中所有点的最小矩形 当点集中没有数据的时候返回null
         */
        public Rectangle getRectangle() {
            if (rectangle == null) return null;
            Rectangle result = rectangle.copy();
            return result;
        }

        /**
         * 获取点集中所有的点 以数组的方式呈现
         *
         * @return 在某些情况下 可能没有数据 此时返回null
         */
        public GpsSample[] getPath() {
            int size = points.size();
            if (size == 0) return null;
            GpsSample[] result = points.toArray(new GpsSample[size]);
            return result;
        }

        public Lock writeLock() {
            return lock.writeLock();
        }

        public Lock readLock() {
            return lock.readLock();
        }
    }
}