import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.util.Date
import java.text.SimpleDateFormat

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.alibaba.arouter")
//    id ("com.huawei.agconnect")
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

        applicationId = AppVersion.application
//        applicationId = "com.jd.test.android"
        versionCode = AppVersion.versionCode
        versionName = AppVersion.versionName

        testInstrumentationRunner = Dependencies.androidJUnitRunner

        flavorDimensions.add("default")
        //只保留中文
//        resConfigs("zh")
        resourceConfigurations.add("zh")

        this.buildConfigField("boolean", "IS_FIVE_MENU", "false")

        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled = true
        multiDexEnabled = true

        this.manifestPlaceholders.apply {
            // 此处一定要设置成包名，不然无法满足微信对路径的要求
            put("weixin_package", AppVersion.application)
            put("channel_value", "formal")
        }

        ndk {
//            this.abiFilters.add("arm64-v8a")
            this.abiFilters.add("armeabi-v7a")
        }

    }


    signingConfigs {
        this.create("release") {
            keyAlias = "jdthuser"
            keyPassword = "20200519"
            storeFile = file("../jdthuser.jks")
            storePassword = "20200519"
        }
    }

    buildTypes {
        this.getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
            resValue("string", "app_name", AppVersion.appName)
        }

        this.getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
            resValue("string", "app_name", AppVersion.appName_Debug)
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
//        compose = true
    }

    applicationVariants.all application@{
        outputs.all output@{
            val buildName = "_${this@application.name}"
            val simple = SimpleDateFormat("yyyy_MM_dd_HHmmss")
            val format = simple.format(Date())
            val outputFileName = "apk_${applicationId}_${versionName}_$format$buildName.apk"
            (this@output as BaseVariantOutputImpl).outputFileName = outputFileName
        }
    }

}

dependencies {


    implementation(project(":user"))
    implementation(project(":map"))
    implementation(project(":common-library"))

    api(Dependencies.arouter)
    implementation("androidx.test.ext:junit-ktx:1.1.3")
    kapt(Dependencies.arouter_compiler)

    testImplementation(Dependencies.junit)

    if (BuildVersions.needGoodsModule) {
        implementation(project(":goods"))
    }
    if (BuildVersions.needOrderModule) {
        implementation(project(":order"))
    }
    if (BuildVersions.needSetupModule) {
        implementation(project(":setup"))
    }
    implementation(project(":shop"))
//    implementation(project(":app"))
//    implementation(project(":map"))
//    api ("com.huawei.hms:scan:2.5.0.300")
}