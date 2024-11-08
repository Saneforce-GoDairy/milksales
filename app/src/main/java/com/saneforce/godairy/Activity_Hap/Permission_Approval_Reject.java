package com.saneforce.godairy.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.godairy.Activity_Hap.Leave_Request.CheckInfo;

public class Permission_Approval_Reject extends Activity implements View.OnClickListener {
    TextView name, empcode, hq, mobilenumber, designation, Preason, fromtime, applieddate, totime, phours;
    String Sf_Code, Tour_plan_Date, Sl_No;
    Button Papprovebutton, Preject, P_rejectsave;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout Approvereject, rejectonly;
    EditText reason;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission__approval__reject);
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
        name = findViewById(R.id.name);
        Papprovebutton = findViewById(R.id.Papprovebutton);
        empcode = findViewById(R.id.empcode);
        reason = findViewById(R.id.reason);
        hq = findViewById(R.id.hq);
        designation = findViewById(R.id.designation);
        mobilenumber = findViewById(R.id.mobilenumber);
        Approvereject = findViewById(R.id.Approvereject);
        rejectonly = findViewById(R.id.rejectonly);
        P_rejectsave = findViewById(R.id.P_rejectsave);
        Preject = findViewById(R.id.Preject);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        Preason = findViewById(R.id.Preason);
        applieddate = findViewById(R.id.applieddate);
        fromtime = findViewById(R.id.fromtime);
        totime = findViewById(R.id.totime);
        phours = findViewById(R.id.phours);
        Papprovebutton.setOnClickListener(this);
        Preject.setOnClickListener(this);
        P_rejectsave.setOnClickListener(this);
        i = getIntent();
        name.setText(i.getExtras().getString("Username"));
        empcode.setText(i.getExtras().getString("Emp_Code"));
        hq.setText(i.getExtras().getString("HQ"));
        designation.setText(i.getExtras().getString("Designation"));
        mobilenumber.setText(i.getExtras().getString("MobileNumber"));
        Preason.setText(i.getExtras().getString("Reason"));
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = inputDateFormat.parse(i.getExtras().getString("permissiondate"));
            applieddate.setText(outputDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        phours.setText(i.getExtras().getString("NoofHours"));
        fromtime.setText(i.getExtras().getString("fromtime"));
        totime.setText(i.getExtras().getString("totime"));
        Sf_Code = i.getExtras().getString("Sf_Code");
        Sl_No = i.getExtras().getString("Sl_No");
        mobilenumber.setOnClickListener(this);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Permission_Approval.class);
            }
        });

    }


    private void SendtpApproval(String Name, int flag) {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "dcr/save");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("Sl_no", Sl_No);
        QueryString.put("Sf_Code", Sf_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            sp.put("Sf_Code", Sf_Code);
            if (flag == 2) {
                common_class.ProgressdialogShow(1, "Rejection for Permission");
                sp.put("reason", common_class.addquote(reason.getText().toString()));
            } else {
                common_class.ProgressdialogShow(1, "Approval for Permission");
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
                    common_class.CommonIntentwithFinish(Permission_Approval.class);
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (flag == 1) {
                        common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Permission  Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Permission Rejected  Successfully", Toast.LENGTH_SHORT).show();

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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Papprovebutton:
                SendtpApproval("PermissionApproval", 1);
                break;

            case R.id.Preject:
                rejectonly.setVisibility(View.VISIBLE);
                Approvereject.setVisibility(View.INVISIBLE);
                break;
            case R.id.P_rejectsave:
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter The Reason", Toast.LENGTH_SHORT).show();
                } else {
                    SendtpApproval("PermissionReject", 2);
                }
                break;
            case R.id.mobilenumber:
                common_class.makeCall(Integer.parseInt(i.getExtras().getString("MobileNumber")));
                break;
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Permission_Approval_Reject.super.onBackPressed();

                }
            });

    @Override
    public void onBackPressed() {

    }
}


