package com.saneforce.godairy.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.saneforce.godairy.Activity_Hap.Block_Information;
import com.saneforce.godairy.Activity_Hap.Common_Class;
import com.saneforce.godairy.Activity_Hap.MainActivity;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.saneforce.godairy.SFA_Activity.HAPApp.activeActivity;

//import android.support.annotation.NonNull;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.LocalBroadcastManager;

public class SANGPSTracker extends Service {
    private static Location location = null;
    private BroadcastReceiver yourReceiver;
    private static final String PACKAGE_NAME ="com.saneforce.saneforce.common";
            //"com.google.android.gms.location.sample.locationupdatesforegroundservice";
    private static final String ACTION_GPS = "android.location.PROVIDERS_CHANGED";

    private static final String TAG = SANGPSTracker.class.getSimpleName();

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";


    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocationBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 120000*1;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;
    Context mContext;
    public static SANGPSTracker sangpsTracker;

    Activity mactivity;
    /**
     * The current location.
     */
    private Location preLocation=null;
    private Location mLocation;
    private Boolean mShownSettings;
    private Date pDtTm=new Date();
    public SANGPSTracker() {

    }

    public SANGPSTracker(Context mContext) {
        this.mContext = mContext;
        this.mactivity = new Activity();
    }

    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };


        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification =false;
        try {
            if(intent!=null)
            startedFromNotification =intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                    false);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            //Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        registerReceiverGPS();
        // Tells the system to not try to recreate the service after it has been killed.
        return START_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration) {
            Log.i(TAG, "Starting foreground service");

           /* if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        SANGPSTracker.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }*/
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {

        // Utils.setRequestingLocationUpdates(this, true);
        Intent playIntent = new Intent(mContext, SANGPSTracker.class);
       // Log.d("playIntent", String.valueOf(playIntent));
        //startService(playIntent);
        mContext.startService(playIntent);

    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, SANGPSTracker.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
//        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            activityPendingIntent = PendingIntent.getActivity
                    (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            activityPendingIntent = PendingIntent.getActivity
                    (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        }


//                PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_launch, getString(R.string.launch_activity),
                        activityPendingIntent)
                //.addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                //        servicePendingIntent)
                //.setSound()
                .setNotificationSilent()
                .setContentText(text)
                .setContentTitle(Utils.getLocationTitle(this))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_so_launcher_foreground)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);
        mLocation = location;
        sendLocationDataToWebsite(location);
        // Notify anyone listening for broadcasts about the new location.
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (location.isFromMockProvider() == true) {
            if(HAPApp.activeActivity.getClass()!=Block_Information.class) {

                SharedPreferences UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject param=new JSONObject();
                try {
                    param.put("SF",UserDetails.getString("Sfcode", ""));
                    param.put("Device",android.os.Build.DEVICE);
                    param.put("BRAND",android.os.Build.BRAND);
                    param.put("Model", android.os.Build.ID);
                    param.put("Display",android.os.Build.DISPLAY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiInterface.blockAppData(param.toString());
                // "save/track", "3", UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());

                Intent nwScr = new Intent(mContext, Block_Information.class);
                nwScr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                nwScr.putExtra("Mode","FGPS");
                nwScr.putExtra("Head","APP Locked");
                nwScr.putExtra("Msg","Your Device Using Mock Location.<br /><br /><b>Contact Administrator.</b>");
                startActivity(nwScr);
            }
        }

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }

    }

    public static boolean isTimeAutomatic(Context c) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */

    public class LocationBinder extends Binder {
        public SANGPSTracker getLocationUpdateService(Context context) {
            mContext = context;
            return SANGPSTracker.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }


    public Location getLocation() {

        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                location = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
        return location;
    }

    private void sendLocationDataToWebsite(Location location) {
        double longitude, latitude;
        //imei = mSharedPreferences.getString(APP_PREFERENCES_IMEI, "");
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        float batteryPct =-1;
        if(ifilter!=null) {
            batteryPct = -1;
            try {
                BroadcastReceiver recvr = new PowerConnectionReceiver();
                Intent batteryStatus = mContext.registerReceiver(recvr, ifilter);

                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                batteryPct = (level * 100) / (float) scale;
                mContext.unregisterReceiver(recvr);
            }
            catch (Exception e){
                Log.d(TAG, "sendLocationDataToWebsite: "+ e.getLocalizedMessage());
            }
        }
        DatabaseHandler db = new DatabaseHandler(this);
        mLocation = location;

        longitude = mLocation.getLongitude();
        latitude = mLocation.getLatitude();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currDt=new Date(mLocation.getTime());
        float cDis=0;
        if(preLocation!=null)
        {
            cDis=preLocation.distanceTo(mLocation);
            Log.d("Location_Dist", String.valueOf(cDis));
            Common_Class DtClass=new Common_Class();
            if(DtClass.minutesBetween(dateFormat.format(new Date(pDtTm.getTime())),dateFormat.format(new Date(currDt.getTime())))<5)
                return;
           // if(cDis>150 && cDis<2500)
           //     return;
        }
        pDtTm.setTime(currDt.getTime());
        preLocation=mLocation;
        String strTime = dateFormat.format(new Date(mLocation.getTime()));

        String strBatt=Float.toString(batteryPct);
        String strSpeed = String.valueOf(mLocation.getSpeed()) + "-"+cDis+" km/h";
        String strHAcc = String.valueOf(mLocation.getAccuracy());
        String strBear = String.valueOf(mLocation.getBearing());
        String strEt = String.valueOf(mLocation.getElapsedRealtimeNanos());
        String strAlt = String.valueOf(mLocation.getAltitude());
        String strVAcc="";
        String strSAcc="";
        String strBAcc="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            strVAcc=String.valueOf(mLocation.getVerticalAccuracyMeters());
            strSAcc=String.valueOf(mLocation.getSpeedAccuracyMetersPerSecond());
            strBAcc=String.valueOf(mLocation.getBearingAccuracyDegrees());
        }

        JSONArray jsonarray = new JSONArray();
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("Time", strTime);
            paramObject.put("Latitude", latitude);
            paramObject.put("Longitude", longitude);
            paramObject.put("Speed", strSpeed);
            paramObject.put("bear", strBear);
            paramObject.put("hAcc", strHAcc);
            paramObject.put("et", strEt);
            paramObject.put("alt", strAlt);
            paramObject.put("vAcc", strVAcc);
            paramObject.put("sAcc", strSAcc);
            paramObject.put("bAcc", strBAcc);
            paramObject.put("mock", String.valueOf(location.isFromMockProvider()));
            paramObject.put("batt", strBatt);
            paramObject.put("DvcID", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("sendOFFlineLocations", "Location fetched...");
        db.addTrackDetails(paramObject);
        /*jsonarray.put(paramObject);
        SharedPreferences UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.JsonSave("save/track", "3", UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Get result Repo from response.body()
                Log.d(TAG, "onResponse Location" + String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.d(TAG, "onFailure Location");
            }
        });*/
    }
    private void registerReceiverGPS() {
        if (yourReceiver == null) {
            final IntentFilter theFilter = new IntentFilter();
            theFilter.addAction(ACTION_GPS);
            Log.d(TAG,"GPS Called Register");mShownSettings=false;
            yourReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    CheckGPSSettings(context);
                   /* if (intent != null) {
                        String s = intent.getAction();
                        if (s != null) {
                            if (s.equals(ACTION_GPS)) {
                                initFusedGPS();
                             //   chkEnaGPS();
                            }
                        }
                    }*/
                }
            };
            Context context = getApplicationContext();
            context.registerReceiver(yourReceiver, theFilter);
        }
    }
    public void CheckGPSSettings(Context context)
    {

        ContentResolver contentResolver = context.getContentResolver();
        // Find out what the settings say about which providers are enabled
        //  String locationMode = "Settings.Secure.LOCATION_MODE_OFF";
        int mode = Settings.Secure.getInt(
                contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
        if (mode == Settings.Secure.LOCATION_MODE_OFF ) {
            if( mShownSettings!=true) {
                Log.d(TAG, "GPS OFF");
                ShowLocationWarn();
            }
        }else
        {
            Log.d(TAG,"GPS ON");mShownSettings=false;
        }
    }
    public void ShowLocationWarn(){
        mShownSettings=true;
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener((Activity) activeActivity, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//  GPS is already enable, callback GPS status through listener
                        /*if (onGpsListener != null) {
                            onGpsListener.gpsStatus(true);
                        }*/
                    }
                })
                .addOnFailureListener((Activity) activeActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    Log.i(TAG, "PendingIntent INSAP.");
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult((Activity) activeActivity, 1000);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
    public class PowerConnectionReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED))
                Toast.makeText(context, "Power Connected", Toast.LENGTH_SHORT).show();

            else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
                Toast.makeText(context, "Power Not Connected", Toast.LENGTH_SHORT).show();

        }
    }
}
