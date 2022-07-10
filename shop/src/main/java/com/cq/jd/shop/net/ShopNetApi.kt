package com.cq.jd.shop.net

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
 class ShopNetApi: NetApi(){
    companion object {

        private val INSTANCE: ShopNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ShopNetApi()
        }


        val service: ShopApiService by lazy {
            INSTANCE.getApi(ShopApiService::class.java, UrlHelper.getBaseUrl())
        }
    }
 }