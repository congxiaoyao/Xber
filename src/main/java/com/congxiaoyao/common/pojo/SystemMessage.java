package com.congxiaoyao.common.pojo;

/**
 * 系统消息定义
 * Created by Jaycejia on 2017/2/18.
 */
public class SystemMessage<T> {
    public static final int NEW_TASK = 0;
    public static final int TASK_START = 1;
    public static final int TASK_END = 2;

    private int code;
    private T payload;

    public SystemMessage() {
    }

    public SystemMessage(int code, T payload) {
        this.code = code;
        this.payload = payload;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
