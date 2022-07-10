package com.common.library.liveData;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by *** on 2021/1/15
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
public class StringLiveData extends MutableLiveData<String> {

    public StringLiveData(String value) {
        super(value);
    }

    public StringLiveData() {
    }

    @NonNull
    @Override
    public String getValue() {
        if (null == super.getValue()) {
            return "";
        }
        return super.getValue();
    }
}
