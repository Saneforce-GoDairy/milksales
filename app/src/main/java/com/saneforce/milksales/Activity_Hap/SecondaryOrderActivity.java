package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Model_Class.RetailerDetailsModel;
import com.saneforce.milksales.Model_Class.RetailerViewDetails;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondaryOrderActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    LinearLayout OrderType, RetailerType, linearCategory;
    String lastOrderAmount = "", mobileNumber = "";
    TextView txtOrder, txtRetailer, txtAddRetailer, txtRetailerChannel, txtClass, txtLastOrderAmount, txtModelOrderValue, txtLastVisited, txtReamrks, txtMobile, txtMobileTwo, txtTempalate, txtDistributor;
    CustomListViewDialog customDialog;
    EditText edtRemarks;
    List<Common_Model> modelRetailChannel = new ArrayList<>();
    Button mSubmit;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    private ArrayList<String> temaplateList;
    List<RetailerViewDetails> mRetailerViewDetails;
    Shared_Common_Pref shared_common_pref;

    List<RetailerDetailsModel> mRetailerDetailsModels;
    Gson gson;
    LinearLayout mRetailerDetails;
    Type userType;
    String retailerId;
    int count = 0, count1 = 0;

    String orderTypestr = "";
    LinearLayout linerEventCapture;
    String EventcapOne = "";
    Shared_Common_Pref mShaeShared_common_pref;
    int countInt;

    String SF_CODE, Image_uri = "", CUTT_OFF_CODE, WORK_TYPE, Town_code,
            reatilerID, retailerName,
            distributorId, distributorName, totalValueString;
    Integer orderType, totalOrderValue;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    /* Submit button */
    String locationValue, dateTime, checkInTime, keyEk = "EK", KeyDate, keyCodeValue, checkOutTime;

    String time;
    SimpleDateFormat simpleDateFormat;
    Calendar calander;

    JSONObject person1;
    JSONArray sendArray;
    JSONObject stockReportObjectArray, reportObjectArray, PersonObjectArray, sampleReportObjectArray, eventCapturesObjectArray, pendingBillObjectArray, ComProductObjectArray, ActivityInputReport;

    int eventDb = 0;

    String selectOrder = "", RetailerName = "", RetailerChannel = "", Retailerclass = "", OrderAmount = "", LastVisited = "", Remarks = "", textMobile = "", PhoneNumber = "", EventCapCount = "0", OrderTypeText = "", RetailerTypeText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_order);

        gson = new Gson();
        shared_common_pref = new Shared_Common_Pref(this);

        SF_CODE = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code);
        WORK_TYPE = shared_common_pref.getvalue("work_type_code");
        WORK_TYPE = "'" + WORK_TYPE + "'";
        Town_code = shared_common_pref.getvalue("town_code");
        Town_code = "'" + Town_code + "'";


        retailerName = shared_common_pref.getvalue("Retailer_name");
        retailerName = "'" + retailerName + "'";

        distributorId = "'" + shared_common_pref.getvalue("distributor_id") + "'";
        distributorName = "'" + shared_common_pref.getvalue("distributor_name") + "'";


        if (countInt != 0) {
            String cValue = mShaeShared_common_pref.getvalue("COUNT");
            countInt = Integer.parseInt(cValue);
            Log.e("NEW_COUNT_VALULE", String.valueOf(countInt));
        }


        Intent intnets = getIntent();


        RetailerChannel = intnets.getStringExtra("RetailerChannel");
        Retailerclass = intnets.getStringExtra("Retailerclass");
        OrderAmount = intnets.getStringExtra("OrderAmount");
        LastVisited = intnets.getStringExtra("LastVisited");
        Remarks = intnets.getStringExtra("Remarks");
        textMobile = intnets.getStringExtra("textMobile");
        PhoneNumber = intnets.getStringExtra("PhoneNumber");
        EventCapCount = intnets.getStringExtra("EventCapCount");
        OrderTypeText = intnets.getStringExtra("selectOrder");
        RetailerTypeText = intnets.getStringExtra("RetailerName");


        Log.e("EventCount", String.valueOf(RetailerChannel));
        Log.e("EventCount", String.valueOf(Retailerclass));
        Log.e("EventCount", String.valueOf(OrderAmount));
        Log.e("EventCount", String.valueOf(LastVisited));
        Log.e("EventCount", String.valueOf(Remarks));
        Log.e("EventCount", String.valueOf(textMobile));
        Log.e("EventCount", String.valueOf(PhoneNumber));
        Log.e("EventCount", String.valueOf(EventCapCount));
        Log.e("EventCount", String.valueOf(OrderTypeText));
        Log.e("EventCount", String.valueOf(RetailerTypeText));


        Intent intent = getIntent();
        if (Image_uri != "") {
            Image_uri = intent.getStringExtra("Event_caputure");
            Log.e("IMAGER_URI", "" + Image_uri);
        }


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

                    startActivity(new Intent(getApplicationContext(), OrderDashBoard.class));


            }
        });
        initilaize();
        RetailerType();
        getTemaplte();
        onClickRetailerTemplate();


        DateTime();
        currentTime();
        locationInitialize();
        startLocationUpdates();


    }

    public void initilaize() {

        mShaeShared_common_pref = new Shared_Common_Pref(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        mSubmit = findViewById(R.id.submit_button);
        mSubmit.setText("Submit");
        txtRetailer = findViewById(R.id.retailer_type);
        txtOrder = findViewById(R.id.order_type);
        txtAddRetailer = findViewById(R.id.txt_add_retailer);
        txtAddRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondaryOrderActivity.this, AddNewRetailer.class));
            }
        });

        RetailerType = findViewById(R.id.linear_Retailer);
        OrderType = findViewById(R.id.linear_order);

        OrderType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });


        RetailerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(SecondaryOrderActivity.this, modelRetailDetails, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                eventDb = 1;
            }
        });


        Log.e("COUNT", String.valueOf(eventDb));
        linerEventCapture = findViewById(R.id.prm_linear_event_capture);
        linerEventCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("COUNT", String.valueOf(eventDb));

                if (txtOrder.getText().toString().matches("")) {
                    Toast.makeText(SecondaryOrderActivity.this, "Select Order Type", Toast.LENGTH_SHORT).show();
                } else if (txtRetailer.getText().toString().matches("")) {
                    Toast.makeText(SecondaryOrderActivity.this, "Select Retailer Name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), EventCaptureActivity.class);
                    intent.putExtra("EventcapOne", EventcapOne);
                    intent.putExtra("id", 1);
                    intent.putExtra("count", eventDb);
                    intent.putExtra("RetailerChannel", RetailerChannel);
                    intent.putExtra("Retailerclass", Retailerclass);
                    intent.putExtra("OrderAmount", OrderAmount);
                    intent.putExtra("LastVisited", LastVisited);
                    intent.putExtra("Remarks", Remarks);
                    intent.putExtra("textMobile", textMobile);
                    intent.putExtra("PhoneNumber", PhoneNumber);
                    intent.putExtra("RetailerName", RetailerName);
                    intent.putExtra("selectOrder", selectOrder);
                    startActivity(intent);
                    finish();
                }

            }
        });


        linearCategory = findViewById(R.id.prm_linear_orders);
        linearCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtOrder.getText().toString().matches("")) {
                    Toast.makeText(SecondaryOrderActivity.this, "Select Order Type", Toast.LENGTH_SHORT).show();
                } else if (txtRetailer.getText().toString().matches("")) {
                    Toast.makeText(SecondaryOrderActivity.this, "Select Retailer Name", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(SecondaryOrderActivity.this, OrderCategoryActivity.class));
                }

            }
        });


        txtRetailerChannel = findViewById(R.id.retailer_channel);
        txtClass = findViewById(R.id.txt_class);
        txtModelOrderValue = findViewById(R.id.model_order_vlaue);
        txtLastVisited = findViewById(R.id.txt_last_visited);
        txtLastOrderAmount = findViewById(R.id.txt_last_order_amount);
        txtReamrks = findViewById(R.id.txt_remarks);
        txtMobile = findViewById(R.id.txt_mobile);
        txtMobileTwo = findViewById(R.id.txt_mobile2);
        txtDistributor = findViewById(R.id.txt_distributor);
        mRetailerDetails = findViewById(R.id.linear_reatiler_details);


        if (txtRetailer.getText().toString().equals("")) {

            mRetailerDetails.setVisibility(View.GONE);
        } else {
            mRetailerDetails.setVisibility(View.VISIBLE);
        }


        if (EventCapCount != "0") {
            mRetailerDetails.setVisibility(View.GONE);
            txtRetailerChannel.setText(RetailerChannel);
            txtClass.setText(Retailerclass);
            txtLastOrderAmount.setText(OrderAmount);
            txtLastVisited.setText(LastVisited);
            txtReamrks.setText(Remarks);
            txtMobile.setText(textMobile);
            txtMobileTwo.setText(PhoneNumber);
            txtRetailer.setText(RetailerTypeText);
            txtOrder.setText(OrderTypeText);
        } else {
            mRetailerDetails.setVisibility(View.VISIBLE);
        }


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveProduct();
            }
        });


    }


    /*   Order Types*/
    public void OrderType() {
        temaplateList = new ArrayList<>();
        temaplateList.add("Phone Order");
        temaplateList.add("Field Order");

        for (int i = 0; i < temaplateList.size(); i++) {
            String id = String.valueOf(temaplateList.get(i));
            String name = temaplateList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            Log.e("LeaveType_Request", id);
            Log.e("LeaveType_Request", name);
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(SecondaryOrderActivity.this, listOrderType, 9);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }


    /*Retailer Type*/
    public void RetailerType() {
        String RetailerDetails = "{\"tableName\":\"vwDoctor_Master_APP\",\"coloumns\":\"[\\\"doctor_code as id\\\", \\\"doctor_name as name\\\",\\\"Type\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"ListedDr_Address1\\\",\\\"ListedDr_Sl_No\\\",\\\"Mobile_Number\\\",\\\"Doc_cat_code\\\",\\\"ContactPersion\\\",\\\"Doc_Special_Code\\\"]\",\"where\":\"[\\\"isnull(Doctor_Active_flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", RetailerDetails);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);

                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("name"));
                    String townName = String.valueOf(jsonObject.get("ListedDr_Address1"));
                    String phone = String.valueOf(jsonObject.get("Mobile_Number"));
                    String strName = String.valueOf(name.subSequence(1, name.length() - 1));
                    String strTownName = String.valueOf(townName.subSequence(1, townName.length() - 1));
                    String strPhone = String.valueOf(phone.subSequence(1, phone.length() - 1));
                    mCommon_model_spinner = new Common_Model(strName, id, "flag", strTownName, strPhone);
                    modelRetailDetails.add(mCommon_model_spinner);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    /*Retailer Details*/

    public void RetailerViewDetailsMethod() {

        ApiInterface apiInterface2 = ApiClient.getClient().create(ApiInterface.class);

        Log.e("API_INTERFACE", apiInterface2.toString());
        Call<RetailerViewDetails> call = apiInterface2.retailerViewDetails(retailerId, shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        Log.e("Retailer_ID_DETAILS", retailerId);

        call.enqueue(new Callback<RetailerViewDetails>() {
            @Override
            public void onResponse(Call<RetailerViewDetails> call, Response<RetailerViewDetails> response) {

                RetailerViewDetails mRetailerViewDetail = response.body();
                mRetailerDetails.setVisibility(View.VISIBLE);
                RetailerChannel = mRetailerViewDetail.getDrSpl();
                Retailerclass = mRetailerViewDetail.getDrCat();
                OrderAmount = String.valueOf(mRetailerViewDetail.getLastorderAmount());
                LastVisited = mRetailerViewDetail.getLVDt();
                Remarks = mRetailerViewDetail.getRmks();
                textMobile = mRetailerViewDetail.getPOTENTIAL().get(0).getListedDrPhone();
                PhoneNumber = String.valueOf(mRetailerViewDetail.getPOTENTIAL().get(0).getListedDrMobile());


                txtRetailerChannel.setText(mRetailerViewDetail.getDrSpl());
                txtClass.setText(mRetailerViewDetail.getDrCat());
                lastOrderAmount = String.valueOf(mRetailerViewDetail.getLastorderAmount());
                txtLastOrderAmount.setText(lastOrderAmount);
                txtLastVisited.setText(mRetailerViewDetail.getLVDt());
                txtReamrks.setText(mRetailerViewDetail.getRmks());
                txtMobile.setText(mRetailerViewDetail.getPOTENTIAL().get(0).getListedDrPhone());
                mobileNumber = String.valueOf(mRetailerViewDetail.getPOTENTIAL().get(0).getListedDrMobile());
                txtMobileTwo.setText(mobileNumber);

            }

            @Override
            public void onFailure(Call<RetailerViewDetails> call, Throwable t) {
                Log.d("Retailer_Details", "Error");
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {
            txtRetailer.setText(myDataset.get(position).getName());
            retailerId = myDataset.get(position).getId();
            retailerId = String.valueOf(retailerId.subSequence(1, retailerId.length() - 1));
            RetailerViewDetailsMethod();
            reatilerID = "'" + retailerId + "'";
            shared_common_pref.save("Retailer_id", retailerId);
            shared_common_pref.save("Retailer_name", myDataset.get(position).getName());
            shared_common_pref.save("Event_Capture", "Remove");
            RetailerName = myDataset.get(position).getName();
            Log.e("Retailer_ID", myDataset.get(position).getName());
        } else if (type == 9) {
            txtOrder.setText(myDataset.get(position).getName());
            if (myDataset.get(position).getName().toString().matches("Phone Order")) {
                count = 0;
                orderTypestr = "Zero";
            } else {
                count = 1;
                orderTypestr = "One";
            }
            shared_common_pref.save("Phone_order_type", myDataset.get(position).getName());

            Log.e("Phone_order_type", String.valueOf(count));
            Log.e("Phone_order_type", myDataset.get(position).getName());
            selectOrder = myDataset.get(position).getName();


        } else if (type == 11) {
            edtRemarks.setText(myDataset.get(position).getName() + " ");
            edtRemarks.setSelection(edtRemarks.getText().length());
        }
    }


    /*Retailer Template */
    public void getTemaplte() {
        String routeMap = "{\"tableName\":\"vwRmksTemplate\",\"coloumns\":\"[\\\"id as id\\\", \\\"content as name\\\"]\",\"where\":\"[\\\"isnull(ActFlag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String className = String.valueOf(jsonObject.get("name"));
                    String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                    Log.e("RETAILER_CLASS_NAME", retailerClass);
                    mCommon_model_spinner = new Common_Model(retailerClass, retailerClass, "flag");
                    Log.e("LeaveType_Request", retailerClass);
                    modelRetailChannel.add(mCommon_model_spinner);
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    /*Retailer Template Click*/
    public void onClickRetailerTemplate() {
        txtTempalate = findViewById(R.id.txt_templates);
        edtRemarks = findViewById(R.id.remarks_week);
        txtTempalate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog = new CustomListViewDialog(SecondaryOrderActivity.this, modelRetailChannel, 11);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    AlertDialogBox.showDialog(SecondaryOrderActivity.this, "", "Do you want to exit?", "Yes", "NO", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            startActivity(new Intent(SecondaryOrderActivity.this, OrderDashBoard.class));
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                        }
                    });
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }


    /*Date and Time Format*/
    public void DateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calobj = Calendar.getInstance();
        dateTime = "'" + df.format(calobj.getTime()) + "'";
        System.out.println("Date_and_Time" + dateTime);
    }


    /*Current Time for Cuttoff Process*/
    public void currentTime() {
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calander.getTime());
        checkInTime = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
        Log.e("CHECK_TIME", checkInTime);
    }



    /*Location Initialize*/

    public void locationInitialize() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();

                getLoactionValue();
            }
        };


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /*Displaying Location Result*/
    private void getLoactionValue() {
        if (mCurrentLocation != null) {
            Log.e("Current_Location_Value", "Lat: " + mCurrentLocation.getLatitude() + ", " +
                    "Lng: " + mCurrentLocation.getLongitude());


            locationValue = String.valueOf(mCurrentLocation.getLatitude());
            locationValue = locationValue.concat(":");
            locationValue = "'" + locationValue.concat(String.valueOf(mCurrentLocation.getLongitude())) + "'";


        }
    }


    /*Getting Location Result*/
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        getLoactionValue();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(SecondaryOrderActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        }
                        getLoactionValue();
                    }
                });
    }


    /*Save Visit call*/
    public void SaveProduct() {
        /*ActivityReport*/
        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calobjw = Calendar.getInstance();
        KeyDate = SF_CODE;
        keyCodeValue = keyEk + KeyDate + dfw.format(calobjw.getTime()).hashCode();

        Log.e("KEY_CODE_HASH", keyCodeValue);

        JSONObject reportObject = new JSONObject();

        reportObjectArray = new JSONObject();

        String sFCODE = "'" + SF_CODE + "'";





        /*Check out timing*/
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calander.getTime());
        checkOutTime = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());


        String remarks = edtRemarks.getText().toString();
        remarks = "'" + remarks + "'";

        try {
            reportObject.put("Worktype_code", WORK_TYPE);
            reportObject.put("Town_code", Town_code);
            reportObject.put("RateEditable", "''");
            reportObject.put("dcr_activity_date", dateTime);
            reportObject.put("Daywise_Remarks", remarks);
            reportObject.put("eKey", keyCodeValue);
            reportObject.put("rx", "'1'");
            reportObject.put("rx_t", "''");
            reportObject.put("DataSF", sFCODE);
            reportObjectArray.put("Activity_Report_APP", reportObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String Activity_Report_APP = reportObjectArray.toString();

        System.out.println(" Activity_Report_APP" + Activity_Report_APP);


        /*StockListReport*/
        JSONObject stockReportObject = new JSONObject();
        stockReportObjectArray = new JSONObject();
        JSONObject fkeyStock = new JSONObject();


        try {

            stockReportObject.put("Doctor_POB", 0);
            stockReportObject.put("Worked_With", sFCODE);
            stockReportObject.put("Doc_Meet_Time", dateTime);
            stockReportObject.put("modified_time", dateTime);
            stockReportObject.put("net_weight_value", 0);
            stockReportObject.put("stockist_code", distributorId);
            stockReportObject.put("stockist_name", distributorName);
            stockReportObject.put("superstockistid", "''");
            stockReportObject.put("Discountpercent", 0);
            stockReportObject.put("CheckinTime", checkInTime);
            stockReportObject.put("CheckoutTime", checkOutTime);
            stockReportObject.put("location", "''");
            stockReportObject.put("geoaddress", "");
            stockReportObject.put("PhoneOrderTypes", txtOrder.getText().toString());
            stockReportObject.put("Order_Stk", distributorId);
            stockReportObject.put("Order_No", "''");
            stockReportObject.put("rootTarget", "0");
            stockReportObject.put("orderValue", 0);
            stockReportObject.put("rateMode", "free");
            stockReportObject.put("discount_price", 0);
            stockReportObject.put("doctor_code", reatilerID);
            stockReportObject.put("doctor_name", retailerName);
            stockReportObject.put("f_key", fkeyStock);
            fkeyStock.put("Activity_Report_Code", "'Activity_Report_APP'");
            stockReportObjectArray.put("Activity_Doctor_Report", stockReportObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String stockReport = stockReportObjectArray.toString();

        System.out.println(" Activity_Stockist_Report" + stockReport);


        /*Activity_Trans_Order_Details*/
        JSONArray sampleReportArray = new JSONArray();
        sampleReportObjectArray = new JSONObject();

        try {

            sampleReportObjectArray.put("Trans_Order_Details", sampleReportArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sampleReport = sampleReportObjectArray.toString();
        System.out.println(" Trans_Order_Details" + sampleReport);



        /*Activity_Input_Report*/
        JSONArray Activity_Input_Report = new JSONArray();
        ActivityInputReport = new JSONObject();

        try {
            ActivityInputReport.put("Activity_Input_Report", Activity_Input_Report);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String inputReport = ActivityInputReport.toString();

        System.out.println(" Activity_Input_Report" + inputReport);




        /*Activity_Event_Captures*/

        JSONArray eventCapturesArray = new JSONArray();
        eventCapturesObjectArray = new JSONObject();


        try {
/*
            eventCapturesArray.put("","");
            eventCapturesArray.put("","");
            eventCapturesArray.put("","");*/
            eventCapturesObjectArray.put("Activity_Event_Captures", eventCapturesArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String EventCap = eventCapturesObjectArray.toString();
        System.out.println("Activity_Event_Captures" + EventCap);


        /*PENDING_Bills*/
        JSONArray pendingBillArray = new JSONArray();
        pendingBillObjectArray = new JSONObject();

        try {
            pendingBillObjectArray.put("PENDING_Bills", pendingBillArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String PendingBill = pendingBillObjectArray.toString();
        System.out.println(" PENDING_Bills" + PendingBill);


        /*Compititor_Product*/
        JSONArray ComProductArray = new JSONArray();
        ComProductObjectArray = new JSONObject();

        try {
            ComProductObjectArray.put("Compititor_Product", ComProductArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String ComProduct = ComProductObjectArray.toString();

        System.out.println(" Compititor_Product" + ComProduct);




        /*product_order_list*/


        JSONArray personarray = new JSONArray();
        PersonObjectArray = new JSONObject();

        try {
            PersonObjectArray.put("Activity_Sample_Report", personarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String JsonData = PersonObjectArray.toString();

        System.out.println("Activity_Sample_Report: " + JsonData);


        sendArray = new JSONArray();
        sendArray.put(reportObjectArray);
        sendArray.put(stockReportObjectArray);
        sendArray.put(PersonObjectArray);
        sendArray.put(sampleReportObjectArray);
        sendArray.put(ActivityInputReport);
        sendArray.put(eventCapturesObjectArray);
        sendArray.put(pendingBillObjectArray);
        sendArray.put(ComProductObjectArray);


        totalValueString = sendArray.toString();


        Log.e("SUBMIT_VALUE", totalValueString);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> responseBodyCall = apiInterface.submitValue(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), SF_CODE, totalValueString);

        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.e("Success", response.body().toString());
                    Log.e("totalValueString", totalValueString);
                    try {
                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                        String san = jsonObjects.getString("success");
                        Log.e("StateCodeSet", san);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SUBMIT_VALUE", "ERROR");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (txtRetailer.getText().toString().equals("")) {

            mRetailerDetails.setVisibility(View.GONE);
        } else {
            mRetailerDetails.setVisibility(View.VISIBLE);
        }
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }
}
