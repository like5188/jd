package com.cq.jd.share

import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import com.sina.weibo.sdk.api.ImageObject
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.WebpageObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import java.util.*


/**
 * Created by BOBOZHU on 2022/6/17 09:12
 * Supporte By BOBOZHU
 */
class WeiBoImpl : IShare {
    override fun showImage(context: Context, imageData: ByteArray?, imagePath: String?) {

        val message = WeiboMultiMessage()
        val imageObject = ImageObject()
        imagePath?.let {
            imageObject.setImagePath(imagePath)
        }
        imageData?.let {
            imageObject.imageData = imageData
        }
        message.imageObject = imageObject
    }

    override fun showUrl(
        context: Context,
        title: String,
        description: String,
        thumbData: ByteArray,
        webUrl: String,
    ) {
        // 分享⽹⻚
        // 分享⽹⻚
        val message = WeiboMultiMessage()
        val webObject = WebpageObject()
        webObject.identify = UUID.randomUUID().toString()
        webObject.title = title
        webObject.description = description
        webObject.thumbData = thumbData
        webObject.actionUrl = webUrl
        webObject.defaultText = title
        message.mediaObject = webObject

        if (context is Lifecycle){

        }
    }
}