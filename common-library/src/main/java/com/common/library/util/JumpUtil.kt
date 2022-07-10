package com.common.library.util

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.common.library.BuildConfig
import com.common.library.router.ARouterPath

/**
 * Created by *** on 2022/5/26 17:13
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
object JumpUtil {
    fun goToMain() {
        if (BuildConfig.DEBUG) {
            ARouter.getInstance().build(ARouterPath.Start.CENTER_TRANSFER).navigation()
        } else {
            ARouter.getInstance().build(ARouterPath.App.MainActivityPath).navigation()
        }
    }

    fun goToShop() {
        if (BuildConfig.DEBUG) {
            ARouter.getInstance().build(ARouterPath.Start.CENTER_TRANSFER).navigation()
        } else {
            ARouter.getInstance().build(ARouterPath.Start.shop).navigation()
        }
    }

    fun goToWeb(url: String?) {
        url?.let {
            ARouter.getInstance().build(ARouterPath.Start.H5).with(
                Bundle().apply { putString("url", it) }
            ).navigation()
        }
    }
}