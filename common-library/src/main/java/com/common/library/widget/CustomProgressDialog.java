package com.common.library.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.common.library.R;
import com.common.library.util.glide.ImageUtils;


/**
 * 加载弹窗
 */
public class CustomProgressDialog extends ProgressDialog {

    private Context context;
    private ImageView progress_img;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.base_dialog_loading_progress, null);
        progress_img = (ImageView) view.findViewById(R.id.iv_bg);
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.base_anim_loading_progress);
        //LinearInterpolator lin = new LinearInterpolator();
        // operatingAnim.setInterpolator(lin);
        progress_img.setAnimation(operatingAnim);
        setContentView(view);
    }

    @Override
    public void show() {
        //setCancelable(false);;
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (progress_img != null){
            ImageUtils.releaseImageViewResouce(progress_img);
        }
    }
}