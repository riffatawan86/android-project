package com.test.project.samplelocation.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.SeekBar
import android.widget.TextView
import com.test.project.samplelocation.R

class MyDownloadingDialog : Dialog {

    private var seekBar: SeekBar? = null
    private var tvProgress: TextView? = null
    private var mContext: Context = context

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.layout_downloading_dialog)
        initViews()
    }

    fun initViews() {
        seekBar = findViewById(R.id.seekBar)
        tvProgress = findViewById(R.id.tvProgress)
    }

    fun setProgressToViews(progres: Int) {
        seekBar?.progress = progres
        tvProgress?.text = " $progres %"
    }

    fun dismissIt() {
        try {
            dismiss()
        } catch (e: Exception) {
        }
    }
}