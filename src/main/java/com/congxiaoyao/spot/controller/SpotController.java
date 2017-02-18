package com.congxiaoyao.spot.controller;

import com.congxiaoyao.spot.pojo.Spot;
import com.congxiaoyao.spot.service.def.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@RestController
public class SpotController {
    @Autowired
    private SpotService spotService;

    @RequestMapping(value = "/spot/all", method = RequestMethod.GET)
    public List<Spot> getAllSpots() {
        return spotService.getSpots();
    }
}
