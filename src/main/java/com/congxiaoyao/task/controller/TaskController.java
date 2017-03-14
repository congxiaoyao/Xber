package com.congxiaoyao.task.controller;

import com.congxiaoyao.location.pojo.GpsSamplePo;
import com.congxiaoyao.spot.service.def.SpotService;
import com.congxiaoyao.task.pojo.*;
import com.congxiaoyao.task.service.def.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private SpotService spotService;

    /**+
     * 生成运输任务
     * @param servletRequest
     * @param request
     */
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public String generateTask(HttpServletRequest servletRequest,@RequestBody LaunchTaskRequest request) {
        taskService.generateTask((Long) servletRequest.getAttribute("userId"), request);
        return "操作成功";
    }

    /**
     * 更改任务状态
     * @param request
     */
    @RequestMapping(value = "/task/status", method = RequestMethod.PUT)
    public String changeTaskStatus(@RequestBody StatusChangeRequest request) {
        taskService.changeTaskStatus(request.getTaskId(), request.getStatus());
        return "操作成功";
    }

    /**
     * 获取任务列表
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @param status
     * @return
     */
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public TaskListRsp getTask(Long userId, Integer pageIndex, Integer pageSize, Integer status, Date timestamp, Long createUserId) {
        List<Task> tasks = taskService.getTask(userId, pageIndex, pageSize, status, timestamp, createUserId);
        List<TaskRsp> taskRsps = new ArrayList<>();
        for (Task task : tasks) {
            TaskRsp taskRsp = new TaskRsp(task);
            taskRsp.setStartSpot(spotService.getSpotById(task.getStartSpot()));
            taskRsp.setEndSpot(spotService.getSpotById(task.getEndSpot()));
            taskRsps.add(taskRsp);
        }
        if (timestamp == null) {
            timestamp = new Date();
        }
        return new TaskListRsp(timestamp, new Page(pageIndex, pageIndex + 1, tasks.size(), taskService.getTaskCount(timestamp)), taskRsps);
    }

    /**
     * 根据任务id获取车辆历史轨迹
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/task/trace", method = RequestMethod.GET)
    public List<GpsSamplePo> getCarTrace(Long taskId) {
        return taskService.getTrace(taskId);
    }
}
