package com.congxiaoyao.task.controller;

import com.congxiaoyao.location.dao.GpsSamplePoMapper;
import com.congxiaoyao.location.dao.LatLngConverter;
import com.congxiaoyao.location.dao.LatLngMapper;
import com.congxiaoyao.location.pojo.GpsSamplePo;
import com.congxiaoyao.spot.service.def.SpotService;
import com.congxiaoyao.task.pojo.*;
import com.congxiaoyao.task.service.def.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
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

    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.GET)
    public TaskRsp getTask(@PathVariable Long taskId) {
        Task task = taskService.getTask(taskId);
        if (task == null) return null;
        TaskRsp taskRsp = new TaskRsp(task);
        taskRsp.setStartSpot(spotService.getSpotById(task.getStartSpot()));
        taskRsp.setEndSpot(spotService.getSpotById(task.getEndSpot()));
        return taskRsp;
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
        Long total = taskService.getTaskCount(userId, status, timestamp, createUserId);
        int next = pageIndex + 1;
        if (pageIndex * pageSize + pageSize >= total) {
            next = -1;
        }
        return new TaskListRsp(timestamp, new Page(pageIndex, next, tasks.size(), total), taskRsps);
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

    /**
     * 获取任务的最后一个位置
     * @param taskId
     * @param response
     * @return
     */
    @RequestMapping(value = "/task/last", method = RequestMethod.GET)
    public GpsSamplePo getLastPosition(Long taskId, HttpServletResponse response) {
        List<GpsSamplePo> carTrace = getCarTrace(taskId);
        if (carTrace == null || carTrace.size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return carTrace.get(carTrace.size() - 1);
    }

    /**
     * 根据任务id获取车辆历史轨迹
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/task/trace/bytes", method = RequestMethod.GET)
    public byte[] getCarTraceBytes(Long taskId) {
        List<GpsSamplePo> trace = taskService.getTrace(taskId);
        return LatLngConverter.init(new GpsSamplePoMapper()).toByteArray(trace);
    }
}
