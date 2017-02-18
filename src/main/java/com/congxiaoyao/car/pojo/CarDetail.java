package com.congxiaoyao.car.pojo;

import com.congxiaoyao.user.pojo.BasicUserInfo;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public class CarDetail {
    private Long carId;
    private BasicUserInfo userInfo;
    private String plate;
    private String spec;

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public BasicUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BasicUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
