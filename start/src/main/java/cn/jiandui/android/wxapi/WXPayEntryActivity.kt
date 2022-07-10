package cn.jiandui.android.wxapi

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.common.library.event.PayEvent
import com.common.library.util.wx.Constants
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus

class WXPayEntryActivity : Activity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID)
        api?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {}
    override fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            runOnUiThread {
                val errCode = resp.errCode
                Log.e("shit", "onResp: $errCode")
                if (errCode == -1) { /*支付失败*/
                    Toast.makeText(this, "支付失败", Toast.LENGTH_LONG)
                        .show()
                    EventBus.getDefault().post(PayEvent(1))
                } else if (errCode == 0) { /*支付成功*/
                    ToastUtils.showShort("支付成功")
                    EventBus.getDefault().post(PayEvent(0))
                } else if (errCode == -2) { /*取消支付*/
                    Toast.makeText(this, "取消支付", Toast.LENGTH_LONG)
                        .show()
                    EventBus.getDefault().post(PayEvent(1))
                } else {
                    EventBus.getDefault().post(PayEvent(1))
                }
            }
        } else {
            Log.e("shit", "onResp: **********")
            ToastUtils.showShort("支付失败 ")
            EventBus.getDefault().post(PayEvent(1))
        }
        finish()
    }
}