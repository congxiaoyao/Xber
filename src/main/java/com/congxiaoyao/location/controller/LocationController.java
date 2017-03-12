package com.congxiaoyao.location.controller;

import com.congxiaoyao.location.cache.IGpsSampleCache;
import com.congxiaoyao.location.service.def.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Jaycejia on 2017/2/11.
 */
@RestController
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    @Resource(name = "gpsSampleCacheProxy")
    private IGpsSampleCache gpsSampleCache;

}
