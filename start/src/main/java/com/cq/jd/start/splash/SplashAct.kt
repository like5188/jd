package com.cq.jd.start.splash

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.common.library.router.ARouterPath
import com.common.library.router.provider.UserService
import com.common.library.ui.activity.BaseViewActivity
import com.common.library.util.JumpUtil
import com.common.library.util.sp.SpConstant
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.start.R
import com.cq.jd.start.databinding.StartActivitySplashBinding
import com.cq.jd.start.dialog.UserAgreeUserDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * 开屏页
 */
class SplashAct : BaseViewActivity<StartActivitySplashBinding>(R.layout.start_activity_splash) {


    override fun statusBarDarkFont(): Boolean = true

    private fun nextPage() {
        next(false)
    }

    private fun next(boolean: Boolean) {
        Log.v("next", "===$boolean")

        Handler(Looper.getMainLooper()!!).postDelayed({

            val userService =
                ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
                    .navigation() as UserService
            userService?.userInfo?.let {
                if (it.is_register == 0) {
                    doIntent(ARouterPath.User.WX_BIND_PHONE)
                } else {
                        JumpUtil.goToMain()
                }
            } ?: kotlin.run {
                ARouter.getInstance().build(ARouterPath.User.LOGIN).navigation()
            }
            finish()
        }, 3000)
    }


    override fun initWidget(savedInstanceState: Bundle?) {

        if (SPUtils.getInstance().getInt(SpConstant.is_firstt_into) == 0) {
            val userAgreementDialog = UserAgreeUserDialog(this)
            userAgreementDialog.setOnConfirmListener {//是不是第一次进入 弹窗弹出
                if (it == 1) {
                    val list =
                        if (this.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.S) {//android 12
                            listOf(Permission.ACCESS_FINE_LOCATION,
                                Permission.ACCESS_COARSE_LOCATION)
                        } else {
                            listOf(
                                Permission.ACCESS_FINE_LOCATION)
                        }
                    XXPermissions.with(this) // 适配 Android 12 可以这样写
                        .permission(list)
                        .request(object : OnPermissionCallback {
                            override fun onGranted(
                                permissions: MutableList<String>?,
                                all: Boolean,
                            ) {
                                if (all) {
                                    nextPage()
                                    SPUtils.getInstance().put(SpConstant.is_firstt_into, 1)
                                } else {
                                    next(true)
                                }
                            }

                            override fun onDenied(
                                permissions: MutableList<String>?, never: Boolean,
                            ) {//权限被拒绝
                                super.onDenied(permissions, never)
                                next(true)
                            }

                        })
                } else {
                    next(true)
                }
            }
            userAgreementDialog.show()
        } else {
            nextPage()
        }
    }

    override fun loadData() {
    }


    override fun getPageViewModel(): BaseViewModel? = null


}