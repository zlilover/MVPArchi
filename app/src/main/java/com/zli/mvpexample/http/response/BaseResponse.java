package com.zli.mvpexample.http.response;

import java.io.Serializable;

/**
 * Created by lizhen on 2021/3/17.
 */
public class BaseResponse<T> implements Serializable {
    // http状态吗，用来做响应分发
    private int httpResponseCode = 200;
    // http请求码
    private int httpTag;

    private String code;
    private String msg;
    private T data;

    public BaseResponse(T data) {
        this.data = data;
    }

    public BaseResponse(T data, String msg){
        this.data = data;
        this.msg = msg;
    }

    public BaseResponse(T data, String msg, int httpResponseCode) {
        this.data = data;
        this.msg = msg;
        this.httpResponseCode = httpResponseCode;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setHttpTag(int httpTag) {
        this.httpTag = httpTag;
    }

    public int getHttpTag() {
        return httpTag;
    }

}
