package com.cq.jd.order

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.common.library.liveData.StringLiveData
import com.common.library.router.ARouterPath
import com.common.library.router.provider.LocationService
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.ShopCarListBean
import com.cq.jd.order.entities.ShopDetailBean
import com.cq.jd.order.entities.ShopGoodsClassify
import com.cq.jd.order.net.OrderNetApi
import com.cq.jd.order.util.PAGE_GOODS_LIMIT

class OrderMainModel(application: Application) :
    BaseViewModel(application) {

    val shopDetailInfo = MutableLiveData<ShopDetailBean>()
    val shopCarBeanInfo = MutableLiveData<ShopCarListBean>()
    val listClsGoods = MutableLiveData<List<ShopGoodsClassify>>()
    var collectStatus = StringLiveData()
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
            collectStatus.value =it.favorites
            shopDetailInfo.value = it
        }, loadingMessage = "数据接收中...")
    }

    fun getShopping(merchantId: Int) {
        val params = HashMap<String, Any>()
        params["merchant_id"] = merchantId
        params["page"] = 1
        params["limit"] = 200

        requestRs({
            OrderNetApi.service.getShopping(params)
        }, {
            shopCarBeanInfo.value = it
        }, loadingMessage = "获取中...")
    }

    fun merchantClassify(merchantId: Int) {
        val params = HashMap<String, Any>()
        params["merchant_id"] = merchantId

        requestRs({
            OrderNetApi.service.merchantClassify(params)
        }, {
            listClsGoods.value = it
        }, loadingMessage = "获取中...")
    }

    fun removeShopping(merchantId: Int, id: String) {
        val params = HashMap<String, Any>()
//        params["merchant_id"] = merchantId
        params["id"] = id

        requestRs({
            OrderNetApi.service.removeShopping(params)
        }, {
            getShopping(merchantId)
            hintMsg.value = "购物车商品移除成功"
        }, loadingMessage = "移除中...")
    }


    fun saveFavorites(
        merchantId: Int,
        keyword: String
    ) {
        val params = HashMap<String, Any>()
        params["type"] = 0
        params["data_id"] = merchantId
        params["keyword"] = keyword
//        params["keyword"] = ""

        requestRs({
            OrderNetApi.service.saveFavorites(params)
        }, {
//            hintMsg.value = "收藏店铺成功"
            collectStatus.value =it.id
        }, loadingMessage = "发送中...")
    }

    fun editShopping(
        id: Int,
        join_quantity: Int,
        completed: ((Boolean) -> Unit)? = null,
    ) {
        val params = HashMap<String, Any>()
        params["id"] = id
        params["join_quantity"] = join_quantity
        requestRs({
            OrderNetApi.service.editShopping(params)
        }, {
//            hintMsg.value = "收藏店铺成功"
//            collectMsg.value = "1"
            completed?.invoke(true)
        }, error = {
            completed?.invoke(false)
        })
    }

    fun removeFavorites(
        merchantId: String
    ) {
        val params = HashMap<String, Any>()
        params["id"] = merchantId
        requestRs({
            OrderNetApi.service.removeFavorites(params)
        }, {
//            hintMsg.value = "收藏店铺成功"
            collectStatus.value = "0"
        }, loadingMessage = "发送中...")
    }

}