package com.cq.jd.order.dialog

import android.content.Context
import android.widget.TextView
import com.common.library.dialog.BaseDialog
import com.cq.jd.order.R
import com.cq.jd.order.widget.VerificationCodeView

class DialogInputPwd(context: Context) : BaseDialog(context) {

    private var tvSinglePrice: TextView? = null
    private var verificationcodeview: VerificationCodeView? = null


    var onConfirmPwdListener: OnConfirmPwdListener? = null

     fun setOnConfirmListener(onConfirmPwdListener: OnConfirmPwdListener?) {
        this.onConfirmPwdListener = onConfirmPwdListener
    }

    override fun layoutResId() = R.layout.order_dialog_input_pwd


    override fun cancelAble() = true

    override fun initView(context: Context?) {
        verificationcodeview = findViewById(R.id.verificationcodeview)
        tvSinglePrice = findViewById(R.id.tvSinglePrice)
    }

    override fun initListener() {
        //onConfirmListener 事件说明  1 立即购买
        verificationcodeview?.setOnCodeFinishListener {
            if (onConfirmPwdListener != null) {
                onConfirmPwdListener?.onConfirm(it)
            }
            dismiss()
        }

    }

    fun setTypeData(mData: String) {
        tvSinglePrice?.text = mData
    }


    interface OnConfirmPwdListener {
        fun onConfirm(type: String) //1 支付宝 2 微信
    }

}