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
 class HistoryNetApi: NetApi(){
    companion object {

        private val INSTANCE: HistoryNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HistoryNetApi()
        }


        val service: HistoryApiService by lazy {
            INSTANCE.getApi(HistoryApiService::class.java, UrlHelper.getBaseUrl())
        }
    }
 }