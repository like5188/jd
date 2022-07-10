object Dependencies {

    const val appcompat = "androidx.appcompat:appcompat:1.3.1"
    const val design = "com.google.android.material:material:1.4.0"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val annotations = "androidx.annotation:annotation:1.3.1"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.3.1"
    const val androidX_core = "androidx.core:core-ktx:1.3.2"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val ktx_fragment = "androidx.fragment:fragment-ktx:1.4.0"
    const val ktx_activity = "androidx.activity:activity-ktx:1.4.0"
    const val ktx_lifecycle_view_model = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
    const val ktx_lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"

    //网络框架
    const val okHttp = "com.squareup.okhttp3:okhttp:4.7.2"
    const val okHttp_logging = "com.squareup.okhttp3:logging-interceptor:4.4.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val retrofit_cover_gson = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val retrofit_log = "com.github.ihsanbal:LoggingInterceptor:3.1.0"

    //   /kotlin
    const val kotlin_jdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:1.6.10"

    //compose_version
    const val compose_version = "1.0.5"

    const val compose_ui = "androidx.compose.ui:ui:$compose_version"
    const val compose_compiler = "androidx.compose.compiler:compiler:$compose_version"
    const val compose_material = "androidx.compose.material:material:$compose_version"
    const val compose_ui_tools = "androidx.compose.ui:ui-tooling-preview:$compose_version"
    const val compose_ui_tool = "androidx.compose.ui:ui-tooling:$compose_version"
    const val compose_activity = "androidx.activity:activity-compose:1.4.0"
    const val compose_runtime_livedata =
        "androidx.compose.runtime:runtime-livedata:$compose_version"
    const val compose_constraint = "androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02"
    const val compose_lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0"
    const val compose_paging3 = "androidx.paging:paging-compose:1.0.0-alpha14"
    const val paging3_runtime = "androidx.paging:paging-runtime-ktx:3.1.1"
    const val compose_swiper = "com.google.accompanist:accompanist-swiperefresh:0.20.3"
    const val compose_viewpager = "com.google.accompanist:accompanist-pager:0.20.3"


    //compose 图片加载框架
    const val compose_coil = "io.coil-kt:coil-compose:1.4.0"

    //  协程相关
    const val kotlin_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1"
    const val kotlin_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1"
    const val retrofit2_kotlin_coroutines_adapter =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"


    //tools
    const val gson = "com.google.code.gson:gson:2.8.6"
    const val utilcodex = "com.blankj:utilcodex:1.30.6"
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val autoSize = "me.jessyan:autosize:1.2.1"
    const val glide = "com.github.bumptech.glide:glide:4.12.0"
    const val glide_compiler = "com.github.bumptech.glide:compiler:4.12.0"

    //  路由
    const val arouter = "com.alibaba:arouter-api:1.5.1"
    const val arouter_compiler = "com.alibaba:arouter-compiler:1.5.1"

    //事件分发
    const val LIVE_BUS = "io.github.jeremyliao:live-event-bus-x:1.8.0"
    const val EVENT_BUS = "org.greenrobot:eventbus:3.2.0"

    //navigation 导航
    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:2.3.3"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:2.3.3"

    //状态栏工具
    const val immersionbar = "com.gyf.immersionbar:immersionbar:3.0.0"
    const val immersionbar_ktx = "com.gyf.immersionbar:immersionbar-ktx:3.0.0"
    const val immersionbar_components = "com.gyf.immersionbar:immersionbar-components:3.0.0"

    //适配器
    const val BaseRecyclerViewAdapterHelper =
        "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.6"

    //刷新控件
    const val smart_refresh = "com.scwang.smart:refresh-layout-kernel:2.0.3"
    const val smart_header_classics = "com.scwang.smart:refresh-header-classics:2.0.3"
    const val smart_header_material = "com.scwang.smart:refresh-header-material:2.0.3"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    const val StateLayout = "com.github.liangjingkanji:StateLayout:1.2.0"

//    const val circleimageview = "de.hdodenhof:circleimageview:3.1.0"// 用ImageFilterView 替换

    //工具类
    const val ZXING = "cn.yipianfengye.android:zxing-library:2.2"//二维码生成器

    //自定义控件
    const val MagicIndicator = "com.github.hackware1993:MagicIndicator:1.7.0"//tab
    const val banner = "com.youth.banner:banner:2.1.0"//banner
    const val flexbox = "com.google.android.flexbox:flexbox:3.0.0"
//    const val PICKER = "com.contrarywind:Android-PickerView:4.1.9"//三级联动
    const val PICKER = "com.github.gzu-liyujiang.AndroidPicker:WheelPicker:4.1.7"//滚动控件
    const val BADGE_VIEW = "q.rorbin:badgeview:1.1.3"//角标

    //    const val ROUNDED_IMAGE_VIEW = "com.makeramen:roundedimageview:2.3.0"//带圆角边框的控件 用户 ImageFilterView替换
    const val X_POPUP = "com.github.li-xiaojun:XPopup:2.8.0"//弹框样式
    const val SHADOW = "com.github.lihangleo2:ShadowLayout:3.2.4"//阴影


    // unit
    const val junit = "junit:junit:4.13.2"
    const val androidJUnitRunner = "androidx.test.ext:junit:1.1.3"
    const val androidJUnitEspresso = "androidx.test.espresso:espresso-core:3.4.0"

    //权限
    const val PERMISSION = "com.yanzhenjie:permission:2.0.3"
    const val XX_PERMISSION = "com.github.getActivity:XXPermissions:13.6"

    //图片选择框架
//    const val PICTURE_SELECTOR = "com.github.LuckSiege.PictureSelector:picture_library:v2.6.0"
    const val PICTURE_SELECTOR = "io.github.lucksiege:pictureselector:v3.0.9"
    const val PICTURE_UCROP = "io.github.lucksiege:ucrop:v3.0.9"

    //第三方SDK以下
    //umeng统计
    const val UMENG_COMMON = "com.umeng.umsdk:common:9.4.4"
    const val UMENG_ASMS = "com.umeng.umsdk:asms:1.4.1" // asms包依赖(必选)
    const val UMENG_APM = "com.umeng.umsdk:apm:1.5.2"// native crash包依赖(必选)

    //umeng分享
    const val UMENG_SHARE_CORE = "com.umeng.umsdk:share-core:7.1.4"
    const val UMENG_SHARE_BOARD = "com.umeng.umsdk:share-board:7.1.4"
    const val UMENG_SHARE_WX = "com.umeng.umsdk:share-wx:7.1.4"
    const val UMENG_SHARE_ALI = "com.umeng.umsdk:share-alipay:7.1.4"

    //微信SDK
    const val WX_SDK = "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:6.8.0"

    //x5 webView内核
    const val TENCENT_X5 = "com.tencent.tbs:tbssdk:44085"


    //百度定位
    const val BAIDU_LOCATION = "com.baidu.lbsyun:BaiduMapSDK_Location:9.1.8"

    //高德定位
    const val GD_LOCATION = "com.amap.api:location:6.1.0"

    //搜索功能
    const val GD_LOCATION_SEARCH = "com.amap.api:search:9.2.0"

    //腾讯直播SDK
    const val LITE_AV_SDK = "com.tencent.liteav:LiteAVSDK_Professional:9.5.11346"
    const val LITE_AV_PLAYER = "com.tencent.liteav:LiteAVSDK_Player:9.4.10921"

    const val TENCENT_MAP = "com.tencent.map:tencent-map-vector-sdk:4.3.4"
    const val TENCENT_LOCATION = "com.tencent.map.geolocation:TencentLocationSdk-openplatform:7.3.0"
}
