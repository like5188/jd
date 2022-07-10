package com.cq.jd.order.entities

data class CouponWaitUseBean(
    val coupon: Coupon,
    val coupon_id: Int,
    val create_at: String,
    val goods_id: Int,
    var orderType: Int,
    val id: Int,
    val merchant_id: Int,
    val order_amount: String,
    val order_no: String,
    val preferential_amount: String,
    val status: Int,
    val user_id: Long
)

data class Coupon(
    val available_quantity: Int,
//    val classify: String,
    val content: String,
    val create_at: String,
    val deleted: Int,
    val deleted_at: String,
    val end_at: String,
    val icon: String,
    val id: Int,
    val indexes: String,
    val receive_quantity: Int,
    val reduce: Int,
    val sort: Int,
    val start_at: String,
    val status: Int,
    val threshold: Int,
    val title: String
)