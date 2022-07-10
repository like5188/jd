package com.common.library;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.common.library.base.LifecycleHandler;
import com.cq.jd.share.WxApiWrapper;
import com.hjq.permissions.XXPermissions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.smtt.sdk.QbSdk;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

public class BaseApp extends MultiDexApplication {

    public static Application application;

    //static 代码段可以防止内存泄露
    static {
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据了";
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.color_red, android.R.color.white);//全局设置主题颜色
            return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        LiveEventBus.config().autoClear(true).enableLogger(BuildConfig.DEBUG).setContext(this);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//        AutoSizeConfig.getInstance().getUnitsManager()
//                .setSupportDP(false)
//                .setSupportSP(false)
//                .setSupportSubunits(Subunits.MM);
//        if (BuildConfig.IS_DEBUG){//内存泄漏分析
//            LeakCanary.Config config = LeakCanary.getConfig().newBuilder()
//                    .retainedVisibleThreshold(3)
//                    .build();
//            LeakCanary.setConfig(config);
//        }

        // 设置权限申请拦截器
//        XXPermissions.setInterceptor(new PermissionInterceptor());
        this.registerActivityLifecycleCallbacks(new LifecycleHandler());
        WxApiWrapper.Companion.getSInstance().setAppID(this, BuildConfig.WX_APP_ID);
    }

    public void initX5Sdk() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
