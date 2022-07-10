plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    compileSdk = BuildVersions.compileSdk

    defaultConfig {
        minSdk = BuildVersions.minSdk
        targetSdk = BuildVersions.targetSdk
        buildConfigField("String", "UMENG_KEY", "\"${ThirdConfig.UMENG_KEY}\"")
        buildConfigField("String", "WX_APP_ID", "\"${ThirdConfig.WX_APP_ID}\"")
//        buildConfigField("String", "TENCENT_APP_KEY", "\"${ThirdConfig.TENCENT_APP_KEY}\"")

//        manifestPlaceholders.apply {
//            put("", "")
//            put("", "")
//        }
    }

    buildTypes {
        this.getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
//        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.compose_version
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        create("this") {
        }
    }

}

dependencies {
    api("org.aspectj:aspectjrt:1.9.5")

    api(Dependencies.kotlin_jdk)
    api(Dependencies.kotlin)
    api(Dependencies.ktx_activity)
    api(Dependencies.ktx_lifecycle_view_model)
    api(Dependencies.ktx_lifecycle_runtime)
    api(Dependencies.ktx_fragment)

    api(Dependencies.appcompat)
    api(Dependencies.androidX_core)
    api(Dependencies.constraintlayout)
    api(Dependencies.design)
    api(Dependencies.paging3_runtime)

    api(Dependencies.BaseRecyclerViewAdapterHelper)
    api(Dependencies.smart_refresh)           //核心必须依赖
    api(Dependencies.smart_header_classics)   //经典刷新头
    api(Dependencies.smart_header_material)   //谷歌刷新头
    api(Dependencies.swiperefreshlayout)   //谷歌刷新头
    api(Dependencies.StateLayout)   //谷歌刷新头


    //tablayout
    api(Dependencies.MagicIndicator)

    api(Dependencies.banner)
    api(Dependencies.X_POPUP)

    api(Dependencies.autoSize)


    api(Dependencies.WX_SDK)
    api(Dependencies.TENCENT_X5)

    //滚动选择组件
    api(Dependencies.PICKER)

    // PictureSelector 基础 (必须)
    api(Dependencies.PICTURE_SELECTOR)
    // 图片裁剪 (按需引入)
    api(Dependencies.PICTURE_UCROP)
    // 图片压缩 (按需引入)
//    implementation 'io.github.lucksiege:compress:v3.0.9'
    // 自定义相机 (按需引入)
//    implementation 'io.github.lucksiege:camerax:v3.0.9'


    //状态栏
    api(Dependencies.immersionbar)
    api(Dependencies.immersionbar_ktx)
    api(Dependencies.immersionbar_components)

    //umeng
    api(Dependencies.UMENG_COMMON)
    api(Dependencies.UMENG_ASMS) // asms包依赖(必选)
    api(Dependencies.UMENG_APM) //  native crash包依赖(必选)

    //路由
    api(Dependencies.arouter)
    kapt(Dependencies.arouter_compiler)

    //分包
    api(Dependencies.multidex)
    //工具类
    api(Dependencies.utilcodex)


    //协程
    api(Dependencies.kotlin_coroutines_core)
    api(Dependencies.kotlin_coroutines_android)

    //权限
    api(Dependencies.XX_PERMISSION)

    //事件分发
    api(Dependencies.EVENT_BUS)
    api(Dependencies.LIVE_BUS)

    //json
    api(Dependencies.gson)

    //glide
    kapt(Dependencies.glide_compiler)
    api(Dependencies.glide)


    api(Dependencies.retrofit_log)
    api(Dependencies.gson)
    api(Dependencies.retrofit_log)
    api(Dependencies.retrofit_cover_gson)

    api(Dependencies.retrofit2_kotlin_coroutines_adapter)

    api(project(":http"))
    api(project(":z_oaid"))
    api(project(":share"))
//    api("com.squareup.retrofit2:adapter-rxjava2:2.5.0")
//    api("io.reactivex.rxjava2:rxandroid:2.1.1")
//    api("io.reactivex.rxjava2:rxjava:2.2.7")

    api(Dependencies.SHADOW)

    testApi(Dependencies.junit)
    androidTestApi(Dependencies.androidJUnitRunner)
    androidTestApi(Dependencies.androidJUnitEspresso)
}