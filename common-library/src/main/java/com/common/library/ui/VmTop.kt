package com.common.library.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.common.library.BaseNetApi
import com.common.library.BuildConfig
import com.common.library.bean.ActionResponse
import com.common.library.router.ARouterPath
import com.common.library.router.provider.UserService
import com.common.library.viewModel.BaseViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.smtt.utils.Md5Utils
import com.zhw.http.AppException
import com.zhw.http.BaseResResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

const val LOG_OUT_CODE = 401

/**
 * Created by *** on 2022/5/27 16:47
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */

fun <T> BaseViewModel.requestRs(
    block: suspend () -> BaseResResponse<T>,
    success: ((T) -> Unit)? = null,
    result: MutableLiveData<T>? = null,
    error: ((AppException) -> Unit)? = null,
    loadingMessage: String? = null,
    showErrorToast: Boolean = true,
    repeatTime: Int = 0,//接口重试，只重试网络错误的情况
) {
    //如果需要弹窗 通知Activity/fragment弹窗
    loadingMessage?.let {
        showAppLoading.value = it
    }
    viewModelScope.launch {
        runCatching {
            //请求代码块调度在Io线程中
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            loadingMessage?.let {
                hideAppLoading.value = true
            }
            try {
                //因为要判断请求的数据结果是否成功，失败会抛出自定义异常，所以在这里try一下
//                LogUtils.e("====" + it.responseCode + "==" + it.isSuccess)
//                LogUtils.e("====" + GsonUtils.toJson(it))
                if (it.isSuccess) {
                    success?.invoke(it.responseData)
                    it.responseData?.let { resData ->
                        result?.value = resData
                    }
                } else {
                    val appException = AppException(it.responseCode, it.responseMsg, "返回异常");
                    if (showErrorToast) {
                        hintMsg.value = appException.msg
                    }
                    error?.invoke(appException)
                    if (it.responseCode == LOG_OUT_CODE) {
                        //token失效
//                        val application = getApplication<BaseApplication>()
//                        val eventViewModel =
//                            application.getAppViewModelProvider().get(EventViewModel::class.java)
//                        eventViewModel.tokenInvalid.value = true
//                        LiveEventBus.get<Boolean>("token").post(false)
                        val userService =
                            ARouter.getInstance().build(ARouterPath.User.USER_INFO_SERVICE)
                                .navigation() as UserService
                        userService.logOut()
                    }
                    if (BuildConfig.DEBUG) {
                        LogUtils.e(appException.toString())
                    }
                }
            } catch (e: Exception) {
                //失败回调
                error?.invoke(AppException("解析异常", e))
                if (BuildConfig.DEBUG) {
                    LogUtils.e("解析异常" + e.message)
                }
            }
        }.onFailure {
            //网络请求异常 关闭弹窗
            //关闭弹框
            val appException = AppException(handleResponseError(it), it);
            if (BuildConfig.DEBUG) {
                LogUtils.e(it)
                LogUtils.e(appException.toString())
            }
            if (repeatTime <= 0) {
                loadingMessage?.let {
                    hideAppLoading.value = true
                }
                if (showErrorToast) {
                    hintMsg.value = appException.msg
                }
                error?.invoke(appException)
            } else {
                delay(300)
                requestRs(block = block,
                    success,
                    result,
                    error,
                    loadingMessage,
                    showErrorToast,
                    repeatTime - 1)
            }
        }
    }
}

/**
 * 图片上传功能
 */
fun BaseViewModel.upLoadFile(
    file: File,
    success: ((String) -> Unit)? = null,
    error: ((AppException) -> Unit)? = null,
    loadingMessage: String? = null,
    showErrorToast: Boolean = true,
    mediaType: MediaType? = "image/*".toMediaTypeOrNull(),
) {
    loadingMessage?.let {
        showAppLoading.value = it
    }
    viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                val mD5 = Md5Utils.getMD5(file)
                val suffix = getSuffix(file = file)
                val regexMd5 = BaseNetApi.service.regexMd5(key = "$mD5$suffix")
                if (regexMd5.responseCode == 200) {// 已上传  //404 未上传
                    ActionResponse(regexMd5.responseData.url, -101)
                } else if (regexMd5.responseCode == 404) {
                    val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("uptype", regexMd5.responseData.uptype)
                        .addFormDataPart("safe", "${regexMd5.responseData.safe}")
                        .addFormDataPart("key", regexMd5.responseData.key)
                        .addFormDataPart("file", file.name, file.asRequestBody(mediaType))
                        .build();
                    val upload = BaseNetApi.service.upload(body)
                    if (upload.responseCode == 1) {
                        ActionResponse(upload.responseData.url, -101)
                    } else {
                        ActionResponse(upload.responseMsg, upload.responseCode)
                    }
                } else {
                    ActionResponse(regexMd5.responseMsg, regexMd5.responseCode)
                }
            }
        }.onSuccess {
            LogUtils.v("==${it.code}===${it.url}")
            if (it.code == -101) {
                success?.invoke(it.url)
            } else {
                //todo token 失效逻辑
                val appException = AppException(it.code, it.url)
                error?.invoke(appException)
                if (showErrorToast) {
                    hintMsg.value = appException.msg
                }
            }
            loadingMessage?.let {
                hideAppLoading.value = true
            }

        }.onFailure {
            loadingMessage?.let {
                hideAppLoading.value = true
            }
            val appException = AppException(handleResponseError(it), it)
            error?.invoke(appException)
            if (showErrorToast) {
                hintMsg.value = appException.msg
            }
            LogUtils.v("errr==${it.localizedMessage}")
        }
    }
}

fun getSuffix(file: File): String {
    val filename = file.name;
    if (filename.lastIndexOf(".") == -1) {
        return ""; // 文件没有后缀名的情况
    }
    // 此时返回的是带有 . 的后缀名，
    return filename.substring(filename.lastIndexOf("."));
}



