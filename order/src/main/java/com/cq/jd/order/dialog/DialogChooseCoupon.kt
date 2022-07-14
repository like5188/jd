package com.cq.jd.order.dialog

import android.content.Context
import android.view.Gravity
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.library.dialog.BaseDialog
import com.cq.jd.order.R
import com.cq.jd.order.dialog.adapter.OrderClsType2Adapter
import com.cq.jd.order.entities.CouponNoLeadBean
import com.cq.jd.order.entities.CouponWaitUseBean
import com.cq.jd.order.widget.layoutmanager.flow.FlowLayoutManager

class DialogChooseCoupon(context: Context) : BaseDialog(context) {

    private var tvTotalNum1: TextView? = null
    private var tvTotalPrice: TextView? = null
    private var recyclerViewShopCar: RecyclerView? = null
    private var tvChooseCoupon: TextView? = null
    private var ivClose: ImageView? = null
    private var pos = 0

    override fun layoutResId() = R.layout.order_dialog_choose_coupon

    override fun getHeightPx() = context.resources.displayMetrics.heightPixels * 2 / 3

    override fun gravity() = Gravity.BOTTOM

    override fun initView(context: Context?) {
        ivClose = findViewById(R.id.ivClose)
        tvTotalNum1 = findViewById(R.id.tvTotalNum1)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        recyclerViewShopCar = findViewById(R.id.recyclerViewShopCar)
        tvChooseCoupon = findViewById(R.id.tvChooseCoupon)
        recyclerViewShopCar?.layoutManager = LinearLayoutManager(context)
    }

    override fun initListener() {
        //onConfirmListener 事件说明  1 立即购买
        tvChooseCoupon?.setOnClickListener {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(pos)
            }
            dismiss()
        }

        ivClose?.setOnClickListener {
            dismiss()
        }

    }

    fun initCls1Adapter(coupon: List<CouponWaitUseBean>) {
        val mTypeAdapter =
            object :
                BaseQuickAdapter<CouponWaitUseBean, BaseViewHolder>(R.layout.order_item_choose_coupon) {
                override fun convert(holder: BaseViewHolder, item: CouponWaitUseBean) {
                    holder.setText(R.id.tvPrice, item.preferential_amount)
                    holder.setText(R.id.tVCondition, item.coupon.title)

                    holder.setText(R.id.tvType, item.coupon.content)
                    holder.setText(R.id.tvTime, "使用期限：" + item.coupon.end_at)
                    val radioButton1 = holder.getView<RadioButton>(R.id.radioButton1)
                    radioButton1.isChecked = item.orderType == 1
                }

            }
        recyclerViewShopCar?.adapter = mTypeAdapter
        mTypeAdapter.setNewInstance(coupon as MutableList<CouponWaitUseBean>)

        mTypeAdapter.setOnItemClickListener { _, _, position ->
            val data = mTypeAdapter.data
//            data.forEach {
//                it.orderType = 0
//            }
            for (item in data) {
                item.orderType = 0
            }
            data[position].orderType = 1
            mTypeAdapter.notifyDataSetChanged()
            pos = position
            tvTotalPrice?.text = data[position].preferential_amount
        }
    }
}