plugins {
    if (BuildVersions.needOrderModule) {
        id("com.android.library")
    } else {
        id("com.android.application")
    }
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.alibaba.arouter")
}
kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    compileSdk = BuildVersions.compileSdk

    defaultConfig {
//        if (!BuildVersions.needOrderModule) {
//            applicationId = "com.cq.jd.order"
//        }
        minSdk = BuildVersions.minSdk
        targetSdk = BuildVersions.targetSdk

        testInstrumentationRunner = Dependencies.androidJUnitRunner

        //只保留中文
        resConfigs("zh")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    sourceSets {
        this.getByName("main") {
            if (BuildVersions.needOrderModule) {
                resources.srcDirs("src/main/res")
                manifest.srcFile("src/main/AndroidManifestLib.xml")
            } else {
                java.srcDirs("src/main/app_java")
                resources.srcDirs("src/main/app_res")
                manifest.srcFile("src/main/AndroidManifest.xml")
            }
        }
    }

}

dependencies {
    implementation(project(":common-library"))
    api(Dependencies.arouter)
    kapt(Dependencies.arouter_compiler)
}