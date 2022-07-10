package com.common.library.dialog

import android.app.Activity
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.common.library.R
import com.github.gzuliyujiang.dialog.DialogLog
import com.github.gzuliyujiang.dialog.ModalDialog

/**
 * 功能维护：朱波
 */
open class JdTimePicker(
    activity: Activity,
    val mStartTime: String? = null,
    val mEndTime: String? = null,
    val callBack: ((dialog: JdTimePicker, startTime: String, endTime: String) -> Unit)? = null,
) : ModalDialog(activity) {
    lateinit var wheelLayout: TimeWheelLayout
    override fun createBodyView(): View {
        wheelLayout = TimeWheelLayout(activity)
        return wheelLayout
    }

    var selectStartTime = true

    lateinit var tvStartTime: TextView
    lateinit var tvEndTime: TextView


    override fun createHeaderView(): View? {
        return View.inflate(activity, R.layout.base_dialog_time_interval, null)
    }


    override fun initView() {
        super.initView()
        initDefaultWheel();
        tvStartTime = findViewById<TextView>(R.id.dialog_start_time)
        tvEndTime = findViewById<TextView>(R.id.dialog_end_time)
        wheelLayout.setOnLinkageSelectedListener { first, second, third ->
            if (selectStartTime) {
                "$first:$second".also {
                    tvStartTime.text = it
                }
            } else {
                "$first:$second".also {
                    tvEndTime.text = it
                }
            }
        }
        tvStartTime.setOnClickListener {
            selectStartTime = true
            changeStyle()
            val toString = tvStartTime.text.toString()
            val split = toString.split(":")
            wheelLayout.setDefaultValue(split[0], split[1], "")
        }
        tvEndTime.setOnClickListener {
            selectStartTime = false
            changeStyle()
            if (TextUtils.isEmpty(tvEndTime.text.toString())) {
                getWheelDateText().also {
                    tvEndTime.text = it
                }
            }else{
                val toString = tvEndTime.text.toString()
                val split = toString.split(":")
                wheelLayout.setDefaultValue(split[0], split[1], "")
            }
        }
//        getWheelDateText().also {
//            tvStartTime.text = it
//        }
    }

    fun getWheelDateText(): String =
        ("${wheelLayout.hour}:${wheelLayout.mi}")


    private fun changeStyle() {
        tvStartTime.setTextColor(if (selectStartTime) activity.resources.getColor(R.color.color_yellow) else activity.resources.getColor(
            R.color.color_c4))
        tvStartTime.setBackgroundResource(if (selectStartTime) R.drawable.base_stroke_yellow_5dp else R.drawable.base_stroke_c4_5dp)

        tvEndTime.setTextColor(if (!selectStartTime) activity.resources.getColor(R.color.color_yellow) else activity.resources.getColor(
            R.color.color_c4))
        tvEndTime.setBackgroundResource(if (!selectStartTime) R.drawable.base_stroke_yellow_5dp else R.drawable.base_stroke_c4_5dp)

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

    override fun show() {
        super.show()
        initDefaultData()
    }

    open fun initDefaultData() {
        val time = if (TextUtils.isEmpty(mStartTime)) {
            "06:30"
        } else mStartTime!!
        tvStartTime.text = time
        tvEndTime.text = mEndTime

        val split = time.split(":")
        wheelLayout.setDefaultValue(split[0], split[1], "")
    }

    private fun initDefaultWheel() {
        setBackgroundResource(R.drawable.base_shape_white_top_corner_30dp)
        wheelLayout.setCurtainEnabled(false)
        wheelLayout.setCurtainColor(-0x340000)
        wheelLayout.setIndicatorEnabled(false)
        wheelLayout.setIndicatorColor(-0x10000)
        wheelLayout.setIndicatorSize(context.resources.displayMetrics.density * 2)
        wheelLayout.setTextColor(Color.parseColor("#FF898F96"))
        wheelLayout.setTextSize(18 * context.resources.displayMetrics.scaledDensity)
        wheelLayout.setSelectedTextColor(context.resources.getColor(R.color.color_132))
//        wheelLayout.setDefaultValue("06", "30", "")
    }

    override fun createTopLineView(): View? {
        return null
    }

    override fun onCancel() {

    }

    override fun onOk() {
        //开始时间和结束时间 的大小不在这里判断
        callBack?.invoke(this, tvStartTime.text.toString(), tvEndTime.text.toString())
    }

}