package com.common.library.knet

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by *** on 2022/2/25 3:04 下午
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class MyConverterFactory() : Converter.Factory() {


    companion object {
        var gson: Gson? = null

        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and decoding from JSON
         * (when no charset is specified by a header) will use UTF-8.
         */
        // Guarding public API nullability.
        fun create(gson: Gson): MyConverterFactory {
            return MyConverterFactory(gson = gson)
        }

        fun create(): MyConverterFactory {
            return create(Gson())
        }

        private fun MyConverterFactory(gson: Gson): MyConverterFactory {
            Companion.gson = gson
            return MyConverterFactory()
        }

    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit,
    ): Converter<ResponseBody, *> {
        val adapter = gson?.getAdapter(TypeToken.get(type)) as TypeAdapter<Any>
        return MyResponseBodyConverter<Any>(gson, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody> {
        val adapter = gson?.getAdapter(TypeToken.get(type)) as TypeAdapter<Any>
        return MyRequestBodyConverter<Any>(gson, adapter)
    }
}