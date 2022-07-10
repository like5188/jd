package com.common.library.router.provider

import com.alibaba.android.arouter.facade.template.IProvider
import com.common.library.bean.UserInfoBean

/**
 * Created by *** on 2022/5/30 11:22
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe 使用
 * UserService mineUserService = (UserService) ARouter.getInstance().build(“login/UserService”).navigation();
 * mineUserService.logOut();
 *
 * @author 又是谁写的
 */
interface UserService : IProvider {
    val isUserLogin: Boolean
    val userInfo: UserInfoBean?
    fun saveUserInfo(userInfoBean: UserInfoBean)
    fun logOut()
}