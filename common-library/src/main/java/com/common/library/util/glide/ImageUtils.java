package com.common.library.util.glide;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.common.library.BaseApp;

public class ImageUtils {

    public static void releaseImageViewResouce(ImageView imageView) {
//        try {
//            if (imageView == null) return;
//            Drawable drawable = imageView.getDrawable();
//            if (drawable != null && drawable instanceof BitmapDrawable) {
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                }
//            }
//        }catch (Exception e){
//            Log.e("shit", "releaseImageViewResouce: "+e.getMessage() );
//        }

    }


    public static void loadImage(String url, ImageView iv) {
        if(TextUtils.isEmpty(url)){
            iv.setVisibility(View.INVISIBLE);
            return;
        }
        if(iv.getVisibility()!=View.VISIBLE){
            iv.setVisibility(View.VISIBLE);
        }
        if (!isHttp(url)) {
            Glide.with(BaseApp.application)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5")))
                    )
                    .load(url)
                    .into(iv);
        } else {
            Glide.with(BaseApp.application)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5")))
                    )
                    .load(glideUrl(url))
                    .into(iv);
        }

    }

    public static void loadImage(String url, int placeholderRes, ImageView iv) {
        if (!isHttp(url)) {
            Glide.with(BaseApp.application)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(placeholderRes)
                    )
                    .load(url)
                    .into(iv);
        } else {
            Glide.with(BaseApp.application)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5")))
                    )
                    .load(glideUrl(url))
                    .into(iv);
        }

    }

    public static void loadImage(int res, ImageView iv) {
        Glide.with(BaseApp.application)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))))
                .load(res)
                .into(iv);
    }

    public static void loadImage(Drawable res, ImageView iv) {
        Glide.with(BaseApp.application)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))))
                .load(res)
                .into(iv);
    }

    public static void loadImage(Bitmap res, ImageView iv) {
        Glide.with(BaseApp.application)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))))
                .load(res)
                .into(iv);
    }


    public static GlideUrl glideUrl(String url) {
//        Log.e("shit", "glideUrl: "+SPHelper.getInstance().getString(SpConstant.img_referer) );
//        Log.e("shit", "glideUrl: " + url);

        return new GlideUrl(url, new LazyHeaders.Builder()
//                .addHeader("Referer", SPHelper.getInstance().getString(SpConstant.img_referer))
                .build());
    }

    /**
     * 是否是网络图片
     *
     */
    public static boolean isHttp(String path) {
        return !TextUtils.isEmpty(path) && path.startsWith("http") || path.startsWith("https");
    }
}
