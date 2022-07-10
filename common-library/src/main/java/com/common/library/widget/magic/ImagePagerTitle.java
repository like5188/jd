package com.common.library.widget.magic;

import android.content.Context;
import android.widget.ImageView;


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
public class ImagePagerTitle extends CommonPagerTitleView {

    ImageView imageView;
    int selectedImage;
    int unselectedImage;

    public ImagePagerTitle(Context context, int selectedImage, int unselectedImage) {
        super(context);
        setContentView(R.layout.base_simple_imagelayout);
        imageView = findViewById(R.id.image);
        this.selectedImage = selectedImage;
        this.unselectedImage = unselectedImage;
    }

    @Override
    public void onSelected(int index, int totalCount) {
        imageView.setImageResource(selectedImage);

    }

    @Override
    public void onDeselected(int index, int totalCount) {
        imageView.setImageResource(unselectedImage);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }

    public void setTitleText(CharSequence text) {

    }
}
