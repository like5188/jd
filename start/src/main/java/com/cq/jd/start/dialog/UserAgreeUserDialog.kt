package com.cq.jd.start.dialog

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.common.library.dialog.BaseDialog
import com.common.library.net.ApiConstant
import com.common.library.util.JumpUtil
import com.common.library.util.TextUtil
import com.cq.jd.start.R
import com.luck.picture.lib.utils.DoubleUtils

/**
 * 用户协议
 */
class UserAgreeUserDialog(context: Context) : BaseDialog(context) {

    override fun layoutResId() = R.layout.start_dialog_private_agreement

    override fun initView(context: Context?) {

        val tvContent=findViewById<TextView>(R.id.tvContent)
        val message =getContext().getString(R.string.agreement_hint)
        val privacy = getContext().getString(R.string.privacy)
        val userAgreeMent =getContext().getString(R.string.user_agreement)
        val indexOf = message.indexOf(privacy)
        val indexOf2 = message.indexOf(userAgreeMent)
        tvContent.setMovementMethod(LinkMovementMethod.getInstance())
        val spannableString: SpannableString = TextUtil.setSpsColor(message,
            getContext().resources.getColor(R.color.colorPrimary),
            indexOf,
            indexOf + privacy.length)
        val url1: SpannableString = TextUtil.setClickText(spannableString,
            indexOf,
            indexOf + privacy.length,
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    if (DoubleUtils.isFastDoubleClick()) {
                        return
                    }
//                    val bundle = Bundle()
//                    bundle.putString("url", ApiConstantsKt.getUSER_AGREEMENT())
//                    doIntent(ARouterPath.App.H5, bundle)
                    JumpUtil.goToWeb(ApiConstant.AgreeUrl)
                }
            })
        val url2: SpannableString =
            TextUtil.setClickText(url1, indexOf2, indexOf2 + userAgreeMent.length, object : ClickableSpan() {
                override fun onClick(widget: View) {
                    if (DoubleUtils.isFastDoubleClick()) {
                        return
                    }
                    JumpUtil.goToWeb(ApiConstant.privacyAgreement)
//                    val bundle = Bundle()
//                    bundle.putString("url", ApiConstantsKt.getPRIVACY())
//                    doIntent(ARouterPath.App.H5, bundle)
                }
            })

        tvContent.text = url2
    }

    override fun initListener() {

        findViewById<View>(R.id.tvCancel).setOnClickListener { //取消
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(0)
                dismiss()
            }
        }
        findViewById<View>(R.id.tvNow).setOnClickListener { //确定
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(1)
                dismiss()
            }
        }
    }
}