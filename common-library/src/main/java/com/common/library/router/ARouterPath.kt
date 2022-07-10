package com.common.library.router

object ARouterPath {

    object App {

        const val MainActivityPath = "/app/home_app"
    }

    object Start {
        const val H5: String = "/start/h5"

        const val main: String = "/start/main"// 假首页
        const val shop: String = "/start/shop"//假逛街
        const val CENTER_TRANSFER: String = "/start/center_transfer"//中转站
    }

    object User {
        const val LOGIN: String = "/user/login"
        const val USER_INFO_SERVICE: String = "/user/user_info_service"
        const val WX_BIND_PHONE: String = "/user/wx_bind_phone"
    }

    object Map {
        const val SELECT_MAP = "/map/select_map"
        const val LOCATION_SERVICE = "/map/location_service"
    }

    object Order{
        const val ORDER_HOME = "/order/order_home"
    }



}