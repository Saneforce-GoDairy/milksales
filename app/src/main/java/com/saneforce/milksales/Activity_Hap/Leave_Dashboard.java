package com.saneforce.milksales.Activity_Hap;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Activity.Advance_Status_Activity;
import com.saneforce.milksales.Status_Activity.Extended_Shift_Activity;
import com.saneforce.milksales.Status_Activity.FlightBooking_Status_Activity;
import com.saneforce.milksales.Status_Activity.Leave_Status_Activity;
import com.saneforce.milksales.Status_Activity.MissedPunch_Status_Activity;
import com.saneforce.milksales.Status_Activity.Onduty_Status_Activity;
import com.saneforce.milksales.Status_Activity.Permission_Status_Activity;
import com.saneforce.milksales.Status_Activity.WeekOff_Status_Activity;

public class Leave_Dashboard extends AppCompatActivity implements View.OnClickListener {

    Common_Class common_class;
    LinearLayout linNewJoinReq,LeaveRequest, PermissionRequest, FlightTick, WeeklyOff, DeveiationEntry,lin_Advance_req;
    LinearLayout LeaveStatus,AdvanceStatus, PermissionStatus, OnDutyStatus, MissedStatus, WeeklyOffStatus, MissedPunc,flightBookStatus,
            ExtdShift, HolidayEntryStatus, DeviationEntryStatus, LeaveCancelStatus, LinearException, DaExcptStaus;
    TextView countLeaveRequest, countPermissionRequest, countMissedPunch, countWeeklyOff;
    Shared_Common_Pref mShared_common_pref;
    SharedPreferences  UserDetails;
    public static final String UserInfo = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_dashboard);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
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
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);

                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));


            }
        });
        common_class = new Common_Class(this);
        /*Request Linear*/
        LeaveRequest = findViewById(R.id.lin_leave_req);
        linNewJoinReq = findViewById(R.id.lin_newjoin_req);
        PermissionRequest = findViewById(R.id.lin_per_req);
        FlightTick = findViewById(R.id.lin_fligh_mode);
        WeeklyOff = findViewById(R.id.lin_week_off);
        lin_Advance_req = findViewById(R.id.lin_Advance_req);
        DeveiationEntry = findViewById(R.id.lin_deviation_entry);

        /*Status Linear*/
        LeaveStatus = findViewById(R.id.lin_leav_sta);
        AdvanceStatus = findViewById(R.id.lin_Advance_sta);
        flightBookStatus=findViewById(R.id.lin_flight_booking_status);
        PermissionStatus = findViewById(R.id.lin_per_sta);
        OnDutyStatus = findViewById(R.id.lin_duty_sta);
        MissedStatus = findViewById(R.id.lin_miss_sta);
        WeeklyOffStatus = findViewById(R.id.lin_week_off_sta);
        ExtdShift = findViewById(R.id.lin_ext_shift);
        HolidayEntryStatus = findViewById(R.id.lin_holiday_status);
        DeviationEntryStatus = findViewById(R.id.lin_deviation_status);
        LeaveCancelStatus = findViewById(R.id.lin_leave_cancel_status);
        MissedPunc = findViewById(R.id.lin_miss_punch);
        LinearException = findViewById(R.id.lin_da_excep);
        DaExcptStaus = findViewById(R.id.lin_da_excep_status);
        /*Request text*/
        countLeaveRequest = findViewById(R.id.txt_leave_req_count);
        countPermissionRequest = findViewById(R.id.txt_per_req_count);
        countMissedPunch = findViewById(R.id.txt_miss_punch_count);
        countWeeklyOff = findViewById(R.id.txt_week_off_count);
        /*Status text*/
        AdvanceStatus.setOnClickListener(this);
        lin_Advance_req.setOnClickListener(this);
        LeaveRequest.setOnClickListener(this);
        PermissionRequest.setOnClickListener(this);
        FlightTick.setOnClickListener(this);
        WeeklyOff.setOnClickListener(this);
        DeveiationEntry.setOnClickListener(this);

        /*Status Linear*/
        LeaveStatus.setOnClickListener(this);
        PermissionStatus.setOnClickListener(this);
        OnDutyStatus.setOnClickListener(this);
        MissedStatus.setOnClickListener(this);
        WeeklyOffStatus.setOnClickListener(this);
        ExtdShift.setOnClickListener(this);
        HolidayEntryStatus.setOnClickListener(this);
        DeviationEntryStatus.setOnClickListener(this);
        LeaveCancelStatus.setOnClickListener(this);
        LinearException.setOnClickListener(this);
        DaExcptStaus.setOnClickListener(this);
        MissedPunc.setOnClickListener(this);
        flightBookStatus.setOnClickListener(this);
        linNewJoinReq.setOnClickListener(this);
        linNewJoinReq.setVisibility(View.GONE);
        if(UserDetails.getInt("NewJoin", 0)==1){
            linNewJoinReq.setVisibility(View.VISIBLE);
        }

        FlightTick.setVisibility(View.GONE);
        flightBookStatus.setVisibility(View.GONE);
        if(UserDetails.getInt("FlightAllowed", 0)!=0){
            FlightTick.setVisibility(View.VISIBLE);
            flightBookStatus.setVisibility(View.VISIBLE);
        }

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        mShared_common_pref = new Shared_Common_Pref(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lin_Advance_req:
                startActivity(new Intent(Leave_Dashboard.this, AdvanceReq.class));
                break;

            case R.id.lin_leave_req:
                startActivity(new Intent(Leave_Dashboard.this, Leave_Request.class));
                break;
            case R.id.lin_newjoin_req:
                startActivity(new Intent(Leave_Dashboard.this, NewJoinEntry.class));
                break;
            case R.id.lin_per_req:
                startActivity(new Intent(Leave_Dashboard.this, Permission_Request.class));
                break;
            case R.id.lin_fligh_mode:
                startActivity(new Intent(Leave_Dashboard.this, FlightTicketRequest.class));
                break;
            case R.id.lin_miss_punch:
                startActivity(new Intent(Leave_Dashboard.this, Missed_Punch.class));
                break;
            case R.id.lin_week_off:
                startActivity(new Intent(Leave_Dashboard.this, Weekly_Off.class));
                break;
            case R.id.lin_deviation_entry:
                startActivity(new Intent(Leave_Dashboard.this, DeviationEntry.class));
                break;

            case R.id.lin_da_excep:
                startActivity(new Intent(Leave_Dashboard.this, DaExceptionEntry.class));
                break;
            case R.id.lin_leav_sta:
                //   common_class.CommonIntentwithoutFinishputextra(Leave_Status_Activity.class, "AMod", "0");
                startActivity(new Intent(Leave_Dashboard.this, Leave_Status_Activity.class));
                mShared_common_pref.save("AMod", "0");
                break;
            case R.id.lin_Advance_sta:
                startActivity(new Intent(Leave_Dashboard.this, Advance_Status_Activity.class));
                mShared_common_pref.save("AMod", "0");
                break;
            case R.id.lin_flight_booking_status:
                //   common_class.CommonIntentwithoutFinishputextra(Leave_Status_Activity.class, "AMod", "0");
                startActivity(new Intent(Leave_Dashboard.this, FlightBooking_Status_Activity.class));
                mShared_common_pref.save("AMod", "0");
                break;
            case R.id.lin_per_sta:
                // startActivity(new Intent(Leave_Dashboard.this, Permission_Status_Activity.class));
                common_class.CommonIntentwithoutFinishputextra(Permission_Status_Activity.class, "AMod", "0");
                break;
            case R.id.lin_duty_sta:
                // startActivity(new Intent(Leave_Dashboard.this, Onduty_Status_Activity.class));
                common_class.CommonIntentwithoutFinishputextra(Onduty_Status_Activity.class, "AMod", "0");
                break;
            case R.id.lin_miss_sta:
                common_class.CommonIntentwithoutFinishputextra(MissedPunch_Status_Activity.class, "AMod", "0");
                //startActivity(new Intent(Leave_Dashboard.this, MissedPunch_Status_Activity.class));
                break;
            case R.id.lin_week_off_sta:
                //startActivity(new Intent(Leave_Dashboard.this, WeekOff_Status_Activity.class));
                common_class.CommonIntentwithoutFinishputextra(WeekOff_Status_Activity.class, "AMod", "0");
                break;
            case R.id.lin_ext_shift:
                //startActivity(new Intent(Leave_Dashboard.this, Extended_Shift_Activity.class));
                common_class.CommonIntentwithoutFinishputextra(Extended_Shift_Activity.class, "AMod", "0");
                break;

            case R.id.lin_holiday_status:
                //startActivity(new Intent(Leave_Dashboard.this, Extended_Shift_Activity.class));
                common_class.CommonIntentwithoutFinishputextra(HolidayEntryStatus.class, "AMod", "0");
                break;


            case R.id.lin_da_excep_status:
                common_class.CommonIntentwithoutFinishputextra(DaExceptionStatus.class, "AMod", "0");
                finish();
                break;
            case R.id.lin_deviation_status:
                //startActivity(new Intent(Leave_Dashboard.this, Extended_Shift_Activity.class));
                common_class.CommonIntentwithoutFinishputextra(DeviationEntryStatus.class, "AMod", "0");
                break;
            case R.id.lin_leave_cancel_status:
                //startActivity(new Intent(Leave_Dashboard.this, Extended_Shift_Activity.class));
                common_class.CommonIntentwithoutFinishputextra(LeaveCancelRequestStatus.class, "AMod", "0");
                break;
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                    Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                    if (CheckIn == true) {
                        Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                        Dashboard.putExtra("Mode", "CIN");
                        startActivity(Dashboard);
                        finish();

                    } else
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                }
            });

    @Override
    public void onBackPressed() {

    }
}
