package com.cq.jd.start.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.common.library.router.ARouterPath
import com.cq.jd.start.R


@Route(path = ARouterPath.Start.shop)
class OpenByShopActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity_shop_test)
    }
}