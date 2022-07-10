package com.common.library.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gyf.immersionbar.ImmersionBar

interface IBar {

    /**
     * 设置返回按钮
     */
    abstract fun getTitleBar(): View?
    /**
     * 设置返回按钮
     */
    abstract fun getBackView(): View?

    /**
     * 返回关闭页面
     */
    abstract fun onReturn()

    /**
     *设置标题
     */
    abstract fun getTitleView(): TextView?

    /**
     *设置右边更多文字
     */
    abstract fun getRightText(): TextView?

    /**
     *设置右边更多图片
     */
    abstract fun getRightImage(): ImageView?

}