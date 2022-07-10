package com.common.library.liveData.adapter;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

/**
 * Created by *** on 2021/3/9 2:24 PM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
public class EditAdapter {

    /**
     * 密码显示与否
     *
     * @param editText
     * @param show
     */
    @BindingAdapter("show")
    public static void show(EditText editText, boolean show) {
        if (show) {
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        if (editText.hasFocus()) {//是否有焦点，光标移动到最后
            editText.setSelection(editText.getText().length());
        }
    }

    /**
     * 提示语
     *
     * @param editText
     * @param show
     */
    @BindingAdapter("hint")
    public static void show(EditText editText, String show) {

        editText.setHint(show);

    }
}
