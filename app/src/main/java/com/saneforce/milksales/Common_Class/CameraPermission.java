package com.saneforce.milksales.Common_Class;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraPermission extends Activity {

    Activity activity;
    Context _context;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    public CameraPermission(Activity activity, Context _context) {
        this.activity = activity;
        this._context = _context;
    }

    public boolean checkPermission() {
        int locationReq = ContextCompat.checkSelfPermission(_context, ACCESS_FINE_LOCATION);
        int coarseReq = ContextCompat.checkSelfPermission(_context, ACCESS_COARSE_LOCATION);
        int cameraReq = ContextCompat.checkSelfPermission(_context, CAMERA);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {
            int BTReq = ContextCompat.checkSelfPermission(_context, BLUETOOTH_CONNECT);
            int postNotiReq = ContextCompat.checkSelfPermission(_context, POST_NOTIFICATIONS);
            int readmediaImages = ContextCompat.checkSelfPermission(_context, READ_MEDIA_IMAGES);
            return locationReq == PackageManager.PERMISSION_GRANTED && cameraReq == PackageManager.PERMISSION_GRANTED &&
                    coarseReq == PackageManager.PERMISSION_GRANTED && readmediaImages==PackageManager.PERMISSION_GRANTED && postNotiReq==PackageManager.PERMISSION_GRANTED && BTReq==PackageManager.PERMISSION_GRANTED;
        } else {
            int wrteStReq = ContextCompat.checkSelfPermission(_context, WRITE_EXTERNAL_STORAGE);
            int readStReq = ContextCompat.checkSelfPermission(_context, READ_EXTERNAL_STORAGE);
            return locationReq == PackageManager.PERMISSION_GRANTED && cameraReq == PackageManager.PERMISSION_GRANTED &&
                    coarseReq == PackageManager.PERMISSION_GRANTED && wrteStReq == PackageManager.PERMISSION_GRANTED &&
                    readStReq == PackageManager.PERMISSION_GRANTED;
        }
        //int readPhone = ContextCompat.checkSelfPermission(_context, READ_PHONE_STATE);


    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,POST_NOTIFICATIONS,BLUETOOTH_CONNECT}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean LoactionAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean CorseAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean readmediaImages = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted && ReadAccepted && LoactionAccepted && CorseAccepted ) {
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_IMAGES},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, CAMERA,
                                            READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }*/
                    }


                }
        }
    }
}



