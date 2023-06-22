package com.saneforce.milksales.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
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

public class LocalConvenActivity extends AppCompatActivity {

    LinearLayout linlocalCon;
    TextView editTexts, txtLCAmnt, lcUKey;
    EditText edtLcFare, edt;
    LinearLayout Dynamicallowance;
    String LCClaim = "", LcUKey = "";
    JSONArray jsonArray = null;
    ImageView imgBck, localImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_conven);
        linlocalCon = findViewById(R.id.lin_dyn_local_con);
        txtLCAmnt = findViewById(R.id.txt_local);
        LCClaim = String.valueOf(getIntent().getSerializableExtra("LCAllowance"));
        txtLCAmnt.setText("Rs." + String.valueOf(getIntent().getSerializableExtra("LCAll_Total")) + ".00");
        String.valueOf(getIntent().getSerializableExtra("date"));
        Log.v("JSON_LC", LCClaim);
        getToolbar();
        imgBck = findViewById(R.id.imag_back);
        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        try {
            jsonArray = new JSONArray(LCClaim);
            localConDraft(jsonArray);
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
                    LocalConvenActivity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {
    }


    public void localConDraft(JSONArray lcDraft) {


        JSONArray jsonAddition = null;
        JSONObject lcdraftJson = null;
        for (int i = 0; i < lcDraft.length(); i++) {

            try {
                lcdraftJson = (JSONObject) lcDraft.get(i);
                jsonAddition = lcdraftJson.getJSONArray("Additional");
                String expCode = String.valueOf(lcdraftJson.get("Exp_Code"));
                expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                LinearLayout.LayoutParams layoutParams = new LinearLayout
                        .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 15, 15, 15);
                View rowView = inflater.inflate(R.layout.local_claim_approval, null);

                linlocalCon.addView(rowView, layoutParams);
                int lcS = linlocalCon.getChildCount() - 1;

                View view = linlocalCon.getChildAt(i);
                editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                edtLcFare = (EditText) (view.findViewById(R.id.edt_la_fare));
                lcUKey = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                localImage = view.findViewById(R.id.img_prvw_lc);
                localImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer Intr = linlocalCon.indexOfChild(rowView);

                        View view = linlocalCon.getChildAt(Intr);
                        editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                        lcUKey = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                        LcUKey = lcUKey.getText().toString();

                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", LcUKey);
                        stat.putExtra("headTravel", "LC");
                        stat.putExtra("Delete", "1");
                        stat.putExtra("mode", editTexts.getText().toString());
                        stat.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
                        startActivity(stat);

                        Log.v("POSITION_VALUES", editTexts.getText().toString());
                        Log.v("POSITION_VALUES", String.valueOf(getIntent().getSerializableExtra("date")));

                        Log.v("LC_POSITION", String.valueOf(Intr));

                    }
                });

                Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);

                localConDisplay(expCode, jsonAddition, lcS);
                editTexts.setText(expCode);
                edtLcFare.setText("" + lcdraftJson.get("Exp_Amt").toString());
                lcUKey.setText(""+lcdraftJson.get("Ukey").toString());

                Log.v("POSITION_VALUES", lcdraftJson.get("Ukey").toString());
                edtLcFare.setEnabled(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    @SuppressLint("ResourceType")
    public void localConDisplay(String modeName, JSONArray jsonAddition, int position) {

        JSONObject jsonObjectAdd = null;
        for (int l = 0; l < jsonAddition.length(); l++) {
            try {
                jsonObjectAdd = (JSONObject) jsonAddition.get(l);
                String edtValueb = String.valueOf(jsonObjectAdd.get("Ref_Value"));
                RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutparams_3.setMargins(20, -10, 0, 0);
                edt = new EditText(getApplicationContext());
                edt.setLayoutParams(layoutparams_3);
                edt.setText(edtValueb.replaceAll("^[\"']+|[\"']+$", ""));
                edt.setId(12345);
                edt.setTextSize(13);
/*
                edt.setBackground(null);
*/
                edt.setEnabled(false);
                childRel.addView(edt);
                View view = linlocalCon.getChildAt(position);
                Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
                Dynamicallowance.addView(childRel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}