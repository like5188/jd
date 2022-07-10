package com.cq.jd.order.activity.goods

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.common.library.liveData.StringLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.ClsGoodsBean
import com.cq.jd.order.entities.ShopCarListBean
import com.cq.jd.order.net.OrderNetApi
import com.cq.jd.order.util.PAGE_GOODS_LIMIT

class OrderSearchModel(application: Application) :
    BaseViewModel(application) {


    val listClsGoods = MutableLiveData<List<ClsGoodsBean>>()
    val shopCarBeanInfo = MutableLiveData<ShopCarListBean>()
    var collectMsg = StringLiveData()
    var collectRemoveMsg = StringLiveData()


    fun goodsList(title: String, merchantId: Int, page: Int) {
        val params = HashMap<String, Any>()
        params["merchant_id"] = merchantId
        params["page"] = page
        params["title"] = title
        params["limit"] = PAGE_GOODS_LIMIT

        requestRs({
            OrderNetApi.service.merchantGoods(params)
        }, {
            listClsGoods.value = it
        }, loadingMessage = "获取中...")
    }

    fun saveShopping(
        goodsId: Int,
        merchantId: Int,
        spec_attribute_id: String,
        join_quantity: String
    ) {
        val params = HashMap<String, Any>()
        params["goods_id"] = goodsId
        params["merchant_id"] = merchantId
        params["spec_attribute_id"] = spec_attribute_id
        params["join_quantity"] = join_quantity

        requestRs({
            OrderNetApi.service.saveShopping(params)
        }, {
            hintMsg.value = "商品加入成功"
        }, loadingMessage = "发送中...")
    }

    fun getShopping( merchantId: Int) {
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

    fun removeShopping( merchantId: Int,id:String) {
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
        keyword: String,
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
            collectMsg.value = "1"
            completed?.invoke(true)
        }, error = {
            completed?.invoke(false)
        })
    }
}