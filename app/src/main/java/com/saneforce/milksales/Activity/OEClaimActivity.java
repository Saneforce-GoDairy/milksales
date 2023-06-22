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

public class OEClaimActivity extends AppCompatActivity {

    LinearLayout LinearOtherAllowance, otherExpenseLayout;
    TextView oeEditext, oeAmt, oeTxtUKey;
    EditText edtOE;
    String OEClaim = "", oEUKey = "";
    JSONArray jsonArray = null;
    ImageView imgBck, imgPrv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_e_claim);

        LinearOtherAllowance = findViewById(R.id.lin_dyn_other_Expense);
        otherExpenseLayout = findViewById(R.id.lin_total_other);
        oeAmt = findViewById(R.id.txt_oe);
        OEClaim = String.valueOf(getIntent().getSerializableExtra("OEAllowance"));
        oeAmt.setText(String.valueOf(getIntent().getSerializableExtra("OEAll_Total")));

        getToolbar();
        imgBck = findViewById(R.id.imag_back);
        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        Log.v("JSON_LC", OEClaim);

        try {
            jsonArray = new JSONArray(OEClaim);
            OeDraft(jsonArray);
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
                    OEClaimActivity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {
    }


    public void OeDraft(JSONArray oEDraft) {

        for (int i = 0; i < oEDraft.length(); i++) {

            JSONObject oejsonArray = null;
            try {
                oejsonArray = (JSONObject) oEDraft.get(i);
                String expCode = oejsonArray.getString("Exp_Code");

                expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                View rowView = inflater.inflate(R.layout.oe_claim_approval, null);
                LinearOtherAllowance.addView(rowView, layoutParams);

                View childView = LinearOtherAllowance.getChildAt(i);
                otherExpenseLayout.setVisibility(View.VISIBLE);
                oeEditext = (TextView) (childView.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (childView.findViewById(R.id.oe_fre_amt));
                oeTxtUKey = (TextView) (childView.findViewById(R.id.txt_oe_ukey));
                imgPrv = (ImageView) childView.findViewById(R.id.img_prvw_oe);
                imgPrv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer Intr = LinearOtherAllowance.indexOfChild(rowView);

                        View view = LinearOtherAllowance.getChildAt(Intr);
                        oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                        edtOE = (EditText) (view.findViewById(R.id.oe_fre_amt));
                        oeTxtUKey = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                        oEUKey = oeTxtUKey.getText().toString();

                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", oEUKey);
                        stat.putExtra("headTravel", "OE");
                        stat.putExtra("Delete", "1");
                        stat.putExtra("mode", oeEditext.getText().toString());
                        stat.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
                        startActivity(stat);

                        Log.v("POSITION_VALUES", oeEditext.getText().toString());
                        Log.v("POSITION_VALUES", String.valueOf(getIntent().getSerializableExtra("date")));

                        Log.v("LC_POSITION", String.valueOf(Intr));
                    }
                });

                oeEditext.setText(expCode);
                edtOE.setText(oejsonArray.getString("Exp_Amt"));
                oeTxtUKey.setText(oejsonArray.getString("Ukey"));
                oeEditext.setEnabled(false);
                edtOE.setEnabled(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}