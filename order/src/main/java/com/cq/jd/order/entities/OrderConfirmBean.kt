package com.cq.jd.order.entities

data class OrderConfirmBean(
    val list: List<OrderConfirmItem>,
    val merchant: Merchant
)

data class OrderConfirmItem(
    val create_at: String,
    val goods_id: Int,
    val id: Int,
    val join_cost_price: String,
    val join_price: String,
    val join_quantity: Int,
    val merchant_id: Int,
    val price_pay: String,
    val sales_promotion: Int,
    val sales_promotion_price: String,
    val sort: Int,
    val spec_attribute: String,
    val spec_attribute_price: String,
    val status: Int,
    val goods: Goods,
    val user_id: Long,
    val spec_attribute_string: String
)

data class DistanceInfo(
    val distance: Int,
    val id: Int,
    val lat: String,
    val lng: String
)

data class OrderPayInfo(
    val order_no: String,
    val price_pay: String
)


