package com.cq.jd.map.service

import android.content.Context
import android.os.Looper
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.common.library.bean.LocationBean
import com.common.library.router.ARouterPath
import com.common.library.router.provider.LocationService
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest

/**
 * Created by *** on 2022/6/2 10:10
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 *  权限适配 https://lbs.qq.com/mobile/androidLocationSDK/androidGeoGuide/androidGeoAdapt
 *
 *  tencent map  doc https://mapapi.qq.com/sdk/locationSDK/Android/doc/index.html
 * @author 又是谁写的
 */
@Route(path = ARouterPath.Map.LOCATION_SERVICE)
class TencentLocation : LocationService {


    override fun signLocation(
        context: Context,
        callback: (success: Boolean, location: LocationBean?) -> Unit,
    ) {
        val instance = TencentLocationManager.getInstance(context)
        val request = TencentLocationRequest.create()
        request.requestLevel = TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA
        instance.requestSingleFreshLocation(request,
            object : TencentLocationListener {
                /**
                 * 位置发生变化.
                 * location - 新的位置, *可能*来自缓存. 定位失败时 location 无效或者为 null
                 * error - 错误码, 仅当错误码为 TencentLocation.ERROR_OK 时表示定位成功, 为其他值时表示定位失败
                 * reason - 错误描述, 简要描述错误原因
                 */
                override fun onLocationChanged(
                    location: TencentLocation?,
                    error: Int,
                    reason: String?,
                ) {

                    if (error == TencentLocation.ERROR_OK && location != null) {
                        LogUtils.v("location data==" + GsonUtils.toJson(location))
                        val locationBean = LocationBean(
                            address = location.address,
                            areaStat = location.areaStat,
                            city = location.city,
                            cityCode = location.cityCode,
                            district = location.district,
                            province = location.province,
                            street = location.street,
                            town = location.town,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            name = location.name
                        )
                        callback.invoke(true, locationBean)
                    } else {
                        callback.invoke(false, null)
                    }
                }

                /**
                 * GPS, WiFi, Radio 等状态发生变化.
                 *
                 * name - 设备名, GPS, WIFI, CELL 中的某个
                 * status - 状态码, STATUS_ENABLED, STATUS_DISABLED, STATUS_UNKNOWN, STATUS_GPS_AVAILABLE, STATUS_GPS_UNAVAILABLE, STATUS_DENIED中的某个 在使用status之前,请先按照name进行区分
                 * desc - 状态描述
                 */
                override fun onStatusUpdate(name: String?, status: Int, desc: String?) {
                }

            },
            Looper.getMainLooper())

    }

    override fun getUserSelectLocation(): LocationBean? {
        return LocationManager.instance.getLocation()
    }

    override fun saveSelectLocation(location: LocationBean) {
        LocationManager.instance.saveLocation(location)
    }

    var instance: TencentLocationManager? = null

    var listener: TencentLocationListener? = null

    fun requestMore(mContext: Context, callBack: () -> Unit) {
        val instance = TencentLocationManager.getInstance(mContext)
        val request = TencentLocationRequest.create()
        ////用户可以自定义定位间隔，时间单位为毫秒，不得小于1000毫秒:
        request.interval = 10000
        //设置请求级别
        request.requestLevel = TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA;
        //是否允许使用GPS
        request.isAllowGPS = true
        //是否需要获取传感器方向
        request.isAllowDirection = true

        //是否需要开启室内定位
        request.isIndoorLocationMode = true
        listener = object : TencentLocationListener {
            override fun onLocationChanged(p0: TencentLocation?, p1: Int, p2: String?) {

            }

            override fun onStatusUpdate(p0: String?, p1: Int, p2: String?) {
            }

        }
        instance.requestLocationUpdates(request,
            listener,
            Looper.getMainLooper())
    }

    fun stopLocation() {
        instance?.removeUpdates(listener);
    }


    override fun init(context: Context?) {
    }
}