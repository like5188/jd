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
open class JdDatePicker(
    activity: Activity,
    val mStartValue: DateEntity = DateEntity.target(2000, 1, 1),
    val mEndValue: DateEntity = DateEntity.target(2100, 12, 31),
    val mStartTime: String? = null,
    val mEndTime: String? = null,
    val callBack: ((dialog: JdDatePicker, startTime: String, endTime: String) -> Unit)? = null,
) : ModalDialog(activity) {


    lateinit var wheelLayout: DateWheelLayout

    var selectStartTime = true

    lateinit var tvStartTime: TextView
    lateinit var tvEndTime: TextView

    override fun initView() {
        super.initView()
        tvStartTime = findViewById<TextView>(R.id.dialog_start_time)
        tvEndTime = findViewById<TextView>(R.id.dialog_end_time)

        wheelLayout.setOnDateSelectedListener { year, month, day ->
            if (selectStartTime) {
                "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}".also {
                    tvStartTime.text = it
                }
            } else {
                "$year-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}".also {
                    tvEndTime.text = it
                }
            }
        }
        tvStartTime.setOnClickListener {
            selectStartTime = true
            changeStyle()

            val toString = tvStartTime.text.toString()
            if (!TextUtils.isEmpty(toString)) {
                val string2Date = TimeDateUtils.string2Date(toString, TimeDateUtils.FORMAT_TYPE_5)
                val target = DateEntity.target(string2Date)
                wheelLayout.setDefaultValue(target)
            }
        }
        tvEndTime.setOnClickListener {
            selectStartTime = false
            changeStyle()
            val toString = tvEndTime.text.toString()
            if (TextUtils.isEmpty(toString)) {
                getWheelDateText().also {
                    tvEndTime.text = it
                }
            } else {
                val string2Date = TimeDateUtils.string2Date(toString, TimeDateUtils.FORMAT_TYPE_5)
                val target1 = DateEntity.target(string2Date)
                wheelLayout.setDefaultValue(target1)
            }
        }

        initDefaultWheel()
    }


    fun getWheelDateText(): String =
        ("${wheelLayout.selectedYear}-" +
                "${if (wheelLayout.selectedMonth < 10) "0${wheelLayout.selectedMonth}" else wheelLayout.selectedMonth}" +
                "-${if (wheelLayout.selectedDay < 10) "0${wheelLayout.selectedDay}" else wheelLayout.selectedDay}")


    private fun changeStyle() {
        tvStartTime.setTextColor(if (selectStartTime) activity.resources.getColor(R.color.color_yellow) else activity.resources.getColor(
            R.color.color_c4))
        tvStartTime.setBackgroundResource(if (selectStartTime) R.drawable.base_stroke_yellow_5dp else R.drawable.base_stroke_c4_5dp)

        tvEndTime.setTextColor(if (!selectStartTime) activity.resources.getColor(R.color.color_yellow) else activity.resources.getColor(
            R.color.color_c4))
        tvEndTime.setBackgroundResource(if (!selectStartTime) R.drawable.base_stroke_yellow_5dp else R.drawable.base_stroke_c4_5dp)

    }


    override fun onShow(dialog: DialogInterface?) {
        super.onShow(dialog)
    }

    override fun show() {
        super.show()
        initDefaultData()
    }

    open fun initDefaultData() {
        LogUtils.v(mStartValue?.toString() + "==" + mEndValue?.toString() + "===" + mStartTime + "===" + mEndTime)
        val target = if (TextUtils.isEmpty(mStartTime)) {
            val today = DateEntity.today()
            tvStartTime.text = ("${today.year}-" +
                    "${if (today.month < 10) "0${today.month}" else today.month}" +
                    "-${if (today.day < 10) "0${today.day}" else today.day}")
            today
        } else {
            tvStartTime.text = mStartTime
            val string2Date = TimeDateUtils.string2Date(mStartTime, TimeDateUtils.FORMAT_TYPE_5)
            DateEntity.target(string2Date)
        }
        tvEndTime.text = mEndTime
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
        return View.inflate(activity, R.layout.base_dialog_date_interval, null)
    }

    override fun onCancel() {}
    override fun onOk() {
        //开始时间和结束时间 的大小不在这里判断
        val mStartTime = tvStartTime.text.toString()
        if (mStartTime.isBlank()) {
            ToastUtils.showShort("请选择开始时间")
            return
        }
        val mEndTime = tvEndTime.text.toString()
        if (mEndTime.isBlank()) {
            ToastUtils.showShort("请选择结束时间")
            return
        }
        if (TimeDateUtils.string2Long(mStartTime,
                TimeDateUtils.FORMAT_TYPE_5) > TimeDateUtils.string2Long(mEndTime,
                TimeDateUtils.FORMAT_TYPE_5)
        ) {
            ToastUtils.showShort("开始时间不能小于结束时间")
        } else {
            callBack?.invoke(this, mStartTime, mEndTime)
        }
    }
}