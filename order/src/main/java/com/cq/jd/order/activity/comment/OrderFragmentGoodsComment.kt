package com.cq.jd.order.activity.comment
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.ui.activity.BaseVmFragment
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.databinding.*
import com.cq.jd.order.entities.EvaluationList
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class OrderFragmentGoodsComment :
    BaseVmFragment<OrderGoodsCommentChildModel, OrderFragmentGoodsFragmentBinding>(R.layout.order_fragment_goods_fragment) {

    private var page = 1
    private var merchantId = 0
    private var goodsId = 0
    private var scoring = 0
    private var currenSize = 0
    private var index = 1

    val adapter = object :
        BaseQuickAdapter<EvaluationList, BaseDataBindingHolder<OrderItemGoodsCommentListBinding>>(R.layout.order_item_goods_comment_list) {
        override fun convert(
            holder: BaseDataBindingHolder<OrderItemGoodsCommentListBinding>,
            item: EvaluationList,
        ) {
            holder.dataBinding?.apply {
                item.user?.headimg?.let {
                    ImageUtils.loadImage(item.user.headimg, ivHead)
                }
                tvName.text = item.user.nickname
                tvTime.text = item.create_at
                tvContent.text = item.content
            }
        }
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        merchantId = requireActivity().intent.getIntExtra("merchantId",0)
        goodsId = requireActivity().intent.getIntExtra("goodsId",0)
        index = arguments?.getInt("exid")!!
        initAdapter()
    }

    override fun loadData() {

    }

    override fun lazyLoadData() {
        mViewBinding.smartRefresh.autoRefresh()
    }

    override fun createObserver() {
       mViewModal.apply {
           evaluationList.observe(requireActivity()) {
               if (it != null && it.size > 0) {
                   if (page == 1) {
                       adapter.setNewInstance(it as MutableList<EvaluationList>)
                   } else {
                       adapter.addData(it)
                   }
                   currenSize += it.size
               } else {

               }
           }
       }
    }

    private fun initAdapter() {
        mViewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    page = 1
                    when (index) {
                        1 -> {
                            mViewModal.evaluationList(merchantId, goodsId, 1, 5,page)
                        }
                        2 -> {
                            mViewModal.evaluationList(merchantId, goodsId, 4, 5,page)
                        }
                        else -> {
                            mViewModal.evaluationList(merchantId, goodsId, 1, 4,page)
                        }
                    }

                    refreshLayout.finishRefresh()
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    page++
                    when (index) {
                        1 -> {
                            mViewModal.evaluationList(merchantId, goodsId, 1, 5,page)
                        }
                        2 -> {
                            mViewModal.evaluationList(merchantId, goodsId, 4, 5,page)
                        }
                        else -> {
                            mViewModal.evaluationList(merchantId, goodsId, 1, 4,page)
                        }
                    }
                    refreshLayout.finishLoadMore()
                }

            })
//            adapter.setNewInstance(data as MutableList<ItemAction>)
        }
    }
}