package com.congxiaoyao.location.manager;

import com.congxiaoyao.location.pojo.GpsSampleOuterClass;
import com.congxiaoyao.location.service.def.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GPS Sample代理类
 * Created by Jaycejia on 2017/2/11.
 */
@Service("gpsSampleCacheProxy")
public class GpsSampleCacheProxy implements IGpsSampleCache {
    private IGpsSampleCache gpsSampleCache;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private LocationService locationService;
    @Autowired
    public GpsSampleCacheProxy(IGpsSampleCache gpsSampleCache) {
        this.gpsSampleCache = gpsSampleCache;
    }

    @Override
    public int getExpired() {
        return gpsSampleCache.getExpired();
    }

    @Override
    public void setExpired(int expireTime) {
        gpsSampleCache.setExpired(expireTime);
    }

    @Override
    public void clear(long carId) {
        gpsSampleCache.clear(carId);
    }

    @Override
    public List<GpsSampleOuterClass.GpsSample[]> nearestN(double lng, double lat, int n, double d) {
        return gpsSampleCache.nearestN(lng, lat, n, d);
    }

    @Override
    public void put(GpsSampleOuterClass.GpsSample point) {
        taskExecutor.execute(()->
            locationService.recordLocation(point)
        );
        gpsSampleCache.put(point);
    }

    @Override
    public void clearExpired() {
        gpsSampleCache.clearExpired();
    }

    @Scheduled(fixedRate = 10000L)
    void autoClearCache() {
        gpsSampleCache.clearExpired();
    }
}
