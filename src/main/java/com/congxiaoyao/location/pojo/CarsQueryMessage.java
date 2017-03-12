package com.congxiaoyao.location.pojo;

import java.util.List;

/**
 * Created by Jaycejia on 2017/3/12.
 */
public class CarsQueryMessage {
    private Long userId;//用户id
    private Long queryId;//查询id
    private List<Long> carIds;//车辆id

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

    public List<Long> getCarIds() {
        return carIds;
    }

    public void setCarIds(List<Long> carIds) {
        this.carIds = carIds;
    }

    @Override
    public String toString() {
        return "CarsQueryMessage{" +
                "userId=" + userId +
                ", queryId=" + queryId +
                ", carIds=" + carIds +
                '}';
    }
}
