package com.cq.jd.order.activity.complaint

import android.os.Bundle
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderActivityCommodityShopBinding
import com.google.gson.Gson

class OrderShopComplaintActivity :
    BaseVmActivity<OrderShopComplaintModel, OrderActivityCommodityShopBinding>(R.layout.order_activity_commodity_shop){
    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        val  mertId= intent.getIntExtra("mertId",0)
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = mertId
        hashMap["title"] = intent.getStringExtra("shopTitle")!!
        mViewModel.link = Gson().toJson(hashMap)
        setTitleText("投诉店铺")
        mDataBinding.apply {
            ImageUtils.loadImage(intent.getStringExtra("shopIcon"),ivLogo)
            tvShopTitle.text = intent.getStringExtra("shopTitle")
        }
    }

    override fun loadData() {
    }

    override fun createObserver() {
        mViewModel.apply {
            hintMsg.observe(this@OrderShopComplaintActivity) {
                finish()
            }
        }
    }
}