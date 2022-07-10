package com.common.library.ui.paging

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.common.library.R
import com.common.library.databinding.BaseFragmentPagingListBinding
import com.common.library.ui.activity.BaseViewFragment
import com.common.library.ui.createViewModel
import com.common.library.ui.removeAnimation
import com.common.library.viewModel.BaseViewModel

import com.zhw.http.AppException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by *** on 2022/1/13 2:31 下午
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
abstract class BasePagingFragment<T : Any, Vm : BasePagingViewModel<T>> :
    BaseViewFragment<BaseFragmentPagingListBinding>(R.layout.base_fragment_paging_list) {

    lateinit var viewModel: Vm
    private lateinit var mAdapter: BasePagingDataAdapter<T, *>
    var isRefreshing = false;

    open var mItemClickListener: ((v: View, position: Int) -> Unit)? = null
    open var mItemLongClickListener: ((v: View, position: Int) -> Boolean)? = null
    open var mItemChildClickListener: ((v: View, position: Int) -> Unit)? = null

    override fun lazyLoadData() {
        loadRefresh()
    }

    override fun createObserver() {

    }

    override fun getFragmentViewModel(): BaseViewModel? = viewModel

    abstract fun getAdapter(): BasePagingDataAdapter<T, *>

    /**
     *  这个地方
     */
    open fun createOpenViewModel(): Vm {
        return createViewModel(getViewmodelKey())
    }

    /**
     * 主要是为了防止在ViewPager里面
     * viewModel重复使用的问题
     *
     * key为null 会被服用。应该设置不同的值
     */
    open fun getViewmodelKey(): String? = null

    open fun getManager(): RecyclerView.LayoutManager = LinearLayoutManager(activity)

    override fun initWidget(savedInstanceState: Bundle?) {
        viewModel = createOpenViewModel()
        val linearLayoutManager = getManager()
        mDataBinding.smartRefresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        mDataBinding.recyclerView.layoutManager = linearLayoutManager
        mAdapter = getAdapter()
        mItemClickListener?.let {
            mAdapter.mItemClickListener = it
        }
        mItemLongClickListener?.let {
            mAdapter.mItemLongClickListener = it
        }
        mItemChildClickListener?.let {
            mAdapter.mItemChildClickListener = it
        }
        val footerAdapter = getFooterAdapter()
        val adapter = if (footerAdapter == null) {
            mAdapter
        } else {
            val concatAdapter = ConcatAdapter(ConcatAdapter.Config.Builder().setIsolateViewTypes(
                linearLayoutManager is LinearLayoutManager).build(),
                mAdapter, footerAdapter)
            mAdapter.addLoadStateListener {
                footerAdapter.loadState = it.append
            }
            concatAdapter
        }
        mDataBinding.recyclerView.adapter = doSomeThing(adapter)
        mDataBinding.recyclerView.removeAnimation()
        mDataBinding.smartRefresh.setOnRefreshListener {
            mAdapter.refresh()
        }
        mDataBinding.stateLayout.setRetryIds(R.id.error_retry)
        mDataBinding.stateLayout.onRefresh {
            mAdapter.refresh()
        }.run {
            if (needStateView()) {
                this.showLoading()
            } else {
                this.showContent()
            }
        }
        mAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {

                }
                is LoadState.NotLoading -> {
                }
                is LoadState.Error -> {
                    val error = (it.refresh as LoadState.Error).error
                    if (error is AppException) {
//                        if (error.status == LOG_OUT_CODE) {
//                            eventViewModel.tokenInvalid.value = true
//                        }
                    } else {
                        error.printStackTrace()
                    }
                }
            }
        }
        lifecycleScope.launch {
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                val refresher = loadStates.refresh
                val append = loadStates.append

                //由于首次加载一样会调用NotLoading，所以采用外摆参数来判断
                LogUtils.e("collect==refresh===" + refresher.toString())
                LogUtils.e("collect==append===" + append.toString())
                if (refresher is LoadState.Loading) {
                    mDataBinding.smartRefresh.isRefreshing = true
                    isRefreshing = true
                    if (mAdapter.itemCount == 0 && needStateView()) {
                        mDataBinding.stateLayout.showLoading(refresh = false)
                    }
                } else if (isRefreshing) {
                    isRefreshing = false
                    mDataBinding.smartRefresh.isRefreshing = false
                    val displayEmptyMessage =
                        (refresher is LoadState.NotLoading && mAdapter.itemCount == 0)
                    val displayErrorMessage =
                        (refresher is LoadState.Error && mAdapter.itemCount == 0)
                    if (needStateView()) {
                        when {
                            displayErrorMessage -> {
                                mDataBinding.stateLayout.showError()
                            }
                            displayEmptyMessage -> {
                                mDataBinding.stateLayout.showEmpty()
                            }
                            else -> {
                                mDataBinding.stateLayout.showContent()
                            }
                        }
                    } else {
                        mDataBinding.stateLayout.showContent()
                    }

                } else {
                    mDataBinding.stateLayout.showContent()
                }
            }
        }

        mDataBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val canScrollVertically = recyclerView.canScrollVertically(-1)
                mDataBinding.smartRefresh.isEnabled = !canScrollVertically
            }
        })
    }

    open fun doSomeThing(adapter: RecyclerView.Adapter<*>): RecyclerView.Adapter<*> {
        return adapter
    }


    open fun needStateView(): Boolean {
        return true
    }

    open fun loadRefresh() {
        lifecycleScope.launch {
            viewModel.loadData(getParams())
                .collect {
                    mAdapter.submitData(it)
                }
        }
    }

    open fun getParams(): HashMap<String, Any>? = null

    open fun getBaseAdapter(): PagingDataAdapter<T, *> {
        return mAdapter
    }

    override fun loadData() {

    }


    open fun getFooterAdapter(): LoadStateAdapter<*>? {
        return BaseLoadFooterAdapter {
            mAdapter.retry()
        }
    }
}