package com.cq.jd.order.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import com.common.library.dialog.BaseDialog
import com.cq.jd.order.R

class PopupGoodsMenu(context: Context) : PopupWindow(context) {

    private var onConfirmListener: BaseDialog.OnConfirmListener? = null

    fun setOnConfirmListener(onConfirmListener: BaseDialog.OnConfirmListener?) {
        this.onConfirmListener = onConfirmListener
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.order_popup_menu, null)
        val tvShare = view.findViewById<TextView>(R.id.tvShare)
        val tvTs = view.findViewById<TextView>(R.id.tvTs)
        contentView = view
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isOutsideTouchable = true

        tvShare.setOnClickListener {
            onConfirmListener?.onConfirm(1)
        }
        tvTs.setOnClickListener {
            onConfirmListener?.onConfirm(2)
        }
    }


}