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
import android.widget.ImageView;
import android.widget.TextView;

import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Adapter.FlightBooking_TravelerDetail_Adapter;
import com.saneforce.milksales.adapters.FlightBooking_Approval_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FlightBookingApproval  extends AppCompatActivity implements UpdateResponseUI {
    RecyclerView rv;
    TextView tvFromDate, tvToDate;
    private DatePickerDialog fromDatePickerDialog;
    com.saneforce.milksales.Common_Class.Common_Class common_class;
    public static FlightBookingApproval activity;
    SharedPreferences UserDetails;
    public static final String UserInfo = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.flight_booking_approval);
            init();

            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
            refreshData();

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

    public void refreshData() {
        common_class.getDb_310Data(Constants.FlightBookingPending, this, null);
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
        common_class = new com.saneforce.milksales.Common_Class.Common_Class(this);
        rv = findViewById(R.id.rvFightBookSta);
    }

    public void showTravelersDialog(JSONArray arr) {
        try {
            LayoutInflater inflater = LayoutInflater.from(FlightBookingApproval.this);

            final View view = inflater.inflate(R.layout.flight_travelers_header, null);
            AlertDialog alertDialog = new AlertDialog.Builder(FlightBookingApproval.this).create();
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
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        switch (key) {
            case Constants.FlightBookingPending:
                try {
                    JSONObject obj = new JSONObject(apiDataResponse);

                    if (obj.getBoolean("success")) {
                        String sSF=UserDetails.getString("Sfcode", "");
                        rv.setAdapter(new FlightBooking_Approval_Adapter(obj.getJSONArray("data"), this,sSF));
                    }

                } catch (Exception e) {

                }
                break;
        }
    }
}
