package com.common.library.util.sp;//package com.common.library.util.sp;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.common.library.BaseApp;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
///**
// * Sp工具  缓存一些基本的东西
// */
//public class SPHelper {
//
//    private static volatile SPHelper instance;
//
//    /**
//     * 文件名字
//     */
//    public static final String FILE_NAME = "yuan_qi_data.xml";
//
//    private final SharedPreferences sharedPreferences;
//
//
//    public static SPHelper getInstance() {
//        if (instance == null) {
//            synchronized (SPHelper.class) {
//                if (instance == null) {
//                    instance = new SPHelper();
//                }
//            }
//        }
//        return instance;
//    }
//
//
//    /*
//     * 保存手机里面的名字
//     */
//    private final SharedPreferences.Editor editor;
//
//    @SuppressLint("CommitPrefEdits")
//    public SPHelper() {
//
//        sharedPreferences = BaseApp.application.getSharedPreferences(FILE_NAME,
//                Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//
//    }
//
//    /**
//     * 存储
//     */
//    public void put(String key, Object object) {
//        if (object instanceof String) {
//            editor.putString(key, (String) object);
//        } else if (object instanceof Integer) {
//            editor.putInt(key, ((Integer) object));
//        } else if (object instanceof Boolean) {
//            editor.putBoolean(key, (Boolean) object);
//        } else if (object instanceof Float) {
//            editor.putFloat(key, (Float) object);
//        } else if (object instanceof Long) {
//            editor.putLong(key, (Long) object);
//        } else {
//            editor.putString(key, object.toString());
//        }
//        editor.apply();
//    }
//
//    public void putInt(String key, int object) {
//        editor.putInt(key, object);
//        editor.apply();
//    }
//
//    public  void  putTpyTime(){
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
//        put(SpConstant.APP_CACHE_TIME,df.format(new  Date(System.currentTimeMillis())));
//
//    }
//
//
//    public String getString(String key) {
//        return sharedPreferences.getString(key, "");
//    }
//
//    public String getTaskString(String key) {
//        return sharedPreferences.getString(key, "0");
//    }
//
//
//    public long getLong(String key) {
//        return sharedPreferences.getLong(key, 0);
//    }
//
//    public int getInt(String key) {
//        return sharedPreferences.getInt(key, 0);
//    }
//
//
//    public boolean getFalseBoolean(String key) {
//        return sharedPreferences.getBoolean(key, false);
//    }
//
//    public boolean getTrueBoolean(String key) {
//        return sharedPreferences.getBoolean(key, true);
//    }
//
//
//    /**
//     * 移除某个key值已经对应的值
//     */
//    public void remove(String key) {
//        editor.remove(key);
//        editor.apply();
//    }
//
//    /**
//     * 清除所有数据
//     */
//    public void clear() {
//        String dy = SPHelper.getInstance().getString(SpConstant.isAgree);
//        editor.clear();
//        editor.apply();
//        SPHelper.getInstance().put(SpConstant.isAgree,dy);
//    }
//
//    /**
//     * 查询某个key是否存在
//     */
//    public Boolean contain(String key) {
//        return sharedPreferences.contains(key);
//    }
//
//}
