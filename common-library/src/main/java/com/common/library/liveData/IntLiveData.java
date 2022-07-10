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
public class IntLiveData extends MutableLiveData<Integer> {

    @Nullable
    @Override
    public Integer getValue() {
        if (super.getValue() == null) {
            return 0;
        }
        return super.getValue();
    }
}
