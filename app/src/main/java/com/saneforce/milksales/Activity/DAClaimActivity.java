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

import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DAClaimActivity extends AppCompatActivity {

    String DaClaim = "";
    TextView dailyAllowance, modeAmount, DrvBrdAmt, Datotal;
    ImageView imgBck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_a_claim);
        getToolbar();
        dailyAllowance = findViewById(R.id.txt_daily_allowance_mode);
        modeAmount = findViewById(R.id.txt_BrdAmt);
        DrvBrdAmt = findViewById(R.id.txt_DrvBrdAmt);
        Datotal = findViewById(R.id.txt_totDA);
        DaClaim = String.valueOf(getIntent().getSerializableExtra("DaAllowance"));


        imgBck = findViewById(R.id.imag_back);
        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        Datotal.setText("Rs." + String.valueOf(getIntent().getSerializableExtra("DaAll_Total")) + ".00");
        Log.v("JSONARRA_Y", DaClaim);
        try {
            JSONArray jsonArray = new JSONArray(DaClaim);
            Log.v("JSONARRAY", jsonArray.toString());


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = (JSONObject) jsonArray.get(i);
                dailyAllowance.setText("" + js.getString("all_name"));
                modeAmount.setText("Rs." + js.getString("brd_amt") + ".00");
                DrvBrdAmt.setText("Rs." + js.getString("drvBrdAmt") + ".00");
                DrvBrdAmt.setText("Rs." + js.getString("drvBrdAmt") + ".00");

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

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    DAClaimActivity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {
    }
}