package com.nfsq.yqf.rocketmq_consumer.bean;

import java.io.Serializable;

/**
 * Created by qfyu
 * Date:2018/12/27
 * Time:14:39
 **/
public class YQFResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    private Boolean success;

    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
