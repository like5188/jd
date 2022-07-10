package com.cq.jd.order.activity.comment

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

//碎片页适配器的构造函数，传入碎片管理器与标题队列
class PageAdapter
    (
    @NonNull fm: FragmentManager?, //声明标题文本队列
    private val title: List<String>,
    private val list: ArrayList<Fragment>,
) :
    FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    //获取指定位置的碎片fragment
    @NonNull
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    //获取fragment的个数
    override fun getCount(): Int {
        return list.size
    }

    //获取指定碎片页的标题文本
//    override fun getPageTitle(position: Int): CharSequence? {
//        return title[position]
//    }
}