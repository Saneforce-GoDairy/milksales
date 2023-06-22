package com.saneforce.milksales.Common_Class;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationServices extends Activity {

    Activity activity;
    Context _context;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    public LocationServices(Activity activity, Context _context) {
        this.activity = activity;
        this._context = _context;
    }

    public boolean checkPermission() {
        int locationReq = ContextCompat.checkSelfPermission(_context, ACCESS_FINE_LOCATION);
        int coarseReq = ContextCompat.checkSelfPermission(_context, ACCESS_COARSE_LOCATION);
        int backReq = ContextCompat.checkSelfPermission(_context, ACCESS_BACKGROUND_LOCATION);

        if ((locationReq == PackageManager.PERMISSION_GRANTED &&
             coarseReq == PackageManager.PERMISSION_GRANTED &&
             backReq == PackageManager.PERMISSION_GRANTED) == true) {
            //Log.v("KARTHIC_KUMAR", "IF_CONDITION");
        } else {
            //Log.v("KARTHIC_KUMAR", "ELSE_CONDITION");
        }

        return locationReq == PackageManager.PERMISSION_GRANTED && coarseReq == PackageManager.PERMISSION_GRANTED &&
                backReq == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermission1() {
        int locationReq = ContextCompat.checkSelfPermission(_context, ACCESS_FINE_LOCATION);
        int coarseReq = ContextCompat.checkSelfPermission(_context, ACCESS_COARSE_LOCATION);

        if ((locationReq == PackageManager.PERMISSION_GRANTED && coarseReq == PackageManager.PERMISSION_GRANTED == true)) {
            //Log.v("KARTHIC_KUMAR", "IF_CONDITION");
        } else {
            //Log.v("KARTHIC_KUMAR", "ELSE_CONDITION");
        }

        return locationReq == PackageManager.PERMISSION_GRANTED && coarseReq == PackageManager.PERMISSION_GRANTED;
    }




    public void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION,ACCESS_BACKGROUND_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Log.v("KARTHIC_KUMAR", "ACCESS_COARSE_LOCATION");
                } else {
                    Log.v("KARTHIC_KUMAR", "ACCESS_COARSE");
                }

                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    Log.v("KARTHIC_KUMAR", "ACCESS_BACKGROUND_LOCATION");
                } else {
                    Log.v("KARTHIC_KUMAR", "ACCESS_BACKGROUND");
                }

                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.v("KARTHIC_KUMAR", "ACCESS_FINE_LOCATION");
                } else {
                    Log.v("KARTHIC_KUMAR", "ACCESS_FINE");
                }


            }

        }

    }
}
