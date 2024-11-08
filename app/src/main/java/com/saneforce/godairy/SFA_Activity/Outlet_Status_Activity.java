package com.saneforce.godairy.SFA_Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity_Hap.Dashboard;
import com.saneforce.godairy.Activity_Hap.ERT;
import com.saneforce.godairy.Activity_Hap.Help_Activity;
import com.saneforce.godairy.Activity_Hap.LeaveReasonStatus;
import com.saneforce.godairy.Activity_Hap.PayslipFtp;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LeaveCancelReason;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.Outlet_Status_Adapter;
import com.saneforce.godairy.Status_Model_Class.Leave_Status_Model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Outlet_Status_Activity extends AppCompatActivity {

    List<Leave_Status_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    String AMOD = "0";
    Shared_Common_Pref mShared_common_pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_status);
        mShared_common_pref = new Shared_Common_Pref(this);

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
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });

        recyclerView = findViewById(R.id.rvOutletStatus);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();


        AMOD = String.valueOf(getIntent().getSerializableExtra("AMod"));

        Log.v("AMODE", AMOD);
        getleavestatus();

    }

    public void getleavestatus() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        common_class.ProgressdialogShow(1, "Outlet Status");
        Call<Object> mCall = apiInterface.GetTPObject1(AMOD, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "GetLeave_Status", routemaster);

        Log.e("GetCurrentMonth_Request", (mCall.request().toString()));

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                Log.e("GetCurrentMonth_Values", (response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                common_class.ProgressdialogShow(2, "Outlet Status");
                userType = new TypeToken<ArrayList<Leave_Status_Model>>() {
                }.getType();
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);
                recyclerView.setAdapter(new Outlet_Status_Adapter(approvalList, R.layout.adapter_outlet_status, getApplicationContext(), AMOD, new LeaveCancelReason() {
                    @Override
                    public void onCancelReason(String reason) {

                        Intent intent = new Intent(Outlet_Status_Activity.this, LeaveReasonStatus.class);
                        intent.putExtra("LeaveId", reason);
                        startActivity(intent);

                    }
                }));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Outlet Status");
            }
        });

    }

}