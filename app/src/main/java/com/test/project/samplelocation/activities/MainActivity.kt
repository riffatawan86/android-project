package com.test.project.samplelocation.activities

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.test.project.samplelocation.R
import com.test.project.samplelocation.adapters.MainItemAdapter
import com.test.project.samplelocation.dialogs.AppUsageDialog
import com.test.project.samplelocation.dialogs.ExitDialog
import com.test.project.samplelocation.interfaces.ExitDialogCallback
import com.test.project.samplelocation.interfaces.MainItemsCallback
import com.test.project.samplelocation.interfaces.MyReceiverCallBack
import com.test.project.samplelocation.models.MainItemModel
import com.test.project.samplelocation.models.MyLocationModel
import com.test.project.samplelocation.receiver.AppBroadCastReceiver
import com.test.project.samplelocation.services.AppLocationService
import com.test.project.samplelocation.utils.AppConstants
import com.test.project.samplelocation.utils.MyConstants
import com.test.project.samplelocation.utils.MyGeocoderUtils
import com.test.project.samplelocation.utils.MyGpsHelperClass
import kotlinx.android.synthetic.main.activity_app_landing.*
import kotlinx.android.synthetic.main.content_landing_activity_new.*

class MainActivity : AppCompatActivity(), ServiceConnection, MyReceiverCallBack,
    View.OnClickListener {

    private lateinit var myReceiver: AppBroadCastReceiver
    private lateinit var myService: AppLocationService
    private val permissionArray = arrayOfNulls<String>(4)
    private var shouldGetData = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_landing)
        animationView.enableMergePathsForKitKatAndAbove(true)
        permissionArray[0] = Manifest.permission.ACCESS_FINE_LOCATION
        permissionArray[1] = Manifest.permission.ACCESS_COARSE_LOCATION
        permissionArray[2] = Manifest.permission.READ_CONTACTS
        permissionArray[3] = Manifest.permission.SEND_SMS
        MyGpsHelperClass.checkGps(this)
        if (Build.VERSION.SDK_INT < 23) {
            initServiceAndReceiver()
        } else {
            checkPermissionAndGo()
        }
        val list = ArrayList<MainItemModel>()
        list.add(MainItemModel("Reminders", R.drawable.ic_reminder_icon))
        list.add(MainItemModel("Messages", R.drawable.ic_message_icon))
        val adapter = MainItemAdapter(this, list, object : MainItemsCallback {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(this@MainActivity, ReminderMenuActivity::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this@MainActivity, MessageMenuActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        })
        recyclerView.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(this, 1)
        recyclerView.adapter = adapter

        val headerView = navigationView.getHeaderView(0)
        headerView.findViewById<CardView>(R.id.cvSignout).setOnClickListener(this)
        headerView.findViewById<CardView>(R.id.cvRate).setOnClickListener(this)
        headerView.findViewById<CardView>(R.id.cvShare).setOnClickListener(this)
        headerView.findViewById<CardView>(R.id.cvUse).setOnClickListener(this)
        val tvSignIn =
            headerView.findViewById<TextView>(R.id.tvSignout)
        ivDrawer.setOnClickListener(this)
        val tvName = headerView.findViewById<TextView>(R.id.tvUserName)
        val tvEmail = headerView.findViewById<TextView>(R.id.tvUserEmail)
        val myPrefs =
            getSharedPreferences(AppConstants.MY_PREFS, Context.MODE_PRIVATE)
        val email = myPrefs.getString(AppConstants.USER_EMAIL, "User Email")
        val name = myPrefs.getString(AppConstants.USER_NAME, "User Name")
        if(email=="User Email"){
            tvSignIn.text="Sign In/Sign Up"
        }
        else{
            tvSignIn.text="Sign out"
        }
        tvName.setText(name)
        tvEmail.setText(email)
    }

    private fun checkPermissionAndGo() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            try {
                ActivityCompat.requestPermissions(this, permissionArray, 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            initServiceAndReceiver()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initServiceAndReceiver()
            } else {
                Toast.makeText(
                    this, "Please Restart App and Grant Location permission for using App.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initServiceAndReceiver() {
        myReceiver = AppBroadCastReceiver(this)
        val intentFilter = IntentFilter(MyConstants.MY_BROADCAST_ACTION)
        this.registerReceiver(myReceiver, intentFilter)
        val serviceIntent = Intent(this, AppLocationService::class.java)
        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun stopReceiverAndService() {
        try {
            this.unregisterReceiver(myReceiver)
            val serviceIntent = Intent(this, AppLocationService::class.java)
            unbindService(this)
            stopService(serviceIntent)
            myService.stopLocationUpdates()
        } catch (e: Exception) {
        }
    }

    override fun setLocationUpdates(myLocationModel: MyLocationModel?) {
        if (MyConstants.latitude == 0.0 && MyConstants.longitude == 0.0) {
            if (myLocationModel != null) {
                MyConstants.latitude = myLocationModel.myLatitude
                MyConstants.longitude = myLocationModel.myLongitude
                if (shouldGetData) {
                    MyConstants.address = MyGeocoderUtils.getAddressFromLocation(
                        this@MainActivity,
                        myLocationModel.myLatitude,
                        myLocationModel.myLongitude
                    )
                    shouldGetData = false
                }
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as AppLocationService.MyServiceBinder
        myService = binder.thisAppService as AppLocationService
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        try {
            myService.stopSelf()
            myService.stopLocationUpdates()
        } catch (e: Exception) {
        }
    }

    override fun onBackPressed() {
        val dialog = ExitDialog(this, object : ExitDialogCallback {
            override fun exitTheApp(flag: Boolean) {
                if (!flag) {
                    stopReceiverAndService()
                }
                finish()
                finishAffinity()
            }

        })
        dialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivDrawer -> {
                drawerLayout.openDrawer(navigationView)
            }
            R.id.cvSignout -> {
                FirebaseAuth.getInstance().signOut()
                val myPrefs =
                    getSharedPreferences(AppConstants.MY_PREFS, Context.MODE_PRIVATE)
                myPrefs.edit().putString(AppConstants.USER_ID, "id").apply()
                myPrefs.edit().putString(AppConstants.USER_EMAIL, "User Email").apply()
                myPrefs.edit().putString(AppConstants.USER_CONTACT, "Contact").apply()
                myPrefs.edit().putString(AppConstants.USER_NAME, "User Name").apply()
                val intent =
                    Intent(this@MainActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.cvRate -> {
                helperForRateApp(this)
            }
            R.id.cvShare -> {
                helperForShareApp(this)
            }
            R.id.cvUse -> {
                openUseDialog()
            }
        }
    }

    private fun openUseDialog() {
        try {
            val dialog =
                AppUsageDialog(
                    this
                )
            dialog.show()
        } catch (e: Exception) {
        }
    }

    private fun helperForRateApp(mContext: Context) {
        try {
            val intentRateApp =
                Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.rate_app)))
            mContext.startActivity(intentRateApp)
        } catch (e: Exception) {
        }
    }

    fun helperForShareApp(mContext: Context) {
        try {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.type = "text/plain"
            val shareSub = "Check this Application on Google Play!"
            val shareBody = mContext.getString(R.string.rate_app)
            shareAppIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            mContext.startActivity(Intent.createChooser(shareAppIntent, "Share App using..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
