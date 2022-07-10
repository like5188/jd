package com.cq.jd.order;


import com.alibaba.android.arouter.launcher.ARouter;
import com.common.library.BaseApp;
import com.common.library.BuildConfig;


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


    }

    public static App getInstance() {
        return instance;
    }


}
