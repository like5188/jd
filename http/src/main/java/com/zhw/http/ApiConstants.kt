package com.zhw.http


//var IS_DEV = BuildConfig.DEBUG

//const val APP_BASE_URL = "https://dmall.qufeiya.cn/"

//const val APP_DEVELOPER_URL = "https://dmall.qufeiya.cn/"
//const val APP_DEVELOPER_URL = "http://dmall.dev.qufeiya.cn/"

//val BASE_URL = if (IS_DEV) BuildConfig.APP_DEVELOPER_URL else BuildConfig.APP_BASE_URL

//val BASE_URL = "http://dmall.dev.qufeiya.cn/"
//val BASE_URL_WEB = UrlHelper.getBaseUrl()
//val BASE_URL = "http://app.sierdian.cn/"

object UrlHelper {

    private var baseUrl: String = "http://v3.02347.net/"

    fun getBaseUrl(): String {

        return baseUrl
    }

    fun setBaseUrl(baseUrl:String){
        this.baseUrl=baseUrl
    }

}


