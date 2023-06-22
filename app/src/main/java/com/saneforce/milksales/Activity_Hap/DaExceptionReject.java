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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class DaExceptionReject extends AppCompatActivity implements View.OnClickListener {
    Button Oapprovebutton, ODreject, OD_rejectsave;
    TextView name, empcode, hq, desgination, mobile, appliedDate, Actutalcheckin, EarlyCheckin, ActualLateCheck, LateCheckout, FromType, ToType, amount, textDaType;
    String Sf_Code, Tour_plan_Date, EmpId;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout Approvereject, rejectonly, LinEarly, LinLate, LinMode;
    EditText reason;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_exception_reject);

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
        appliedDate = findViewById(R.id.applieddate);
        Oapprovebutton = findViewById(R.id.Oapprovebutton);
        empcode = findViewById(R.id.empcode);
        reason = findViewById(R.id.reason);
        hq = findViewById(R.id.hq);
        desgination = findViewById(R.id.designation);
        mobile = findViewById(R.id.mobilenumber);
        Actutalcheckin = findViewById(R.id.workinghours);
        EarlyCheckin = findViewById(R.id.shiftdate);
        amount = findViewById(R.id.geocheckin);
        textDaType = findViewById(R.id.daType);
        Approvereject = findViewById(R.id.Approvereject);
        rejectonly = findViewById(R.id.rejectonly);


        ActualLateCheck = findViewById(R.id.lateactualout);
        LateCheckout = findViewById(R.id.latecheckout);
        FromType = findViewById(R.id.from_type);
        ToType = findViewById(R.id.to_type);
        LinEarly = findViewById(R.id.lin_actual);
        LinLate = findViewById(R.id.lin_late);
        LinMode = findViewById(R.id.lin_daType);


        OD_rejectsave = findViewById(R.id.OD_rejectsave);
        ODreject = findViewById(R.id.ODreject);


        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);


        name.setText(String.valueOf(getIntent().getSerializableExtra("Sf_Name")));
        appliedDate.setText(String.valueOf(getIntent().getSerializableExtra("DaDT")));
        textDaType.setText(String.valueOf(getIntent().getSerializableExtra("DA_Type")));
        amount.setText(String.valueOf(getIntent().getSerializableExtra("DA_Amt")));
        mobile.setText(String.valueOf(getIntent().getSerializableExtra("Mobile")));
        hq.setText(String.valueOf(getIntent().getSerializableExtra("HQ")));
        desgination.setText(String.valueOf(getIntent().getSerializableExtra("Designation")));
        empcode.setText(String.valueOf(getIntent().getSerializableExtra("sf_emp_id")));
        Actutalcheckin.setText(String.valueOf(getIntent().getSerializableExtra("Actual_Time")));
        ActualLateCheck.setText(String.valueOf(getIntent().getSerializableExtra("Actual_Time")));
        EarlyCheckin.setText(String.valueOf(getIntent().getSerializableExtra("Early_Late_Time")));
        LateCheckout.setText(String.valueOf(getIntent().getSerializableExtra("Early_Late_Time")));
        FromType.setText(String.valueOf(getIntent().getSerializableExtra("FMOTName")));
        ToType.setText(String.valueOf(getIntent().getSerializableExtra("TMOTName")));
        EmpId = String.valueOf(getIntent().getSerializableExtra("ID"));

        Sf_Code = String.valueOf(getIntent().getSerializableExtra("Sf_Code"));
        if (String.valueOf(getIntent().getSerializableExtra("DA_Type")).equalsIgnoreCase("EARLY CHECK-IN")) {

            LinEarly.setVisibility(View.VISIBLE);
            LinLate.setVisibility(View.GONE);
            LinMode.setVisibility(View.GONE);
        } else if (String.valueOf(getIntent().getSerializableExtra("DA_Type")).equalsIgnoreCase("LATE CHECK-OUT")) {
            LinEarly.setVisibility(View.GONE);
            LinLate.setVisibility(View.VISIBLE);
            LinMode.setVisibility(View.GONE);
        } else {
            LinEarly.setVisibility(View.GONE);
            LinLate.setVisibility(View.GONE);
            LinMode.setVisibility(View.VISIBLE);
        }

        Oapprovebutton.setOnClickListener(this);
        ODreject.setOnClickListener(this);
        OD_rejectsave.setOnClickListener(this);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(DAExcApproval.class);
            }
        });

    }


    private void SendtpApproval(String Name, int flag) {

        JSONObject json = new JSONObject();
        try {

            json.put("ID", EmpId);
            json.put("DaDT", appliedDate.getText().toString());
            json.put("DA_Type", textDaType.getText().toString());
            json.put("Approve_by", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            json.put("Flag", String.valueOf(flag));
            json.put("Sf_Code", Sf_Code);
            json.put("Reject_Reason", reason.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSaves(json.toString());

        Log.v("JSON___ARRAY", mCall.request().toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                try {
                    common_class.CommonIntentwithFinish(DAExcApproval.class);
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (flag == 0) {
                        common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Rejected  Successfully", Toast.LENGTH_SHORT).show();

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
            case R.id.Oapprovebutton:
                SendtpApproval("ondutyApproval", 0);
                break;
            case R.id.ODreject:
                rejectonly.setVisibility(View.VISIBLE);
                Approvereject.setVisibility(View.INVISIBLE);
                break;
            case R.id.OD_rejectsave:
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter The Reason", Toast.LENGTH_SHORT).show();
                } else {
                    SendtpApproval("ondutyApprovalR", 2);
                }
                break;
            case R.id.mobilenumber:
                common_class.makeCall(Integer.parseInt(i.getExtras().getString("MobileNumber")));
                break;
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    DaExceptionReject.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}
