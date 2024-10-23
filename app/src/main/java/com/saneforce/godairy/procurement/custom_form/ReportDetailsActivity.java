package com.saneforce.godairy.procurement.custom_form;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_CUSTOM_FORM_REPORTS;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.SFA_Model_Class.TimeUtils;
import com.saneforce.godairy.databinding.ActivityCustomFormReportDetailsBinding;
import com.saneforce.godairy.procurement.custom_form.adapter.DynamicFormReportDetAdapter;
import com.saneforce.godairy.procurement.custom_form.model.CustomReportModel;
import com.saneforce.godairy.universal.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailsActivity extends AppCompatActivity {
   private ActivityCustomFormReportDetailsBinding binding;
   private final Context context = this;
   String mModuleId;
    SharedPreferences UserDetails;
    public static final String MY_PREFERENCES = "MyPrefs";
    private String SF_CODE , DIV_CODE , DESIGN;
    private String mCurrentDateFrom, mCurrentDateTo;
    ArrayList<CustomReportModel> modelArrayList;
    DynamicFormReportDetAdapter dynamicFormReportDetAdapter;
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat dfDate   = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomFormReportDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();

        modelArrayList = new ArrayList<>();

        mModuleId = getIntent().getStringExtra("module_id");

        UserDetails = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        SF_CODE = Shared_Common_Pref.Sf_Code;
        DIV_CODE = UserDetails.getString("Divcode", "");
        DESIGN = UserDetails.getString("DesigNm", "");

        mCurrentDateFrom = TimeUtils.getCurrentTime(TimeUtils.FORMAT1);
        mCurrentDateTo = TimeUtils.getCurrentTime(TimeUtils.FORMAT1);

        binding.fromDate.setText(mCurrentDateFrom);
        binding.toDate.setText(mCurrentDateTo);

        //-------------------------------------
        binding.fromDatePr.setText(parseDateToddMMyyyy(mCurrentDateFrom));
        binding.toDatePr.setText(parseDateToddMMyyyy(mCurrentDateTo));

        binding.fromDatePr.setOnClickListener(v -> {

            modelArrayList.clear();
            dynamicFormReportDetAdapter.notifyDataSetChanged();

            int day, month, year;
            if (!binding.fromDate.getText().toString().equals("")) {
                String[] dateArray = binding.fromDate.getText().toString().split("-");
                year = Integer.parseInt(dateArray[0]);
                month = Integer.parseInt(dateArray[1]) - 1;
                day = Integer.parseInt(dateArray[2]);
            } else {
                Calendar c = Calendar.getInstance();
                day = c.get(Calendar.MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
            }
            DatePickerDialog dialog = new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> {
                String _year = String.valueOf(year1);
                String _month = (month1 + 1) < 10 ? "0" + (month1 + 1) : String.valueOf(month1 + 1);
                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String _pickedDate = year1 + "-" + _month + "-" + _date;
                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                mCurrentDateFrom = _pickedDate;// _date +"/"+_month+"/"+_year;
                if (checkDates(mCurrentDateFrom,mCurrentDateTo)){
                    loadCustomFormData(mCurrentDateFrom,mCurrentDateTo);
                }else {
                    modelArrayList.clear();
                    dynamicFormReportDetAdapter.notifyDataSetChanged();
                    showError2();
                    // Toast.makeText(context, "From date never less than the to date", Toast.LENGTH_SHORT).show();
                }
                binding.fromDate.setText(mCurrentDateFrom);
                //-----------------------------
                binding.fromDatePr.setText(parseDateToddMMyyyy(mCurrentDateFrom));
            }, year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        binding.toDatePr.setOnClickListener(v -> {

            modelArrayList.clear();
            dynamicFormReportDetAdapter.notifyDataSetChanged();

            int day, month, year;
            if (!binding.toDate.getText().toString().equals("")) {
                String[] dateArray = binding.toDate.getText().toString().split("-");
                year = Integer.parseInt(dateArray[0]);
                month = Integer.parseInt(dateArray[1]) - 1;
                day = Integer.parseInt(dateArray[2]);
            } else {
                Calendar c = Calendar.getInstance();

                day = c.get(Calendar.MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
            }
            DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String _year = String.valueOf(year);
                    String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                    String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                    String _pickedDate = year + "-" + _month + "-" + _date;
                    Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                    mCurrentDateTo = _pickedDate;//_date +"/"+_month+"/"+_year;
                    if (checkDates(mCurrentDateFrom, mCurrentDateTo)){
                        loadCustomFormData(mCurrentDateFrom, mCurrentDateTo);
                    }else {
                        modelArrayList.clear();
                        dynamicFormReportDetAdapter.notifyDataSetChanged();
                        showError2();
                        // Toast.makeText(context, "From date never less than the to date", Toast.LENGTH_SHORT).show();
                    }
                    binding.toDate.setText(mCurrentDateTo);
                    //-----------------------------
                    binding.toDatePr.setText(parseDateToddMMyyyy(mCurrentDateTo));
                }
            }, year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        dynamicFormReportDetAdapter = new DynamicFormReportDetAdapter(getApplicationContext(), modelArrayList);
        dynamicFormReportDetAdapter.setModuleId(mModuleId);
        dynamicFormReportDetAdapter.setSfCode(SF_CODE);
        binding.recyclerView.setAdapter(dynamicFormReportDetAdapter);

        loadCustomFormData(mCurrentDateFrom, mCurrentDateTo);

        binding.back.setOnClickListener(v -> finish());
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void showError2() {
        binding.errorContainer.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(() -> binding.errorContainer.setVisibility(View.GONE), 2000);
    }

    public static boolean checkDates(String d1, String d2)    {
        boolean b = false;
        try {
            if(dfDate.parse(d1).before(dfDate.parse(d2)))
            {
                b = true;//If start date is before end date
            }
            else if(dfDate.parse(d1).equals(dfDate.parse(d2)))
            {
                b = true;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
                Log.e("dd__", "from date never less than the to date");
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    private void loadCustomFormData(String currentDateFrom,String currentDateTo) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("axn", PROCUREMENT_GET_CUSTOM_FORM_REPORTS);
        queryParams.put("divisionCode", Constant.DIVISION_CODE);
        queryParams.put("State_Code", Constant.STATE_CODE);
        queryParams.put("sfCode", SF_CODE);
        queryParams.put("moduleId", mModuleId);
        queryParams.put("fromDate", currentDateFrom);
        queryParams.put("toDate", currentDateTo);

        SharedPreferences UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Shared_Common_Pref sharedCommonPref = new Shared_Common_Pref(context);
        queryParams.put("diC", sharedCommonPref.getvalue(Constants.Distributor_Id));
        queryParams.put("sfC", UserDetails.getString("Sfcode", ""));
        queryParams.put("reC", Shared_Common_Pref.OutletCode.equals("OutletCode") ? "" : Shared_Common_Pref.OutletCode);

        Call<ResponseBody> call = apiInterface.getCustomFormReportsModuleList(queryParams);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(GONE);
                    String customReportList;

                    try {
                        customReportList = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);

                    }
                    Log.e("res_", customReportList);

                    try {
                        JSONArray jsonArray = new JSONArray(customReportList);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CustomReportModel model = new CustomReportModel();

                            model.setId("" + i + 1);

                            if (jsonObject.has("EntryId")) {
                                model.setEntryId(jsonObject.getString("EntryId"));
                            }
                            model.setType(1);
                            modelArrayList.add(model);

                        }

                        if(modelArrayList.size()>0) {
                            dynamicFormReportDetAdapter.setModelArrayList(modelArrayList);
                            binding.noData.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        }else{
                            binding.noData.setVisibility(View.VISIBLE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                    //    throw new RuntimeException(e);
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.shimmerLayout.setVisibility(GONE);
                        binding.nullError.setVisibility(View.VISIBLE);
                        binding.message.setText("Something went wrong!");
                    }

//                    try {
//                        customReportList = response.body().string();
//                        Log.e("res_", customReportList);
//                        JSONArray jsonArray = new JSONArray(customReportList);
//
//                        for (int i = 0; i < jsonArray.length(); i++){
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        }
//
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        binding.shimmerLayout.setVisibility(GONE);
        binding.recyclerView.setVisibility(GONE);
        binding.nullError.setVisibility(View.VISIBLE);
        binding.message.setText("Something went wrong!");
    }

    private void onClick() {
        binding.back.setOnClickListener(v -> finish());
    }
}