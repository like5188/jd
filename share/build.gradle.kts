plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
//    id("com.alibaba.arouter")
}
//kapt {
//    arguments {
//        arg("AROUTER_MODULE_NAME", project.name)
//    }
//}
android {

    compileSdk = BuildVersions.compileSdk

    defaultConfig {
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

//    composeOptions {
//        kotlinCompilerExtensionVersion = Dependencies.compose_version
//    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

}


dependencies {
    implementation(Dependencies.WX_SDK)
    implementation("io.github.sinaweibosdk:core:11.12.0@aar")
}