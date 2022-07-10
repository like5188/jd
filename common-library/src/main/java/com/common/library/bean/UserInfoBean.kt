package com.common.library.bean

/**
 * Created by *** on 2022/5/30 11:21
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
data class UserInfoBean(
    val authorize: List<String>,
    val headimg: String,
    val id: String,
    val is_merchant: Int,
    var is_register: Int,
    val merchant_id: Int,
    val nickname: String,
    val password: String,
    val status: Int,
    val token: String,
    val username: String,
    val wechat_openid: String,
    val wechat_unionid: String
)