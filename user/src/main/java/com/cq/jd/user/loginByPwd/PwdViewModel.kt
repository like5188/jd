package com.cq.jd.user.loginByPwd

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.RegexUtils
import com.common.library.liveData.BooleanLiveData
import com.common.library.liveData.StringLiveData
import com.common.library.router.ARouterPath
import com.common.library.router.provider.UserService
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.user.net.UserNetApi

/**
 * Created by *** on 2022/5/28 17:33
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class PwdViewModel(application: Application) : BaseViewModel(application) {

    val phone = StringLiveData()

    val pwd = StringLiveData()


    val loginSuccess = BooleanLiveData()

    fun login() {

        if (!RegexUtils.isMobileExact(phone.value)) {
            hintMsg.value = "请输入手机号码"
            return
        }
        if (pwd.value.isEmpty()) {
            hintMsg.value = "请输入密码"
            return
        }

        val map = HashMap<String, Any>()
            .apply {
                put("username", phone.value)
                put("password", pwd.value)
            }
        requestRs({
            UserNetApi.service.adminLogin(map)
        }, {
            it.is_register = 1
            val userService = ARouter.getInstance()
                .build(ARouterPath.User.USER_INFO_SERVICE).navigation() as UserService
            userService.saveUserInfo(it)
            loginSuccess.value = true
        }, loadingMessage = "登录中...")
    }
}