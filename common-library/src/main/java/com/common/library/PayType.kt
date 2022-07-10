package com.common.library

/**
 * Created by BOBOZHU on 2022/6/16 15:45
 * Supporte By BOBOZHU
 */
enum class PayType(type: String) {
    AliPAY("alipay"),//
    WX("wechat"),
    BALANCE("balance"),
    BANK("unionPay"),
    SCORE("score")
}