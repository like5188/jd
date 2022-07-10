package com.cq.jd.user.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.RegexUtils
import com.common.library.bean.UserInfoBean
import com.common.library.liveData.BooleanLiveData
import com.common.library.liveData.IntLiveData
import com.common.library.liveData.StringLiveData
import com.common.library.ui.requestRs
import com.common.library.util.wx.Constants
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.user.net.UserNetApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by *** on 2022/5/30 15:23
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class LoinViewModel(application: Application) : BaseViewModel(application) {

    val phone = StringLiveData()

    val code = StringLiveData()

    val inviteCode = StringLiveData()

    val codeTime = IntLiveData()

    val check = BooleanLiveData()

    val loginInfoBean = MutableLiveData<UserInfoBean>()


    fun sendCode() {
        if (!RegexUtils.isMobileExact(phone.value)) {
            hintMsg.value = "请输入正确手机号码"
            return
        }
        val params = HashMap<String, Any>()
        params["phone"] = phone.value

        requestRs({
            UserNetApi.service.verificationCode(params)
        }, {
            startTime()
        }, showErrorToast = true, loadingMessage = "发送中...")
    }

    fun login() {

        if (!RegexUtils.isMobileExact(phone.value)) {
            hintMsg.value = "请输入正确手机号码"
            return
        }
        if (code.value.isEmpty()) {
            hintMsg.value = "请输入验证码"
            return
        }
        if (check.value == false) {
            hintMsg.value = "请同意用户隐私协议"
            return
        }

        val params = HashMap<String, Any>()
        params["username"] = phone.value
        params["code"] = code.value
        params["introducer"] = inviteCode.value

        requestRs({
            UserNetApi.service.adminLogin(params)
        }, {
            it.is_register = 1
            loginInfoBean.value = it
        }, loadingMessage = "登录中...")
    }

    /**
     * 是否注册过
     */
    val isRegister = BooleanLiveData()

    fun isRegister() {

        if (!RegexUtils.isMobileExact(phone.value)) {
            isRegister.value = false
            return
        }

        val params = HashMap<String, Any>()
        params["username"] = phone.value
        requestRs({
            UserNetApi.service.isRegister(params)
        }, {
            isRegister.value = it.is_register == 1
        })

    }

    fun checkAgreement() {
        check.value = !check.value!!
    }

    fun wxLogin(wxCode: String) {
        val dtas = HashMap<String, Any>()
        dtas["appid"] = Constants.WX_APP_ID
//        dtas["secret"] = Constants.WX_APP_SCERET
        dtas["code"] = wxCode
        dtas["grant_type"] = "authorization_code"

        requestRs({
            UserNetApi.service.wxLogin(dtas)
        }, result = loginInfoBean, loadingMessage = "登录中...")
    }


    private var timeJob: Job? = null
    fun startTime() {
        timeJob = viewModelScope.launch {   // launch coroutine in UI context
            for (i in 60 downTo 0) { // countdown from 10 to 1
                codeTime.value = i
                delay(1000) // wait half a second
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timeJob?.cancel()
    }
}