package com.cq.jd.order.activity.coupon

import android.app.Application
import android.view.View
import com.common.library.liveData.IntLiveData
import com.common.library.viewModel.BaseViewModel

class OrderCouponModel(application: Application) :
    BaseViewModel(application) {

    private var orderCouponModelListener: OrderCouponModelListener? = null

    fun serOrderCouponModelListener(l:OrderCouponModelListener){
        orderCouponModelListener = l
    }

    val tabChooseState = IntLiveData()


    fun clickWaitUse(view: View) {
        tabChooseState.value = 0
        orderCouponModelListener?.clickWaitUse()
    }

    fun clickLead(view: View) {
        tabChooseState.value = 1
        orderCouponModelListener?.clickLeadCoupon()
    }


}