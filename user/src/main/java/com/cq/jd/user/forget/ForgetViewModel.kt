package com.cq.jd.user.forget

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.RegexUtils
import com.common.library.liveData.BooleanLiveData
import com.common.library.liveData.IntLiveData
import com.common.library.liveData.StringLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.user.net.UserNetApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by *** on 2022/5/30 16:32
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class ForgetViewModel(application: Application) : BaseViewModel(application) {


    val pwd = StringLiveData()

    val newPwd = StringLiveData()


    val phone = StringLiveData()

    val code = StringLiveData()


    val codeTime = IntLiveData()

    val resetSuccess = BooleanLiveData()

    fun bind() {

        if (pwd.value.isEmpty()) {
            hintMsg.value = "请输入密码"
            return
        }
        if (pwd.value != newPwd.value) {
            hintMsg.value = "两次密码输入不一致"
            return
        }

        if (!RegexUtils.isMobileExact(phone.value)) {
            hintMsg.value = "请输入正确手机号码"
            return
        }
        if (code.value.isEmpty()) {
            hintMsg.value = "请输入验证码"
            return
        }

        val params = HashMap<String, Any>()
        params["phone"] = phone.value
        params["password"] = pwd.value
        params["repassword"] = newPwd.value
        params["code"] = code.value

        requestRs({
            UserNetApi.service.forgetPassword(params)
        }, {
            resetSuccess.value = true
        }, loadingMessage = "重置中...")

    }


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