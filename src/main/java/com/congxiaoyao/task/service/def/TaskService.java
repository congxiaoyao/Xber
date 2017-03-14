package com.congxiaoyao.task.service.def;

import com.congxiaoyao.location.pojo.GpsSamplePo;
import com.congxiaoyao.task.pojo.LaunchTaskRequest;
import com.congxiaoyao.task.pojo.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public interface TaskService {
    /**
     * 获取司机运输任务
     *
     * @param userId
     * @param pageIndex
     * @param pageSize
     */
    List<Task> getTask(Long userId, Integer pageIndex, Integer pageSize, Integer status, Date timestamp, Long createUserId);

    /**
     * 根据时间戳获取任务总数
     * @param timestamp
     * @return
     */
    Long getTaskCount(Date timestamp);
    /**
     * 改变任务状态
     * @param taskId
     * @param status
     */
    void changeTaskStatus(Long taskId, Integer status);

    /**
     * 生成任务
     *
     * @param taskRequest
     */
    void generateTask(Long userId, LaunchTaskRequest taskRequest);

    /**
     * 根据任务id获取车辆轨迹
     * @param taskId
     * @return
     */
    List<GpsSamplePo> getTrace(Long taskId);
}
