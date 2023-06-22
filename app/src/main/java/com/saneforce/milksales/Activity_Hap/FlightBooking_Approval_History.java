package com.saneforce.milksales.Activity_Hap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Adapter.FlightBooking_TravelerDetail_Adapter;
import com.saneforce.milksales.adapters.FlightBooking_ApprHistory_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class FlightBooking_Approval_History extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    RecyclerView rv;
    TextView tvFromDate, tvToDate;
    private DatePickerDialog fromDatePickerDialog;
    Common_Class common_class;
    public static FlightBooking_Approval_History activity;
    SharedPreferences  UserDetails;
    public static final String UserInfo = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_flight_booking_status);
            init();

            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
            JsonObject jParam = new JsonObject();

            jParam.addProperty("FDT", tvFromDate.getText().toString());
            jParam.addProperty("TDT", tvToDate.getText().toString());
            common_class.getDb_310Data(Constants.FlightBookingApprovalHistory, this, jParam);


            ImageView backView = findViewById(R.id.imag_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBackPressedDispatcher.onBackPressed();
                }
            });
        } catch (Exception e) {

        }

    }
    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });


    void init() {
        activity = this;
        common_class = new Common_Class(this);
        tvFromDate = findViewById(R.id.tvFDate);
        tvToDate = findViewById(R.id.tvTDate);
        rv = findViewById(R.id.rvFightBookSta);

        tvToDate.setOnClickListener(this);
        tvFromDate.setOnClickListener(this);

        tvFromDate.setText(Common_Class.GetDatewothouttime());
        tvToDate.setText(Common_Class.GetDatewothouttime());

    }

    public void showTravelersDialog(JSONArray arr) {
        try {
            LayoutInflater inflater = LayoutInflater.from(FlightBooking_Approval_History.this);

            final View view = inflater.inflate(R.layout.flight_travelers_header, null);
            AlertDialog alertDialog = new AlertDialog.Builder(FlightBooking_Approval_History.this).create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageView ivClose = view.findViewById(R.id.ivClose);

            RecyclerView rv = view.findViewById(R.id.rvFlightTravelers);

            rv.setAdapter(new FlightBooking_TravelerDetail_Adapter(arr, this));

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


            alertDialog.setView(view);
            alertDialog.show();
        } catch (Exception e) {
            Log.e("OrderAdapter:dialog ", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFDate:
                showDatePickerDialog(0, tvFromDate);
                break;
            case R.id.tvTDate:
                showDatePickerDialog(1, tvToDate);
                break;
        }
    }
    void RefreshData(){
        JsonObject jParam = new JsonObject();
        jParam.addProperty("FDT", tvFromDate.getText().toString());
        jParam.addProperty("TDT", tvToDate.getText().toString());
        common_class.getDb_310Data(Constants.FlightBookingApprovalHistory, FlightBooking_Approval_History.this, jParam);
    }
    void showDatePickerDialog(int pos, TextView tv) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(FlightBooking_Approval_History.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);

                if (common_class.checkDates(pos == 0 ? date : tvFromDate.getText().toString(), pos == 1 ? date : tvToDate.getText().toString(), FlightBooking_Approval_History.this)) {
                    tv.setText(date);
                    RefreshData();
                    } else {
                    common_class.showMsg(FlightBooking_Approval_History.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        switch (key) {
            case Constants.FlightBookingApprovalHistory:
                try {
                    JSONObject obj = new JSONObject(apiDataResponse);

                    if (obj.getBoolean("success")) {
                        String sSF=UserDetails.getString("Sfcode", "");
                        rv.setAdapter(new FlightBooking_ApprHistory_Adapter(obj.getJSONArray("data"), this,sSF));
                    }

                } catch (Exception e) {

                }
                break;
        }
    }
}