package com.cq.jd.order.dialog

import android.content.Context
import android.text.TextUtils
import android.util.Log
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
import com.common.library.widget.imageview.RoundedImageView
import com.cq.jd.order.R
import com.cq.jd.order.dialog.adapter.OrderClsType2Adapter
import com.cq.jd.order.entities.ClsGoodsBean
import com.cq.jd.order.entities.GoodsSpecAttribute
import com.cq.jd.order.entities.GoodsSpecAttributeData
import com.cq.jd.order.widget.layoutmanager.flow.FlowLayoutManager

class DialogChooseGoodsType(context: Context) : BaseDialog(context) {

    private var tvSinglePrice: TextView? = null
    private var tvSize: TextView? = null
    private var tvChoose: TextView? = null
    private var tvAddShopCar: TextView? = null
    private var tvNum: TextView? = null
    private var tvSub: ImageView? = null
    private var tvAdd: ImageView? = null
    private var recyclerViewType: RecyclerView? = null
    private var tvNowBuy: TextView? = null
    private var ivLogo: RoundedImageView? = null
    private val datas = ArrayList<GoodsSpecAttributeData>()
    private var num = 1
    private var numMax = 1

    var onResultListener: OnAddShopCarResultListener? = null

    fun setOnConfirmListener(onResultListener: OnAddShopCarResultListener?) {
        this.onResultListener = onResultListener
    }


    override fun cancelAble() = true

    override fun layoutResId() = R.layout.order_dialog_btn_goods_type

    override fun getHeightPx() = context.resources.displayMetrics.heightPixels * 2 / 3

    override fun gravity() = Gravity.BOTTOM

    override fun initView(context: Context?) {
        tvSinglePrice = findViewById(R.id.tvSinglePrice)
        tvSize = findViewById(R.id.tvSize)
        tvChoose = findViewById(R.id.tvChoose)
        tvSub = findViewById(R.id.tvSub)
        tvAdd = findViewById(R.id.tvAdd)
        tvNum = findViewById(R.id.tvNum)
        recyclerViewType = findViewById(R.id.recyclerViewType)
        ivLogo = findViewById(R.id.ivLogo)
        tvAddShopCar = findViewById(R.id.tvAddShopCar)
        tvNowBuy = findViewById(R.id.tvNowBuy)
        recyclerViewType?.layoutManager = LinearLayoutManager(context)
    }

    override fun initListener() {
        //onConfirmListener 事件说明  1 立即购买 2 加入购物车 #EDF1F6
        tvNowBuy?.setOnClickListener {
            var flag = false
            var ids = ""
            for (itemll in datas) {
                if (itemll.choose == 1) {
                    flag = true
                    ids += "${itemll.id},"
                }
            }
            if (!flag) {
                ToastUtils.showShort("请选择规格")
                return@setOnClickListener
            }
            if (onResultListener != null) {
                onResultListener?.onResult(2, ids, num)
            }
            dismiss()
        }

        tvAddShopCar?.setOnClickListener {
            var flag = false
            var ids = ""
            for (itemll in datas) {
                if (itemll.choose == 1) {
                    flag = true
                    ids += "${itemll.id},"
                }
            }
            if (!flag) {
                ToastUtils.showShort("请选择规格")
                return@setOnClickListener
            }
            if (onResultListener != null) {
                ids = ids.substring(0, ids.length - 1)
                onResultListener?.onResult(1, ids, num)
            }
            dismiss()
        }

        tvSub?.setOnClickListener {
            num--
            if (num <= 1) {
                num = 1
            }
            tvNum?.text = num.toString()
        }
        tvAdd?.setOnClickListener {
            num++
            if (num >= numMax) {
                num = numMax
            }
            tvNum?.text = num.toString()
        }
    }

    private fun groupData() {

    }

    fun setUiData(clsGoodsBean: ClsGoodsBean) {
        try {
            ImageUtils.loadImage(clsGoodsBean.cover, ivLogo)
            tvSinglePrice?.text = clsGoodsBean.price
            val specAttribute = clsGoodsBean.spec_attribute
            val mTypeAdapter =
                object :
                    BaseQuickAdapter<GoodsSpecAttribute, BaseViewHolder>(R.layout.order_item_type_cls1) {
                    override fun convert(holder: BaseViewHolder, item: GoodsSpecAttribute) {

                        holder.setText(R.id.tvClsName, item.spec)

                        val recyclerViewContent = holder.getView<RecyclerView>(R.id.recyclerViewContent)
                        recyclerViewContent.layoutManager = FlowLayoutManager()
                        val orderClsType2Adapter = OrderClsType2Adapter()
                        recyclerViewContent.adapter = orderClsType2Adapter

                        val data1 = item.data
                        if (data1 != null && data1.size > 0) {
                            datas.addAll(data1)
                            orderClsType2Adapter.setNewInstance(data1 as MutableList<GoodsSpecAttributeData>)
                        }
                        orderClsType2Adapter.setOnItemClickListener { _, _, position ->
                            val data2 = orderClsType2Adapter.data
                            if (data2[position].goods_stock == 0) {
                                return@setOnItemClickListener
                            }
                            for (iteml in data2) {
                                iteml.choose = 0
                            }
                            data2[position].choose = 1
                            for (itemll in datas) {
                                if (data2[position].id == itemll.id) {
                                    itemll.choose = 1
                                }
                            }
                            orderClsType2Adapter.notifyDataSetChanged()


                            var ts = ""
                            var sz = 0
                            var price = clsGoodsBean.price.toDouble()
                            for (itemll in datas) {
                                if (itemll.choose == 1) {
                                    ts += itemll.spec_attribute + "    "
                                    price += itemll.attribute_price.toDouble()
                                    sz = if (sz == 0) {// 库存为所有属性中最小的一个
                                        itemll.goods_stock
                                    } else {
                                        minOf(sz, itemll.goods_stock)
                                    }
                                }
                            }
                            tvSinglePrice?.text = price.toString()
                            tvSize?.text = sz.toString()
                            numMax = sz
                            if (!TextUtils.isEmpty(ts)) {
                                tvChoose?.setText(ts)
                            }
                        }

                    }

                }
            recyclerViewType?.adapter = mTypeAdapter
            mTypeAdapter.setNewInstance(specAttribute as MutableList<GoodsSpecAttribute>)
        } catch (e: Exception) {
            Log.e("shit", "setUiData: " + e.message)
        }

    }

    override fun dismiss() {
        datas.clear()
        super.dismiss()
    }

    interface OnAddShopCarResultListener {
        fun onResult(type: Int, ids: String, num: Int)
    }


}