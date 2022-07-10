package com.cq.jd.order.activity.comment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.ClsGoodsBean
import com.cq.jd.order.entities.EvaluationList
import com.cq.jd.order.net.OrderNetApi
import com.cq.jd.order.util.PAGE_GOODS_LIMIT

class OrderGoodsCommentChildModel (application: Application) :
    BaseViewModel(application){

    val evaluationList = MutableLiveData<List<EvaluationList>>()

    fun evaluationList( merchantId: Int, goods_id: Int,scoring_start:Int,scoring_end:Int,page: Int) {
        val params = HashMap<String, Any>()
        params["merchant_id"] = merchantId
        params["page"] = page
        params["goods_id"] = goods_id
        params["scoring_start"] = scoring_start
        params["scoring_end"] = scoring_end
        params["limit"] = PAGE_GOODS_LIMIT
        params["identity"] = 1
//        params["order_no"] = 1

        requestRs({
            OrderNetApi.service.evaluateIndex(params)
        }, {
            evaluationList.value = it
        }, loadingMessage = "获取中...")
    }

    }