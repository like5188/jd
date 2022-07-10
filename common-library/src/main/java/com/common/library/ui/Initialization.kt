package com.common.library.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView

interface Initialization {

    /**
     *初始化View
     */
    abstract fun initWidget(savedInstanceState: Bundle?)

    /**
     * 加载数据
     */
    abstract fun loadData()

    /**
     * 显示加载dialog
     */
    abstract fun showLoading(message: String?)

    /**
     * 取消加载dialog
     */
    abstract fun hiddenLoading()
}