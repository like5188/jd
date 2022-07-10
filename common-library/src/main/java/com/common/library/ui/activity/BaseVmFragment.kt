package com.common.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.common.library.R
import com.common.library.base.AppBaseFragment
import com.common.library.ui.FragmentLife
import com.common.library.ui.getVmClazz
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
abstract class BaseVmFragment<VM : BaseViewModel, DB : ViewDataBinding>(val layoutId:Int) : AppBaseFragment() {
    //是否第一次加载
    private var isFirst = true
    open lateinit var mViewModal: VM
    open lateinit var mViewBinding: DB

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
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = DataBindingUtil.inflate(inflater,layoutId, container, false)
        mViewBinding.lifecycleOwner = this
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModal = createViewModel()
        if (hasToolBar()) {
            initStatusBar()
            setBackView()
        }
        initWidget(savedInstanceState)
        onVisible()
        loadData()
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
     * 创建viewModel
     */
    open fun createViewModel(): VM {
        return ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
        ).get(getVmClazz(this))
    }


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
            mViewModal.hintMsg.observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()) {
                    ToastUtils.showShort(it)
                }
            })
            mViewModal.showAppLoading.observe(viewLifecycleOwner, Observer {
                showLoading(it)
            })
            mViewModal.hideAppLoading.observe(viewLifecycleOwner, Observer {
                hiddenLoading()
            })
        }
    }
}