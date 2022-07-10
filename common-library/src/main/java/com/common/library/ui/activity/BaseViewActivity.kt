package com.common.library.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.common.library.R
import com.common.library.base.AppBaseActivity
import com.common.library.viewModel.BaseViewModel
import com.gyf.immersionbar.ImmersionBar

/**
 * Created by *** on 2021/3/2 2:38 PM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
abstract class BaseViewActivity<DB : ViewDataBinding>(layoutId: Int) : AppBaseActivity() {

//    val appViewModel: AppViewModel by lazy {
//        getAppViewModel<AppViewModel>()
//    }
//
//    val eventViewModel: EventViewModel by lazy {
//        getAppViewModel<EventViewModel>()
//    }

    protected val mDataBinding: DB by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView(this, layoutId)
    }

    init {
        addOnContextAvailableListener {
            mDataBinding.lifecycleOwner = this
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)

        initStatusBar()
        initActionBar()
        initWidget(savedInstanceState)
        loadData()
        createObserver()

        LogUtils.e("activity=====onCreate===" + javaClass.name + "=============")
//        if (invalidLive()) {
//            eventViewModel.tokenInvalid.observe(this) {
//                LogUtils.e("activity=====onCreate===" + javaClass.name + "========tokenInvalid=====" + it)
//                if (it) {
//                    showToast("登录失效")
//                    val userService =
//                        ARouter.getInstance()
//                            .build(ARouterPath.User.USER_INFO_SERVICE).navigation() as UserService
//
//                    userService.logOut()
//                }
//            }
//        }

        getPageViewModel()?.let { viewModel ->
            viewModel.showAppLoading.observe(this) {
                showLoading(it)
            }
            viewModel.hideAppLoading.observe(this) {
                hiddenLoading()
            }
            viewModel.hintMsg.observe(this) {
                if (!it.isNullOrEmpty()) {
                    ToastUtils.showShort(it)
                }
            }
        }
    }

    open fun initStatusBar() {
        ImmersionBar.with(this).apply {
            statusBarDarkFont(statusBarDarkFont())
            navigationBarColor(R.color.white)
        }.init()

        getTitleBar()?.let {
            ImmersionBar.setTitleBar(this,it)
        }
    }

    open fun statusBarDarkFont() = false

    open fun initActionBar() {
        setBackView()
    }


    abstract fun getPageViewModel(): BaseViewModel?

    /**
     * token失效功能 是否具备 true 具备 false不具备
     */
    open fun invalidLive(): Boolean = true

    open fun createObserver() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.unbind()
    }
}