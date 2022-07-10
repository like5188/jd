package com.common.library

import com.common.library.bean.RegexBean
import com.common.library.bean.UpLoadBean
import com.zhw.http.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by *** on 2021/3/16 9:43 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
interface BaseNetApiService {




    @FormUrlEncoded
    @POST("/s_public/upload/state")
    suspend fun regexMd5(
        @Field("safe") safe: Int = 0,
        @Field("key") key: String,
    ): ApiResponse<RegexBean>

    @POST("/s_public/upload/file")
    suspend fun upload(
        @Body body:RequestBody
    ): ApiResponse<UpLoadBean>

    /**
     * 上传单张图片
     */
    @Multipart
    @POST("/task/task/uploadFile")
    suspend fun uploadFile(@Part part: MultipartBody.Part): ApiResponse<String>;

    /**
     * 上传单张图片
     */
    @Multipart
    @POST("/api/upload.photos")
    suspend fun uploadFiles( @Part("type") type: RequestBody,@Part part: List<MultipartBody.Part>): ApiResponse<List<String>>;

    /**
     * 上传单张图片
     */
    @Multipart
    @POST("/api/upload.photos")
    suspend fun uploadFile( @Part("type") type: RequestBody,@Part part: MultipartBody.Part): ApiResponse<List<String>>;


    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>

    @FormUrlEncoded
    @POST("")
    suspend fun testApi(@Field("source") source: String = "android"): ApiResponse<List<String>>

}