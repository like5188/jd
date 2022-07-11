package com.cq.jd.order.activity.goods

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.common.library.liveData.IntLiveData
import com.common.library.liveData.StringLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.GoodsDetailInfo
import com.cq.jd.order.entities.LicenseInfoBean
import com.cq.jd.order.entities.ShopCarListBean
import com.cq.jd.order.net.OrderNetApi

class OrderGoodsDetailModel(application: Application) :
    BaseViewModel(application) {

    val goodsDetailInfo = MutableLiveData<GoodsDetailInfo>()

//    var collectMsg = StringLiveData()
//    var collectRemoveMsg = StringLiveData()
    val collectStatus = StringLiveData()

    val shopCarBeanInfo = MutableLiveData<ShopCarListBean>()
    fun licenseDetail(merchantId: Int) {
        requestRs({
            OrderNetApi.service.goodsDetail(merchantId)
        }, {
            collectStatus.value = it.favorites.toString()
            goodsDetailInfo.value = it
        })
    }


    fun saveFavorites(
        merchantId: Int,
        keyword: String
    ) {
        val params = HashMap<String, Any>()
        params["type"] = 1
        params["data_id"] = merchantId
        params["keyword"] = keyword

        requestRs({
            OrderNetApi.service.saveFavorites(params)
        }, {
//            hintMsg.value = "收藏店铺成功"
            collectStatus.value = it.id
        })
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
            collectStatus.value="0"
//            collectRemoveMsg.value = "1"
        })
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
        })
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
        })
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

    fun saveShopping(
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
            OrderNetApi.service.saveShopping(params)
        }, {
            hintMsg.value = "商品加入成功"
        })
    }

}