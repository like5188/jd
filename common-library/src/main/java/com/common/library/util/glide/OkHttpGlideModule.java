package com.common.library.util.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.GlideUrl;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 解决Glide  调用https图片显示不出来的问题需配合AndroidManifest.xml中的meta-data使用
 * <meta-data
 * android:name="com.sf.glidehttps.OkHttpGlideModule"
 * android:value="GlideModule"/>
 */
public class OkHttpGlideModule implements com.bumptech.glide.module.GlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // Do nothing.
    }

    @Override
    public void registerComponents(@NotNull Context context, @NotNull Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory((Call.Factory) getHttpClient()));
    }

    // 简化示意的初始化代码
    public static OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLUtil.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        return builder.build();
    }
}
