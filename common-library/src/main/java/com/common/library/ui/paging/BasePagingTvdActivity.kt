package com.common.library.ui.paging

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.common.library.R
import com.common.library.ui.activity.BaseViewActivity
import com.common.library.ui.getVmClazz1
import com.common.library.ui.paging.BaseLoadFooterAdapter
import com.common.library.ui.paging.BasePagingDataAdapter
import com.common.library.ui.paging.BasePagingViewModel
import com.common.library.viewModel.BaseViewModel
import com.drake.statelayout.StateLayout
import com.zhw.http.AppException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *
 * @author like
 */
abstract class BasePagingTvdActivity<T : Any, Vm : BasePagingViewModel<T>, DB : ViewDataBinding>(layoutId: Int) :
    BaseViewActivity<DB>(layoutId) {

    lateinit var viewModel: Vm
    lateinit var mAdapter: BasePagingDataAdapter<T, *>
    var isRefreshing = false;

    private val recyclerView: RecyclerView by lazy {
        mDataBinding.root.findViewById<RecyclerView>(R.id.recyclerView)
            ?: throw RuntimeException("RecyclerView id must be R.id.recyclerView in BasePagingActivity")
    }
    private val smartRefresh: SwipeRefreshLayout? by lazy {
        mDataBinding.root.findViewById(R.id.smartRefresh)
    }
    private val stateLayout: StateLayout? by lazy {
        mDataBinding.root.findViewById(R.id.stateLayout)
    }

    override fun createObserver() {

    }

    abstract fun getAdapter(): BasePagingDataAdapter<T, *>


    open fun createOpenViewModel(): Vm {
        return createViewModel()
    }

    open fun getManager(): LayoutManager = LinearLayoutManager(this)

    override fun initWidget(savedInstanceState: Bundle?) {
        viewModel = createOpenViewModel()
        val linearLayoutManager = getManager()
        recyclerView.layoutManager = linearLayoutManager
        val animator = recyclerView.itemAnimator;
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false;
        }
        mAdapter = getAdapter()
        val footerAdapter = getFooterAdapters()
        val headerAdapter = getHeaderAdapters()
        if (footerAdapter == null && headerAdapter == null) {
            recyclerView.adapter = mAdapter
        } else {
            val adapters = arrayListOf<Adapter<out ViewHolder?>>()

            headerAdapter?.let {
                adapters.addAll(headerAdapter as Array<out Adapter<out ViewHolder?>>)
            }
            adapters.add(mAdapter)
            footerAdapter?.let {
                adapters.addAll(footerAdapter as Array<out Adapter<out ViewHolder?>>)
            }
            val concatAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder().setIsolateViewTypes(
                    linearLayoutManager is LinearLayoutManager
                ).build(),
                adapters
            )
            recyclerView.adapter = concatAdapter
        }

        smartRefresh?.setOnRefreshListener {
            refresh()
        }
        stateLayout?.setRetryIds(R.id.error_retry)
        stateLayout?.onRefresh {
            refresh()
        }?.showLoading()
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
                    mAdapter.submitData(handlePagingData(it))
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
                    smartRefresh?.isRefreshing = true
                    isRefreshing = true
                    if (mAdapter.itemCount == 0) {
                        stateLayout?.showLoading(refresh = false)
                    }
                } else if (isRefreshing) {
                    isRefreshing = false
                    smartRefresh?.isRefreshing = false
                    val displayEmptyMessage =
                        (refresher is LoadState.NotLoading && mAdapter.itemCount == 0 && headerAdapter?.size == 0)
                    val displayErrorMessage =
                        (refresher is LoadState.Error && mAdapter.itemCount == 0)
                    when {
                        displayErrorMessage -> {
                            stateLayout?.showError()
                        }
                        displayEmptyMessage -> {
                            stateLayout?.showEmpty()
                        }
                        else -> {
                            stateLayout?.showContent()
                        }
                    }

                }

            }
        }
    }

    open fun handlePagingData(data: PagingData<T>): PagingData<T> {
        return data
    }

    open fun getFooterAdapters(): Array<Adapter<out ViewHolder?>?>? {

        return arrayOf(getFooterAdapter() as Adapter<out ViewHolder?>)
    }

    open fun getHeaderAdapters(): Array<Adapter<out ViewHolder?>?>?? = null

    open fun getPagingParams(): HashMap<String, Any>? = null

    open fun getBaseAdapter(): PagingDataAdapter<T, *> {
        return mAdapter
    }

    open fun refresh() {
        mAdapter.refresh()
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

fun <VM : BasePagingViewModel<*>, DB : ViewDataBinding> BasePagingTvdActivity<*, VM, DB>.createViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(this.application)
    ).get(getVmClazz1(this))
}
