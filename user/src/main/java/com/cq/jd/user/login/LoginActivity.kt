package com.cq.jd.user.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.common.library.event.WxBackEvent
import com.common.library.net.ApiConstant
import com.common.library.router.ARouterPath
import com.common.library.router.provider.UserService
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.ActivityManager
import com.common.library.util.JumpUtil
import com.common.library.util.wx.Constants
import com.cq.jd.share.WxApiWrapper
import com.cq.jd.user.R
import com.cq.jd.user.databinding.UserActivityLoginBinding
import com.cq.jd.user.loginByPwd.LoginByPwdActivity
import com.cq.jd.user.wxLogin.WxBindActivity
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = ARouterPath.User.LOGIN)
class LoginActivity :
    BaseVmActivity<LoinViewModel, UserActivityLoginBinding>(R.layout.user_activity_login) {


    private var role = 1
    private var api: IWXAPI? = null
    var isFirst = true;

    override fun statusBarDarkFont(): Boolean = true

    fun initView() {
        EventBus.getDefault().register(this)
        api = WxApiWrapper.sInstance.getWxApi()
        mDataBinding.apply {
            login10.visibility = View.VISIBLE
            login11.visibility = View.VISIBLE
            login12.visibility = View.VISIBLE
            login13.visibility = View.VISIBLE
            login13.visibility = View.VISIBLE
            llAgree1.visibility = View.VISIBLE
            cardFinish.visibility = View.VISIBLE
            llLoginInfo1.visibility = View.VISIBLE

            llLoginInfo.visibility = View.GONE
            login20.visibility = View.GONE
            login21.visibility = View.GONE
            login22.visibility = View.GONE
            login23.visibility = View.GONE
            tvLogin.visibility = View.GONE


            tvLogin1.setOnClickListener {
                llLoginInfo1.visibility = View.GONE
                cardFinish.visibility = View.GONE
                login10.visibility = View.GONE
                login11.visibility = View.GONE
                login12.visibility = View.GONE
                login13.visibility = View.GONE
                llAgree1.visibility = View.GONE

                cardFinish.visibility = View.GONE
                login20.visibility = View.VISIBLE
                login21.visibility = View.VISIBLE
                login22.visibility = View.VISIBLE
                login23.visibility = View.VISIBLE
                tvLogin.visibility = View.VISIBLE
                llLoginInfo.visibility = View.VISIBLE
            }

            tvFinish.setOnClickListener {
                JumpUtil.goToMain()
                finish()
            }
//
//            etPhone.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    if (!TextUtils.isEmpty(p0) && p0?.length == 11) {
//                        mPresenter.isRegister(p0.toString())
//                    } else {
//                        llIntroduce.visibility = View.VISIBLE
//                        viIntro.visibility = View.VISIBLE
//                    }
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                }
//
//            })
//
//            tvCode.setOnClickListener {
//                val phone = etPhone.text.toString()
//                if (TextUtils.isEmpty(phone)) {
//                    ToastUtils.showShort("请输入手机号")
//                    return@setOnClickListener
//                }
//                showLoadingDialog()
//                mPresenter.verificationCode(phone)
//            }
//
//            tvLogin.setOnClickListener {
//                val phone = etPhone.text.toString()
//                if (TextUtils.isEmpty(phone)) {
//                    ToastUtils.showShort("请输入手机号")
//                    return@setOnClickListener
//                }
////            val etInvite = etPhone.text.toString()
//                val code = etCode.text.toString()
//                if (TextUtils.isEmpty(code)) {
//                    ToastUtils.showShort("请输入验证码")
//                    return@setOnClickListener
//                }
//                if (!cbSelect.isChecked) {
//                    ToastUtils.showShort("请同意用户隐私协议")
//                    return@setOnClickListener
//                }
//                showLoadingDialog()
//                mPresenter.adminLogin(phone, "", etInvite.text.toString().trim(), code)
//            }
            tvAgreement.setOnClickListener {
                JumpUtil.goToWeb(ApiConstant.AgreeUrl)
            }
            tvAgreement1.setOnClickListener {
                JumpUtil.goToWeb(ApiConstant.AgreeUrl)
            }
//
//
            tvWx.setOnClickListener {
                if (mViewModel.check.value == false) {
                    ToastUtils.showShort("请同意用户隐私协议")
                    return@setOnClickListener
                }
                showLoading("授权中...")
                val req = SendAuth.Req()
                req.scope = "snsapi_userinfo"
                req.state = "jdth_app_wechat"
                req.extData
                api!!.sendReq(req)
            }
//
            tvBill.setOnClickListener {
                if (mViewModel.check.value == false) {
                    ToastUtils.showShort("请同意用户隐私协议")
                    return@setOnClickListener
                }
                startActivityForResult(
                    Intent(this@LoginActivity, LoginByPwdActivity::class.java),
                    1
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirst) {
            ActivityManager.getInstance().finishOthersActivity(LoginActivity::class.java)
            isFirst = false;
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        initView()
        mDataBinding.model = mViewModel
    }

    override fun loadData() {
    }

//    override fun RegisterCheckSuccess(loginBean: UserInfoBean) {
//        mViewBinding.apply {
//            if (loginBean.is_register ==1){
//                llIntroduce.visibility = View.GONE
//                viIntro.visibility = View.GONE
//            }else{
//                llIntroduce.visibility = View.VISIBLE
//                viIntro.visibility = View.VISIBLE
//            }
//        }
//    }

//    override fun onWxLoginSuccess(loginBean: UserInfoBean) {
//        dismissLoadingDialog()
//        if (loginBean.is_register == 0) {
//            startActivityForResult(Intent(this, WxBindActivity::class.java), 1)
//        } else {
//            finish()
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWxBackEvent(event: WxBackEvent) {
        if (event.cancel) {
            hiddenLoading()
        } else {
            mViewModel.wxLogin(event.wx_code)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        isFirst = true
        EventBus.getDefault().unregister(this)
    }

    override fun createObserver() {
        mViewModel.apply {
            isRegister.observe(this@LoginActivity) {
                mDataBinding.llIntroduce.visibility = if (it) View.GONE else View.VISIBLE
                mDataBinding.viIntro.visibility = if (it) View.GONE else View.VISIBLE
            }
            phone.observe(this@LoginActivity) {
                mViewModel.isRegister()
            }

            loginInfoBean.observe(this@LoginActivity) {
                val userService = ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
                    .navigation() as UserService
                userService.saveUserInfo(it)

                if (it.is_register == 0) {
                    startActivityForResult(Intent(this@LoginActivity, WxBindActivity::class.java),
                        1)
                } else {
                    JumpUtil.goToMain()
                }
            }
        }

    }

}