package com.common.library.ui.paging

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SimpleItemAnimator
import com.blankj.utilcode.util.LogUtils
import com.common.library.R
import com.common.library.databinding.BaseActivityPagingListBinding
import com.common.library.ui.activity.BaseViewActivity
import com.common.library.ui.createViewModel
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
abstract class BasePagingActivity<T : Any, Vm : BasePagingViewModel<T>> :
    BaseViewActivity<BaseActivityPagingListBinding>(R.layout.base_activity_paging_list) {

    lateinit var viewModel: Vm
    private lateinit var mAdapter: BasePagingDataAdapter<T, *>
    var isRefreshing = false;

    open var mItemClickListener: ((v: View, position: Int) -> Unit)? = null
    open var mItemLongClickListener: ((v: View, position: Int) -> Boolean)? = null
    open var mItemChildClickListener: ((v: View, position: Int) -> Unit)? = null


    override fun createObserver() {

    }


    abstract fun getAdapter(): BasePagingDataAdapter<T, *>


    open fun createOpenViewModel(): Vm {
        return createViewModel()
    }

    /**
     * 主要是为了防止在ViewPager里面
     * viewModel重复使用的问题
     *
     * key为null 会被服用。应该设置不同的值
     */
    open fun getViewmodelKey(): String? = null

    open fun getManager(): LayoutManager = LinearLayoutManager(this)

    override fun initWidget(savedInstanceState: Bundle?) {
        viewModel = createOpenViewModel()
        val linearLayoutManager = getManager()
        mDataBinding.recyclerView.layoutManager = linearLayoutManager
        val animator = mDataBinding.recyclerView.itemAnimator;
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false;
        }
        mAdapter = getAdapter()
        mAdapter.mItemClickListener = mItemClickListener
        mAdapter.mItemLongClickListener = mItemLongClickListener
        mAdapter.mItemChildClickListener = mItemChildClickListener
        val footerAdapter = getFooterAdapters()
        val headerAdapter = getHeaderAdapters()
        if (footerAdapter == null && headerAdapter == null) {
            mDataBinding.recyclerView.adapter = mAdapter
        } else {
            val adapters = arrayListOf<Adapter<out ViewHolder?>>()

            headerAdapter?.let {
                adapters.addAll(headerAdapter as Array<out Adapter<out ViewHolder?>>)
            }
            adapters.add(mAdapter)
            footerAdapter?.let {
                adapters.addAll(footerAdapter as Array<out Adapter<out ViewHolder?>>)
            }
            val concatAdapter = ConcatAdapter(ConcatAdapter.Config.Builder().setIsolateViewTypes(
                linearLayoutManager is LinearLayoutManager).build(),
                adapters)
            mDataBinding.recyclerView.adapter = concatAdapter
        }

        mDataBinding.smartRefresh.setOnRefreshListener {
            mAdapter.refresh()
        }
        mDataBinding.stateLayout.setRetryIds(R.id.error_retry)
        mDataBinding.stateLayout.onRefresh {
            mAdapter.refresh()
        }.showLoading()
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
            viewModel.loadData(getPagingParams())
                .collect {
                    mAdapter.submitData(it)
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
//                    mDataBinding.smartRefresh.isRefreshing = true
                    isRefreshing = true
                    if (mAdapter.itemCount == 0) {
                        mDataBinding.stateLayout.showLoading(refresh = false)
                    }
                } else if (isRefreshing) {
                    isRefreshing = false
                    mDataBinding.smartRefresh.isRefreshing = false
                    val displayEmptyMessage =
                        (refresher is LoadState.NotLoading && mAdapter.itemCount == 0 && headerAdapter?.size == 0)
                    val displayErrorMessage =
                        (refresher is LoadState.Error && mAdapter.itemCount == 0)
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

                }

            }
        }
    }


    open fun getFooterAdapters(): Array<Adapter<out ViewHolder?>?>? {

        return arrayOf(getFooterAdapter() as Adapter<out ViewHolder?>)
    }

    open fun getHeaderAdapters(): Array<Adapter<out ViewHolder?>?>?? = null

    open fun getPagingParams(): HashMap<String, Any>? = null

    open fun getBaseAdapter(): PagingDataAdapter<T, *> {
        return mAdapter
    }

    override fun loadData() {

    }

    override fun getPageViewModel(): BaseViewModel? {
        return viewModel
    }


    open fun getFooterAdapter(): LoadStateAdapter<*>? {
        val footerAdapter = BaseLoadFooterAdapter {
            mAdapter.retry()
        }
        mAdapter.addLoadStateListener {
            footerAdapter.loadState = it.append
        }
        return footerAdapter
    }
}