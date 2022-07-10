package com.common.library.clicklimt.aspectj;

import android.util.Log;
import android.view.View;


import com.common.library.R;
import com.common.library.clicklimt.aa.ClickLimit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class ClickLimitAspect {

    private static final String TAG = "ClickLimitAspect";
    private static long clickGapTime = 0;
    private static final int CHECK_FOR_DEFAULT_TIME = 2000;//设置系统的点击间隔事件

    private static final String POINTCUT_ON_ANNOTATION = "execution(@com.common.library.clicklimt.aa.ClickLimit * *(..))";

    private static final String POINTCUT_ON_CLICK = "execution(* android.view.View.OnClickListener.onClick(..))";

    private static final String POINTCUT_ADAPTER_ON_CLICK = "execution(* com.chad.library.adapter.base.listener.OnItemClickListener.onItemClick(..))";
    private static final String POINTCUT_ADAPTER_ITEM_CHILD_ON_CLICK = "execution(* com.chad.library.adapter.base.listener.OnItemChildClickListener.onItemChildClick(..))";

    @Pointcut(POINTCUT_ON_ANNOTATION)
    public void onAnnotationClick() {

    }

    // 定义切入点：View.OnClickListener#onClick()方法
    @Pointcut(POINTCUT_ON_CLICK)
    public void methodViewOnClick() {
    }

    @Pointcut(POINTCUT_ADAPTER_ON_CLICK)
    public void methodBaseQuickAdapterOnClick() {
    }

    @Pointcut(POINTCUT_ADAPTER_ITEM_CHILD_ON_CLICK)
    public void methodBaseQuickAdapterChildItemOnClick() {

    }

    //监听系统方法或者自定义list adapter中的item方法
    @Around("methodViewOnClick()||onAnnotationClick()||methodBaseQuickAdapterOnClick()||methodBaseQuickAdapterChildItemOnClick()")
    public void processJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Signature signature = joinPoint.getSignature();
            if (!(signature instanceof MethodSignature)) {
                joinPoint.proceed();
                return;
            }

            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            boolean isHasLimitAnnotation = method.isAnnotationPresent(ClickLimit.class);

            String methodName = method.getName();

            int intervalTime = CHECK_FOR_DEFAULT_TIME;

            if (isHasLimitAnnotation) {
                ClickLimit clickLimit = method.getAnnotation(ClickLimit.class);
                int limitTime = 0;//得到点击事件的设置事件
                if (clickLimit != null) {
                    limitTime = clickLimit.value();
                }
                if (limitTime <= 0) {
                    Log.d(TAG, "method: " + methodName + " limitTime is zero, so proceed it");
                    joinPoint.proceed();
                    return;
                }
                intervalTime = limitTime;
                Log.d(TAG, "methodName " + methodName + " intervalTime is " + intervalTime);
            }
            //得到传入的参数
            Object[] args = joinPoint.getArgs();
            View view = getViewFromArgs(args);
            if (view == null) {
                Log.d(TAG, "view is null, proceed");
                joinPoint.proceed();
                return;
            }

            Object viewTimeTag = view.getTag(R.integer.click_limit_tag_view);
            // first click viewTimeTag is null.
            if (viewTimeTag == null) {
                Log.d(TAG, "lastClickTime is zero , proceed");
                proceedAnSetTimeTag(joinPoint, view);
                return;
            }

            long lastClickTime = (long) viewTimeTag;
            if (lastClickTime <= 0) {
                Log.d(TAG, "lastClickTime is zero , proceed");
                proceedAnSetTimeTag(joinPoint, view);
                return;
            }

            // in limit time
            if (!canClick(lastClickTime, intervalTime)) {
                Log.d(TAG, "is in limit time , return");
                return;

            }
            proceedAnSetTimeTag(joinPoint, view);
            Log.d(TAG, "view proceed.");
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
            joinPoint.proceed();
        }
    }

    //判断是否应该执行，true执行，false不执行
    protected boolean clickGapFilter() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - clickGapTime < CHECK_FOR_DEFAULT_TIME) {
            return false;
        }
        clickGapTime = currentTimeMillis;
        return true;
    }


    public void proceedAnSetTimeTag(ProceedingJoinPoint joinPoint, View view) throws Throwable {
        view.setTag(R.integer.click_limit_tag_view, System.currentTimeMillis());
        joinPoint.proceed();
    }


    /**
     * 获取 view 参数
     *
     */
    public View getViewFromArgs(Object[] args) {
        if (null == args) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof View) {
                return (View) arg;
            }
        }
        return null;
    }

    /**
     * 判断是否达到可以点击的时间间隔
     *
     */
    public boolean canClick(long lastClickTime, int intervalTime) {
        long currentTime = System.currentTimeMillis();
        long realIntervalTime = currentTime - lastClickTime;
        Log.d(TAG, "canClick currentTime= " + currentTime + " lastClickTime= " + lastClickTime +
                " realIntervalTime= " + realIntervalTime);
        return realIntervalTime >= intervalTime;
    }
}
