package com.test.project.samplelocation.utils

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

class MyGpsHelperClass {

    companion object {
        fun checkGps(mContext: Context) {
            val locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser(mContext)
            }
        }

        private fun showGPSDisabledAlertToUser(mContext: Context) {
            val alertDialogBuilder = android.app.AlertDialog.Builder(mContext)
            alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton(
                    "Enable GPS"
                ) { _, id ->
                    val callGPSSettingIntent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    mContext.startActivity(callGPSSettingIntent)
                }
            alertDialogBuilder.setNegativeButton(
                "Cancel"
            ) { dialog, id -> dialog.cancel() }
            val alert = alertDialogBuilder.create()
            try {
                alert.show()
            } catch (e: Exception) {
            }
        }
    }
}