package com.cq.jd.map.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.common.library.bean.LocationBean
import com.common.library.router.ARouterPath
import com.common.library.ui.activity.BaseVmActivity
import com.common.library.ui.removeAnimation
import com.cq.jd.map.R
import com.cq.jd.map.databinding.MapActivitySelectBinding
import com.cq.jd.map.databinding.MapItemSearchAddressBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import com.tencent.tencentmap.mapsdk.maps.*
import com.tencent.tencentmap.mapsdk.maps.model.*

/**
 * Created by *** on 2022/5/25 16:24
 * Supporte By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
@Route(path = ARouterPath.Map.SELECT_MAP)
class MapActivity :
    BaseVmActivity<MapViewModel, MapActivitySelectBinding>(R.layout.map_activity_select),
    LocationSource, TencentLocationListener {


//    val viewModel by viewModels<MapViewModel> {
//        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
//    }

    val TAG = "MapActivity"

    //腾讯地图操控类
    private var tencentMap: TencentMap? = null

    //UI控制类
    private var mapUiSettings: UiSettings? = null

    //地址数据
    private var locationChangedListener: LocationSource.OnLocationChangedListener? = null

    //位置管理
    private var locationManager: TencentLocationManager? = null

    //地址请求封装
    private var locationRequest: TencentLocationRequest? = null

    //地图图标样式
    private var locationStyle: MyLocationStyle? = null

    //地图标点
    private var marker: Marker? = null

    //默认北京
    private var locationLatLng = LatLng(39.984066, 116.307548)

    //适配器相关
    private val mapAdapter by lazy {
        object :
            BaseQuickAdapter<LocationBean, BaseDataBindingHolder<MapItemSearchAddressBinding>>(R.layout.map_item_search_address) {
            override fun convert(
                holder: BaseDataBindingHolder<MapItemSearchAddressBinding>,
                item: LocationBean,
            ) {

                val select = selectMapLocation?.let {
                    it.toString() == item.toString()
                } ?: false
                holder.dataBinding?.let {
                    it.tvName.text = item.name
                    it.tvAddress.text = item.address
                    it.tvDistance.text = item.distance
                    if (select) {
                        it.tvName.setTextColor(resources.getColor(R.color.color_yellow))
                    } else {
                        it.tvName.setTextColor(resources.getColor(R.color.color_132))
                    }
                }
            }

        }
    }

    //适配器相关
    private val keyMapAdapter by lazy {

        object :
            BaseQuickAdapter<LocationBean, BaseDataBindingHolder<MapItemSearchAddressBinding>>(R.layout.map_item_search_address) {
            override fun convert(
                holder: BaseDataBindingHolder<MapItemSearchAddressBinding>,
                item: LocationBean,
            ) {

                holder.dataBinding?.let {
                    it.tvName.text = item.name
                    it.tvAddress.text = item.address
                    it.tvDistance.text = item.distance
                }
            }

        }
    }

    private var firstInter = true

    var selectMapLocation: LocationBean? = null

    val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun initWidget(savedInstanceState: Bundle?) {
        initMap()
        //建立定位
        initLocation()
        mDataBinding.recyclerView.layoutManager = linearLayoutManager
        mDataBinding.recyclerView.adapter = mapAdapter
        mDataBinding.recyclerView.addItemDecoration(DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL))
        mDataBinding.keyRecyclerView.addItemDecoration(DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL))
        mDataBinding.ivReset.setOnClickListener {
            initCamera()
        }

        mDataBinding.edtInput.inputType = InputType.TYPE_NULL
        mDataBinding.edtInput.setOnClickListener {
            mDataBinding.edtInput.inputType = InputType.TYPE_CLASS_TEXT
            mDataBinding.tvCancel.visibility = View.VISIBLE
            mDataBinding.viewMap.visibility = View.GONE
            mDataBinding.keyRecyclerView.visibility = View.VISIBLE
            mDataBinding.edtInput.isEnabled = true
            mDataBinding.edtInput.isFocusable = true
            mDataBinding.edtInput.isFocusableInTouchMode = true
            mDataBinding.edtInput.requestFocus()
            mDataBinding.edtInput.isCursorVisible = true
            KeyboardUtils.showSoftInput(mDataBinding.edtInput)
        }
        mDataBinding.tvCancel.setOnClickListener {
            hideSearch()
        }

        mDataBinding.keyRecyclerView.layoutManager = LinearLayoutManager(this)
        mDataBinding.keyRecyclerView.adapter = keyMapAdapter

        keyMapAdapter.setOnItemClickListener { _, view, position ->
            val data = keyMapAdapter.data
            val item = data[position]
            selectMapLocation = item
            mapAdapter.setNewInstance(data)
            linearLayoutManager.scrollToPositionWithOffset(position, 0)
            moveCenter(LatLng(item.latitude!!.toDouble(), item.longitude!!.toDouble()))
            hideSearch()
        }
        mapAdapter.setOnItemClickListener { adapter, view, position ->
            val item = mapAdapter.getItem(position)
            selectMapLocation = item
            moveCenter(LatLng(item.latitude!!.toDouble(), item.longitude!!.toDouble()))
            mapAdapter.notifyDataSetChanged()
        }
        mDataBinding.model = mViewModel
        mDataBinding.recyclerView.removeAnimation()
        setTitleText("选择地址")

        setRightText("保存") {
            selectMapLocation?.let { data ->
                LogUtils.v(GsonUtils.toJson(data))
                LiveEventBus.get("selectLocation", LocationBean::class.java).post(data)
                this.finish()
            } ?: kotlin.run {
                showToast("请选择位置")
            }
        }
    }

    private fun hideSearch() {
        mDataBinding.edtInput.setText("")
        mDataBinding.edtInput.inputType = InputType.TYPE_NULL
        mDataBinding.tvCancel.visibility = View.GONE
        mDataBinding.viewMap.visibility = View.VISIBLE
        mDataBinding.keyRecyclerView.visibility = View.GONE
        KeyboardUtils.hideSoftInput(mDataBinding.edtInput)
    }

    var search = true

    private fun moveCenter(latLng: LatLng, needSearch: Boolean = false) {
        search = needSearch
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f);
        tencentMap?.animateCamera(cameraUpdate, object : TencentMap.CancelableCallback {
            override fun onFinish() {
            }

            override fun onCancel() {
            }
        });
    }

    //移动镜头
    private fun initCamera() {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationLatLng, 15f);
        tencentMap?.animateCamera(cameraUpdate, object : TencentMap.CancelableCallback {
            override fun onFinish() {
                setMarker(locationLatLng)
            }

            override fun onCancel() {
            }
        });
    }

    var centerMarker: Marker? = null
    var markerPoint: Point? = null
    fun setMarker(latLng: LatLng) {

        val options = MarkerOptions()
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            .position(latLng);
        centerMarker = tencentMap!!.addMarker(options);
        //设置marker固定到中心点
        val target = tencentMap!!.cameraPosition.target;
        //坐标系转换，坐标信息转换为屏幕的中心点信息
        markerPoint = tencentMap!!.getProjection().toScreenLocation(target);
        centerMarker?.setFixingPointEnable(true);
        markerPoint?.x?.let {
            markerPoint?.y?.let { it1 ->
                centerMarker?.setFixingPoint(it,
                    it1)
            }
        };
    }


    private fun initMap() {
        tencentMap = mDataBinding.mapFrag.map
        //设置当前位置可见
        tencentMap?.isMyLocationEnabled = true
        //地图UI设置
        mapUiSettings = tencentMap?.uiSettings
        //设置显示定位的图标
//        mapUiSettings?.isMyLocationButtonEnabled = true
        mapUiSettings?.setLogoPosition(TencentMapOptions.LOGO_POSITION_TOP_RIGHT)
        //地图点击监听
        tencentMap?.setOnMapClickListener {
        }
        tencentMap?.setOnCameraChangeListener(object : TencentMap.OnCameraChangeListener {
            override fun onCameraChange(p0: CameraPosition?) {

            }

            override fun onCameraChangeFinished(p0: CameraPosition?) {
                p0?.let {
                    Log.v(TAG,
                        "onCameraChangeFinished==${p0.target.longitude}===${p0.target.latitude}")
                    if (search) {
                        mViewModel.searchByLa(p0.target, 1, locationLatLng)
                    } else {
                        search = true
                    }
                }
            }

        })

    }

    /**
     * 定位的一些初始化设置
     */
    private fun initLocation() {
        //用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
        locationManager = TencentLocationManager.getInstance(this)
        //设置坐标系
        locationManager?.coordinateType = TencentLocationManager.COORDINATE_TYPE_GCJ02
        //创建定位请求
        locationRequest = TencentLocationRequest.create()
        //设置定位周期（位置监听器回调周期）为3s
        locationRequest?.interval = 3000

        //地图上设置定位数据源
        tencentMap?.setLocationSource(this)
        //设置当前位置可见
        tencentMap?.isMyLocationEnabled = true
        //设置定位图标样式
        setLocMarkerStyle()
        tencentMap?.setMyLocationStyle(locationStyle)
    }

    /**
     * 设置定位图标样式
     */
    private fun setLocMarkerStyle() {
        locationStyle = MyLocationStyle()
        //创建图标
        val bitmapDescriptor =
//            BitmapDescriptorFactory.fromBitmap(getBitMap(R.drawable.ic_map_location))
            BitmapDescriptorFactory.fromResource(R.mipmap.map_ic_map_location)
        locationStyle?.icon(bitmapDescriptor)
        //设置定位圆形区域的边框宽度
        locationStyle?.strokeWidth(3)
        //设置圆区域的颜色
//        locationStyle?.fillColor(R.color.style)
        //连续定位，但不会移动到地图中心点，并且会跟随设备移动
//        locationStyle = locationStyle?.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER)
    }

    //设置定位图片
    private fun getBitMap(resourceId: Int): Bitmap? {
        var bitmap = BitmapFactory.decodeResource(resources, resourceId)
        val width = bitmap.width
        val height = bitmap.height
        val newWidth = 55
        val newHeight = 55
        val widthScale = newWidth.toFloat() / width
        val heightScale = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(widthScale, heightScale)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        return bitmap
    }


    //首次启动验证
    override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener?) {
        locationChangedListener = onLocationChangedListener
        val err = locationManager?.requestLocationUpdates(locationRequest, this, Looper.myLooper())
        when (err) {
            1 -> Toast.makeText(this, "设备缺少使用腾讯定位服务需要的基本条件", Toast.LENGTH_SHORT).show()
            2 -> Toast.makeText(this, "manifest 中配置的 key 不正确", Toast.LENGTH_SHORT).show()
            3 -> Toast.makeText(this, "自动加载libtencentloc.so失败", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }
    }

    override fun deactivate() {
        locationManager?.removeUpdates(this)
        locationManager = null
        locationRequest = null
        locationChangedListener = null
    }

    //定位回调
    override fun onLocationChanged(
        tencentLocation: TencentLocation?,
        error: Int,
        errorMsg: String?,
    ) {
        if (error == TencentLocation.ERROR_OK && locationChangedListener != null) {
            if (tencentLocation != null) {
                val location = Location(tencentLocation.provider)
                //设置经纬度以及精度
                location.latitude = tencentLocation.latitude
                location.longitude = tencentLocation.longitude
                location.accuracy = tencentLocation.accuracy
                locationChangedListener?.onLocationChanged(location)
                //记录坐标
                locationLatLng.setLatitude(tencentLocation.latitude)
                locationLatLng.setLongitude(tencentLocation.longitude)
                //设置镜头不随中心点移动
                if (locationStyle?.myLocationType != MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) {
                    locationStyle =
                        locationStyle?.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER)
                    tencentMap?.setMyLocationStyle(locationStyle)
                }
                //地址搜索
                if (firstInter) {
                    firstInter = false
                    initCamera()
                    mViewModel.searchByLa(locationLatLng, 1, locationLatLng)
                }
                if (mViewModel.cityName.value.isBlank()) {
                    mViewModel.cityName.value = tencentLocation.city
                }
                locationManager?.removeUpdates(this)
            } else {

            }
        }
    }

    override fun onStatusUpdate(p0: String?, p1: Int, p2: String?) {

    }


    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.mapFrag.onDestroy()
    }


    override fun loadData() {
    }

    override fun onStart() {
        super.onStart()
        mDataBinding.mapFrag.onStart()
    }

    override fun onPause() {
        super.onPause()
        mDataBinding.mapFrag.onPause()
    }

    override fun onStop() {
        super.onStop()
        mDataBinding.mapFrag.onStop()
    }

    override fun onResume() {
        super.onResume()
        mDataBinding.mapFrag.onResume()
    }

    override fun createObserver() {
        mViewModel.keyPwd.observe(this) {
            if (TextUtils.isEmpty(it)) {
                keyMapAdapter.setNewInstance(arrayListOf())
            } else {
                mViewModel.searchByKey(it)
            }

        }
        mViewModel.keySearchData.observe(this) {
            if (mDataBinding.keyRecyclerView.visibility == View.VISIBLE) {
                keyMapAdapter.setNewInstance(it as MutableList<LocationBean>)
            }
        }

        mViewModel.searchData.observe(this) {
            selectMapLocation = null
            mapAdapter.setNewInstance(it as MutableList<LocationBean>)
            linearLayoutManager.scrollToPositionWithOffset(0, 0)
        }
    }

}