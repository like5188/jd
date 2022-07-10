package com.cq.jd.order.activity.shop

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.common.library.BaseApp
import com.common.library.liveData.StringLiveData
import com.common.library.router.ARouterPath
import com.common.library.router.provider.LocationService
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.ShopDetailBean
import com.cq.jd.order.net.OrderNetApi

class OrderShopDetailModel(application: Application) :
    BaseViewModel(application) {

    var collectMsg = StringLiveData()
    var collectRemoveMsg = StringLiveData()

    val shopDetailInfo = MutableLiveData<ShopDetailBean>()

    //        fun clickCertificate(){
//            BaseApp.application.startActivity(Intent(
//                BaseApp.application,
//                OrderShopCertificateActivity::class.java
//            ))
//        }
    fun shopDetail(merchantId: Int) {
        val locationService =
            ARouter.getInstance().build(ARouterPath.Map.LOCATION_SERVICE)
                .navigation() as LocationService
        val userSelectLocation = locationService.getUserSelectLocation()

        val params = HashMap<String, Any>()
        params["merchant_id"] = merchantId
        params["lng"] = userSelectLocation?.longitude.toString()
        params["lat"] = userSelectLocation?.latitude.toString()

        requestRs({
            OrderNetApi.service.merchantDetail(params)
        }, {
            shopDetailInfo.value = it
        }, loadingMessage = "数据接收中...")
    }

    fun saveFavorites(
        merchantId: Int,
        keyword: String
    ) {
        val params = HashMap<String, Any>()
        params["type"] = 0
        params["data_id"] = merchantId
        params["keyword"] = keyword

        requestRs({
            OrderNetApi.service.saveFavorites(params)
        }, {
//            hintMsg.value = "收藏店铺成功"
            collectMsg.value = "1"
        }, loadingMessage = "发送中...")
    }

    fun removeFavorites(
        merchantId: Int
    ) {
        val params = HashMap<String, Any>()
        params["id"] = merchantId

        requestRs({
            OrderNetApi.service.removeFavorites(params)
        }, {
//            hintMsg.value = "收藏店铺成功"
            collectRemoveMsg.value = "1"
        }, loadingMessage = "发送中...")
    }

}