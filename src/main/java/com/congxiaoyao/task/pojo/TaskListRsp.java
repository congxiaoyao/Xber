package com.congxiaoyao.task.pojo;

import java.util.Date;
import java.util.List;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public class TaskListRsp {
    private Date timestamp;
    private Page page;
    private List<TaskRsp> taskList;

    public TaskListRsp() {
    }

    public TaskListRsp(Date timestamp, Page page, List<TaskRsp> taskList) {
        this.timestamp = timestamp;
        this.page = page;
        this.taskList = taskList;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<TaskRsp> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskRsp> taskList) {
        this.taskList = taskList;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "TaskListRsp{" +
                "timestamp=" + timestamp +
                ", page=" + page +
                ", taskList=" + taskList +
                '}';
    }
}
