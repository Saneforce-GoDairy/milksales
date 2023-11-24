package com.saneforce.godairy.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.MaxMinDate;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewJoinEntry extends AppCompatActivity {

    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";

    DatePickerDialog picker;
    EditText eText, remark;
    Gson gson;
    Button btnSubmit;

    Boolean CheckIn;
    List<MaxMinDate> maxMinDates;
    String maxYear = "", maxMonth = "", maxDay = "", minYear = "", minMonth = "", minDay = "", maxDate = "", minDate = "";
    ConstraintLayout ConstraintLayout;
    private String mUkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_join_entry);

        mUkey = Common_Class.GetEkey();

        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        TextView txtHelp = findViewById(R.id.toolbar_help);
        SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        CheckIn = CheckInDetails.getBoolean("CheckIn", false);
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
                CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));


            }
        });
        eText = (EditText) findViewById(R.id.edate);
        remark = (EditText) findViewById(R.id.reason_joinee);
        btnSubmit = (Button) findViewById(R.id.submitButton);
        gson = new Gson();

        Bundle params = getIntent().getExtras();
        if (params != null) {
            String[] stDt = params.getString("EDt").split("/");
            eText.setText(stDt[2] + "-" + stDt[1] + "-" + stDt[0]);
            eText.setEnabled(false);
        }

        MaxMinDate();

        ConstraintLayout = findViewById(R.id.constrain_newjoin);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWindow().getDecorView().clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ConstraintLayout.getWindowToken(), 0);
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewJoinEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                if (!TextUtils.isEmpty(minYear) && !TextUtils.isEmpty(minMonth) && !TextUtils.isEmpty(minDay)) {
                    calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                    Calendar calendarmax = Calendar.getInstance();
                    calendarmax.set(Integer.parseInt(maxYear), Integer.parseInt(maxMonth) - 1, Integer.parseInt(maxDay));
                    picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                    picker.getDatePicker().setMaxDate(calendarmax.getTimeInMillis());
                    picker.show();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(eText.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(NewJoinEntry.this, "Select the date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(remark.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(NewJoinEntry.this, "Select the Remarks", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonleaveType = new JSONObject();
                JSONObject jsonleaveTypeS = new JSONObject();
                JSONArray jsonArray1 = new JSONArray();
                try {
                    jsonleaveType.put("Ukey", mUkey);
                    jsonleaveType.put("From_Date", eText.getText().toString());
                    jsonleaveType.put("Reason", remark.getText().toString());
                    jsonleaveTypeS.put("NewJoinForm", jsonleaveType);
                    jsonArray1.put(jsonleaveTypeS);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String leaveCap1 = jsonleaveType.toString();
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("save/newjoin",Shared_Common_Pref.Div_Code,Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode,"",leaveCap1).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        JsonArray jsonArr = response.body();
                        JsonObject jsonObjecta = jsonArr.get(0).getAsJsonObject();
                        Log.e("TOTAL_REPOSNEaaa", String.valueOf(jsonObjecta));
                        String Msg = jsonObjecta.get("Msg").getAsString();
                        if (!Msg.equalsIgnoreCase("")) {
                            AlertDialogBox.showDialog(NewJoinEntry.this, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    if (jsonObjecta.get("success").getAsBoolean() == true) openHome();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
            }
        });
    }

    /*getMax and Min date*/
    public void MaxMinDate() {

                minDate = UserDetails.getString("NwJoinDate","");
                maxDate = UserDetails.getString("NwJoinMxDate","");

                /*MAx Date*/
                String[] separated = maxDate.replace(" ","-").split("-");
                separated[0] = separated[0].trim();
                separated[1] = separated[1].trim();
                separated[2] = separated[2].trim();

                maxYear = separated[0];
                maxMonth = separated[1];
                maxDay = separated[2];

                /*Min Date*/
                String[] separated1 = minDate.replace(" ","-").split("-");
                separated1[0] = separated1[0].trim();
                separated1[1] = separated1[1].trim();
                separated1[2] = separated1[2].trim();

                minYear = separated1[0];
                minMonth = separated1[1];
                minDay = separated1[2];
    }

    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

        if (CheckIn == true) {
            Intent Dashboard = new Intent(NewJoinEntry.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

}