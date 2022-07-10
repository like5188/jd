package com.cq.jd.order.entities

data class GoodsDetailInfo(
//    val classify: List<ShopGoodsClassify>,
    val cost_price: String,
    val cover: String,
    val create_at: String,
    val cycle_end_at: String,
    val cycle_start_at: String,
    val deleted: Int,
    val deleted_at: String,
    val share: ShareBean,
    val end_at: String,
    val evaluate_count: Int,
    val id: Int,
    val limits: Int,
    val merchant_id: Int,
    val new_product: Int,
    val num_read: Int,
    val is_favorites: Int,
    val number: String,
    val payment: String,
    val price: String,
    val refund: String,
    val refund_immediately: Int,
    val remark: String,
    val sales_promotion: Int,
//    val sales_promotion_at: Any,
    val sales_promotion_price: String,
    val slider: List<String>,
    val sort: Int,
    val spec_attribute: List<GoodsSpecAttribute>,
    val status: Int,
    val stock_sales: Int,
    val merchant: MerchantBean,
    val evaluate: List<EvaluateBean>,
    val stock_virtual: Int,
    val title: String
)

data class MerchantBean(
    val id: Int,
    val head_pic: String,
    val logo: String,
    val environment_pic: List<String>,
    val title: String
)

data class EvaluateBean(
    val content: String,
    val create_at: String,
    val deleted: Int,
    val deleted_at: String,
    val goods_id: Int,
    val id: Int,
    val identity: Int,
    val is_reply: Int,
    val merchant_id: Int,
    val order_no: String,
    val reply_id: Int,
    val scoring: String,
    val user: User,
    val scoring_1: Int,
    val scoring_2: Int,
    val scoring_3: Int,
    val sort: Int,
    val status: Int,
    val user_id: Long
)
