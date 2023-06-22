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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.ShiftListItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;
public class Checkin extends AppCompatActivity {
    private static String Tag = "HAP_Check-In";
    SharedPreferences sharedPreferences;
    SharedPreferences CheckInDetails;
    public static final String spCheckIn = "CheckInDetail";
    public static final String MyPREFERENCES = "MyPrefs";
    private JsonArray ShiftItems = new JsonArray();
    private RecyclerView recyclerView;
    private ShiftListItem mAdapter;
    String ODFlag, onDutyPlcID, onDutyPlcNm, vstPurpose, Check_Flag, onDutyFlag, DutyAlp = "0", DutyType = "",exData="";
    Intent intent;
    public static final String mypreference = "mypref";
    /*  ShiftDuty*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);
        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
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
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
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
        Check_Flag = "CIN";
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("ShiftDuty")) {
            DutyAlp = sharedPreferences.getString("ShiftDuty", "0");
        }
        SharedPreferences CheckInDetails = getSharedPreferences(spCheckIn, MODE_PRIVATE);
        String SFTID = CheckInDetails.getString("Shift_Selected_Id", "");
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        onDutyFlag = "0";
        if (bundle != null) {
            Check_Flag = String.valueOf(bundle.getSerializable("Mode"));
            exData=String.valueOf(bundle.getSerializable("data"));
            ODFlag = String.valueOf(bundle.getSerializable("ODFlag"));
            onDutyPlcID = String.valueOf(bundle.getSerializable("onDutyPlcID"));
            onDutyPlcNm = String.valueOf(bundle.getSerializable("onDutyPlcNm"));
            vstPurpose = String.valueOf(bundle.getSerializable("vstPurpose"));
            DutyType = String.valueOf(bundle.getString("onDuty",""));
            onDutyFlag = String.valueOf(bundle.getSerializable("HolidayFlag"));

            if (onDutyPlcID == "0") {
                SFTID = "0";
                onDutyPlcID = "";
            }
            if (Check_Flag.equals("holidayentry")) {
                DutyType="holiday";
                onDutyFlag = "1";
            }
        }
        if (SFTID != "") {
            Intent takePhoto = new Intent(this, ImageCapture.class);
            takePhoto.putExtra("Mode", Check_Flag);
            takePhoto.putExtra("ShiftId", SFTID);
            takePhoto.putExtra("On_Duty_Flag", onDutyFlag);
            takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
            takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", ""));
            takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", ""));
            takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", ""));
            takePhoto.putExtra("ODFlag", ODFlag);
            takePhoto.putExtra("onDutyPlcID", onDutyPlcID);
            takePhoto.putExtra("onDutyPlcNm", onDutyPlcNm);
            takePhoto.putExtra("vstPurpose", vstPurpose);
            takePhoto.putExtra("data",exData);
            startActivity(takePhoto);
            finish();
            return;
        }
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String Scode = (shared.getString("Sfcode", "null"));
        String Dcode = (shared.getString("Divcode", "null"));
        if (!DutyAlp.equals("0")) {
            Log.v("KARTHIC_DUTY_1","1");
            Intent takePhoto = new Intent(this, ImageCapture.class);
            takePhoto.putExtra("Mode", Check_Flag);
            takePhoto.putExtra("ShiftId", SFTID);
            takePhoto.putExtra("On_Duty_Flag", onDutyFlag);
            takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
            takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", ""));
            takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", ""));
            takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", ""));
            takePhoto.putExtra("ODFlag", ODFlag);
            takePhoto.putExtra("onDutyPlcID", onDutyPlcID);
            takePhoto.putExtra("onDutyPlcNm", onDutyPlcNm);
            takePhoto.putExtra("vstPurpose", vstPurpose);
            takePhoto.putExtra("data",exData);
            startActivity(takePhoto);
            finish();
        } else {
            if (DutyType.equals("cba") || DutyType.equalsIgnoreCase("")|| DutyType.equalsIgnoreCase("holiday")) {
                Log.v("KARTHIC_DUTY_1","2");
                spinnerValue("get/Shift_timing", Dcode, Scode);
            } else {
                Log.v("KARTHIC_DUTY_1","3");
                Intent takePhoto = new Intent(this, ImageCapture.class);
                takePhoto.putExtra("Mode", Check_Flag);
                takePhoto.putExtra("ShiftId", SFTID);
                takePhoto.putExtra("On_Duty_Flag", onDutyFlag);
                takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
                takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", ""));
                takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", ""));
                takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", ""));
                takePhoto.putExtra("ODFlag", ODFlag);
                takePhoto.putExtra("onDutyPlcID", onDutyPlcID);
                takePhoto.putExtra("onDutyPlcNm", onDutyPlcNm);
                takePhoto.putExtra("vstPurpose", vstPurpose);
                takePhoto.putExtra("data",exData);
                startActivity(takePhoto);
                finish();
            }
        }

    }
    public void SetShitItems() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ShiftListItem(ShiftItems, this, Check_Flag, onDutyFlag,exData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
    private void spinnerValue(String a, String dc, String sc) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> shiftCall = apiInterface.getDataArrayList(a, dc, sc);
        shiftCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.e("ShiftTime", String.valueOf(response.body()));
                ShiftItems = response.body();
                SetShitItems();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }
}