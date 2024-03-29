package com.cq.jd.order.activity.goods
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.dialog.ShareDialog
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.Util
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.activity.comment.OrderGoodsCommentActivity
import com.cq.jd.order.activity.complaint.OrderCommodityComplaintActivity
import com.cq.jd.order.activity.coupon.OrderCouponActivity
import com.cq.jd.order.activity.shop.OrderShopDetailActivity
import com.cq.jd.order.databinding.OrderActivityGoodsDetailBinding
import com.cq.jd.order.databinding.OrderItemGoodsCommentListBinding
import com.cq.jd.order.dialog.DialogChooseGoodsType
import com.cq.jd.order.dialog.DialogShopCar
import com.cq.jd.order.dialog.PopupGoodsMenu
import com.cq.jd.order.entities.*
import com.cq.jd.order.util.EVENT_BUS_KEY_SAVE_SHOPPING_SUCCESS
import com.cq.jd.order.widget.mzbanner.BannerViewHolder
import com.cq.jd.share.ShareUtil
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.XPopup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class OrderGoodsDetailActivity :
    BaseVmActivity<OrderGoodsDetailModel, OrderActivityGoodsDetailBinding>(R.layout.order_activity_goods_detail) {

    private var goodsId = 0
//    private var isCollect = 0
    private var merchantId = 0
    private var clsGoodsBean: ClsGoodsBean? = null
    private var shopCarData: ShopCarListBean? = null
    private var dialogShopCar: DialogShopCar? = null
    companion object{
         var shopDetailBean: ShopDetailBean? = null
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        shopDetailBean = intent.getSerializableExtra("shopDetailBean") as ShopDetailBean?
        goodsId = intent.getIntExtra("goodsId", 0)
        mViewModel.licenseDetail(goodsId)
    }
    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        shopDetailBean = intent.getSerializableExtra("shopDetailBean") as ShopDetailBean?
        mDataBinding.apply {
            headerLayout
                .setTitle("")
                .setBackEnable(true)
//                .setTopImageRes(R.drawable.indexbg)
            headerLayout.llSearch.setOnClickListener {//搜索
                if (shopDetailBean == null) {
                    return@setOnClickListener
                }
                val intent = Intent(this@OrderGoodsDetailActivity, OrderSearchActivity::class.java)
                intent.putExtra("shopDetailBean", shopDetailBean)
                startActivity(intent)
            }
            llShopCar.setOnClickListener {//购物车
                if (shopCarData == null) {
                    return@setOnClickListener
                }
                if (orderDetail == null) {
                    return@setOnClickListener
                }
                if (shopCarData?.list == null || shopCarData?.list!!.size == 0) {
                    ToastUtils.showShort("购物车空空如也，去挑选您喜欢的商品吧")
                    return@setOnClickListener
                }
                val list = shopCarData?.list
                dialogShopCar = DialogShopCar(this@OrderGoodsDetailActivity)
                dialogShopCar?.initCls1Adapter(
                    orderDetail?.merchant?.head_pic!!,
                    orderDetail?.merchant?.title!!, list!!
                )
                dialogShopCar?.setOnConfirmListener {
                    if (it == 1) {
                        val intent =
                            Intent(this@OrderGoodsDetailActivity, OrderConfirmActivity::class.java)
                        intent.putExtra("merchantId", merchantId)
                        startActivity(intent)
                    } else {
                        mViewModel.removeShopping(merchantId, dialogShopCar?.opId.toString())
                    }
                }
                dialogShopCar?.setOnConfirmListener(object : DialogShopCar.OnConfirmNumListener {
                    override fun onConfirm(id: Int, num: Int, callback: (Boolean) -> Unit) {
                        mViewModel.editShopping(id, num) {
                            callback(it)
                        }
                    }

                })
                dialogShopCar?.show()
            }

            ivCollect.setOnClickListener {//收藏
                if (orderDetail == null) {
                    return@setOnClickListener
                }
                if (mViewModel.collectStatus.value!= "0") {
                    mViewModel.removeFavorites(mViewModel.collectStatus.value!!)
                } else {
                    mViewModel.saveFavorites(goodsId, orderDetail?.title!!)
                }
            }
            tvCouponName.setOnClickListener {//优惠券
                startActivity(
                    Intent(
                        this@OrderGoodsDetailActivity,
                        OrderCouponActivity::class.java
                    )
                )
            }
            tvShopName.setOnClickListener {//商铺详情
                if (merchantId == 0) {
                    return@setOnClickListener
                }
                val intent =
                    Intent(this@OrderGoodsDetailActivity, OrderShopDetailActivity::class.java)
                intent.putExtra("merchantId", merchantId)
                startActivity(intent)

            }

            tvCommentName.setOnClickListener {//评论
                if (goodsId == 0) {
                    return@setOnClickListener
                }
                val intent =
                    Intent(this@OrderGoodsDetailActivity, OrderGoodsCommentActivity::class.java)
                intent.putExtra("goodsId", goodsId)
                intent.putExtra("merchantId", merchantId)
                startActivity(intent)
            }

            val l: View.OnClickListener = object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    if (clsGoodsBean == null) {
                        return
                    }
                    val dialogChooseGoodsType = DialogChooseGoodsType(this@OrderGoodsDetailActivity)
                    dialogChooseGoodsType.show()
                    dialogChooseGoodsType.apply {
                        setUiData(clsGoodsBean!!)
                        setOnConfirmListener(object :
                            DialogChooseGoodsType.OnAddShopCarResultListener {
                            override fun onResult(type: Int, ids: String, num: Int) {
                                if (type == 1) {//购物车
                                    mViewModel.saveShopping(goodsId, merchantId, ids, num.toString())
                                } else {
                                    val intent =
                                        Intent(
                                            this@OrderGoodsDetailActivity,
                                            OrderConfirmActivity::class.java
                                        )
                                    intent.putExtra("goodsId", goodsId)
                                    intent.putExtra("merchantId", merchantId)
                                    intent.putExtra("ids", ids)
                                    intent.putExtra("num", num)
                                    startActivity(intent)
                                }
                            }
                        })
                    }
                }
            }
            tvAddShopCar.setOnClickListener(l) // 加入购物车
            tvNowBuy.setOnClickListener(l)//立即购买

            headerLayout.ivMore.setOnClickListener {
                if (orderDetail == null) {
                    return@setOnClickListener
                }
                val popupGoodsMenu = PopupGoodsMenu(this@OrderGoodsDetailActivity)
                popupGoodsMenu.setOnConfirmListener {
                    if (it == 1) {
                        val share = orderDetail?.share
                        XPopup.Builder(this@OrderGoodsDetailActivity)
                            .asCustom(ShareDialog(this@OrderGoodsDetailActivity) { dialog, shareBean ->
                                lifecycleScope.launch {
                                    kotlin.runCatching {
                                        withContext(Dispatchers.IO) {
                                            Util.getUrlBitmap(
                                                share?.imgUrl,
//                                                "https://t12.baidu.com/it/u=984106723,176204573&fm=30&app=106&f=JPEG?w=312&h=208&s=0151AB66C6081157CB40B48A03008092",
                                                32
                                            )
                                        }
                                    }.onSuccess { it1 ->

                                        ShareUtil.shareWebUrl(
                                            this@OrderGoodsDetailActivity,
                                            shareBean.shareType,
                                            share?.title!!,
                                            share.desc,
                                            it1,
                                            share.link
                                        )

                                    }.onFailure {
//                                        LogUtils.v(it.localizedMessage)
                                    }
                                }
                            })
                            .show()
                    } else {
                        val intent =
                            Intent(
                                this@OrderGoodsDetailActivity,
                                OrderCommodityComplaintActivity::class.java
                            )
                        intent.putExtra("mertId", merchantId)
                        intent.putExtra("goodsId", goodsId)
                        intent.putExtra("goodsTitle", orderDetail?.title)
                        intent.putExtra("goodsIcon", orderDetail?.cover)
                        intent.putExtra("shopPrice", orderDetail?.price)
                        startActivity(intent)
                    }
                }
                popupGoodsMenu.showAsDropDown(it)
            }

            headerLayout.getmToolbar().setNavigationOnClickListener { finish() }


            ImmersionBar.with(this@OrderGoodsDetailActivity)
                .statusBarColor(R.color.colorMain)
                .init()
//            headerLayout.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _,
//                                                                                                  verticalOffset ->
//                if (verticalOffset < 0) {
//                    headerLayout.getmToolbar().setBackgroundColor(
//                        ContextCompat.getColor(this@OrderGoodsDetailActivity, R.color.colorMain)
//                    )
//                } else {
////                    headerLayout.getmToolbar().setBackgroundColor(
////                        ContextCompat.getColor(
////                            this@OrderGoodsDetailActivity,
////                            R.color.transparent
////                        )
////                    )
////                    ImmersionBar.with(this@OrderGoodsDetailActivity)
////                        .transparentStatusBar()
////                        .init()
//                }
//            })

        }
        initAdapter()
        LiveEventBus.get<Int>(EVENT_BUS_KEY_SAVE_SHOPPING_SUCCESS).observe(this) {
            mViewModel.getShopping(merchantId)
        }
    }



    override fun onResume() {
        super.onResume()
        mDataBinding.headerLayout.banner.start()
    }

    override fun onPause() {
        super.onPause()
        mDataBinding.headerLayout.banner.pause()
    }

    override fun loadData() {
        goodsId = intent.getIntExtra("goodsId", 0)
        mViewModel.licenseDetail(goodsId)
    }

    private var orderDetail: GoodsDetailInfo? = null

    private val tagAdapter:TagAdapter by lazy {
        TagAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun createObserver() {
        mViewModel.apply {
            //购物车
            shopCarBeanInfo.observe(this@OrderGoodsDetailActivity) {
                shopCarData = it
                if (it.total > 0) {
                    mDataBinding.tvShopNum.text = it.total.toString()
                    mDataBinding.tvShopNum.visibility = View.VISIBLE
                    if (dialogShopCar != null && dialogShopCar?.isShowing!!) {
                        dialogShopCar?.initCls1Adapter(
                            orderDetail?.merchant?.head_pic!!,
                            orderDetail?.merchant?.title!!, it.list
                        )
                    }
                } else {
                    if (dialogShopCar != null && dialogShopCar?.isShowing!!) {
                        dialogShopCar?.dismiss()
                    }
                    mDataBinding.tvShopNum.visibility = View.INVISIBLE
                }
            }
            collectStatus.observe(this@OrderGoodsDetailActivity){
                if(it=="0"){
                    mDataBinding.ivCollect.compoundDrawableTintList = null
                }else{
                    mDataBinding.ivCollect.compoundDrawableTintList =
                        ColorStateList.valueOf(Color.parseColor("#FFAA32"))
                }
            }
//            collectMsg.observe(this@OrderGoodsDetailActivity) {
//                mDataBinding.ivCollect.compoundDrawableTintList =
//                    ColorStateList.valueOf(Color.parseColor("#FFAA32"))
//            }
//            collectRemoveMsg.observe(this@OrderGoodsDetailActivity) {
//                mDataBinding.ivCollect.compoundDrawableTintList = null
//            }
            mDataBinding.rvTag.adapter =tagAdapter
            //伤品详情
            goodsDetailInfo.observe(this@OrderGoodsDetailActivity) {
                BannerViewHolder.setPageItem(
                    this@OrderGoodsDetailActivity,
                    mDataBinding.headerLayout.banner, it.slider
                )
                orderDetail = it
                clsGoodsBean = ClsGoodsBean()
                clsGoodsBean?.price = it.price
                clsGoodsBean?.spec_attribute = it.spec_attribute
                clsGoodsBean?.cover = if (it.slider == null) "" else it.slider[0]
                mDataBinding.headerLayout.banner.start()
                merchantId = it.merchant_id
                mViewModel.getShopping(merchantId)
                ImageUtils.loadImage(it.merchant.head_pic, mDataBinding.ivGoodsCover)

                mDataBinding.tvNum.text = it.stock_sales.toString()

                mDataBinding.tvSinglePrice.text = it.price
                mDataBinding.tvGoodsName.text = it.title
                mDataBinding.tvContent.text = it.remark
                mDataBinding.tvShopName.text = it.merchant.title
                mDataBinding.tvCommentName.text = "共有${it.evaluate_count}个消费评价"
                tagAdapter.setNewInstance(it.payment_data.take(3).toMutableList())
//                mDataBinding.llTag.addView(initTagView(it.refund))
//                mDataBinding.llTag.addView(initTagView(it.payment))
                val evaluate = it.evaluate
                if (evaluate != null && evaluate.size > 0) {
                    mDataBinding.recyclerView.visibility = View.VISIBLE
                    adapter.setNewInstance(evaluate as MutableList<EvaluateBean>)
                } else {
                    mDataBinding.recyclerView.visibility = View.GONE
                }
            }
        }
    }

    val adapter = object :
        BaseQuickAdapter<EvaluateBean, BaseDataBindingHolder<OrderItemGoodsCommentListBinding>>(
            R.layout.order_item_goods_comment_list
        ) {
        override fun convert(
            holder: BaseDataBindingHolder<OrderItemGoodsCommentListBinding>,
            item: EvaluateBean,
        ) {
            holder.dataBinding?.apply {
                if (item.user != null) {
                    ImageUtils.loadImage(item.user.headimg, ivHead)
                    tvName.text = item.user.nickname
                }
                tvTime.text = item.create_at
                tvContent.text = item.content
            }
        }
    }


    private fun initAdapter() {
        mDataBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@OrderGoodsDetailActivity)
            recyclerView.adapter = adapter
        }
    }

    private fun initTagView(text: String): View {
        val tagView = LayoutInflater.from(this).inflate(R.layout.order_item_tag, null)
        val tvTagName = tagView.findViewById<TextView>(R.id.tvTagName)
        tvTagName.text = text
        return tagView
    }

}