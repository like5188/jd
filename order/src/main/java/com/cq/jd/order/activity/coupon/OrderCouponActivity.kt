package com.cq.jd.order.activity.coupon

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.common.library.ui.activity.BaseVmActivity
import com.cq.jd.order.R
import com.cq.jd.order.activity.coupon.fragment.OrderCouponLeadFragment
import com.cq.jd.order.activity.coupon.fragment.OrderCouponWaitFragment
import com.cq.jd.order.databinding.OrderActivityCouponBinding
import com.cq.jd.order.util.FragmentTabAdapter

class OrderCouponActivity :
    BaseVmActivity<OrderCouponModel, OrderActivityCouponBinding>(R.layout.order_activity_coupon),OrderCouponModelListener {

    private var tabAdapter: FragmentTabAdapter? = null
    private val fragments = ArrayList<Fragment>()

    override fun statusBarDarkFont(): Boolean = true

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        mViewModel.serOrderCouponModelListener(this)

        fragments.add(OrderCouponWaitFragment())
        fragments.add(OrderCouponLeadFragment())

        tabAdapter = FragmentTabAdapter(this, fragments, R.id.flChooseCoupon)

        setTitleText("优惠券")
        getTitleBar()?.setBackgroundColor(ContextCompat.getColor(this,R.color.white))

        tabAdapter?.setCurrentFragment(0)
    }

    override fun loadData() {

    }

    override fun createObserver() {

    }

    override fun clickWaitUse() {
        Log.e("shit", "clickWaitUse: " )
        tabAdapter?.setCurrentFragment(0)
    }

    override fun clickLeadCoupon() {
        Log.e("shit", "clickLeadCoupon: " )
        tabAdapter?.setCurrentFragment(1)
    }

    override fun updateWaitUseList() {
        (fragments[0] as OrderCouponWaitFragment).mViewModal.couponWaitUse(false)
    }


}