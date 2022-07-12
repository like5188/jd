package com.cq.jd.order

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.dialog.ShareDialog
import com.common.library.router.ARouterPath
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.Util
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.activity.comment.PageAdapter
import com.cq.jd.order.activity.complaint.OrderShopComplaintActivity
import com.cq.jd.order.activity.goods.OrderConfirmActivity
import com.cq.jd.order.activity.goods.OrderGoodsDetailActivity
import com.cq.jd.order.activity.goods.OrderSearchActivity
import com.cq.jd.order.activity.goods.fragment.OrderGoodsClsFragment
import com.cq.jd.order.activity.shop.OrderShopCertificateActivity
import com.cq.jd.order.databinding.OrderActivityMainBinding
import com.cq.jd.order.databinding.OrderItemRecommendBinding
import com.cq.jd.order.dialog.DialogShopCar
import com.cq.jd.order.dialog.PopupGoodsMenu
import com.cq.jd.order.entities.Recommend
import com.cq.jd.order.entities.ShopCarListBean
import com.cq.jd.order.entities.ShopDetailBean
import com.cq.jd.order.entities.event.IndexRefreshEventEvent
import com.cq.jd.order.util.EVENT_BUS_KEY_SAVE_SHOPPING_SUCCESS
import com.cq.jd.share.ShareUtil
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.xpopup.XPopup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus


@Route(path = ARouterPath.Order.ORDER_HOME)
class OrderMainActivity :
    BaseVmActivity<OrderMainModel, OrderActivityMainBinding>(R.layout.order_activity_main) {

    private var indexId = 39
    private var shopCarData: ShopCarListBean? = null
    private var dialogShopCar: DialogShopCar? = null

   companion object{
        var shopDetailBean: ShopDetailBean? = null
   }

    private val recommendAdapter = object :
        BaseQuickAdapter<Recommend, BaseDataBindingHolder<OrderItemRecommendBinding>>(R.layout.order_item_recommend) {
        override fun convert(
            holder: BaseDataBindingHolder<OrderItemRecommendBinding>,
            item: Recommend,
        ) {
            holder.dataBinding?.apply {
                ImageUtils.loadImage(item.cover, ivRecommendCover)
                tvDes.text = item.remark
                tvTitle.text = item.title
                tvJob.text = item.refund
            }
        }
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
//        window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#32527BA4")))
        if (intent.hasExtra("merchantId")) {
            indexId = intent.getIntExtra("merchantId", 0)
        }
        mDataBinding.apply {
            mCoordinatorTabLayout.setTranslucentStatusBar(this@OrderMainActivity)
                .setTitle("")
                .setBackEnable(true)
                .setupWithViewPager(viewPager)

            mCoordinatorTabLayout.llSearch.setOnClickListener {//搜索
                if (shopDetailBean == null) {
                    return@setOnClickListener
                }
                val intent = Intent(this@OrderMainActivity, OrderSearchActivity::class.java)
                intent.putExtra("shopDetailBean", shopDetailBean)
                startActivity(intent)
            }


            mCoordinatorTabLayout.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _,
                                                                                                           verticalOffset ->
                if (verticalOffset < 0) {
                    mCoordinatorTabLayout.getmToolbar().setBackgroundColor(
                        ContextCompat.getColor(this@OrderMainActivity, R.color.colorMain)
                    )

                    ImmersionBar.with(this@OrderMainActivity)
                        .statusBarColor(R.color.colorMain)
                        .init()
                    //                    mCoordinatorTabLayout.getmToolbar().visibility = View.VISIBLE
                } else {
                    mCoordinatorTabLayout.getmToolbar().setBackgroundColor(
                        ContextCompat.getColor(this@OrderMainActivity, R.color.transparent)
                    )
                    ImmersionBar.with(this@OrderMainActivity)
                        .transparentStatusBar()
                        .init()
//                    mCoordinatorTabLayout.getmToolbar().visibility = View.INVISIBLE
                }
            })
            llShopCar.setOnClickListener {
                if (shopCarData == null) {
                    return@setOnClickListener
                }
                if (shopCarData?.list == null || shopCarData?.list!!.size == 0) {
                    ToastUtils.showShort("购物车空空如也，去挑选您喜欢的商品吧")
                    return@setOnClickListener
                }
                val list = shopCarData?.list
                dialogShopCar = DialogShopCar(this@OrderMainActivity)
                dialogShopCar?.initCls1Adapter(shopDetailBean!!, list!!)
                dialogShopCar?.setOnConfirmListener {
                    if (it == 1) {
                        val intent =
                            Intent(this@OrderMainActivity, OrderConfirmActivity::class.java)
                        intent.putExtra("merchantId", indexId)
                        startActivity(intent)
                    } else {
                        mViewModel.removeShopping(indexId, dialogShopCar?.opId.toString())
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

            mCoordinatorTabLayout.getmToolbar().setNavigationOnClickListener { finish() }

            mCoordinatorTabLayout.ivMore.setOnClickListener { it1 ->
                if (shopDetailBean == null) {
                    return@setOnClickListener
                }
                val popupGoodsMenu = PopupGoodsMenu(this@OrderMainActivity)
                popupGoodsMenu.setOnConfirmListener {
                    if (it == 1) {//分享
                        val share = shopDetailBean?.share
                        XPopup.Builder(this@OrderMainActivity)
                            .asCustom(ShareDialog(this@OrderMainActivity) { dialog, shareBean ->
                                lifecycleScope.launch {
                                    kotlin.runCatching {
                                        withContext(Dispatchers.IO) {
                                            Util.getUrlBitmap(
                                                share?.imgUrl,
                                                32
                                            )
                                        }
                                    }.onSuccess { it1 ->

                                        ShareUtil.shareWebUrl(
                                            this@OrderMainActivity,
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
                            Intent(this@OrderMainActivity, OrderShopComplaintActivity::class.java)
                        intent.putExtra("mertId", indexId)
                        intent.putExtra("shopIcon", shopDetailBean?.head_pic)
                        intent.putExtra("shopTitle", shopDetailBean?.title)
                        startActivity(intent)
                    }
                }
                popupGoodsMenu.showAsDropDown(it1)
            }


            mDataBinding.mCoordinatorTabLayout.ivCollect.setOnClickListener {
                if (shopDetailBean == null) {
                    return@setOnClickListener
                }
                if (mViewModel.collectStatus.value != "0") {
                    mViewModel.removeFavorites(mViewModel.collectStatus.value!!)
                } else {
                    mViewModel.saveFavorites(indexId, shopDetailBean?.title!!)
                }
            }
        }
        initRecommendAdapter()

        LiveEventBus.get<Int>(EVENT_BUS_KEY_SAVE_SHOPPING_SUCCESS).observe(this) {
            mViewModel.getShopping(indexId)
        }
    }

    override fun loadData() {
        mViewModel.shopDetail(indexId)
        mViewModel.getShopping(indexId)
        mViewModel.merchantClassify(indexId)
    }

    override fun createObserver() {
        mViewModel.apply {
            //收藏
            collectStatus.observe(this@OrderMainActivity) {
                if (it != "0") {
                    mDataBinding.mCoordinatorTabLayout.ivCollect.imageTintList =
                        ColorStateList.valueOf(Color.parseColor("#FFAA32"))
                } else {
                    mDataBinding.mCoordinatorTabLayout.ivCollect.imageTintList = null
                }
            }
            //购物车
            shopCarBeanInfo.observe(this@OrderMainActivity) {
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
            //订单详情
            shopDetailInfo.observe(this@OrderMainActivity) {
                shopDetailBean = it
                //头部信息
                if (!it.environment_pic.isNullOrEmpty()) {
                    ImageUtils.loadImage(
                        it.environment_pic[0],
                        mDataBinding.mCoordinatorTabLayout.imageView
                    )
                }
                ImageUtils.loadImage(it.head_pic, mDataBinding.mCoordinatorTabLayout.ivLogo)
//                ImageUtils.loadImage(it.head_pic, mDataBinding.mCoordinatorTabLayout.ivLogo)
                mDataBinding.mCoordinatorTabLayout.tvName.text = it.title
                mDataBinding.mCoordinatorTabLayout.tvDistance.text = "${it.distance / 1000}km"
                mDataBinding.mCoordinatorTabLayout.tvYyTime.text = it.operation_at.toString()
                mDataBinding.mCoordinatorTabLayout.tvNum.text = "销量${it.sales_volume}"
                mDataBinding.mCoordinatorTabLayout.getsRating().grade = it.evaluate_score
                mDataBinding.mCoordinatorTabLayout.tvSinglePrice.text = "${it.average}/人"
                if (it.favorites != "0") {
                    mDataBinding.mCoordinatorTabLayout.ivCollect.imageTintList =
                        ColorStateList.valueOf(Color.parseColor("#FFAA32"))
                } else {
                    mDataBinding.mCoordinatorTabLayout.ivCollect.imageTintList = null
                }
                mDataBinding.mCoordinatorTabLayout.llTag.addView(initTagView("随时退"))

                // 推荐信息
                val recommend = it.recommend
                if (recommend != null && recommend.size > 0) {
                    val rvRecommend = mDataBinding.mCoordinatorTabLayout.recyclerViewRecommend
                    rvRecommend.visibility = View.VISIBLE
                    recommendAdapter.setNewInstance(recommend as MutableList<Recommend>)
                } else {
                    mDataBinding.mCoordinatorTabLayout.recyclerViewRecommend.visibility = View.GONE
                }
            }
            listClsGoods.observe(this@OrderMainActivity) {
                //分类商品
                val classify = it
                if (classify != null && classify.size > 0) {
                    val size = classify.size
                    val arrayList = ArrayList<Fragment>()
                    val titles = ArrayList<String>()
                    for (i in 0 until size) {
                        val orderGoodsClsFragment = OrderGoodsClsFragment()
                        val bundle = Bundle()
                        bundle.putInt("idIndex", classify[i].id)
                        bundle.putInt("merchantId", indexId)
                        orderGoodsClsFragment.arguments = bundle
                        arrayList.add(orderGoodsClsFragment)
                        titles.add(classify[i].title)
                    }
                    val pagerAdapter = PageAdapter(supportFragmentManager, titles, arrayList)
                    mDataBinding.viewPager.adapter = pagerAdapter
                    mDataBinding.mCoordinatorTabLayout.setupWithViewPager(mDataBinding.viewPager)
                    for (i in 0 until mDataBinding.mCoordinatorTabLayout.tabLayout.tabCount) {
                        val view = LayoutInflater.from(this@OrderMainActivity).inflate(
                            R.layout.order_comment_top_item, null
                        )
                        val tv = view.findViewById<TextView>(R.id.tv_top_item)
                        tv.text = titles[i]
                        tv.setTextColor(Color.parseColor("#13202D"))
                        if (i == 0) {
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) //直接用setTextSize(22)也一样
                        } else {
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) //直接用setTextSize(22)也一样
                        }

                        mDataBinding.mCoordinatorTabLayout.tabLayout.getTabAt(i)?.customView = view
                    }
                    mDataBinding.mCoordinatorTabLayout.tabLayout.addOnTabSelectedListener(object :
                        TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            if (tab!!.customView != null) {
                                val tv: TextView = tab.customView!!.findViewById(R.id.tv_top_item)
                                tv.setTextSize(
                                    TypedValue.COMPLEX_UNIT_SP,
                                    20f
                                ) //直接用setTextSize(22)也一样
                                EventBus.getDefault()
                                    .post(IndexRefreshEventEvent(classify[tab.position].id))
                            }
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                            if (tab!!.customView != null) {
                                val tv: TextView = tab.customView!!.findViewById(R.id.tv_top_item)
                                tv.setTextSize(
                                    TypedValue.COMPLEX_UNIT_SP,
                                    14f
                                ) //直接用setTextSize(22)也一样
                            }
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {

                        }

                    })
                }
            }
        }
        mDataBinding.mCoordinatorTabLayout.tvZz.setOnClickListener {
            val intent = Intent(this, OrderShopCertificateActivity::class.java)
            intent.putExtra("indexId", indexId)
            startActivity(intent)
        }
    }

    private fun initRecommendAdapter() {//推荐shangpin
        mDataBinding.apply {
            val linearLayoutManager = LinearLayoutManager(this@OrderMainActivity)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            mCoordinatorTabLayout.recyclerViewRecommend.layoutManager = linearLayoutManager
            mCoordinatorTabLayout.recyclerViewRecommend.adapter = recommendAdapter
        }
        recommendAdapter.setOnItemClickListener { _, _, position ->
            val item = recommendAdapter.getItem(position)
            if (shopDetailBean == null) {
                return@setOnItemClickListener
            }

            val intent = Intent(this, OrderGoodsDetailActivity::class.java)
            intent.putExtra("goodsId", item.id)
            intent.putExtra("shopDetailBean", shopDetailBean)
            intent.putExtra("merchant_id", item.merchant_id)
            startActivity(intent)
        }
    }

    private fun initTagView(text: String): View {
        val tagView = LayoutInflater.from(this).inflate(R.layout.order_item_tag, null)
        val tvTagName = tagView.findViewById<TextView>(R.id.tvTagName)
        tvTagName.text = text
        return tagView
    }


}