package com.congxiaoyao.location.cache;

import com.congxiaoyao.location.pojo.GpsSampleOuterClass;

import java.util.List;

/**
 * Created by Jaycejia on 2017/2/11.
 */
public interface IGpsSampleCache {
    /**
     * 获取过期时间
     */
    int getExpired();

    /**
     * 设置过期时间，单位秒
     * @param expireTime
     */
    void setExpired(int expireTime);

    /**
     * 清除某个car的缓存轨迹
     *
     * @param carId
     */
    void clear(long carId);

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
    List<GpsSampleOuterClass.GpsSample[]> nearestN(double lng, double lat, int n, double d);

    /**
     * 将这个Gps采样点保存至cache中
     *
     * @param point
     */
    void put(GpsSampleOuterClass.GpsSample point);

    /**
     * 清除所有已经过期的缓存
     */
    void clearExpired();
}
