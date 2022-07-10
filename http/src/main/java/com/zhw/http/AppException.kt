package com.zhw.http

import android.util.Log

/**
 * Created by *** on 2021/3/10 9:50 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class AppException : RuntimeException {

    var msg: String = "" //错误提示消息
    var status: Int = -404 //错误码
    var errorLog: String? //错误日志

    constructor(errCode: Int, msg: String?, errorLog: String? = "") : super(msg) {
        this.msg = msg ?: "请求失败，请稍后再试"
        this.status = errCode
        this.errorLog = errorLog ?: this.msg
        Log.e("appException", this.toString())
    }


    constructor(error: String, e: Throwable?) {
        if (e is AppException) {
            status = e.status
        }
        msg = error
        errorLog = e?.message
        Log.e("appException", this.toString() + "" + e?.printStackTrace())
    }

    override fun toString(): String {
        return "AppException(msg='$msg', status=$status, errorLog=$errorLog)"
    }


}