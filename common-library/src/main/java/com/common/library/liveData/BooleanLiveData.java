package com.common.library.liveData;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by *** on 2021/1/15
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
public class BooleanLiveData extends MutableLiveData<Boolean> {

    @Nullable
    @Override
    public Boolean getValue() {
        if (null == super.getValue()) {
            return false;
        }
        return super.getValue();
    }
}
