package com.cq.jd.user.manager

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.common.library.bean.UserInfoBean
import com.common.library.router.ARouterPath
import com.common.library.router.provider.UserService
import com.cq.jd.user.manager.UserInfoManager.Companion.instance

/**
 * Created by *** on 2022/5/30 11:23
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
@Route(path = ARouterPath.User.USER_INFO_SERVICE)
class UserServiceImpl : UserService {

    override val isUserLogin: Boolean
        get() = instance?.isUserLogin() ?: false
    override val userInfo: UserInfoBean?
        get() = instance?.getUser()

    override fun saveUserInfo(userInfoBean: UserInfoBean) {
        instance?.saveUser(userInfoBean)
    }
    override fun logOut() {
        instance!!.logOut()
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    override fun init(context: Context) {
    }
}