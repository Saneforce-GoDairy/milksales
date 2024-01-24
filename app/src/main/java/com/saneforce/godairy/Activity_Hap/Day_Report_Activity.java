package com.saneforce.godairy.Activity_Hap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Model_Class.DayReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.adapters.DateReportAdapter;
import com.saneforce.godairy.adapters.DayReportAdapter;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.databinding.ActivityDayReportBinding;
import com.saneforce.godairy.databinding.ActivityTpMydayplanBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Day_Report_Activity extends AppCompatActivity {
    private ActivityDayReportBinding binding;
    public static String mDate = "";
    private DatePickerDialog datePickerDialog;
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    BottomSheetBehavior bottomSheetBehavior;

    DayReportAdapter dayReportAdapter;
    BottomSheetDialog sheetDialog;
    Gson gson;
    Common_Class common_class;
    DatabaseHandler db;
    Shared_Common_Pref shared_common_pref;
    List<DayReport> Dayreport = new ArrayList<>();
    private float initialX, initialY;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDayReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        shared_common_pref = new Shared_Common_Pref(Day_Report_Activity.this);
        common_class = new Common_Class(this);
        initialX =  binding.bottomAnc.getX();
        initialY =  binding.bottomAnc.getY();

        initViews();



    }

    private void initBottomSheet() {
        sheetDialog = new BottomSheetDialog(Day_Report_Activity.this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.getBehavior().toString();
        sheetDialog.setContentView(R.layout.day_bottom_sheet);
        sheetDialog.show();
    }


    private void initViews() {

        binding.bottomAnc.setOnClickListener(v -> {

            ObjectAnimator animation = ObjectAnimator.ofFloat(binding.bottomAnc, "translationY", -100f);
            animation.setDuration(200);
            animation.start();
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                onTouch();
                binding.bottomAnc.setVisibility(View.GONE);
                initBottomSheet();
             //   handler.removeCallbacks(Day_Report_Activity.this);
            }, 500);

        });
        // Start animation
    /*    binding.bottomAnc.setOnClickListener(v -> {
                    binding.bottomAnc.startAnimation(slide_down);

                    if (binding.bottomAnc.getVisibility() == View.GONE) {
                        Animation.ABSOLUTE.slideDown(binding.bottomAnc);
                        binding.bottomAnc.setVisibility(View.VISIBLE);

                    } else {
                        binding.bottomAnc.setVisibility(View.GONE);
                        ViewAnimatorSlideUpDown.slideUp(binding.bottomAnc);
                    }
                }*/
        binding.txtdayDate.setText(DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));
      /*  binding.bottomAnc.setOnClickListener(v -> {
            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
            slide_up.start();
            sheetDialog = new BottomSheetDialog(Day_Report_Activity.this, R.style.AppBottomSheetDialogTheme);
            sheetDialog.getBehavior().toString();
            sheetDialog.setContentView(R.layout.day_bottom_sheet);
            sheetDialog.show();
        });*/


        //Load animation
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);



        binding.txtdayDate.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = monthOfYear + 1;

                    date = ("" + year + "-" + month + "-" + dayOfMonth).toString();
                    binding.txtdayDate.setText(date);

                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });


      /*  bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomAnc);
        return bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //Log.e("BottomSheet", "Expanded");
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    //Log.e("BottomSheet", "Collapsed");
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }*/
        }

    private void onTouch() {
    }
}



