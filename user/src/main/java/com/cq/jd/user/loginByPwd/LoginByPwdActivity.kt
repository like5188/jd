package com.cq.jd.user.loginByPwd

import android.os.Bundle
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.JumpUtil
import com.cq.jd.user.R
import com.cq.jd.user.databinding.UserActivityBillLoginBinding
import com.cq.jd.user.forget.ForgetPwdActivity

class LoginByPwdActivity :
    BaseVmActivity<PwdViewModel, UserActivityBillLoginBinding>(R.layout.user_activity_bill_login) {

    override fun statusBarDarkFont(): Boolean = true

//    override fun onBtnTextAction() {
//        super.onBtnTextAction()
//        startActivity(Intent(this, ForgetPwdActivity::class.java))
//    }


//    override fun initView() {
//
//    }

//    override fun initListener() {
//        super.initListener()
//        mViewBinding.apply {
//            tvLogin.setOnClickListener {
//                val phone = etPhone.text.toString()
//                if (TextUtils.isEmpty(phone)){
//                    ToastUtils.showShort("请输入手机号")
//                    return@setOnClickListener
//                }
//                val code = etCode.text.toString()
//                if (TextUtils.isEmpty(code)){
//                    ToastUtils.showShort("请输入密码")
//                    return@setOnClickListener
//                }
//                mPresenter.adminLogin(phone,code)
//            }
//        }
//    }


    //    override fun onErr(i: Int, errorMsg: String) {
//        dismissLoadingDialog()
//        if (!TextUtils.isDigitsOnly(errorMsg)) {
//            ToastUtils.showShort(errorMsg)
//        }
//    }
//
//    override fun LoginSuccess(loginBean: LonginInfoBean) {
//        dismissLoadingDialog()
//        setResult(RESULT_OK)
//        JumpUtil.goToMain()
//        finish()
//    }
    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        setTitleText("账密登录")
        setRightText("忘记密码") {
            doIntent(ForgetPwdActivity::class.java)
        }

    }

    override fun loadData() {
    }

    override fun createObserver() {
        mViewModel.loginSuccess.observe(this) {
            JumpUtil.goToMain()
            finish()
        }
    }


}