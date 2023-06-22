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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class Extended_Approval_Reject extends AppCompatActivity implements View.OnClickListener {
    TextView name, applieddate, empcode, hq, mobilenumber, designation, workinghours, shiftdate, geocheckin, geocheckout, checkin, checkout;
    String Sf_Code, Sl_No;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout Approvereject, rejectonly;
    EditText reason;
    Intent i;
    Button Oapprovebutton, ODreject, OD_rejectsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended__approval__reject);

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
        applieddate = findViewById(R.id.applieddate);
        Oapprovebutton = findViewById(R.id.Oapprovebutton);
        empcode = findViewById(R.id.empcode);
        reason = findViewById(R.id.reason);
        hq = findViewById(R.id.hq);
        designation = findViewById(R.id.designation);
        mobilenumber = findViewById(R.id.mobilenumber);
        Approvereject = findViewById(R.id.Approvereject);
        rejectonly = findViewById(R.id.rejectonly);
        OD_rejectsave = findViewById(R.id.OD_rejectsave);
        ODreject = findViewById(R.id.ODreject);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        workinghours = findViewById(R.id.workinghours);
        shiftdate = findViewById(R.id.shiftdate);

        geocheckin = findViewById(R.id.geocheckin);
        geocheckout = findViewById(R.id.geocheckout);
        Oapprovebutton.setOnClickListener(this);
        ODreject.setOnClickListener(this);
        OD_rejectsave.setOnClickListener(this);
        mobilenumber.setOnClickListener(this);
        geocheckin.setOnClickListener(this);
        geocheckout.setOnClickListener(this);
        i = getIntent();
        Log.e("MOBILE_NUMBER", i.getExtras().getString("MobileNumber"));
        name.setText("" + i.getExtras().getString("Username"));
        empcode.setText("" + i.getExtras().getString("Emp_Code"));
        hq.setText(" " + i.getExtras().getString("HQ"));
        designation.setText(" " + i.getExtras().getString("Designation"));
        mobilenumber.setText("" + i.getExtras().getString("MobileNumber"));
        workinghours.setText(" " + i.getExtras().getString("workinghours"));
        applieddate.setText("" + i.getExtras().getString("Applieddate"));
        shiftdate.setText(" " + i.getExtras().getString("shiftdate"));
        geocheckin.setText("" + i.getExtras().getString("geoin"));
        geocheckout.setText(" " + i.getExtras().getString("geoout"));
        Sl_No = i.getExtras().getString("Sl_No");

        mobilenumber.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Extendedshift_approval.class);
            }
        });


    }


    private void SendtpApproval(String Name, int flag) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "dcr/save");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("Extend_Id", Sl_No);
        QueryString.put("Numbers", "1");

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            sp.put("Sf_Code", Sf_Code);
            sp.put("Total_number", i.getExtras().getString("workinghours"));
            if (flag == 2) {
                common_class.ProgressdialogShow(1, "Rejection for Extended");
                sp.put("reason", common_class.addquote(reason.getText().toString()));
            } else {
                common_class.ProgressdialogShow(1, "Approval for  Extended");
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
                        Toast.makeText(getApplicationContext(), "Extended  Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Extended Rejected  Successfully", Toast.LENGTH_SHORT).show();

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
                SendtpApproval("ExtendedApproval", 1);
                break;
            case R.id.ODreject:
                rejectonly.setVisibility(View.VISIBLE);
                Approvereject.setVisibility(View.INVISIBLE);
                break;
            case R.id.OD_rejectsave:
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter The Reason", Toast.LENGTH_SHORT).show();
                } else {
                    SendtpApproval("ExtendedApprovalR", 2);
                }
                break;
            case R.id.mobilenumber:
                common_class.makeCall(Integer.parseInt(i.getExtras().getString("MobileNumber")));
                break;
            case R.id.geocheckin:
                Log.e("GEoCHECKIN", i.getExtras().getString("geoin"));
                common_class.CommonIntentwithoutFinishputextra(Webview_Activity.class, "Locations", i.getExtras().getString("geoin"));
                break;
            case R.id.geocheckout:
                Log.e("GEoCHECKIN", i.getExtras().getString("geoout"));
                common_class.CommonIntentwithoutFinishputextra(Webview_Activity.class, "Locations", i.getExtras().getString("geoout"));
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
                    Extended_Approval_Reject.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}



