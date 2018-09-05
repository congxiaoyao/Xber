package com.congxiaoyao.location.dao;

import com.congxiaoyao.location.pojo.GpsSamplePo;

/**
 * Created by congxiaoyao on 2017/4/2.
 */
public class GpsSamplePoMapper implements LatLngMapper<GpsSamplePo> {
    @Override
    public double getLat(GpsSamplePo gpsSamplePo) {
        return gpsSamplePo.getLatitude();
    }

    @Override
    public double getLng(GpsSamplePo gpsSamplePo) {
        return gpsSamplePo.getLongitude();
    }

    @Override
    public GpsSamplePo toObject(double lat, double lng) {
        GpsSamplePo gpsSamplePo = new GpsSamplePo();
        gpsSamplePo.setLatitude(lat);
        gpsSamplePo.setLongitude(lng);
        return gpsSamplePo;
    }
}
