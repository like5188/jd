package com.common.library.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.CropFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine

/**
 * Created by BOBOZHU on 2022/6/17 14:55
 * Supporte By BOBOZHU
 */
object PictureSelectorUtil {


    val option by lazy {
        UCrop.Options().apply {
            isForbidCropGifWebp(true)
            isCropDragSmoothToCenter(true)
            isForbidSkipMultipleCrop(true)
            isDarkStatusBarBlack(true)
        }
    }


    fun getCropFileEngine() =
        CropFileEngine { fragment, srcUri, destinationUri, dataSource, requestCode ->
            val uCrop = UCrop.of(srcUri, destinationUri, dataSource)
            uCrop.setImageEngine(object : UCropImageEngine {
                override fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
                    imageView?.let { Glide.with(context!!).load(url).into(it) }
                }

                override fun loadImage(
                    context: Context?,
                    url: Uri?,
                    maxWidth: Int,
                    maxHeight: Int,
                    call: UCropImageEngine.OnCallbackListener<Bitmap>?,
                ) {

                }
            })
            uCrop.withOptions(option)
            uCrop.start(fragment.requireActivity(), fragment, requestCode)
        }

    /**
     * 单选图片
     */
    fun selectSingle(context: Context, needCrop: Boolean = false, callBack: (String) -> Unit) {
        selectMore(context, needCrop = needCrop, maxSize = 1) { list, _ ->
            callBack.invoke(list[0])
        }
    }

    /**
     * 多选图片
     */
    fun selectMore(
        context: Context, maxSize: Int,
        needCrop: Boolean = false,
        selectedList: List<LocalMedia>? = null,
        callBack: (List<String>, ArrayList<LocalMedia>) -> Unit,
    ) {
        val cropFileEngine = if (needCrop) getCropFileEngine() else null
        PictureSelector.create(context)
            .openGallery(PictureMimeType.getMimeType(PictureMimeType.MIME_TYPE_PREFIX_IMAGE))
            .setMaxSelectNum(maxSize)
            .setSelectedData(selectedList)
            .setImageEngine(GlideEngine.instance)
            .setCropEngine(cropFileEngine)
            .forResult(object : OnResultCallbackListener<LocalMedia> {

                override fun onCancel() {}
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    if (result != null) {
                        // 结果回调
                        val fileUri = FileUriHelper(context)
                        val data = arrayListOf<String>()
                        result.forEach {
                            fileUri.getFilePathByUri(Uri.parse(it.availablePath))
                                ?.let { it1 -> data.add(it1) }
                        }
                        callBack.invoke(data, result)
                    }
                }
            })
    }


}