package com.congxiaoyao.car.service.def;

import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.user.pojo.BasicUserInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public interface CarService {
    /**
     * 根据车辆Id查询司机用户详情
     *
     * @param carId
     * @return
     */
    BasicUserInfo getUserByCarId(Long carId);

    /**
     * 根据car id获取车辆信息
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
    List<CarDetail> getCars(Date startTime, Date endTime, Integer status);
}
