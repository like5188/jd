package com.cq.jd.order.dialog.adapter

import android.graphics.Color
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.entities.GoodsSpecAttributeData

class OrderClsType2Adapter :
    BaseQuickAdapter<GoodsSpecAttributeData, BaseViewHolder>(R.layout.order_item_type_cls2) {
    override fun convert(holder: BaseViewHolder, item: GoodsSpecAttributeData) {
        holder.apply {
            setBackgroundResource(
                R.id.llClsBg,
                if (item.choose == 1) R.drawable.order_shape_btn else R.drawable.order_shape_btn_gray
            )
            setTextColor(
                R.id.tvTypeName, if (item.choose == 1)
                    Color.parseColor("#ffffff") else Color.parseColor("#13202D")
            )
            if(!TextUtils.isEmpty(item.attribute_pic)){
                holder.setVisible(R.id.ivGoodsCover,true)
                ImageUtils.loadImage(item.attribute_pic, holder.getView(R.id.ivGoodsCover))
            }else{
                holder.setGone(R.id.ivGoodsCover,true)
            }

            setText(R.id.tvTypeName, item.spec_attribute)
            setGone(R.id.ivState, item.goods_stock != 0)
        }
    }

}