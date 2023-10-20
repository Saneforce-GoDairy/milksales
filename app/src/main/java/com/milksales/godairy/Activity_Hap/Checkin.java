package com.milksales.godairy.Activity_Hap;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.JsonArray;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.adapters.ShiftTimeAdapter;
import com.milksales.godairy.databinding.ActivityCheckinBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.milksales.godairy.Activity_Hap.Leave_Request.CheckInfo;

public class Checkin extends AppCompatActivity implements ShiftTimeAdapter.OnClickInterface{
    private ActivityCheckinBinding binding;
    private Context context = this;
    private static String Tag = "HAP_Check-In";
    private SharedPreferences sharedPreferences;
    private SharedPreferences CheckInDetails;
    public static final String spCheckIn = "CheckInDetail";
    public static final String MyPREFERENCES = "MyPrefs";
   // private JsonArray ShiftItems = new JsonArray();
   //  private RecyclerView recyclerView;
   // private ShiftListItem mAdapter;
    private String ODFlag, onDutyPlcID, onDutyPlcNm, vstPurpose, Check_Flag, onDutyFlag, DutyAlp = "0", DutyType = "",exData="";
    private Intent intent;
    public static final String mypreference = "mypref";
    private String mode, shiftId, shiftName, onDutyFlag1, shiftStart, shiftEnd, shiftCutOff, data;
    private JsonArray ShiftItems = new JsonArray();
    /*  ShiftDuty*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckinBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        onClick();

        /*  Previous design code

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(binding.toolBar.toolbarErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
         */

        binding.toolBar.toolbarErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        binding.toolBar.toolbarPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
            }
        });
        binding.toolBar.toolbarHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        binding.toolBar.toolbarHome.setOnClickListener(new View.OnClickListener() {
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
            Intent takePhoto = new Intent(this, CameraxActivity.class);
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
            Intent takePhoto = new Intent(this, CameraxActivity.class);
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
                Intent takePhoto = new Intent(this, CameraxActivity.class);
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

    private void onClick() {
        binding.confirmShift.setOnClickListener(v -> {
            Log.e("shift_adapter", "Mode : " + mode + "\n"
                    + "Shift Id : " + shiftId + "\n"
                    + "Shift Name : " + shiftName + "\n"
                    + "onDutyFlag : " + onDutyFlag1 + "\n"
                    + "shiftStart : " + shiftStart + "\n"
                    + "ShiftEnd : " + shiftEnd + "\n"
                    + "ShiftCutOff : " + shiftCutOff + "\n"
                    + "data : " + data + "\n");

            Intent intent = new Intent(context, CameraxActivity.class);
            intent.putExtra("Mode", mode);
            intent.putExtra("ShiftId", shiftId);
            intent.putExtra("ShiftName", shiftName);
            intent.putExtra("On_Duty_Flag", onDutyFlag1);
            intent.putExtra("ShiftStart", shiftStart);
            intent.putExtra("ShiftEnd", shiftEnd);
            intent.putExtra("ShiftCutOff", shiftCutOff);
            intent.putExtra("data",data);
            startActivity(intent);
        });

        binding.back.setOnClickListener(v -> {
            finish();
        });
    }

    public void SetShitItems() {

        ShiftTimeAdapter mAdapter = new ShiftTimeAdapter(ShiftItems, context, Check_Flag, onDutyFlag, exData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAdapter);

        // Previous design code
        /*
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ShiftListItem(ShiftItems, this, Check_Flag, onDutyFlag,exData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
         */
    }

    private void spinnerValue(String a, String dc, String sc) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> shiftCall = apiInterface.getDataArrayList(a, dc, sc);
        shiftCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                Log.e("ShiftTime", String.valueOf(response.body()));
                ShiftItems = response.body();
                SetShitItems();
            }
            @Override
            public void onFailure(@NonNull Call<JsonArray> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    @Override
    public void onClickInterface(Intent intent) {
        mode = intent.getStringExtra("Mode");
        shiftId = intent.getStringExtra("ShiftId");
        shiftName = intent.getStringExtra("ShiftName");
        onDutyFlag1 = intent.getStringExtra("On_Duty_Flag");
        shiftStart = intent.getStringExtra("ShiftStart");
        shiftEnd = intent.getStringExtra("ShiftEnd");
        shiftCutOff = intent.getStringExtra("ShiftCutOff");
        data = intent.getStringExtra("data");
        binding.confirmShift.setVisibility(View.VISIBLE);
    }

        // Previous design code
        /*
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
         */
}