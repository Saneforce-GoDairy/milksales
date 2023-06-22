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
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Model_Class.Missed_Punch_Model;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.Missed_Punch_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class Missed_punch_Approval extends AppCompatActivity {
    String Scode;
    String Dcode;
    String Rf_code;

    List<Missed_Punch_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_punch__approval);

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
        recyclerView = findViewById(R.id.Mpunchrecyclerview);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        common_class.ProgressdialogShow(1, "Missed Punch Approval");
        Rf_code = Scode;
        gson = new Gson();
        getmissedpunchdetails();
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Approvals.class);
            }
        });


    }


    public void getmissedpunchdetails() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwmissedpunch", routemaster);

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                //Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                //Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                userType = new TypeToken<ArrayList<Missed_Punch_Model>>() {
                }.getType();


                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);
                Log.e("TAG_TP_RESPONSE", approvalList.toString());
                Log.e("TAG_TP_RESPONSE", userType.toString());
                recyclerView.setAdapter(new Missed_Punch_Adapter(approvalList, R.layout.missed_punch_list_item, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int Name) {

                        Intent intent = new Intent(Missed_punch_Approval.this, Missed_Punch_Approval_Reject.class);
                        intent.putExtra("Sl_No", String.valueOf(approvalList.get(Name).getSlNo()));
                        intent.putExtra("Username", approvalList.get(Name).getSfName());
                        intent.putExtra("Emp_Code", approvalList.get(Name).getEmpCode());
                        intent.putExtra("HQ", approvalList.get(Name).getHQ());
                        intent.putExtra("Designation", approvalList.get(Name).getDesignation());
                        intent.putExtra("AppliedDate", approvalList.get(Name).getAppliedDate());
                        intent.putExtra("MobileNumber", approvalList.get(Name).getMobilenumber());
                        intent.putExtra("Reason", approvalList.get(Name).getReason());
                        intent.putExtra("Shiftonduty", approvalList.get(Name).getShiftName());
                        intent.putExtra("Sf_Code", approvalList.get(Name).getSfCode());
                        intent.putExtra("MissedPunchDate", approvalList.get(Name).getMissedPunchDate());
                        intent.putExtra("CheckinTime", approvalList.get(Name).getCheckinTime());
                        intent.putExtra("CheckoutTime", approvalList.get(Name).getCheckoutTme());

                        Log.e("TAG_TP_RESPONSE", "CHECKIN: " + approvalList.get(Name).getCheckinTime());
                        Log.e("TAG_TP_RESPONSE", "CHCEKOUT: " + approvalList.get(Name).getCheckoutTme());

                       startActivity(intent);
                    }
                }));
                common_class.ProgressdialogShow(2, "Missed Punch Approval");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Missed Punch Approval");
            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Missed_punch_Approval.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}
