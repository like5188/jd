package com.common.library.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.library.util.ActivityManager;

public class LifecycleHandler implements Application.ActivityLifecycleCallbacks{

    public static final int STATE_NORMAL = 0;
    public static final int STATE_BACK_TO_FRONT = 1;
    public static final int STATE_FRONT_TO_BACK = 2;
    public static int sAppState = STATE_NORMAL;
    private int mVisibleActivityCount = 0;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        ActivityManager.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // 每有一个activity可见都会走该方法，mVisibleActivityCount会增1。
        mVisibleActivityCount++;
        if (mVisibleActivityCount == 1) {
            // 从后台进入前台
            sAppState = STATE_BACK_TO_FRONT;
        } else {
            // 否则是正常状态
            sAppState = STATE_NORMAL;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        //每有一个acitivity不可见都会走该方法，让mVisibleActivityCount减1。
        mVisibleActivityCount--;
        if (mVisibleActivityCount == 0) {
            // 从前台进入后台
            sAppState = STATE_FRONT_TO_BACK;
        } else {
            // 否则是正常状态
            sAppState = STATE_NORMAL;
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ActivityManager.getInstance().removeActivity(activity);
    }
}
