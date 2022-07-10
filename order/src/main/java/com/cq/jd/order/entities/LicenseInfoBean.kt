package com.cq.jd.order.entities

data class LicenseInfoBean(
    val create_at: String,
    val id: Int,
    val license_at: String,
    val license_name: String,
    val license_number: String,
    val license_pic: String,
    val license_reason: String,
    val merchant_id: Int,
    val status: Int,
    val update_at: String
)