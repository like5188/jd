package com.cq.jd.order.activity.shop

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.common.library.ui.requestRs
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.order.entities.LicenseInfoBean
import com.cq.jd.order.net.OrderNetApi

class OrderShopCertificateModel(application: Application) :
    BaseViewModel(application) {

    val licenseInfoBean = MutableLiveData<List<LicenseInfoBean>>()

    fun licenseDetail(merchantId: Int ) {

        requestRs({
            OrderNetApi.service.licenseDetail(merchantId)
        }, {
            licenseInfoBean.value = it
        }, loadingMessage = "数据获取中...")
    }

}