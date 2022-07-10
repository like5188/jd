package com.cq.jd.order.entities

import java.io.Serializable

data class ClsGoodsBean(
    var classify: List<ShopGoodsClassify>,
    val cost_price: String,
    var cover: String,
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
    var price: String,
    val refund: String,
    val refund_immediately: Int,
    val remark: String,
    val sales_promotion: Int,
//    val sales_promotion_at: String,
    val sales_promotion_price: String,
    val slider: List<String>,
    val sort: Int,
    var spec_attribute: List<GoodsSpecAttribute>,
    val status: Int,
    val stock_sales: Int,
    val stock_virtual: Int,
    val title: String
) {
    constructor() : this(
        emptyList(), "","","","","",0,
        "","",0,0,0,0,0,"",
        "","","",0,"",0,"",
        emptyList(),0, emptyList(),0,0,0,""
    ) {

    }
}


data class GoodsSpecAttribute(
    val `data`: List<GoodsSpecAttributeData>,
    val spec: String
): Serializable

data class GoodsSpecAttributeData(
    val attribute_pic: String,
    val attribute_price: String,
    val create_at: String,
    val deleted: Int,
    var choose: Int,
    val deleted_at: String,
    val goods_stock: Int,
    val id: Int,
    val merchant_id: Int,
    val spec_attribute: String,
    val spec_name: String,
    val status: Int
):Serializable



data class ShareBean(
    val desc: String,
    val imgUrl: String,
    val link: String,
    val title: String
):Serializable