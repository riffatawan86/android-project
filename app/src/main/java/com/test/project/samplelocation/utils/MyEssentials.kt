package com.test.project.samplelocation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import androidx.appcompat.app.AlertDialog


class MyEssentials {
    companion object {
        fun showAlertDialog(context: Context, title: String, message: String) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", { dialog, which -> dialog.dismiss() })
                .show()
        }

        fun isNetworkAvailable(mContext: Context): Boolean {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            try {
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }




        fun shareApp(mContext: Activity) {
            try {
                val shareAppIntent = Intent(Intent.ACTION_SEND)
                shareAppIntent.type = "text/plain"
                val shareSub = "Check out this application on play store!"
                val shareBody =""
                shareAppIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
                shareAppIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                mContext.startActivity(Intent.createChooser(shareAppIntent, "Share App using..."))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}