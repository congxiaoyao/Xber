package com.congxiaoyao.location.pojo;

/**
 * Created by congxiaoyao on 2017/5/3.
 */
public class CarPosition {

    private Long carId;
    private Double lat;
    private Double lng;

    public CarPosition(Long carId, Double lat, Double lng) {
        this.carId = carId;
        this.lat = lat;
        this.lng = lng;
    }

    public CarPosition() {
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "CarPosition{" +
                "carId=" + carId +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
