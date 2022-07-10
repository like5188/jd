package com.cq.jd.order.activity.complaint

import android.app.Application
import android.text.TextUtils
import com.common.library.liveData.StringLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.net.OrderNetApi

class OrderCommodityComplaintModel(application: Application) :
    BaseViewModel(application) {
    val content = StringLiveData()
    var link = ""


    fun complaintGoodShop() {
        if (TextUtils.isEmpty(content.value)) {
            hintMsg.value = "请输入投诉内容"
            return
        }
        val params = HashMap<String, Any>()
        params["content"] = content.value
        params["source"] = "商品"
        params["link"] = link
        requestRs({
            OrderNetApi.service.applyComplaint(params)
        }, {
            hintMsg.value = "商品投诉成功"
        }, loadingMessage = "发送中...")
    }
}