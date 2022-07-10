package com.common.library.bean

/**
 * Created by *** on 2022/6/2 16:31
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
data class AdBean(
    val image: String,//图片
    val title: String,//标题
    val url: String,//跳转地址 h5  https:或者 http 开头   原生页面：以 action_router: 开头 如登录页面："action_router:/user/login"
    val params: Params,
    //参数 若需要参数，必须提前约定 哪些页面需要参数。 若分类页面： 固定的url + 固定的参数，
)

data class Params(//参数类型仅供参考
    val id: Int,
    val type: String,
)