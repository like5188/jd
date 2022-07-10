package com.cq.jd.start.center

import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.dialog.BaseCenterHintDialog
import com.common.library.router.ARouterPath
import com.common.library.router.provider.LocationService
import com.common.library.router.provider.UserService
import com.common.library.ui.activity.BaseViewActivity
import com.common.library.viewModel.BaseViewModel
import com.cq.jd.start.R
import com.cq.jd.start.databinding.StartActivityCenterBinding
import com.cq.jd.start.databinding.StartItemCenterActionBinding
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lxj.xpopup.XPopup
import java.io.Serializable

data class ItemAction(val name: String, val path: String) : Serializable

/**
 * Created by *** on 2022/5/31 09:57
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
@Route(path = ARouterPath.Start.CENTER_TRANSFER)
class CenterActivity :
    BaseViewActivity<StartActivityCenterBinding>(R.layout.start_activity_center) {

    override fun statusBarDarkFont(): Boolean = true

    val centerAdapter by lazy {

        val adapter = object :
            BaseQuickAdapter<ItemAction, BaseDataBindingHolder<StartItemCenterActionBinding>>(R.layout.start_item_center_action) {
            override fun convert(
                holder: BaseDataBindingHolder<StartItemCenterActionBinding>,
                item: ItemAction,
            ) {
                holder.dataBinding?.apply {
                    this.tvAction.text = item.name
                }
            }
        }
        adapter.setOnItemClickListener { _, _, position ->
            adapter.getItem(position).path.let {
                when (it) {
                    ARouterPath.User.LOGIN -> {
                        val userService =
                            ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
                                .navigation() as UserService
                        if (userService.isUserLogin) {
                            XPopup.Builder(this).asCustom(BaseCenterHintDialog(this, "是否切换其他账号？") {
                                val userService =
                                    ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
                                        .navigation() as UserService
                                userService.logOut()
                            }).show()
                        } else {
                            doIntent(it)
                        }
                    }
                    ARouterPath.Map.LOCATION_SERVICE -> {
                        if (adapter.getItem(position).name.equals("获取定位")) {
                            requestLocation()
                        } else {
                            val locationService =
                                ARouter.getInstance().build(ARouterPath.Map.LOCATION_SERVICE)
                                    .navigation() as LocationService
                            val userSelectLocation = locationService.getUserSelectLocation()
                            userSelectLocation?.let { lc ->
                                showToast("选择的位置信息：" + GsonUtils.toJson(lc))
                            }
                        }
                    }
                    else -> {
                        doIntent(it)
                    }
                }
            }

        }
        adapter

    }

    val data = arrayListOf(
        ItemAction("首页", ARouterPath.App.MainActivityPath),
        ItemAction("商城", ARouterPath.Start.shop),
        ItemAction("登录页面", ARouterPath.User.LOGIN),
        ItemAction("二维码", ARouterPath.Start.main),
        ItemAction("获取定位", ARouterPath.Map.LOCATION_SERVICE),
        ItemAction("获取信息", ARouterPath.Map.LOCATION_SERVICE),
        ItemAction("下单流程", ARouterPath.Order.ORDER_HOME),
    )

    override fun initWidget(savedInstanceState: Bundle?) {
        setTitleText("中转站")
        mDataBinding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@CenterActivity)
            recyclerView.addItemDecoration(DividerItemDecoration(this@CenterActivity,
                DividerItemDecoration.VERTICAL))
            recyclerView.adapter = centerAdapter
            centerAdapter.setNewInstance(data as MutableList<ItemAction>)
        }
//
//        val data = arrayListOf(
//            PayTypeBean(type = "balance",
//                name = "余额支付",
//                "https://jianduitupian.obs.cn-southwest-2.myhuaweicloud.com/static/1app/icon/banlance.png",
//                "余额  ¥154.00",
//                "当前余额不足，请另行选择支付方式"),
//            PayTypeBean(type = "wechat",
//                name = "微信支付",
//                "https://jianduitupian.obs.cn-southwest-2.myhuaweicloud.com/static/1app/icon/wechat.png",
//                "",
//                ""),
//            PayTypeBean(type = "alipay",
//                name = "支付宝支付",
//                "https://jianduitupian.obs.cn-southwest-2.myhuaweicloud.com/static/1app/icon/alipay.png",
//                "",
//                ""),
//            PayTypeBean(type = "unionPay",
//                name = "银联支付",
//                "https://jianduitupian.obs.cn-southwest-2.myhuaweicloud.com/static/1app/icon/upay.png",
//                "",
//                ""),
//            PayTypeBean(type = "unionPay",
//                name = "普通积分",
//                "https://jianduitupian.obs.cn-southwest-2.myhuaweicloud.com/static/1app/icon/score.png",
//                "剩余普通积分 1500",
//                ""),
//        )
//
//        mDataBinding.payTypeView.apply {
//            setPayTypeData(data)
//            callBack = {
//                showToast(it.name)
//
//                XPopup.Builder(this@CenterActivity)
//                    .asCustom(ShareDialog(this@CenterActivity) { dialog, shareBean ->
//                        lifecycleScope.launch {
//                            kotlin.runCatching {
//                                withContext(Dispatchers.IO) {
//                                    Util.getUrlBitmap("https://t12.baidu.com/it/u=984106723,176204573&fm=30&app=106&f=JPEG?w=312&h=208&s=0151AB66C6081157CB40B48A03008092",
//                                        32)
//                                }
//                            }.onSuccess {
//                                ShareUtil.shareWebUrl(context,
//                                    shareBean.shareType,
//                                    "分享",
//                                    "描述",
//                                    it,
//                                    "https://www.baidu.com")
//
//                            }.onFailure {
//                                LogUtils.v(it.localizedMessage)
//                            }
//                        }
//                    })
//                    .show()
//            }
//        }
    }


    /**
     * 判断权限
     */
    fun requestLocation() {
        val list =
            if (this.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.S) {//android 12
                listOf(
                    Permission.ACCESS_FINE_LOCATION,
                    Permission.ACCESS_COARSE_LOCATION
                )
            } else {
                listOf(
                    Permission.ACCESS_FINE_LOCATION
                )
            }
        if (XXPermissions.isGranted(this, list)) {
            toLocation()
        } else {
            XXPermissions.with(this) // 适配 Android 12 可以这样写
                .permission(list)
                .request { permissions, all ->
                    if (!all) {
                        showToast("请开启定位权限")
                    } else {
                        toLocation()
                    }
                }
        }
    }

    /**
     * 定位
     */
    fun toLocation() {
        showLoading("定位中...")
        val locationService = ARouter.getInstance().build(ARouterPath.Map.LOCATION_SERVICE)
            .navigation() as LocationService
        locationService.signLocation(this) { bl, location ->
            hiddenLoading()
            if (bl && location != null) {
                showToast(GsonUtils.toJson(location))
                val service =
                    ARouter.getInstance().build(ARouterPath.Map.LOCATION_SERVICE)
                        .navigation() as LocationService
                service.saveSelectLocation(location)
            } else {
                showToast("定位失败")
            }
        }
    }

    override fun loadData() {
    }

    override fun getPageViewModel(): BaseViewModel? = null
}