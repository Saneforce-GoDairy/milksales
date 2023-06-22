package com.saneforce.milksales.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.Activity_Hap.AttachementActivity;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TL_cliam_Apprval extends AppCompatActivity {

    LinearLayout travelDynamicLoaction;
    TextView editText, tvTxtUKeys;
    EditText etrTaFr, etrTaTo, enterFrom, enterTo, enterFare;
    String TLClaim = "", StrToEnd = "0", TlUKey = "";
    JSONArray jsonArray = null;
    ImageView ImgPreview, imgBck;
    Integer tlPos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_l_cliam__apprval);
        travelDynamicLoaction = findViewById(R.id.lin_travel_dynamic_location);
        getToolbar();
        TLClaim = String.valueOf(getIntent().getSerializableExtra("TLAllowance"));
        StrToEnd = String.valueOf(getIntent().getSerializableExtra("strEnd"));

        imgBck = findViewById(R.id.imag_back);
        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        Log.v("TLAllowance", TLClaim);
        Log.v("TLStrToEnd", StrToEnd);

        try {
            jsonArray = new JSONArray(TLClaim);
            if (StrToEnd.equals("0")) {
                trvldLocationBus(jsonArray);
            } else {
                trvldLocation(jsonArray);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void trvldLocation(JSONArray traveldLoc) {

        for (int i = 0; i < traveldLoc.length(); i++) {
            JSONObject tldraftJson = null;
            try {
                tldraftJson = (JSONObject) traveldLoc.get(i);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                rowView = inflater.inflate(R.layout.tl_claim_appro, null);
                travelDynamicLoaction.addView(rowView, layoutParams);
                View views = travelDynamicLoaction.getChildAt(i);

                etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                etrTaFr.setText(tldraftJson.getString("From_P"));
                etrTaTo.setText(tldraftJson.getString("To_P"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


    public void trvldLocationBus(JSONArray traveldLoc) {

        for (int j = 0; j < traveldLoc.length(); j++) {

            Log.v("TravelldArray", String.valueOf(traveldLoc.length()));

            JSONObject tldraftJsons = null;
            try {
                tldraftJsons = (JSONObject) traveldLoc.get(j);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                View rowView = inflater.inflate(R.layout.tl_claim_appro_one, null);

                travelDynamicLoaction.addView(rowView, layoutParams);


                View views = travelDynamicLoaction.getChildAt(j);
                LinearLayout lad = (LinearLayout) views.findViewById(R.id.linear_row_ad);
                editText = (TextView) views.findViewById(R.id.enter_mode);
                enterFrom = views.findViewById(R.id.enter_from);
                enterTo = views.findViewById(R.id.enter_to);
                enterFare = views.findViewById(R.id.enter_fare);
                ImgPreview = views.findViewById(R.id.tl_img_prv);
                tvTxtUKeys = (TextView) (views.findViewById(R.id.txt_tv_ukey));


                editText.setText("" + tldraftJsons.getString("Mode"));
                enterFrom.setText("" + tldraftJsons.getString("From_P"));
                enterTo.setText("" + tldraftJsons.getString("To_P"));
                enterFare.setText("" + tldraftJsons.getString("Fare"));
                tvTxtUKeys.setText("" + tldraftJsons.getString("Ukey"));


                tlPos = travelDynamicLoaction.indexOfChild(rowView);
                ImgPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* View views = travelDynamicLoaction.getChildAt(tlPos);*/


                        Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                        View view = travelDynamicLoaction.getChildAt(tvSizes);
                        editText = (TextView) (view.findViewById(R.id.enter_mode));
                        enterFare = (EditText) view.findViewById(R.id.enter_fare);
                        tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));

                        TlUKey = tvTxtUKeys.getText().toString();


                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", TlUKey);
                        stat.putExtra("headTravel", "TL");
                        stat.putExtra("mode", editText.getText().toString());
                        stat.putExtra("Delete", "1");
                        stat.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
                        startActivity(stat);


                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    TL_cliam_Apprval.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}