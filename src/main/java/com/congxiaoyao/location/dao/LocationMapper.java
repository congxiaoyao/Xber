package com.congxiaoyao.location.dao;

import com.congxiaoyao.location.pojo.GpsSamplePo;

import java.util.List;

/**
 * Created by Jaycejia on 2017/2/11.
 */
public interface LocationMapper {
    List<GpsSamplePo> getAllSample();

    List<GpsSamplePo> getTrace(Long taskId);

    void insertSample(GpsSamplePo samplePo);
}
