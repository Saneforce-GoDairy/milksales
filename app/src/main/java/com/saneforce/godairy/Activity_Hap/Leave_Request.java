package com.saneforce.godairy.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Model_Class.Leave_Type;
import com.saneforce.godairy.Model_Class.MaxMinDate;
import com.saneforce.godairy.Model_Class.RemainingLeave;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.adapters.HAPListItem;
import com.saneforce.godairy.adapters.LeaveRemaining;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Leave_Request extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    private static String Tag = "HAP_Check-In";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    DatePickerDialog picker;
    EditText eText;
    EditText etext2;
    EditText etext3;
    int daysBetween;
    Gson gson;
    List<Leave_Type> leavetypelist;
    Type userType;
    Button Submit;
    String leavetype_id = "";
    String fromData, toData, maxTWoDate = "";
    String daysDifferce;
    CheckBox mHalfCheck;
    private ArrayList<String> shitList, halfTypeList;
    LinearLayout mCheckHalf, mShitTiming, mHalfDayType,WrkDt_linear,lin_coffWrk;
    RecyclerView mRecycleLeaveRemaining;
    List<RemainingLeave> mRemainingLeaves;
    LeaveRemaining leaveRemaining;
    EditText reasonForLeave;
    String shiftTypeVal = "", halfTypeVal = "", halfChecked, getReason;
    Boolean oneTwo = false;

    TextView shitType, leaveType, halfType,WrkDt,wrkCin,wrkCOut,wrkHrs,wrkEligi;
    CustomListViewDialog customDialog;
    List<Common_Model> modelShiftType = new ArrayList<>();
    List<Common_Model> modelWrkDt = new ArrayList<>();
    List<Common_Model> modelleaveType = new ArrayList<>();
    List<Common_Model> modelhalfdayType = new ArrayList<>();
    Common_Model Model_Pojo;
    List<MaxMinDate> maxMinDates;
    String minDate = "";
    String minYear = "", minMonth = "", minDay = "";
    String tominYear = "", tominMonth = "", tominDay = "";
    String frmDte = "",flCOff="0",sWrkDt="";
    int AutoPost=0,MaxDays=0;
    private JsonArray WrkedDys = new JsonArray();
    HAPListItem lstWrkDts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave__request);

        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

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
        eText = (EditText) findViewById(R.id.from_date);
        eText.setInputType(InputType.TYPE_NULL);
        MaxMinDate();

        etext2 = (EditText) findViewById(R.id.to_date);
        etext3 = (EditText) findViewById(R.id.no_of_days);
        Submit = (Button) findViewById(R.id.submitButton);

         reasonForLeave = (EditText) findViewById(R.id.reason_leave);
        mCheckHalf = (LinearLayout) findViewById(R.id.check_half_linear);
        mShitTiming = (LinearLayout) findViewById(R.id.shit_linear);
        mHalfDayType = (LinearLayout) findViewById(R.id.half_day_linear);
        lin_coffWrk= (LinearLayout) findViewById(R.id.coffWrk);
        mHalfCheck = (CheckBox) findViewById(R.id.check_half);

        WrkDt_linear = findViewById(R.id.WrkDt_linear);
        WrkDt = findViewById(R.id.WrkDt);
        wrkCin = findViewById(R.id.wrkCin);
        wrkCOut = findViewById(R.id.wrkCOut);
        wrkHrs = findViewById(R.id.wrkHrs);
        wrkEligi = findViewById(R.id.wrkEligi);

        Bundle params = getIntent().getExtras();
        if (params != null) {
            String Edt=params.getString("EDt","");
            String[] stDt;
            AutoPost=1;
            if(Edt.indexOf("/")>-1) {
                stDt = Edt.split("/");
                fromData = stDt[2] + "-" + stDt[1] + "-" + stDt[0];
                toData = stDt[2] + "-" + stDt[1] + "-" + stDt[0];

                eText.setText(params.getString("EDt"));
                etext2.setText(params.getString("EDt"));
            }else{

                fromData = Edt;
                toData = Edt;
                stDt = params.getString("EDt").split("-");
                String sEdt=stDt[2] + "/" + stDt[1] + "/" + stDt[0];
                eText.setText(sEdt);
                etext2.setText(sEdt);
            }
            difference();
            mCheckHalf.setVisibility(View.GONE);
            //eText.setText(stDt[2]+"-"+stDt[1]+"-"+stDt[0]);
            eText.setEnabled(false);
            etext2.setEnabled(false);
        }

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        lin_coffWrk.setVisibility(View.GONE);

        mRecycleLeaveRemaining = (RecyclerView) findViewById(R.id.leave_remaining);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleLeaveRemaining.setLayoutManager(layoutManager);

        leaveTypeMethod();

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = service.getDataArrayList("get/WrkDates",UserDetails.getString("Divcode",""),UserDetails.getString("Sfcode",""));
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                WrkedDys=response.body();
                Log.d("WorkedDays",String.valueOf(WrkedDys));
                for (int i = 0; i < WrkedDys.size(); i++) {
                    JsonObject jsonObject1 = WrkedDys.get(i).getAsJsonObject();
                    String id = jsonObject1.get("id").getAsString();
                    String name = jsonObject1.get("name").getAsString();
                    Common_Model item = new Common_Model(id, name, jsonObject1);
                    modelWrkDt.add(item);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

        WrkDt_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(Leave_Request.this, modelWrkDt, 7);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                /*JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(WrkedDys.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lstWrkDts= new HAPListItem(jsonArray,Leave_Request.this);*/

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
                picker = new DatePickerDialog(Leave_Request.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                difference();
                                fromData = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                maxTWoDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                MaxMinDateTo(maxTWoDate);
                                frmDte = eText.getText().toString();

                                if (TextUtils.isEmpty(eText.getText().toString())) {
                                    Log.v("EDITEXT_VALUE", frmDte);
                                } else {
                                    Log.v("EDITEXT_VALUE", frmDte);
                                    etext2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final Calendar cldr = Calendar.getInstance();

                                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                                            int month = cldr.get(Calendar.MONTH);
                                            int year = cldr.get(Calendar.YEAR);
                                            // date picker dialog
                                            picker = new DatePickerDialog(Leave_Request.this,
                                                    new DatePickerDialog.OnDateSetListener() {
                                                        @Override
                                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                            etext2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                                            difference();
                                                            toData = "'" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "'";
                                                        }
                                                    }, year, month, day);
                                            Calendar calendarmin = Calendar.getInstance();

                                            calendarmin.set(Integer.parseInt(tominYear), Integer.parseInt(tominMonth) - 1, Integer.parseInt(tominDay));
                                            picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                                            if(flCOff.equalsIgnoreCase("1"))
                                                calendarmin.add(Calendar.DAY_OF_YEAR, 0);
                                            else
                                                calendarmin.add(Calendar.DAY_OF_YEAR, MaxDays-1);

                                            picker.getDatePicker().setMaxDate(calendarmin.getTimeInMillis());
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


        leaveReaming();

        modelShiftType.clear();
        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String Scode = (shared.getString("Sfcode", "null"));
        String Dcode = (shared.getString("Divcode", "null"));
        spinnerValue("get/Shift_timing", Dcode, Scode);

        // addingShiftToSpinner();
        shitType = findViewById(R.id.shift_timing);
        shitType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(Leave_Request.this, modelShiftType, 4);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });


        /*adding spinner to leave type*/
        leaveType = findViewById(R.id.leave_type);
        leaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(Leave_Request.this, modelleaveType, 5);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });


        /*adding spinner to half day*/
        addingHalfToSpinner();
        halfType = findViewById(R.id.half_day_type);
        halfType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelhalfdayType.clear();

                for (int i = 0; i < halfTypeList.size(); i++) {
                    String name = String.valueOf(halfTypeList.get(i));

                    Model_Pojo = new Common_Model("id", name, "flag");
                    Log.e("LeaveType_Request", "id");
                    Log.e("LeaveType_Request", name);

                    modelhalfdayType.add(Model_Pojo);
                }

                customDialog = new CustomListViewDialog(Leave_Request.this, modelhalfdayType, 6);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("halfType", leavetype_id);
                if (leaveType.getText().toString().matches("")) {
                    Toast.makeText(Leave_Request.this, "Enter Leave Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(flCOff.equalsIgnoreCase("1")){
                    if (sWrkDt.equalsIgnoreCase("")) {
                        Toast.makeText(Leave_Request.this, "Select the worked date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(Float.parseFloat(etext3.getText().toString())>Float.parseFloat(wrkEligi.getText().toString())){
                        Toast.makeText(Leave_Request.this, leaveType.getText()+" Limit Exceed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(Float.parseFloat(etext3.getText().toString())>0.5 && Float.parseFloat(wrkEligi.getText().toString())!=1){
                        Toast.makeText(Leave_Request.this, "Allowed! Only Half Day", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (eText.getText().toString().matches("")) {
                    Toast.makeText(Leave_Request.this, "Enter From date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etext2.getText().toString().matches("")) {
                    Toast.makeText(Leave_Request.this, "Enter To date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (reasonForLeave.getText().toString().matches((""))) {
                    Toast.makeText(Leave_Request.this, "Enter Reason", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oneTwo == true) {
                    if (shitType.getText().toString().matches("")) {
                        Toast.makeText(Leave_Request.this, "Enter Shift Time", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (halfType.getText().toString().matches("")) {
                        Toast.makeText(Leave_Request.this, "Enter HalfDay Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (reasonForLeave.getText().toString().matches((""))) {
                    Toast.makeText(Leave_Request.this, "Enter Reason", Toast.LENGTH_SHORT).show();
                    return;
                }
                LeaveSubmitOne();

            }
        });
    }
    /*getMax and Min date*/

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

    public void addingHalfToSpinner() {
        halfTypeList = new ArrayList<>();
        halfTypeList.add("First Half");
        halfTypeList.add("Second Half");
    }

    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
        Log.d(Tag, String.valueOf(CheckIn));
        if (CheckIn) {
            Intent Dashboard = new Intent(Leave_Request.this, Dashboard_Two.class);
            Dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else {
            Intent intent = new Intent(Leave_Request.this, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    public void difference() {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateBefore = myFormat.parse(eText.getText().toString());
            Date dateAfter = myFormat.parse(etext2.getText().toString());
            long difference = dateAfter.getTime() - dateBefore.getTime();
            /*  float daysBetween = (difference / (1000*60*60*24));*/
            daysBetween = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
            System.out.println("Number of Days between dates: " + (daysBetween + 1));
            if (!etext2.getText().toString().equals(""))
            {
                if(mHalfCheck.isChecked())
                    etext3.setText(String.valueOf(0.5));
                else
                    etext3.setText("" + (daysBetween + 1));
            }
            if (daysBetween >= 0) {

            } else {
                etext2.setText("");
                etext3.setText("");
                Toast.makeText(this, "Please choose greater than from date" + "", Toast.LENGTH_SHORT).show();

            }

            daysDifferce = String.valueOf((daysBetween + 1));
            Log.e("DIFFERNCE_DATE", daysDifferce);
            if (daysDifferce.equals("1")) {
                mCheckHalf.setVisibility(View.VISIBLE);
                mHalfCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (mHalfCheck.isChecked()) {
                            etext3.setText(String.valueOf(0.5));
                            halfChecked = "1";
                            mShitTiming.setVisibility(View.VISIBLE);
                            mHalfDayType.setVisibility(View.VISIBLE);
                            oneTwo = true;
                        } else {
                            halfChecked = "0";
                            etext3.setText(String.valueOf(daysBetween + 1));
                            mShitTiming.setVisibility(View.GONE);
                            mHalfDayType.setVisibility(View.GONE);
                            oneTwo = false;
                        }
                    }
                });

            } else {
                mHalfCheck.setChecked(false);
                mCheckHalf.setVisibility(View.GONE);
                mShitTiming.setVisibility(View.GONE);
                mHalfDayType.setVisibility(View.GONE);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*Leave Type api call*/

    public void leaveTypeMethod() {

        modelleaveType.clear();
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        String commonLeaveType = "{\"tableName\":\"vwLeaveType\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObject(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.v("LEAVE_TYPE_CHEC", response.body().toString());
                userType = new TypeToken<ArrayList<Leave_Type>>() {
                }.getType();
                leavetypelist = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < leavetypelist.size(); i++) {
                    String id = String.valueOf(leavetypelist.get(i).getId());
                    String name = leavetypelist.get(i).getName();
                    String cOffType = String.valueOf(leavetypelist.get(i).getCOffType());
                    Integer MxDys = leavetypelist.get(i).getMaxDays();



                    Model_Pojo = new Common_Model(id, name, cOffType,MxDys);
                    modelleaveType.add(Model_Pojo);
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }


    /*Submit api checking leave status*/

    public void LeaveSubmitOne() {

        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            halfChecked = "0";
            if (mHalfCheck.isChecked()) {
                halfChecked = "1";
            }
            jsonleaveType.put("Leave_Type", leavetype_id);
            jsonleaveType.put("From_Date", fromData);
            jsonleaveType.put("To_Date", toData);
            jsonleaveType.put("Shift", shiftTypeVal);
            jsonleaveType.put("PChk", AutoPost);
            jsonleaveType.put("HalfDay_Type", halfTypeVal);
            jsonleaveType.put("HalfDay", halfChecked);
            jsonleaveType.put("Shift_Id", shiftTypeVal);

            jsonleaveType.put("value", sWrkDt);
            jsonleaveType.put("Intime", wrkCin.getText());
            jsonleaveType.put("Outime", wrkCOut.getText());
            jsonleaveType.put("NoofHrs", wrkHrs.getText());
            jsonleaveType.put("EligDys", wrkEligi.getText());
            jsonleaveType.put("Ekey", Common_Class.GetEkey());
            //  jsonleaveArrayType.put(jsonleaveType);
            jsonleaveTypeS.put("LeaveFormValidate", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String leaveCap = "[{\"LeaveFormValidate\":{\"Leave_Type\":\"'9'\",\"From_Date\":\"2020-11-02\",\"To_Date\":\"'2020-11-05'\",\"Shift\":\"0\",\"PChk\":0}}]";
        String leaveCap = jsonArray1.toString();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.leaveSubmit(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "MGR", leaveCap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjecta = response.body();
                String Msg = jsonObjecta.get("Msg").getAsString();

                if (Msg.equalsIgnoreCase("")) {
                    LeaveSubmitTwo();
                } else {
                    AlertDialogBox.showDialog(Leave_Request.this, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


    /*SUbmit Leave Request 2 */
    public void LeaveSubmitTwo() {
        getReason = "'" + reasonForLeave.getText().toString() + "'";
        String dateDiff = etext3.getText().toString();

        if (halfTypeVal == "First Half") {
            halfTypeVal = "1";
        } else if (halfTypeVal == "Second Half") {
            halfTypeVal = "2";
        } else {
            halfTypeVal = "3";
        }
        halfChecked = "0";
        if (mHalfCheck.isChecked()) {
            halfChecked = "1";
        }
        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("Leave_Type", leavetype_id);
            jsonleaveType.put("From_Date", fromData);
            jsonleaveType.put("To_Date", toData);
            jsonleaveType.put("Reason", getReason);
            jsonleaveType.put("address", "''");
            jsonleaveType.put("No_of_Days", dateDiff);
            jsonleaveType.put("HalfDay_Type", halfTypeVal);
            jsonleaveType.put("HalfDay", halfChecked);
            jsonleaveType.put("Shift_Id", shiftTypeVal);

            jsonleaveType.put("value", sWrkDt);
            jsonleaveType.put("Intime", wrkCin.getText());
            jsonleaveType.put("Outime", wrkCOut.getText());
            jsonleaveType.put("NoofHrs", wrkHrs.getText());
            jsonleaveType.put("EligDys", wrkEligi.getText());

            jsonleaveTypeS.put("LeaveForm", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String leaveCap1 = jsonArray1.toString();
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.leaveSubmit(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "MGR", leaveCap1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObjecta = response.body();
                Log.e("TOTAL_REPOSNEaaa", String.valueOf(jsonObjecta));
                String Msg = jsonObjecta.get("Msg").getAsString();
                if (!Msg.equalsIgnoreCase("")) {
                    AlertDialogBox.showDialog(Leave_Request.this, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


    /*Leave reamining*/
    public void leaveReaming() {

        int year = Calendar.getInstance().get(Calendar.YEAR);

        Log.v("CURRENT_YEAR", String.valueOf(year));
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        Call<Object> call = service.remainingLeave(String.valueOf(year), Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode);

        Log.e("REMAINING_LEAVES_RE", call.request().toString());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Type userType1 = new TypeToken<ArrayList<RemainingLeave>>() {
                }.getType();
                mRemainingLeaves = gson.fromJson(new Gson().toJson(response.body()), userType1);
                Log.e("REMAINING_LEAVES", String.valueOf(mRemainingLeaves));
                leaveRemaining = new LeaveRemaining(Leave_Request.this, mRemainingLeaves);
                mRecycleLeaveRemaining.setAdapter(leaveRemaining);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });

    @Override
    public void onBackPressed() {

    }


    private void spinnerValue(String a, String dc, String sc) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> shiftCall = apiInterface.getDataArrayList(a, dc, sc);
        shiftCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {


                String REPONSE = String.valueOf(response.body());


                //Log.e("ShiftTime_Leave_Request", REPONSE);
                try {
                    JSONArray jsonArray = new JSONArray(REPONSE);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.optString("id");
                        String name = jsonObject1.optString("name");
                        String flag = jsonObject1.optString("FWFlg");
                        Model_Pojo = new Common_Model(id, name, flag);
                        modelShiftType.add(Model_Pojo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 4) {
            shitType.setText(myDataset.get(position).getName());
            shiftTypeVal = myDataset.get(position).getId();
        } else if (type == 5) {
            leaveType.setText(myDataset.get(position).getName());
            leavetype_id = myDataset.get(position).getId();
            flCOff=myDataset.get(position).getFlag();
            MaxDays=myDataset.get(position).getMaxDays();
            lin_coffWrk.setVisibility(View.GONE);
            WrkDt.setText("");
            sWrkDt="";
            wrkCin.setText("");
            wrkCOut.setText("");
            wrkHrs.setText("");
            wrkEligi.setText("");
            mHalfCheck.setClickable(true);
            mHalfCheck.setChecked(false);
            if(flCOff.equalsIgnoreCase("1")){
                lin_coffWrk.setVisibility(View.VISIBLE);

            }
        } else if (type == 6) {
            halfType.setText(myDataset.get(position).getName());
        } else if (type == 7) {
           JsonObject item= myDataset.get(position).getJsonObject();
            WrkDt.setText(myDataset.get(position).getName());
            sWrkDt=item.get("Startdate").getAsString();
            wrkCin.setText(item.get("InTime").getAsString());
            wrkCOut.setText(item.get("OutTime").getAsString());
            wrkHrs.setText(item.get("NoofHrs").getAsString());
            wrkEligi.setText(item.get("EligDys").getAsString());
            float eli=Float.parseFloat(item.get("EligDys").getAsString());
            mHalfCheck.setClickable(true);
            mHalfCheck.setChecked(false);
            if(eli<1){
                mHalfCheck.setChecked(true);
                mHalfCheck.setClickable(false);
                etext3.setText("0.5");
            }
        }
    }
}