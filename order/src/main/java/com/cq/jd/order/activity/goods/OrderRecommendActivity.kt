package com.cq.jd.order.activity.goods

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.common.library.ui.activity.BaseVmActivity
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderActivityRecommendBinding
import java.lang.reflect.Method


class OrderRecommendActivity :
    BaseVmActivity<OrderRecommendModel, OrderActivityRecommendBinding>(R.layout.order_activity_recommend) {

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        mDataBinding.apply {
            mCoordinatorTabLayout.setTranslucentStatusBar(this@OrderRecommendActivity)
                .setTitle("")
                .setBackEnable(true)
                .setupWithViewPager(viewPager)
        }
    }

    override fun loadData() {
    }

    override fun createObserver() {
    }


    private fun  initTagView(text: String):View{
        val tagView = LayoutInflater.from(this).inflate(R.layout.order_item_tag, null)
        val tvTagName = tagView.findViewById<TextView>(R.id.tvTagName)
        tvTagName.text = text
        return  tagView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
//            R.id.action_about -> /*IntentUtils.openUrl(
//                this,
//                "https://github.com/hugeterry/CoordinatorTabLayout"
//            )*/
//            R.id.action_about_me -> /*IntentUtils.openUrl(this, "http://hugeterry.cn/about")*/
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        if (menu.javaClass.simpleName.equals("MenuBuilder", ignoreCase = true)) {
            try {
                val method: Method = menu.javaClass.getDeclaredMethod(
                    "setOptionalIconsVisible",
                    java.lang.Boolean.TYPE
                )
                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onMenuOpened(featureId, menu)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        showMenu(menu,R.id.action_about)
        showMenu(menu,R.id.action_about_me)
        return super.onCreateOptionsMenu(menu)
    }

    private  fun  showMenu(menu: Menu,resId: Int){
        val item = menu.findItem(resId)
        item.icon = resources.getDrawable(R.mipmap.add)
        val spannableString = SpannableString(item.title)
        spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, spannableString.length, 0)
        item.title = spannableString
    }

}