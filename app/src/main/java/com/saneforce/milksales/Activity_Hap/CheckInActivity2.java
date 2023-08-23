package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.databinding.ActivityCheckIn2Binding;
import com.saneforce.milksales.fragments.GateInOutFragment;
import com.saneforce.milksales.fragments.MonthlyFragment;
import com.saneforce.milksales.fragments.TodayFragment;
import com.saneforce.milksales.session.SessionHandler;
import com.saneforce.milksales.session.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CheckInActivity2 extends AppCompatActivity {
    private ActivityCheckIn2Binding binding;
    private SessionHandler session;
    private User user;
    private final Context context = this;
    private MyViewPagerAdapter myViewPagerAdapter;
    private SharedPreferences userDetails;
    private Shared_Common_Pref SHARED_COMMON_PREF;
    public static final String My_PREFERENCES = "MyPrefs";
    private String KEY_CHECK_IN_INFO = "CheckInDetail";
    private SharedPreferences CHECK_IN_DETAILS;
    String viewMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckIn2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userDetails = getSharedPreferences(My_PREFERENCES, MODE_PRIVATE);

        binding.userName.setText(userDetails.getString("SfName", ""));

        if (getIntent().getExtras() != null) {
            Bundle params = getIntent().getExtras();
            viewMode = params.getString("Mode");
        }

        initSession();
        loadSharedPref();
        checkInMyDayPlanTodayEnabled();
        checkInTimer();
        loadFragment();
        onClick();
    }

    private void checkInTimer() {
        Log.e("check_in_home", "Check in Enabled : " + user.getCheckInEnabled());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                100);

        if (user.getCheckInEnabled().equals("true")){
            String checkInTimeStamp = user.getCheckInTimeStamp();
            Log.e("check_in_home", "Check in TimeStamp : " + checkInTimeStamp);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date past = null;
            try {
                past = format.parse(checkInTimeStamp);

                Date now = new Date();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

//                if (seconds < 60) {
//                    binding.checkInBtn.setText("CHECK OUT " + " (" + seconds + " seconds ago" + ")");
//                } else if (minutes < 60) {
//                    binding.checkInBtn.setText("CHECK OUT " + " (" + minutes + " minutes ago" + ")");
//                } else if (hours < 24) {
//                    binding.checkInBtn.setText("CHECK OUT " + " (" + hours + " hours ago" + ")");
//                } else {
//                    binding.checkInBtn.setText("CHECK OUT " + " (" + days + " days ago" + ")");
//                }


                binding.checkInBtn.setText("CHECK OUT (" + hours +":" +  minutes + ":" + seconds + ")");
            } catch (ParseException e) {
                e.printStackTrace();
            }

          //  binding.checkInBtn.setText("CHECK OUT");
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.checkInBtn.setLayoutParams(params);
                binding.checkInBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_check_out1) );
            } else {
                binding.checkInBtn.setLayoutParams(params);
                binding.checkInBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.button_check_out1));
            }
        }
    }

    private void initSession() {
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();
    }

    private void loadSharedPref() {
        CHECK_IN_DETAILS = getSharedPreferences(KEY_CHECK_IN_INFO, Context.MODE_PRIVATE);
        SHARED_COMMON_PREF = new Shared_Common_Pref(context);
    }

    private void checkInMyDayPlanTodayEnabled() {
       Log.e("check_in_home", "My day plan Enabled : " + user.getMyDayPlan());
       if (user.getMyDayPlan().isEmpty() || user.getMyDayPlan().equals("")){
           Intent intent = new Intent(context, MyDayPlanActivity.class);
           startActivity(intent);
           overridePendingTransition(0,0);
           finish();
       }
    }

    private void loadFragment() {
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.viewPager.setAdapter(myViewPagerAdapter);
    }

    public static class MyViewPagerAdapter  extends FragmentStateAdapter {
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new TodayFragment();
                case 1:
                    return new MonthlyFragment();
                case 2:
                    return new GateInOutFragment();
                default:
                    return new TodayFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    private void onClick() {
        binding.checkInBtn.setOnClickListener(v -> {

            if (user.getCheckInEnabled().equals("true")){
                AlertDialogBox.showDialog(context, HAPApp.Title, "Do you want to Checkout?", "Yes", "No", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {

                        Intent takePhoto = new Intent(context, ImageCaptureActivity.class);

                        if(viewMode.equalsIgnoreCase("extended")){
                            takePhoto.putExtra("Mode", "EXOUT");
                        }else
                        {
                            takePhoto.putExtra("Mode", "COUT");
                        }
                        startActivity(takePhoto);
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                return;
            }
            String[] latlongs = userDetails.getString("HOLocation", "").split(":");
            Intent intent = new Intent(context, MapDirectionActivity.class);
            intent.putExtra(Constants.DEST_LAT, latlongs[0]);
            intent.putExtra(Constants.DEST_LNG, latlongs[1]);
            intent.putExtra(Constants.DEST_NAME, "HOLocation");
            intent.putExtra(Constants.NEW_OUTLET, "checkin");
            startActivity(intent);
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabLayout.getTabAt(position)).select();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInTimer();
    }
}