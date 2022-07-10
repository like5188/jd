package com.cq.jd.order.net

import com.common.library.knet.NetApi
import com.zhw.http.UrlHelper

/**
 * Created by *** on 2021/3/16 9:43 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
 class OrderNetApi: NetApi(){
    companion object {

        private val INSTANCE: OrderNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OrderNetApi()
        }


        val service: OrderApiService by lazy {
            INSTANCE.getApi(OrderApiService::class.java, UrlHelper.getBaseUrl())
        }
    }
 }