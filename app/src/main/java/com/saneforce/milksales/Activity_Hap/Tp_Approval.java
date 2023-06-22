package com.saneforce.milksales.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;

import com.saneforce.milksales.Model_Class.Tp_Approval_FF_Modal;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.New_TP_Approval_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class Tp_Approval extends AppCompatActivity {
    Gson gson;
    Type userType;
    ArrayList<Tp_Approval_FF_Modal> Tp_Approval_Model;

    private RecyclerView recyclerView;
    TextView title;
    Common_Class common_class;
    private Toolbar toolbar;
    Shared_Common_Pref sharedCommonPref;
    public static Tp_Approval tp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__approval);
        recyclerView = findViewById(R.id.tprecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        sharedCommonPref = new Shared_Common_Pref(this);
        gettp_Details();
        tp = this;
        TextView txtHelp = findViewById(R.id.toolbar_help);
        common_class = new Common_Class(this);
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


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

    }

    public void gettp_Details() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Object> mCall = apiInterface.GetPJPApproval(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwtplist", routemaster);
        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                // Log.e("GetTPApprovals", String.valueOf(response.body().toString()));
//                            Tp_Approval.tp.finish();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<Tp_Approval_FF_Modal>>() {
                }.getType();
                Tp_Approval_Model = gson.fromJson(new Gson().toJson(response.body()), userType);

                Set<Tp_Approval_FF_Modal> s = new HashSet<Tp_Approval_FF_Modal>(Tp_Approval_Model);
                Tp_Approval_Model = new ArrayList<Tp_Approval_FF_Modal>();
                Tp_Approval_Model.addAll(s);


                recyclerView.setAdapter(new New_TP_Approval_Adapter(Tp_Approval_Model, R.layout.tpapproval_layout, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int Name) {

                        Intent intent = new Intent(Tp_Approval.this, TP_Approval_Details.class);
                        intent.putExtra("FieldForceName",Tp_Approval_Model.get(Name).getFieldForceName());
                        intent.putExtra("ReportingSF",Tp_Approval_Model.get(Name).getReportingSFCode());
                        intent.putExtra("sfCode",Tp_Approval_Model.get(Name).getSfCode());
                        intent.putExtra("Emp_Code", Tp_Approval_Model.get(Name).getEmpCode());
                        intent.putExtra("Work_Type", Tp_Approval_Model.get(Name).getWorktypeName());
                        intent.putExtra("HQ", Tp_Approval_Model.get(Name).getHQName());
//                        sharedCommonPref.save(Constants.rSFCode,Tp_Approval_Model.get(Name).getReportingSFCode());
//
//                        Log.v("esfcode",Tp_Approval_Model.get(Name).getReportingSFCode());

//                      intent.putExtra("Plan_Date", Tp_Approval_Model.get(Name).getStartDate());
                        /*intent.putExtra("Emp_Code", Tp_Approval_Model.get(Name).getEmpCode());
                        intent.putExtra("HQ", Tp_Approval_Model.get(Name).getHQ());
                        intent.putExtra("Designation", Tp_Approval_Model.get(Name).getDesignation());
                        intent.putExtra("MobileNumber", Tp_Approval_Model.get(Name).getSFMobile());
                        intent.putExtra("Plan_Date", Tp_Approval_Model.get(Name).getStartDate());

                        intent.putExtra("Route", Tp_Approval_Model.get(Name).getRouteName());
                        intent.putExtra("Distributor", Tp_Approval_Model.get(Name).getWorkedWithName());
                        intent.putExtra("Sf_Code", Tp_Approval_Model.get(Name).getSFCode());
                        intent.putExtra("Remarks", Tp_Approval_Model.get(Name).getRemarks());
                        intent.putExtra("workedwithname", Tp_Approval_Model.get(Name).getJointWorkName());
                        intent.putExtra("TPHqname", Tp_Approval_Model.get(Name).getTourHQName());
                        intent.putExtra("ShiftType", Tp_Approval_Model.get(Name).getTypename());
                        intent.putExtra("ChillCentreName", Tp_Approval_Model.get(Name).getCCentreName());
                        intent.putExtra("FromDate", Tp_Approval_Model.get(Name).getFromdate());
                        intent.putExtra("Worktype_Flag", Tp_Approval_Model.get(Name).getWorktypeFlag());
                        intent.putExtra("ToDate", Tp_Approval_Model.get(Name).getTodate());
                        intent.putExtra("DeptType", Tp_Approval_Model.get(Name).getDeptType());
                        intent.putExtra("MOT", Tp_Approval_Model.get(Name).getMOT());
                        intent.putExtra("DA_Type", Tp_Approval_Model.get(Name).getDA_Type());
                        intent.putExtra("Da", Tp_Approval_Model.get(Name).getDriver_Allow());
                        intent.putExtra("From_Place", Tp_Approval_Model.get(Name).getFrom_Place());
                        intent.putExtra("To_Place", Tp_Approval_Model.get(Name).getTo_Place());*/


                        startActivity(intent);

                        //startActivity(new Intent(this, Tp_Month_Select.class));

                    }
                }));

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(Tp_Approval.this
                        ,"response failed",Toast.LENGTH_SHORT).show();
            }


        });

    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Tp_Approval.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}