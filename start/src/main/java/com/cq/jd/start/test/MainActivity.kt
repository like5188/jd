//package com.cq.jd.start.test
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Parcelable
//import android.text.TextUtils
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.alibaba.android.arouter.facade.annotation.Route
//import com.common.library.router.ARouterPath
//import com.cq.jd.start.R
//import com.huawei.hms.hmsscankit.ScanUtil
//import com.huawei.hms.ml.scan.HmsScan
//import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
//
//@Route(path = ARouterPath.Start.main)
//class MainActivity : AppCompatActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.start_activity_main_test)
//
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        // 当扫码页面结束后，处理扫码结果。
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != RESULT_OK || data == null) {
//            return
//        }
//        // 从onActivityResult返回data中，用ScanUtil.RESULT作为key值取到HmsScan返回值。
//        else if (requestCode == 111) {
//            when (val obj: Parcelable? = data.getParcelableExtra(ScanUtil.RESULT)) {
//                is HmsScan -> {
//                    if (!TextUtils.isEmpty(obj.getOriginalValue())) {
//                        Toast.makeText(this, obj.getOriginalValue(), Toast.LENGTH_SHORT).show()
//                    }
//                    return
//                }
//            }
//        }
//    }
//
//    fun start(view: View) {
//        // 申请权限之后，调用DefaultView扫码界面。
//        ScanUtil.startScan(this@MainActivity, 111,
//            HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create())
//    }
//}