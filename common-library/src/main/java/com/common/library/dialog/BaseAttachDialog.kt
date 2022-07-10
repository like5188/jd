package com.common.library.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.library.R
import com.common.library.ui.removeAnimation
import com.common.library.util.dp
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 *  单选或者多选
 *  drop popup
 */
@SuppressLint("ViewConstructor")
class BaseAttachDialog<T>(
    context: Context,
    val list: List<T>,
    val oldList: MutableList<T> = arrayListOf(),
    val isMultiple: Boolean = false,
    val formatter: AttachFormatter<T>,
    val callBack: (MutableList<T>) -> Unit,
) : AttachPopupView(context) {
    private lateinit var rcView: RecyclerView
    val adapter by lazy {
        object : BaseQuickAdapter<T, BaseViewHolder>(R.layout.base_item_attach) {
            override fun convert(baseViewHolder: BaseViewHolder, s: T) {
                baseViewHolder.setText(R.id.tv_title, formatter.formatItem(s))
                val textView = baseViewHolder.getView<AppCompatTextView>(R.id.tv_title)
                var isSelect = false
                for (t in oldList) {
                    if (formatter.isSameItem(s, t)) {
                        isSelect = true
                        break
                    }
                }
                baseViewHolder.getView<View>(R.id.tv_title).isSelected = isSelect
                if (isSelect) {
                    val drawable = if (isMultiple) drawableGou else drawableCircle
                    textView.setCompoundDrawables(null, null, drawable, null)
                } else {
                    textView.setCompoundDrawables(null, null, null, null)
                }
            }
        }
    }
    val drawableGou by lazy {
        val drawable: Drawable? = context.getDrawable(R.mipmap.base_ic_select_gou)
        drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        drawable
    }
    val drawableCircle by lazy {
        val drawable: Drawable? = context.getDrawable(R.mipmap.base_ic_select_circle)
        drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        drawable
    }

    override fun getImplLayoutId(): Int {
        return R.layout.base_dialog_attach
    }

    override fun onCreate() {
        super.onCreate()

        rcView = findViewById(R.id.rc_view)
        rcView.removeAnimation()
        rcView.adapter = adapter
        adapter.setNewInstance(list = list as MutableList<T>)
        adapter.setOnItemClickListener { _, _, position: Int ->
            val s = list[position] //获取当前点中的项目
            if (isMultiple) { //多选
                var delPosition = -1;
                oldList.forEachIndexed { index, t ->
                    if (formatter.isSameItem(t, s)) {
                        delPosition = index
                    }
                }
                if (delPosition == -1) {
                    oldList.add(s)
                } else {
                    oldList.removeAt(delPosition)
                }
            } else { //单选
                oldList.clear()
                oldList.add(s)
                dismiss()
            }
            adapter.notifyDataSetChanged()
            callBack.invoke(oldList)
        }
    }

    override fun getMaxHeight(): Int {
        return 200.dp
    }


//    init {
//        this.list = list
//        this.oldList = oldList
//        this.isMultiple = isMultiple
//    }
}