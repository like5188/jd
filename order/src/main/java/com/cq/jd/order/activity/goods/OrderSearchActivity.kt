package com.cq.jd.order.activity.goods

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
import com.cq.jd.order.activity.complaint.OrderShopComplaintActivity
import com.cq.jd.order.databinding.OrderActivitySearchBinding
import com.cq.jd.order.databinding.OrderItemGoodsListBinding
import com.cq.jd.order.dialog.DialogChooseGoodsType
import com.cq.jd.order.dialog.DialogShopCar
import com.cq.jd.order.dialog.PopupGoodsMenu
import com.cq.jd.order.entities.ClsGoodsBean
import com.cq.jd.order.entities.ShopCarListBean
import com.cq.jd.order.entities.ShopDetailBean
import com.cq.jd.share.ShareUtil
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderSearchActivity :
    BaseVmActivity<OrderSearchModel, OrderActivitySearchBinding>(R.layout.order_activity_search) {

    private var shopDetailBean: ShopDetailBean? = null
    private var indexId = 0
    private var page = 1
    private var shopCarData: ShopCarListBean? = null
    private var dialogShopCar: DialogShopCar? = null

    private var currenSize = 0
    private val adapter = object :
        BaseQuickAdapter<ClsGoodsBean, BaseDataBindingHolder<OrderItemGoodsListBinding>>(R.layout.order_item_goods_list) {
        init {
            addChildClickViewIds(R.id.tvWaitUse)
        }


        override fun convert(
            holder: BaseDataBindingHolder<OrderItemGoodsListBinding>,
            item: ClsGoodsBean,
        ) {
            holder.dataBinding?.apply {
                ImageUtils.loadImage(item.cover, ivLogo)
                tvTitle.text = item.title
                tvSinglePrice.text = item.price
                tvNum.text = item.stock_sales.toString()
            }
        }
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        shopDetailBean = intent.getSerializableExtra("shopDetailBean") as ShopDetailBean?
        indexId = shopDetailBean?.id!!
        mDataBinding.apply {
            mCoordinatorTabLayout.setTranslucentStatusBar(this@OrderSearchActivity)
                .setTitle("")
                .setBackEnable(true)

            mCoordinatorTabLayout.getmToolbar().setNavigationOnClickListener { finish() }

            mCoordinatorTabLayout.ivMore.setOnClickListener { it1 ->
                if (shopDetailBean == null) {
                    return@setOnClickListener
                }
                val popupGoodsMenu = PopupGoodsMenu(this@OrderSearchActivity)
                popupGoodsMenu.setOnConfirmListener {
                    if (it == 1) {
                        val share = shopDetailBean?.share
                        XPopup.Builder(this@OrderSearchActivity)
                            .asCustom(ShareDialog(this@OrderSearchActivity) { dialog, shareBean ->
                                lifecycleScope.launch {
                                    kotlin.runCatching {
                                        withContext(Dispatchers.IO) {
                                            Util.getUrlBitmap(
                                                share?.imgUrl,
                                                32
                                            )
                                        }
                                    }.onSuccess {it1->

                                        ShareUtil.shareWebUrl(
                                            this@OrderSearchActivity,
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
                            Intent(this@OrderSearchActivity, OrderShopComplaintActivity::class.java)
                        intent.putExtra("mertId", indexId)
                        intent.putExtra("shopIcon", shopDetailBean?.head_pic)
                        intent.putExtra("shopTitle", shopDetailBean?.title)
                        startActivity(intent)
                    }
                }
                popupGoodsMenu.showAsDropDown(it1)
            }

            mDataBinding.mCoordinatorTabLayout.ivCollect.setOnClickListener {
                if (shopDetailBean == null){
                    return@setOnClickListener
                }
                if (shopDetailBean?.is_favorites ==1){
                    mViewModel.removeFavorites(indexId)
                }else{
                    mViewModel.saveFavorites(indexId, shopDetailBean?.title!!)
                }
            }

            mCoordinatorTabLayout.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _,
                                                                                                           verticalOffset ->
                if (verticalOffset < 0) {
                    mCoordinatorTabLayout.getmToolbar().setBackgroundColor(
                        ContextCompat.getColor(this@OrderSearchActivity, R.color.colorMain)
                    )

                    ImmersionBar.with(this@OrderSearchActivity)
                        .statusBarColor(R.color.colorMain)
                        .init()
                } else {
                    mCoordinatorTabLayout.getmToolbar().setBackgroundColor(
                        ContextCompat.getColor(this@OrderSearchActivity, R.color.transparent)
                    )
                    ImmersionBar.with(this@OrderSearchActivity)
                        .transparentStatusBar()
                        .init()
                }
            })

            shopDetailBean.apply {
                //头部信息
                ImageUtils.loadImage(this!!.head_pic, mDataBinding.mCoordinatorTabLayout.imageView)
                ImageUtils.loadImage(this.logo, mDataBinding.mCoordinatorTabLayout.ivLogo)
                mCoordinatorTabLayout.tvName.text = this.title
                mCoordinatorTabLayout.tvDistance.text = "${this.distance / 1000}km"
                mCoordinatorTabLayout.tvYyTime.text = this.operation_at.toString()
                mCoordinatorTabLayout.tvNum.text = "月销量${this.sales_volume}"
                mCoordinatorTabLayout.getsRating().grade = this.evaluate_score
                mCoordinatorTabLayout.tvSinglePrice.text = "${this.average}/人"
                if (this.is_favorites == 1) {
                    mCoordinatorTabLayout.ivCollect.imageTintList =
                        ColorStateList.valueOf(Color.parseColor("#FFAA32"))
                } else {
                    mCoordinatorTabLayout.ivCollect.imageTintList = null
                }
                mCoordinatorTabLayout.llTag.addView(initTagView("随时退"))
            }

            mCoordinatorTabLayout.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    page = 1
                    if (!TextUtils.isEmpty(p0.toString())) {
                        mViewModel.goodsList(p0.toString(), indexId, 1)
                    }
                }

            })

            smartRefresh.setOnLoadMoreListener {
                val key = mCoordinatorTabLayout.etSearch.text.toString()
                if (!TextUtils.isEmpty(key)) {
                    page++
                    mViewModel.goodsList(key, indexId, page)
                    it.finishLoadMore()
                }
            }

            llShopCar.setOnClickListener {
                if (shopCarData == null) {
                    return@setOnClickListener
                }
                if (shopCarData?.list == null || shopCarData?.list!!.size == 0) {
                    ToastUtils.showShort("购物车空空如也，去挑选您喜欢的商品吧")
                    return@setOnClickListener
                }
                val list = shopCarData?.list
                dialogShopCar = DialogShopCar(this@OrderSearchActivity)
                dialogShopCar?.initCls1Adapter(shopDetailBean!!, list!!)
                dialogShopCar?.setOnConfirmListener {
                    if (it == 1) {
                        val intent =
                            Intent(this@OrderSearchActivity, OrderConfirmActivity::class.java)
                        intent.putExtra("merchantId", indexId)
                        startActivity(intent)
                    } else {
                        mViewModel.removeShopping(indexId, dialogShopCar?.opId.toString())
                    }
                }
                dialogShopCar?.setOnConfirmListener(object : DialogShopCar.OnConfirmNumListener {
                    override fun onConfirm(id: Int, num: Int, callback:(Boolean)->Unit) {
                        mViewModel.editShopping(id, num){
                            callback(it)
                        }
                    }

                })
                dialogShopCar?.show()
            }

        }

        initAdapter()

    }

    private fun initAdapter() {
        adapter
        mDataBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@OrderSearchActivity)
            recyclerView.adapter = adapter
        }
        adapter.setOnItemClickListener { _, _, position ->
            val intent = Intent(this, OrderGoodsDetailActivity::class.java)
            intent.putExtra("goodsId", adapter.getItem(position).id)
            intent.putExtra("merchant_id", adapter.getItem(position).merchant_id)
            intent.putExtra("merchant_id", adapter.getItem(position).merchant_id)
            startActivity(intent)
        }
        adapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.tvWaitUse) {
                val item = adapter.getItem(position)
                val merchantId = item.merchant_id
                val dialogChooseGoodsType = DialogChooseGoodsType(this)
                dialogChooseGoodsType.setUiData(item)
                dialogChooseGoodsType.setOnConfirmListener(object :
                    DialogChooseGoodsType.OnAddShopCarResultListener {
                    override fun onResult(type: Int, ids: String, num: Int) {
                        if (type == 1) {
                            mViewModel.saveShopping(item.id,merchantId,ids,num.toString())
                        }else{
                            val intent =
                                Intent(
                                    this@OrderSearchActivity,
                                    OrderConfirmActivity::class.java
                                )
                            intent.putExtra("goodsId", item.id)
                            intent.putExtra("merchantId", merchantId)
                            intent.putExtra("ids", ids)
                            intent.putExtra("num", num)
                            startActivity(intent)
                        }
                    }

                })
                dialogChooseGoodsType.show()
            }
        }
    }

    override fun loadData() {
        mViewModel.getShopping(indexId)
    }

    override fun createObserver() {

        mViewModel.apply {
            collectMsg.observe(this@OrderSearchActivity) {
                mDataBinding.mCoordinatorTabLayout.ivCollect.imageTintList =
                    ColorStateList.valueOf(Color.parseColor("#FFAA32"))
            }
            collectRemoveMsg.observe(this@OrderSearchActivity) {
                mDataBinding.mCoordinatorTabLayout.ivCollect.imageTintList = null
            }
            //伤品搜素
            listClsGoods.observe(this@OrderSearchActivity) {
                if (it != null && it.size > 0) {
                    if (page == 1) {
                        adapter.setNewInstance(it as MutableList<ClsGoodsBean>)
                    } else {
                        adapter.addData(it)
                    }
                    currenSize += it.size
                } else {

                }
            }
            //购物车
            shopCarBeanInfo.observe(this@OrderSearchActivity) {
                shopCarData = it
                if (it.total > 0) {
                    mDataBinding.tvShopNum.text = it.total.toString()
                    mDataBinding.tvShopNum.visibility = View.VISIBLE
                    if (dialogShopCar != null && dialogShopCar?.isShowing!!) {
                        dialogShopCar?.initCls1Adapter(shopDetailBean!!, it.list)
                    }
                } else {
                    if (dialogShopCar != null && dialogShopCar?.isShowing!!) {
                        dialogShopCar?.dismiss()
                    }
                    mDataBinding.tvShopNum.visibility = View.INVISIBLE
                }
            }
        }
    }


    private fun initTagView(text: String): View {
        val tagView = LayoutInflater.from(this).inflate(R.layout.order_item_tag, null)
        val tvTagName = tagView.findViewById<TextView>(R.id.tvTagName)
        tvTagName.text = text
        return tagView
    }


}