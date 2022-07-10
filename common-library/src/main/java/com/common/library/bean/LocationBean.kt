package com.common.library.bean

/**
 * Created by *** on 2022/6/2 10:34
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
data class LocationBean(
    val address: String = "",//当前位置
    val areaStat: Int = -1,//行政区划 -1代表没有
    val city: String,//当前城市
    val cityCode: String,//城市编码
    val district: String,//区县
    val province: String,//省份
    val street: String,//街道
    val town: String,//乡镇
    val latitude: Double,//纬度
    val longitude: Double,//经度
    val name: String,//当前位置的名称
    var distance: String="",//当前位置的名称
){
    override fun toString(): String {
        return "LocationBean(address='$address', city='$city', cityCode='$cityCode', district='$district', province='$province', latitude=$latitude, longitude=$longitude, name='$name', distance='$distance')"
    }
}