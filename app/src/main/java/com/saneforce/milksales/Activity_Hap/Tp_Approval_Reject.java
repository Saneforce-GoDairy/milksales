package com.saneforce.milksales.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class Tp_Approval_Reject extends AppCompatActivity implements View.OnClickListener {
    TextView name, empcode, hq, mobilenumber, designation, plandate, worktype, route, distributor, textremarks,
            tpapprovebutton, tpreject, tp_rejectsave, captionsremarks, routecaption, distributorcaption, tphqcaption,
            ChillingCentercaption, shifttypecaption, fromdatecaption, todatecaption, tphq, ChillingCenter, shifttype,
            fromdate, todate, jointworkcaption, jointwork,txtMot,txtDaType,txtDa,txtFrom,txtTo;
    String Sf_Code, Tour_plan_Date;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout Approvereject, rejectonly;
    EditText reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__approval__reject);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        name = findViewById(R.id.name);
        tpapprovebutton = findViewById(R.id.tpapprovebutton);
        empcode = findViewById(R.id.empcode);
        reason = findViewById(R.id.reason);
        hq = findViewById(R.id.hq);
        designation = findViewById(R.id.designation);
        mobilenumber = findViewById(R.id.mobilenumber);
        Approvereject = findViewById(R.id.Approvereject);
        rejectonly = findViewById(R.id.rejectonly);
        tp_rejectsave = findViewById(R.id.tp_rejectsave);
        tpreject = findViewById(R.id.tpreject);
        jointworkcaption = findViewById(R.id.jointworkcaption);
        jointwork = findViewById(R.id.jointwork);
        captionsremarks = findViewById(R.id.captionsremarks);
        plandate = findViewById(R.id.plandate);
        worktype = findViewById(R.id.worktype);
        route = findViewById(R.id.route);
        distributor = findViewById(R.id.distributor);
        textremarks = findViewById(R.id.edt_remarks);
        tphq = findViewById(R.id.tphq);
        ChillingCenter = findViewById(R.id.ChillingCenter);
        shifttype = findViewById(R.id.shifttype);
        fromdate = findViewById(R.id.fromdate);
        todate = findViewById(R.id.todate);
        routecaption = findViewById(R.id.routecaption);
        distributorcaption = findViewById(R.id.distributorcaption);
        tphqcaption = findViewById(R.id.tphqcaption);
        ChillingCentercaption = findViewById(R.id.ChillingCentercaption);
        shifttypecaption = findViewById(R.id.shifttypecaption);
        fromdatecaption = findViewById(R.id.fromdatecaption);
        todatecaption = findViewById(R.id.todatecaption);


        txtMot = findViewById(R.id.motravel);
        txtDaType = findViewById(R.id.daype);
        txtDa = findViewById(R.id.DAllowance);
        txtFrom = findViewById(R.id.Fromplace);
        txtTo = findViewById(R.id.Toplace);

        tpapprovebutton.setOnClickListener(this);
        tpreject.setOnClickListener(this);
        tp_rejectsave.setOnClickListener(this);
        Intent i = getIntent();
        name.setText(":" + i.getExtras().getString("Username"));
        empcode.setText(":" + i.getExtras().getString("Emp_Code"));
        designation.setText(":" + i.getExtras().getString("Designation"));
        mobilenumber.setText(":" + i.getExtras().getString("MobileNumber"));
        plandate.setText(":" + i.getExtras().getString("Plan_Date"));
        Tour_plan_Date = i.getExtras().getString("Plan_Date");
        worktype.setText(":" + i.getExtras().getString("Work_Type"));
        route.setText(":" + i.getExtras().getString("Route"));
        distributor.setText(":" + i.getExtras().getString("Distributor"));
        Sf_Code = i.getExtras().getString("Sf_Code");
        textremarks.setText(":" + i.getExtras().getString("Remarks"));
        hq.setText(":" + i.getExtras().getString("HQ"));
        jointwork.setText(":" + i.getExtras().getString("workedwithname"));

        txtMot.setText(":" + i.getExtras().getString("MOT"));
        txtDaType.setText(":" + i.getExtras().getString("DA_Type"));
        txtDa.setText(":" + i.getExtras().getString("Da"));
        txtFrom.setText(":" + i.getExtras().getString("From_Place"));
        txtTo.setText(":" + i.getExtras().getString("To_Place"));




        Log.e("DEP_TYPE", String.valueOf(i.getExtras().getString("DeptType")));
        if (i.getExtras().getString("DeptType").equals("1")) {
            tphq.setText(":" + i.getExtras().getString("TPHqname"));
            shifttype.setText(":" + i.getExtras().getString("ShiftType"));
            ChillingCenter.setText(":" + i.getExtras().getString("ChillCentreName"));
            fromdate.setText(":" + i.getExtras().getString("FromDate"));
            todate.setText(":" + i.getExtras().getString("ToDate"));
            tphqcaption.setVisibility(View.VISIBLE);
            ChillingCentercaption.setVisibility(View.VISIBLE);
            shifttypecaption.setVisibility(View.VISIBLE);
            fromdatecaption.setVisibility(View.VISIBLE);
            todatecaption.setVisibility(View.VISIBLE);
            fromdate.setVisibility(View.VISIBLE);
            shifttype.setVisibility(View.VISIBLE);
            todate.setVisibility(View.VISIBLE);
            tphq.setVisibility(View.VISIBLE);
            ChillingCenter.setVisibility(View.VISIBLE);
            route.setVisibility(View.GONE);
            jointworkcaption.setVisibility(View.GONE);
            jointwork.setVisibility(View.GONE);
            distributor.setVisibility(View.GONE);
            routecaption.setVisibility(View.GONE);
            distributorcaption.setVisibility(View.GONE);
            captionsremarks.setText("Purpose of Visit");

        } else {
            distributor.setVisibility(View.VISIBLE);
            route.setVisibility(View.VISIBLE);
            jointworkcaption.setVisibility(View.VISIBLE);
            jointwork.setVisibility(View.VISIBLE);
            tphqcaption.setVisibility(View.GONE);
            ChillingCentercaption.setVisibility(View.GONE);
            shifttypecaption.setVisibility(View.GONE);
            fromdatecaption.setVisibility(View.GONE);
            todatecaption.setVisibility(View.GONE);
            fromdate.setVisibility(View.GONE);
            todate.setVisibility(View.GONE);
            tphq.setVisibility(View.GONE);
            ChillingCenter.setVisibility(View.GONE);
            shifttype.setVisibility(View.GONE);
            routecaption.setVisibility(View.VISIBLE);
            distributorcaption.setVisibility(View.VISIBLE);
            captionsremarks.setText("Remarks");
            if (i.getExtras().getString("Worktype_Flag").equals("N")) {
                distributor.setVisibility(View.GONE);
                route.setVisibility(View.GONE);
            }
        }



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


    private void SendtpApproval(String Name, int flag) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "dcr/save");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("Start_date", Tour_plan_Date);
        QueryString.put("Mr_Sfcode", Sf_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            sp.put("Sf_Code", Sf_Code);
            if (flag == 2) {
                sp.put("reason", common_class.addquote(reason.getText().toString()));
            }
            jsonObject.put(Name, sp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        Log.e("Log_TpQuerySTring", QueryString.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();

                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                try {
                    common_class.CommonIntentwithFinish(Tp_Approval.class);
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));


                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "TP Approved  Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "TP Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tpapprovebutton:
                SendtpApproval("NTPApproval", 1);
                break;

            case R.id.tpreject:
                rejectonly.setVisibility(View.VISIBLE);
                Approvereject.setVisibility(View.INVISIBLE);
                break;

            case R.id.tp_rejectsave:
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter The Reason", Toast.LENGTH_SHORT).show();
                } else {
                    SendtpApproval("NTPApprovalR", 2);
                }
                break;
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Tp_Approval_Reject.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}


