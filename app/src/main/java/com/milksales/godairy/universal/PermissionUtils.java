package com.milksales.godairy.universal;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.SFA_Activity.HAPApp;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static boolean showFirstTimePermission = true;

    public static boolean hasCameraPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean hasLocPermission(Context activity) {
        boolean isGiven = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) > -1;


        return isGiven;
    }

    public static boolean hasLocationPermission(Context activity) {
        boolean isGiven = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) > -1;

        if (!Constant.getInstance().getValue(Constants.DONT_SHOW_LOCATION_PERMISSION_DIALOG, false) && isGiven && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                isGiven = ActivityCompat.checkSelfPermission(HAPApp.getActiveActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) > -1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isGiven;
    }

    public static List<String> getPermissionList(){
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        boolean isGivenFine = ContextCompat.checkSelfPermission(HAPApp.getActiveActivity(), Manifest.permission.ACCESS_FINE_LOCATION) > -1;

        boolean isGivenBg =ActivityCompat.checkSelfPermission(HAPApp.getActiveActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) > -1;

        if (isGivenFine && !isGivenBg && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
        return permissions;
    }



    public static List<String> getCameraStoragePermissionList(){

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(READ_MEDIA_IMAGES);
        }else{
            permissions.add(READ_EXTERNAL_STORAGE);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                permissions.add(WRITE_EXTERNAL_STORAGE);
            }
        }
        return permissions;
    }



    public static boolean isGpsON(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        int mode = Settings.Secure.getInt(
                contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

        return mode != Settings.Secure.LOCATION_MODE_OFF;
    }


    public static void checkPermission(Context context){
        boolean isGiven = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) > -1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && isGiven && !Constant.getInstance().getValue(Constants.DONT_SHOW_LOCATION_PERMISSION_DIALOG, false) && !PermissionUtils.hasGivenBackgroundPermission(HAPApp.getApplication())) {
            DialogUtils.showLocationPermissionDialog(context);
        }else
            checkLocation(context);


    }

    public static void checkLocation(Context context){
        if(!PermissionUtils.hasLocationPermission(context)) {
            Dexter.withContext(HAPApp.getActiveActivity())
                    .withPermissions(getPermissionList())
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report)
                        {
                            if(!report.areAllPermissionsGranted()) {
                                if(HAPApp.getApplication().isAppInForeground && !Constant.getInstance().getValue(Constants.USER_REJECTED_PERMISSION, false)){
                                   Constant.getInstance().setValue(true, Constants.USER_REJECTED_PERMISSION);
//                                    Toast.makeText(HAPApp.getActiveActivity(), "Location Permission required, please enable in settings", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                if(showFirstTimePermission){
                                    showFirstTimePermission = false;
                                    PermissionUtils.checkPermission(context);
                                }

                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }

                    }).check();

        }
    }

    public static boolean hasGivenBackgroundPermission(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) > -1;
    }
    public static void requestPermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{
                CAMERA,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION}, 100);
    }

}
