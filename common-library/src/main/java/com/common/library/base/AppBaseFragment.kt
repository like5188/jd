package com.common.library.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.common.library.R
import com.common.library.ui.FragmentLife
import com.common.library.ui.IBar
import com.common.library.ui.Initialization
import com.common.library.ui.fid
import com.lxj.xpopup.XPopup

/**
 * Created by *** on 2021/3/5 11:31 AM
 * Supported By 宇宙无敌大公司.
 * Official Website: www.www.cn.
 * Describe
 *
 * @author 又是谁写的
 */
abstract class AppBaseFragment : Fragment(), Initialization, IBar {


    init {
        lifecycle.addObserver(FragmentLife(javaClass.name))
    }

    /**
     * 获取Activity
     * @return FragmentActivity
     */
    fun getSupportActivity(): FragmentActivity = super.getActivity() as FragmentActivity


    fun showToast(content: String) {
        ToastUtils.showShort(content)
    }

    fun showToast(@StringRes stringId: Int) {
        ToastUtils.showShort(stringId)
    }

    /**
     * 跳转页面
     *
     * @param cl 跳转的页面class
     */
    open fun doIntent(cl: Class<*>?) {
        doIntent(cl, null)
    }

    open fun doIntent(intent: Intent, bundle: Bundle?) {
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    open fun doIntent(intent: Intent?) {
        startActivity(intent)
    }


    /**
     * 跳转页面
     *
     * @param cl     跳转的页面class
     * @param bundle 携带的数据
     */
    open fun doIntent(cl: Class<*>?, bundle: Bundle?) {
        val intent = Intent(activity, cl)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


    open fun doIntent(path: String) {
        doIntent(path, null)
    }

    open fun doIntent(path: String, bundle: Bundle?) {
        ARouter.getInstance().build(path).with(bundle).navigation()
    }


    /**
     * 隐藏View
     * @param views 视图
     */
    fun gone(vararg views: View) {
        if (views.isNotEmpty()) {
            for (view in views) {
                view.visibility = View.GONE
            }
        }
    }

    /**
     * 显示View 不占位置
     * @param views 视图
     */
    fun visible(vararg views: View) {
        if (views.isNotEmpty()) {
            for (view in views) {
                view.visibility = View.VISIBLE
            }
        }
    }

    override fun getTitleBar(): View? {
        return fid(R.id.base_tool_view)
    }

    /**
     * 设置标题
     *
     * @param titleText 标题
     */
    open fun setTitleText(titleText: String?): TextView? {
        return getTitleView()?.let {
            it.text = titleText
            return it
        }
    }

    /**
     * 设置标题
     *
     * @param resId 字符串id
     */
    open fun setTitleText(@StringRes resId: Int): TextView? {
        return getTitleView()?.let {
            it.setText(resId)
            return it
        }
    }

    /**
     * 设置更多文字
     *
     * @param right 标题
     */
    open fun setRightText(
        right: String?,
        show: Boolean = true,
        onClick: View.OnClickListener? = null,
    ): TextView? {
        return getRightText()?.let {
            it.text = right
            it.visibility = if (show) View.VISIBLE else View.GONE
            it.setOnClickListener(onClick)
            return it
        }
    }

    /**
     * 设置更多图片
     *
     * @param right 标题
     */
    open fun setRightImage(resId: Int, show: Boolean = true): ImageView? {
        fid<View>(R.id.tool_view_action_more)?.visibility = if (show) View.VISIBLE else View.GONE
        return getRightImage()?.let {
            it.setImageResource(resId)
            return it
        }
    }

    /**
     * 对View添加点击事件
     */
    open fun addViewOnClick(listener: View.OnClickListener, vararg views: View) {
        if (views.isNotEmpty()) {
            for (view in views) {
                view.setOnClickListener(listener)
            }
        }
    }

    /**
     * 点击关闭
     */
    open fun setBackView() {
        getBackView()?.setOnClickListener {
            onReturn()
        }
    }

    override fun getBackView(): View? {
        return view?.findViewById(R.id.tool_btn_back)
    }

    override fun getTitleView(): TextView? {
        return view?.findViewById(R.id.tool_titleView)
    }


    override fun getRightImage(): ImageView? {
        return this.fid(R.id.tool_iv_action_more)
    }


    override fun getRightText(): TextView? {
        return this.fid(R.id.tool_tvOther)
    }

    private val loadingPopup by lazy {
        XPopup.Builder(activity)
            .dismissOnTouchOutside(false).asLoading()
    }

    override fun showLoading(message: String?) {
        loadingPopup.setTitle(message)
        loadingPopup.show()
    }

    override fun hiddenLoading() {
        loadingPopup.dismiss()
    }

    override fun onStop() {
        loadingPopup.dismiss();
        super.onStop()

    }

    //    override fun getStatusBarColor(): Int = R.color.colorWhite
    override fun onReturn() {
        activity?.finish()
    }

//    protected open fun nav(): NavController? {
//        return NavHostFragment.findNavController(this)
//    }
}