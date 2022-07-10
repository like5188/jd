package com.cq.jd.user.wxLogin

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.common.library.dialog.BaseCenterHintDialog
import com.common.library.router.ARouterPath
import com.common.library.router.provider.UserService
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.JumpUtil
import com.cq.jd.user.R
import com.cq.jd.user.databinding.UserActivityWxBindBinding
import com.lxj.xpopup.XPopup

@Route(path = ARouterPath.User.WX_BIND_PHONE)
class WxBindActivity :
    BaseVmActivity<WxBindViewModel, UserActivityWxBindBinding>(R.layout.user_activity_wx_bind) {

    override fun statusBarDarkFont(): Boolean = true
    //    private val mViewBinding by lazy {
//        UserActivityWxBindBinding.inflate(layoutInflater)
//    }
//
//    override fun getLayoutView(): View = mViewBinding.root
//
//    private val timer by lazy {
//        SmsCountDownTimer(mViewBinding.tvCode, "", 60 * 1000L, 1000)
//    }
//
//    override fun getLayoutResId() = R.layout.user_activity_wx_bind
//
//    override fun title() = "绑定手机号码"
//
//    override fun createPresenter() = WxBindPresewnter()
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
//                if (TextUtils.isEmpty(phone)) {
//                    ToastUtils.showShort("请输入手机号")
//                    return@setOnClickListener
//                }
//                showLoadingDialog()
//                mPresenter.verificationCode(phone)
//            }
//            tvLogin.setOnClickListener {
//                val phone = etPhone.text.toString()
//                if (TextUtils.isEmpty(phone)) {
//                    ToastUtils.showShort("请输入手机号")
//                    return@setOnClickListener
//                }
//                val code = etCode.text.toString()
//                if (TextUtils.isEmpty(code)) {
//                    ToastUtils.showShort("请输入验证码")
//                    return@setOnClickListener
//                }
////                val etInvite = etInvite.text.toString()
//                showLoadingDialog()
//                mPresenter.wechatRegister(phone, "", code)
//            }
//        }
//    }
//
//    override fun onSmsSuccess() {
//        dismissLoadingDialog()
//        timer.start()
//    }
//
//    override fun onErr(code: Int, errorMsg: String) {
//        dismissLoadingDialog()
//        if (!TextUtils.isDigitsOnly(errorMsg)) {
//            ToastUtils.showShort(errorMsg)
//        }
//    }
//
//    override fun onWxLoginSuccess(data: UserInfoBean) {
//        JumpUtil.goToMain()
//        setResult(RESULT_OK)
//        finish()
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        timer.cancel()
//    }
    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        setTitleText("绑定手机号码")
    }

    override fun loadData() {

    }


    override fun onBackPressed() {
        XPopup.Builder(this).asCustom(BaseCenterHintDialog(this, "是否切换其他账号？") {
            val userService = ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
                .navigation() as UserService
            userService.logOut()
            this.finish()
        }).show()
    }

    override fun createObserver() {
        mViewModel.loginInfoBean.observe(this) {
            val userService = ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
                .navigation() as UserService
            userService.saveUserInfo(it)
            JumpUtil.goToMain()
            setResult(RESULT_OK)
            finish()
        }
    }


}