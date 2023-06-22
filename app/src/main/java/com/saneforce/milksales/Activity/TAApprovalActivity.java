package com.saneforce.milksales.Activity;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity_Hap.Dashboard;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Adapter.Travel_Approval_Adapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TAApprovalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref mShared_common_pref;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_approval);
        mShared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        getTAList();
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        tvName=findViewById(R.id.tvName);
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
        recyclerView = findViewById(R.id.leaverecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               // common_class.CommonIntentwithFinish(Approvals.class);

            }
        });

        tvName.setText(getIntent().getStringExtra("name"));
    }

    public void getTAList() {
        try {

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> mTrave = apiInterface.getApprovalList(mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            mTrave.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    JsonArray jsonArray = response.body();

                    JsonArray filterArr = new JsonArray();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(i);

                        if (getIntent().getStringExtra("view_id")!=null&&getIntent().getStringExtra("view_id").equalsIgnoreCase(jsonObject.get("Sf_code").getAsString())) {
                            filterArr.add(jsonObject);
                        }
                    }

                    Log.v("APPROVAL_LIST", jsonArray.toString());
                    recyclerView.setAdapter(new Travel_Approval_Adapter(filterArr, R.layout.leave_approval_layout, getApplicationContext(), new AdapterOnClick() {
                        @Override
                        public void onIntentClick(int Name) {
                            JsonObject jsonObject = (JsonObject) filterArr.get(Name);
                            Intent intent = new Intent(getApplicationContext(), TAViewStatus.class);
                            intent.putExtra("TA_Date", jsonObject.get("id").getAsString());
                            intent.putExtra("name", jsonObject.get("Sf_Name").getAsString());
                            intent.putExtra("total_amount", jsonObject.get("Total_Amount").getAsString());
                            intent.putExtra("head_quaters", jsonObject.get("HQ").getAsString());
                            intent.putExtra("travel_mode", jsonObject.get("MOT_Name").getAsString());
                            intent.putExtra("desig", jsonObject.get("sf_Designation_Short_Name").getAsString());
                            intent.putExtra("dept", jsonObject.get("DeptName").getAsString());
                            intent.putExtra("Sl_No", jsonObject.get("Sl_No").getAsString());
                            intent.putExtra("sfCode", jsonObject.get("Sf_code").getAsString());
                            intent.putExtra("SF_Mobile", jsonObject.get("SF_Mobile").getAsString());
                            intent.putExtra("sf_emp_id", jsonObject.get("sf_emp_id").getAsString());
                            intent.putExtra("TA_APPROVAL", "1");
                            startActivity(intent);

                        }
                    }));
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Log.e("TAApproval: ", e.getMessage());
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTAList();
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }
}