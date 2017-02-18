package com.congxiaoyao.location.pojo;

import java.util.Date;

/**
 * Created by Jaycejia on 2017/2/11.
 */
public class GpsSamplePo {
    private Long traceId;
    private Long carId;
    private Long taskId;
    private Double vLat;
    private Double vLong;
    private Double latitude;
    private Double longitude;
    private Date time;

    public GpsSamplePo() {
    }

    public GpsSamplePo(GpsSampleOuterClass.GpsSample sample) {
        this.carId = sample.getCarId();
        this.taskId = sample.getTaskId();
        this.vLat = sample.getVlat();
        this.vLong = sample.getVlng();
        this.latitude = sample.getLat();
        this.longitude = sample.getLng();
        this.time = new Date(sample.getTime());
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Double getvLat() {
        return vLat;
    }

    public void setvLat(Double vLat) {
        this.vLat = vLat;
    }

    public Double getvLong() {
        return vLong;
    }

    public void setvLong(Double vLong) {
        this.vLong = vLong;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public GpsSampleOuterClass.GpsSample toGpsSample() {
        GpsSampleOuterClass.GpsSample.Builder builder = GpsSampleOuterClass.GpsSample.newBuilder();
        builder.setCarId(carId);
        builder.setTaskId(taskId);
        builder.setLat(latitude);
        builder.setLng(longitude);
        builder.setVlat(vLat);
        builder.setVlng(vLong);
        builder.setTime(time.getTime());
        return builder.build();
    }
}
