package com.cq.jd.order.activity.shop

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.util.glide.ImageUtils
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderActivityShopCertificateBinding
import com.cq.jd.order.databinding.OrderItemShopCertificateListBinding
import com.cq.jd.order.entities.LicenseInfoBean

class OrderShopCertificateActivity :
    BaseVmActivity<OrderShopCertificateModel, OrderActivityShopCertificateBinding>(R.layout.order_activity_shop_certificate) {

    private val adapter = object :
        BaseQuickAdapter<LicenseInfoBean, BaseDataBindingHolder<OrderItemShopCertificateListBinding>>(
            R.layout.order_item_shop_certificate_list
        ) {
        override fun convert(
            holder: BaseDataBindingHolder<OrderItemShopCertificateListBinding>,
            item: LicenseInfoBean
        ) {
            holder.dataBinding?.apply {
                tvName.text = item.license_name
                tvTime.text = item.license_at
                ImageUtils.loadImage(item.license_pic,ivPic)
            }
        }


    }

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        setTitleText("门店资质")
        initAdapter()
    }

    override fun loadData() {
        mViewModel.licenseDetail(intent.getIntExtra("indexId", 0))
    }

    override fun createObserver() {
        mViewModel.apply {
            licenseInfoBean.observe(this@OrderShopCertificateActivity) {
                if (it != null && it.size > 0) {
                    adapter.setNewInstance(it as MutableList<LicenseInfoBean>)
                }
            }
        }
    }

    private fun initAdapter() {

        mDataBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@OrderShopCertificateActivity)
            recyclerView.adapter = adapter
        }
    }
}