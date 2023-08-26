package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity.AllowanceActivity;
import com.saneforce.milksales.Activity.AllowanceActivityTwo;
import com.saneforce.milksales.Activity.TAClaimActivity;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.common.DatabaseHandler;
import com.saneforce.milksales.databinding.ActivityCheckIn2Binding;
import com.saneforce.milksales.fragments.GateInOutFragment;
import com.saneforce.milksales.fragments.MonthlyFragment;
import com.saneforce.milksales.fragments.TodayFragment;
import com.saneforce.milksales.session.SessionHandler;
import com.saneforce.milksales.session.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInActivity2 extends AppCompatActivity {
    private ActivityCheckIn2Binding binding;
    private static final String KEY_My_PREFERENCES = "MyPrefs";
    private static final String KEY_CHECK_IN_INFO = "CheckInDetail";

    private final Context context = this;

    private MyViewPagerAdapter myViewPagerAdapter;

    private SharedPreferences SHARED_USER_DETAILS;
    private Shared_Common_Pref SHARED_COMMON_PREF;
    private SharedPreferences SHARED_CHECK_IN_DETAILS;

    private String viewMode = "";
    private DatabaseHandler mDatabase;

    private Common_Class COMMON_CLASS;
    com.saneforce.milksales.Activity_Hap.Common_Class DT = new com.saneforce.milksales.Activity_Hap.Common_Class();

    private Integer ClosingKm = 0;
    private String onDuty = "", ClosingDate = "";
    private SharedPreferences.Editor editors;
    private SharedPreferences SHARED_PREFERENCE;
    private String imageProfile = "";
    private String mSfType = "";

    private SessionHandler session;
    private User user;
    private Integer type;
    private Integer OTFlg = 0;
    TextView approvalcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckIn2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDatabase = new DatabaseHandler(context);

        initSession();
        initSharedPref();

        if (mSfType.equals("1")) {
            binding.myDayPlanBtn.setVisibility(View.VISIBLE);
            binding.checkInBtn.setVisibility(View.GONE);
        }

        getMyDayPlan(1, "check/mydayplan");
        getHapLocations();
        getHAPWorkTypes();

        checkInTimer();
        loadFragment();

        type = (SHARED_USER_DETAILS.getInt("CheckCount", 0));

        COMMON_CLASS = new Common_Class(this);

        /*  NOT USED NEW DESIGN
              String eMail = SHARED_USER_DETAILS.getString("email", "");
              String sSFName = SHARED_USER_DETAILS.getString("SfName", "");
         */

        mSfType = SHARED_USER_DETAILS.getString("Sf_Type", "");
        OTFlg = SHARED_USER_DETAILS.getInt("OTFlg", 0);

        imageProfile = SHARED_USER_DETAILS.getString("url", "");

        binding.userName.setText(SHARED_USER_DETAILS.getString("SfName", ""));
        imageProfile = SHARED_USER_DETAILS.getString("url", "");
//        try {
//            Uri Profile = Uri.parse(SHARED_COMMON_PREF.getvalue(Shared_Common_Pref.Profile));
//            Glide.with(this).load(Profile).into(binding.imageViewUserProfile);
//        } catch (Exception e) {
//        }

        if (getIntent().getExtras() != null) {
            Bundle params = getIntent().getExtras();
            viewMode = params.getString("Mode");
        }

        getcountdetails();
        /*  NOT USED NEW DESIGN
            updateFlxlayout();
        */

        onClick();

          new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(() -> {
                            checkInTimer();
                            Log.e("check_in_home", "Time counter : updated");
                        });
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }.start();

            /* NOT USED NEW DESIGN

        mRelApproval = findViewById(R.id.rel_app);
        lblEmail.setText(eMail);

        btMyQR = findViewById(R.id.myQR);
        linMyday.setVisibility(View.GONE);

        if (sSFType.equals("1")) {
            linMyday.setVisibility(View.VISIBLE);
            linHolidayWorking.setVisibility(View.GONE);
            linCheckin.setVisibility(View.GONE);
        }

        linRequstStaus = (findViewById(R.id.lin_request_status));
        linReport = (findViewById(R.id.lin_report));
        linOnDuty = (findViewById(R.id.lin_onduty));
        linSFA = findViewById(R.id.lin_sfa);

        linSFA.setVisibility(View.GONE);

        linOnDuty.setVisibility(View.GONE);
        if (sSFType.equals("0"))
            linOnDuty.setVisibility(View.VISIBLE);
        else {
            linSFA.setVisibility(View.VISIBLE);
            linReCheck.setVisibility(View.VISIBLE);
        }

        if (linOnDuty.getVisibility() == View.VISIBLE) {
            linCheckin.setVisibility(View.VISIBLE);
            linHolidayWorking.setVisibility(View.VISIBLE);
        } else {
            linCheckin.setVisibility(View.GONE);
        }
        */


        /*   NOT USED NEW DESIGN

        linApprovals = findViewById(R.id.lin_approvals);
        linTaClaim = (findViewById(R.id.lin_ta_claim));
        linExtShift = (findViewById(R.id.lin_extenden_shift));
        linExtShift.setVisibility(View.GONE);
        if (OTFlg == 1) linExtShift.setVisibility(View.VISIBLE);
        linTourPlan = (findViewById(R.id.lin_tour_plan));
        linTourPlan.setVisibility(View.GONE);
        if (sSFType.equals("1")) linTourPlan.setVisibility(View.VISIBLE);
        linExit = (findViewById(R.id.lin_exit));
        approvalcount = findViewById(R.id.approvalcount);

        if (UserDetails.getInt("CheckCount", 0) <= 0) {
            mRelApproval.setVisibility(View.GONE);
            approvalcount.setVisibility(View.GONE);
        } else {
            mRelApproval.setVisibility(View.VISIBLE);
        }
        */
    }

    private void getHAPWorkTypes() {
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", SHARED_USER_DETAILS.getString("Sfcode", ""));
            jParam.put("div", SHARED_USER_DETAILS.getString("Divcode", ""));
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("get/worktypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    mDatabase.deleteMasterData("HAPWorkTypes");
                    mDatabase.addMasterData("HAPWorkTypes", response.body());
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSharedPref() {
        SHARED_CHECK_IN_DETAILS = getSharedPreferences(KEY_CHECK_IN_INFO, Context.MODE_PRIVATE);
        SHARED_USER_DETAILS = getSharedPreferences(KEY_My_PREFERENCES, Context.MODE_PRIVATE);
        SHARED_COMMON_PREF = new Shared_Common_Pref(context);
    }

    private void getMyDayPlan(int flag, String axnString) {
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("axn", axnString);
            QueryString.put("Sf_code", SHARED_USER_DETAILS.getString("Sfcode", ""));
            QueryString.put("Date", Common_Class.GetDate());
            QueryString.put("divisionCode", SHARED_USER_DETAILS.getString("Divcode", ""));
            QueryString.put("desig", "MGR");
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, "[]");

        mCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    Log.v("GET_MYDAY_PLAN", jsonObject.toString());
                    Integer MotCount = Integer.valueOf(jsonObject.getString("checkMOT"));

                    ClosingKm = Integer.valueOf(jsonObject.getString("CheckEndKM"));
                    ClosingDate = jsonObject.getString("CheckEndDT");
                    /* *********  Missing KM Auto Asking ******* */
                    if (ClosingKm == 1) {
                        Intent closingIntet = new Intent(context, AllowanceActivityTwo.class);
                        closingIntet.putExtra("Cls_con", "cls");
                        closingIntet.putExtra("Cls_dte", ClosingDate);
                        startActivity(closingIntet);
                        finish();
                        return;
                    }


                    Log.v("MOT_COUNT", String.valueOf(MotCount));

//                    if (MotCount > 0)
//                        linReCheck.setVisibility(View.VISIBLE);

                    onDuty = jsonObject.getString("CheckOnduty");
                    Log.v("ONDUTY_RESPONSE", jsonObject.getString("CheckOnduty"));

                    SHARED_PREFERENCE = getSharedPreferences(KEY_My_PREFERENCES, Context.MODE_PRIVATE);
                    editors = SHARED_PREFERENCE.edit();
                    editors.putString("Onduty", onDuty);
                    editors.putString("ShiftDuty", jsonObject.getString("Todaycheckin_Flag"));
                    editors.commit();

                    binding.checkInBtn.setVisibility(View.VISIBLE);
                    if (flag == 1 && mSfType.equals("1")) {
                        JSONArray jsoncc = jsonObject.getJSONArray("Checkdayplan");
                        if (jsoncc.length() > 0) {

                            if (jsoncc.getJSONObject(0).getInt("Cnt") < 1) {
                                Intent intent = new Intent(context, AllowanceActivity.class);
                                intent.putExtra("My_Day_Plan", "One");
                                startActivity(intent);
                            } else {
                                binding.myDayPlanBtn.setVisibility(View.GONE);

                                if (jsoncc.getJSONObject(0).getString("wtype").equalsIgnoreCase("43")) {
                                    binding.checkInBtn.setVisibility(View.GONE);
                                    // NOT USE NEW DESIGN
                                    //  linReCheck.setVisibility(View.GONE);

                                } else {
                                    binding.checkInBtn.setVisibility(View.VISIBLE);
                                }
                                // NOT USE NEW DESIGN
                                // linHolidayWorking.setVisibility(View.VISIBLE);
                                // updateFlxlayout();
                            }
                        } else {
                            binding.checkInBtn.setVisibility(View.GONE);
                            binding.myDayPlanBtn.setVisibility(View.VISIBLE);

                            // NOT USE NEW DESIGN
                            //linHolidayWorking.setVisibility(View.GONE);
                            //updateFlxlayout();
                        }

//                        Log.v("wrkType:",shared_common_pref.getvalue("worktype", ""));
//                        if (shared_common_pref.getvalue("worktype", "").equalsIgnoreCase("43")) {
//                            linCheckin.setVisibility(View.GONE);
//                            linReCheck.setVisibility(View.GONE);
//                        }

                    } else {
                        String success = jsonObject.getString("success");
                        String Msg = jsonObject.getString("msg");
                        if (!Msg.equals("")) {
                            AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });
                        } else {
                            AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "YES", "NO", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    COMMON_CLASS.CommonIntentwithoutFinishputextra(ShiftTimeActivity.class, "Mode", "extended");
                                    /*Intent intent = new Intent(getApplicationContext(), Checkin.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Extended_Flag", "extended");
                                    startActivity(intent);*/
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            // Toast.makeText(Dashboard.this, "Send To Checkin", Toast.LENGTH_SHORT).show();
                        }
                    }
                    // updateFlxlayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("MDPError", t.getMessage());
            }
        });
    }

    /*  NOT USED NEW DESIGN

       public void updateFlxlayout() {
        FlexboxLayout flexboxLayout = findViewById(R.id.flxlayut);
        View flxlastChild = null;
        int flg = 0;
        Log.d("TagName_FlexCount", String.valueOf(flexboxLayout.getChildCount()));
        for (int il = 0; il < flexboxLayout.getChildCount(); il++) {
            if (flexboxLayout.getChildAt(il).getVisibility() == View.VISIBLE) {
                flxlastChild = flexboxLayout.getChildAt(il);
                if (flg == 1)
                    flg = 0;
                else
                    flg = 1;
                FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) flxlastChild.getLayoutParams();
                Log.d("TagName", flxlastChild.toString() + " - " + lp.getFlexBasisPercent() + "-" + flg);
                lp.setFlexBasisPercent(0.47f);
                flxlastChild.setLayoutParams(lp);
            }
        }
        if (flg == 1) {
            FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) flxlastChild.getLayoutParams();
            lp.setFlexBasisPercent(100);
            flxlastChild.setLayoutParams(lp);
        }
    }

     */

    public void getHapLocations() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> GetHAPLocation = service.GetHAPLocation(SHARED_USER_DETAILS.getString("Divcode", ""), SHARED_USER_DETAILS.getString("Sfcode", ""), commonLeaveType);
        GetHAPLocation.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mDatabase.deleteMasterData("HAPLocations");
                mDatabase.addMasterData("HAPLocations", response.body());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    public void getcountdetails() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "ViewAllCount");
        QueryString.put("sfCode", SHARED_USER_DETAILS.getString("Sfcode", ""));
        QueryString.put("State_Code", SHARED_USER_DETAILS.getString("State_Code", ""));
        QueryString.put("divisionCode", SHARED_USER_DETAILS.getString("Divcode", ""));
        QueryString.put("rSF", SHARED_USER_DETAILS.getString("Sfcode", ""));
        QueryString.put("desig", "MGR");
        String commonworktype = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, commonworktype);

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSEcount", "response Tp_View: " + new Gson().toJson(response.body()));
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    // int TC=Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"));
                    //jsonObject.getString("leave"))
                    Log.e("TOTAl_COUNT", String.valueOf(Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"))));
                    //count = count +

                    Shared_Common_Pref.TotalCountApproval = jsonObject.getInt("leave") + jsonObject.getInt("Permission") +
                            jsonObject.getInt("vwOnduty") + jsonObject.getInt("vwmissedpunch") +
                            jsonObject.getInt("vwExtended") + jsonObject.getInt("TountPlanCount") +
                            jsonObject.getInt("FlightAppr") +
                            jsonObject.getInt("HolidayCount") + jsonObject.getInt("DeviationC") +
                            jsonObject.getInt("CancelLeave") + jsonObject.getInt("ExpList");
                    /* NOT USED NEW DESIGN
                        approvalcount.setText(String.valueOf(Shared_Common_Pref.TotalCountApproval));
                        approvalcount.setVisibility(View.GONE);
                        if(Shared_Common_Pref.TotalCountApproval>0) approvalcount.setVisibility(View.VISIBLE);
                     */

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                COMMON_CLASS.ProgressdialogShow(2, "");
            }
        });

    }

    private void checkInTimer() {
        Log.e("check_in_home", "Check in Enabled : " + user.getCheckInEnabled());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                100);

        if (user.getCheckInEnabled().equals("true")){
            String checkInTimeStamp = user.getCheckInTimeStamp();
            Log.e("check_in_home", "Check in TimeStamp : " + checkInTimeStamp);

            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            Date d1;
            Date d2;

            String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String mTimeDateFormat  = mDate +" "+mTime;

            try {
                d1 = format.parse(checkInTimeStamp);
                d2 = format.parse(mTimeDateFormat);

                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                binding.checkInBtn.setText("CHECK OUT (" + addZero(Math.toIntExact(diffHours)) +":" +  addZero(Math.toIntExact(diffMinutes)) + ":" + diffSeconds + ")");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.checkInBtn.setLayoutParams(params);
                binding.checkInBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_check_out1) );
            } else {
                binding.checkInBtn.setLayoutParams(params);
                binding.checkInBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.button_check_out1));
            }
            params.setMargins(20, 30,20,0);
        }
    }
    public String addZero(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }


    private void initSession() {
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();
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


            int val = SHARED_USER_DETAILS.getInt("checkRadius", 0);
            Log.v("CHECKIN:", "" + val);
            if (/*sSFType.equals("0")*/SHARED_USER_DETAILS.getInt("checkRadius", 0) == 1) {
                String[] latlongs = SHARED_USER_DETAILS.getString("HOLocation", "").split(":");
                //  String[] latlongs = "13.0299326:80.2414088".split(":");

                Intent intent = new Intent(context, MapDirectionActivity.class);
                intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                intent.putExtra(Constants.DEST_NAME, "HOLocation");
                intent.putExtra(Constants.NEW_OUTLET, "checkin");
                startActivity(intent);
            } else {

                String ETime = SHARED_CHECK_IN_DETAILS.getString("CINEnd", "");
                if (!ETime.equalsIgnoreCase("")) {
                    String CutOFFDt = SHARED_CHECK_IN_DETAILS.getString("ShiftCutOff", "0");
                    String SftId = SHARED_CHECK_IN_DETAILS.getString("Shift_Selected_Id", "0");
                    if (DT.GetCurrDateTime(this).getTime() >= DT.getDate(CutOFFDt).getTime() || SftId == "0") {
                        ETime = "";
                    }
                }
                if (!ETime.equalsIgnoreCase("")) {
                    Intent takePhoto = new Intent(this, ImageCaptureActivity.class);
                    takePhoto.putExtra("Mode", "CIN");
                    takePhoto.putExtra("ShiftId", SHARED_CHECK_IN_DETAILS.getString("Shift_Selected_Id", ""));
                    takePhoto.putExtra("ShiftName", SHARED_CHECK_IN_DETAILS.getString("Shift_Name", ""));
                    takePhoto.putExtra("On_Duty_Flag", SHARED_CHECK_IN_DETAILS.getString("On_Duty_Flag", "0"));
                    takePhoto.putExtra("ShiftStart", SHARED_CHECK_IN_DETAILS.getString("ShiftStart", "0"));
                    takePhoto.putExtra("ShiftEnd", SHARED_CHECK_IN_DETAILS.getString("ShiftEnd", "0"));
                    takePhoto.putExtra("ShiftCutOff", SHARED_CHECK_IN_DETAILS.getString("ShiftCutOff", "0"));
                    startActivity(takePhoto);
                } else {
                    Intent i = new Intent(this, ShiftTimeActivity.class);
                    startActivity(i);
                }

            }
        });

        binding.myDayPlanBtn.setOnClickListener(v -> {
            if (ClosingKm == 1) {
                Intent closingIntet = new Intent(this, AllowanceActivityTwo.class);
                closingIntet.putExtra("Cls_con", "cls");
                closingIntet.putExtra("Cls_dte", ClosingDate);
                startActivity(closingIntet);
                finish();
            } else {
                startActivity(new Intent(this, MyDayPlanActivity.class));
            }
        });
        binding.leaveRequestStatus.setOnClickListener(v -> {
            startActivity(new Intent(context, Leave_Dashboard.class));
        });
        binding.taClaim.setOnClickListener(v -> {
            Shared_Common_Pref.TravelAllowance = 0;
            startActivity(new Intent(context, TAClaimActivity.class)); //Travel_Allowance
        });
        binding.sfa.setOnClickListener(v -> {
            startActivity(new Intent(context, SFA_Activity.class));
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
        binding.canteenScan.setOnClickListener(v -> {
            Intent intent = new Intent(context, CateenToken.class);
            startActivity(intent);
        });

        /*   NOT USED NEW DESIGN

           case R.id.lin_report:
                Intent Dashboard = new Intent(this, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "RPT");
                startActivity(Dashboard);
                break;

           case R.id.lin_approvals:
                Shared_Common_Pref.TravelAllowance = 1;
                startActivity(new Intent(this, Approvals.class));
                break;

           case R.id.lin_approvals:
                Shared_Common_Pref.TravelAllowance = 1;
                startActivity(new Intent(this, Approvals.class));
                break;

           case R.id.lin_myday_plan:
                if (ClosingKm == 1) {
                    Intent closingIntet = new Intent(this, AllowanceActivityTwo.class);
                    closingIntet.putExtra("Cls_con", "cls");
                    closingIntet.putExtra("Cls_dte", ClosingDate);
                    startActivity(closingIntet);
                    finish();
                } else {
                    startActivity(new Intent(this, Mydayplan_Activity.class));
                }
                break;

           case R.id.lin_RecheckIn:
                Intent recall = new Intent(this, AllowanceActivity.class);
                recall.putExtra("Recall", "Recall");
                startActivity(recall);
                break;

           case R.id.lin_tour_plan:
                Shared_Common_Pref.Tp_Approvalflag = "0";
                startActivity(new Intent(this, Tp_Month_Select.class));

                break;

                 case R.id.lin_holiday_working:
                AlertDialogBox.showDialog(Dashboard.this, HAPApp.Title, "Are you sure want to Check-in with Hoilday Entry", "YES", "NO", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "holidayentry");
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.lin_onduty:

                // startActivity(new Intent(this, On_Duty_Activity.class));
                Intent oDutyInt = new Intent(this, On_Duty_Activity.class);
                oDutyInt.putExtra("Onduty", onDuty);
                startActivity(oDutyInt);

                break;
                 case R.id.lin_exit:
                shared_common_pref.clear_pref(Constants.LOGIN_DATA);
                SharedPreferences.Editor editor = UserDetails.edit();
                editor.putBoolean("Login", false);
                editor.apply();
                CheckInDetails.edit().clear().commit();
                Intent playIntent = new Intent(this, SANGPSTracker.class);
                stopService(playIntent);
                finishAffinity();

                break;
            case R.id.lin_extenden_shift:
                validateExtened("ValidateExtended");
                break;
         */
    }

    protected void onResume() {
        super.onResume();
        //Get_MydayPlan(1, "check/mydayplan");

        checkInTimer();

        Boolean CheckIn = SHARED_CHECK_IN_DETAILS.getBoolean("CheckIn", false);
        if (CheckIn == true) {
            Shared_Common_Pref.Sf_Code = SHARED_CHECK_IN_DETAILS.getString("Sfcode", "");
            Shared_Common_Pref.Sf_Name = SHARED_CHECK_IN_DETAILS.getString("SfName", "");
            Shared_Common_Pref.Div_Code = SHARED_CHECK_IN_DETAILS.getString("Divcode", "");
            Shared_Common_Pref.StateCode = SHARED_CHECK_IN_DETAILS.getString("State_Code", "");

            String ActStarted = SHARED_COMMON_PREF.getvalue("ActivityStart");
            if (ActStarted.equalsIgnoreCase("true")) {
                Intent aIntent;
                String sDeptType = SHARED_USER_DETAILS.getString("DeptType", "");
                if (sDeptType.equalsIgnoreCase("1")) {
                    aIntent = (new Intent(getApplicationContext(), SFA_Activity.class));
                } else {
                    Shared_Common_Pref.Sync_Flag = "0";
                    aIntent = new Intent(context, SFA_Activity.class);
                }
                startActivity(aIntent);
                finish();
            } else {
                Intent Dashboard = new Intent(context, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "CIN");
                startActivity(Dashboard);
                finish();
            }
        }
    }
}