package com.test.project.samplelocation.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.test.project.samplelocation.R
import com.test.project.samplelocation.receiver.MyAlarmReceiver
import com.test.project.samplelocation.utils.MyConstants
import kotlinx.android.synthetic.main.activity_my_alarm.*

class MyAlarmActivity : AppCompatActivity() {

    private lateinit var mAlarmManager: AlarmManager
    private lateinit var wrapIntent: Intent
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_alarm)
        mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        wrapIntent = Intent(this, MyAlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            this,
            MyConstants.PENDING_INTNET_REQUEST_CODE,
            wrapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        btnSetAlarm.setOnClickListener { setAlarm() }
        btnStopAlarm.setOnClickListener { stopAlarm() }
    }

    private fun setAlarm() {
        mAlarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + (1000 * 5),
            1000 * 10,
            pendingIntent
        )

        Toast.makeText(this, "Alarm Set!", Toast.LENGTH_SHORT).show()
    }

    private fun stopAlarm() {
        mAlarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Canceled!", Toast.LENGTH_SHORT).show()
    }
}
