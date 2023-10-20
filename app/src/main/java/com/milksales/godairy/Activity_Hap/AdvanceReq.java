package com.milksales.godairy.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Common_Model;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Interface.Master_Interface;
import com.milksales.godairy.R;
import com.milksales.godairy.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvanceReq extends AppCompatActivity implements Master_Interface {
    private static String Tag = "HAP_Check-In";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;

    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";

    Gson gson;

    DatabaseHandler db;

    Common_Class common_class;
    CustomListViewDialog customDialog;
    List<Common_Model> modelAdvType = new ArrayList<>();

    DatePickerDialog picker;
    TextView AdvType;
    EditText eText,Setl_date;
    EditText etext2,AdvAmt,txLocation,txPurpose;
    Button Submit;

    String minDate = "",AdvTypeVal="",eKey="";
    String minYear = "", minMonth = "", minDay = "";
    String tominYear = "", tominMonth = "", tominDay = "";
    String fromData="", toData="", maxTWoDate = "",Setldate="";
    String frmDte = "",flCOff="0",sWrkDt="";
    int AutoPost=0,MaxDays=0;

    com.milksales.godairy.Activity_Hap.Common_Class DT = new com.milksales.godairy.Activity_Hap.Common_Class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_req);

        db = new DatabaseHandler(this);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        common_class = new Common_Class(this);
        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calobjw = Calendar.getInstance();
        eKey =   UserDetails.getString("Sfcode","-") + dfw.format(calobjw.getTime()).hashCode();


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
                openHome();
            }
        });
        gson = new Gson();
        getAdvanceTypes();
        eText = (EditText) findViewById(R.id.from_date);
        eText.setInputType(InputType.TYPE_NULL);
        MaxMinDate();

        etext2 = (EditText) findViewById(R.id.to_date);
        Setl_date = (EditText) findViewById(R.id.Setl_date);
        txLocation = (EditText) findViewById(R.id.edt_location);
        txPurpose = (EditText) findViewById(R.id.edt_Purpose);
        Submit = (Button) findViewById(R.id.submitButton);
        Setl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdvanceReq.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Setl_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Setldate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(year, month, day);
                picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                picker.show();

            }
        });

        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("EDIT_TEXT", eText.getText().toString());
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AdvanceReq.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fromData = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                maxTWoDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                MaxMinDateTo(maxTWoDate);

                                if (TextUtils.isEmpty(eText.getText().toString())) {
                                    Log.v("EDITEXT_VALUE", "");
                                } else {

                                    etext2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final Calendar cldr = Calendar.getInstance();

                                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                                            int month = cldr.get(Calendar.MONTH);
                                            int year = cldr.get(Calendar.YEAR);
                                            // date picker dialog
                                            picker = new DatePickerDialog(AdvanceReq.this,
                                                    new DatePickerDialog.OnDateSetListener() {
                                                        @Override
                                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                            etext2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                                            toData =  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                                        }
                                                    }, year, month, day);
                                            Calendar calendarmin = Calendar.getInstance();
                                            calendarmin.set(Integer.parseInt(tominYear), Integer.parseInt(tominMonth) - 1, Integer.parseInt(tominDay));
                                            picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                                            picker.show();
                                        }
                                    });
                                }


                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                int mnth = Integer.parseInt(minMonth) - 2;
                int fYr = Integer.parseInt(minYear);
                if (mnth < 0) {
                    mnth = 11;
                    fYr = fYr - 1;
                }
                Log.d("MINMonth", String.valueOf(mnth));
                calendarmin.set(fYr, mnth, 23);
                picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());

                picker.show();

            }
        });

        AdvType = findViewById(R.id.Adv_type);
        AdvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(AdvanceReq.this, modelAdvType, 4);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

        AdvAmt = findViewById(R.id.AdvAmt);
        AdvAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (AdvAmt.getText().toString().equalsIgnoreCase("0.00") || AdvAmt.getText().toString().equalsIgnoreCase("0.00")) {
                        AdvAmt.setText("");
                    }
                } else {
                    if (AdvAmt.getText().toString().equalsIgnoreCase("")) {
                        AdvAmt.setText("0.00");
                    }
                }
            }
        });
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Leave_Dashboard.class);
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitData();
            }
        });
    }
    public void getAdvanceTypes() {

        if (common_class.isNetworkAvailable(this)) {
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("get/Advtypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        modelAdvType.clear();
                        try {
                            JSONArray AdvTyps=new JSONArray( response.body().toString());
                            for (int i = 0; i < AdvTyps.length(); i++) {
                                JSONObject iobj=AdvTyps.getJSONObject(i);
                                String id = iobj.getString("id");
                                String name = iobj.getString("name");
                                Common_Model item = new Common_Model(id, name, "","");
                                modelAdvType.add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void MaxMinDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println("Current_DATE_FORMAT" + formatter.format(date));

        String strMinDate = formatter.format(date);
        minDate = strMinDate;
        /*Min Date*/
        String[] separated1 = minDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        minYear = separated1[0];
        minMonth = separated1[1];
        minDay = separated1[2];

    }
    public void MaxMinDateTo(String strMinDate) {
        Log.e("MAX_DATE_TWO", " " + strMinDate);

        String[] separated1 = strMinDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        tominYear = separated1[0];
        tominMonth = separated1[1];
        tominDay = separated1[2];
    }
    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
        Log.d(Tag, String.valueOf(CheckIn));

        if (CheckIn == true) {
            Intent Dashboard = new Intent(AdvanceReq.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }
    public Boolean validate(){
        if(fromData.equalsIgnoreCase("")){
            Toast.makeText(this,"Select the From Date",Toast.LENGTH_LONG).show();
            return false;
        }
        if(toData.equalsIgnoreCase("")){
            Toast.makeText(this,"Select the To Date",Toast.LENGTH_LONG).show();
            return false;
        }
        if(AdvTypeVal.equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Select the Advance Type",Toast.LENGTH_LONG).show();
            return false;
        }
        if(txLocation.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Enter the Location",Toast.LENGTH_LONG).show();
            return false;
        }
        if(txPurpose.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Enter the Purpose",Toast.LENGTH_LONG).show();
            return false;
        }
        String sAmt=AdvAmt.getText().toString();
        if(sAmt.equalsIgnoreCase("") || sAmt.equalsIgnoreCase("0.00"))
        {
            Toast.makeText(this,"Enter the Advance Amount",Toast.LENGTH_LONG).show();
            return false;
        }

        if(Setldate.equalsIgnoreCase("")){
            Toast.makeText(this,"Select the Settlement Date",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void SubmitData(){
        if(!validate()) return;
        JSONArray jsonArray=new JSONArray();
        JSONObject param=new JSONObject();

        String sTime=DT.GetDateTime(AdvanceReq.this,"yyyy-MM-dd HH:mm:ss");

        try {
            param.put("eKey",eKey);
            param.put("SF",UserDetails.getString("Sfcode",""));
            param.put("eDate", sTime);
            param.put("AdvFrom",fromData);
            param.put("AdvTo",toData);
            param.put("AdvTyp",AdvTypeVal);
            param.put("AdvLoc",txLocation.getText().toString());
            param.put("AdvPurp",txPurpose.getText().toString());
            String sAmt=AdvAmt.getText().toString();
            param.put("AdvAmt",sAmt);
            param.put("AdvSettle",Setldate);
            jsonArray.put(param);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("save/advance",param.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JSONArray res=new JSONArray(response.body().toString());
                        JSONObject itm=res.getJSONObject(0);
                        Toast.makeText( AdvanceReq.this,itm.getString("Msg"),Toast.LENGTH_LONG).show();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText( AdvanceReq.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText( AdvanceReq.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText( AdvanceReq.this,"Data Error: " +e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 4) {
            AdvType.setText(myDataset.get(position).getName());
            AdvTypeVal = myDataset.get(position).getId();
        }
    }
}