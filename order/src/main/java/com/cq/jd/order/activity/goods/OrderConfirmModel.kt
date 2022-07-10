package com.cq.jd.order.activity.goods

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.common.library.bean.PayTypeBean
import com.common.library.liveData.StringLiveData
import com.common.library.router.ARouterPath
import com.common.library.router.provider.LocationService
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.*
import com.cq.jd.order.net.OrderNetApi

class OrderConfirmModel(application: Application) :
    BaseViewModel(application) {

    val OrderConfirmBean = MutableLiveData<OrderConfirmBean>()
    val OrderPayInfo = MutableLiveData<OrderPayInfo>()
    val DistanceInfo = MutableLiveData<DistanceInfo>()
    val PayTypeBean = MutableLiveData<List<PayTypeBean>>()
    val couponListAll = MutableLiveData<List<CouponWaitUseBean>>()
    open var hintMsg1 = StringLiveData()

    fun couponWaitUse() {
        requestRs({
            OrderNetApi.service.couponWaitUse(1)
        }, {
            couponListAll.value = it
        }, loadingMessage = "获取中...")
    }


    fun handleOrder(
        goodsId: Int,
        merchantId: Int,
        spec_attribute_id: String,
        join_quantity: String
    ) {
        val params = HashMap<String, Any>()
        params["goods_id"] = goodsId
        params["merchant_id"] = merchantId
        params["spec_attribute"] = spec_attribute_id
        params["join_quantity"] = join_quantity

        requestRs({
            OrderNetApi.service.handleOrder(params)
        }, {
            OrderConfirmBean.value = it
        }, loadingMessage = "发送中...")
    }

    fun handleOrder(
        merchantId: Int
    ) {
        val params = HashMap<String, Any>()
        params["merchant_id"] = merchantId

        requestRs({
            OrderNetApi.service.handleOrder(params)
        }, {
            OrderConfirmBean.value = it
        }, loadingMessage = "发送中...")
    }

    fun createOrder(
        pay_type: String,
        merchantId: Int,
        goodsId: Int,
        coupon: Int,
        remark: String,
        pay_code: String
    ) {
        val params = HashMap<String, Any>()
        params["pay_type"] = pay_type
        params["coupon"] = coupon
        params["merchant_id"] = merchantId
        params["goods_id"] = goodsId
        params["remark"] = remark
        params["pay_code"] = pay_code

        requestRs({
            OrderNetApi.service.createOrder(params)
        }, {
            OrderPayInfo.value = it
        }, loadingMessage = "发送中...")
    }

    fun getDistance(
        merchantId: Int
    ) {
        val locationService =
            ARouter.getInstance().build(ARouterPath.Map.LOCATION_SERVICE)
                .navigation() as LocationService
        val userSelectLocation = locationService.getUserSelectLocation()

        val params = HashMap<String, Any>()
        params["id"] = merchantId
        params["lng"] = userSelectLocation?.longitude.toString()
        params["lat"] = userSelectLocation?.latitude.toString()

        requestRs({
            OrderNetApi.service.getDistance(params)
        }, {
            DistanceInfo.value = it
        }, loadingMessage = "发送中...")
    }

    fun getPayType() {
        requestRs({
            OrderNetApi.service.getPayType("")
        }, {
            PayTypeBean.value = it
        }, loadingMessage = "发送中...")
    }


    fun checkPayPassword(
        password: String
    ) {
        val params = HashMap<String, Any>()
        val split = password.split(",")
        params["password"] = split[0]

        requestRs({
            OrderNetApi.service.checkPayPassword(params)
        }, {
            hintMsg.value = "密码验证成功"
//            hintMsg1.value = split[1]
            createPayment(split[1],password)
        }, loadingMessage = "发送中...")
    }

    fun createPayment(
        order_no: String,
        password: String
    ) {
        val params = HashMap<String, Any>()
        params["password"] = password
        params["order_no"] = order_no

        requestRs({
            OrderNetApi.service.createPayment(params)
        }, {
            hintMsg.value = "订单支付成功"
            hintMsg1.value = "1"
        }, loadingMessage = "发送中...")
    }
}