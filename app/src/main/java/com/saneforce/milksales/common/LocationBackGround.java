package com.saneforce.milksales.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saneforce.milksales.Activity_Hap.Block_Information;
import com.saneforce.milksales.Common_Class.LocationServices;
import com.saneforce.milksales.SFA_Activity.HAPApp;

import java.util.Timer;
import java.util.TimerTask;

public class LocationBackGround extends Service {
    private static final String TAG = LocationBackGround.class.getSimpleName();
    public static final int notify = 1000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandlers = new Handler();   //run on another Thread to avoid crash
    private Timer location = null;    //timer handling

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {

        if (location != null) {
            location.cancel();
        } else
            location = new Timer();   //recreate new
        location.scheduleAtFixedRate(new LocationDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        location.cancel();
    }


    class LocationDisplay extends TimerTask {


        @Override
        public void run() {
            mHandlers.post(new Runnable() {
                @SuppressLint("ResourceType")
                @Override
                public void run() {
                    Activity cAtivity = HAPApp.getActiveActivity();
                    String sMsg = "";
                    Context context = getApplicationContext();


                    LocationServices locationServices = new LocationServices(cAtivity, context);

                    if (!locationServices.checkPermission()) {
                        sMsg = "NO PERMISSION";
                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                    } else {
                        Log.v("PERMISSION", "PERMISSION");
                    }

                    ViewGroup rootView = cAtivity.getWindow().getDecorView().findViewById(android.R.id.content);
                    try {
                        RelativeLayout el = rootView.findViewById(4231);
                        if (el.getVisibility() == View.VISIBLE) {
                            rootView.removeView(el);
                        }
                    } catch (Exception e) {
                    }

                    if (isTimeAutomatic(context) != true) {
                        if (HAPApp.activeActivity.getClass() != Block_Information.class) {
                            Intent nwScr = new Intent(context, Block_Information.class);
                            nwScr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(nwScr);
                        }
                    }


                    if (!sMsg.equalsIgnoreCase("")) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                (RelativeLayout.LayoutParams.MATCH_PARENT), (RelativeLayout.LayoutParams.MATCH_PARENT));
                        lp.setMargins(13, 13, 13, 13);

                        RelativeLayout relative = new RelativeLayout(getApplicationContext());
                        relative.setId(4231);
                        relative.setLayoutParams(lp);
                        relative.setBackgroundColor(Color.parseColor("#008000"));

                        TextView tv = new TextView(getApplicationContext());
                        tv.setLayoutParams(lp);

                        tv.setText(sMsg);
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        relative.addView(tv);

                        rootView.addView(relative);
                        Log.d("service is ", "running" + cAtivity.getClass().getName());
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
