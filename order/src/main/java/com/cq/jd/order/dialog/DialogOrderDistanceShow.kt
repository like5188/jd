package com.cq.jd.order.dialog

import android.content.Context
import android.os.CountDownTimer
import android.widget.TextView
import com.common.library.dialog.BaseDialog
import com.cq.jd.order.R

class DialogOrderDistanceShow(context: Context) :BaseDialog(context){

    private var  tvGoto :TextView ?=null

    override fun layoutResId() =  R.layout.order_dialog_distance
    override fun initView(context: Context?) {
        tvGoto = findViewById(R.id.tvGoto)
        startTime()
        timeJob?.start()
    }

    override fun initListener() {
    }

    private var timeJob: CountDownTimer? = null
    private  fun startTime() {

        timeJob = object : CountDownTimer(5000,1000){
            override fun onTick(p0: Long) {
                tvGoto?.text ="跳过(${p0/1000})"
            }

            override fun onFinish() {
                dismiss()
            }

        }

        tvGoto?.setOnClickListener {
            dismiss()
        }

    }

    fun  setUiData(distabce:String){
        findViewById<TextView>(R.id.tvTips).text = "您消费的商品距离您${distabce}公里"
    }

    override fun dismiss() {
        super.dismiss()
        timeJob?.cancel()
    }

}