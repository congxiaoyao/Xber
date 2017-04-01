package com.congxiaoyao.spot.controller;

import com.congxiaoyao.spot.pojo.Spot;
import com.congxiaoyao.spot.service.def.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/spot", method = RequestMethod.POST)
    public String addSpot(@RequestBody Spot spot) {
        spotService.addSpot(spot);
        return "添加成功";
    }

    @RequestMapping(value = "/spot", method = RequestMethod.PUT)
    public String updateSpot(@RequestBody Spot spot, HttpServletResponse response) {
        int count = spotService.updateSpot(spot);
        if (count < 1) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "更新失败";
        }
        return "更新成功";
    }

    @RequestMapping(value = "/spot/{id}", method = RequestMethod.DELETE)
    public String deleteSpot(@PathVariable Long id, HttpServletResponse response) {
        int count = spotService.removeSpotById(id);
        if (count < 1) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "删除失败";
        }
        return "删除成功";
    }
}
