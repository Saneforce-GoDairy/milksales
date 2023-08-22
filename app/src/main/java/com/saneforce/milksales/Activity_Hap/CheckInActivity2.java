package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.databinding.ActivityCheckIn2Binding;
import com.saneforce.milksales.fragments.GateInOutFragment;
import com.saneforce.milksales.fragments.MonthlyFragment;
import com.saneforce.milksales.fragments.TodayFragment;

import java.util.Objects;

public class CheckInActivity2 extends AppCompatActivity {
    private ActivityCheckIn2Binding binding;
    private final Context context = this;
    private MyViewPagerAdapter myViewPagerAdapter;
    private SharedPreferences userDetails;
    public static final String My_PREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckIn2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userDetails = getSharedPreferences(My_PREFERENCES, MODE_PRIVATE);

        binding.userName.setText(userDetails.getString("SfName", ""));

        loadFragment();
        onClick();
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
            String[] latlongs = userDetails.getString("HOLocation", "").split(":");
            // String[] latlongs = "13.0299326:80.2414088".split(":");
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
}