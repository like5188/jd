package com.cq.jd.order.activity.goods

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderActivityOrderConfirmBinding
import com.cq.jd.order.databinding.OrderItemOrderGoodsListBinding
import com.cq.jd.order.dialog.DialogChooseCoupon
import com.cq.jd.order.dialog.DialogChoosePay
import com.cq.jd.order.dialog.DialogInputPwd
import com.cq.jd.order.dialog.DialogOrderDistanceShow
import com.cq.jd.order.entities.OrderConfirmBean
import com.cq.jd.order.entities.OrderConfirmItem
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar


class OrderConfirmActivity :
    BaseVmActivity<OrderConfirmModel, OrderActivityOrderConfirmBinding>(R.layout.order_activity_order_confirm) {
    private var goodsId = 0
    private var merchantId = 0
    private var num = 0
    private var ids = ""
    private var payType = ""
    private var payCode = ""
    private var coupon = 0
    private var price = 0.0


    override fun initWidget(savedInstanceState: Bundle?) {
        merchantId = intent.getIntExtra("merchantId", 0)
        goodsId = intent.getIntExtra("goodsId", 0)
        num = intent.getIntExtra("num", 0)
        ids = intent.getStringExtra("ids").toString()
        mDataBinding.model = mViewModel
        mDataBinding.apply {
            mCoordinatorTabLayout.setTranslucentStatusBar(this@OrderConfirmActivity)
                .setTitle("")
                .setBackEnable(true)

            mCoordinatorTabLayout.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _,
                                                                                                           verticalOffset ->
                if (verticalOffset < 0) {
                    mCoordinatorTabLayout.getmToolbar().setBackgroundColor(
                        ContextCompat.getColor(this@OrderConfirmActivity, R.color.colorMain)
                    )

                    ImmersionBar.with(this@OrderConfirmActivity)
                        .statusBarColor(R.color.colorMain)
                        .init()
                } else {
                    mCoordinatorTabLayout.getmToolbar().setBackgroundColor(
                        ContextCompat.getColor(
                            this@OrderConfirmActivity,
                            R.color.transparent
                        )
                    )
                    ImmersionBar.with(this@OrderConfirmActivity)
                        .transparentStatusBar()
                        .init()
                }
            })

            mCoordinatorTabLayout.getmToolbar().setNavigationOnClickListener { finish() }

            tvPayType.setOnClickListener {//zhifu fangshi
                mViewModel.getPayType()
            }

            tvCoupon.setOnClickListener {//xuan ze youhuiquan
                mViewModel.couponWaitUse()
            }

            tvPayOrder.setOnClickListener {//提交订单
                val remark = mDataBinding.etDes.text.toString()
                mViewModel.createOrder(
                    payType,
                    merchantId,
                    goodsId,
                    coupon,
                    remark,
                    payCode
                )
            }

        }
        initAdapter()
    }

    val adapter = object :
        BaseQuickAdapter<OrderConfirmItem, BaseDataBindingHolder<OrderItemOrderGoodsListBinding>>(R.layout.order_item_order_goods_list) {
        override fun convert(
            holder: BaseDataBindingHolder<OrderItemOrderGoodsListBinding>,
            item: OrderConfirmItem,
        ) {
            holder.dataBinding?.apply {
                ImageUtils.loadImage(
                    item.goods.cover, ivLogo
                )
                tvTitle.text = item.goods.title
                tvSinglePrice.text = item.price_pay
                tvNum.text = "x" + item.join_quantity.toString()
                tvType.text = item.spec_attribute_string
            }
        }
    }

    override fun loadData() {
        if (goodsId != 0) {
            mViewModel.handleOrder(goodsId, merchantId, ids, num.toString())
        }else{
            mViewModel.handleOrder(merchantId)
        }
        mViewModel.getDistance(merchantId)
    }

    override fun createObserver() {
        mViewModel.apply {
            //确认订单
            OrderConfirmBean.observe(this@OrderConfirmActivity) {
                ImageUtils.loadImage(
                    it.merchant.head_pic,
                    mDataBinding.mCoordinatorTabLayout.ivGoodsCover
                )
                mDataBinding.mCoordinatorTabLayout.tvShopName.text = it.merchant.title
                adapter.setNewInstance(it.list as MutableList<OrderConfirmItem>)
                if (it.list.isNotEmpty()) {
                    it.list.forEach { item ->
                        price += item.price_pay.toDouble() * item.join_quantity
                    }
                    mDataBinding.tvTotalPrice.text = price.toString()
                }
            }
            //支付方式
            PayTypeBean.observe(this@OrderConfirmActivity) {
                val dialogChoosePay = DialogChoosePay(this@OrderConfirmActivity)
                dialogChoosePay.setPayTypeData(it)
                dialogChoosePay.setOnConfirmListener { pos ->
                    val payTypeBean = it[pos]
                    mDataBinding.tvPayType.text = payTypeBean.name
                    payType = payTypeBean.name
                    payCode = payTypeBean.type
                }
                dialogChoosePay.show()
            }
            //显示距离弹窗
            DistanceInfo.observe(this@OrderConfirmActivity) {
                if (it.distance > 2000) {
                    val dialogOrderDistanceShow = DialogOrderDistanceShow(this@OrderConfirmActivity)
                    dialogOrderDistanceShow.setUiData((it.distance / 1000).toString())
                    dialogOrderDistanceShow.show()
                }
            }
            //优惠券订单
            couponListAll.observe(this@OrderConfirmActivity) {
                if (it != null && it.size > 0) {
                    val dialogChooseCoupon = DialogChooseCoupon(this@OrderConfirmActivity)
                    dialogChooseCoupon.initCls1Adapter(it)
                    dialogChooseCoupon.setOnConfirmListener { pos ->
                        val couponWaitUseBean = it[pos]
                        coupon = couponWaitUseBean.id
                        mDataBinding.tvCoupon.text = couponWaitUseBean.coupon.title
                    }
                    dialogChooseCoupon.show()
                }
            }
            //生成订单
            OrderPayInfo.observe(this@OrderConfirmActivity) {
                if (payCode != "wechat" && payCode != "unionPay" && payCode != "alipay") {
                    val dialogInputPwd = DialogInputPwd(this@OrderConfirmActivity)
                    dialogInputPwd.setOnConfirmListener(object :
                        DialogInputPwd.OnConfirmPwdListener {
                        override fun onConfirm(type: String) {
                            val type1 = type + "," + it.order_no
                            mViewModel.checkPayPassword(type1)
                        }
                    })
                    dialogInputPwd.show()
                }
            }
            //余额支付
            hintMsg1.observe(this@OrderConfirmActivity){

            }
        }
    }


    private fun initAdapter() {

        mDataBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@OrderConfirmActivity)
//            recyclerView.addItemDecoration(
//                DividerItemDecoration(
//                    this@OrderConfirmActivity,
//                    DividerItemDecoration.VERTICAL
//                )
//            )
            recyclerView.adapter = adapter
        }
    }

}