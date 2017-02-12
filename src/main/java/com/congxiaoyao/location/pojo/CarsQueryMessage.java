package com.congxiaoyao.location.pojo;

/**
 * Created by Jaycejia on 2017/2/12.
 */
public class CarsQueryMessage {
    private Long userId;//用户id
    private Long queryId;//查询id
    private Double latitude;//纬度
    private Double longitude;//经度
    private Integer number;//查询数量
    private Double radius;//半径

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "CarsQueryMessage{" +
                "userId=" + userId +
                ", queryId=" + queryId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", number=" + number +
                ", radius=" + radius +
                '}';
    }
}
