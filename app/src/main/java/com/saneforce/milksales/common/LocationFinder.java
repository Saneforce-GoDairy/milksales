package com.saneforce.milksales.common;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.saneforce.milksales.Interface.LocationEvents;

import java.util.List;

import static com.saneforce.milksales.SFA_Activity.HAPApp.activeActivity;

public class LocationFinder {

    /*The desired interval for location updates. Inexact. Updates may be more or less frequent.*/
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    static LocationEvents mlocEvents;
    /* The fastest rate for active location updates. Updates will never be more frequent than this value. */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /* The identifier for the notification displayed for the foreground service. */
    private static final int NOTIFICATION_ID = 12345678;
    /*
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    /* Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}. */
    private LocationRequest mLocationRequest;
    /* Provides access to the Fused Location Provider API. */
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG ="Location Finder";
    Runnable runnable=null;
    Handler handler=null;
    int running=0;
    int timeout=30;
    Context mContext;
    /* The current location. */
    private Location mLocation;

    public LocationFinder(Context context,LocationEvents locationEvents) {
        mContext=context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mlocEvents=locationEvents;
        this.running=0;
        createLocationRequest();
        ShowLocationWarn();
    }

    /* Sets the location request parameters. */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    Location location=null;
    public Location getLocation()
    {
        location=null;
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            handler = new Handler(Looper.getMainLooper());
            runnable = new Runnable() {
                @Override
                public void run() {
                    running++;
                    Log.d("MapsActivity","calling Timeout "+running+"-"+timeout);
                    if(running>timeout){
                        Log.d("MapsActivity","Timeout Event Fired");
                        mlocEvents.OnLocationRecived(null);
                        if(mFusedLocationClient!=null)
                        {
                            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                            mFusedLocationClient=null;
                        }
                        handler.removeCallbacks(runnable);
                        return;
                    }
                    handler.postDelayed(this, 1000); // Optional, to repeat the task.
                }
            };
            handler.postDelayed(runnable, 1000);
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
        return location;
    }
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
        List<Location> locationList = locationResult.getLocations();
        if (locationList.size() > 0) {
            //The last location in the list is the newest

            Location location = locationList.get(locationList.size() - 1);
            if(running<timeout) mlocEvents.OnLocationRecived(location);
            Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
            try
            {
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }catch (Exception e){}
            mFusedLocationClient=null;
            handler.removeCallbacks(runnable);
        }
        }
    };
    public void ShowLocationWarn(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        SettingsClient settingsClient = LocationServices.getSettingsClient(mContext);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener((Activity) activeActivity, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Log.v("LOACTION_SUCCESS","ONSUCCESS");
                        getLocation();
                    }
                })
//                .addOnSuccessListener((Activity) activeActivity, new OnSuccessListener<LocationSettingsResponse>() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                        Log.v("LOACTION_SUCCESS","ONSUCCESS");
//                        getLocation();
////  GPS is already enable, callback GPS status through listener
//                        /*if (onGpsListener != null) {
//                            onGpsListener.gpsStatus(true);
//                        }*/
//                    }
//                })
                .addOnFailureListener((Activity) activeActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                   // Log.i(TAG, "PendingIntent INSAP.");

                                    Log.v("LOACTION_SUCCESS","ONFAILURE");
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult((Activity) activeActivity, 1000);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                //Log.e(TAG, errorMessage);
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}
