package com.cq.jd.share

import android.content.Context
import android.text.TextUtils
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by BOBOZHU on 2022/6/16 17:55
 * Supporte By BOBOZHU
 */
class WxApiWrapper {

    private lateinit var mApi: IWXAPI


    companion object {
        val sInstance: WxApiWrapper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WxApiWrapper()
        }
    }


    fun setAppID(context: Context, appID: String) {
        mApi = WXAPIFactory.createWXAPI(context, appID,false)
        mApi.registerApp(appID)
    }


    fun getWxApi(): IWXAPI {
        return mApi
    }
}