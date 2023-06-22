package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.ProjectionApprovListActivity;
import com.saneforce.milksales.Status_Activity.Advance_Status_Activity;
import com.saneforce.milksales.Status_Activity.Extended_Shift_Activity;
import com.saneforce.milksales.Status_Activity.Leave_Status_Activity;
import com.saneforce.milksales.Status_Activity.MissedPunch_Status_Activity;
import com.saneforce.milksales.Status_Activity.Onduty_Status_Activity;
import com.saneforce.milksales.Status_Activity.Permission_Status_Activity;
import com.saneforce.milksales.Status_Activity.WeekOff_Status_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Approvals extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout linProjectionApprove, linQpsApprove, LeaveRequest, AdvanceRequest,FlightAppr, PermissionRequest,
            OnDuty, MissedPunch, ExtendedShift, TravelAllowance, TourPlan, lin_leavecancel_histry,
            lin_newjoin, lin_leaveholidaystatus;
    LinearLayout LeaveStatus, advanceStatus, DaExcptStaus, PermissionStatus, OnDutyStatus, MissedStatus, ExtdShift, lin_weekoff, linLeaveCancel,
            lin_DeviationApproval, lin_holidayentryApproval, linDaExceptionEntry, llTrvlAllowStatus,llFlightApprHist, pjpHistory;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    SharedPreferences Setups;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    public static final String SetupsInfo = "MySettings";
    TextView countLeaveRequest, extendedcount,countFlightAppr , countPermissionRequest, countOnDuty, countMissedPunch,
            countTravelAllowance, countTourPlan, txt_holiday_count, txt_deviation_count, txt_leavecancel_count,txt_newjoin_count,txt_Advance_req_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvals);
        shared_common_pref = new Shared_Common_Pref(this);

        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        Setups = getSharedPreferences(SetupsInfo, Context.MODE_PRIVATE);
        lin_leavecancel_histry = findViewById(R.id.lin_leavecancel_histry);
        lin_leaveholidaystatus = findViewById(R.id.lin_leaveholidaystatus);
        linLeaveCancel = findViewById(R.id.lin_leave_cancel);
        linDaExceptionEntry = findViewById(R.id.lin_daExp_entry);
        llTrvlAllowStatus = findViewById(R.id.lin_travel_histry);

        DaExcptStaus = findViewById(R.id.lin_da_excep_status);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
                Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
                Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
                Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });


        common_class = new Common_Class(this);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        LeaveRequest = findViewById(R.id.lin_leave_req);
        AdvanceRequest = findViewById(R.id.lin_Advance_req);
        FlightAppr = findViewById(R.id.lin_FlightAppr);
        PermissionRequest = findViewById(R.id.lin_per_req);
        OnDuty = findViewById(R.id.lin_on_duty);
        MissedPunch = findViewById(R.id.lin_miss_punch);
        ExtendedShift = findViewById(R.id.lin_ext_shift_status);
        TravelAllowance = findViewById(R.id.lin_travel_allow);
        TourPlan = findViewById(R.id.lin_tour_plan);
        lin_newjoin = findViewById(R.id.lin_newjoin);

        /*Status Linear*/
        LeaveStatus = findViewById(R.id.lin_leav_sta);
        advanceStatus = findViewById(R.id.lin_Advance_sta);
        PermissionStatus = findViewById(R.id.lin_per_sta);
        OnDutyStatus = findViewById(R.id.lin_duty_sta);
        MissedStatus = findViewById(R.id.lin_miss_sta);
        ExtdShift = findViewById(R.id.lin_ext_shift);
        lin_weekoff = findViewById(R.id.lin_weekoff);
        /*Request Text*/
        extendedcount = findViewById(R.id.txt_week_off_count);
        countLeaveRequest = findViewById(R.id.txt_leave_req_count);
        countPermissionRequest = findViewById(R.id.txt_per_req_count);
        countFlightAppr = findViewById(R.id.txt_FlightAppr_count);
        countOnDuty = findViewById(R.id.txt_on_duty_count);
        countMissedPunch = findViewById(R.id.txt_miss_punch_count);
        countTravelAllowance = findViewById(R.id.txt_trvl_all);
        countTourPlan = findViewById(R.id.txt_tour_plan);
        txt_holiday_count = findViewById(R.id.txt_holiday_count);
        txt_deviation_count = findViewById(R.id.txt_deviation_count);
        txt_leavecancel_count = findViewById(R.id.txt_leave_cancel_req_count);
        txt_newjoin_count = findViewById(R.id.txt_newjoin_count);
        txt_Advance_req_count = findViewById(R.id.txt_Advance_req_count);

        lin_holidayentryApproval = findViewById(R.id.lin_holidayentryApproval);
        lin_DeviationApproval = findViewById(R.id.lin_DeviationApproval);
        linProjectionApprove = findViewById(R.id.lin_productProjectionApproval);
        llFlightApprHist=findViewById(R.id.lin_FlightApprHist);
        linQpsApprove = findViewById(R.id.lin_qps);
        pjpHistory=findViewById(R.id.lin_pjp_histry);


        /*Status text*/
        /*SetOnClickListner*/

        linQpsApprove.setOnClickListener(this);
        LeaveRequest.setOnClickListener(this);
        AdvanceRequest.setOnClickListener(this);
        PermissionRequest.setOnClickListener(this);
        FlightAppr.setOnClickListener(this);
        OnDuty.setOnClickListener(this);
        MissedPunch.setOnClickListener(this);
        lin_newjoin.setOnClickListener(this);
        ExtendedShift.setOnClickListener(this);
        TravelAllowance.setOnClickListener(this);
        TourPlan.setOnClickListener(this);
        LeaveStatus.setOnClickListener(this);
        advanceStatus.setOnClickListener(this);
        PermissionStatus.setOnClickListener(this);
        OnDutyStatus.setOnClickListener(this);
        MissedStatus.setOnClickListener(this);
        ExtdShift.setOnClickListener(this);
        lin_weekoff.setOnClickListener(this);
        lin_leavecancel_histry.setOnClickListener(this);
        llFlightApprHist.setOnClickListener(this);
        lin_leaveholidaystatus.setOnClickListener(this);
        linLeaveCancel.setOnClickListener(this);
        lin_holidayentryApproval.setOnClickListener(this);
        lin_DeviationApproval.setOnClickListener(this);
        linDaExceptionEntry.setOnClickListener(this);
        DaExcptStaus.setOnClickListener(this);
        linProjectionApprove.setOnClickListener(this);
        llTrvlAllowStatus.setOnClickListener(this);
        pjpHistory.setOnClickListener(this);

        FlightAppr.setVisibility(View.GONE);
        llFlightApprHist.setVisibility(View.GONE);
        if(UserDetails.getInt("FlightAllowed", 0)==2){
            FlightAppr.setVisibility(View.VISIBLE);
            llFlightApprHist.setVisibility(View.VISIBLE);
        }
        getcountdetails();
    }

    public void getcountdetails() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "ViewAllCount");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("desig", "MGR");
        String commonworktype = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, commonworktype);

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    countLeaveRequest.setText(jsonObject.getString("leave"));
                    countPermissionRequest.setText(jsonObject.getString("Permission"));
                    countFlightAppr.setText(jsonObject.getString("FlightAppr"));
                    countOnDuty.setText(jsonObject.getString("vwOnduty"));
                    countTravelAllowance.setText(jsonObject.getString("ExpList"));
                    countMissedPunch.setText(jsonObject.getString("vwmissedpunch"));
                    countTourPlan.setText(jsonObject.getString("TountPlanCount"));
                    extendedcount.setText(jsonObject.getString("vwExtended"));
                    txt_holiday_count.setText(jsonObject.getString("HolidayCount"));
                    txt_deviation_count.setText(jsonObject.getString("DeviationC"));
                    txt_leavecancel_count.setText(jsonObject.getString("CancelLeave"));
                    txt_newjoin_count.setText(jsonObject.getString("Newjoin"));
                    txt_Advance_req_count.setText(jsonObject.getString("Advance"));

//                    if (Integer.parseInt(jsonObject.getString("leave")) < 1) {
//                        txt_newjoin_count.setVisibility(View.GONE);
//                        findViewById(R.id.ivNewjoinArw).setVisibility(View.GONE);
////                        findViewById(R.id.llLeave).setVisibility(View.GONE);
////                        LeaveRequest.setVisibility(View.GONE);
//                    }
                    if (Integer.parseInt(jsonObject.getString("leave")) < 1) {
                        countLeaveRequest.setVisibility(View.GONE);
                        findViewById(R.id.ivLvArw).setVisibility(View.GONE);
//                        findViewById(R.id.llLeave).setVisibility(View.GONE);
//                        LeaveRequest.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("Newjoin")) < 1) {
                        txt_newjoin_count.setVisibility(View.GONE);
                        findViewById(R.id.ivNewjoinArw).setVisibility(View.GONE);
//                        findViewById(R.id.llLeave).setVisibility(View.GONE);
//                        LeaveRequest.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("Advance")) < 1) {
                        txt_Advance_req_count.setVisibility(View.GONE);
                        findViewById(R.id.ivAdvArw).setVisibility(View.GONE);
//                        findViewById(R.id.llLeave).setVisibility(View.GONE);
//                        LeaveRequest.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(jsonObject.getString("FlightAppr")) < 1) {
                        countFlightAppr.setVisibility(View.GONE);
                        findViewById(R.id.ivFlightApprArw).setVisibility(View.GONE);
//                        findViewById(R.id.llPermission).setVisibility(View.GONE);
//                        PermissionRequest.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("Permission")) < 1) {
                        countPermissionRequest.setVisibility(View.GONE);
                        findViewById(R.id.ivPermissionArw).setVisibility(View.GONE);
//                        findViewById(R.id.llPermission).setVisibility(View.GONE);
//                        PermissionRequest.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(jsonObject.getString("vwOnduty")) < 1) {
                        countOnDuty.setVisibility(View.GONE);
                        findViewById(R.id.ivOnDutyArw).setVisibility(View.GONE);
//                        findViewById(R.id.llOnDuty).setVisibility(View.GONE);
//                        OnDuty.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("ExpList")) < 1) {
                        countTravelAllowance.setVisibility(View.GONE);
                        findViewById(R.id.ivTrvlAllowArr).setVisibility(View.GONE);

//                        findViewById(R.id.llTrvlAlow).setVisibility(View.GONE);
//                        TravelAllowance.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("vwmissedpunch")) < 1) {
                        countMissedPunch.setVisibility(View.GONE);
                        findViewById(R.id.ivMissPunchArw).setVisibility(View.GONE);
//                        findViewById(R.id.llMissedPunch).setVisibility(View.GONE);
//                        MissedPunch.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("TountPlanCount")) < 1) {
                        countTourPlan.setVisibility(View.GONE);
                        findViewById(R.id.ivWrkPlanArw).setVisibility(View.GONE);
//                        findViewById(R.id.llWrkPln).setVisibility(View.GONE);
//                        TourPlan.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("vwExtended")) < 1) {
                        extendedcount.setVisibility(View.GONE);
                        findViewById(R.id.ivXtndSft).setVisibility(View.GONE);
//                        findViewById(R.id.llExtndSft).setVisibility(View.GONE);
//                        ExtendedShift.setVisibility(View.GONE);
                    }

                    if (Integer.parseInt(jsonObject.getString("HolidayCount")) < 1) {
                        txt_holiday_count.setVisibility(View.GONE);
                        findViewById(R.id.ivHoliEntryArw).setVisibility(View.GONE);
//                        findViewById(R.id.llHoliEntry).setVisibility(View.GONE);
//                        lin_holidayentryApproval.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("DeviationC")) < 1) {
                        txt_deviation_count.setVisibility(View.GONE);
                        findViewById(R.id.ivDevEntryArw).setVisibility(View.GONE);
//                        findViewById(R.id.llDevEntry).setVisibility(View.GONE);
//                        lin_DeviationApproval.setVisibility(View.GONE);
                    }
                    if (Integer.parseInt(jsonObject.getString("CancelLeave")) < 1) {
                        txt_leavecancel_count.setVisibility(View.GONE);
                        findViewById(R.id.ivLvCancelArw).setVisibility(View.GONE);
//                        findViewById(R.id.llLvCancel).setVisibility(View.GONE);
//                        linLeaveCancel.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_travel_histry:
                startActivity(new Intent(Approvals.this, TAHistory.class));
                break;
            case R.id.lin_leave_req:
                startActivity(new Intent(Approvals.this, Leave_Approval.class));
                finish();
                break;
            case R.id.lin_newjoin:
                startActivity(new Intent(Approvals.this, NewJoineeApproval.class));
                finish();
                break;
            case R.id.lin_Advance_req:
                startActivity(new Intent(Approvals.this, Advance_Approval.class));
                finish();
                break;

            case R.id.lin_FlightAppr:
                startActivity(new Intent(Approvals.this, FlightBookingApproval.class));
                finish();
                break;
            case R.id.lin_FlightApprHist:
                startActivity(new Intent(Approvals.this, FlightBooking_Approval_History.class));
                finish();
                break;
            case R.id.lin_productProjectionApproval:
                startActivity(new Intent(Approvals.this, ProjectionApprovListActivity.class));
                finish();
                break;

            case R.id.lin_leave_cancel:
                startActivity(new Intent(Approvals.this, Leave_Cancel_Approval.class));
                finish();
                break;

            case R.id.lin_per_req:
                startActivity(new Intent(Approvals.this, Permission_Approval.class));
                finish();
                break;

            case R.id.lin_on_duty:
                startActivity(new Intent(Approvals.this, Onduty_approval.class));
                finish();
                break;

            case R.id.lin_miss_punch:
                startActivity(new Intent(Approvals.this, Missed_punch_Approval.class));
                finish();
                break;

            case R.id.lin_ext_shift_status:
                startActivity(new Intent(Approvals.this, Extendedshift_approval.class));
                finish();
                break;

            case R.id.lin_travel_allow:
                // startActivity(new Intent(Approvals.this, TAApprovalActivity.class));

                common_class.getDb_310Data(Constants.WEEKLY_EXPENSE, this);

                break;

            case R.id.lin_tour_plan:
                startActivity(new Intent(Approvals.this, Tp_Approval.class));
                finish();
                break;
            case R.id.lin_qps:
                startActivity(new Intent(Approvals.this, QPS_Approval.class));
                finish();
                break;


            case R.id.lin_daExp_entry:
                startActivity(new Intent(Approvals.this, DAExcApproval.class));
                finish();
                break;

            case R.id.lin_leav_sta:
                common_class.CommonIntentwithoutFinishputextra(Leave_Status_Activity.class, "AMod", "1");
                finish();
                break;
            case R.id.lin_Advance_sta:
                common_class.CommonIntentwithoutFinishputextra(Advance_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_per_sta:
                common_class.CommonIntentwithoutFinishputextra(Permission_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_duty_sta:
                common_class.CommonIntentwithoutFinishputextra(Onduty_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_miss_sta:
                common_class.CommonIntentwithoutFinishputextra(MissedPunch_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_ext_shift:
                common_class.CommonIntentwithoutFinishputextra(Extended_Shift_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_weekoff:
                common_class.CommonIntentwithoutFinishputextra(WeekOff_Status_Activity.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_leavecancel_histry:
                common_class.CommonIntentwithoutFinishputextra(LeaveCancelRequestStatus.class, "AMod", "1");
                finish();
                break;
            case R.id.lin_leaveholidaystatus:
                common_class.CommonIntentwithoutFinishputextra(HolidayEntryStatus.class, "AMod", "1");
                finish();
                break;

            case R.id.lin_da_excep_status:
                common_class.CommonIntentwithoutFinishputextra(DaExceptionStatus.class, "AMod", "1");
                finish();
                break;
            case R.id.lin_pjp_histry:
                startActivity(new Intent(Approvals.this, TP_Approval_Status.class));
                finish();
                break;
            case R.id.lin_holidayentryApproval:
                startActivity(new Intent(Approvals.this, Holiday_Entry_Approval.class));
                finish();
                break;
            case R.id.lin_DeviationApproval:
                startActivity(new Intent(Approvals.this, Deviation_Entry_Approval.class));
                finish();
                break;

        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getcountdetails();
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                    if (CheckIn == true) {
                        Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                        Dashboard.putExtra("Mode", "CIN");
                        startActivity(Dashboard);
                    } else
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));

                }
            });


    @Override
    public void onBackPressed() {

    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.WEEKLY_EXPENSE:
                    Log.v(key + ":", apiDataResponse);
                    JSONObject obj = new JSONObject(apiDataResponse);
                    if (obj.getBoolean("success")) {
                        startActivity(new Intent(Approvals.this, TACumulativeApproval.class));
                        //  startActivity(new Intent(Approvals.this, TAApprovalActivity.class));
                        // finish();
                    } else {
                        common_class.showMsg(this, obj.getString("Msg"));
                    }

                    break;
            }

        } catch (Exception e) {

        }
    }
}