package com.cq.jd.order.activity.complaint

import android.os.Bundle
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderActivityCommodityComplaintBinding
import com.google.gson.Gson

class OrderCommodityComplaintActivity :
    BaseVmActivity<OrderCommodityComplaintModel, OrderActivityCommodityComplaintBinding>(R.layout.order_activity_commodity_complaint) {
    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = intent.getIntExtra("mertId",0)
        hashMap["title"] = intent.getStringExtra("goodsTitle")!!
        mViewModel.link = Gson().toJson(hashMap)
        mViewModel.link = "www.baidu.com"
        setTitleText("投诉商品")
        mDataBinding.apply {
            ImageUtils.loadImage(intent.getStringExtra("goodsIcon"), ivLogo)
            tvGoodsTitle.text = intent.getStringExtra("goodsTitle")
            tvGoodsPrice.text = intent.getStringExtra("shopPrice")
        }
    }

    override fun loadData() {

    }

    override fun createObserver() {
        mViewModel.apply {
            hintMsg.observe(this@OrderCommodityComplaintActivity) {
                finish()
            }
        }
    }
}