package com.common.library.dialog

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.common.library.R
import com.common.library.util.TimeDateUtils
import com.github.gzuliyujiang.dialog.DialogLog
import com.github.gzuliyujiang.dialog.ModalDialog
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout

/**
 * 功能维护：朱波
 */
class JdDateSinglePicker(
    activity: Activity,
    val mStartValue: DateEntity = DateEntity.target(1950, 1, 1),
    val mEndValue: DateEntity = DateEntity.target(2010, 12, 31),
    val mDefaultValue: String? = null,
    val callBack: ((dialog: JdDateSinglePicker, startTime: String) -> Unit)? = null,
) : ModalDialog(activity) {

    lateinit var wheelLayout: DateWheelLayout


    lateinit var tvStartTime: TextView

    override fun initView() {
        initDefaultWheel()
        super.initView()
        tvStartTime = findViewById<TextView>(R.id.dialog_center_time)
        wheelLayout.setOnDateSelectedListener { year, month, day ->
            "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}".also {
                tvStartTime.text = it
            }
        }
//        getWheelDateText().also {
//            tvStartTime.text = it
//        }
        changeStyle()
    }

    fun getWheelDateText(): String =
        ("${wheelLayout.selectedYear}-" +
                "${if (wheelLayout.selectedMonth < 10) "0${wheelLayout.selectedMonth}" else wheelLayout.selectedMonth}" +
                "-${if (wheelLayout.selectedDay < 10) "0${wheelLayout.selectedDay}" else wheelLayout.selectedDay}")


    private fun changeStyle() {
        tvStartTime.setTextColor(activity.resources.getColor(R.color.color_yellow))
        tvStartTime.setBackgroundResource(R.drawable.base_stroke_yellow_5dp)


    }

    override fun onShow(dialog: DialogInterface?) {
        super.onShow(dialog)
    }

    override fun show() {
        super.show()
        initDefaultData()
    }


    open fun initDefaultData() {
        LogUtils.v(mStartValue?.toString() + "==" + mEndValue?.toString() + "===" + mDefaultValue )
        val target = if (TextUtils.isEmpty(mDefaultValue)) {
            val today = DateEntity.today()
            tvStartTime.text = ("${today.year}-" +
                    "${if (today.month < 10) "0${today.month}" else today.month}" +
                    "-${if (today.day < 10) "0${today.day}" else today.day}")
            today
        } else {
            tvStartTime.text = mDefaultValue
            val string2Date = TimeDateUtils.string2Date(mDefaultValue, TimeDateUtils.FORMAT_TYPE_5)
            DateEntity.target(string2Date)
        }
        wheelLayout.setRange(mStartValue, mEndValue, target)
    }

    open fun initDefaultWheel() {
        setBackgroundResource(R.drawable.base_shape_white_top_corner_30dp)
        wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY)
        wheelLayout.setCurtainEnabled(false)
        wheelLayout.setCurtainColor(-0x340000)
        wheelLayout.setIndicatorEnabled(false)
        wheelLayout.setIndicatorColor(-0x10000)
        wheelLayout.setIndicatorSize(context.resources.displayMetrics.density * 2)
        wheelLayout.setTextColor(Color.parseColor("#FF898F96"))
        wheelLayout.setTextSize(18 * context.resources.displayMetrics.scaledDensity)
        wheelLayout.setSelectedTextColor(context.resources.getColor(R.color.color_132))
        wheelLayout.setResetWhenLinkage(false)
    }

    override fun createTopLineView(): View? {
        return null
    }


    override fun onClick(v: View?) {
        val id = v!!.id
        when (id) {
            R.id.dialog_modal_cancel -> {
                DialogLog.print("cancel clicked")
                onCancel()
                dismiss()
            }
            R.id.dialog_modal_ok -> {
                DialogLog.print("ok clicked")
                onOk()
            }
            else -> {
                super.onClick(v)
            }
        }
    }

    override fun createBodyView(): View {
        wheelLayout = DateWheelLayout(activity)
        return wheelLayout
    }

    override fun createHeaderView(): View? {
        return View.inflate(activity, R.layout.base_dialog_date_single, null)
    }

    override fun onCancel() {}
    override fun onOk() {
        //开始时间和结束时间 的大小不在这里判断
        val mStartTime = tvStartTime.text.toString()
        if (mStartTime.isBlank()) {
            ToastUtils.showShort("请选择时间")
            return
        }
        callBack?.invoke(this, mStartTime)

    }
}