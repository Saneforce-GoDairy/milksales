package com.saneforce.milksales.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.Activity_Hap.AttachementActivity;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class LodgingCliamActivity extends AppCompatActivity {

    LinearLayout linImgPrv, jointLodging;
    String LCClaim = "";
    JSONArray jsonArray = null;

    TextView  txt_ldg_type, edt_ldg_JnEmp, ldg_cin, ldg_cout, txtJNName, txtJNDesig, txtJNDept, txtJNHQ, txtJNMob,
            lblHdBill, lblHdBln, ldgWOBBal, txtJNMyEli, txtMyEligi, txtDrivEligi, lbl_ldg_eligi, txtJNEligi,
            txtStyDays, txtLodgUKey;
    EditText edt_ldg_bill, lodgStyLocation;
    ImageView imgBck,img_lodg_prvw, img_lodg_atta;

    String DateTime = "";


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodging_cliam);

        ldg_cin = findViewById(R.id.from_picker);
        ldg_cout = findViewById(R.id.to_picker);
        lodgStyLocation = findViewById(R.id.edt_stay_loc);
        txtLodgUKey = findViewById(R.id.log_ukey);

        linImgPrv = findViewById(R.id.lin_img_prv);
        txtMyEligi = findViewById(R.id.txtMyEligi);
        txtDrivEligi = findViewById(R.id.txtDrvLgd);
        lbl_ldg_eligi = findViewById(R.id.lbl_ldg_eligi);
        lblHdBill = findViewById(R.id.lblHdBill);
        lblHdBln = findViewById(R.id.lblHdBln);
        ldgWOBBal = findViewById(R.id.ldgWOBBal);
        edt_ldg_bill = findViewById(R.id.edt_ldg_bill);
        img_lodg_prvw = findViewById(R.id.lodg_preview);
        img_lodg_atta = findViewById(R.id.lodg_attach);
        txtJNEligi = findViewById(R.id.txtJNEligi);
        txt_ldg_type = findViewById(R.id.ldg_typ);
        jointLodging = findViewById(R.id.lin_join_person);
        txtStyDays = findViewById(R.id.txt_stay_total);

        LCClaim = String.valueOf(getIntent().getSerializableExtra("lodgAllowance"));

        String.valueOf(getIntent().getSerializableExtra("date"));

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
            lodging(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void lodging(JSONArray lodingDraft) {


        for (int i = 0; i < lodingDraft.length(); i++) {


            try {

                JSONArray jsonAddition = null;
                JSONObject ldraft = null;

                ldraft = (JSONObject) lodingDraft.get(i);
                ldg_cin.setText(String.valueOf(ldraft.get("Stay_Date")));
                ldg_cout.setText(String.valueOf(ldraft.get("To_Date")));
                lodgStyLocation.setText(String.valueOf(ldraft.get("Ldg_Stay_Loc")));

                jsonAddition = ldraft.getJSONArray("Additional");


                Log.v("JSON_ADITION", String.valueOf(jsonAddition.length()));

                Double totlLdgAmt = Double.valueOf(String.valueOf(ldraft.get("Total_Ldg_Amount")));
                Integer noday = Integer.valueOf(String.valueOf(ldraft.get("NO_Of_Days")));

                txtLodgUKey.setText(String.valueOf(ldraft.get("Ukey")));
                double elibs = Integer.valueOf(String.valueOf(ldraft.get("Eligible")));
                /*            double elibs = elib * noday;*/

                txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(elibs));

                double srtjdgAmt = Integer.valueOf(String.valueOf(ldraft.get("Joining_Ldg_Amount")));
                txtJNEligi.setText("Rs." + new DecimalFormat("##0.00").format(srtjdgAmt));
                Double wobal = Double.valueOf(String.valueOf(ldraft.get("WOB_Amt")));

                Log.v("ldgWOBBal", String.valueOf(wobal));
                ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(wobal));

                Log.v("ldgWOBBal_______", ldgWOBBal.getText().toString());

                edt_ldg_bill.setText(String.valueOf(ldraft.get("Bill_Amt")));
                lbl_ldg_eligi.setText("Rs." + new DecimalFormat("##0.00").format(totlLdgAmt));

                txtStyDays.setText(String.valueOf(ldraft.get("NO_Of_Days")));

                if (String.valueOf(ldraft.get("Lodging_Type")).equals("Joined Stay")) {
                    txt_ldg_type.setText("Joined Stay");
                } else if (String.valueOf(ldraft.get("Lodging_Type")).equals("Independent Stay")) {
                    txt_ldg_type.setText("Independent Stay");

                } else {
                    txt_ldg_type.setText("Stay At Relative's House");
                }

                img_lodg_prvw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", txtLodgUKey.getText().toString());
                        stat.putExtra("headTravel", "LOD");
                        stat.putExtra("mode", "Room");
                        stat.putExtra("Delete", "1");
                        stat.putExtra("date", getIntent().getSerializableExtra("date"));
                        startActivity(stat);


                    }
                });


                JSONObject jsonObjectAdd = null;
                for (int l = 0; l < jsonAddition.length(); l++) {

                    jsonObjectAdd = (JSONObject) jsonAddition.get(l);

                    jointLodging.setVisibility(View.VISIBLE);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(15, 15, 15, 15);
                    View rowView = inflater.inflate(R.layout.activity_approval_lodg, null);
                    jointLodging.addView(rowView, layoutParams);

                    Integer jfd = jointLodging.indexOfChild(rowView);

                    View jdV = jointLodging.getChildAt(jfd);
                    edt_ldg_JnEmp = (EditText) jdV.findViewById(R.id.edt_ldg_JnEmp);
                    txtJNName = (TextView) jdV.findViewById(R.id.txtJNName);
                    txtJNDesig = (TextView) jdV.findViewById(R.id.txtJNDesig);
                    txtJNDept = (TextView) jdV.findViewById(R.id.txtJNDept);
                    txtJNHQ = (TextView) jdV.findViewById(R.id.txtJNHQ);
                    txtJNMob = (TextView) jdV.findViewById(R.id.txtJNMob);
                    txtJNMyEli = (TextView) jdV.findViewById(R.id.txtJNMyEli);

                    edt_ldg_JnEmp.setText(jsonObjectAdd.getString("Emp_Code"));
                    txtJNName.setText(jsonObjectAdd.getString("Sf_Name"));
                    txtJNDesig.setText(jsonObjectAdd.getString("Desig"));
                    txtJNDept.setText(jsonObjectAdd.getString("Dept"));
                    txtJNHQ.setText(jsonObjectAdd.getString("Sf_Hq"));
                    txtJNMob.setText(jsonObjectAdd.getString("Sf_Mobile"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                    LodgingCliamActivity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {
    }

}