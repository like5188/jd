package com.common.library.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class MyViewPager extends ViewPager {

    private float xDistance, xLast;

    private boolean noScroll = false;

    /**
     * DrawerLayout 定义拖拽范围
     * @param activity  			    上下文
     * @param drawerLayout			     受用的 DrawerLayout
     * @param displayWidthPercentage  拖拽范围，float
     */
    public static void setDrawerLeftEdgeSize(Activity activity,
                                             DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)
            return;
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField(
                    "mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField
                    .get(drawerLayout);
            // find edgesize and set is accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField(
                    "mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // set new edgesize
            // Point displaySize = new Point();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize,
                    (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
            Log.e("NoSuchFieldException", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("IllegalArgument", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }


    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 禁止第一个frament右滑
     *
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (noScroll) {
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = 0f;
                xLast = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                //第一个Fragment禁止右滑
                if (xLast - curX < 0 && getCurrentItem() == 0) {
                    return false;
                }
                //最后一个Fragment禁止左滑
//                if (xLast - curX > 0 && getCurrentItem() == 1) {
//                    return false;
//                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    /**
     * 控制Viewpager是否可滑动
     * @param noScroll : true 不能滑动
     */

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
