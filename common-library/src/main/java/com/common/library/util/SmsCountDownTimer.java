package com.common.library.util;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.MessageFormat;

public class SmsCountDownTimer extends CountDownTimer {
    private final TextView mTextView;
    private String type;
    private boolean isSoing = false;
    private View view;

    /**
     * @param textView          The TextView
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public SmsCountDownTimer(TextView textView, String type, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.type = type;
    }

    public SmsCountDownTimer(TextView textView, View view, String type, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.type = type;
        this.view = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setEnabled(false);
        if (view !=null){
            view.setClickable(false); //设置不可点击
            view.setEnabled(false);
        }
        mTextView.setText(MessageFormat.format(TextUtils.isEmpty(type) ? "{0}s后重新获取" : "剩余{0}s" + type, millisUntilFinished / 1000));  //设置倒计时时间
        isSoing =  true;

//        /**
//         * 超链接 URLSpan
//         * 文字背景颜色 BackgroundColorSpan
//         * 文字颜色 ForegroundColorSpan
//         * 字体大小 AbsoluteSizeSpan
//         * 粗体、斜体 StyleSpan
//         * 删除线 StrikethroughSpan
//         * 下划线 UnderlineSpan
//         * 图片 ImageSpan
//         * http://blog.csdn.net/ah200614435/article/details/7914459
//         */
//        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
//        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
//        /**
//         * public void setSpan(Object what, int start, int end, int flags) {
//         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
//         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
//         */
//        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
//        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        isSoing = false;
        mTextView.setText(TextUtils.isEmpty(type) ? "获取验证码" : "立即领取");
        mTextView.setClickable(true);//重新获得点击
        mTextView.setEnabled(true);
        if (view !=null){
            view.setClickable(true); //设置不可点击
            view.setEnabled(true);
        }
    }
}