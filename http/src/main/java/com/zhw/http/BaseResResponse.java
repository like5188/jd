package com.zhw.http;

import java.io.Serializable;

/**
 * Created by *** on 2021/3/10 9:28 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
public abstract class BaseResResponse<T> implements Serializable {

    public abstract boolean isSuccess();

    public abstract T getResponseData();

    public abstract int getResponseCode();

    public abstract String getResponseMsg();
}