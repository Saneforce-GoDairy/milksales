package com.saneforce.milksales.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.saneforce.milksales.SFA_Activity.HAPApp;

public class ConnectivityReceiver extends BroadcastReceiver {
    public ConnectivityReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();

            // Check internet connection and accrding to state change the
            // text of activity by calling method
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("Connectivity","Connected");
                HAPApp.sendOFFlineLocations();
            } else {
                Log.d("Connectivity","Not Connected");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
