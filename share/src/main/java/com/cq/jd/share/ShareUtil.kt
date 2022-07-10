package com.cq.jd.share

import android.content.Context

/**
 * Created by BOBOZHU on 2022/6/17 09:51
 * Supporte By BOBOZHU
 */
object ShareUtil {


    fun shareImage(context: Context, shareType: String, imageData: ByteArray?, imagePath: String?) {

        when (shareType) {
            SHARE_TYPE_WX -> {
                WxShareImpl(shareType).showImage(context, imageData, imagePath)
            }
            SHARE_TYPE_WX_FRIEND -> {
                WxShareImpl(shareType).showImage(context, imageData, imagePath)
            }
            SHARE_TYPE_WX_WEIBO -> {
                WeiBoImpl().showImage(context, imageData, imagePath)
            }
        }
    }

    fun shareWebUrl(
        context: Context, shareType: String, title: String,
        description: String,
        thumbData: ByteArray,
        webUrl: String,
    ) {

        when (shareType) {
            SHARE_TYPE_WX -> {
                WxShareImpl(shareType).showUrl(context, title, description, thumbData, webUrl)
            }
            SHARE_TYPE_WX_FRIEND -> {
                WxShareImpl(shareType).showUrl(context, title, description, thumbData, webUrl)
            }
            SHARE_TYPE_WX_WEIBO -> {
                WeiBoImpl().showUrl(context, title, description, thumbData, webUrl)
            }
        }
    }
}