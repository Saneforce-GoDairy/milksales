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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity.TAClaimActivity;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.ViewTAStatusAdapter;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTAStatus extends AppCompatActivity {

    RecyclerView mTaStatusList;
    ViewTAStatusAdapter viewTAStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_t_a_status);
        mTaStatusList = findViewById(R.id.ta_list);
        mTaStatusList.setLayoutManager(new LinearLayoutManager(this));


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

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TaListResponse();
    }

    @Override
    public void onBackPressed() {
    }

    public void TaListResponse() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> mTAStatus = apiInterface.getTaViewStatus(Shared_Common_Pref.Sf_Code);

        Log.v("TA_STATUS_REQUEST", mTAStatus.request().toString());
        mTAStatus.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonTa = response.body();
                Log.v("JSON_VIEW_Array", jsonTa.toString());

                viewTAStatusAdapter = new ViewTAStatusAdapter(ViewTAStatus.this, jsonTa, new AdapterOnClick() {
                    @Override
                    public void onIntentClick(JsonObject item, int Name) {

                        cancelPendingApproval(item);
                    }
                });
                mTaStatusList.setAdapter(viewTAStatusAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }

    private void cancelPendingApproval(JsonObject obj) {
        try {
            JSONObject jParam = new JSONObject();

            jParam.put("Sf_code", obj.get("Sf_code").getAsString());
            jParam.put("EDT", obj.get("EDT").getAsString());
            jParam.put("Approval_Flag", obj.get("Approval_Flag").getAsString());
            jParam.put("Expdt", obj.get("Expdt").getAsString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> responseBodyCall = apiInterface.submit("cancel/tapending", jParam.toString());
            responseBodyCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {

                            Log.v("ViewTAStatus:cancel:", response.body().toString());
                            JSONObject jsonObjects = new JSONObject(response.body().toString());

                            if (jsonObjects.getBoolean("success")) {
                                startActivity(new Intent(ViewTAStatus.this, TAClaimActivity.class));
                                // finish();
                                //common_class.getDb_310Data(Constants.POP_ENTRY_STATUS, POPActivity.popActivity);
                            }
                            Toast.makeText(ViewTAStatus.this, jsonObjects.getString("Msg"), Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("SUBMIT_VALUE", "ERROR:" + t.getMessage());
                }
            });
        } catch (Exception e) {

        }
    }
}