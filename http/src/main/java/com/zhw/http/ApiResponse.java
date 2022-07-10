package com.zhw.http;

/**
 * 请求返回数据基类
 *
 * @param <T>
 */
public class ApiResponse<T> extends BaseResResponse<T> {

    private int code;

    private T data;

    private String info;

    @Override
    public boolean isSuccess() {
        return code == 1;
    }

    @Override
    public T getResponseData() {
        return data;
    }

    @Override
    public int getResponseCode() {
        return code;
    }

    @Override
    public String getResponseMsg() {
        return info;
    }


}
