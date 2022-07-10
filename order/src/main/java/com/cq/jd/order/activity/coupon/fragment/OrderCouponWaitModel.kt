package com.cq.jd.order.activity.coupon.fragment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.CouponWaitUseBean
import com.cq.jd.order.net.OrderNetApi
import com.cq.jd.order.util.PAGE_GOODS_LIMIT

class OrderCouponWaitModel(application: Application) :
    BaseViewModel(application) {

    val couponListWait = MutableLiveData<List<CouponWaitUseBean>>()

    fun couponWaitUse(showLoading:Boolean) {
        if (showLoading) {
            requestRs({
                OrderNetApi.service.couponWaitUse(1)
            }, {
                couponListWait.value = it
            }, loadingMessage = "获取中...")
        } else {
            requestRs({
                OrderNetApi.service.couponWaitUse(1)
            }, {
                couponListWait.value = it
            })
        }
    }

}