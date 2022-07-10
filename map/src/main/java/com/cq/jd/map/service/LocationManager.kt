package com.cq.jd.map.service

import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.common.library.bean.LocationBean

/**
 * Created by BOBOZHU on 2022/6/11 09:15
 * Supporte By BOBOZHU
 */
class LocationManager {

    private var locationBean: LocationBean? = null

    @Synchronized
    fun saveLocation(bean: LocationBean) {
        this.locationBean = bean
        val json = GsonUtils.toJson(locationBean)
        SPUtils.getInstance().put(LOCATION_SELECT, json)
    }

    fun getLocation(): LocationBean? {
        if (locationBean == null) {
            val string = SPUtils.getInstance().getString(LOCATION_SELECT)
            locationBean = if (TextUtils.isEmpty(string)) {
                null
            } else {
                GsonUtils.fromJson(string, LocationBean::class.java)
            }
            if (locationBean==null){
                locationBean = LocationBean(city = "重庆城区",
                    cityCode = "500100",
                    address = "",
                    district = "",
                    province = "",
                    street = "",
                    town = "",
                    latitude = 29.533155,
                    longitude = 106.504962,
                    name = "重庆城区")
            }
        }
        return locationBean
    }

    @Synchronized
    fun clear() {
        if (locationBean != null) {
            locationBean = null
            SPUtils.getInstance().put(LOCATION_SELECT, "")
        }
    }

    companion object {
        private var locationManager: LocationManager? = null
        const val LOCATION_KEY = "user_location_by_location"//定位信息
        const val LOCATION_SELECT = "user_location_by_select"//选择的定位信息

        @JvmStatic
        val instance: LocationManager
            get() {
                if (locationManager == null) {
                    synchronized(LocationManager::class.java) {
                        if (locationManager == null) {
                            locationManager = LocationManager()
                        }
                    }
                }
                return locationManager!!
            }
    }
}