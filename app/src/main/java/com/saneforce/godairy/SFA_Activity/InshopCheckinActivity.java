package com.saneforce.godairy.SFA_Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.InshopModel;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.godairy.SFA_Model_Class.TimeUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InshopCheckinActivity extends AppCompatActivity {

    public static final int ACTIVITY_REQUEST_CODE = 2;
    TextView checkinRunTime, checkedinTime, search, retailerName, tvDate;
    Button checkin;
    ImageView back;
    CardView searchLay;
    LinearLayout checkinLay, checkedinLay;
    final Handler handler = new Handler();
    String n="", m="",nR="",cinTime="";
    ArrayList<Retailer_Modal_List> retailerList = new ArrayList<>();
    ArrayList<InshopModel> checkInList=new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;
    String Date= "";
    String time="";
    String SF_code = "", div = "", State_Code = "", date="",retailerCode="",retailerMobile="",retailerRoute="", SFName="";
    private static String name,checkinTime;
    public static String getName() {
        return name;
    }
    public static String getCheckinTime() {
        return checkinTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkin);
        checkinRunTime = findViewById(R.id.tvCheckInRunTime);
        checkin = findViewById(R.id.btnInshopCheckin);
        checkinLay = findViewById(R.id.inshopCheckinLay);
        checkedinLay = findViewById(R.id.inshopCheckedInLay);
        checkedinTime = findViewById(R.id.tvCheckedinTime);
        search = findViewById(R.id.tvInshopSearchRet);
        searchLay = findViewById(R.id.isSearchView);
        retailerName = findViewById(R.id.inshopRetName);
        tvDate = findViewById(R.id.ischeckinDate);
        back=findViewById(R.id.cin_back);
        retailerCode = getIntent().getStringExtra("rcode");
        retailerMobile = getIntent().getStringExtra("rmobile");
        retailerRoute = getIntent().getStringExtra("rroute");

        if (getIntent().hasExtra("idData")) {
            n=getIntent().getStringExtra("idData");
            m=getIntent().getStringExtra("idData");
        }

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = TimeUtils.getCurrentTime(TimeUtils.FORMAT1);
        tvDate.setText(date);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        SFName = UserDetails.getString("SfName","");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");

        search.setText(n);

        getCheckinLay();

        cinTime = TimeUtils.getCurrentTime(TimeUtils.FORMAT);
        handler.postDelayed(new Runnable() {
            public void run() {
                checkinRunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retailerName.setText(n);
                checkedinTime.setText(Common_Class.GetRunTime());
                name = retailerName.getText().toString().trim();
                checkinTime = checkedinTime.getText().toString().trim();


                if (name.isEmpty()){
                    Toast.makeText(InshopCheckinActivity.this,"Choose the Retailer to Check In",Toast.LENGTH_SHORT).show();
                }else{
                    int count=0;
                    for(int i=0;i<checkInList.size();i++) {
                        if (checkInList.size()!=0 &&checkInList.get(i).getCflag()==1) {
                            count++;
                        }
                    }
                    if(count>0){
                        Toast.makeText(getApplicationContext(), "Already CheckIn", Toast.LENGTH_SHORT).show();
                    }else {
                        checkinData();
                    }

                }
            }

//            @Override
//            public void onClick(View view) {
//
//                checkedinTime.setText(Common_Class.GetRunTime());
//                checkedinLay.setVisibility(View.VISIBLE);
//                checkinLay.setVisibility(View.GONE);
//
//                retailerName.setText(m);
//
//                name = retailerName.getText().toString().trim();
//                checkinTime = checkedinTime.getText().toString().trim();
//
//                if (name.isEmpty()){
//                    Toast.makeText(InshopCheckinActivity.this,"Choose the Retailer to Check In",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    checkinData();
//                }
//
//            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InshopCheckinActivity.this, InshopRetailerActivity.class));
                finish();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if(requestCode == ACTIVITY_REQUEST_CODE) {
                try {
                    if (data == null)
                        return;

                    String name = "";
                    if (data.hasExtra("idData"))
                        name = data.getStringExtra("idData");

                    int type = 1;
                    if (data.hasExtra("type"))
                        type = data.getIntExtra("type", 1);

                    Retailer_Modal_List selectionModel = null;
                    if (type == 2 && data.hasExtra("selectionListModel")) {
                        selectionModel = (Retailer_Modal_List) data.getSerializableExtra("selectionListModel");
                        Log.d("OutletCheckInActivity", "onActivityResult: selectionListModel " + selectionModel);

                    }
                    String id = "1";
                    if (data.hasExtra("id"))
                        id = data.getStringExtra("id");

                    int position = 2;
                    if (data.hasExtra("position"))
                        position = data.getIntExtra("position", 2);


                    Log.d("OutletCheckInActivity", "onActivityResult: name " + name + " type " + type + " id " + id + " position " + position + " workTypFlag ");


                    switch (type) {
                        case 2 :
//                            retailerName.setText(name);
//                            distributorId = id;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCheckinLay() {
        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);
//        Log.v("api",request.toString());
        Call<ResponseBody> call = request.getInshopRetailer("get/checkInList",
                div,
                SF_code,
                SF_code,
                Shared_Common_Pref.StateCode,
                date);

        Log.v("api",call.toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String responseBody = response.body().string();
                    Log.v("json_reply",div + SF_code+date);
                    Log.d("CheckInActivity","responseBody"+responseBody);

                    checkedinLay.setVisibility(View.GONE);

                    checkinLay.setVisibility(View.VISIBLE);

                    if (responseBody != null) {
                        checkInList.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody.toString());
                            Log.d("arraycount","jsonArray"+jsonArray.length());

                            for(int i = 0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                InshopModel checkInModel=new InshopModel(jsonObject.getInt("Sl_No"),jsonObject.getString("Sf_Code"),jsonObject.getString("Retailer_Code"),jsonObject.getString("eKey"));

                                String name = jsonObject.getString("Retailer_Name");
                                retailerName.setText(name);


                                if(jsonObject.has("CIn_Time")){
                                    String string= jsonObject.getString("CIn_Time");
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(string);
                                        if(jsonObject1.has("date")){

                                            InshopModel cInTime=new InshopModel(jsonObject1.getString("date"));
                                            checkInModel.setCintime(cInTime.toString());
                                            String  s=jsonObject1.getString("date");

                                            try {
                                                String ss=" ";
                                                time = s.substring(s.indexOf(ss),s.length()).trim();
                                                checkedinTime.setText(time);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                String ss=" ";
                                                Date = s.substring(0,s.indexOf(ss)).trim();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            checkInModel.setCintime(time);
                                            checkInModel.setEntrydate(jsonObject1.getString("date"));

                                        }




                                    }catch (JSONException err){
                                        Log.d("Error", err.toString());
                                    }
                                }

                                if(jsonObject.has("C_Flag")){
                                    checkInModel.setCflag(jsonObject.getInt("C_Flag"));
                                }

                                checkInList.add(checkInModel);
                            }
                            if(checkInList.size()!=0) {
                                for (int k = 0; k < checkInList.size(); k++) {
                                    if (checkInList.get(k).getCflag() == 1) {

                                        checkinLay.setVisibility(View.GONE);
                                        checkedinLay.setVisibility(View.VISIBLE);

                                    }else{
                                        checkinLay.setVisibility(View.VISIBLE);
                                        checkedinLay.setVisibility(View.GONE);
                                    }
                                }
                            }else{
                                checkinLay.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {

                            checkedinLay.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }else{

                        Log.e("error_responsev",response.toString());
                        checkedinLay.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    checkedinLay.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                checkedinLay.setVisibility(View.GONE);
                Log.d("Error", "" + t.getMessage());
                Toast.makeText(getApplicationContext(), "Failure : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }


    private void checkinData() {

        JsonArray jsonArray = new JsonArray();
        JsonObject activityReportAppObject = new JsonObject();
        activityReportAppObject.addProperty("sfCode", SF_code);
        activityReportAppObject.addProperty("sfname", SFName);
        activityReportAppObject.addProperty("stateCode",State_Code);
        activityReportAppObject.addProperty("retailer_code", "'" +retailerCode+ "'");
        activityReportAppObject.addProperty("retailer_name", "'" +name+ "'");
        activityReportAppObject.addProperty("route",retailerRoute);
        activityReportAppObject.addProperty("ekey", "'" +Common_Class.GetEkey()+ "'");
        activityReportAppObject.addProperty("checkin_time", "'" +cinTime+ "'");
        activityReportAppObject.addProperty("entry_date", "'" +date+ "'");
        activityReportAppObject.addProperty("c_flag",1);
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.add("Activity_Check_In_App", activityReportAppObject);
        jsonArray.add(jsonObject1);


        Log.d("isCheckinhjj","ghkj"+activityReportAppObject.toString());


        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = request.inshopSave(div, SF_code,"dcr/save", jsonArray.toString() );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    if (responseBody != null)
                    {
                        try {
                            JSONObject jsonArray = new JSONObject(responseBody);


                            Log.v("json_response",div);
                            if (jsonArray.getBoolean("success")) {
                                checkinLay.setVisibility(View.GONE);
                                checkedinLay.setVisibility(View.VISIBLE);

                                if(jsonArray.has("msg")){
                                    Toast.makeText(InshopCheckinActivity.this, jsonArray.getString("msg"), Toast.LENGTH_LONG).show();
                                }else
                                    Toast.makeText(InshopCheckinActivity.this, "CheckIn Entry Submitted Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                if(jsonArray.has("msg")){
                                    Toast.makeText(InshopCheckinActivity.this, jsonArray.getString("msg"), Toast.LENGTH_LONG).show();
                                }else
                                    Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Exception 1 " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Log.e("error_response", response.toString());
                        Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Exception 2 " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                Log.d("Error", "" + t.getMessage());
                Toast.makeText(getApplicationContext(), "Failure : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void getRetailerName(){
        for (int i=0;i<checkInList.size();i++){
            for (int j=0;j<retailerList.size();j++){
                if((checkInList.get(i).getRetailercode()).equals(retailerList.get(j).getMobileNumber())){
                    nR =retailerList.get(j).getName();

                }
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(InshopCheckinActivity.this, InshopActivity.class));
        finish();
    }
}