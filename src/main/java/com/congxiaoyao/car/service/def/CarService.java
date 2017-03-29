package com.congxiaoyao.car.service.def;

import com.congxiaoyao.car.pojo.Car;
import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.car.pojo.NewCar;
import com.congxiaoyao.location.pojo.GpsSamplePo;
import com.congxiaoyao.user.pojo.BasicUserInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public interface CarService {
    /**
     * 添加一辆新的车辆
     *
     * @param car
     */
    void addCar(NewCar car);

    /**
     * 获取所有未绑司机车辆
     *
     * @return
     */
    List<Car> getCarsWithoutDriver();

    /**
     * 根据车辆Id查询司机用户详情
     *
     * @param carId
     * @return
     */
    BasicUserInfo getUserByCarId(Long carId);

    /**
     * 根据car id获取车辆信息
     *
     * @param carId
     * @return
     */
    CarDetail getCarInfo(Long carId);

    /**
     * 获取车辆列表
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<CarDetail> getFreeCars(Date startTime, Date endTime);

    /**
     * 根据始发目的地，获取正在执行任务的车辆列表
     *
     * @param startSpot
     * @param endSpot
     * @return
     */
    List<CarDetail> getCarsOnTask(Long startSpot, Long endSpot);

    /**
     * 根据车牌号查
     *
     * @param plate
     * @return
     */
    List<CarDetail> getCarsByPlate(String plate);

    /**
     * 根据用户的名字查询车辆
     *
     * @param name
     * @return
     */
    List<CarDetail> getCarsByUserName(String name);

    /**
     * 获得所有的司机信息
     * @return
     */
    List<CarDetail> getAllDrivers();

    /**
     * 设置车辆司机
     *
     * @param carId
     * @param userId
     */
    void changeCarDriver(Long carId, Long userId);
}
