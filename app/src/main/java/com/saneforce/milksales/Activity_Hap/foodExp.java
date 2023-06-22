package com.saneforce.milksales.Activity_Hap;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.adFoodexp;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class foodExp extends AppCompatActivity implements View.OnClickListener {
    public static final String UserDetail = "MyPrefs";
    SharedPreferences UserDetails;
    RecyclerView mRecyclerView;
    TextView txtempid, txtempName, txtHQ, txtTot, tvStartDate, tvEndDate;
    ImageView btMyQR, btHome;
    adFoodexp lsExp;
    Common_Class common_class;
    DatePickerDialog fromDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_exp);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class = new Common_Class();

        txtempid = findViewById(R.id.empId);
        txtempName = findViewById(R.id.empName);
        txtHQ = findViewById(R.id.empHQ);
        txtTot = findViewById(R.id.TotAmt);

        btMyQR = findViewById(R.id.myQR);
        btHome = findViewById(R.id.toolbar_home);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);


        txtempid.setText(UserDetails.getString("EmpId", ""));
        txtempName.setText(UserDetails.getString("SfName", ""));
        txtHQ.setText(UserDetails.getString("SFHQ", ""));
        mRecyclerView = findViewById(R.id.foodExpList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        btHome.setOnClickListener(this);

        btMyQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(foodExp.this, CateenToken.class);
                startActivity(intent);
                finish();
            }
        });

        tvStartDate.setText(Common_Class.GetDatewothouttime());
        tvEndDate.setText(Common_Class.GetDatewothouttime());
        //  "{"SF":"MGR0013","fdt":"2022-04-01","tdt":"2022-04-30"}"

        getData();
    }


    void getData() {
        try {
            JSONObject data = new JSONObject();
            data.put("SF", UserDetails.getString("Sfcode", ""));
            data.put("fdt", tvStartDate.getText().toString());
            data.put("tdt", tvEndDate.getText().toString());


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> rptCall = apiInterface.getDataArrayListA("get/foodexp",
                    UserDetails.getString("Divcode", ""),
                    UserDetails.getString("Sfcode", ""), "", "", data.toString());
            rptCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JsonArray res = response.body();
                        Log.d("Res Data:", res.toString());
                        if (res.size() < 1) {
                            Toast.makeText(getApplicationContext(), "No Records Today", Toast.LENGTH_LONG).show();
                            //return;
                        }

                        lsExp = new adFoodexp(res, foodExp.this);
                        mRecyclerView.setAdapter(lsExp);
                        Double amt = 0.0;
                        for (int il = 0; il < res.size(); il++) {
                            JsonObject item = res.get(il).getAsJsonObject();
                            amt += item.get("amount").getAsDouble();
                        }

                        txtTot.setText("Rs. " + new DecimalFormat("##0.00").format(amt));
                    } catch (Exception e) {
                        Log.d("Res:Ex:", e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.d("FoodExp", "Error:" + t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {

        }
    }

    void selectDate(int val) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(foodExp.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;


                String date = ("" + year + "-" + (month < 10 ? "0" : "") + month + "-" + ((dayOfMonth) < 10 ? "0" : "") + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), foodExp.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        //  stDate = tvStartDate.getText().toString();
                        // common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, foodExp.this, false);
                        getData();
                    } else
                        common_class.showMsg(foodExp.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, foodExp.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        getData();
                        //endDate = tvEndDate.getText().toString();
                        //  common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);

                    } else
                        common_class.showMsg(foodExp.this, "Please select valid date");

                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                selectDate(1);
                break;
            case R.id.tvEndDate:
                selectDate(2);
                break;
            case R.id.toolbar_home:
                common_class.openHome(foodExp.this);
                break;
        }

    }
}