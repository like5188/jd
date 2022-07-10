package com.common.library.router.provider

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.common.library.bean.LocationBean
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
interface LocationService : IProvider {

    /**
     * 单次定位
     */
    fun signLocation(
        context: Context,
        callback: (success: Boolean, location: LocationBean?) -> Unit,
    )

    /**
     * 获取逛街首页用户保存的定位信息
     *  1。可能来自定位  2 可能来自选择
     */
    fun getUserSelectLocation(): LocationBean?

    /**
     * 保存选择的定位信息
     */
    fun saveSelectLocation(location: LocationBean)
}