package com.cq.jd.start.tools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;


public class OaidHelper implements IIdentifierListener {

    static  String OAID_SUPPORT = "app_oaid_support"; //手机是不是支持移动安全联盟
    static String OAID = "app_oaid"; //手机是不是支持移动安全联盟

    String oaid, vaid, aaid;
    private AppIdsUpdater _listener;
    private static  SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor = null;
    /**
     * 文件名字
     */
    public static final String FILE_NAME = "yuan_qi_data.xml";

    public OaidHelper(Application application, AppIdsUpdater callback) {
        _listener = callback;
        sharedPreferences = application.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void getDeviceIds(Context cxt) {
        long timeb = System.currentTimeMillis();
        // 方法调用
        int nres = CallFromReflect(cxt);

        long timee = System.currentTimeMillis();
        long offset = timee - timeb;
        if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//不支持的设备

        } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//加载配置文件出错

        } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//不支持的设备厂商

        } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程

        } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {//反射调用出错

        }
        Log.d(getClass().getSimpleName(), "return value: " + nres);

    }

    /*
     * 方法调用
     *
     * */
    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    /*
     * 获取相应id
     *
     * */
//    @Override
//    public void onSupport(IdSupplier _supplier) {
//        if (_supplier == null) {
//            return;
//        }
//        oaid = _supplier.getOAID();
//        vaid = _supplier.getVAID();
//        aaid = _supplier.getAAID();
//        StringBuilder builder = new StringBuilder();
//        builder.append("support: ").append(_supplier.isSupported() ? "true" : "false").append("\n");
//        builder.append("OAID: ").append(oaid).append("\n");
//        builder.append("VAID: ").append(vaid).append("\n");
//        builder.append("AAID: ").append(aaid).append("\n");
//        String idstext = builder.toString();
//        if (_listener != null) {
//            _listener.OnIdsAvalid(_supplier.isSupported(), oaid, vaid, aaid);
//        }
//    }

    @Override
    public void OnSupport(boolean b, IdSupplier _supplier) {
        if (_supplier == null) {
            return;
        }
        oaid = _supplier.getOAID();
        vaid = _supplier.getVAID();
        aaid = _supplier.getAAID();
        StringBuilder builder = new StringBuilder();
        builder.append("support: ").append(_supplier.isSupported() ? "true" : "false").append("\n");
        builder.append("OAID: ").append(oaid).append("\n");
        builder.append("VAID: ").append(vaid).append("\n");
        builder.append("AAID: ").append(aaid).append("\n");
        String idstext = builder.toString();
        if (_listener != null) {
            _listener.OnIdsAvalid(_supplier.isSupported(), oaid, vaid, aaid);
        }
    }

    public interface AppIdsUpdater {
        void OnIdsAvalid(boolean isSupport, String oaid, String vaid, String aaid);
    }


    public static String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static boolean getFalseBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

}
