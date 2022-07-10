package com.common.library.widget.magic;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.common.library.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

/**
 * Created by *** on 2021/3/23 10:04 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
public class BaseTextPagerTitle extends CommonPagerTitleView {
    TextView tvTitle;

    int selectTextColor = getResources().getColor(R.color.color_333);
    int unSelectTextColor = getResources().getColor(R.color.color_666);

    public BaseTextPagerTitle(Context context, @NonNull String title) {
        this(context, title, 0);
    }

    public BaseTextPagerTitle(Context context, @NonNull String title, int icon) {
        super(context);
        setContentView(R.layout.base_simple_title_text);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        tvTitle.setTextColor(selectTextColor);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        tvTitle.setTextColor(unSelectTextColor);
    }
//
//    @Override
//    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//        tvTitle.setScaleX(1.1f + (0.9f - 1.1f) * leavePercent);
//        tvTitle.setScaleY(1.1f + (0.9f - 1.1f) * leavePercent);
//    }
//
//    @Override
//    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//        tvTitle.setScaleX(0.8f + (1.1f - 0.9f) * enterPercent);
//        tvTitle.setScaleY(0.9f + (1.1f - 0.9f) * enterPercent);
//    }

    public void setTitleText(CharSequence text) {
        if (tvTitle != null) {
            tvTitle.setText(text);
        }
    }

    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
    }

    public void setUnSelectTextColor(int unSelectTextColor) {
        this.unSelectTextColor = unSelectTextColor;
    }
}
