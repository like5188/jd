package com.cq.jd.share

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by BOBOZHU on 2022/6/16 17:47
 * Supporte By BOBOZHU
 *
 *  share
 */
class WxShareImpl(val shareType: String) : IShare {

    /**
     * 聊天
     * SendMessageToWX.Req.WXSceneSession
    分享到朋友圈:
    SendMessageToWX.Req.WXSceneTimeline ;
     */

    val api: IWXAPI by lazy {
        WxApiWrapper.sInstance.getWxApi()
    }

    override fun showImage(context: Context, imageData: ByteArray?, imagePath: String?) {
        val wxImageObject = WXImageObject()
        imagePath?.let {
            wxImageObject.setImagePath(it)
        }
        imageData?.let {
            wxImageObject.imageData = it
        }
        val msg = WXMediaMessage()
        msg.mediaObject = wxImageObject
        val req = SendMessageToWX.Req()
        req.message = msg
        req.scene =
            if (shareType == SHARE_TYPE_WX) SendMessageToWX.Req.WXSceneSession else SendMessageToWX.Req.WXSceneTimeline
        req.userOpenId = ""
        req.transaction = "share_" + System.currentTimeMillis()
        api.sendReq(req)
    }

    override fun showUrl(
        context: Context,
        title: String,
        description: String,
        thumbData: ByteArray,
        webUrl: String,
    ) {

        val webpage = WXWebpageObject()
        webpage.webpageUrl = webUrl
        val msg = WXMediaMessage(webpage)
        msg.title = title
        msg.description = description
        msg.thumbData = thumbData
        val req = SendMessageToWX.Req()
        req.transaction = "share_" + System.currentTimeMillis()
        req.message = msg
        req.scene =
            if (shareType == SHARE_TYPE_WX) SendMessageToWX.Req.WXSceneSession else SendMessageToWX.Req.WXSceneTimeline
        api.sendReq(req)
    }
}
