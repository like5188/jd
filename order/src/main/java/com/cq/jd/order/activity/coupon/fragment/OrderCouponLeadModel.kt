package com.cq.jd.order.activity.coupon.fragment

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.CouponNoLeadBean
import com.cq.jd.order.entities.CouponWaitUseBean
import com.cq.jd.order.net.OrderNetApi

class OrderCouponLeadModel(application: Application) :
    BaseViewModel(application) {

    val couponListAll = MutableLiveData<List<CouponNoLeadBean>>()

    fun couponWaitUse() {
        requestRs({
            OrderNetApi.service.getCoupon()
        }, {
            couponListAll.value = it
        }, loadingMessage = "获取中...")
    }

    fun receiveCoupon(id:Int) {
        requestRs({
            OrderNetApi.service.receiveCoupon(id)
        }, {
            hintMsg.value = "优惠券成功"
        }, loadingMessage = "发送中...")
    }

}