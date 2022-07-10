package com.cq.jd.order.activity.coupon

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.fragment.app.Fragment
import com.common.library.ui.activity.BaseVmActivity
import com.cq.jd.order.R
import com.cq.jd.order.activity.coupon.fragment.OrderCouponLeadFragment
import com.cq.jd.order.activity.coupon.fragment.OrderCouponWaitFragment
import com.cq.jd.order.databinding.OrderActivityCouponBinding
import com.cq.jd.order.util.FragmentTabAdapter

class OrderCouponActivity :
    BaseVmActivity<OrderCouponModel, OrderActivityCouponBinding>(R.layout.order_activity_coupon), OrderCouponModelListener {
    private var tabAdapter: FragmentTabAdapter? = null
    private val fragments = ArrayList<Fragment>()
    private val showSelectUi = ObservableBoolean(false)
    private val selectedIndex = ObservableInt(1)

    override fun statusBarDarkFont(): Boolean = true

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        mDataBinding.showSelectUi = showSelectUi
        mDataBinding.selectedIndex = selectedIndex
        mDataBinding.llTitle.setOnClickListener {
            showSelectUi.set(!showSelectUi.get())
        }
        mDataBinding.clSelect.setOnClickListener {
            showSelectUi.set(false)
        }
        mDataBinding.tvSelect0.setOnClickListener {
            selectedIndex.set(0)
            showSelectUi.set(false)
        }
        mDataBinding.tvSelect1.setOnClickListener {
            selectedIndex.set(1)
            showSelectUi.set(false)
        }

        mViewModel.serOrderCouponModelListener(this)

        fragments.add(OrderCouponWaitFragment())
        fragments.add(OrderCouponLeadFragment())

        tabAdapter = FragmentTabAdapter(this, fragments, R.id.flChooseCoupon)

        tabAdapter?.setCurrentFragment(0)
    }

    override fun loadData() {

    }

    override fun createObserver() {

    }

    override fun clickWaitUse() {
        Log.e("shit", "clickWaitUse: ")
        tabAdapter?.setCurrentFragment(0)
    }

    override fun clickLeadCoupon() {
        Log.e("shit", "clickLeadCoupon: ")
        tabAdapter?.setCurrentFragment(1)
    }

    override fun updateWaitUseList() {
        (fragments[0] as OrderCouponWaitFragment).mViewModal.couponWaitUse(false)
    }


}