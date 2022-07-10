package com.cq.jd.user.forget

import android.os.Bundle
import com.common.library.ui.activity.BaseVmActivity
import com.cq.jd.user.R
import com.cq.jd.user.databinding.UserActivityForgetPwdBinding

class ForgetPwdActivity :
    BaseVmActivity<ForgetViewModel, UserActivityForgetPwdBinding>(R.layout.user_activity_forget_pwd) {

    override fun statusBarDarkFont(): Boolean = true
    //    private val mViewBinding by lazy {
//        UserActivityForgetPwdBinding.inflate(layoutInflater)
//    }
//
//    override fun getLayoutView(): View =mViewBinding.root
//
//    override fun getLayoutResId() = R.layout.user_activity_forget_pwd
//
//    override fun title() = "账密登录"
//
//    private val timer by lazy {
//        SmsCountDownTimer(mViewBinding.tvCode, "", 60 * 1000L, 1000)
//    }
//
//    override fun initView() {
//
//    }
//
//    override fun initListener() {
//        super.initListener()
//        mViewBinding.apply {
//            tvCode.setOnClickListener {
//                val phone = etPhone.text.toString()
//                if (TextUtils.isEmpty(phone)){
//                    ToastUtils.showShort("请输入手机号")
//                    return@setOnClickListener
//                }
//                showLoadingDialog()
//                mPresenter.verificationCode(phone)
//            }
//            tvLogin.setOnClickListener {
//                val pwd = etPwd.text.toString()
//                if (TextUtils.isEmpty(pwd)) {
//                    ToastUtils.showShort("请输入密码")
//                    return@setOnClickListener
//                }
//                val pwdA = etPwdA.text.toString()
//                if (TextUtils.isEmpty(pwdA)) {
//                    ToastUtils.showShort("请再次输入密码")
//                    return@setOnClickListener
//                }
//                val phone = etPhone.text.toString()
//                if (TextUtils.isEmpty(phone)){
//                    ToastUtils.showShort("请输入手机号")
//                    return@setOnClickListener
//                }
//                val code = etCode.text.toString()
//                if (TextUtils.isEmpty(code)){
//                    ToastUtils.showShort("请输入验证码")
//                    return@setOnClickListener
//                }
//
//                showLoadingDialog()
//                mPresenter.forgetPassword(phone,pwd,pwdA,code)
//            }
//        }
//    }
//
//    override fun createPresenter() = FotgetPwdPresewnter()
//
//    override fun onSmsSuccess() {
//        dismissLoadingDialog()
//        timer.start()
//    }
//
//    override fun onErr(i: Int, errorMsg: String) {
//        dismissLoadingDialog()
//        if (!TextUtils.isDigitsOnly(errorMsg)){
//            ToastUtils.showShort(errorMsg)
//        }
//    }
//
//
//    override fun alterSuccess() {
//        ToastUtils.showShort("密码修改成功")
//        finish()
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        timer.cancel()
//    }
    override fun initWidget(savedInstanceState: Bundle?) {
        setTitleText("忘记密码")
        mDataBinding.model=mViewModel
    }

    override fun loadData() {
    }

    override fun createObserver() {
        mViewModel.resetSuccess.observe(this
        ) {
            showToast("密码修改成功")
            finish()
        }
    }

}