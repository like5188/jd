package com.common.library.knet

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by *** on 2022/2/25 3:25 下午
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
data class HttpResponse(@SerializedName("code") val code: Int,@SerializedName("msg") val msg: String) : Serializable