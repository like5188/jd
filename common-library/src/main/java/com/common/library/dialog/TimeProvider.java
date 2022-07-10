/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.common.library.dialog;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据参见 http://www.360doc.com/content/12/0602/07/3899427_215339300.shtml
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/9 11:31
 */
public class TimeProvider implements LinkageProvider {
    private static final String[] ABBREVIATIONS = {};

    @Override
    public boolean firstLevelVisible() {
        return true;
    }

    @Override
    public boolean thirdLevelVisible() {
        return false;
    }

    @NonNull
    @Override
    public List<String> provideFirstData() {
        List<String> provinces = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                provinces.add("0" + i);
            } else {
                provinces.add("" + i);
            }
        }
        return provinces;
    }

    @NonNull
    @Override
    public List<String> linkageSecondData(int firstIndex) {
        List<String> provinces = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                provinces.add("0" + i);
            } else {
                provinces.add("" + i);
            }
        }
        return provinces;
    }

    @NonNull
    @Override
    public List<?> linkageThirdData(int firstIndex, int secondIndex) {
        return new ArrayList<>();
    }

    @Override
    public int findFirstIndex(Object firstValue) {

        return provideFirstData().indexOf(firstValue);
    }

    @Override
    public int findSecondIndex(int firstIndex, Object secondValue) {

        return linkageSecondData(firstIndex).indexOf(secondValue);
    }

    @Override
    public int findThirdIndex(int firstIndex, int secondIndex, Object thirdValue) {
        return 0;
    }

}
