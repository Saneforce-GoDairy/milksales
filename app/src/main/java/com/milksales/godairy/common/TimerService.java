package com.milksales.godairy.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;
import com.milksales.godairy.Activity_Hap.Block_Information;
import com.milksales.godairy.Activity_Hap.Common_Class;
import com.milksales.godairy.Activity_Hap.LoginHome;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.LocationServices;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.SFA_Activity.HAPApp;
import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerService extends Service {
    private static final String TAG = TimerService.class.getSimpleName();
    public static final int notify = 1000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    private Boolean UpdtFlag = false;
    private int intMin = 0;


    @Override
    public IBinder onBind(Intent intent) {

        /*        *//*if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else*//*
        mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);*/
        throw new UnsupportedOperationException("Not yet implemented");

    }

    public void startTimerService(){
        if (isMyServiceRunning(TimerService.class)==false) {
            try{
            startService(new Intent(this, TimerService.class));
            }catch (Exception e){}
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
        if (isMyServiceRunning(TimerService.class)==false)
        {
            try {
                Intent inten = new Intent(this, TimerService.class);
                startService(inten);
            }catch (Exception e){}
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Log.d("service is ","Destroyed");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        if (isMyServiceRunning(TimerService.class)==false)
        {
            try{
            Intent inten = new Intent(this, TimerService.class);
            startService(inten);
            }catch (Exception e){}
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (isMyServiceRunning(TimerService.class)==false)
        {
            try{
                Intent inten = new Intent(this, TimerService.class);
                startService(inten);
            }catch (Exception e){}
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        return START_STICKY;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Activity cAtivity = HAPApp.getActiveActivity();
        String sMsg = "";
        Context context = getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            LocationServices locationServices = new LocationServices(cAtivity, context);
            if (locationServices.checkPermission() == false) {
                sMsg = "Higher Version";
            } else {
            }
            ViewGroup rootView =null;
            try {
                if(cAtivity.getWindow()!=null){
                    rootView = cAtivity.getWindow().getDecorView().findViewById(android.R.id.content);
                    RelativeLayout el = rootView.findViewById(42311);
                    if (el.getVisibility() == View.VISIBLE) {
                        rootView.removeView(el);
                    }
                }
            } catch (Exception e) {
            }


            if (sMsg.equalsIgnoreCase("Higher Version")) {

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        (RelativeLayout.LayoutParams.MATCH_PARENT), (RelativeLayout.LayoutParams.MATCH_PARENT));

                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                RelativeLayout relative = new RelativeLayout(getApplicationContext());
                relative.setGravity(Gravity.CENTER);
                relative.setId(42311);
                relative.setLayoutParams(lp);
                relative.setBackgroundColor(Color.parseColor("#FFFFFF"));

                RelativeLayout.LayoutParams ImageRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                ImageRel.addRule(RelativeLayout.CENTER_HORIZONTAL);

                ImageRel.setMargins(0, 0, 3, 0);
                ImageView img = new ImageView(this);
                img.setId(123);
                img.setImageResource(R.drawable.location);
                img.setLayoutParams(ImageRel);
                relative.addView(img);


                RelativeLayout.LayoutParams headRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                headRel.addRule(RelativeLayout.CENTER_IN_PARENT);
                headRel.addRule(RelativeLayout.BELOW, 123);
                headRel.setMargins(50, 70, 50, 50);
                TextView headTxt = new TextView(this);
                headTxt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                headTxt.setGravity(Gravity.CENTER);
                headTxt.setLayoutParams(headRel);
                headTxt.setTextColor(Color.BLACK);
                headTxt.setId(12);
                headTxt.setTextSize(20);
                headTxt.setPadding(0, 10, 10, 10);
                headTxt.setTypeface(null, Typeface.BOLD);
                headTxt.setText("Location permission required");
                relative.addView(headTxt);


                RelativeLayout.LayoutParams subheadRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                subheadRel.addRule(RelativeLayout.CENTER_HORIZONTAL);
                subheadRel.addRule(RelativeLayout.BELOW, 12);
                subheadRel.setMargins(50, 10, 50, 50);
                TextView subheadTxt = new TextView(this);
                subheadTxt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                subheadTxt.setGravity(Gravity.CENTER);
                subheadTxt.setLayoutParams(subheadRel);
                subheadTxt.setTextColor(Color.parseColor("#585858"));
                subheadTxt.setId(152);
                subheadTxt.setTextSize(16);
                subheadTxt.setPadding(0, 10, 10, 10);
                /* edt.setText("Please provide AllOW ALWAYS in the permission setting to access the Application");*/
                subheadTxt.setText("Allow App to automatically detect your current location for travel allowance");
                relative.addView(subheadTxt);


                RelativeLayout.LayoutParams enableRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                enableRel.addRule(RelativeLayout.CENTER_HORIZONTAL);
                enableRel.addRule(RelativeLayout.BELOW, 152);
                enableRel.setMargins(50, 50, 50, 50);
                TextView edt = new TextView(this);
                edt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                edt.setGravity(Gravity.CENTER);
                edt.setLayoutParams(enableRel);
                edt.setTextColor(Color.BLACK);
                edt.setId(1520);
                edt.setTextSize(12);
                edt.setPadding(0, 10, 10, 10);
                /* edt.setText("Please provide AllOW ALWAYS in the permission setting to access the Application");*/
                edt.setText("To enable, go to 'Settings' and turn on Location permission 'Allow all the time'");
                relative.addView(edt);


                RelativeLayout.LayoutParams btnRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100);

                btnRel.addRule(RelativeLayout.CENTER_HORIZONTAL);

                btnRel.addRule(RelativeLayout.BELOW, 1520);
                btnRel.setMargins(100, 50, 100, 50);

                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(200,
                        100));
                btn.setGravity(Gravity.CENTER);
                btn.setLayoutParams(btnRel);
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundResource(R.drawable.button_blueg);

                btn.setText("Open Setting");
                btn.setAllCaps(false);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q))
                        {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(cAtivity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1001);
                                return;
                            }
                        }else{
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
                relative.addView(btn);

                if(rootView!=null) rootView.addView(relative);
            }

            super.onTaskRemoved(rootIntent);
        } else {
            LocationServices locationServices = new LocationServices(cAtivity, context);
            if (locationServices.checkPermission1() == false) {
                sMsg = "Lower Version";
            } else {
                sMsg = "PERMISIN IS THERE";
            }
            ViewGroup rootView = null;

            try {
                if(cAtivity.getWindow()!=null){
                    rootView=cAtivity.getWindow().getDecorView().findViewById(android.R.id.content);
                    RelativeLayout el = rootView.findViewById(42311);
                    if (el.getVisibility() == View.VISIBLE) {
                        rootView.removeView(el);

                    }
                }
            } catch (Exception e) {
            }


            if (sMsg.equalsIgnoreCase("Lower Version")) {

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        (RelativeLayout.LayoutParams.MATCH_PARENT), (RelativeLayout.LayoutParams.MATCH_PARENT));

                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                RelativeLayout relative = new RelativeLayout(getApplicationContext());
                relative.setGravity(Gravity.CENTER);
                relative.setId(42311);
                relative.setLayoutParams(lp);
                relative.setBackgroundColor(Color.parseColor("#FFFFFF"));

                RelativeLayout.LayoutParams ImageRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                ImageRel.addRule(RelativeLayout.CENTER_HORIZONTAL);

                ImageRel.setMargins(0, 0, 3, 0);
                ImageView img = new ImageView(this);
                img.setId(123);
                img.setImageResource(R.drawable.location);
                img.setLayoutParams(ImageRel);
                relative.addView(img);


                RelativeLayout.LayoutParams headRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                headRel.addRule(RelativeLayout.CENTER_IN_PARENT);
                headRel.addRule(RelativeLayout.BELOW, 123);
                headRel.setMargins(50, 70, 50, 50);
                TextView headTxt = new TextView(this);
                headTxt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                headTxt.setGravity(Gravity.CENTER);
                headTxt.setLayoutParams(headRel);
                headTxt.setTextColor(Color.BLACK);
                headTxt.setId(12);
                headTxt.setTextSize(20);
                headTxt.setPadding(0, 10, 10, 10);
                headTxt.setTypeface(null, Typeface.BOLD);
                headTxt.setText("Location permission required");
                relative.addView(headTxt);


                RelativeLayout.LayoutParams subheadRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                subheadRel.addRule(RelativeLayout.CENTER_HORIZONTAL);
                subheadRel.addRule(RelativeLayout.BELOW, 12);
                subheadRel.setMargins(50, 10, 50, 50);
                TextView subheadTxt = new TextView(this);
                subheadTxt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                subheadTxt.setGravity(Gravity.CENTER);
                subheadTxt.setLayoutParams(subheadRel);
                subheadTxt.setTextColor(Color.parseColor("#585858"));
                subheadTxt.setId(152);
                subheadTxt.setTextSize(16);
                subheadTxt.setPadding(0, 10, 10, 10);
                /* edt.setText("Please provide AllOW ALWAYS in the permission setting to access the Application");*/
                subheadTxt.setText("Allow App to automatically detect your current location for travel allowance");
                relative.addView(subheadTxt);


                RelativeLayout.LayoutParams enableRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                enableRel.addRule(RelativeLayout.CENTER_HORIZONTAL);
                enableRel.addRule(RelativeLayout.BELOW, 152);
                enableRel.setMargins(50, 50, 50, 50);
                TextView edt = new TextView(this);
                edt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                edt.setGravity(Gravity.CENTER);
                edt.setLayoutParams(enableRel);
                edt.setTextColor(Color.BLACK);
                edt.setId(1520);
                edt.setTextSize(12);
                edt.setPadding(0, 10, 10, 10);
                /* edt.setText("Please provide AllOW ALWAYS in the permission setting to access the Application");*/
                edt.setText("To enable, go to 'Settings' and turn on Location permission 'Allow all the time'");
                relative.addView(edt);


                RelativeLayout.LayoutParams btnRel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100);

                btnRel.addRule(RelativeLayout.CENTER_HORIZONTAL);

                btnRel.addRule(RelativeLayout.BELOW, 1520);
                btnRel.setMargins(100, 50, 100, 50);

                Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(200,
                        100));
                btn.setGravity(Gravity.CENTER);
                btn.setLayoutParams(btnRel);
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundResource(R.drawable.button_blueg);

                btn.setText("Open Setting");
                btn.setAllCaps(false);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                relative.addView(btn);


                rootView.addView(relative);
            }

            super.onTaskRemoved(rootIntent);
        }

    }
    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @SuppressLint("ResourceType")
                @Override
                public void run() {
                    Intent intent = new Intent();
                    onTaskRemoved(intent);
                    Activity cAtivity=HAPApp.getActiveActivity();
                    String sMsg="";
                    Context context = getApplicationContext();
                    Connectivity cn=new Connectivity();
                    if(Connectivity.isConnected(context)==false){
                        sMsg="No Internet Connectivity detected!.Kindly check your Internet Data Settings";
                    }else if(Connectivity.isConnectedFast(context)==false){
                        sMsg="Poor internet connectivity detected,access will take more time.";
                    }
                    ViewGroup rootView = null;

                    try {
                        if(cAtivity.getWindow()!=null) {
                            rootView = cAtivity.getWindow().getDecorView().findViewById(android.R.id.content);
                            RelativeLayout el = rootView.findViewById(4231);
                            if (el.getVisibility() == View.VISIBLE) {
                                rootView.removeView(el);
                            }
                        }
                    } catch(Exception e){}

                    SharedPreferences UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    if (UserDetails.getString("Sfcode", "") != "") {
                        //Log.d("Minitue",String.valueOf(intMin));
                        if(intMin>=(60*60)) {
                            DatabaseHandler db = new DatabaseHandler(context);
                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                            JSONArray locations = db.getAllPendingTrackDetails();
                            if (locations.length() > 0 && UpdtFlag == false) {
                                try {
                                    UpdtFlag = true;
                                    Call<JsonObject> call = apiInterface.JsonSave("save/trackall", "3", UserDetails.getString("Sfcode", ""), "", "", locations.toString());
                                    call.enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            // Get result Repo from response.body()
                                            db.deleteAllTrackDetails();
                                            UpdtFlag = false;
                                        }

                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {
                                            Log.d("LocationUpdate", "onFailure Local Location");
                                            UpdtFlag = false;
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            intMin=0;
                        }
                        intMin++;
                    }
                    if (context != null) {
                        if (isTimeAutomatic(context) != true ) {
                            if(HAPApp.activeActivity.getClass()!= Block_Information.class){

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

                                Intent nwScr = new Intent(context, Block_Information.class);
                                nwScr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(nwScr);
                            }
                        }
                    }

                    SharedPreferences CheckInDetails = getSharedPreferences("CheckInDetail", Context.MODE_PRIVATE);
                    String ACutOff=CheckInDetails.getString("ShiftCutOff","");
                    if(!ACutOff.equalsIgnoreCase("")){
                        Common_Class Dt=new Common_Class();
                        Date CutOff=Dt.getDate(ACutOff);
                        String sDt=Dt.GetDateTime(getApplicationContext(),"yyyy-MM-dd HH:mm:ss");
                        Date Cdate=Dt.getDate(sDt);
                        if (Cdate.getTime()>=CutOff.getTime()){
                            Log.d("Cutoff","Time REached");
                            Shared_Common_Pref sharedCommonPref = new Shared_Common_Pref(TimerService.this);
                            sharedCommonPref.save("ActivityStart","false");
                            sharedCommonPref.save(sharedCommonPref.DCRMode, "");

                            SharedPreferences.Editor editor = UserDetails.edit();
                            editor.putBoolean("Login", false);
                            editor.apply();
                            SharedPreferences.Editor cInEditor = CheckInDetails.edit();
                            cInEditor.remove("Shift_Selected_Id");
                            cInEditor.remove("Shift_Name");
                            cInEditor.remove("ShiftStart");
                            cInEditor.remove("ShiftEnd");
                            cInEditor.remove("ShiftCutOff");
                            cInEditor.remove("FTime");
                            cInEditor.remove("Logintime");
                            cInEditor.putBoolean("CheckIn", false);
                            cInEditor.apply();
                            sharedCommonPref.clear_pref(Constants.LOGIN_DATA);
                            sharedCommonPref.clear_pref(Constants.DB_TWO_GET_DYREPORTS);
                            sharedCommonPref.clear_pref(Constants.DB_TWO_GET_MREPORTS);
                            sharedCommonPref.clear_pref(Constants.DB_TWO_GET_NOTIFY);
                            Intent nwScr = new Intent(context, LoginHome.class);
                            nwScr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(nwScr);
                            /*window.localStorage.removeItem("Sfift_End_Time");
                            window.localStorage.removeItem("LOGIN");
                            $state.go('signin');*/
                        }
                    }
                    if (!sMsg.equalsIgnoreCase("")) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                (RelativeLayout.LayoutParams.MATCH_PARENT), (RelativeLayout.LayoutParams.WRAP_CONTENT));
                        lp.setMargins(13, 13, 13, 13);

                        RelativeLayout relative = new RelativeLayout(getApplicationContext());
                        relative.setId(4231);
                        relative.setLayoutParams(lp);
                        relative.setBackgroundColor(Color.parseColor("#b7454587"));

                        TextView tv = new TextView(getApplicationContext());
                        tv.setLayoutParams(lp);

                        tv.setText(sMsg);
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        relative.addView(tv);

                       if (rootView!=null) rootView.addView(relative);
                       // Log.d("service is ", "running" + cAtivity.getClass().getName());
                    }

                }

            });
        }
        public boolean isTimeAutomatic(Context c) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
            } else {
                return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
            }
        }
    }
}
