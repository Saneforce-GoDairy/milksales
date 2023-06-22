package com.saneforce.milksales.Activity;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity_Hap.Dashboard;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.LocationEvents;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.LocationFinder;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaApprovalDisplay extends AppCompatActivity {

    TextView txtTaAmt, txtDate, txtName, txtTotalAmt, txtHQ, txtTrvlMode, txtDesig, txtDept, txtDA,
            txtTL, txtLA, txtLC, txtOE, txtReject, txtEmpId, txtMobile;
    Common_Class common_class;
    Shared_Common_Pref mShared_common_pref;
    String date = " ", SlStart = "", TotalAmt = "", sfCode = "", STEND = "", SDA = "", SLC = "", SOE = "", stImg = "",
            endImg = "", lodgTotal = "";
    LinearLayout linAccept, linReject;
    AppCompatEditText appCompatEditText;
    JsonArray jsonArray = null, jsonTravDetai = null, lcDraftArray = null, oeDraftArray = null, trvldArray = null, ldArray = null,
            daArray = null;
    Double ldgTtl, brd, ta, ldg, oe, lc, trv_lc;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_approval_display);
        mShared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        date = String.valueOf(getIntent().getSerializableExtra("date"));
        Log.v("datedate", date);


        txtDate = findViewById(R.id.txt_date);
        txtTotalAmt = findViewById(R.id.txt_amt);
        txtName = findViewById(R.id.txt_Name);
        txtHQ = findViewById(R.id.txt_hq);
        txtDesig = findViewById(R.id.txt_desg);
        txtDept = findViewById(R.id.txt_dep);
        txtTrvlMode = findViewById(R.id.txt_mde);
        linAccept = findViewById(R.id.lin_accp);
        linReject = findViewById(R.id.rejectonly);
        txtReject = findViewById(R.id.L_rejectsave);
        txtTaAmt = findViewById(R.id.txt_tvrl_amt);
        txtEmpId = findViewById(R.id.txt_emp_id);
        txtMobile = findViewById(R.id.txt_moble);

        appCompatEditText = findViewById(R.id.reason);
        txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendtpApproval(2);
            }
        });

        txtDA = findViewById(R.id.txt_da);
        txtTL = findViewById(R.id.txt_tl);
        txtLA = findViewById(R.id.txt_la);
        txtLC = findViewById(R.id.txt_lc);
        txtOE = findViewById(R.id.txt_oe);
        txtLA.setText("Rs. 0.0");

        txtDate.setText(String.valueOf(getIntent().getSerializableExtra("date")));

        txtName.setText(String.valueOf(getIntent().getSerializableExtra("name")));
        txtHQ.setText(String.valueOf(getIntent().getSerializableExtra("head_quaters")));
        txtDesig.setText(String.valueOf(getIntent().getSerializableExtra("desig")));
        txtDept.setText(String.valueOf(getIntent().getSerializableExtra("dept")));
        txtTrvlMode.setText(String.valueOf(getIntent().getSerializableExtra("travel_mode")));

        txtEmpId.setText(String.valueOf(getIntent().getSerializableExtra("sf_emp_id")));
        txtMobile.setText(String.valueOf(getIntent().getSerializableExtra("SF_Mobile")));


        SlStart = String.valueOf(getIntent().getSerializableExtra("Sl_No"));
        sfCode = String.valueOf(getIntent().getSerializableExtra("sfCode"));


        Double total = Double.parseDouble(String.valueOf(getIntent().getSerializableExtra("total_amount")));

        txtTotalAmt.setText("Rs. " + total);

        getTAList(String.valueOf(getIntent().getSerializableExtra("date")), String.valueOf(getIntent().getSerializableExtra("sfCode")));
        Log.e("SFCode", String.valueOf(getIntent().getSerializableExtra("total_amount")));


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
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(TAApprovalActivity.class);

            }
        });

        displayTravelMode(String.valueOf(getIntent().getSerializableExtra("date")));

    }

    public void DaApproval(View v) {

        /*  brd,ta,ldg,oe,lc,trv_lc*/
        if (!brd.equals("0.0")) {
            Intent intent = new Intent(getApplicationContext(), DAClaimActivity.class);
            intent.putExtra("DaAllowance", daArray.toString());
            intent.putExtra("DaAll_Total", SDA);
            intent.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
            startActivity(intent);
        }
    }

    public void TLApproval(View v) {

        if (!trv_lc.equals("0.0")) {
            Intent intent = new Intent(getApplicationContext(), TL_cliam_Apprval.class);
            intent.putExtra("strEnd", STEND);
            intent.putExtra("TLAllowance", trvldArray.toString());
            intent.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
            startActivity(intent);
        }
    }

    public void OEApproval(View v) {
        if (!oe.equals("0.0")) {
            Intent intent = new Intent(getApplicationContext(), OEClaimActivity.class);
            intent.putExtra("OEAllowance", oeDraftArray.toString());
            intent.putExtra("OEAll_Total", SOE);
            intent.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
            startActivity(intent);
        }
    }

    public void FuelApproval(View v) {
/*
        if (!txtTL.getText().equals("Rs. 0.0")) {*/
        Intent intent = new Intent(getApplicationContext(), FuelAllowance.class);
        intent.putExtra("jsonTravDetai", jsonTravDetai.toString());
        intent.putExtra("start_Photo", stImg);
        intent.putExtra("End_photo", endImg);
        intent.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
        startActivity(intent);
        /*    }*/
    }

    public void LCApproval(View v) {
        if (!lc.equals("0.0")) {
            Intent intent = new Intent(getApplicationContext(), LocalConvenActivity.class);
            intent.putExtra("LCAllowance", lcDraftArray.toString());
            intent.putExtra("LCAll_Total", SLC);
            intent.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
            startActivity(intent);
        }
    }

    public void LAApproval(View v) {
        if (!txtLA.getText().equals("Rs. 0.0")) {
            Intent intent = new Intent(getApplicationContext(), LodgingCliamActivity.class);
            intent.putExtra("lodgAllowance", ldArray.toString());
            intent.putExtra("lodgAll_Total", lodgTotal);
            intent.putExtra("date", String.valueOf(getIntent().getSerializableExtra("date")));
            startActivity(intent);
        }
    }


    public void getTAList(String Date, String sFCode) {
        Log.v("datedatefgdfgd", Date);
        JSONObject taReq = new JSONObject();
        try {
            taReq.put("sfCode", sFCode);
            taReq.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            taReq.put("Selectdate", Date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("GET_TA_LIST", taReq.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> mTrave = apiInterface.getApprovalDisplay(taReq.toString());
        mTrave.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int m = 0; m < jsonArray.size(); m++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(m);

                    brd = Double.parseDouble(jsonObject.get("Boarding_Amt").getAsString());
                    ta = Double.parseDouble(jsonObject.get("Ta_totalAmt").getAsString());
                    ldg = Double.parseDouble(jsonObject.get("Ldg_totalAmt").getAsString());
                    lc = Double.parseDouble(jsonObject.get("Lc_totalAmt").getAsString());
                    oe = Double.parseDouble(jsonObject.get("Oe_totalAmt").getAsString());
                    trv_lc = Double.parseDouble(jsonObject.get("trv_lc_amt").getAsString());


                    Log.v("txtTLtxtTL", String.valueOf(ta));

                    txtDA.setText("Rs. " + brd);
                    txtTL.setText("Rs. " + ta);

                    txtLC.setText("Rs. " + lc);
                    txtOE.setText("Rs. " + oe);
                    txtTaAmt.setText("Rs. " + trv_lc);
                    SDA = jsonObject.get("Boarding_Amt").getAsString();
                    SLC = jsonObject.get("Lc_totalAmt").getAsString();
                    SOE = jsonObject.get("Oe_totalAmt").getAsString();

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    TaApprovalDisplay.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {
    }

    public void onApproval(View v) {
        new LocationFinder(getApplication(), new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                SendtpApproval(1);
            }
        });
    }

    public void onReject(View v) {
        linAccept.setVisibility(View.GONE);
        linReject.setVisibility(View.VISIBLE);
    }


    private void SendtpApproval(int flag) {
        JSONObject taReq = new JSONObject();

        try {
            taReq.put("login_sfCode", UserDetails.getString("Sfcode", ""));
            taReq.put("sfCode", sfCode);
            taReq.put("Flag", flag);
            taReq.put("Sl_No", SlStart);
            taReq.put("AAmount", TotalAmt);
            taReq.put("Reason", appCompatEditText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TA_REQ", taReq.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.taApprove(taReq.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                try {
                    common_class.CommonIntentwithFinish(TAApprovalActivity.class);
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "TA  Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "TA Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public void displayTravelMode(String Date) {


        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", sfCode);
            jj.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("Selectdate", Date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("JSON_VALUE", jj.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.getTAdateDetails(jj.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjects = response.body();
                Log.v("TA_APPROVAl_DISPLAY", jsonObjects.toString());
                jsonArray = jsonObjects.getAsJsonArray("TodayStart_Details");
                jsonTravDetai = jsonObjects.getAsJsonArray("Travelled_Details");
                lcDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseLC");
                oeDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseOE");
                trvldArray = jsonObjects.getAsJsonArray("Travelled_Loc");
                ldArray = jsonObjects.getAsJsonArray("Lodging_Head");
                daArray = jsonObjects.getAsJsonArray("Da_Claim");

                Log.v("JSON_ARRAY", jsonArray.toString());
                Log.v("jsonTravDetai", jsonTravDetai.toString());
                Log.v("lcDraftArray", lcDraftArray.toString());
                Log.v("oeDraftArray", oeDraftArray.toString());
                Log.v("trvldArray", trvldArray.toString());
                Log.v("ldArray", ldArray.toString());
                Log.v("daArray", daArray.toString());

                JsonObject jsLdg = null;
                for (int z = 0; z < ldArray.size(); z++) {
                    jsLdg = (JsonObject) ldArray.get(z);
                    ldgTtl = Double.parseDouble(jsLdg.get("Total_Ldg_Amount").getAsString());

                    txtLA.setText("Rs." + ldgTtl);
                    lodgTotal = jsLdg.get("Total_Ldg_Amount").getAsString();
                }


                JsonObject jsonObject = null;
                for (int i = 0; i < jsonArray.size(); i++) {
                    jsonObject = (JsonObject) jsonArray.get(i);
                    STEND = jsonObject.get("StEndNeed").getAsString();
                    stImg = jsonObject.get("start_Photo").getAsString();
                    endImg = jsonObject.get("End_photo").getAsString();
                }

            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }
}