package com.congxiaoyao.location.controller;

import com.congxiaoyao.location.cache.IGpsSampleCache;
import com.congxiaoyao.location.pojo.CarPosition;
import com.congxiaoyao.location.pojo.GpsSampleOuterClass;
import com.congxiaoyao.location.pojo.GpsSampleOuterClass.GpsSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Jaycejia on 2017/2/11.
 */
@RestController
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    @Resource(name = "gpsSampleCacheProxy")
    private IGpsSampleCache gpsSampleCache;

    @RequestMapping(value = "/location/running", method = RequestMethod.POST)
    public List<CarPosition> getRunningCars(@RequestBody List<Long> carIds) {
        if (carIds == null || carIds.size() == 0) {
            return Collections.emptyList();
        }
        return gpsSampleCache.getTraceByCarIds(carIds)
                .stream()
                .filter(Objects::nonNull)
                .filter(trace -> trace.length > 0)
                .map(trace -> new CarPosition(trace[0].getCarId(),
                        Stream.of(trace).mapToDouble(GpsSample::getLat).average().getAsDouble(),
                        Stream.of(trace).mapToDouble(GpsSample::getLng).average().getAsDouble()))
                .collect(Collectors.toList());
    }
}
