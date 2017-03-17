package com.congxiaoyao.task.service.impl;

import com.congxiaoyao.car.dao.CarUserMapper;
import com.congxiaoyao.common.URIConfig;
import com.congxiaoyao.location.dao.LocationMapper;
import com.congxiaoyao.location.pojo.GpsSamplePo;
import com.congxiaoyao.task.dao.TaskDetailMapper;
import com.congxiaoyao.task.dao.TaskMapper;
import com.congxiaoyao.task.pojo.LaunchTaskRequest;
import com.congxiaoyao.task.pojo.Task;
import com.congxiaoyao.task.pojo.TaskExample;
import com.congxiaoyao.task.service.def.TaskService;
import com.congxiaoyao.user.pojo.BasicUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@Transactional
@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskDetailMapper detailMapper;
    @Autowired
    private CarUserMapper carUserMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 获取司机运输任务
     *
     * @param userId
     * @param pageIndex
     * @param pageSize
     */
    @Override
    public List<Task> getTask(Long userId, Integer pageIndex, Integer pageSize, Integer status, Date timestamp, Long createUserId) {
        return detailMapper.selectByCondition(userId, pageIndex * pageSize, pageSize, status, timestamp, createUserId);
    }

    /**
     * 根据时间戳获取任务总数
     *
     * @param timestamp
     * @return
     */
    @Override
    public Long getTaskCount(Long userId, Integer status, Date timestamp, Long createUserId) {
        return detailMapper.countByCondition(userId, status, timestamp, createUserId);
    }

    /**
     * 改变任务状态
     *
     * @param taskId
     * @param status
     */
    @Override
    public void changeTaskStatus(Long taskId, Integer status) {
        Task task = new Task();
        task.setTaskId(taskId);
        if (status == Task.STATUS_EXECUTING) {
            task.setRealStartTime(new Date());
        } else if (status == Task.STATUS_COMPLETED) {
            task.setRealEndTime(new Date());
        }
        task.setStatus(status);
        if (logger.isDebugEnabled()) {
            logger.debug("Task {} has changed to status:{}", taskId, status);
        }
        taskMapper.updateByPrimaryKeySelective(task);
        //通知管理员状态变化
        messagingTemplate.convertAndSend(URIConfig.TASK_STATUS_CHANGE, task);
    }

    /**
     * 生成任务
     *
     * @param taskRequest
     */
    @Override
    public void generateTask(Long userId, LaunchTaskRequest taskRequest) {
        Task task = new Task(taskRequest.getCarId(), taskRequest.getStartTime(),
                taskRequest.getStartSpot(), taskRequest.getEndTime(),
                taskRequest.getEndSpot(), taskRequest.getContent(), userId, new Date(),
                null, null,
                Task.STATUS_DELIVERED, taskRequest.getNote());
        if (logger.isDebugEnabled()) {
            logger.debug("{} has generated task:{}", userId, task);
        }
        taskMapper.insert(task);
        //推送给特定用户
        BasicUserInfo userInfo = carUserMapper.selectUserByCarId(task.getCarId());
        messagingTemplate.convertAndSendToUser(userInfo.getUserId().toString(), URIConfig.USER_SYSTEM, 1);
    }


    /**
     * 根据任务id获取车辆轨迹
     *
     * @param taskId
     * @return
     */
    @Override
    public List<GpsSamplePo> getTrace(Long taskId) {
        return locationMapper.getTrace(taskId);
    }
}
