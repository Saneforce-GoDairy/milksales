package com.saneforce.godairy.universal;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.gson.JsonArray;
import com.saneforce.godairy.procurement.custom_form.utils.DBController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

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
    public static String Sf_Code = "Sf_Code";
    public static int STATE_CODE= 0;
    public static String DESIG= "";
    public static String WORK_TYPE = "";
    public static String CURRENCY_SYMBOL = "";
    public static boolean DEBUG_MODE = true;
    Context context;

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

    public static RequestBody toRequestBody (JsonArray value) {
        return RequestBody.create(MediaType.parse("text/plain"), value.toString());
    }

    public String getSetup(String key, String defaultValue, DBController dbController){
        if(dbController == null)
            dbController = new DBController(context.getApplicationContext());

        String value = defaultValue;
        String valueStr = "";
        try {
            valueStr = Constant.getResponseFromArrayValue(dbController.getResponse("setup"), key);
            if(!valueStr.equals("") && !valueStr.equals("null"))
                value = valueStr;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getResponseFromArrayValue(String response, String key){
        String value = "";
        if (response == null || response.equals("") || key == null || key.equals("") || key.equals("null"))
            return value;

        try {
            JSONArray jsonArray = new JSONArray(response);
            if(jsonArray.length()>0){
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if(jsonObject.has(key))
                    value = jsonObject.getString(key);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return value;
    }
}

