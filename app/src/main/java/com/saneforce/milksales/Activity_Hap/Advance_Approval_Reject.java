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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Advance_Approval_Reject extends AppCompatActivity implements View.OnClickListener {
    TextView name, empcode, hq, mobilenumber, designation, AdvLoc, leavereason, leavetype,
            fromdate, todate, AdvAmt, tpapprovebutton, Lreject, L_rejectsave;
    String Sf_Code, Tour_plan_Date, LeaveId;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    LinearLayout Approvereject, rejectonly;
    EditText reason;
    private WebView wv1;
    Intent i;

    SharedPreferences UserDetails;
    String UserInfo = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance__approval__reject);

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
        name = findViewById(R.id.name);
        tpapprovebutton = findViewById(R.id.Lapprovebutton);
        empcode = findViewById(R.id.empcode);
        reason = findViewById(R.id.reason);
        hq = findViewById(R.id.hq);

        designation = findViewById(R.id.designation);
        mobilenumber = findViewById(R.id.mobilenumber);
        Approvereject = findViewById(R.id.Approvereject);
        rejectonly = findViewById(R.id.rejectonly);
        L_rejectsave = findViewById(R.id.L_rejectsave);
        Lreject = findViewById(R.id.Lreject);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        leavereason = findViewById(R.id.leavereason);
        leavetype = findViewById(R.id.leavetype);
        AdvLoc = findViewById(R.id.AdvLoc);
        fromdate = findViewById(R.id.fromdate);
        todate = findViewById(R.id.todate);
        AdvAmt = findViewById(R.id.AdvAmt);
        tpapprovebutton.setOnClickListener(this);
        Lreject.setOnClickListener(this);
        L_rejectsave.setOnClickListener(this);
        i = getIntent();

        name.setText(":" + i.getExtras().getString("name"));
        empcode.setText(":" + i.getExtras().getString("Emp_Code"));
        hq.setText(":" + i.getExtras().getString("HQ"));
        designation.setText(":" + i.getExtras().getString("Designation"));
        mobilenumber.setText(":" + i.getExtras().getString("MobileNumber"));

        fromdate.setText(":" + i.getExtras().getString("fromdate"));
        todate.setText(":" + i.getExtras().getString("todate"));
        leavetype.setText(":"+i.getExtras().getString("Type"));
        AdvLoc.setText(":" + i.getExtras().getString("Loc"));
        leavereason.setText(":" + i.getExtras().getString("Purpose"));
        AdvAmt.setText(":" + i.getExtras().getString("Amount"));
        Sf_Code = i.getExtras().getString("Sf_Code");
        LeaveId = i.getExtras().getString("eID");
        ImageView backView = findViewById(R.id.imag_back);
        mobilenumber.setOnClickListener(this);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Advance_Approval.class);
            }
        });


    }


    private void SaveAdvApproval(String Name, int flag) {
        JSONObject sp = new JSONObject();
        try {
            sp.put("Sf_Code", Sf_Code);
            sp.put("rSF", UserDetails.getString("Sfcode", ""));
            sp.put("eid", LeaveId);
            sp.put("flag", flag);
            if (flag == 2) {
                sp.put("reason", reason.getText().toString());
            }else{
                sp.put("reason","");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getDataArrayList("approve/advance", sp.toString())
        .enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {

                    JSONArray jsonObject = new JSONArray(response.body().toString());
                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "Advance Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Advance Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }
                    common_class.CommonIntentwithFinish(Advance_Approval.class);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Lapprovebutton:
                SaveAdvApproval("AdvanceApproval", 1);
                break;

            case R.id.Lreject:
                rejectonly.setVisibility(View.VISIBLE);
                Approvereject.setVisibility(View.INVISIBLE);
                break;
            case R.id.L_rejectsave:
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter The Reason", Toast.LENGTH_SHORT).show();
                } else {
                    SaveAdvApproval("AdvanceReject", 2);
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
                    Advance_Approval_Reject.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

}


