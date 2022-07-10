package com.cq.jd.order.activity.goods.fragment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.ClsGoodsBean
import com.cq.jd.order.entities.ShopDetailBean
import com.cq.jd.order.entities.ShopGoodsClassify
import com.cq.jd.order.net.OrderNetApi
import com.cq.jd.order.util.EVENT_BUS_KEY_SAVE_SHOPPING_SUCCESS
import com.cq.jd.order.util.PAGE_GOODS_LIMIT
import com.jeremyliao.liveeventbus.LiveEventBus

class OrderGoodsClsModel(application: Application) :
    BaseViewModel(application) {

    val listClsGoods = MutableLiveData<List<ClsGoodsBean>>()


    fun goodsList(classifyId: Int, merchantId: Int, page: Int) {
        val params = HashMap<String, Any>()
        params["classify_id"] = classifyId
        params["merchant_id"] = merchantId
        params["page"] = page
        params["title"] = ""
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
        params["spec_attribute"] = spec_attribute_id
        params["join_quantity"] = join_quantity

        requestRs({
            OrderNetApi.service.saveShopping(params)
        }, {
            hintMsg.value = "商品加入成功"
            LiveEventBus.get<Int>(EVENT_BUS_KEY_SAVE_SHOPPING_SUCCESS).post(0)
        }, loadingMessage = "发送中...")
    }

}