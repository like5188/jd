package com.common.library.ui.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.common.library.R
import com.common.library.databinding.BaseViewLoadMoreBinding

/**
 * Created by *** on 2022/1/8 11:55 上午
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class BaseNewLoadFooterAdapter(val retry: () -> Unit) :
    LoadStateAdapter<BaseNewLoadFooterAdapter.LoadingViewHolder>() {
    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bindNetWorkStatus(loadState)
        holder.baseViewLoadMoreBinding.loadMoreLoadFailView.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        return LoadingViewHolder.instance(parent = parent)
    }


    /**
     * 该方法用来出发加载完后，loadState.NotLoading
     * 没有进
     * onBindViewHolder里面
     *
     */
    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }

    class LoadingViewHolder(val baseViewLoadMoreBinding: BaseViewLoadMoreBinding) :
        RecyclerView.ViewHolder(baseViewLoadMoreBinding.root) {


        companion object {
            // 1
            fun instance(parent: ViewGroup): LoadingViewHolder {
                val baseViewLoadMoreBinding =
                    DataBindingUtil.inflate<BaseViewLoadMoreBinding>(LayoutInflater.from(parent.context),
                        R.layout.base_view_load_more, parent, false)
                return LoadingViewHolder(baseViewLoadMoreBinding)
            }
        }

        // 2
        fun bindNetWorkStatus(loadingStatus: LoadState) {
            // 3
            when (loadingStatus) {
                is LoadState.Error -> {
                    baseViewLoadMoreBinding.loadMoreLoadingView.visibility = View.GONE
                    baseViewLoadMoreBinding.loadMoreLoadEndView.visibility = View.GONE
                    baseViewLoadMoreBinding.loadMoreLoadFailView.visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    baseViewLoadMoreBinding.loadMoreLoadingView.visibility = View.GONE
                    baseViewLoadMoreBinding.loadMoreLoadFailView.visibility = View.GONE
                    baseViewLoadMoreBinding.loadMoreLoadEndView.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    baseViewLoadMoreBinding.loadMoreLoadEndView.visibility = View.GONE
                    baseViewLoadMoreBinding.loadMoreLoadFailView.visibility = View.GONE
                    baseViewLoadMoreBinding.loadMoreLoadingView.visibility = View.VISIBLE
                }
            }
        }

    }
}