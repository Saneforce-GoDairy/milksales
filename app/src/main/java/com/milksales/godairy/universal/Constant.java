package com.milksales.godairy.universal;

import static android.content.Context.BATTERY_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.milksales.godairy.SFA_Activity.HAPApp;
import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class Constant {


    private static Constant ourInstance = new Constant();
    private static final String TAG = Constant.class.getSimpleName();
    public static final String MyPREFERENCES = "SanSalesPrefs";
    public boolean isFirstTimeSynAll = false;


    //    public static String deviceType = "2";
    public static int IsSlideDownloadPopup = 0;
    public static int IsSlideDownloadPopupClosed = 0;
    public static String SlidesStoragePath = "";
    public static String PD_OrderType = "";
    public static String PD_RetailerID = "";
    public static String PD_RetailerName = "";
    public static boolean IfInitialGroup = true;
    public static int InitialSlidesPosition = 0;
    public static String SlideIdentityCode = "";
    public static String GroupID = "";
    public static String GroupName = "";
    public static String GroupIdentityCode = "";
    public static int GroupPosition = 0;
    public static int SlidePosition = 0;
    public static String SlideFileName = "";
    public static String SelectedGroupID = "";
    public static String SelectedGroupName = "";
    public static int SelectedGroupPosition = 0;
    public static int SelectedSlidePosition = 0;
    public static String SelectedSlideFileName = "";
    public static int GroupReportSerialNo = 1;
    public static int SlideWiseReportSerialNo = 1;
    public static String SlideWiseReportSlideName = "";


    public static String DIVISION_CODE= "";
    public static String SF_CODE= "";
    public static String RSF_CODE= "";
    public static int STATE_CODE= 0;
    public static String DESIG= "";
    public static String WORK_TYPE = "";
    public static String CURRENCY_SYMBOL = "";
    public static boolean DEBUG_MODE = true;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    LocationRequest mLocationRequest;


    public Constant(){
    }

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;



    public void loadDefaults(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.apply();
    }

    public static Constant getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new Constant(context);
        return ourInstance;
    }

    public static Constant getInstance() {
        if (ourInstance == null)
            ourInstance = new Constant();
        return ourInstance;
    }

    public Constant(Context context) {
        loadDefaults(context);
    }

    public void setValue(boolean value, String key) {
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void setValue(String value, String key) {
        editor.putString(key, value);
        editor.commit();
    }
    public String getValue(String key, String defValue) {
        return sharedpreferences.getString(key, defValue);
    }
    public boolean getValue(String key, boolean defValue) {
        return sharedpreferences.getBoolean(key, defValue);
    }
    public static boolean isNetworkAvailable(Context context) {
        boolean isConnected = false;
        if(context!=null) {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            if(connectivityManager.getActiveNetworkInfo()!=null) {
                isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
            }
        }
        Log.d(TAG, "isNetworkAvailable: isConnected => " + isConnected);

        return isConnected;
    }


    public void setValue(int value, String key) {
        editor.putInt(key, value);
        editor.commit();
    }


    public int getValue(String key, int defValue) {
        return sharedpreferences.getInt(key, defValue);
    }




    private static DecimalFormat df = new DecimalFormat("0.00");






    public static double getLatitude(Location location){
        if(location!=null){
            return location.getLatitude();
        }
        return 0;
    }

    public static double getLongitude(Location location){
        if(location!=null){
            return location.getLongitude();
        }
        return 0;
    }

}

