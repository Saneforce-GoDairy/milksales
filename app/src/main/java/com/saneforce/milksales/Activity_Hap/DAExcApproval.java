package com.saneforce.milksales.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.DaExceptionAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class DAExcApproval extends AppCompatActivity {
    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref mShared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_a_exc_approval);
        mShared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        getTAList();
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
        recyclerView = findViewById(R.id.leaverecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Approvals.class);

            }
        });
    }

    public void getTAList() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> mTrave = apiInterface.getDaException(mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));

        Log.v("APPROVAL_LIST_Request", mTrave.request().toString());
        mTrave.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();

                Log.v("APPROVAL_LIST", jsonArray.toString());
                recyclerView.setAdapter(new DaExceptionAdapter(jsonArray, R.layout.holiday_approval_listitem, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int Name) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(Name);
                        Intent intent = new Intent(getApplicationContext(), DaExceptionReject.class);
                        intent.putExtra("Sf_Name", jsonObject.get("Name").getAsString());
                        intent.putExtra("Sf_Code", jsonObject.get("Sf_Code").getAsString());
                        intent.putExtra("ID", jsonObject.get("ID").getAsString());
                        intent.putExtra("DA_Type", jsonObject.get("DA_Type").getAsString());
                        intent.putExtra("DA_Amt", jsonObject.get("DA_Amt").getAsString());
                        intent.putExtra("DaDT", jsonObject.get("DaDT").getAsString());
                        intent.putExtra("Applied_Date", jsonObject.get("Applied_Date").getAsString());
                        intent.putExtra("Mobile", jsonObject.get("Mobile").getAsString());
                        intent.putExtra("HQ", jsonObject.get("HQ").getAsString());
                        intent.putExtra("Designation", jsonObject.get("Designation").getAsString());
                        intent.putExtra("Actual_Time", jsonObject.get("Actual_Time").getAsString());
                        intent.putExtra("Early_Late_Time", jsonObject.get("Early_Late_Time").getAsString());
                        intent.putExtra("sf_emp_id", jsonObject.get("sf_emp_id").getAsString());
                        intent.putExtra("FMOTName", jsonObject.get("FMOTName").getAsString());
                        intent.putExtra("TMOTName", jsonObject.get("TMOTName").getAsString());
                        startActivity(intent);

                    }
                }));
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

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