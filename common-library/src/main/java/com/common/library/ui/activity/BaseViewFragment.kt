package com.common.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.blankj.utilcode.util.ToastUtils
import com.common.library.R
import com.common.library.base.AppBaseFragment
import com.common.library.viewModel.BaseViewModel
import com.gyf.immersionbar.ImmersionBar


/**
 * Created by *** on 2021/1/14
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
abstract class BaseViewFragment<DB : ViewDataBinding>(private val layoutId: Int) :
    AppBaseFragment() {
    //是否第一次加载
    private var isFirst = true
    open lateinit var mDataBinding: DB

//    val appViewModel: AppViewModel by lazy {
//        getAppViewModel<AppViewModel>()
//    }
//
//    val eventViewModel: EventViewModel by lazy {
//        getAppViewModel<EventViewModel>()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasToolBar()) {
            initStatusBar()
            setBackView()
        }
        initWidget(savedInstanceState)
        onVisible()
        loadData()
//        view?.findViewById<SwipeRefreshLayout>(R.id.smartRefresh)?.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
//        view?.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)?.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
//        view?.findViewById<SwipeRefreshLayout>(R.id.smartRefresh)?.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
    }


    open fun hasToolBar(): Boolean {
        return getTitleBar() != null || getBackView() != null
    }

    open fun initStatusBar() {
        ImmersionBar.with(this).apply {
            statusBarDarkFont(statusBarDarkFont())
        }.init()

        getTitleBar()?.let {
            ImmersionBar.setTitleBar(this, it)
        }
    }

    open fun statusBarDarkFont() = false

    /**
     * 懒加载
     */
    protected abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    protected abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        onVisible()
    }


    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            isFirst = false
            lazyLoadData()
            createObserver()
            getFragmentViewModel()?.let { mViewModel ->
                mViewModel.hintMsg.observe(viewLifecycleOwner, Observer {
                    if (!it.isNullOrEmpty()) {
                        ToastUtils.showShort(it)
                    }
                })
                mViewModel.showAppLoading.observe(viewLifecycleOwner, Observer {
                    showLoading(it)
                })
                mViewModel.hideAppLoading.observe(viewLifecycleOwner, Observer {
                    hiddenLoading()
                })
            }
        }
    }

    abstract fun getFragmentViewModel(): BaseViewModel?


    override fun getBackView(): View? {
        return view?.findViewById(R.id.tool_btn_back)
    }

    override fun getTitleView(): TextView? {
        return view?.findViewById(R.id.tool_titleView)
    }

//    fun <VM> createViewModel(key: String? = null): VM {
//        val vmClazz1: Class<VM> = getVmClazz1(this)
//        val newKey = key ?: "com.base.zhw.paging" + vmClazz1.javaClass.simpleName
//        return ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
//        ).get(newKey, vmClazz1)
//    }
}