package com.cq.jd.user.net

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
 class UserNetApi: NetApi(){
    companion object {

        private val INSTANCE: UserNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserNetApi()
        }


        val service: UserApiService by lazy {
            INSTANCE.getApi(UserApiService::class.java, UrlHelper.getBaseUrl())
        }
    }
 }