package com.common.library.widget.payview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.R
import com.common.library.bean.PayTypeBean
import com.common.library.databinding.BaseItemPayType1Binding
import com.common.library.databinding.BaseViewPayTypeBinding
import com.common.library.ui.loadImage
import com.common.library.ui.removeAnimation

/**
 * Created by BOBOZHU on 2022/6/16 15:48
 * Supporte By BOBOZHU
 */
class PayTypeView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var bind: BaseViewPayTypeBinding

    private var selectPosition: Int = 0

    var callBack: ((typeBean: PayTypeBean) -> Unit)? = null

    fun selectPosition() = selectPosition

    val mApter by lazy {
        val adapter = object :
            BaseQuickAdapter<PayTypeBean, BaseDataBindingHolder<BaseItemPayType1Binding>>(R.layout.base_item_pay_type_1) {
            override fun convert(
                holder: BaseDataBindingHolder<BaseItemPayType1Binding>,
                item: PayTypeBean,
            ) {
                holder.dataBinding?.apply {
                    ivLogo.loadImage(item.img)
                    tvTitle.text = item.name
//                    tvHint.visibility = if (item.proportion?.isNullOrBlank()) View.GONE else View.VISIBLE
//                    tvMoney.visibility =
//                        if (item.proportion?.isNullOrBlank()) View.GONE else View.VISIBLE
//                    tvHint.text = item.proportion
                    tvMoney.visibility = when (item.type) {
                        "wechat" -> {
                            View.GONE
                        }
                        "unionPay" -> {
                            View.GONE
                        }
                        "alipay" -> {
                            View.GONE
                        }
                        else -> {
                            View.VISIBLE
                        }
                    }
                    tvMoney.text = "余额：${item.proportion}"


                    ivState.setImageResource(if (selectPosition == holder.layoutPosition) R.drawable.base_shape_circle_blue else R.drawable.base_shape_circle_f5_out_line)

                }
            }
        }
        adapter.setOnItemClickListener { _, _, position ->
            val beforePosition = selectPosition
            selectPosition = position
            adapter.notifyItemChanged(beforePosition)
            adapter.notifyItemChanged(selectPosition)
            callBack?.invoke(adapter.getItem(position))
        }
        adapter
    }

    init {
        //添加布局文件
        val view = LayoutInflater.from(context).inflate(R.layout.base_view_pay_type, null)
        bind = DataBindingUtil.bind(view)!!
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = layoutParams
        bind.recyclerView.layoutManager = LinearLayoutManager(context)
        bind.recyclerView.adapter = mApter
        bind.recyclerView.removeAnimation()
        addView(view)
    }


    fun setPayTypeData(mData: List<PayTypeBean>) {
        mApter.setNewInstance(mData as MutableList<PayTypeBean>)
    }


}