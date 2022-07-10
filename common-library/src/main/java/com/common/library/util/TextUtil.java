package com.common.library.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

/**
 * Created by zhubo on 2020/7/29
 * Supported By 甜瓜移动.
 * Official Website: www.melonmobile.cn.
 * Describe 跳转 文字样式
 *
 * @author zhubo
 */
public class TextUtil {

    /**
     * 为文字的后面加上图片
     *
     * @param str
     * @param context
     * @param imgId
     * @return
     */
    public static SpannableString insertImage(CharSequence str, final Context context, final int imgId) {
        DynamicDrawableSpan drawableSpan2 = new DynamicDrawableSpan(

                DynamicDrawableSpan.ALIGN_BOTTOM) {

            @Override

            public Drawable getDrawable() {

                Drawable d = context.getResources().getDrawable(imgId);

                d.setBounds(0, 0, 50, 50);

                return d;

            }

        };
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(drawableSpan2, str.length(), str.length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    /**
     * 更改文字在 start 和 end  的样式
     *
     * @param context
     * @param str
     * @param styleId
     * @param start
     * @param end
     * @return
     */
    public static SpannableString changTextStyle(Context context, CharSequence str, int styleId, int start, int end) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new TextAppearanceSpan(context, styleId), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }


    /**
     * 为文字中start 到end 添加点击事件
     * 在最后  改控件必须添加view.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
     *
     * @param str
     * @param start
     * @param end
     * @param clickableSpan
     * @return
     */
    public static SpannableString setClickText(CharSequence str, int start, int end, ClickableSpan clickableSpan) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 改变文字start 到end 的颜色
     *
     * @param str
     * @param colorId
     * @param start
     * @return
     */
    public static SpannableString setSpsColor(CharSequence str, int colorId, int start) {
        return setSpsColor(str,colorId,start,str.length());
    }

    /**
     * 改变文字start 到end 的颜色
     *
     * @param str
     * @param colorId
     * @param start
     * @param end
     * @return
     */
    public static SpannableString setSpsColor(CharSequence str, int colorId, int start, int end) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(colorId), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置相对大小
     *
     * @param str
     * @return
     */
    public static SpannableString setTextSize(CharSequence str) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new RelativeSizeSpan(0.7f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 更改位置的相对大小
     *
     * @param str
     * @param start
     * @param end
     * @param fa    0.5f
     * @return
     */
    public static SpannableString setTextSize(CharSequence str, int start, int end, float fa) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new RelativeSizeSpan(fa), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 更改文字的绝对大小
     *
     * @param str
     * @param textsize 字体大小
     * @param start
     * @param end
     * @param isdp     是否以dp为单位
     * @return
     */
    public static SpannableString setTextSize(CharSequence str, int start, int end, int textsize, boolean isdp) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(textsize, isdp), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public static void setTextIndentation(TextView tvContent) {
        SpannableStringBuilder span = new SpannableStringBuilder("缩进"+tvContent.getText());
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvContent.setText(span);
    }

}
