package com.cq.jd.order.activity.goods.fragment

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.ui.activity.BaseVmFragment
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.OrderMainActivity
import com.cq.jd.order.R
import com.cq.jd.order.activity.goods.OrderConfirmActivity
import com.cq.jd.order.activity.goods.OrderGoodsDetailActivity
import com.cq.jd.order.databinding.OrderFragmentGoodsClsBinding
import com.cq.jd.order.databinding.OrderItemGoodsListBinding
import com.cq.jd.order.dialog.DialogChooseGoodsType
import com.cq.jd.order.entities.ClsGoodsBean
import com.cq.jd.order.entities.event.IndexRefreshEventEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrderGoodsClsFragment :
    BaseVmFragment<OrderGoodsClsModel, OrderFragmentGoodsClsBinding>(R.layout.order_fragment_goods_cls) {


    private var idIndex = 0
    private var merchantId = 0
    private var page = 1
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        mViewBinding.model = mViewModal
        mViewBinding.apply {
            smartRefresh.setOnLoadMoreListener {
                page++
                mViewModal.goodsList(idIndex, merchantId, page)
                it.finishLoadMore()
            }
        }
        initAdapter()
    }


    override fun loadData() {
        page = 1
        currenSize = 0
        idIndex = arguments?.getInt("idIndex")!!
        merchantId = arguments?.getInt("merchantId")!!
        mViewModal.goodsList(idIndex, merchantId, page)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onIndexRefreshEvent(event: IndexRefreshEventEvent) {
        if (event.idIndex == arguments?.getInt("idIndex")) {
            currenSize = 0
            page = 1
            mViewModal.goodsList(idIndex, merchantId, page)
        }
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {
        mViewModal.apply {
            listClsGoods.observe(requireActivity()) {
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
        }
    }

    private fun initAdapter() {
        adapter
        mViewBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        adapter.setOnItemClickListener { _, _, position ->
            val intent = Intent(requireContext(), OrderGoodsDetailActivity::class.java)
            OrderMainActivity.shopDetailBean?.let {
                intent.putExtra("shopDetailBean", it)
            }
            intent.putExtra("goodsId",adapter.getItem(position).id)
            intent.putExtra("merchant_id",adapter.getItem(position).merchant_id)
            intent.putExtra("merchant_id",adapter.getItem(position).merchant_id)
            startActivity(intent)
        }
        adapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.tvWaitUse) {
                val item = adapter.getItem(position)
                val merchantId = item.merchant_id
                val dialogChooseGoodsType = DialogChooseGoodsType(requireContext())
                dialogChooseGoodsType.setUiData(item)
                dialogChooseGoodsType.setOnConfirmListener(object :
                    DialogChooseGoodsType.OnAddShopCarResultListener {
                    override fun onResult(type: Int, ids: String, num: Int) {
                        if (type == 1) {
                            mViewModal.saveShopping(item.id,merchantId,ids,num.toString())
                        }else{
                            val intent =
                                Intent(
                                    requireContext(),
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}