package com.saneforce.godairy.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.InshopViewAdapter;
import com.saneforce.godairy.SFA_Model_Class.InshopModel;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.godairy.SFA_Model_Class.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InshopViewActivity extends AppCompatActivity {

    TextView currDate, checkinTime, rName, no_data;
    LinearLayout ll_head;
    RecyclerView rv_item;
    ImageView back;
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    ApiInterface apiInterface;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;
    int Sl_no = 0;
    String SF_code = "", div = "", State_Code = "", date = "", UserInfo = "MyPrefs", imageSet = "", imageServer = "", imageConvert = "",
            retailerName = "", SFName = "", coutTime = "";
    String Date = "";
    String time = "";
    String time1 = "", time2 = "";
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    ArrayList<Retailer_Modal_List> retailerList = new ArrayList<>();
    ArrayList<InshopModel> checkInList = new ArrayList<>();
    InshopViewAdapter outletReportAdapter;
    int isoutlet = 1;
    String currentDate = "";
    String pickedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_view);

        currDate = findViewById(R.id.tvDate);
        back = findViewById(R.id.back);
        ll_head = findViewById(R.id.ll_head);
        no_data = findViewById(R.id.tv_no_data);
        rv_item = findViewById(R.id.rvInshopOrder);

//        currDate.setText("" + DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));
//        Intent intent = getIntent();
//        if (intent.hasExtra("isoutlet")) {
//            isoutlet = intent.getIntExtra("isoutlet", 1);
//        }

        currentDate = TimeUtils.getCurrentTime(TimeUtils.FORMAT2);
        pickedDate = TimeUtils.getCurrentTime(TimeUtils.FORMAT1);
        currDate.setText(currentDate);
        Intent intent=getIntent();
        if(intent.hasExtra("isoutlet")){
            isoutlet=intent.getIntExtra("isoutlet",1);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkExist();

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        outletReportAdapter = new InshopViewAdapter(getApplicationContext(), checkInList);
        rv_item.setLayoutManager(mLayoutManager1);
        rv_item.setAdapter(outletReportAdapter);

//        currDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int day, month, year;
//                if (!currDate.getText().toString().equals("")) {
//                    String[] dateArray = currDate.getText().toString().split("/");
//                    day = Integer.parseInt(dateArray[0]);
//                    month = Integer.parseInt(dateArray[1]) - 1;
//                    year = Integer.parseInt(dateArray[2]);
//                } else {
//                    Calendar c = Calendar.getInstance();
//
//                    day = c.get(Calendar.DAY_OF_MONTH);
//                    month = c.get(Calendar.MONTH);
//                    year = c.get(Calendar.YEAR);
//                }
//                DatePickerDialog dialog = new DatePickerDialog(InshopViewActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        String _year = String.valueOf(year);
//                        String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
//                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
//                        String _pickedDate = year + "-" + _month + "-" + _date;
//                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
//                        pickedDate = _pickedDate;
//                        currentDate = _date + "/" + _month + "/" + _year;
//                        currDate.setText(currentDate);
//                        no_data.setVisibility(View.GONE);
//                        ll_head.setVisibility(View.VISIBLE);
////                        shimmerFrameLayout.startShimmer();
////                        shimmerFrameLayout.setVisibility(View.VISIBLE);
//                        checkExist();
////                        getPoPMaterialList();
////                        getPopOrderDetail();
//                        outletReportAdapter.setDate(pickedDate);
//
//                    }
//                }, year, month, day);
//                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
//                dialog.show();
//            }
//        });
//        outletReportAdapter.setDate(pickedDate);
//        outletReportAdapter.setIsoutlet(isoutlet);

        currDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day, month, year;
                if(!currDate.getText().toString().equals("")){
                    String[] dateArray =  currDate.getText().toString().split("/");
                    day = Integer.parseInt(dateArray[0]);
                    month = Integer.parseInt(dateArray[1])-1;
                    year = Integer.parseInt(dateArray[2]);
                }else {
                    Calendar c = Calendar.getInstance();

                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH);
                    year = c.get(Calendar.YEAR);
                }
                DatePickerDialog dialog = new DatePickerDialog(InshopViewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = year + "-" + _month + "-" + _date;
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                        pickedDate=_pickedDate;
                        currentDate = _date +"/"+_month+"/"+_year;
                        currDate.setText(currentDate);
                        no_data.setVisibility(View.GONE);
                        ll_head.setVisibility(View.VISIBLE);

                        checkExist();

                        outletReportAdapter.setDate(pickedDate);

                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });
        outletReportAdapter.setDate(pickedDate);
//        outletReportAdapter.setIsoutlet(isoutlet);
    }

    private void checkExist() {
        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = request.getInshopRetailer("get/checkInList",
                div,
                SF_code,
                SF_code,
                Shared_Common_Pref.StateCode,
                date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    if (responseBody != null &&!responseBody.isEmpty()) {
                        checkInList.clear();
                        Log.v("responseList",responseBody);
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            Log.d("CheckOutActivity", "jsonArray" + jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                InshopModel checkInModel=new InshopModel(jsonObject.getInt("Sl_No"),jsonObject.getString("Sf_Code"),jsonObject.getString("Retailer_Code"),jsonObject.getString("eKey"));

//                                String name = jsonObject.getString("Retailer_Name");
//                                rName.setText(name);

                                if(jsonObject.has("CIn_Time")){
                                    String string= jsonObject.getString("CIn_Time");
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(string);
                                        if(jsonObject1.has("date")){
                                            InshopModel cInTime=new InshopModel(jsonObject1.getString("date"));
                                            checkInModel.setCintime(String.valueOf(cInTime));
                                            String  s=jsonObject1.getString("date");


                                            try {
                                                String ss=" ";
                                                time = s.substring(s.indexOf(ss),s.length()).trim();
                                                checkinTime.setText(time);
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



                                        };

                                    }catch (JSONException err){
                                        Log.d("Error", err.toString());
                                    }
                                }
                                if(jsonObject.has("C_Flag")){
                                    checkInModel.setCflag(jsonObject.getInt("C_Flag"));
                                }
                                checkInList.add(checkInModel);

                            }
                            int count=0;
                            if(checkInList.size()!=0) {
                                getRetailerName();
                                /*for (int k = 0; k < checkInList.size(); k++) {
//                                    if (checkInList.get(k).getCflag() == 1) {
////                                        tv_cIn_name.setVisibility(View.VISIBLE);
////                                        tv_status.setVisibility(View.VISIBLE);
////                                        tv_date.setVisibility(View.GONE);
////                                        ll_order.setVisibility(View.VISIBLE);
////
////                                        checkedinLay.setVisibility(View.VISIBLE);
////                                        checkinLay.setVisibility(View.GONE);
//                                        getRetailerName();
//                                        rName.setVisibility(View.VISIBLE);
//                                        checkinTime.setVisibility(View.VISIBLE);
////                                        rName.setVisibility(View.GONE);
////                                        checkinTime.setVisibility(View.GONE);
////                                        tv_cIn_name.setText(retailerLabelName + ": " + retailerName);
////                                        // tv_date.setText("Date: "+Date);
////                                        tv_tl_date.setVisibility(View.VISIBLE);
////                                        tv_tl_date.setText(Date);
////                                        ll_event_captures.setVisibility(View.GONE);
////                                        ll_retailer.setVisibility(View.GONE);
////                                        ll_head.setVisibility(View.VISIBLE);
////                                        btn_save.setVisibility(View.GONE);
////                                        textClock.setVisibility(View.GONE);
////                                        c_time.setVisibility(View.VISIBLE);
////                                        c_time.setText(checkInList.get(k).getTime());
////                                        checkedinTime.setText(checkInList.get(k).getCintime());
////                                        checkedinTime.setVisibility(View.VISIBLE);
//
//
//                                    }else{
//                                        rName.setVisibility(View.GONE);
//                                        checkinTime.setVisibility(View.GONE);
//
//                                    }
                                }*/
//
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

//    public  void checkExist(){
//        // shimmerFrameLayout.stopShimmer();
//        //  shimmerFrameLayout.setVisibility(View.GONE);
//
//
//            ApiInterface request = ApiClient.getClient().create(ApiInterface.class);
////        Call<ResponseBody> call = request.getInshopRetailer("get/checkInList",
////                div,
////                SF_code,
////                SF_code,
////                Shared_Common_Pref.StateCode,
////                date);
//            String type="";
//            if(isoutlet==1){
//                type="M";
//            }else{
//                type="I";
//            }
//
//        Call<ResponseBody> call = request.getInshopRetailer1("get/rptCheckInList",
//                div,
//                SF_code,
//                SF_code,
//                Shared_Common_Pref.StateCode,
//                date);
////                type);// TimeUtils.getCurrentTime(TimeUtils.FORMAT1)
//            call.enqueue(new Callback<ResponseBody>(){
//
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//                        String responseBody = response.body().string();
//                        Log.d("CheckInActivity","responseBody1234"+responseBody);
//                        if (responseBody != null) {
//                            checkInList.clear();
//                            try {
//                                JSONArray jsonArray = new JSONArray(responseBody.toString());
//                                Log.d("CheckOutActivity","jsonArray12334"+jsonArray.length());
//
//                                for(int i = 0; i< jsonArray.length(); i++){
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    InshopModel checkInModel=new InshopModel(jsonObject.getInt("Sl_No"),jsonObject.getString("Sf_Code"),jsonObject.getString("Retailer_Code"),jsonObject.getString("eKey"));
//
//                                    if(jsonObject.has("CIn_Time")){
//                                        String string= jsonObject.getString("CIn_Time");
//                                        try {
//                                            JSONObject jsonObject1 = new JSONObject(string);
//                                            if(jsonObject1.has("date")){
//                                                //ArrayList<CheckInModel.CInTime> cInTimeArrayList=new ArrayList<>();
//                                                InshopModel cInTime=new InshopModel(jsonObject1.getString("date"));
//                                                checkInModel.setCintime(String.valueOf(cInTime));
//                                                checkInModel.setCintime(jsonObject1.getString("date"));
//                                            }
//
//                                            String  s=jsonObject1.getString("date");
//                                            String ss=" ";
//                                            time1 = s.substring(s.indexOf(ss),s.length()).trim();
//
//
//                                        }catch (JSONException err){
//                                            Log.d("Error", err.toString());
//                                        }
//                                    }
//                                    if(jsonObject.has("COut_Time")){
//                                        String string= jsonObject.getString("COut_Time");
//                                        try {
//                                            JSONObject jsonObject1 = new JSONObject(string);
//                                            if(jsonObject1.has("date")){
//                                                InshopModel cOutTime=new InshopModel(jsonObject1.getString("date"));
//                                                checkInModel.setCouttime(String.valueOf(cOutTime));
//                                                checkInModel.setCouttime(jsonObject1.getString("date"));
//                                            }
//
//                                            String  s=jsonObject1.getString("date");
//                                            String ss=" ";
//                                            time2 = s.substring(s.indexOf(ss),s.length()).trim();
//                                            checkInModel.setIn_Time(getInTime(time1,time2));
//                                        }catch (JSONException err){
//                                            Log.d("Error", err.toString());
//                                        }
//
//                                    }
//                                    if(jsonObject.has("C_Flag")){
//                                        checkInModel.setCflag(jsonObject.getInt("C_Flag"));
//                                    }
//                                    if(jsonObject.has("Entry_Type")){
//                                        checkInModel.setEntrytype(jsonObject.getString("Entry_Type"));
//                                    }
//                                   /* if(jsonObject.has("Retailer_Code")){
//                                        checkInModel.setC_flag(jsonObject.getInt("Retailer_Code"));
//                                        getRetailerName();
//                                    }*/
//                                    checkInList.add(checkInModel);
//                                }
//                                if(checkInList.size()!=0) {
//                                    getRetailerName();
//                                    //for (int k = 0; k < checkInList.size(); k++) {
//                                    //  getRetailerName();
//                                    //  }
//                                }else{
//                                    ll_head.setVisibility(View.GONE);
//                                    no_data.setVisibility(View.VISIBLE);
//                                    Toast.makeText(InshopViewActivity.this,"No data Available",Toast.LENGTH_SHORT).show();
//                                }
//                                outletReportAdapter.setPopStockList(checkInList);
//                                outletReportAdapter.notifyDataSetChanged();
////                                shimmerFrameLayout.stopShimmer();
////                                shimmerFrameLayout.setVisibility(View.GONE);
//
//                            } catch (JSONException e) {
////                                shimmerFrameLayout.stopShimmer();
////                                shimmerFrameLayout.setVisibility(View.GONE);
//                                e.printStackTrace();
//                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
//                            }
//                        }else{
////                            shimmerFrameLayout.stopShimmer();
////                            shimmerFrameLayout.setVisibility(View.GONE);
//                            ll_head.setVisibility(View.GONE);
//                            no_data.setVisibility(View.VISIBLE);
//                            Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();
//                        }
//                    } catch (IOException e) {
//
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.d("Error", "" + t.getMessage());
////                    shimmerFrameLayout.stopShimmer();
////                    shimmerFrameLayout.setVisibility(View.GONE);
//                    ll_head.setVisibility(View.GONE);
//                    no_data.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(), "Failure : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//
//    }


    private void getRetailerName() {
        for (int i = 0; i < checkInList.size(); i++) {
            for (int j = 0; j < retailerList.size(); j++) {
                if ((checkInList.get(i).getRetailercode()).equals(retailerList.get(j).getMobileNumber())) {
                    retailerName = retailerList.get(j).getName();
                    //retailer_code=retailerList.get(j).getMobileNumber();
                    Log.d("checkInActivityrName", "name" + retailerName);
                }

            }
        }
    }

    public String getInTime(String time1, String time2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null, date2 = null;
        try {
            date1 = simpleDateFormat.parse(this.time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            date2 = simpleDateFormat.parse(this.time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        int sec = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / 1000) % 60;
        hours = (hours < 0 ? -hours : hours);
        Log.i("======= Hours", " :: " + hours + ":" + min + ":" + sec);
        String diffTime = hours + ":" + min + ":" + sec;
        return diffTime;


    }
}