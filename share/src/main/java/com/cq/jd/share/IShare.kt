package com.cq.jd.share

import android.content.Context

/**
 * Created by BOBOZHU on 2022/6/16 17:38
 * Supporte By BOBOZHU
 */
interface IShare {

    /**
     * 分享图片
     * @param context 上下文
     * @param imageData 图片byte数组
     * wx imagePath 必须为本地地址 图片不能大于10m
     */
    fun showImage(context: Context, imageData: ByteArray?, imagePath: String?)

    /**
     * 分享链接
     * @param context 上下文
     * @param title 标题
     * @param description 描述
     * @param thumbData 缩略图 （微信必须小于 32kb）
     * @param webUrl 网页地址
     */
    fun showUrl(
        context: Context,
        title: String,
        description: String,
        thumbData: ByteArray,
        webUrl: String,
    )

}