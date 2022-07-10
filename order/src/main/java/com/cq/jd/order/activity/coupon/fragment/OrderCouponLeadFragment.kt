package com.cq.jd.order.activity.coupon.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.ui.activity.BaseVmFragment
import com.cq.jd.order.R
import com.cq.jd.order.activity.coupon.OrderCouponActivity
import com.cq.jd.order.databinding.OrderFragmentCouponLeadBinding
import com.cq.jd.order.databinding.OrderItemCouponListBinding
import com.cq.jd.order.entities.CouponNoLeadBean

class OrderCouponLeadFragment :
    BaseVmFragment<OrderCouponLeadModel, OrderFragmentCouponLeadBinding>(R.layout.order_fragment_coupon_lead) {
    override fun initWidget(savedInstanceState: Bundle?) {
        mViewBinding.model = mViewModal
        initAdapter()
    }

    val adapter = object :
        BaseQuickAdapter<CouponNoLeadBean, BaseDataBindingHolder<OrderItemCouponListBinding>>(R.layout.order_item_coupon_list) {

        init {
            addChildClickViewIds(R.id.tvWaitUse)
        }

        override fun convert(
            holder: BaseDataBindingHolder<OrderItemCouponListBinding>,
            item: CouponNoLeadBean,
        ) {
            holder.dataBinding?.apply {
                tvWaitUse.text = "立即领取"
                tvTitle.text = item.title
                tvCondition.text = item.content
                tvEndTime.text = item.end_at + "\t 到期"
            }
        }
    }

    override fun loadData() {
    }

    override fun createObserver() {
        mViewModal.apply {
            couponListAll.observe(this@OrderCouponLeadFragment) {
                adapter.setNewInstance(it as MutableList<CouponNoLeadBean>)
            }
            hintMsg.observe(this@OrderCouponLeadFragment) {
                mViewModal.couponWaitUse()
                (activity as OrderCouponActivity).updateWaitUseList()
            }
        }
    }

    override fun lazyLoadData() {
        mViewModal.couponWaitUse()
    }

    private fun initAdapter() {
        mViewBinding.apply {
            recyclerViewLead.layoutManager = LinearLayoutManager(requireContext())
//            recyclerViewLead.addItemDecoration(
//                DividerItemDecoration(
//                    requireContext(),
//                    DividerItemDecoration.VERTICAL
//                )
//            )
            recyclerViewLead.adapter = adapter

            adapter.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.tvWaitUse) {
                    mViewModal.receiveCoupon(adapter.getItem(position).id)
                }
            }
        }
    }
}