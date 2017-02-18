package com.congxiaoyao.car.dao;

import com.congxiaoyao.car.pojo.Car;
import com.congxiaoyao.car.pojo.CarDetail;
import com.congxiaoyao.user.pojo.BasicUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public interface CarUserMapper {
    List<Car> selectCarsWithoutDriver();

    BasicUserInfo selectUserByCarId(@Param("carId") Long carId);

    CarDetail selectCarInfoByCarId(@Param("carId") Long carId);

    List<CarDetail> selectFreeCars(@Param("startTime") Date startTime,
                                   @Param("endTime") Date endTime);

    List<CarDetail> selectCarsOnMission(@Param("startSpot")Long startSpot,
                                        @Param("endSpot") Long endSpot,
                                        @Param("currentTime") Date currentTime);

    List<CarDetail> selectCarsByPlate(String plate);

    List<CarDetail> selectCarsByUserName(String name);

    void updateCarUser(@Param("carId") Long carId, @Param("userId") Long userId);
}
