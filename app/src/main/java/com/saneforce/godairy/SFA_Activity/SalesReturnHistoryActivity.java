package com.saneforce.godairy.SFA_Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Activity_Hap.AllowancCapture;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.SalesReturnHistoryAdapter;
import com.saneforce.godairy.SFA_Model_Class.SalesReturnHistoryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReturnHistoryActivity extends AppCompatActivity {
    TextView fromDateTV, toDateTV, totalTV;
    RecyclerView recyclerView;
    LinearLayout rowLayout;
    CardView totalLayout;
    DatePickerDialog fromDatePickerDialog;
    Context context = this;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    ArrayList<SalesReturnHistoryModel> list;
    SalesReturnHistoryAdapter adapter;
    String date, stDate, endDate;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return_history);
        fromDateTV = findViewById(R.id.tvStartDate);
        toDateTV = findViewById(R.id.tvEndDate);
        totalTV = findViewById(R.id.txtTotAmt);
        recyclerView = findViewById(R.id.invoicerecyclerview);
        rowLayout = findViewById(R.id.row_report);
        totalLayout = findViewById(R.id.cvTotParent);
        home = findViewById(R.id.toolbar_home);
        common_class = new Common_Class(context);
        shared_common_pref = new Shared_Common_Pref(context);
        list = new ArrayList<>();
//        common_class.gotoHomeScreen(context, home);

        home.setOnClickListener(v -> {
            try {
                AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                    @Override
                    public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                        String imageServer = FileName;
                        String imageConvert = fullPath;
                        Log.e("OnImageURIPick", imageServer + imageConvert);
                        Toast.makeText(context, imageServer + ": " + imageConvert, Toast.LENGTH_LONG).show();
                        home.setImageBitmap(image);
                    }
                });
                Intent intent = new Intent(context, AllowancCapture.class);
                intent.putExtra("allowance", "One");
                startActivity(intent);
            } catch (Exception ignored) {}
        });

        fromDateTV.setOnClickListener(v -> selectDate(1));
        toDateTV.setOnClickListener(v -> selectDate(2));
    }

    void selectDate(int val) {
        Calendar calendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            int month = monthOfYear + 1;
            date = ("" + year + "-" + month + "-" + dayOfMonth);
            if (val == 1) {
                if (common_class.checkDates(date, toDateTV.getText().toString(), (Activity) context) || toDateTV.getText().toString().equals("")) {
                    fromDateTV.setText(date);
                    stDate = fromDateTV.getText().toString();
                    getDataFromAPI();
                } else
                    common_class.showMsg((Activity) context, "Please select valid date");
            } else {
                if (common_class.checkDates(fromDateTV.getText().toString(), date, (Activity) context) ||
                        fromDateTV.getText().toString().equals("")) {
                    toDateTV.setText(date);
                    endDate = toDateTV.getText().toString();
                    getDataFromAPI();
                } else
                    common_class.showMsg((Activity) context, "Please select valid date");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private void getDataFromAPI() {
        if (fromDateTV.getText().toString().equals("") || toDateTV.getText().toString().equals("")) {
            return;
        }
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading returned sales...");
        dialog.setCancelable(false);
        dialog.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_sales_return_history");
        params.put("fromDate", stDate);
        params.put("toDate", endDate);
        params.put("fromStockist", shared_common_pref.getvalue(Constants.Distributor_Id));
        params.put("toCus", Shared_Common_Pref.OutletCode);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray array = jsonObject.getJSONArray("response");
                            LoadResponse(array);
                            Log.e("status", "Request Result: \n" + jsonObject);
                        } else {
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void LoadResponse(JSONArray array) {
        try {
            list.clear();
            double grandTotal = 0;
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String id = object.getString("id");
                String date = object.getString("date");
                double value = object.getDouble("total");
                grandTotal += value;
                list.add(new SalesReturnHistoryModel(id, date, value));
            }
            if (list.isEmpty()) {
                Toast.makeText(context, "No Sales Return found for the selected time period", Toast.LENGTH_SHORT).show();
                rowLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                totalLayout.setVisibility(View.GONE);
                return;
            }
            adapter = new SalesReturnHistoryAdapter(context, list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(adapter);
            rowLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            totalLayout.setVisibility(View.VISIBLE);
            totalTV.setText(common_class.formatCurrency(grandTotal));
        } catch (Exception e) {
            rowLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            totalLayout.setVisibility(View.GONE);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}