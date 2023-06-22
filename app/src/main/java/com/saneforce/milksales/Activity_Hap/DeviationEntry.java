package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.BuildConfig;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.LocationEvents;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Model_Class.DeviationEntryModel;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.LocationFinder;

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
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviationEntry extends AppCompatActivity implements Master_Interface {

    TextView DeviationTypeEntry;
    EditText chooseDate, remarks;
    SharedPreferences CheckInDetails,UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    DatePickerDialog picker1;
    String  minDate, minYear, minMonth, minDay, dateInput,sAppVer,myVersion,deivationID="",
            sSf_Code,sSf_Name,sDiv_Code,sStateCode,Currentlocation = "";
    int sdkVersion;
    Button DeviationSubmit;

    /*Deviation Entry*/
    Gson gson;
    List<DeviationEntryModel> deviationEntry;
    Type userType;
    Common_Model Model_Pojo;
    List<Common_Model> modelleaveType = new ArrayList<>();
    CustomListViewDialog customDialog;
    Location mLocation;
    LinearLayout LinearDevaitaionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviation_entry);
        getToolbar();
        gson = new Gson();
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        DeviationTypeEntry = findViewById(R.id.deviation_type);
        chooseDate = (EditText) findViewById(R.id.choose_date);
        remarks = findViewById(R.id.remarks);
        DeviationSubmit = findViewById(R.id.deviation_submit);

        sSf_Code = UserDetails.getString("Sfcode", "");
        sSf_Name = UserDetails.getString("SfName", "");
        sDiv_Code = UserDetails.getString("Divcode", "");
        sStateCode = UserDetails.getString("State_Code", "");
        sAppVer= BuildConfig.VERSION_NAME;
        myVersion = Build.VERSION.RELEASE;
        sdkVersion = android.os.Build.VERSION.SDK_INT;

        new LocationFinder(DeviationEntry.this, new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                mLocation=location;
            }
        });
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog.

                picker1 = new DatePickerDialog(DeviationEntry.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            chooseDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            dateInput = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                    }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                picker1.getDatePicker().setMaxDate(cldr.getTimeInMillis());
                picker1.show();
            }

        });

        modelleaveType.clear();
        getDevetionType();
//        DeviationTypeEntry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customDialog = new CustomListViewDialog(DeviationEntry.this, modelleaveType, 8);
//                Window window = customDialog.getWindow();
//                window.setGravity(Gravity.CENTER);
//                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                customDialog.show();
//            }
//        });

        LinearDevaitaionType = findViewById(R.id.lin_deviation_entry);
        LinearDevaitaionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(DeviationEntry.this, modelleaveType, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        DeviationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLocation!=null){
                    Currentlocation = mLocation.getLatitude() + "," + mLocation.getLongitude();
                    deviationEntrySubmit();
                }
                else {
                    new LocationFinder(DeviationEntry.this, new LocationEvents() {
                        @Override
                        public void OnLocationRecived(Location location) {
                            mLocation = location;
                            Currentlocation = mLocation.getLatitude() + "," + mLocation.getLongitude();
                            deviationEntrySubmit();
                        }
                    });
                }
            }
        });

        Bundle params = getIntent().getExtras();
        if (params != null) {
            String[] stDt = params.getString("EDt").split("/");
            chooseDate.setText(stDt[2] + "-" + stDt[1] + "-" + stDt[0]);
            dateInput = stDt[2] + "-" + stDt[1] + "-" + stDt[0];
            chooseDate.setEnabled(false);
        }
    }

    public void getToolbar() {
        MaxMinDate();
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


    }
    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

        if (CheckIn == true) {
            Intent Dashboard = new Intent(DeviationEntry.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
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

    /*Deviation entry type*/
    public void getDevetionType() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", sDiv_Code);
        QueryString.put("sfCode", sSf_Code);
        QueryString.put("rSF",sSf_Code);
        QueryString.put("State_Code", sStateCode);
        String commonLeaveType = "{\"tableName\":\"vwdeviationtype\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                userType = new TypeToken<ArrayList<DeviationEntryModel>>() {
                }.getType();
                deviationEntry = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < deviationEntry.size(); i++) {
                    String id = String.valueOf(deviationEntry.get(i).getId());
                    String name = deviationEntry.get(i).getName();
                    Model_Pojo = new Common_Model(id, name, "flag");
                    modelleaveType.add(Model_Pojo);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    /*Submit Deviation Entry*/
    public void deviationEntrySubmit() {
        Date currentTime = Calendar.getInstance().getTime();
        JSONObject deviationObject = new JSONObject();
        JSONObject deviationArray = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {
            deviationObject.put("Deviation_Type", deivationID);
            deviationObject.put("AppVer", myVersion);
            deviationObject.put("AndVer", "Android "+myVersion);
            deviationObject.put("From_Date", dateInput);
            deviationObject.put("reason", "'" + remarks.getText().toString() + "'");
            deviationObject.put("Time", "'" + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()) + "'");
            deviationObject.put("LatLng", "'" + Currentlocation + "'");
            deviationArray.put("DeviationEntry", deviationObject);
            jsonArray1.put(deviationArray);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.deviationSave(sSf_Name, sDiv_Code, sSf_Code, sStateCode, "MGR", jsonArray1.toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObjecta = response.body();

                Log.e("SDFDFD", String.valueOf(jsonObjecta));
                String Msg = jsonObjecta.get("Msg").getAsString();
                Log.e("SDFDFDDFF", String.valueOf(Msg.length()));
                if (!Msg.equals("")) {
                    AlertDialogBox.showDialog(DeviationEntry.this, "Confrimation", Msg, "OK", "", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            openHome();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });


                } else {
                    openHome();
                    Toast.makeText(DeviationEntry.this, "Deviation Entry Successfully", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {
            DeviationTypeEntry.setText(myDataset.get(position).getName());
            Log.e("DeviationId", myDataset.get(position).getId());
            deivationID = myDataset.get(position).getId();
        }
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
}