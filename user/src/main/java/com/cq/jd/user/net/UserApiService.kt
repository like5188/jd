package com.cq.jd.user.net

import com.common.library.bean.UserInfoBean
import com.cq.jd.user.bean.LonginCheck
import com.zhw.http.ApiResponse
import retrofit2.http.*

/**
 * Created by *** on 2021/3/16 9:43 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
interface UserApiService {


    /**
     * 注册
     */
    @POST("/login/mall/wechatRegister")
    @FormUrlEncoded
    suspend fun register(@FieldMap map: Map<String, Any>): ApiResponse<Any>

    /**
     * 验证登录
     * 登录方式
    账号密码登录
    免密登录
    免密登录（username、code）参数
    账号密码登录（username、password）参数
     */
    @POST("/login/mall/handleCheck")
    @FormUrlEncoded
    suspend fun adminLogin(@FieldMap map: HashMap<String, Any>): ApiResponse<UserInfoBean> //登录


    @POST("/s_public/sms/verificationCode")
    @FormUrlEncoded
    suspend fun verificationCode(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //验证码


    @POST("/login/mall/isRegister")
    @FormUrlEncoded
    suspend fun isRegister(@FieldMap map: HashMap<String, Any>): ApiResponse<LonginCheck>//验证账号是否注册


    @GET("/login/mall/wechatCode/")
    suspend fun wxLogin(@QueryMap map: HashMap<String, Any>): ApiResponse<UserInfoBean>  //用户登录


    @POST("/login/mall/wechatRegister")
    @FormUrlEncoded
    suspend fun wechatRegister(@FieldMap map: HashMap<String, Any>): ApiResponse<UserInfoBean>  //微信绑定手机号


    @POST("/login/mall/forgetPassword")
    @FormUrlEncoded
    suspend fun forgetPassword(@FieldMap map: HashMap<String, Any>): ApiResponse<Any>  //忘记密码

}