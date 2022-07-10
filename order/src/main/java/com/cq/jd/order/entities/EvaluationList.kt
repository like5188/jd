package com.cq.jd.order.entities

data class EvaluationList(
    val content: String,
    val create_at: String,
    val deleted: Int,
    val deleted_at: String,
    val goods_id: Int,
    val id: Int,
    val identity: Int,
    val is_reply: Int,
    val merchant: MerchantBean,
    val merchant_id: Int,
    val order_no: String,
    val reply_id: Int,
    val scoring: String,
    val scoring_1: Int,
    val scoring_2: Int,
    val scoring_3: Int,
    val sort: Int,
    val status: Int,
    val sub: Any,
    val user: User,
    val user_id: Long
)


data class User(
    val headimg: String,
    val id: Long,
    val level: Int,
    val name: String,
    val new_user: Int,
    val parentsid: Int,
    val nickname: String,
    val phone: String,
    val status: String,
    val unionid: String,
    val wechat_app_openid: String,
    val wechat_applets_openid: String,
    val wechat_openid: String
)