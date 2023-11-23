package com.plant.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装返回结果
 */
@Data
public class Result implements Serializable {
    private boolean flag; // 执行结果，true为执行成功 false为执行失败
    private String code; // 系统码
    private String message; // 返回结果信息，主要用于页面提示信息
    private Object data; // 返回数据

    public Result(boolean flag, String code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag, String code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean flag, String code, Object data) {
        this.flag = flag;
        this.code = code;
        this.data = data;
    }

}
