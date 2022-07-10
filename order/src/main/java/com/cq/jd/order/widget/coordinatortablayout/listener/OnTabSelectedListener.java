package com.cq.jd.order.widget.coordinatortablayout.listener;


import com.google.android.material.tabs.TabLayout;

/**
 * @author hugeterry(http://hugeterry.cn)
 */

public interface OnTabSelectedListener {

    public void onTabSelected(TabLayout.Tab tab);

    public void onTabUnselected(TabLayout.Tab tab);

    public void onTabReselected(TabLayout.Tab tab);
}
