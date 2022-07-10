package com.cq.jd.share

import android.content.Context
import android.text.TextUtils
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.openapi.IWBAPI
import com.sina.weibo.sdk.openapi.SdkListener
import com.sina.weibo.sdk.openapi.WBAPIFactory
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.lang.Exception

/**
 * Created by BOBOZHU on 2022/6/16 17:55
 * Supporte By BOBOZHU
 */
class WeiBoApiWrapper {

    private lateinit var mApi: IWBAPI


    companion object {
        val sInstance: WeiBoApiWrapper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WeiBoApiWrapper()
        }
    }


    fun initSdk(context: Context, appID: String) {
        val authInfo = AuthInfo(context, "APP_KY", "REDIRECT_URL", "SCOPE");
        mApi = WBAPIFactory.createWBAPI(context); // 传Context即可，不再依赖于Activity
        mApi.registerApp(context, authInfo, object : SdkListener {
            override fun onInitSuccess() {

            }

            override fun onInitFailure(p0: Exception?) {
            }
        });

    }


    fun getWxApi(): IWBAPI {
        return mApi
    }
}