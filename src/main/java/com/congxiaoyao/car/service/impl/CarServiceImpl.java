package com.congxiaoyao.car.service.impl;

import com.congxiaoyao.car.dao.CarUserMapper;
import com.congxiaoyao.car.pojo.Car;
import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.car.pojo.NewCar;
import com.congxiaoyao.car.service.def.CarService;
import com.congxiaoyao.location.dao.LocationMapper;
import com.congxiaoyao.user.pojo.BasicUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@Transactional
@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarUserMapper carUserMapper;

    /**
     * 添加一辆新的车辆
     *
     * @param car
     */
    @Override
    public void addCar(NewCar car) {
        carUserMapper.insertCar(car);
    }

    /**
     * 获取所有未绑司机车辆
     *
     * @return
     */
    @Override
    public List<Car> getCarsWithoutDriver() {
        return carUserMapper.selectCarsWithoutDriver();
    }

    /**
     * 根据车辆Id查询司机用户详情
     *
     * @param carId
     * @return
     */
    @Override
    public BasicUserInfo getUserByCarId(Long carId) {
        return carUserMapper.selectUserByCarId(carId);
    }

    /**
     * 根据car id获取车辆信息
     *
     * @param carId
     * @return
     */
    @Override
    public CarDetail getCarInfo(Long carId) {
        return carUserMapper.selectCarInfoByCarId(carId);
    }
    /**
     * 获取车辆列表
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<CarDetail> getFreeCars(Date startTime, Date endTime) {
        return carUserMapper.selectFreeCars(startTime, endTime);
    }

    /**
     * 根据始发目的地，获取正在执行任务的车辆列表
     *
     * @param startSpot
     * @param endSpot
     * @return
     */
    @Override
    public List<CarDetail> getCarsOnTask(Long startSpot, Long endSpot) {
        return carUserMapper.selectCarsOnMission(startSpot, endSpot, new Date());
    }

    /**
     * 根据车牌号查
     *
     * @param plate
     * @return
     */
    @Override
    public List<CarDetail> getCarsByPlate(String plate) {
        return carUserMapper.selectCarsByPlate(plate);
    }

    /**
     * 根据用户的名字查询车辆
     *
     * @param name
     * @return
     */
    @Override
    public List<CarDetail> getCarsByUserName(String name) {
        return carUserMapper.selectCarsByUserName(name);
    }

    /**
     * 获得所有的司机信息
     *
     * @return
     */
    @Override
    public List<CarDetail> getAllDrivers() {
        return carUserMapper.selectDrivers();
    }

    /**
     * 设置车辆司机
     *
     * @param carId
     * @param userId
     */
    @Override
    public void changeCarDriver(Long carId, Long userId) {
        carUserMapper.updateCarUser(carId, userId);
    }
}
