package com.saneforce.godairy.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Model_Class.AvalaibilityHours;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Permission_Request extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    private static String Tag = "HAP_Check-In";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    SharedPreferences Setups;

    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    public static final String SetupsInfo = "MySettings";

    TimePickerDialog picker;
    EditText eText, eText2;
    String DateSelection;
    DatePickerDialog picker1;
    EditText permsissionDate, takenHrs, hrsCurr, hrsAvail, reasonPermission;
    private ArrayList<String> shitList;
    Date d1 = null;
    Date d2 = null;
    String clickedDate, fromTime = "", toTime = "", FTime = "", TTime, Clicked, shiftTypeId = "";
    Integer differnce;
    Button buttonSubmit;
    List<AvalaibilityHours> mAvalaibilityHours;
    Gson gson;
    String takenLeave = "";
    String ShiftName;
    JsonObject aSetups;
    int TotHrs, AvlHrs, tknHrs;

    String shiftFromDate, shiftToDate;
    private static String HOUR_FORMAT = "HH:mm";


    TextView shitType;
    CustomListViewDialog customDialog;
    List<Common_Model> modelShiftType = new ArrayList<>();
    List<Common_Model> permissionSelectHours = new ArrayList<>();
    Common_Model Model_Pojo;

    String maxDate, minDate;
    String maxYear, maxMonth, maxDay, minYear, minMonth, minDay, StringFromTinme = "", StringPremissonEntry = "", TOTime = "";
    TextView PermissionHours;

    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission__request);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        Setups = getSharedPreferences(SetupsInfo, Context.MODE_PRIVATE);
      /*  JsonParser jsonParser = new JsonParser();
        JsonArray jArray = (JsonArray) jsonParser.parse(Setups.getString("Setups",""));
        aSetups=jArray.get(0).getAsJsonObject();*/
        setMaxMinDate();

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
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

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        takenHrs = (EditText) findViewById(R.id.hrsTkn);
        hrsCurr = (EditText) findViewById(R.id.hrsCurr);
        hrsAvail = (EditText) findViewById(R.id.hrsAvail);
        gson = new Gson();
        tknHrs = 0;
        TotHrs=UserDetails.getInt("THrsPerm",0);
    //  TotHrs=aSetups.get("Permission_hours").getAsInt();
        AvlHrs=TotHrs-tknHrs;
        hrsAvail.setText(String.valueOf(AvlHrs));
        takenHrs.setText(String.valueOf(tknHrs));
        clickedDate = new SimpleDateFormat("dd-MM-yyy").format(Calendar.getInstance().getTime());
        AvalaibilityHours();
        permsissionDate = (EditText) findViewById(R.id.permission_date);
        permsissionDate.setInputType(InputType.TYPE_NULL);
        permsissionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog.
                picker1 = new DatePickerDialog(Permission_Request.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                permsissionDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                /*07/15/2016*/
                                clickedDate =  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth ;
                                AvalaibilityHours();
                            }
                        }, year, month, day);

                Calendar calendarmin = Calendar.getInstance();
                int mnth=Integer.parseInt(minMonth) - 1;
                int fYr=Integer.parseInt(minYear);
                if (day<24){
                     mnth=Integer.parseInt(minMonth) - 2;
                     fYr=Integer.parseInt(minYear);
                }
                if(mnth<0){ mnth=11;fYr=fYr-1;}
                calendarmin.set(fYr, mnth, 23);
                picker1.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                picker1.show();

            }
        });

        modelShiftType.clear();
        SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String Scode = (shared.getString("Sfcode", "null"));
        String Dcode = (shared.getString("Divcode", "null"));
        spinnerValue("get/Shift_timing", Dcode, Scode);

        eText = (EditText) findViewById(R.id.from_time);
        eText2 = (EditText) findViewById(R.id.to_time);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR);
                int minutes = cldr.get(Calendar.MINUTE);
                int aa = cldr.get(Calendar.AM_PM);
                // time picker dialog

                if (!shitType.getText().toString().matches("")) {
                    picker = new TimePickerDialog(Permission_Request.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                    StringFromTinme = clickedDate+" "+ String.format("%02d:%02d:00", sHour, sMinute);
                                    Date fTime=DT.getDate(StringFromTinme);
                                    int sftFHour=DT.getHourNew(shiftFromDate); int sftFMinute=DT.getMinuteNew(shiftFromDate);
                                    String sTime = clickedDate+" "+ String.format("%02d:%02d:00", sftFHour, sftFMinute);
                                    Date sftSTime=DT.getDate(sTime);
                                    int sftTHour=DT.getHourNew(shiftToDate); int sftTMinute=DT.getMinuteNew(shiftToDate);
                                    String sETime = clickedDate+" "+ String.format("%02d:%02d:00", sftTHour, sftTMinute);
                                    Date sftETime=DT.getDate(sETime);
                                    if(sftFHour>sftTHour){
                                        sftETime=DT.AddDays(sETime,1);
                                        if(sftFHour>DT.getHour(StringFromTinme))
                                            fTime=DT.AddDays(StringFromTinme,1);
                                    }
                                    if(sftSTime.getTime()<=fTime.getTime() && fTime.getTime()<=sftETime.getTime()){
                                        StringFromTinme = clickedDate+" "+ String.format("%02d:%02d:00", sHour, sMinute);
                                        eText.setText(String.format("%02d:%02d", sHour, sMinute));
                                        FTime=String.format("%02d:%02d", sHour, sMinute);
                                    }else{
                                        eText.setText("");
                                        eText2.setText("");
                                        FTime="";
                                        Toast.makeText(Permission_Request.this, "Please Choose the time between the Shifttime", Toast.LENGTH_SHORT).show();
                                    }
                                    ToTimeData();

                                    /*StringFromTinme = String.format("%02d:%02d", sHour, sMinute);
                                    Log.e("From_Time_sMinute", String.format("%02d:%02d", sHour, sMinute));

                                    fromTime(sHour, sMinute);
                                    fromTime = String.valueOf(sHour);
                                    FTime = StringFromTinme;

                                    String now = StringFromTinme;
                                    String start = shiftFromDate;
                                    String end = shiftToDate;

                                    if (isHourInInterval(now, start, end) == true) {
                                        eText.setText(StringFromTinme);
                                    } else {
                                        eText.setText("");
                                        eText2.setText("");
                                        Toast.makeText(Permission_Request.this, "Please Choose the time between the Shifttime", Toast.LENGTH_SHORT).show();
                                    }
                                    ToTimeData();*/
                                }
                            }, hour, minutes, true);
                    picker.show();
                } else {
                    Toast.makeText(Permission_Request.this, "Please Choose the Shifttime", Toast.LENGTH_SHORT).show();
                }
            }
        });


        eText2.setInputType(InputType.TYPE_NULL);

        // addingShiftToSpinner();
        shitType = findViewById(R.id.shift_type);
        shitType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permsissionDate.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(Permission_Request.this, "select the date of permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(PermissionHours.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(Permission_Request.this, "select the permission hours", Toast.LENGTH_SHORT).show();
                    return;
                }
                customDialog = new CustomListViewDialog(Permission_Request.this, modelShiftType, 7);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                /*selectedHours("", "", "");*/
            }
        });


        reasonPermission = (EditText) findViewById(R.id.reason_permission);


        buttonSubmit = (Button) findViewById(R.id.btn_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eText.getText().toString().matches("") && !permsissionDate.getText().toString().matches("") && !eText2.getText().toString().matches("") && !reasonPermission.getText().toString().matches("") && !shitType.getText().toString().matches("")) {
                    PermissionRequestOne();
                } else if (permsissionDate.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter Date", Toast.LENGTH_SHORT).show();
                } else if (shitType.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter Shite time", Toast.LENGTH_SHORT).show();
                } else if (eText.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter From time", Toast.LENGTH_SHORT).show();
                } else if (eText2.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter To Time", Toast.LENGTH_SHORT).show();
                } else if (reasonPermission.getText().toString().matches("")) {
                    Toast.makeText(Permission_Request.this, "Enter Remarks  ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        PermissionHours = findViewById(R.id.select_hours);
        PermissionHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionSelectHours.clear();
                selectedHours();
            }
        });

    }

    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
        Log.d(Tag, String.valueOf(CheckIn));
        if (CheckIn == true) {
            Intent Dashboard = new Intent(this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }


    /**
     * @param target hour to check
     * @param start  interval start
     * @param end    interval end
     * @return true    true if the given hour is between
     */
    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0)
                && (target.compareTo(end) <= 0));
    }


    public void setMaxMinDate() {

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
        Log.e("Sresdfsd", minYear);
        Log.e("Sresdfsd", minMonth);
        Log.e("Sresdfsd", minDay);

    }


    private void fromTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        fromTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        Log.e("TIME_WITH_AM_PM", fromTime);
    }

    private void toTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        toTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

    }


    private void differ() {
        AvlHrs = TotHrs - tknHrs;
        hrsAvail.setText(String.valueOf(AvlHrs));
        if (!fromTime.equals("") && !toTime.equals("")) {
            if (Integer.valueOf(fromTime) < Integer.valueOf(toTime)) {

                differnce = Integer.valueOf(toTime) - Integer.valueOf(fromTime);
                if (differnce > AvlHrs) {
                    Toast.makeText(this, "Your Permission Limit Exceed. " + (AvlHrs) + " Hrs Only Available", Toast.LENGTH_SHORT).show();
                    hrsCurr.setText("");
                    eText2.setText("");
                    return;
                } else {
                    AvlHrs = AvlHrs - differnce;
                    hrsCurr.setText(String.valueOf(differnce));
                }
                hrsAvail.setText(String.valueOf(AvlHrs));

            } else {
                Toast.makeText(this, "PLease choose higher than from time", Toast.LENGTH_SHORT).show();
                hrsCurr.setText("");
                eText2.setText("");
            }

        } else {
        }

    }







    /*Checking availability with date*/

    public void AvalaibilityHours() {

        String datePermission = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.availabilityLeave(clickedDate, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, datePermission);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                Type userType1 = new TypeToken<ArrayList<AvalaibilityHours>>() {
                }.getType();
                mAvalaibilityHours = gson.fromJson(new Gson().toJson(response.body()), userType1);
                Log.e("REMAINING_LEAVES", String.valueOf(response.body()));
                for (int i = 0; i < mAvalaibilityHours.size(); i++) {
                    takenLeave = mAvalaibilityHours.get(i).getTknHrS();
                    if (takenLeave != null) {
                        takenHrs.setText(takenLeave);
                        tknHrs = Integer.parseInt(takenLeave);
                    } else {
                        takenHrs.setText("0");
                        tknHrs = 0;
                    }
                    differ();
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }



    /*Submit Hours*/

    public void PermissionRequestOne() {

        String hoursCount = "'" + String.valueOf(differnce) + "'";

        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("pdate", clickedDate);
            jsonleaveType.put("start_at", FTime);
            jsonleaveType.put("end_at", TOTime);
            jsonleaveType.put("Reason", reasonPermission.getText().toString());
            jsonleaveType.put("Noof_Count", StringPremissonEntry);
            jsonleaveType.put("Shift", shiftTypeId);
            jsonleaveTypeS.put("PermissionFormValidate", jsonleaveType);
            jsonleaveTypeS.put("Ekey", com.saneforce.godairy.Common_Class.Common_Class.GetEkey());
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

                Log.e("TOTAL_REPOSNE_PER", String.valueOf(jsonObjecta));
                String Msg = jsonObjecta.get("Msg").getAsString();

                if (Msg != null && !Msg.isEmpty() && !Msg.equals("")) {

                    AlertDialogBox.showDialog(Permission_Request.this, "Confrimation", Msg, "OK", "", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                } else {
                    PermissionRequestTwo();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }


    /*PermissionRequestTwo*/
    private void PermissionRequestTwo() {

        String hoursCount = String.valueOf(differnce);

        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("pdate", clickedDate);
            jsonleaveType.put("start_at", FTime);
            jsonleaveType.put("end_at", TOTime);
            jsonleaveType.put("Reason", reasonPermission.getText().toString());
            jsonleaveType.put("No_of_Hrs", StringPremissonEntry);
            jsonleaveTypeS.put("PermissionEntry", jsonleaveType);
            jsonleaveTypeS.put("Ekey", com.saneforce.godairy.Common_Class.Common_Class.GetEkey());
            jsonArray1.put(jsonleaveTypeS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String leaveCap1 = jsonArray1.toString();

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.leaveSubmit(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, "MGR", leaveCap1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjecta = response.body();

                String Msg = jsonObjecta.get("Msg").getAsString();
                if (Msg != null && !Msg.isEmpty()) {
                    AlertDialogBox.showDialog(Permission_Request.this, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
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
                //startActivity(new Intent(Permission_Request.this, Leave_Dashboard.class));

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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
                try {
                    JSONArray jsonArray = new JSONArray(REPONSE);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.optString("id");
                        String name = jsonObject1.optString("name");
                        String flag = jsonObject1.optString("FWFlg");
                        Model_Pojo = new Common_Model(id, name, jsonObject1);
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

    /*Permission Select Hours*/
    private void selectedHours() {

        Common_Model Model_Pojo1, Model_Pojo2;
        Model_Pojo1 = new Common_Model("1", "1", "FWFlg");
        Model_Pojo2 = new Common_Model("2", "2", "FWFlg");
        permissionSelectHours.add(Model_Pojo1);
        permissionSelectHours.add(Model_Pojo2);


        customDialog = new CustomListViewDialog(Permission_Request.this, permissionSelectHours, 10);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();


    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 7) {
            shitType.setText(myDataset.get(position).getName());
            shiftTypeId = myDataset.get(position).getId();
            JSONObject item=myDataset.get(position).getJSONObject();
            try {
                shiftFromDate=item.getString("Sft_STime");
                shiftToDate=item.getString("sft_ETime");
            } catch (JSONException e) {
                e.printStackTrace();
            }

/*
            shiftFromDate = shitType.getText().toString();
            shiftToDate = shitType.getText().toString();
            shiftFromDate = shiftFromDate.substring(0, 5);
            shiftToDate = shiftToDate.substring(9, 14);*/
        } else if (type == 10) {
            PermissionHours.setText(myDataset.get(position).getName());
            hrsCurr.setText(myDataset.get(position).getName());
            StringPremissonEntry = myDataset.get(position).getName();
            ToTimeData();
        }
    }


    public void ToTimeData() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.permissionHours("CalTotime", StringFromTinme, StringPremissonEntry);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObjecta = response.body();

                Log.e("TOTAL_REPOSNE_PER", String.valueOf(jsonObjecta));
                TOTime = jsonObjecta.get("Totime").getAsString();
                if (!StringFromTinme.equals("")) {
                    Log.v("eText.getText",eText.getText().toString());
                    if (!eText.getText().toString().equals("")) {
                        eText2.setText(TOTime);
                    }else{
                        eText2.setText("");
                    }

                }
                AvalaibilityHours();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }
}