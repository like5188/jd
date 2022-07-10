package com.common.library.liveData.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.tencent.smtt.sdk.ui.dialog.widget.RoundImageView;


/**
 * Created by *** on 2021/1/15
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
public class ImageViewAdapter {

    @BindingAdapter("src")
    public static void setSrc(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("src")
    public static void setSrc(RoundImageView imageView, String url) {
//        ImgLoader.display(url, imageView);
    }

    @BindingAdapter("src")
    public static void setSrc(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }

    @BindingAdapter("src")
    public static void setSc(ImageView imageView, String url) {
//        ImgLoader.display(url, imageView);
    }

    @BindingAdapter("imageUrl")
    public static void setSrc(ImageView imageView, String url) {
//        ImgLoader.display(url, imageView);
    }
    @BindingAdapter("avatar")
    public static void setAvatar(ImageView imageView, String url) {
//        ImgLoader.displayAvatar(url, imageView);
    }

//    @BindingAdapter("visibility")
//    public static void setVisibility(ImageView imageView, int visible) {
//       imageView.setVisibility(visible);
//    }

    /**
     * @param imageView
     * @param url
     * @param placeholder
     * @param errorDrawable
     * @BindingAdapter(value={"imageUrl", "placeholder"}, requireAll=false)
     * requireAll 表示是否可以不用都设置，true代表必须都设置，反之不需要  默认true
     */
    @BindingAdapter({"imageUrl", "placeHolder", "error"})
    public static void loadImage(ImageView imageView, String url, Drawable placeholder, Drawable errorDrawable) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(placeholder)
                .error(errorDrawable)
                .into(imageView);

    }


}