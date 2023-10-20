package com.milksales.godairy.SFA_Activity;

import static com.milksales.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.POSStockHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LedgerHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    public static String ledgerFDT = "", ledgerTDT = "";
    public TextView tvOutletName, tvStartDate, tvEndDate;
    DatePickerDialog fromDatePickerDialog;
    RecyclerView rvLedger;
    POSStockHistoryAdapter plAdapter;
    Shared_Common_Pref sharedCommonPref;
    TextView tvGrandTot;
    Context context = this;
    private Common_Class common_class;
    private String date;

    TextView info;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_history);
        init();

        JsonObject jParam = new JsonObject();
        jParam.addProperty("FDate", ledgerFDT);
        jParam.addProperty("TDate", ledgerTDT);
        getData(jParam);

        // tvOutletName.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
        tvOutletName.setText(sharedCommonPref.getvalue(Constants.Distributor_name));
        tvOutletName.setSelected(true);

    }

    public void init() {
        common_class = new Common_Class(this);
        sharedCommonPref = new Shared_Common_Pref(this);
        rvLedger = findViewById(R.id.rvLedger);
        tvOutletName = findViewById(R.id.retailername);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvGrandTot = findViewById(R.id.txtTotCBAmt);

        info = findViewById(R.id.info_text);
        progressBar = findViewById(R.id.progressbar);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);


        tvStartDate.setText(Common_Class.GetDatewothouttime());
        tvEndDate.setText(Common_Class.GetDatewothouttime());

        ledgerFDT = String.valueOf(tvStartDate.getText());
        ledgerTDT = String.valueOf(tvEndDate.getText());

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
        }
    }


    void selectDate(int val) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), (Activity) context) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        ledgerFDT = tvStartDate.getText().toString();
                        JsonObject jParam = new JsonObject();
                        jParam.addProperty("FDate", ledgerFDT);
                        jParam.addProperty("TDate", ledgerTDT);
                        getData(jParam);
                    } else
                        common_class.showMsg((Activity) context, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, (Activity) context) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        ledgerTDT = tvEndDate.getText().toString();

                        JsonObject jParam = new JsonObject();
                        jParam.addProperty("FDate", ledgerFDT);
                        jParam.addProperty("TDate", ledgerTDT);
                        getData(jParam);
//                        common_class.getDb_310Data(Constants.LEDGER, (Activity) context, jParam);

                    } else
                        common_class.showMsg((Activity) context, "Please select valid date");

                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();


    }

    private void getData(JsonObject jParam) {
        progressBar.setVisibility(View.VISIBLE);
        info.setVisibility(View.GONE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(context);
        params.put("axn", "get_dist_pos_stock_ledger");
        params.put("SF", Shared_Common_Pref.Sf_Code);
        params.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
        params.put("FDT", jParam.get("FDate").getAsString());
        params.put("TDT", jParam.get("TDate").getAsString());

        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            LoadUI(jsonArray.toString());
                        } else {
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadUI(String apiDataResponse) {
        try {
            JSONArray legList = new JSONArray(apiDataResponse);
            plAdapter = new POSStockHistoryAdapter(this, legList);
            rvLedger.setAdapter(plAdapter);
            double totAmt = 0;
            for (int i = 0; i < legList.length(); i++) {
                JSONObject obj = legList.getJSONObject(i);
                totAmt += obj.getDouble("ClAmt");
            }
            tvGrandTot.setText(CurrencySymbol + " " + new DecimalFormat("##0.00").format(totAmt));
            progressBar.setVisibility(View.GONE);
            if (legList.length() == 0) {
                info.setVisibility(View.VISIBLE);
            } else {
                info.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
