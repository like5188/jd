package com.cq.jd.order.activity.shop

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.BaseApp
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderActivityShopCertificateBinding
import com.cq.jd.order.databinding.OrderActivityShopDetailBinding
import com.cq.jd.order.databinding.OrderItemShopCertificateListBinding

class OrderShopDetailActivity :
    BaseVmActivity<OrderShopDetailModel, OrderActivityShopDetailBinding>(R.layout.order_activity_shop_detail) {

    private var merchantId = 0
    private var is_favorites = 0
    private var title = ""

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        setTitleText("店铺详情")
        getTitleBar()?.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        merchantId = intent.getIntExtra("merchantId", 0)
        mDataBinding.tvCertificate.setOnClickListener {
            val intent = Intent(this,
                OrderShopCertificateActivity::class.java
            )
            intent.putExtra("indexId", merchantId)
            startActivity(intent)
        }
        mDataBinding.ivCollect.setOnClickListener {
            if(TextUtils.isEmpty(title)){
                return@setOnClickListener
            }
            if (is_favorites ==1){
                mViewModel.removeFavorites(merchantId)
            }else{
                mViewModel.saveFavorites(merchantId,title)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun loadData() {
        mViewModel.shopDetail(merchantId)
    }

    override fun createObserver() {
        mViewModel.apply {
            //收藏
            collectMsg.observe(this@OrderShopDetailActivity) {
                mDataBinding.ivCollect.imageTintList =
                    ColorStateList.valueOf(Color.parseColor("#FFAA32"))
            }
            collectRemoveMsg.observe(this@OrderShopDetailActivity) {
                mDataBinding.ivCollect.imageTintList = null
            }
            shopDetailInfo.observe(this@OrderShopDetailActivity){
                mDataBinding.apply {
                    ImageUtils.loadImage(it.head_pic,ivLogo)
                    tvName.text = it.title
                    tvAddress.text = it.title
                    title= it.title
                    tvIntro.text = it.notice
                    tvBelong.text = it.province+it.city+it.area+it.street
                    ratingBar.grade = it.evaluate_score
                    tvCertificateLess.text = "已签署消保协议"
                    tvTextStart.text = it.evaluate_score.toString()
                    tvStartLv.text = when {
                        it.evaluate_score >=4 -> {
                            "高"
                        }
                        it.evaluate_score >=3 -> {
                            "中"
                        }
                        else -> {
                            "低"
                        }
                    }
                    is_favorites = it.is_favorites
                    if (it.is_favorites == 1) {
                        ivCollect.imageTintList =
                            ColorStateList.valueOf(Color.parseColor("#FFAA32"))
                    } else {
                        ivCollect.imageTintList = null
                    }
                }

            }
        }
    }


}