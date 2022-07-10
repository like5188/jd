package com.common.library.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.common.library.BuildConfig;

public class SystemUtils {

    /**
     * SystemUtils.getMetaData(context, "CHANNEL_VALUE"))
     * 从Manifest中获取meta-data值
     */
    public static String getMetaData(Context context, String key) {
        String value;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
            if (value.equals("formal")) {
                value = "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            value = "";
        }
        return value;
    }


}
