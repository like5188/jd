package com.cq.jd.order.activity.coupon.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.ui.activity.BaseVmFragment
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderFragmentCouponWaitBinding
import com.cq.jd.order.databinding.OrderItemCouponListBinding
import com.cq.jd.order.entities.CouponWaitUseBean

class OrderCouponWaitFragment :
    BaseVmFragment<OrderCouponWaitModel, OrderFragmentCouponWaitBinding>(R.layout.order_fragment_coupon_wait) {
    override fun initWidget(savedInstanceState: Bundle?) {
        mViewBinding.model = mViewModal
        initAdapter()
    }

    val adapter = object :
        BaseQuickAdapter<CouponWaitUseBean, BaseDataBindingHolder<OrderItemCouponListBinding>>(R.layout.order_item_coupon_list) {
        init {
            addChildClickViewIds(R.id.tvWaitUse)
        }
        override fun convert(
            holder: BaseDataBindingHolder<OrderItemCouponListBinding>,
            item: CouponWaitUseBean,
        ) {
            holder.dataBinding?.apply {
                tvWaitUse.text = "去使用"
                tvTitle.text = item.coupon.title
                tvCondition.text = item.coupon.content
                tvEndTime.text = item.coupon.end_at + "\t 到期"
            }
        }
    }

    override fun loadData() {

    }

    override fun createObserver() {
        mViewModal.apply {
            couponListWait.observe(this@OrderCouponWaitFragment){
                adapter.setNewInstance(it as MutableList<CouponWaitUseBean>)
            }
        }
    }

    override fun lazyLoadData() {
        mViewModal.couponWaitUse(true)
    }

    private fun initAdapter(){

        mViewBinding.apply {
            recyclerViewWait.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewWait.adapter = adapter
            adapter.setOnItemChildClickListener { _, view, _ ->
                if (view.id == R.id.tvWaitUse){
                    requireActivity().finish()
                }
            }
        }
    }

}