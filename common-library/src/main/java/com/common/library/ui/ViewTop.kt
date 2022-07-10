package com.common.library.ui

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.request.target.CustomViewTarget
import com.common.library.ui.paging.BasePagingActivity
import com.common.library.ui.paging.BasePagingFragment
import com.common.library.ui.paging.BasePagingViewModel
import com.common.library.util.GlideEngine
import com.common.library.viewModel.BaseViewModel
import com.luck.picture.lib.utils.DoubleUtils
import java.lang.reflect.ParameterizedType

/**
 * Created by *** on 2022/5/27 16:47
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */

/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
@Suppress("UNCHECKED_CAST")
inline fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
inline fun <reified VM> getVmClazz1(obj: Any): VM {
    val genericSuperclass = obj.javaClass.genericSuperclass
//    if (genericSuperclass is ParameterizedType) {
//        val actualTypeArguments = genericSuperclass.actualTypeArguments
//        actualTypeArguments.forEachIndexed { index, type ->
//            LogUtils.v("${index}====${type.javaClass.name}==${type.javaClass.simpleName}")
//            if (type is WildcardType) {
//                val lowerBounds = type.lowerBounds
//                val upperBounds = type.upperBounds
//
//            }
//        }
//    }
    return (genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
}

fun <VM : BasePagingViewModel<*>> BasePagingActivity<*, VM>.createViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(this.application)
    ).get(getVmClazz1(this))
}

//fun <VM : BaseViewModel> BaseVmFragment<VM, *>.createViewModel(): VM {
//    return ViewModelProvider(
//        this,
//        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
//    ).get(getVmClazz1(this))
//}

fun <VM : BasePagingViewModel<*>> BasePagingFragment<*, VM>.createViewModel(key: String? = null): VM {
    val vmClazz1: Class<VM> = getVmClazz1(this)
    val newKey = key ?: "com.base.zhw.paging" + vmClazz1.javaClass.simpleName
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
    ).get(newKey, vmClazz1)
}


fun <T : View> Activity.fid(id: Int): T? {
    return findViewById(id)
}

fun <T : View> Fragment.fid(id: Int): T? {
    return view?.findViewById(id)
}

fun View.click(action: (v: View) -> Unit = {}) {
    this.setOnClickListener {
        if (DoubleUtils.isFastDoubleClick()) {
            return@setOnClickListener
        }
        action.invoke(this)
    }
}


//fun AppResource(resourceId: Int): String = AppUtils.getAppContext().resources.getString(resourceId)
//
//fun AppResourceColor(resourceId: Int): Int = AppUtils.getAppContext().resources.getColor(resourceId)


inline fun RecyclerView.removeAnimation() {
    val animator = this.itemAnimator;
    if (animator is SimpleItemAnimator) {
        animator.supportsChangeAnimations = false;
    }
}

inline fun ConcatAdapter.getAdapterByItemPosition(position: Int): RecyclerView.Adapter<out RecyclerView.ViewHolder>? {
    var pos = position
    val adapters = adapters
    for (adapter in adapters) {
        when {
            pos >= adapter.itemCount -> {
                pos -= adapter.itemCount
            }
            pos < 0 -> return null
            else -> return adapter
        }
    }
    return null
}

fun ImageView.loadImage(url: String) {
    GlideEngine.instance.loadAppImage(this.context, url, this)
}

fun ImageView.loadImage(url: String, transformation: Transformation<Bitmap>) {
    GlideEngine.instance.loadAppImage(this.context,
        url,
        imageView = this,
        transformation = transformation)
}

fun View.loadImage(url: String, target: CustomViewTarget<View, Bitmap>) {
    GlideEngine.instance.loadAppImage(this.context, url, target = target)
}