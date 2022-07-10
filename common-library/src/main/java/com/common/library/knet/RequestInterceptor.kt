package com.common.library.knet

import com.alibaba.android.arouter.launcher.ARouter
import com.common.library.router.ARouterPath
import com.common.library.router.provider.UserService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Created by *** on 2021/3/30 2:56 PM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class RequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val userService = ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
            .navigation() as UserService
        userService.userInfo?.token?.let {
            builder.addHeader("token", it);
            builder.addHeader("Access-Token", it);
        }
        return chain.proceed(builder.build())
    }
}