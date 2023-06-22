package com.saneforce.milksales.Status_Activity;

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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Adapter.ViewAll_Status_Adapter;
import com.saneforce.milksales.Status_Model_Class.View_All_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class View_All_Status_Activity extends AppCompatActivity {
    List<View_All_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    Intent i;
    String AMOD = "0", mMode = "0", mStatus = "";


    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    Shared_Common_Pref shared_common_pref;

    JSONArray arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__all__status_);
        shared_common_pref = new Shared_Common_Pref(this);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        TextView txtHelp = findViewById(R.id.toolbar_help);
        TextView tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText("" + getIntent().getStringExtra("name"));
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

        Bundle params = getIntent().getExtras();
        try {
            mMode = params.getString("Priod", "0");
            mStatus = params.getString("Status", "");

        } catch (Exception e) {

        }

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
                    Intent Dashboard = new Intent(View_All_Status_Activity.this, Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard_Two.class));


            }
        });
        recyclerView = findViewById(R.id.viewallstatus);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gson = new Gson();

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        getOnDutyStatus();
        getWeekOffStatus();

    }

    private void getOnDutyStatus() {
        JSONObject taReq = new JSONObject();

        try {
            taReq.put("login_sfCode", UserDetails.getString("Sfcode", ""));


            Log.v("TA_REQ", taReq.toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> mCall = apiInterface.getOnDutyStatus(taReq.toString());

            mCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // locationList=response.body();
                    try {
                        Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                        JSONObject obj = new JSONObject(new Gson().toJson(response.body()));
                        if (obj.getBoolean("success")) {
                            arr = obj.getJSONArray("Data");
                        }

                    } catch (Exception e) {
                        Log.v("TA_REQ1", e.getMessage());

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.v("TA_REQ2", t.getMessage());

                }
            });

        } catch (Exception e) {
            Log.v("TA_REQ3", e.getMessage());
        }
    }

    private void getWeekOffStatus() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        common_class.ProgressdialogShow(1, "Status");
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/AttnStatus");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("Status", mStatus);
        QueryString.put("Priod", mMode);
        String commonworktype = "[]";


        Call<Object> mCall = apiInterface.Getwe_Status(mMode, Shared_Common_Pref.Sf_Code, "get/AttnStatus", mStatus, "[]");

        Log.v("get/AttnStatus", mCall.request().toString());
        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    common_class.ProgressdialogShow(2, "Onduty Status");
                    userType = new TypeToken<ArrayList<View_All_Model>>() {
                    }.getType();
                    approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);

                    try {
                        for (int i = 0; i < arr.length(); i++) {
                            for (int v = 0; v < approvalList.size(); v++) {

                                if (arr.getJSONObject(i).getString("dt").equalsIgnoreCase(approvalList.get(v).getAttndt())) {
                                    approvalList.get(v).setFlag(arr.getJSONObject(i).getInt("flag"));
                                    break;

                                }
                            }

                        }
                    } catch (Exception e) {

                    }

                    recyclerView.setAdapter(new ViewAll_Status_Adapter(approvalList, R.layout.view_all_status_listitem, View_All_Status_Activity.this, AMOD));
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                common_class.ProgressdialogShow(2, "Permission Status");
            }
        });


    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    View_All_Status_Activity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}