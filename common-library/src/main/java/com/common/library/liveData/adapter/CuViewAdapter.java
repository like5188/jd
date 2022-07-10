package com.common.library.liveData.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;



/**
 * Created by *** on 2021/1/26
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
public class CuViewAdapter {

//    @BindingAdapter("right_text")
//    public static void setSrc(MineItemView itemView, CharSequence sequence) {
//        itemView.setRightDesc(sequence);
//    }


    @BindingAdapter("background")
    public static void setBackground(View itemView, int resource) {
        itemView.setBackgroundResource(resource);
    }

    @BindingAdapter("textColor")
    public static void setBackground(TextView itemView, String textColor) {
        itemView.setTextColor(Color.parseColor(textColor));
    }

}
