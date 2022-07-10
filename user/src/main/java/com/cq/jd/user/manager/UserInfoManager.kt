package com.cq.jd.user.manager

import android.content.Context
import com.common.library.bean.UserInfoBean
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.cq.jd.user.manager.UserInfoManager
import com.alibaba.android.arouter.launcher.ARouter
import com.common.library.router.ARouterPath
import android.content.Intent
import android.text.TextUtils

/**
 * Created by *** on 2022/5/30 11:23
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class UserInfoManager {
    private var userBean: UserInfoBean? = null

    @Synchronized
    fun saveUser(userBean: UserInfoBean) {
        this.userBean = userBean
        val json = GsonUtils.toJson(userBean)
        SPUtils.getInstance().put(USER_INFO_KEY, json)
    }

    fun getUser(): UserInfoBean? {
        if (userBean == null) {
            val string = SPUtils.getInstance().getString(USER_INFO_KEY)
            userBean = if (TextUtils.isEmpty(string)) {
                null
            } else {
                GsonUtils.fromJson(string, UserInfoBean::class.java)
            }
        }
        return userBean
    }

    fun isUserLogin(): Boolean {
        return getUser() != null
    }

    @Synchronized
    fun logOut() {
        if (userBean != null) {
            userBean = null
            SPUtils.getInstance().put(USER_INFO_KEY, "")
            ARouter.getInstance().build(ARouterPath.User.LOGIN)
                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .navigation()
        }
    }

    companion object {
        private var userManager: UserInfoManager? = null
        const val USER_INFO_KEY = "user_info_key"

        @JvmStatic
        val instance: UserInfoManager?
            get() {
                if (userManager == null) {
                    synchronized(UserInfoManager::class.java) {
                        if (userManager == null) {
                            userManager = UserInfoManager()
                        }
                    }
                }
                return userManager
            }
    }
}