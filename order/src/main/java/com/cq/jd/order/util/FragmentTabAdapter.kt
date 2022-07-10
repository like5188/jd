package com.cq.jd.order.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class FragmentTabAdapter {

    private var fragments // tab页面对应的Fragment
            : List<Fragment>? = null
    private var fragmentActivity // Fragment所在的Activity
            : AppCompatActivity? = null
    private var fragmentContentId // Activity中所要被替换的区域的id
            = 0
    var currentTab // 当前Tab页面索引
            = 0
        private set
    private var onTabChangeListener: OnTabChangeListener? = null

    constructor(
        fragmentActivity: AppCompatActivity,
        fragments: List<Fragment>, fragmentContentId: Int
    ) {
        this.fragments = fragments
        this.fragmentActivity = fragmentActivity
        this.fragmentContentId = fragmentContentId
        // 默认显示第一页
        val ft = fragmentActivity.supportFragmentManager
            .beginTransaction()
        currentTab = 0
        ft.add(fragmentContentId, fragments[0])
        try {
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (null != onTabChangeListener) onTabChangeListener!!.OnTabChanged(0)
    }



    /**
     * 切换tab
     *
     * @param idx 下标
     */
    private fun showTab(idx: Int) {
        for (i in fragments!!.indices) {
            val fragment = fragments!![i]
            val ft = obtainFragmentTransaction(idx)
            if (idx == i) {
                ft.show(fragment)
            } else {
                ft.hide(fragment)
            }
            ft.commitAllowingStateLoss()
        }
        currentTab = idx // 更新目标tab为当前tab
    }

    /**
     * 获取带动画的FragmentTransaction
     */
    private fun obtainFragmentTransaction(index: Int): FragmentTransaction {
        // 设置切换动画
//    if (index > currentTab) {
//       ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
//    } else {
//       ft.setCustomAnimations(R.anim.slide_right_in,
//             R.anim.slide_right_out);
//    }
        return fragmentActivity!!.supportFragmentManager
            .beginTransaction()
    }

    val currentFragment: Fragment
        get() = fragments!![currentTab]

    fun setCurrentFragment(idx: Int) {
        if (idx == currentTab) {
            return
        }
        val fragment = fragments!![idx]
        val ft = obtainFragmentTransaction(idx)
        currentFragment.onPause() // 暂停当前tab
        // getCurrentFragment().onStop(); // 暂停当前tab
        if (fragment.isAdded) {
            // fragment.onStart(); // 启动目标tab的onStart()
            fragment.onResume() // 启动目标tab的onResume()
        } else {
            ft.add(fragmentContentId, fragment)
        }
        showTab(idx) // 显示目标tab
        ft.commitAllowingStateLoss()
        // 如果设置了切换tab额外功能功能接口
        if (null != onTabChangeListener) {
            onTabChangeListener!!.OnTabChanged(idx)
        }
    }

    fun setOnTabChangeListener(onTabChangeListener: OnTabChangeListener?) {
        this.onTabChangeListener = onTabChangeListener
    }

    interface OnTabChangeListener {
        fun OnTabChanged(index: Int)
    }
}