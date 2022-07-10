package com.common.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.library.R;


public abstract class BaseDialog extends Dialog {

    public OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.DialogTheme);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public boolean cancelAble() {
        return false;
    }

    public int gravity() {
        return Gravity.CENTER;
    }

    private void init(Context context) {
        setContentView(layoutResId());
        setCancelable(cancelAble());
        setCanceledOnTouchOutside(cancelAble());
        //有些手机dialog宽高显示不正常 需要手动设置
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = getHeightPx();
        params.gravity = gravity();
        window.setWindowAnimations(R.style.AnimCenter);
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView(context);

        initListener();
    }

    protected abstract int layoutResId();

    protected abstract void initView(Context context);

    protected abstract void initListener();

    public int getHeightPx() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }


    public interface OnConfirmListener {
        void onConfirm(int type); //1 支付宝 2 微信
    }
}
