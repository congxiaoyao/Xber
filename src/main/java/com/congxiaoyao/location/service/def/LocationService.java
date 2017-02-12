package com.congxiaoyao.location.service.def;

import java.util.List;

import static com.congxiaoyao.location.pojo.GpsSampleOuterClass.GpsSample;

/**
 * Created by Jaycejia on 2017/2/11.
 */
public interface LocationService {
    List<GpsSample> getSamples();

    /**
     * 记录定位采样数据
     *
     * @param gpsSample
     */
    void recordLocation(GpsSample gpsSample);
}
