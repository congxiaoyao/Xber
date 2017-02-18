package com.congxiaoyao.car.controller;

import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.car.service.def.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@RestController
public class CarController {
    @Autowired
    private CarService carService;

    /**
     * 根据car id获取车辆信息
     *
     * @param carId
     * @return
     */
    @RequestMapping(value = "/car", method = RequestMethod.GET)
    public CarDetail getCarInfo(Long carId) {
        return carService.getCarInfo(carId);
    }

    /**
     * 获取车辆列表
     *
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    public List<CarDetail> getCars(Date startTime, Date endTime, Integer status) {
        return carService.getCars(startTime, endTime, status);
    }
}

