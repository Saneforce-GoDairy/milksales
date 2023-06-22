package com.saneforce.milksales.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.Activity_Hap.ProductImageView;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class FuelAllowance extends AppCompatActivity {
    String FUClaim = "", StartedKm = "", ClosingKm = "", PersonalKm = "", StrDaName = "", strFuelAmount = "";
    Double tofuel = 0.0, fAmount = 0.0;
    TextView TxtStartedKm, TxtClosingKm, travelTypeMode, TotalTravelledKm, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
            fuelAmount, TextTotalAmount;
    Integer totalkm = 0, totalPersonalKm = 0, Pva, C = 0, S = 0;
    JSONArray jsonArray = null;
    ImageView startImage, EndImage,imgBck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_allowance);

        FUClaim = String.valueOf(getIntent().getSerializableExtra("jsonTravDetai"));
        txtTaClaim = findViewById(R.id.mode_name);
        TxtStartedKm = findViewById(R.id.txt_started_km);
        TxtClosingKm = findViewById(R.id.txt_ended_km);
        travelTypeMode = findViewById(R.id.txt_type_travel);
        PersonalTextKM = findViewById(R.id.personal_km_text);
        TextTotalAmount = findViewById(R.id.txt_total_amt);
        TotalTravelledKm = findViewById(R.id.total_km);
        PersonalKiloMeter = findViewById(R.id.pers_kilo_meter);
        getToolbar();

        imgBck = findViewById(R.id.imag_back);
        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        fuelAmount = findViewById(R.id.fuel_amount);
        try {
            jsonArray = new JSONArray(FUClaim);
            fuelAll(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("JSON_FUC", FUClaim);

    }

    public void getToolbar() {
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                   FuelAllowance.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {
    }



    public void fuelAll(JSONArray jsonArray) {
        JSONObject jsonObject = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            int finalC = i;

            Log.e("JsonArray", String.valueOf(jsonArray.length()));

            try {
                jsonObject = (JSONObject) jsonArray.get(i);
                Log.e("jsonObject", String.valueOf(jsonObject.toString()));
                StartedKm = jsonObject.getString("Start_KM");
                ClosingKm = jsonObject.getString("End_KM");
                PersonalKm = jsonObject.getString("Personal_KM");
                StrDaName = jsonObject.getString("Mode_Of_Travel");
                strFuelAmount = jsonObject.getString("Fare");


                startImage = findViewById(R.id.startkmimage);
                EndImage = findViewById(R.id.endkmimage);

                Glide.with(FuelAllowance.this)
                        .load(String.valueOf(getIntent().getSerializableExtra("start_Photo")))
                        .into(startImage);
                Glide.with(FuelAllowance.this)
                        .load(String.valueOf(getIntent().getSerializableExtra("End_photo")))
                        .into(EndImage);


                startImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                        intent.putExtra("ImageUrl", String.valueOf(getIntent().getSerializableExtra("start_Photo")));
                        startActivity(intent);
                    }
                });

                EndImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                        intent.putExtra("ImageUrl", String.valueOf(getIntent().getSerializableExtra("End_photo")));
                        startActivity(intent);
                    }
                });

                fAmount = Double.valueOf(strFuelAmount);
                fuelAmount.setText(" Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM ");
                /*                txtTaClaim.setText(StrDaName);*/

                StartedKm = StartedKm.replaceAll("^[\"']+|[\"']+$", "");
                if (StartedKm != null && !StartedKm.isEmpty() && !StartedKm.equals("null") && !StartedKm.equals("")) {
                    S = Integer.valueOf(StartedKm);
                    TxtStartedKm.setText(StartedKm);
                } else {

                }

                PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");

                if (PersonalKm.equals("null")) {
                    PersonalKiloMeter.setText("0");
                } else {
                    PersonalKiloMeter.setText(PersonalKm);
                }

                if (ClosingKm != null && !ClosingKm.isEmpty() && !ClosingKm.equals("null") && !ClosingKm.equals("")) {
                    ClosingKm = ClosingKm.replaceAll("^[\"']+|[\"']+$", "");
                    TxtClosingKm.setText(ClosingKm);
                    C = Integer.valueOf(ClosingKm);
                    totalkm = C - S;

                }

                if (PersonalKm != null && !PersonalKm.isEmpty() && !PersonalKm.equals("null") && !PersonalKm.equals("")) {
                    PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                    Pva = Integer.valueOf(PersonalKm);
                    totalPersonalKm = totalkm - Pva;
                    PersonalTextKM.setText(String.valueOf(totalPersonalKm));
                }

                Double totalAmount = Double.valueOf(strFuelAmount);
                tofuel = totalPersonalKm * totalAmount;
                TextTotalAmount.setText("Rs. " + new DecimalFormat("##0.00").format(tofuel));
                TotalTravelledKm.setText(String.valueOf(totalkm));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}