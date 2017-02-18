package com.congxiaoyao.task.pojo;

import java.util.Date;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public class LaunchTaskRequest {
    private Date startTime;
    private Long startSpot;
    private Date endTime;
    private Long endSpot;
    private Long carId;
    private String content;
    private String note;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getStartSpot() {
        return startSpot;
    }

    public void setStartSpot(Long startSpot) {
        this.startSpot = startSpot;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getEndSpot() {
        return endSpot;
    }

    public void setEndSpot(Long endSpot) {
        this.endSpot = endSpot;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
