package com.cq.jd.order.dialog

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import com.common.library.bean.PayTypeBean
import com.common.library.dialog.BaseDialog
import com.common.library.widget.payview.PayTypeView
import com.cq.jd.order.R

class DialogChoosePay(context: Context) : BaseDialog(context) {

    private var tvPayOrder: TextView? = null
    private var payTypeView: PayTypeView? = null

    override fun layoutResId() = R.layout.order_dialog_choose_pay

    override fun getHeightPx() = context.resources.displayMetrics.heightPixels * 2 / 3

    override fun gravity() = Gravity.BOTTOM

    override fun cancelAble() = true

    override fun initView(context: Context?) {
        payTypeView = findViewById(R.id.payTypeView)
        tvPayOrder = findViewById(R.id.tvPayOrder)
    }

    override fun initListener() {
        //onConfirmListener 事件说明  1 立即购买
        tvPayOrder?.setOnClickListener {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(payTypeView?.selectPosition()!!)
            }
            dismiss()
        }

    }

    fun setPayTypeData(mData: List<PayTypeBean>) {
        payTypeView?.setPayTypeData(mData)
    }

}