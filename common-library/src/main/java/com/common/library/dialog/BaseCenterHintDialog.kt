package com.common.library.dialog

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.DataBindingUtil
import com.common.library.R
import com.common.library.databinding.BaseDialogCenterBinding
import com.lxj.xpopup.core.CenterPopupView

/**
 * Created by *** on 2022/5/30 17:27
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class BaseCenterHintDialog(
    context: Context,
    val content: String,
    val cancel: String = "取消",
    val confirm: String = "确认",
    val cancelCallBack: (() -> Unit)? = null,
    val confirmCallBack: (() -> Unit)? = null,
) : CenterPopupView(context) {


    override fun getImplLayoutId(): Int = R.layout.base_dialog_center


    override fun onCreate() {
        super.onCreate()

        val bind = DataBindingUtil.bind<BaseDialogCenterBinding>(popupImplView)

        bind?.apply {

            tvCancel.text = cancel
            tvConfirm.text = confirm

            tvCancel.setOnClickListener {
                this@BaseCenterHintDialog.dismiss()
                cancelCallBack?.invoke()
            }

            tvConfirm.setOnClickListener {
                this@BaseCenterHintDialog.dismiss()
                confirmCallBack?.invoke()
            }
            tvContent.text = content

        }
    }
}