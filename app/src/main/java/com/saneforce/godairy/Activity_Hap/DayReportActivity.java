package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.R.layout.day_bottom_sheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.DayReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.Invoice_History;
import com.saneforce.godairy.adapters.DayReportAdapter;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.databinding.ActivityDayReportBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayReportActivity extends AppCompatActivity {

    public static final String UserDetail = "MyPrefs";
    private static final String MIN_DISTANCE = "";
    private ActivityDayReportBinding binding;
    public static String mDate = "";
    private DatePickerDialog datePickerDialog;
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    BottomSheetBehavior bottomSheetBehavior;

//    DayReportAdapter dayReportAdapter;
    BottomSheetDialog sheetDialog;
    Gson gson;
    Common_Class common_class;
    DatabaseHandler db;
    Shared_Common_Pref shared_common_pref;
  //  List<DayReport> dayreportlist;
    Type userType;
    private float initialX, initialY;
    private RecyclerView recyclerView;
    TextView tv_view_summary;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDayReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        shared_common_pref = new Shared_Common_Pref(DayReportActivity.this);
        common_class = new Common_Class(this);
//        common_class.getDataFromApi(Constants.DayReport, this, true);

        initialX = binding.tvViewSummary.getX();
        initialY = binding.tvViewSummary.getY();
        SharedPreferences userDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

        String sUName = userDetails.getString("SfName", "");
        String CTime = userDetails.getString("sSFType ", "");
        String SFDesig = userDetails.getString("EmpId", "");
        String sSFType = userDetails.getString("SFDesig", "");
        String empID = userDetails.getString("Sf_Type", "");

        binding.empName.setText(sUName);
        binding.empIdDeg.setText(empID);
        binding.empIdDeg.setText(SFDesig);
        binding.workType.setText(sSFType);
        binding.submitTime.setText(CTime);

        binding.dayreportRecyclerview.setAdapter(new DayReportAdapter(dayreportlist, R.layout.item_day_report, getApplicationContext(), new AdapterOnClick() {
            @Override
            public void onIntentClick(int Name) {
                AdapterOnClick.super.onIntentClick(Name);
                Intent intent = new Intent(DayReportActivity.this, Dashboard.class);

            }
        }));


        initViews();
//        getdayreportdetails();


    }

 /*   private void getdayreportdetails() {
        JSONArray data = new JSONArray();
        JSONObject jsonobj = new JSONObject();
        Log.v("TA_REQ", jsonobj.toString());
        try {
            JSONObject HeadItem = new JSONObject();
            HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
            jsonobj.put("Json_Head", HeadItem);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> responseBodyCall = apiInterface.dayreport(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());

        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e("JSON_VALUES", response.body().toString());
                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                        if (jsonObjects.getString("success").equals("true")) {
                            startActivity(new Intent(getApplicationContext(), Invoice_History.class));
                            finish();
                        }
                        common_class.showMsg(DayReportActivity.this, jsonObjects.getString("Msg"));

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SUBMIT_VALUE", "ERROR");
            }

        });

    }*/

    private void initBottomSheet() {
        sheetDialog = new BottomSheetDialog(DayReportActivity.this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.getBehavior().toString();
        sheetDialog.setContentView(day_bottom_sheet);
        sheetDialog.show();
        sheetDialog.getBehavior().getPeekHeight();
        sheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
      /*  String event;
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        binding.summaryView.setOnClickListener(v -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaY = event.getY() - initialY;
                    float newY = binding.summaryView.getY() + deltaY;
                    if (newY > initialY) {
                        binding.summaryView.setY(newY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    float finalY = event.getY();
                    if (finalY < initialY) {
                        // User has swiped upwards, open bottom sheet
//                        openBottomSheet();
                    } else {
                        // User has swiped downwards, reset view
//                        resetSummaryView();
                    }
                    break;
            }



        });*/
        // to be enalbe
/*        binding.summaryView.setOnClickListener(v -> {
            ObjectAnimator animation = ObjectAnimator.ofFloat(binding.summaryView, "translationY", -100f);
            animation.setDuration(200);
            animation.start();

            final Handler handler = new Handler();
            handler.postDelayed(() -> {

                onTouch();
                binding.summaryView.setVisibility(View.VISIBLE);
//                binding.summaryView.setGravity(Gravity.BOTTOM);

                initBottomSheet();
//                   handler.removeCallbacks(R.layout.day_bottom_sheet, this);
            }, 50);


        });*/
        // For Test

        binding.summaryView.setOnTouchListener((v, event) -> {
            ObjectAnimator animation = ObjectAnimator.ofFloat(binding.summaryView, "translationY", -0f);
            animation.setDuration(200);
            animation.start();
            animation.setFloatValues();

            final Handler handler = new Handler();
            handler.postDelayed(() -> {

                onTouch(event);
//                   handler.removeCallbacks(R.layout.day_bottom_sheet, this);
            }, 50);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialY = event.getY();

                    binding.summaryView.setVisibility(View.VISIBLE);
                    initBottomSheet();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaY = event.getY() - initialY;
                    float newY = binding.summaryView.getY() + deltaY;
                    if (newY > initialY) {
                        binding.summaryView.setY(newY);

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    float finalY = event.getY();
                    if (finalY < initialY) {
                        // User has swiped upwards, open bottom sheet
//                binding.summaryView.setGravity(Gravity.BOTTOM);

                    } else {
                        // User has swiped downwards, reset view

                    }
                    break;
            }
            return true;
        });
     /*   binding.summaryView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialY = motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float deltaY = motionEvent.getY() - initialY;
                            float newY = binding.summaryView.getY() + deltaY;
                            if (newY > initialY) {
                                binding.summaryView.setY(newY);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            float finalY = motionEvent.getY();
                            if (finalY < initialY) {
                                // User has swiped upwards, open bottom sheet
//                        openBottomSheet();
                            } else {
                                // User has swiped downwards, reset view
//                        resetSummaryView();
                            }
                            break;
                    }


                    return true;
                }
//                return false;


        });*/

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
       /* binding.summaryView.setOnClickListener(v -> {
            Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
            slide_up.start();
//            sheetDialog = new BottomSheetDialog(Day_Report_Activity.this, R.style.AppBottomSheetDialogTheme);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            sheetDialog.getBehavior().toString();
            sheetDialog.setContentView(R.layout.day_bottom_sheet);
            sheetDialog.show();
            sheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        });*/


        //Load animation
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        binding.dateContainer.setOnClickListener(v -> {
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


//        bottomSheetBehavior = BottomSheetBehavior.from(binding.summaryView);
       /* bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

            }
        });*/


    }

    private boolean onTouch(MotionEvent event) {
        float downX = 0, downY;
            // TODO Auto-generated method stub

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN :
                    downX = event.getX();
                    downY = event.getY();
                    Toast.makeText(getApplicationContext(), "action down", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP :

                    Toast.makeText(getApplicationContext(), "action up", Toast.LENGTH_SHORT).show();
                    float deltaX = downX - event.getX();
                    Toast.makeText(getApplicationContext(), Math.abs(deltaX)+"k", Toast.LENGTH_SHORT).show();
                    if (Math.abs(deltaX) > 3){
                        if(deltaX < 0) { onTopToBottomSwipe();
                            return true; }
                        if(deltaX > 0) { onBottomToTopSwipe();
                            return true; }
                    }
                    else {
                        String str = "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE;
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
            }
            return false;
        }

    private void onTopToBottomSwipe() {
        Toast.makeText(getApplicationContext(), "UP Swipe", Toast.LENGTH_SHORT).show();

    }

    private void onBottomToTopSwipe() {
        Toast.makeText(getApplicationContext(), "Down Swipe", Toast.LENGTH_SHORT).show();


    }
};



