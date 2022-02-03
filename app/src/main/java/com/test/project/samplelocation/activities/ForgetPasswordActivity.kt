package com.test.project.samplelocation.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.test.project.samplelocation.R


class ForgetPasswordActivity : AppCompatActivity() {

    private var mEmail: EditText? = null
    private var mLogin: CardView? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        initViews()
    }


    private fun initViews() {
        mAuth = FirebaseAuth.getInstance()
        mEmail = findViewById<View>(R.id.email) as EditText
        mLogin = findViewById<View>(R.id.verificationBtn) as CardView
        mLogin!!.setOnClickListener {
            val email = mEmail!!.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    this@ForgetPasswordActivity,
                    "Please enter email address",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            mAuth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mEmail!!.setText("")
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            "Check your email id for reset link.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ForgetPasswordActivity,
                            "Cannot send reset link.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

}
