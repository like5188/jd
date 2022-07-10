package com.cq.jd.order.entities

import java.io.Serializable

data class ShopDetailBean(
    val adcode: String,
    val address: String,
    val area: String,
    val auto_order: Int,
    val average: Double,
    val city: String,
    val classify: List<ShopGoodsClassify>,
    val close: Int,
    val share: ShareBean,
    val contacts_name: String,
    val contacts_order_phone: String,
    val contacts_phone: String,
    val create_at: String,
    val deleted: Int,
    val deleted_at: String,
    val disable: Int,
    val environment_pic: List<String>,
    val evaluate_bad: Int,
    val evaluate_praise: Int,
    val distance: Long,
    val evaluate_score: Float,
    val head_pic: String,
    val id: Int,
    val is_favorites: Int,
    val lat: String,
    val lng: String,
    val logo: String,
    val notice: String,
    val operation_at: List<String>,
    val province: String,
    val reason: String,
    val reason_classify: String,
    val reason_merchant: String,
    val recommend: List<Recommend>,
    val return_goods: Int,
    val sales_volume: Int,
    val sort: Int,
    val status: Int,
    val status_classify: Int,
    val status_merchant: Int,
    val street: String,
    val title: String,
    val update_at: String,
    val weight: Int
):Serializable



data class ShopGoodsClassify(
    val background: String,
    val create_at: String,
    val icon: String,
    val id: Int,
    val pid: Int,
    val sort: Int,
    val status: Int,
    val title: String
):Serializable

data class Recommend(
    val classify: List<ShopGoodsClassify>,
    val cost_price: String,
    val cover: String,
    val create_at: String,
    val cycle_end_at: String,
    val cycle_start_at: String,
    val deleted: Int,
    val deleted_at: String,
    val end_at: String,
    val id: Int,
    val limits: Int,
    val merchant_id: Int,
    val new_product: Int,
    val num_read: Int,
    val number: String,
    val payment: String,
    val price: String,
    val refund: String,
    val refund_immediately: Int,
    val remark: String,
    val sales_promotion: Int,
//    val sales_promotion_at: String,
    val sales_promotion_price: String,
    val slider: List<String>,
    val sort: Int,
    val spec_attribute: List<GoodsSpecAttribute>,
    val status: Int,
    val stock_sales: Int,
    val stock_virtual: Int,
    val title: String
):Serializable

