package com.common.library

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
 class BaseNetApi: NetApi(){
    companion object {

        private val instance: BaseNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BaseNetApi()
        }


        val service: BaseNetApiService by lazy {
            instance.getApi(BaseNetApiService::class.java, UrlHelper.getBaseUrl())
        }
    }
 }