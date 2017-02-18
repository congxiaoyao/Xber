package com.congxiaoyao.car.service.impl;

import com.congxiaoyao.car.dao.CarUserMapper;
import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.car.service.def.CarService;
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
        return carUserMapper.selectCarInfobyCarId(carId);
    }
    /**
     * 获取车辆列表
     *
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    @Override
    public List<CarDetail> getFreeCars(Date startTime, Date endTime, Integer status) {
        return null;
    }
}
