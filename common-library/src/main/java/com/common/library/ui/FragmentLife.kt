package com.common.library.ui

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.LogUtils

/**
 * Created by zhubo on 2020/11/27
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class FragmentLife(name:String="") : LifecycleObserver {
    
    private val fragmentTag:String="LifecycleObserver===${name}==="
    
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        LogUtils.i(fragmentTag, "Observer【onCreate】")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        LogUtils.i(fragmentTag, "Observer【onStart】")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(source: LifecycleOwner) {
        LogUtils.i(
            fragmentTag,
            "Observer【onResume】" + source.lifecycle.javaClass.simpleName
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        LogUtils.i(fragmentTag, "Observer【onPause】")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        LogUtils.i(fragmentTag, "Observer【onStop】")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        LogUtils.i(fragmentTag, "Observer【onDestroy】")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(
        source: LifecycleOwner?,
        event: Lifecycle.Event
    ) {
        LogUtils.i(fragmentTag, "Observer onAny：" + event.name)
    }

}