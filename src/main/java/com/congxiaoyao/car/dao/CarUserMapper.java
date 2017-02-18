package com.congxiaoyao.car.dao;

import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.user.pojo.BasicUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public interface CarUserMapper {

    BasicUserInfo selectUserByCarId(@Param("carId") Long carId);

    CarDetail selectCarInfobyCarId(@Param("carId") Long carId);

    List<CarDetail> selectCars(@Param("startTime") Date startTime,
                               @Param("endTime") Date endTime,
                               @Param("status") Integer status);
}
