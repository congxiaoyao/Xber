package com.congxiaoyao.car.controller;

import com.congxiaoyao.car.pojo.Car;
import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.car.pojo.CarDriverReq;
import com.congxiaoyao.car.pojo.NewCar;
import com.congxiaoyao.car.service.def.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@RestController
public class CarController {
    @Autowired
    private CarService carService;

    @RequestMapping(value = "/car", method = RequestMethod.POST)
    public String addCar(@RequestBody NewCar car) {
        carService.addCar(car);
        return "添加成功";
    }

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
     * 根据user id获取车辆信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/car/user", method = RequestMethod.GET)
    public CarDetail getCarInfoByUserId(Long userId) {
        return carService.getDriverDetail(userId);
    }

    /**
     * 获取没有分配司机的车辆
     * @return
     */
    @RequestMapping(value = "/car/unused", method = RequestMethod.GET)
    public List<Car> getCarsWithoutDriver() {
        return carService.getCarsWithoutDriver();
    }

    /**
     * 获取闲置车辆列表
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/car/free", method = RequestMethod.GET)
    public List<CarDetail> getFreeCars(Date startTime, Date endTime) {
        return carService.getFreeCars(startTime, endTime);
    }

    /**
     * 根据目的地始发地获取目前正在执行任务的车辆，若传空为不指定
     *
     * @param startSpot
     * @param endSpot
     * @return
     */
    @RequestMapping(value = "/car/onTask", method = RequestMethod.GET)
    public List<CarDetail> getCarsOnTask(Long startSpot, Long endSpot) {
        return carService.getCarsOnTask(startSpot, endSpot);
    }

    /**
     * 根据车牌获取车辆信息(模糊匹配）
     *
     * @param plate
     * @return
     */
    @RequestMapping(value = "/car/{plate}/plate", method = RequestMethod.GET)
    public List<CarDetail> getCarsByPlate(@PathVariable("plate") String plate) {
        return carService.getCarsByPlate(plate);
    }

    /**
     * 根据用户姓名获取车辆信息
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/car/{name}/name",method = RequestMethod.GET)
    public List<CarDetail> getCarsByName(@PathVariable String name) {
        return carService.getCarsByUserName(name);
    }

    /**
     * 为车辆分配司机
     * @param req
     * @return
     */
    @RequestMapping(value = "/car/driver", method = RequestMethod.PUT)
    public String changeCarDriver(@RequestBody CarDriverReq req) {
        carService.changeCarDriver(req.getCarId(), req.getUserId());
        return "操作成功";
    }

    @RequestMapping(value = "/car/driver", method = RequestMethod.GET)
    public List<CarDetail> getAllCarDrivers() {
        return carService.getAllDrivers();
    }
}

