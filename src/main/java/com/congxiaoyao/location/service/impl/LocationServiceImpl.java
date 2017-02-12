package com.congxiaoyao.location.service.impl;

import com.congxiaoyao.location.dao.LocationMapper;
import com.congxiaoyao.location.pojo.GpsSamplePo;
import com.congxiaoyao.location.service.def.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.congxiaoyao.location.pojo.GpsSampleOuterClass.GpsSample;
/**
 * Created by Jaycejia on 2017/2/11.
 */
@Transactional
@Service
public class LocationServiceImpl implements LocationService{
    @Autowired
    private LocationMapper locationMapper;

    @Override
    public List<GpsSample> getSamples() {
        List<GpsSamplePo> allSample = locationMapper.getAllSample();
        List<GpsSample> result = new ArrayList<>();
        for (GpsSamplePo gpsSamplePo : allSample) {
            result.add(gpsSamplePo.toGpsSample());
        }
        return result;
    }

    @Override
    public void recordLocation(GpsSample gpsSample) {
        locationMapper.insertSample(new GpsSamplePo(gpsSample));
    }
}
