package com.common.library.knet

import android.util.Log.VERBOSE
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.zhw.http.BaseNetApi
import com.zhw.http.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by *** on 2021/3/10 11:26 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
open class NetApi : BaseNetApi() {

    // 连接时长
    private val DEFAULT_CONNECT_TIMEOUT_MILLIS = 20 * 1000

    // 写入时长
    private val DEFAULT_WIRTE_TIMEOUT_MILLS = 20 * 1000

    // 读取时长
    private val DEFAULT_READ_TIMEOUOT_MILLS = 20 * 1000


    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {

        var loggingInterceptor = LoggingInterceptor.Builder().apply {
            addHeader("versionName", AppUtils.getAppVersionName())
            addHeader("versionCode", AppUtils.getAppVersionCode().toString())
            addHeader("XX-Device-Type", "Android")
            addHeader("model", DeviceUtils.getModel())
            addHeader("manufacturer", DeviceUtils.getManufacturer())
            log(VERBOSE)
            if (!BuildConfig.DEBUG) {
                setLevel(Level.NONE)
            } else {
                setLevel(Level.BASIC)
            }
        }.build()


        builder.apply {
            connectTimeout(
                DEFAULT_CONNECT_TIMEOUT_MILLIS.toLong(),
                TimeUnit.MILLISECONDS
            )
            writeTimeout(DEFAULT_WIRTE_TIMEOUT_MILLS.toLong(), TimeUnit.MILLISECONDS)
            readTimeout(DEFAULT_READ_TIMEOUOT_MILLS.toLong(), TimeUnit.MILLISECONDS)
            //添加重组数据
            addInterceptor(RequestInterceptor())
            // 打印网络信息
            addInterceptor(loggingInterceptor)
            // cookie持久化
//          .proxy(Proxy.NO_PROXY)
        }
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {

        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
//            addConverterFactory(MyConverterFactory.create(GsonBuilder().create()))下个项目采用自定义converter
            //如果使用了携程需要配置
//            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }
}