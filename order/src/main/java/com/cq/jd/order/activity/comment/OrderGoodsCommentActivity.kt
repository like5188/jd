package com.cq.jd.order.activity.comment

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.common.library.ui.activity.BaseVmActivity
import com.cq.jd.order.R
import com.cq.jd.order.databinding.OrderActivityGoodsCommentBinding
import com.cq.jd.order.dialog.DialogOrderDistanceShow
import com.google.android.material.tabs.TabLayout


class OrderGoodsCommentActivity :
    BaseVmActivity<OrderGoodsCommentModel, OrderActivityGoodsCommentBinding>(R.layout.order_activity_goods_comment) {

    override fun initWidget(savedInstanceState: Bundle?) {
        mDataBinding.model = mViewModel
        setTitleText("商品评论")
        mDataBinding.apply {
            val titles = mutableListOf("全部", "好评", "中/差评")

            val arrayList = ArrayList<Fragment>()
            val orderFragmentGoodsComment = OrderFragmentGoodsComment()
            val bundle = Bundle()
            bundle.putInt("exid",1)
            orderFragmentGoodsComment.arguments = bundle
            arrayList.add(orderFragmentGoodsComment)
            val orderFragmentGoodsComment2 = OrderFragmentGoodsComment()
            val bundle2 = Bundle()
            bundle2.putInt("exid",2)
            orderFragmentGoodsComment2.arguments = bundle2
            arrayList.add(orderFragmentGoodsComment2)
            val orderFragmentGoodsComment3 = OrderFragmentGoodsComment()
            val bundle3 = Bundle()
            bundle3.putInt("exid",3)
            orderFragmentGoodsComment3.arguments = bundle3
            arrayList.add(orderFragmentGoodsComment3)

            val pagerAdapter = PageAdapter(supportFragmentManager, titles, arrayList)
            viewPager.adapter = pagerAdapter
            tabLayout.setupWithViewPager(viewPager)
            for ( i in 0 until tabLayout.tabCount){
                val view = LayoutInflater.from(this@OrderGoodsCommentActivity).inflate(
                    R.layout.order_comment_top_item, null
                )
                val tv = view.findViewById<TextView>(R.id.tv_top_item)
                tv.text = titles[i]
                tv.setTextColor(Color.parseColor("#13202D"))
                if (i ==0){
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) //直接用setTextSize(22)也一样
                }else{
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) //直接用setTextSize(22)也一样
                }

                tabLayout.getTabAt(i)?.customView = view
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if(tab!!.customView != null) {
                        val tv: TextView = tab.customView!!.findViewById(R.id.tv_top_item)
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f) //直接用setTextSize(22)也一样
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    if(tab!!.customView != null) {
                        val tv: TextView = tab.customView!!.findViewById(R.id.tv_top_item)
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) //直接用setTextSize(22)也一样
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
//        val dialogOrderDistanceShow = DialogOrderDistanceShow(this)
//        dialogOrderDistanceShow.show()
    }

    override fun loadData() {
    }

    override fun createObserver() {
    }
}