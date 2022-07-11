package com.cq.jd.order.dialog

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.library.dialog.BaseDialog
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.entities.ShopCarInfo
import com.cq.jd.order.entities.ShopDetailBean

class DialogShopCar(context: Context) : BaseDialog(context) {

    private var tvTotalNum1: TextView? = null
    private var tvTotalPrice: TextView? = null
    private var tvTotalNum: TextView? = null
    private var tvShopName: TextView? = null
    private var ivGoodsCover: ImageView? = null
    private var recyclerViewShopCar: RecyclerView? = null
    private var tvNowBuy: TextView? = null
    var opId = 0

    private  var onConfirmNumListener: OnConfirmNumListener? = null

     fun setOnConfirmListener(onConfirmNumListener: OnConfirmNumListener?) {
        this.onConfirmNumListener = onConfirmNumListener
    }


    override fun layoutResId() = R.layout.order_dialog_shop_car

    override fun getHeightPx() = context.resources.displayMetrics.heightPixels * 2 / 3

    override fun gravity() = Gravity.BOTTOM

    override fun cancelAble() = true

    override fun initView(context: Context?) {
        tvTotalNum1 = findViewById(R.id.tvTotalNum1)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        tvShopName = findViewById(R.id.tvShopName)
        tvTotalNum = findViewById(R.id.tvTotalNum)
        ivGoodsCover = findViewById(R.id.ivGoodsCover)
        recyclerViewShopCar = findViewById(R.id.recyclerViewShopCar)
        tvNowBuy = findViewById(R.id.tvNowBuy)
        recyclerViewShopCar?.layoutManager = LinearLayoutManager(context)
    }

    override fun initListener() {
        //onConfirmListener 事件说明  1 立即购买
        tvNowBuy?.setOnClickListener {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(1)
            }
        }

    }


    fun initCls1Adapter(shopDetailBean: ShopDetailBean, list: List<ShopCarInfo>) {
        ImageUtils.loadImage(shopDetailBean.head_pic, ivGoodsCover)
        tvShopName?.text = shopDetailBean.title
        tvTotalNum1?.text = "(共${list.size}件商品)"
        tvTotalNum?.text = "共${list.size}件商品"
        updateTotalPrice(list)
        val mTypeAdapter =
            object :
                BaseQuickAdapter<ShopCarInfo, BaseViewHolder>(R.layout.order_item_shop_car_item) {

                init {
                    addChildClickViewIds(R.id.ivClose, R.id.tvSub, R.id.tvAdd)
                }

                override fun convert(holder: BaseViewHolder, item: ShopCarInfo) {
                    val goods = item.goods
                    ImageUtils.loadImage(goods.cover, holder.getView(R.id.ivLogo))
                    holder.setText(R.id.tvTitle, goods.title)
                    holder.setText(R.id.tvSinglePrice, goods.price_pay)
                    holder.setText(R.id.tvNum, item.join_quantity.toString())
                    holder.setText(R.id.tvType, item.spec_attribute_string)
                }

            }
        recyclerViewShopCar?.adapter = mTypeAdapter
        mTypeAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.ivClose) {
                opId = mTypeAdapter.getItem(position).id
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm(0)
                }
            } else if (view.id == R.id.tvSub) {
                if (onConfirmNumListener != null) {
                    val tvSub = mTypeAdapter.getViewByPosition(position, R.id.tvSub)
                    val tvAdd = mTypeAdapter.getViewByPosition(position, R.id.tvAdd)
                    tvSub?.isClickable = false
                    tvAdd?.isClickable = false
                    val item = mTypeAdapter.getItem(position)
                    var pending_join_quantity = item.join_quantity - 1
                    if (pending_join_quantity <= 1) {
                        pending_join_quantity = 1
                    }
                    onConfirmNumListener?.onConfirm(item.id,pending_join_quantity){
                        if (it) {
                            item.join_quantity = pending_join_quantity
                            updateTotalPrice(list)
                        }
                        tvSub?.isClickable = true
                        tvAdd?.isClickable = true
                        mTypeAdapter.notifyDataSetChanged()
                    }
                }
            } else if (view.id == R.id.tvAdd) {
                if (onConfirmNumListener != null) {
                    val tvSub = mTypeAdapter.getViewByPosition(position, R.id.tvSub)
                    val tvAdd = mTypeAdapter.getViewByPosition(position, R.id.tvAdd)
                    tvSub?.isClickable = false
                    tvAdd?.isClickable = false
                    val item = mTypeAdapter.getItem(position)
                    val pending_join_quantity = item.join_quantity + 1
                    onConfirmNumListener?.onConfirm(item.id,pending_join_quantity){
                        if (it) {
                            item.join_quantity = pending_join_quantity
                            updateTotalPrice(list)
                        }
                        tvSub?.isClickable = true
                        tvAdd?.isClickable = true
                        mTypeAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        mTypeAdapter.setNewInstance(list as MutableList<ShopCarInfo>)
    }

    private fun updateTotalPrice(list: List<ShopCarInfo>) {
        var  totalPrice = 0.0
        list.forEach {
            totalPrice += it.goods.price_pay.toDouble() * it.join_quantity
        }
        tvTotalPrice?.text = totalPrice.toString()
    }

    fun initCls1Adapter(head_pic:String,title:String, list: List<ShopCarInfo>) {
        ImageUtils.loadImage(head_pic, ivGoodsCover)
        tvShopName?.text = title
        tvTotalNum1?.text = "(共${list.size}件商品)"
        tvTotalNum?.text = "共${list.size}件商品"
        updateTotalPrice(list)
        val mTypeAdapter =
            object :
                BaseQuickAdapter<ShopCarInfo, BaseViewHolder>(R.layout.order_item_shop_car_item) {

                init {
                    addChildClickViewIds(R.id.ivClose, R.id.tvSub, R.id.tvAdd)
                }

                override fun convert(holder: BaseViewHolder, item: ShopCarInfo) {
                    val goods = item.goods
                    ImageUtils.loadImage(goods.cover, holder.getView(R.id.ivLogo))
                    holder.setText(R.id.tvTitle, goods.title)
                    holder.setText(R.id.tvSinglePrice, goods.price_pay)
                    holder.setText(R.id.tvNum, item.join_quantity.toString())
                    holder.setText(R.id.tvType, item.spec_attribute_string)
                }

            }
        recyclerViewShopCar?.adapter = mTypeAdapter
        mTypeAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.ivClose) {
                opId = mTypeAdapter.getItem(position).id
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm(0)
                }
            } else if (view.id == R.id.tvSub) {
                val tvSub = mTypeAdapter.getViewByPosition(position, R.id.tvSub)
                val tvAdd = mTypeAdapter.getViewByPosition(position, R.id.tvAdd)
                tvSub?.isClickable = false
                tvAdd?.isClickable = false
                val item = mTypeAdapter.getItem(position)
                var pending_join_quantity = item.join_quantity - 1
                if (pending_join_quantity <= 1) {
                    pending_join_quantity = 1
                }
                onConfirmNumListener?.onConfirm(item.id,pending_join_quantity){
                    if (it) {
                        item.join_quantity = pending_join_quantity
                        updateTotalPrice(list)
                    }
                    tvSub?.isClickable = true
                    tvAdd?.isClickable = true
                    mTypeAdapter.notifyDataSetChanged()
                }
            } else if (view.id == R.id.tvAdd) {
                val tvSub = mTypeAdapter.getViewByPosition(position, R.id.tvSub)
                val tvAdd = mTypeAdapter.getViewByPosition(position, R.id.tvAdd)
                tvSub?.isClickable = false
                tvAdd?.isClickable = false
                val item = mTypeAdapter.getItem(position)
                val pending_join_quantity = item.join_quantity + 1
                onConfirmNumListener?.onConfirm(item.id,pending_join_quantity){
                    if (it) {
                        item.join_quantity = pending_join_quantity
                        updateTotalPrice(list)
                    }
                    tvSub?.isClickable = true
                    tvAdd?.isClickable = true
                    mTypeAdapter.notifyDataSetChanged()
                }
            }
        }
        mTypeAdapter.setNewInstance(list as MutableList<ShopCarInfo>)
    }


    interface OnConfirmNumListener {
        fun onConfirm(id: Int,num: Int, callback:(Boolean)->Unit) //1 支付宝 2 微信
    }
}