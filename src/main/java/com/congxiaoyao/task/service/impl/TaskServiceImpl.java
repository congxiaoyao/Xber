package com.congxiaoyao.task.service.impl;

import com.congxiaoyao.car.dao.CarUserMapper;
import com.congxiaoyao.common.URIConfig;
import com.congxiaoyao.location.dao.LocationMapper;
import com.congxiaoyao.location.pojo.GpsSamplePo;
import com.congxiaoyao.task.dao.TaskDetailMapper;
import com.congxiaoyao.task.dao.TaskMapper;
import com.congxiaoyao.task.pojo.LaunchTaskRequest;
import com.congxiaoyao.task.pojo.Task;
import com.congxiaoyao.task.pojo.TaskAndDriver;
import com.congxiaoyao.task.pojo.TaskRsp;
import com.congxiaoyao.task.service.def.TaskService;
import com.congxiaoyao.user.pojo.BasicUserInfo;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Task getTask(Long taskId) {
        return taskMapper.selectByPrimaryKey(taskId);
    }

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
        task = taskMapper.selectByPrimaryKey(taskId);
        task.setStatus(status);
        TaskAndDriver taskAndDriver = new TaskAndDriver(task, carUserMapper
                .selectCarInfoByCarId(task.getCarId()));
        taskAndDriver.setTaskId(taskId);
        //通知管理员状态变化
        CompletableFuture.runAsync(() -> pushToAdmin(taskAndDriver));
//        try {
//            messagingTemplate.convertAndSend(URIConfig.TASK_STATUS_CHANGE, taskAndDriver);
//        } catch (Exception e) {
//            e.printStackTrace();
//            messagingTemplate.setMessageConverter(new MappingJackson2MessageConverter());
//            messagingTemplate.convertAndSend(URIConfig.TASK_STATUS_CHANGE, taskAndDriver);
//        }
    }


    private void pushToAdmin(TaskAndDriver task) {
        if (task.getCreateUser() == 1) return;
        Constants.useOfficial();
        Sender sender = new Sender(URIConfig.ADMIN_APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "Xber司机接单啦！";
        if (Integer.valueOf(Task.STATUS_COMPLETED).equals(task.getStatus())) {
            title = "Xber司机完成了一个任务！";
        }
        String description = "点击查看详情";
        Message message = new Message.Builder()
                .title(title)
                .passThrough(0)
                .timeToLive(7 * 24 * 60 * 60 * 1000)
                .extra(Constants.EXTRA_PARAM_NOTIFY_FOREGROUND, "1")
                .extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_ACTIVITY)
                .extra(Constants.EXTRA_PARAM_INTENT_URI, URIConfig.ADMIN_APP_ACTIVITY_URI)
                .extra(URIConfig.KEY_TASK_ID, String.valueOf(task.getTaskId()))
                .extra(URIConfig.KEY_TASK_STATUS, String.valueOf(task.getStatus()))
                .description(description).payload(messagePayload)
                .restrictedPackageName(URIConfig.ADMIN_ARP_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();

        try {
            sender.sendToUserAccount(message, String.valueOf(task.getCreateUser()), 3);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        CompletableFuture.runAsync(() -> pushToDriver(userInfo.getUserId().toString()));
    }

    private void pushToDriver(String driverId) {
        Constants.useOfficial();
        Sender sender = new Sender(URIConfig.DRIVER_APP_SECRET_KEY);
        String messagePayload=  "This is a message";
        String title =  "您有一个新的任务！";
        String description = "点击查看详情";
        Message message = new Message.Builder()
                .title(title)
                .passThrough(0)
                .timeToLive(7 * 24 * 60 * 60 * 1000)
                .extra(Constants.EXTRA_PARAM_NOTIFY_FOREGROUND, "0")
                .extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_ACTIVITY)
                .extra(Constants.EXTRA_PARAM_INTENT_URI, URIConfig.DRIVER_APP_ACTIVITY_URI)
                .description(description).payload(messagePayload)
                .restrictedPackageName(URIConfig.DRIVER_APP_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();

        try {
            sender.sendToUserAccount(message, driverId, 3);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
