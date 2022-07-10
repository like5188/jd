package com.cq.jd.user.bean


data class WXUserBean(
    val city: String,
    val country: String,
    val headimgurl: String,
    val nickname: String,
    val openid: String,
    val privilege: List<String>,
    val province: String,
    val sex: Int,
    val unionid: String,
)


data class LoginVersion(
    val guge: Int,
    val huawei: Int,
    val meizu: Int,
    val oppo: Int,
    val vivo: Int,
    val xiaomi: Int,
    val yingyongbao: Int,
)

//
//data class LonginInfoBean(
//    val authorize: List<String>,
//    val headimg: String,
//    val id: Int,
//    val is_merchant: Int,
//    val merchant_id: Int,
//    val nickname: String,
//    val introducer: String,
//    val status: Int,
//    val token: String,
//    val username: String,
//    val wechat_openid: String,
//    val wechat_unionid: String,
//)


data class LonginCheck(
    val is_register: Int
)




