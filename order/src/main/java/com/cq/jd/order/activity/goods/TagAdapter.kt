package com.cq.jd.order.activity.goods

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderItemTagBindBinding

/**
 *@description 业务新增
 *@author 吴江
 *@data 2022-07-10 16:44
 *
 */
class TagAdapter:BaseQuickAdapter<String,BaseDataBindingHolder<OrderItemTagBindBinding>> (R.layout.order_item_tag_bind){
    override fun convert(holder: BaseDataBindingHolder<OrderItemTagBindBinding>, item: String) {
        holder.dataBinding?.tagName = item
    }
}