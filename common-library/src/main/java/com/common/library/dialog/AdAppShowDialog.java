package com.common.library.dialog;//package com.common.library.dialog;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.common.library.BaseApp;
//import com.common.library.R;
//import com.common.library.util.ViewUtils;
//import com.common.library.util.glide.ImageUtils;
//import com.common.library.widget.imageview.RoundedImageView;
//
//
///**
// * 系统公告
// */
//public class AdAppShowDialog extends BaseDialog {
//
//    private StandardGSYVideoPlayer gsyVideoPlayer;
//    private ScrollView scContent;
//    private RoundedImageView ivAdImage;
//    private TextView tvText;
//    private TextView tvTitle;
//    private TextView tvNow;
//    private LinearLayout llTitle;
//    private int jumpType;
//    private String jump_android_url;
//
//    public AdAppShowDialog(@NonNull Context context) {
//        super(context);
//    }
//
//    @Override
//    int layoutResId() {
//        return R.layout.dialog_simple_play;
//    }
//
//    @Override
//    void initView(Context context) {
//        gsyVideoPlayer = findViewById(R.id.video_player);
//        scContent = findViewById(R.id.scContent);
//        ivAdImage = findViewById(R.id.ivAdImage);
//        tvTitle = findViewById(R.id.tvTitle);
//        tvText = findViewById(R.id.tvText);
//        llTitle = findViewById(R.id.llTitle);
//        tvNow = findViewById(R.id.tvNow);
//        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
//        gsyVideoPlayer.setIfCurrentIsFullscreen(false);
//    }
//
//    public void onPause() {
//        gsyVideoPlayer.onVideoPause();
//    }
//
//    public void onResume() {
//        gsyVideoPlayer.onVideoResume();
//    }
//
//    private void onDestroy() {
//        GSYVideoManager.releaseAllVideos();
//    }
//
//    /**
//     * 视屏
//     */
//    public void showVideo(int jumpType, String jump_android_url, String url) {
//        this.jumpType = jumpType;
//        this.jump_android_url = jump_android_url;
//        scContent.setVisibility(View.GONE);
//        llTitle.setVisibility(View.GONE);
//        gsyVideoPlayer.setVisibility(View.VISIBLE);
//        tvNow.setVisibility(View.VISIBLE);
//        gsyVideoPlayer.setUp(url, false, "");
//    }
//
//    /**
//     * 文字
//     * @param jumpType 跳转类型
//     * @param jump_android_url android 跳转地址
//     * @param title 文字
//     * @param text 内容
//     */
//    public void showText(int jumpType, String jump_android_url,String title, String text) {
//        this.jumpType = jumpType;
//        this.jump_android_url = jump_android_url;
//        scContent.setVisibility(View.VISIBLE);
//        gsyVideoPlayer.setVisibility(View.GONE);
//        tvNow.setVisibility(View.VISIBLE);
//        ivAdImage.setVisibility(View.GONE);
//        llTitle.setVisibility(View.VISIBLE);
//        tvTitle.setText(title);
//        tvText.setText(text);
//    }
//
//    /**
//     *  图片
//     */
//    public void showImage(int jumpType, String jump_android_url, String text) {
//        this.jumpType = jumpType;
//        this.jump_android_url = jump_android_url;
//        scContent.setVisibility(View.VISIBLE);
//        gsyVideoPlayer.setVisibility(View.GONE);
//        tvNow.setVisibility(View.GONE);
//        ivAdImage.setVisibility(View.VISIBLE);
//        llTitle.setVisibility(View.GONE);
//        ImageUtils.loadImage(text, ivAdImage);
//    }
//
//    @Override
//    void initListener() {
//        findViewById(R.id.ivClose).setOnClickListener(v -> dismiss());
//        ivAdImage.setOnClickListener(v -> {
//            jumpGotoPage();
//            dismiss();
//        });
//        tvNow.setOnClickListener(v -> {
//            jumpGotoPage();
//            dismiss();
//        });
//    }
//
//    /**
//     * 跳转类型 0不跳转 1内链-原生 2内连-h5 3外链
//     */
//    private void jumpGotoPage() {
//        switch (jumpType) {
//            case 0:
//                break;
//            case 1:
//                getContext().startActivity(new Intent(jump_android_url));
//                break;
//            case 2:
//                BrowserActivity.Companion.forwardH5(BaseApp.application,jump_android_url);
//                break;
//            case 3:
//                ViewUtils.openBrowser(BaseApp.application,jump_android_url);
//                break;
//        }
//    }
//
//    @Override
//    public void dismiss() {
//        onDestroy();
//        super.dismiss();
//    }
//}
