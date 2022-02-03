package com.test.project.samplelocation.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.test.project.samplelocation.R;
import com.test.project.samplelocation.activities.SavedRemindersActivity;
import com.test.project.samplelocation.activities.SplashActivity;
import com.test.project.samplelocation.models.MessageModel;
import com.test.project.samplelocation.models.MyLocationModel;
import com.test.project.samplelocation.models.ReminderModel;
import com.test.project.samplelocation.room_database.LocationAppDatabase;
import com.test.project.samplelocation.room_database.MessageDao;
import com.test.project.samplelocation.room_database.ReminderDao;
import com.test.project.samplelocation.utils.MyConstants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppLocationService extends Service implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    MyServiceBinder binder = new MyServiceBinder();
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    public String TAG = "LOCATIONSERVICE";
    private NotificationManager notificationManager;
    private NotificationManagerCompat notificationManagerNew;
    private static ReminderDao reminderDao;
    private static MessageDao messageDao;
    private static ArrayList<ReminderModel> listReminders = new ArrayList<>();
    private static ArrayList<MessageModel> listMessages = new ArrayList<>();
    private String currentLocality = "locality";
    private SharedPreferences myPrefs;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        myPrefs.edit().putString("MyPrefsLocality", "locality").apply();
        myPrefs.edit().putString("MyPrefsNumber", "number").apply();
        myPrefs.edit().putString("MyPrefsLocalityNew", "locality").apply();
        reminderDao = LocationAppDatabase.getINSTANCE(this).getReminderDao();
        messageDao = LocationAppDatabase.getINSTANCE(this).getMessageDao();
        listMessages = (ArrayList<MessageModel>) messageDao.getAll();
        listReminders = (ArrayList<ReminderModel>) reminderDao.getAll();
    }

    public static void resetLists() {
        listMessages.clear();
        listReminders.clear();
        listMessages = (ArrayList<MessageModel>) messageDao.getAll();
        listReminders = (ArrayList<ReminderModel>) reminderDao.getAll();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        notificationManagerNew = NotificationManagerCompat.from(this);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyConstants.Companion.getNOTIFICATION_CHANNEL_ID())
                .setSmallIcon(R.drawable.ic_notification_icon_service)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentText("This_app is running in background")
                .setSubText(" for Live location services.")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pi);
        try {
            notificationManager.notify(1, builder.build());
            startForeground(1, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        createLocationRequest();
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                CharSequence name = MyConstants.Companion.getNOTIFICATION_CHANNEL_NAME();
                String description = MyConstants.Companion.getNOTIFICATION_CHANNEL_DESC();
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel channel = new NotificationChannel(MyConstants.Companion.getNOTIFICATION_CHANNEL_ID(), name, importance);
                channel.setDescription(description);
                notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest()
                .setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        if (googleApiClient.isConnected()) {
            try {
                googleApiClient.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged:" + location.getLatitude() + " : " + location.getLongitude());
        sendDataToReceiver(location);
        checkForReminder(location.getLatitude(), location.getLongitude());
        checkForMessage(location.getLatitude(), location.getLongitude());
    }

    private void checkForReminder(double latitude, double longitude) {
        float[] distance = new float[2];
        Intent notificationIntent = new Intent(this, SavedRemindersActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //String subLocality = MyGeocoderUtils.Companion.getSubLocalityFromLocation(this, latitude, longitude);
        // Log.d("LOCATIONCHECK", ": " + subLocality);
        for (ReminderModel model : listReminders) {
            Location.distanceBetween(latitude, longitude, model.getLatitude(), model.getLongitude(), distance);
            //String itemSubLocality = MyGeocoderUtils.Companion.getSubLocalityFromLocation(this, model.getLatitude(), model.getLongitude());
            if (distance[0] < 5000) {
                if (!myPrefs.getString("MyPrefsLocalityNew", "locality").equals(model.getAddress())) {
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm zzzz yyyy");
                    String todayDate = formatter.format(new Date(System.currentTimeMillis()));
                    Log.d("DETAILS:", "Today: " + todayDate);
                    if (model.getDateTime() == null) {
                        Log.d("LOCATIONCHECK", "mathced null " + ": " + model.getDateTime() + distance[0]);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyConstants.Companion.getNOTIFICATION_CHANNEL_ID())
                                .setSmallIcon(R.drawable.ic_notification_icon_service)
                                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("You have reached your location " + model.getAddress() + "\nTitle: " + model.getTitle() + "\nDetails: " + model.getNote()))
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setAutoCancel(true)
                                .setContentIntent(pi);
                        playAlarm();
                        /*builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(alarmSound);*/
                        notificationManager.notify(2, builder.build());
                        myPrefs.edit().putString("MyPrefsLocalityNew", model.getAddress()).apply();
                    } else if (model.getDateTime().equalsIgnoreCase(todayDate)) {
                        Log.d("LOCATIONCHECK", "mathced timed " + ": " + model.getDateTime() + distance[0]);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyConstants.Companion.getNOTIFICATION_CHANNEL_ID())
                                .setSmallIcon(R.drawable.ic_notification_icon_service)
                                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("You have reached your location " + model.getAddress() + "\nTitle: " + model.getTitle() + "\nDetails: " + model.getNote()))
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setAutoCancel(true)
                                .setContentIntent(pi);
                        notificationManager.notify(2, builder.build());
                        myPrefs.edit().putString("MyPrefsLocalityNew", model.getAddress()).apply();
                        playAlarm();
                    }
                }
            }
        }
    }

    private void playAlarm() {
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        try {
            final MediaPlayer mediaPlayer = new MediaPlayer();
            AssetFileDescriptor descriptor = getAssets().openFd("alec_calendar.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
            /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });*/
            mediaPlayer.start();
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 20 * 1000);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForMessage(double latitude, double longitude) {
        float[] distance = new float[2];
        //String subLocality = MyGeocoderUtils.Companion.getSubLocalityFromLocation(this, latitude, longitude);
        for (MessageModel model : listMessages) {
            Location.distanceBetween(latitude, longitude, model.getLatitude(), model.getLongitude(), distance);
            // String itemSubLocality = MyGeocoderUtils.Companion.getSubLocalityFromLocation(this, model.getLatitude(), model.getLongitude());
            if (distance[0] < 5000) {
                sendSms(model.getContactNo().trim(), model.getAddress(), model);
            }
        }
    }

    //for one location only one message will be sent//but if after gap of another location then message will be sent
    private void sendSms(String number, String locality, MessageModel messageModel) {
        if (!myPrefs.getString("MyPrefsLocality", "locality").equals(locality) && !myPrefs.getString("MyPrefsNumber", "number").equals(number)) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm zzzz yyyy");
            String todayDate = formatter.format(new Date(System.currentTimeMillis()));
            Log.d("DETAILS:", "Today: " + todayDate);
            if (messageModel.getDateTime() == null) {
                Log.d("LOCATIONCHECK:sms-sent", ": " + locality);
                String messageToSend = "This is an auto-generated sms send to you.\nTitle: " + messageModel.getTitle();
                SmsManager sms = SmsManager.getDefault();
                PendingIntent sentPI;
                String SENT = "SMS_SENT";
                sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
                sms.sendTextMessage(number, null, messageToSend, sentPI, null);
                //SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
                MessageDao messageDao = LocationAppDatabase.getINSTANCE(this).getMessageDao();
                messageDao.deleteSingle(messageModel.getId());
                myPrefs.edit().putString("MyPrefsLocality", locality).apply();
                myPrefs.edit().putString("MyPrefsNumber", number).apply();
            } else if (messageModel.getDateTime().equalsIgnoreCase(todayDate)) {
                Log.d("LOCATIONCHECK:sms-sent", ": " + locality);
                String messageToSend = "This is an auto-generated sms send to you.\nTitle: " + messageModel.getTitle();
                SmsManager sms = SmsManager.getDefault();
                PendingIntent sentPI;
                String SENT = "SMS_SENT";
                sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
                sms.sendTextMessage(number, null, messageToSend, sentPI, null);
                //SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
                MessageDao messageDao = LocationAppDatabase.getINSTANCE(this).getMessageDao();
                messageDao.deleteSingle(messageModel.getId());
                myPrefs.edit().putString("MyPrefsLocality", locality).apply();
                myPrefs.edit().putString("MyPrefsNumber", number).apply();
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private void sendDataToReceiver(Location location) {
        MyLocationModel model = new MyLocationModel(location.getLatitude(), location.getLongitude());
        Intent intent = new Intent();
        intent.setAction(MyConstants.Companion.getMY_BROADCAST_ACTION());
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra(MyConstants.Companion.getDATA_FROM_SERVICE_TO_RECEIVER(), model);
        this.sendBroadcast(intent);
    }

    public class MyServiceBinder extends Binder {
        public AppLocationService getThisAppService() {
            return AppLocationService.this;
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        Log.d("LINKLISTSIZE", "Des Service");
        try {
            stopLocationUpdates();
            stopSelf();
            stopForeground(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationManager.cancel(1);
        notificationManager.cancel(2);
        super.onDestroy();
    }
}
