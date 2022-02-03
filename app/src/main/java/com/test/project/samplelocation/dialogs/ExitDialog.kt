package com.test.project.samplelocation.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.cardview.widget.CardView
import android.view.View
import android.view.Window
import android.widget.RatingBar
import android.widget.Toast
import com.test.project.samplelocation.R
import com.test.project.samplelocation.interfaces.ExitDialogCallback
import kotlinx.android.synthetic.main.layout_exit_dialog.*


class ExitDialog(context: Context, private val callBack: ExitDialogCallback) : Dialog(context), View.OnClickListener,
        RatingBar.OnRatingBarChangeListener {

    private var mContext: Context = context
    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.layout_exit_dialog)
        ratingBar = findViewById(R.id.ratingBar)
        ratingBar.onRatingBarChangeListener = this
        findViewById<androidx.cardview.widget.CardView>(R.id.cardViewYes).setOnClickListener(this)
        findViewById<androidx.cardview.widget.CardView>(R.id.cardViewNo).setOnClickListener(this)
        checkbox.setOnClickListener(this)
        tvCheck.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cardViewYes -> {
                if (checkbox.isChecked) {
                    callBack.exitTheApp(true)
                } else {
                    callBack.exitTheApp(false)
                }
                dismissIt()
            }
            R.id.cardViewNo -> {
                dismissIt()
            }
            R.id.tvCheck -> {
                checkbox.isChecked = !checkbox.isChecked
            }
        }
    }

    private fun dismissIt() {
        try {
            dismiss()
        } catch (e: Exception) {
        }
    }

    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
        Toast.makeText(mContext, "Thanks", Toast.LENGTH_SHORT).show()
        dismissIt()
    }
}