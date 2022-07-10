package com.common.library.clicklimt;

import android.view.View;

import java.util.HashMap;

public class ClickKit {
    // 两次点击按钮之间的点击间隔不能少于2000毫秒
    private static int MIN_CLICK_DELAY_TIME = 1000;
    private static HashMap<Integer, Long> sLastClickTimeMap = new HashMap<>();

    public static void setMinClickDelayTime(int minClickDelayTime) {
        MIN_CLICK_DELAY_TIME = minClickDelayTime;
    }

    public static boolean isFastClick(int viewId) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        long lastClickTime = getLastClickTime(viewId);
        if ((curClickTime - lastClickTime) < MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        sLastClickTimeMap.put(viewId, curClickTime);
        return flag;
    }

    public static void clear() {
        sLastClickTimeMap.clear();
    }

    private static Long getLastClickTime(int viewId) {
        Long lastClickTime = sLastClickTimeMap.get(viewId);
        if (lastClickTime == null) {
            lastClickTime = 0L;
        }
        return lastClickTime;
    }

    public static void addClickListener(View view, OnClickAction clickAction) {
        view.setOnClickListener(v1 -> {
            if (isFastClick(view.getId())) return;
            if (null != clickAction) {
                clickAction.onClick(view);
            }
        });
    }

    public interface OnClickAction {
        void onClick(View v);
    }
}
