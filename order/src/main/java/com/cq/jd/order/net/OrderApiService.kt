package com.cq.jd.order.net

import com.common.library.bean.PayTypeBean
import com.cq.jd.order.entities.*
import com.zhw.http.ApiResponse
import retrofit2.http.*

/**
 * Created by *** on 2021/3/16 9:43 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author ni dage
 */
interface OrderApiService {



    @GET("/merchant/mall/getMerchant")
    suspend fun merchantDetail(@QueryMap map: HashMap<String, Any>): ApiResponse<ShopDetailBean> //商户详情

    @GET("/shopping/mall/getShopping")
    suspend fun getShopping(@QueryMap map: HashMap<String, Any>): ApiResponse<ShopCarListBean> //商户详情-购物车列表

    @GET("/merchant_classify/mall/getAllData")
    suspend fun merchantClassify(@QueryMap map: HashMap<String, Any>): ApiResponse<List<ShopGoodsClassify>> //商户详情-购物车列表

    @GET("/goods/mall/getMerchantGoods")
    suspend fun merchantGoods(@QueryMap map: HashMap<String, Any>): ApiResponse<List<ClsGoodsBean>> //商户详情-分类列表

    @GET("/merchant/mall/getLicense")
    suspend fun licenseDetail(@Query("id")id:Int): ApiResponse<List<LicenseInfoBean>> //商户详情-資質

    @GET("/goods/mall/getData")
    suspend fun goodsDetail(@Query("goods_id")id:Int): ApiResponse<GoodsDetailInfo> //商户详情-資質

    @GET("/evaluate/mall/index")
    suspend fun evaluateIndex(@QueryMap map: HashMap<String, Any>): ApiResponse<List<EvaluationList>> //商户详情-資質

    @GET("/merchant/mall/getDistance")
    suspend fun getDistance(@QueryMap map: HashMap<String, Any>): ApiResponse<DistanceInfo> //商户详情-資質

    @GET("/coupon/mall/getNotUsed")
    suspend fun couponWaitUse(@Query("type")type:Int): ApiResponse<List<CouponWaitUseBean>> //商户详情-待使用优惠券

    @GET("/coupon/mall/getCoupon")
    suspend fun getCoupon(): ApiResponse<List<CouponNoLeadBean>> //商户详情-待领取优惠券


    @POST("/shopping/mall/saveShopping")
    @FormUrlEncoded
    suspend fun saveShopping(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-加入購物車

    @POST("/favorites/mall/save")
    @FormUrlEncoded
    suspend fun saveFavorites(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-收藏

    @POST("/favorites/mall/remove")
    @FormUrlEncoded
    suspend fun removeFavorites(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-收藏

    @POST("/user/mall/checkPayPassword")
    @FormUrlEncoded
    suspend fun checkPayPassword(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-密码验证

    @POST("/order/mall/createPayment")
    @FormUrlEncoded
    suspend fun createPayment(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-验证支付

    @POST("/order/mall/handleOrder")
    @FormUrlEncoded
    suspend fun handleOrder(@FieldMap map: HashMap<String, Any>): ApiResponse<OrderConfirmBean> //商户详情-加入購物車

    @POST("/order/mall/createOrder")
    @FormUrlEncoded
    suspend fun createOrder(@FieldMap map: HashMap<String, Any>): ApiResponse<OrderPayInfo> //商户详情-加入購物車

    @POST("/shopping/mall/editShopping")
    @FormUrlEncoded
    suspend fun editShopping(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-加入購物車

    @GET("/s_public/base/getPayType")
    suspend fun getPayType(@Query("type")type:String ): ApiResponse<List<PayTypeBean>> //商户详情-加入購物車

    @POST("/coupon/mall/receiveCoupon")
    @FormUrlEncoded
    suspend fun receiveCoupon(@Field("id")id:Int): ApiResponse<Any> //商户详情-加入購物車

    @POST("/complaint/mall/apply")
    @FormUrlEncoded
    suspend fun applyComplaint(@FieldMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-投诉

    @GET("/shopping/mall/remove")
    suspend fun removeShopping(@QueryMap map: HashMap<String, Any>): ApiResponse<Any> //商户详情-移除購物車




}