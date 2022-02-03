package com.test.project.samplelocation.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.test.project.samplelocation.R

class SplashActivity : AppCompatActivity() {

    private var firstTimeChecker: Boolean = false
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)
        firstTimeChecker = sharedPreferences!!.getBoolean("FirstRunNew", true)
        if (!firstTimeChecker) {
            moveToMain()
        } else {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
            sharedPreferences!!.edit().putBoolean("FirstRunNew", false).apply()
        }
    }

    private fun moveToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ moveToMain() }, 6000)
    }
}