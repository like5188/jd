package com.common.library.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.util.ByteBufferUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.library.R
import com.common.library.ui.removeAnimation
import com.common.library.util.Util
import com.cq.jd.share.SHARE_TYPE_WX
import com.cq.jd.share.SHARE_TYPE_WX_FRIEND
import com.cq.jd.share.ShareUtil
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.coroutines.*
import java.io.Serializable
import java.util.ArrayList

data class ShareBean(val resId: Int, val name: String, val shareType: String) : Serializable

/**
 * Created by BOBOZHU on 2022/6/17 09:58
 * Supporte By BOBOZHU
 */
class ShareDialog(
    context: Context,
    val itemClick: ((dialog: ShareDialog, shareBean: ShareBean) -> Unit)? = null,
) :
    BottomPopupView(context) {
    lateinit var rvShare: RecyclerView
    private val shareAdapter by lazy {
        object : BaseQuickAdapter<ShareBean, BaseViewHolder>(R.layout.base_item_share) {
            override fun convert(holder: BaseViewHolder, item: ShareBean) {
                holder.setImageResource(R.id.iv_icon, item.resId)
                    .setText(R.id.tv_text, item.name)
            }

        }
    }
    private val shareBeans = ArrayList<ShareBean>()


    override fun onCreate() {
        super.onCreate()
        rvShare = findViewById(R.id.recyclerView)
        init()
        rvShare.layoutManager = GridLayoutManager(context, 4)
        rvShare.removeAnimation()
        rvShare.adapter = shareAdapter
        initData()
        initListener()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.base_dialog_share
    }

    private fun initListener() {
        shareAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            itemClick?.invoke(this, shareAdapter.getItem(position))
//            val shareBean = shareBeans[position]
//            runBlocking {
//                kotlin.runCatching {
//                    withContext(Dispatchers.IO) {
//                        Util.getUrlBitmap("https://t12.baidu.com/it/u=984106723,176204573&fm=30&app=106&f=JPEG?w=312&h=208&s=0151AB66C6081157CB40B48A03008092",
//                            32)
//                    }
//                }.onSuccess {
//                    ShareUtil.shareWebUrl(context,
//                        shareBean.shareType,
//                        "分享",
//                        "描述",
//                        it,
//                        "https://www.baidu.com")
//
//                }.onFailure {
//                    LogUtils.v(it.localizedMessage)
//                }
//            }
        }
    }

    private fun initData() {
        shareBeans.clear()
        shareBeans.add(ShareBean(R.mipmap.base_icon_circle,
            "朋友圈",
            SHARE_TYPE_WX_FRIEND))
        shareBeans.add(ShareBean(R.mipmap.base_icon_wxchat,
            "微信",
            SHARE_TYPE_WX))
        shareAdapter.setNewInstance(shareBeans)
    }

    fun setNewData(mData: List<ShareBean>) {
        shareAdapter.setNewInstance(shareBeans)
    }

    fun addData() {
        shareAdapter.addData(shareBeans)
    }
}