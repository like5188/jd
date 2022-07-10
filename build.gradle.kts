buildscript {
    repositories {
        maven(url="https://developer.huawei.com/repo/")
        google()
        jcenter()
        maven(url= "https://jitpack.io")
//        maven(url="http://maven.aliyun.com/nexus/content/repositories/releases/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.alibaba:arouter-register:1.0.2")
    }
}

allprojects {
    repositories {
        maven(
            url = "https://developer.huawei.com/repo/"
        )
        google()
        jcenter()
        maven(url = "https://jitpack.io")
//        maven( url = "http://maven.aliyun.com/nexus/content/groups/public")
    }
}