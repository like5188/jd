package com.cq.jd.map.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.common.library.bean.LocationBean
import com.common.library.liveData.StringLiveData
import com.common.library.viewModel.BaseViewModel
import com.tencent.lbssearch.TencentSearch
import com.tencent.lbssearch.`object`.param.Geo2AddressParam
import com.tencent.lbssearch.`object`.param.SuggestionParam
import com.tencent.lbssearch.`object`.result.Geo2AddressResultObject
import com.tencent.lbssearch.`object`.result.SuggestionResultObject
import com.tencent.map.geolocation.TencentLocationUtils
import com.tencent.map.tools.json.JsonUtils
import com.tencent.map.tools.net.http.HttpResponseListener
import com.tencent.tencentmap.mapsdk.maps.model.LatLng

/**
 * Created by *** on 2022/5/25 17:38
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
class MapViewModel(application: Application) : BaseViewModel(application) {

    val searchData = MutableLiveData<List<LocationBean>>()
    val keySearchData = MutableLiveData<List<LocationBean>>()

    val keyPwd = StringLiveData()

    val cityName = StringLiveData()


    /**
     * @param latLng 地图中点 选中的位置
     * @param page 页面
     * @param locationLat 定位的位置
     */
    fun searchByLa(latLng: LatLng, page: Int = 1, locationLat: LatLng? = null) {
        val tencentSearch = TencentSearch(getApplication())
        val param = Geo2AddressParam(latLng)
        param.getPoi(true)
        param.setPoiOptions(Geo2AddressParam.PoiOptions()
            .setAddressFormat(Geo2AddressParam.PoiOptions.ADDRESS_FORMAT_SHORT)
            .setPageIndex(page)
            .setPageSize(20)
            .setPolicy(5)
            .setRadius(5000))
        tencentSearch.geo2address(param, object : HttpResponseListener<Geo2AddressResultObject> {
            override fun onSuccess(code: Int, addressBean: Geo2AddressResultObject) {
                Log.v("TAG", "CODE==$code")
                Log.v("TAG", JsonUtils.modelToJsonString(addressBean))

                if (addressBean.isStatusOk) {
                    val result = addressBean.result
                    if (result.formatted_addresses == null
                        || result.address_component == null
                    ) {
//                        view.onFailure(addressBean.status, "获取位置信息失败")
                        Log.v("TAG", "获取位置信息失败")
                        return
                    }
                    if (addressBean.status == 0) {
                        val mapList: MutableList<LocationBean> = ArrayList()
                        if (page == 1) {
                            val bean = LocationBean(
                                address = result.address_component.district,
                                areaStat = -1,//行政区划 -1代表没有
                                city = result.address_component.city,//当前城市
                                cityCode = result.ad_info.adcode,//城市编码
                                district = result.ad_info.district,//区县
                                province = result.ad_info.province,//省份
                                street = result.address_component.street,//街道
                                town = result.address_component.street,//乡镇
                                latitude = latLng.latitude,//纬度
                                longitude = latLng.longitude,//经度
                                name = result.formatted_addresses.recommend,//当前位置的名称
                            )
//                            bean.name = result.formatted_addresses.recommend
//                            bean.address = result.address_component.district
                            bean.distance = if (locationLat != null) {
                                val dist =
                                    TencentLocationUtils.distanceBetween(locationLat.latitude,
                                        locationLat.longitude,
                                        latLng.latitude,
                                        latLng.longitude)
                                        .toFloat()
                                when {
                                    dist <= 100 -> {
                                        "100m以内"
                                    }
                                    dist > 1000 -> {

                                        (dist / 1000).toString() + "km"
                                    }
                                    else -> {
                                        dist.toString() + "m"
                                    }
                                }
                            } else {
                                ""//未知
                            }
//                            bean.latitude = latLng.latitude.toString()
//                            bean.longitude = latLng.longitude.toString()
//                            Log.v("TAG", bean.toString())
                            mapList.add(bean)
                        }
                        if (result.pois != null) {
                            for (i in result.pois.indices) {
                                val posBean = result.pois[i]
                                val addressStr =
                                    if (result.ad_info != null && posBean.address != null) {
                                        val s =
                                            result.ad_info.province + result.ad_info.city
                                        posBean.address.replace(s, "")
                                    } else {
                                        ""
                                    }
                                val bean = LocationBean(
                                    address = addressStr,
                                    areaStat = -1,//行政区划 -1代表没有
                                    city = result.address_component.city,//当前城市
                                    cityCode = result.ad_info.adcode,//城市编码
                                    district = result.ad_info.district,//区县
                                    province = result.ad_info.province,//省份
                                    street = result.address_component.street,//街道
                                    town = result.address_component.street,//乡镇
                                    latitude = posBean.latLng.latitude,//纬度
                                    longitude = posBean.latLng.longitude,//经度
                                    name = posBean.title,//当前位置的名称
                                )

//                                mapLocationBean.name = posBean.title

                                bean.distance = if (locationLat != null) {
                                    val dist =
                                        TencentLocationUtils.distanceBetween(locationLat.latitude,
                                            locationLat.longitude,
                                            latLng.latitude,
                                            latLng.longitude)
                                            .toFloat()
                                    when {
                                        dist <= 100 -> {
                                            "100m以内"
                                        }
                                        dist > 1000 -> {

                                            (dist / 1000).toString() + "km"
                                        }
                                        else -> {
                                            dist.toString() + "m"
                                        }
                                    }
                                } else {
                                    ""//未知
                                }
//                                mapLocationBean.latitude = posBean.latLng.latitude.toString()
//                                mapLocationBean.longitude = posBean.latLng.longitude.toString()
//                                Log.v("TAG", mapLocationBean.toString())
                                mapList.add(bean)
                            }
                            searchData.value = mapList
                        }
                    } else {
                        Log.v("TAG", "${addressBean.status}==" + addressBean.message)
                    }
                }
            }

            override fun onFailure(i: Int, s: String, throwable: Throwable?) {
                Log.v("TAG", "onFailure==$i===$s")
            }


        })

    }

    fun searchByKey(key: String) {
//        val cityN = cityName.value
//        if (cityN.isBlank()) {
//            return
//        }
        val tencentSearch = TencentSearch(getApplication())
        val searchParam = SuggestionParam()
        searchParam.region("全国")
        searchParam.getSubPois(true)
        searchParam.keyword(key)
        tencentSearch.suggestion(searchParam,
            object : HttpResponseListener<SuggestionResultObject> {
                override fun onSuccess(p0: Int, addressBean: SuggestionResultObject) {

                    LogUtils.v("onSuccess" + GsonUtils.toJson(addressBean))
                    if (addressBean.isStatusOk) {
                        val mapList: MutableList<LocationBean> = ArrayList()
                        if (addressBean.data != null) {
                            for (i in addressBean.data.indices) {
                                val posBean = addressBean.data[i]
                                val bean = LocationBean(
                                    address = posBean.address,
                                    areaStat = -1,//行政区划 -1代表没有
                                    city = posBean.city,//当前城市
                                    cityCode = posBean.adcode,//城市编码
                                    district = posBean.district,//区县
                                    province = posBean.province,//省份
                                    street = "",//街道
                                    town = "",//乡镇
                                    latitude = posBean.latLng.latitude,//纬度
                                    longitude = posBean.latLng.longitude,//经度
                                    name = posBean.title,//当前位置的名称
                                )
//                                mapLocationBean.name = posBean.title
//                                mapLocationBean.address = posBean.address
//                                mapLocationBean.distance = ""
//                                mapLocationBean.latitude = posBean.latLng.latitude.toString()
//                                mapLocationBean.longitude = posBean.latLng.longitude.toString()
//                                Log.v("TAG", mapLocationBean.toString())
                                mapList.add(bean)
                            }
                            keySearchData.value = mapList
                        }
                    }
                }

                override fun onFailure(p0: Int, p1: String, p2: Throwable) {
                    LogUtils.v(GsonUtils.toJson("$p0 $p1 ${p2.localizedMessage}"))
                }

            })
    }
}