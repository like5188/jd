package com.cq.jd.start;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.common.library.BaseApp;
import com.common.library.util.sp.SpConstant;
import com.common.library.util.wx.Constants;
import com.cq.jd.start.tools.OaidHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;


/**
 * 因优量汇现规则为包名唯 一性，改版后的广告接口调整为以下：
 * 1、直接接优量汇官方接口：开屏广告、所有激励视频、信息流广告
 * 2、神蓍接口：趣儿快手短视频
 * --------------------------------------
 * 1、激励视频包括：领取奖励；红包；签到；大转盘；分享乐；
 * 2、信息流广告包括：“任务”页面下方；“我的”页面中间；“领取奖励”页面下方；“日常任务”页面下方；“我的授权”页面下方
 */

public class App extends BaseApp {

    private static App instance;

    public long adRewardTime = 0;
    public long adLookRewardTime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        //微信注册
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        api.registerApp(Constants.WX_APP_ID);
//        Utils.init(getApplicationContext());
        initSdk();

//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
//        strategy.setAppChannel(EmulatorCheckUtil.getSingleInstance().getProperty("ro.hardware"));
//        CrashReport.initCrashReport(getApplicationContext(), Constants.WX_NUG_ID, false);

    }

    public static App getInstance() {
        return instance;
    }

    public void initSdk() {
        initThirdPush();
        if (SPUtils.getInstance().getString(SpConstant.isAgree).equals("1")) {
            //获取oaid
            new OaidHelper(this, (isSupport, oaid, vaid, aaid) -> {
                SPUtils.getInstance().put(SpConstant.OAID_SUPPORT, isSupport);
                if (isSupport) {
                    SPUtils.getInstance().put(SpConstant.OAID, oaid);
                }
            });
            initUm();
            initX5Sdk();
        }else {
            UMConfigure.preInit(this, com.common.library.BuildConfig.UMENG_KEY, getChannelName(this));
            QbSdk.disableSensitiveApi();
        }
    }


    /**
     * TAG:MPS
     */
    private void initThirdPush() {
//        PushServiceFactory.init(getApplicationContext());
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
//        pushService.setLogLevel(CloudPushService.LOG_DEBUG);
//        pushService.register(this, new CommonCallback() {
//            @Override
//            public void onSuccess(String response) {
//                Log.d("shit", "init cloudchannel success");
//
//
//            }
//
//            @Override
//            public void onFailed(String errorCode, String errorMessage) {
//                Log.d("shit", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
//            }
//        });
//
//        HuaWeiRegister.register(this);
//
//        // vivo通道注册
//        VivoRegister.register(getApplicationContext());
//        OppoRegister.register(getApplicationContext(), "a9368b783cc34e809289de749d0e9a02", "9efb86c1263f43578d98f6b00e21e177");
//        MeizuRegister.register(getApplicationContext(), "145064", "5d239076c19e43c0a26498acf74fb9a4");
//        MiPushRegister.register(getApplicationContext(), "2882303761520022898", "5272002222898");
    }

    private void initUm() {
        //友盟预初始化
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);

        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        Log.e("shit", "=====>: " + getChannelName(this));
        UMConfigure.init(getApplicationContext(), com.common.library.BuildConfig.UMENG_KEY,
                getChannelName(this), UMConfigure.DEVICE_TYPE_PHONE, "");
        //设置微信的信息
//        PlatformConfig.setWeixin(ApiConstant.WXCHAT_APP_ID, ApiConstant.WECHAT_APP_SECRET);
        //设置QQ的信息
//        PlatformConfig.setQQZone(CfConfig.QQ_APP_ID, CfConfig.QQ_APP_SECRET);
//        PlatformConfig.setWXFileProvider("com.yuwu.group.mall.fileprovider");
    }

    // 获取渠道工具函数
    public static String getChannelName(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.get("UMENG_CHANNEL") + "";
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(channelName)) {
            channelName = "Unknown";
        }
        return channelName;
    }

}
