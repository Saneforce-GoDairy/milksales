package com.saneforce.godairy.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.godairy.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POS_SalesEntryActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI{

    List<Product_Details_Modal> list=new ArrayList<>();

    RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    public static final String MyPREFERENCES = "MyPrefs";
    Type userType;
    Gson gson;
    DatabaseHandler db;
    final Handler handler = new Handler();
    SharedPreferences UserDetails;
    Button btnSubmit;
    ApiInterface apiInterface;

    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();


    private DatePickerDialog fromDatePickerDialog;

    public static TextView tvStartDate, tvEndDate, currDate, totalExpense, tvView;
    public static String stDate = "", endDate = "", entryDate="";
    String date = "", SF_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_sales_entry);

        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(POS_SalesEntryActivity.this);
        common_class = new Common_Class(this);

        tvView = findViewById(R.id.tvView);

        tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(POS_SalesEntryActivity.this, POSViewEntryActivity.class));

            }
        });


        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");

        ImageView ivToolbarHome = findViewById(R.id.posentry_toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);


        currDate=findViewById(R.id.tvDate);
        currDate.setText(""+DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));
        entryDate= DT.getDateWithFormat(new Date(), "dd-MMM-yyyy HH:mm:ss");

        totalExpense = findViewById(R.id.totalExpense);
        btnSubmit = findViewById(R.id.btn_posentry_submit);
        recyclerView = findViewById(R.id.pos_entrysalesRecyclerview);

        tvStartDate = findViewById(R.id.tvPosEntryStartDate);
        tvEndDate = findViewById(R.id.tvPosEntryEndDate);

        stDate = Common_Class.GetDatewothouttime();
        endDate = Common_Class.GetDatewothouttime();
        tvStartDate.setText(stDate);
        tvEndDate.setText(endDate);

        common_class.getDb_310Data(Constants.POS_Category_EntryList, this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void submitData() {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("eKey",common_class.GetEkey());
            jObj.put("SFCode",SF_code);
            jObj.put("currentDate",entryDate);
            jObj.put("fromDate",stDate);
            jObj.put("toDate",endDate);
            jObj.put("totalExpense", totalExpense.getText().toString());

            JSONArray jArr=new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj1 = new JSONObject();
                obj1.put("productID", list.get(i).getId());
                obj1.put("productName",list.get(i).getName());
                obj1.put("productValue",list.get(i).getentryValue());
                jArr.put(obj1);
            }
            jObj.accumulate("POSEntryData" , jArr);

            Log.d("savehjj","ghkj"+jObj.toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<JsonObject> responseBodyCall =apiInterface.posCounterEntrySave(Shared_Common_Pref.Div_Code, jObj.toString());
            //Log.v("divcodepos",Shared_Common_Pref.Div_Code);
            responseBodyCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.e("JSON_VALUES", response.body().toString());
                            Toast.makeText(POS_SalesEntryActivity.this, "Counter sales entry submitted Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e) {
                            Log.v("error", e.toString());
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.v("errormsg", t.toString());
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.POS_Category_EntryList:
                    Log.v("posCategoryEntryList", apiDataResponse);

                    GetJsonData(apiDataResponse);

                    break;

            }
        } catch (Exception e) {

        }
    }

    private void GetJsonData(String jsonResponse) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String name = jsonObject1.optString("name");
//                    String value = jsonObject1.optString("pvalue");
                String id = jsonObject1.optString("id");

                Log.v("jsonarray123",jsonArray.toString());

                list.add(new Product_Details_Modal(id,name ));
            }

                PosEntrySalesAdapter customAdapteravail = new PosEntrySalesAdapter(getApplicationContext(), list);
                recyclerView.setAdapter(customAdapteravail);
                customAdapteravail.notifyDataSetChanged();

        } catch (Exception e) {
        }
    }



    void showDatePickerDialog(int val) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(POS_SalesEntryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), POS_SalesEntryActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();

                        Log.v("sdatefd",stDate);
                    } else
                        common_class.showMsg(POS_SalesEntryActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, POS_SalesEntryActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        Log.v("sdatefd",endDate);


                    } else
                        common_class.showMsg(POS_SalesEntryActivity.this, "Please select valid date");

                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPosEntryStartDate:
                showDatePickerDialog(1);
                break;
            case R.id.tvPosEntryEndDate:
                showDatePickerDialog(2);
                break;

        }
    }


}