package com.saneforce.milksales.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.util.Log;

import com.saneforce.milksales.Activity_Hap.Common_Class;

public class  LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = intent.getParcelableExtra(SANGPSTracker.EXTRA_LOCATION);
        if (location != null) {
            Common_Class.location=location;
            Log.d("New Location",Utils.getLocationText(location));
            // Toast.makeText(context, Utils.getLocationText(location), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isTimeAutomatic(Context c) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

}
