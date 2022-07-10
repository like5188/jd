package com.common.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.common.library.R
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.interfaces.OnCallbackListener


/**
 * @author：luck
 * @date：2019-11-13 17:02
 * @describe：Glide加载引擎
 */
class GlideEngine private constructor() : ImageEngine {
    /**
     * 加载图片
     *
     * @param context   上下文
     * @param url       资源url
     * @param imageView 图片承载控件
     */
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    @SuppressLint("CheckResult")
    fun loadAppImage(
        context: Context,
        url: String,
        imageView: ImageView? = null,
        holderInt: Int = 0,
        errorId: Int = 0,
        transformation: Transformation<Bitmap>? = null,
        target: CustomViewTarget<View, Bitmap>? = null,
    ) {
        val options = RequestOptions().apply {
            if (holderInt != 0) {
                this.placeholder(holderInt)
            }
            if (errorId != 0) {
                this.error(errorId)
            }
            transformation?.let { transform(it) }
        }
        imageView?.let {
            Glide.with(context).applyDefaultRequestOptions(options)
                .load(url)
                .into(it)
        } ?: kotlin.run {
            target?.let {
                Glide.with(context).applyDefaultRequestOptions(options)
                    .asBitmap()
                    .load(url)
                    .into(it)
            }
        }
    }

    /**
     * 加载指定url并返回bitmap
     *
     * @param context   上下文
     * @param url       资源url
     * @param maxWidth  资源最大加载尺寸
     * @param maxHeight 资源最大加载尺寸
     * @param call      回调接口
     */
    override fun loadImageBitmap(
        context: Context,
        url: String,
        maxWidth: Int,
        maxHeight: Int,
        call: OnCallbackListener<Bitmap>,
    ) {
        Glide.with(context)
            .asBitmap()
            .override(maxWidth, maxHeight)
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    if (call != null) {
                        call.onCall(resource)
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    if (call != null) {
                        call.onCall(null)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    /**
     * 加载相册目录封面
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .sizeMultiplier(0.5f)
            .transform(CenterCrop(), RoundedCorners(8))
            .placeholder(R.drawable.ps_image_placeholder)
            .into(imageView)
    }

    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .placeholder(R.drawable.ps_image_placeholder)
            .into(imageView)
    }

    override fun pauseRequests(context: Context) {
        Glide.with(context).pauseRequests()
    }

    override fun resumeRequests(context: Context) {
        Glide.with(context).resumeRequests()
    }

    companion object {
        val instance: GlideEngine by lazy {
            GlideEngine()
        }
    }
}