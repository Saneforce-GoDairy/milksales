package com.saneforce.godairy.SFA_Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.rvVanStockview;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class VanStockViewActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    Common_Class common_class;
    TextView tvDt, tvLoadAmt, tvUnLoadAmt, tvTotVanSalQty, tvTotStkQty;
    NumberFormat formatter = new DecimalFormat("##0.00");
    RecyclerView rvVanStockDets;

    private DatePickerDialog fromDatePickerDialog;

    public static TextView tvStartDate, tvEndDate;
    public static String stDate = "", endDate = "";
    String date = "";

    Shared_Common_Pref sharedCommonPref;
    TextView tv_no_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_van_stockview);


        sharedCommonPref = new Shared_Common_Pref(VanStockViewActivity.this);
        common_class = new Common_Class(this);

        rvVanStockDets=findViewById(R.id.idStockDets);

        tvLoadAmt = findViewById(R.id.tvLoadStkAmt);
        tvUnLoadAmt = findViewById(R.id.tvUnLoadStkAmt);

        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tv_no_data=findViewById(R.id.tv_no_data);

        stDate = Common_Class.GetDatewothouttime();
        endDate = Common_Class.GetDatewothouttime();
        tvStartDate.setText(stDate);
        tvEndDate.setText(endDate);
//
////        Log.v("sdate",tvStartDate.toString());

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

//        tvStartDate.setText(Common_Class.GetDatewothouttime());
//        tvEndDate.setText(Common_Class.GetDatewothouttime());

        common_class.getDb_310Data(Constants.VAN_STOCK_DTWS, this);

        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);

    }


    void showDatePickerDialog(int val) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(VanStockViewActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), VanStockViewActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();

                        Log.v("sdatefd",stDate);
                        common_class.getDb_310Data(Constants.VAN_STOCK_DTWS, VanStockViewActivity.this);
                    } else
                        common_class.showMsg(VanStockViewActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, VanStockViewActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        Log.v("sdatefd",endDate);

                        common_class.getDb_310Data(Constants.VAN_STOCK_DTWS, VanStockViewActivity.this);

                    } else
                        common_class.showMsg(VanStockViewActivity.this, "Please select valid date");

                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                showDatePickerDialog(1);
                break;
            case R.id.tvEndDate:
                showDatePickerDialog(2);
                break;

        }
    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.VAN_STOCK_DTWS:
                    Log.v("vanstocks_date", apiDataResponse);

                    setHistoryAdapter(apiDataResponse);

                    break;

            }
        } catch (Exception e) {

        }
    }

    private void setHistoryAdapter(String apiDataResponse) {
        try {

            JSONObject stkObj = new JSONObject(apiDataResponse);
//            salAmt = 0;
//            totStk = 0;
//            totSal = 0;
            JSONArray arr = stkObj.getJSONArray("Data");

//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject obj = arr.getJSONObject(i);
//
//                totStk += obj.getInt("Cr");
//                totSal += obj.getInt("Dr");
//
//            }
//            tvTotVanSalQty.setText("" + totSal);
//            tvTotStkQty.setText("" + totStk);


            //  tvUnLoadAmt.setText(CurrencySymbol+" " + formatter.format(getIntent().getDoubleExtra("stkLoadAmt", 0) - salAmt));
             if(arr.length()>0) {
                 rvVanStockDets.setVisibility(View.VISIBLE);
                 tv_no_data.setVisibility(View.GONE);
                 rvVanStockDets.setAdapter(new rvVanStockview(arr, R.layout.layvanstockadb, this));
             }else{
                 rvVanStockDets.setVisibility(View.GONE);
                 tv_no_data.setVisibility(View.VISIBLE);
             }

        } catch (Exception e) {
            Log.v("adap:", e.getMessage());
        }

    }

}