/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.library.knet;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.zhw.http.AppException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class MyResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String string = value.string();
            JSONObject jsonObject = new JSONObject(string);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == 200) {
                String data = jsonObject.getString("data");
                if (data == null) {
                    //这个看具体业务系统情况返回
                    return (T) new Object();
                }
                T result;
                try {
                    result = adapter.fromJson(data);
                } catch (Exception e) {
                    throw e;
                } finally {
                    value.close();
                }
                return result;
            } else {
                value.close();
                throw new AppException(code, msg, "");
            }
        } catch (JSONException e) {
            throw new AppException(404, "json parse fail", "");
        }
    }
}
