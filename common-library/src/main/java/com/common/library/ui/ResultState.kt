package com.common.library.ui

import android.content.Context
import android.net.ParseException
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.zhw.http.ApiResponse
import com.zhw.http.AppException
import com.zhw.http.BaseResResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by *** on 2021/3/10 10:35 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe  结果集封装类
 *
 * @author 又是谁写的
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T): ResultState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage: String): ResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: AppException): ResultState<T> = Error(error)
    }

    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: AppException) : ResultState<Nothing>()
}

fun <T> parseResult(result: ApiResponse<T>, interrupt: (T) -> Unit): ResultState<T> {
    return if (result.isSuccess) {
        interrupt(result.responseData)
        ResultState.onAppSuccess(result.responseData)
    } else {
        ResultState.onAppError(AppException(result.responseCode, result.responseMsg))
    }
}

suspend fun <T> doResult(
    block: suspend () -> ApiResponse<T>,
    interrupt: (T) -> Unit = {},
): ResultState<T> {
    val block1 = block.invoke()
    return if (block1.isSuccess) {
        interrupt(block1.responseData)
        ResultState.onAppSuccess(block1.responseData)
    } else {
        ResultState.onAppError(AppException(block1.responseCode, block1.responseMsg))
    }
}

suspend fun <T> doResultNoData(block: suspend () -> ApiResponse<T>,  interrupt: (T) -> Unit = {}): ResultState<String> {
    val block1 = block.invoke()
    return if (block1.isSuccess) {
        interrupt(block1.responseData)
        ResultState.onAppSuccess("success")
    } else {
        ResultState.onAppError(AppException(block1.responseCode, block1.responseMsg))
    }
}


fun <T>  doAppFlow(
    block: suspend () -> ApiResponse<T>,
    loadHint: String? = null,
    interrupt: (T) -> Unit = {},
): Flow<ResultState<T>> =
    flow {
        emit(doResult(block, interrupt))
    }.onStart {
        loadHint?.let {
            emit(ResultState.onAppLoading(it))
        }
    }.catch {
        val appException = AppException(handleResponseError(it), it);
        emit(ResultState.onAppError(appException))
}

fun <T> doAppEmptyFlow(
    block: suspend () -> ApiResponse<T>,
    loadHint: String? = null,
    interrupt: (T) -> Unit = {},
): Flow<ResultState<String>> = flow {
    emit(doResultNoData(block, interrupt))
}.onStart {
    loadHint?.let {
        emit(ResultState.onAppLoading(it))
    }
}.catch {
    val appException = AppException(handleResponseError(it), it);
    emit(ResultState.onAppError(appException))
}


/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: BaseResResponse<T>) {
    value = if (result.isSuccess()) ResultState.onAppSuccess(result.responseData) else
        ResultState.onAppError(AppException(result.responseCode, result.responseMsg))
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.paresException(e: Throwable) {
    Log.e("exception", "error:==" + e.message)
    this.value = ResultState.onAppError(AppException(handleResponseError(e), e))
}

fun handleResponseError(t: Throwable): String {
    LogUtils.e("handleResponseError" + t.localizedMessage + "\n" + t.message)
    return when (t) {
        is AppException->{
            t.msg
        }
        is UnknownHostException -> {
            "网络不可用"
        }
        is SocketTimeoutException -> {
            "请求网络超时"
        }
        is HttpException -> {
            convertStatusCode(t)
        }
        is JsonParseException, is ParseException, is JSONException, is JsonIOException -> {
            "数据解析错误"
        }
        else -> {
            "未知问题"
        }
    }
}

fun convertStatusCode(httpException: HttpException): String {
    return when {
        httpException.code() >= 500 -> {
            "服务器发生错误"
        }
        httpException.code() == 404 -> {
            "请求地址不存在"
        }
        httpException.code() == 403 -> {
            "请求被服务器拒绝"
        }
        httpException.code() == 307 -> {
            "请求被重定向到其他页面"
        }
        httpException.code() == 400 -> {
            "请求错误"
        }
        else -> {
            "网络问题"
        }
    }

}
