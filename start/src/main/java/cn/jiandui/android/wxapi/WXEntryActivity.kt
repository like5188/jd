package cn.jiandui.android.wxapi

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.common.library.event.WxBackEvent
import com.common.library.util.wx.Constants
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus

class WXEntryActivity : Activity(), IWXAPIEventHandler {

    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true)

        try {
            val intent = intent
            api!!.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onResp(resp: BaseResp?) {
        Log.e("shit", "onResp: " + resp?.errCode)
        if (resp?.errCode == BaseResp.ErrCode.ERR_OK) {
//            Log.e("shit", "onResp: " + resp.openId)
            if (resp is SendAuth.Resp) {
                val re = resp as SendAuth.Resp
                if (re.state == "jdth_app_wechat") {
                    EventBus.getDefault().post(WxBackEvent(re.code, false))
                }
            }
        } else {
            if (resp is SendAuth.Resp) {
                if (resp.state == "jdth_app_wechat") {
                    EventBus.getDefault().post(WxBackEvent("", true))
                }
            }
        }
        finish()

    }

    override fun onReq(p0: BaseReq?) {
        Log.e("shit", "onReq: " + p0?.openId)
    }


}