package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.CameraPermission;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.Model_Class.MissedPunch;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;
import com.saneforce.milksales.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Missed_Punch extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener, Master_Interface {

    String Tag = "HAP_Missed_Punch";
    EditText checkOutTime, checkIn, reasonMP;

    EditText shiftType;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear;
    String DateNTime;
    Gson gson1;
    List<MissedPunch> leavetypelist;
    Type userType;
    String missedDates, missedShift, missedCHeckin, missedCheckOut, missedMode = "";
    Button missedSubmit;
    List<Common_Model> missed_punch = new ArrayList<>();
    Common_Model Model_Pojo;
    TextView misseddateselect;
    LinearLayout misseddatelayout;
    CustomListViewDialog customDialog;
    Button mButtonSubmit;

    TextView TextDate, TextModeTravel, TextStartedKm, TextMaxKm, TextToPlace;
    ImageView StartedKmImage, EndedKmImage;
    Button takeEndedPhoto;
    EditText EndedEditText, PersonalKmEdit;
    Integer stKM = 0, endKm = 0, personalKM = 0, StratKm = 0, maxKM = 0, TotalKm = 0, totalPM = 0;
    SharedPreferences CheckInDetails, sharedpreferences, UserDetails;
    Shared_Common_Pref shared_common_pref;
    ApiInterface apiInterface;
    String Photo_Name = "", imageConvert = "", StartedKm = "", StartedImage = "", CLOSINGKM = "", EndedImage = "",
            CheckInfo = "CheckInDetail", UserInfo = "MyPrefs", MOT = "ModeOfTravel", Name = "Allowance",
            mypreference = "mypref", StrToCode = "", toPlace = "", TOKM = " ", cOUT = "", ImageStart = "",
            strImg = "", strMod = "", strKm = "", Hq = "", EdtReasn = "", SF_code = "", div = "", State_Code = "",
            visbleMOde = "", MissedDate = "";
    LinearLayout linToPlace, linMode;
    Common_Model mCommon_model_spinner;
    Integer count = 0;
    List<Common_Model> modelRetailDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed__punch);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        TextModeTravel = findViewById(R.id.txt_mode_travel);
        TextDate = findViewById(R.id.txt_date);
        TextStartedKm = findViewById(R.id.txt_started_km);
        TextMaxKm = findViewById(R.id.txt_max);
        StartedKmImage = findViewById(R.id.img_started_km);
        EndedEditText = findViewById(R.id.ended_km);
        EndedKmImage = findViewById(R.id.img_ended_km);
        takeEndedPhoto = findViewById(R.id.btn_take_photo);

        PersonalKmEdit = findViewById(R.id.personal_ended_km);
        linToPlace = findViewById(R.id.lin_to);
        linMode = findViewById(R.id.lin_mode_trav);
        TextToPlace = findViewById(R.id.txt_to);
        shared_common_pref = new Shared_Common_Pref(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

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
        gson1 = new Gson();
        misseddatelayout = findViewById(R.id.misseddatelayout);
        misseddateselect = findViewById(R.id.misseddateselect);
        checkOutTime = (EditText) findViewById(R.id.missed_checkout);


        checkOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Missed_Punch.this, Missed_Punch.this, year, month, day);
                datePickerDialog.show();
            }
        });

        misseddatelayout.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        shiftType = (EditText) findViewById(R.id.missed_shift);
        checkIn = (EditText) findViewById(R.id.missed_checkin);
        reasonMP = (EditText) findViewById(R.id.reason_missed);

        Bundle params = getIntent().getExtras();

        if (!(params == null)) {
            missedDates = params.getString("EDt");
            missedShift = params.getString("Shift");
            missedCHeckin = params.getString("CInTm");
            missedCheckOut = params.getString("COutTm");
            visbleMOde = params.getString("Aflag");


            if (visbleMOde.equalsIgnoreCase("1")) {
                linMode.setVisibility(View.VISIBLE);
                Log.v("VISIBLE_MODE_VISIBLE", visbleMOde);
            } else {
                linMode.setVisibility(View.GONE);
                Log.v("VISIBLE_MODE_GONE", visbleMOde);
            }


            /*checkIn.setText(missedCHeckin);
            shiftType.setText(missedShift);
            misseddateselect.setText(missedDates);
            checkOutTime.setText(missedCheckOut);*/
        }
        leaveTypeMethod();

        //  Log.d(Tag, String.valueOf(params));


        mButtonSubmit = (Button) findViewById(R.id.submit_missed);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.v("VISIBLE_MODE_VALUE", visbleMOde);

                if (!misseddateselect.getText().toString().matches("") && !reasonMP.getText().toString().matches("")) {
                    if (visbleMOde.equalsIgnoreCase("1")) {
                        if (EndedEditText.getText().toString().matches("")) {
                            Toast.makeText(Missed_Punch.this, "Choose End Km", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (EndedImage.matches("")) {
                            Toast.makeText(Missed_Punch.this, "Choose End photo", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            try {
                                stKM = Integer.valueOf(TextStartedKm.getText().toString());
                            } catch (NumberFormatException ex) { // handle your exception

                            }
                            endKm = Integer.valueOf(String.valueOf(EndedEditText.getText().toString()));

                            Log.e("START_KM", String.valueOf(stKM));
                            Log.e("End_KM", String.valueOf(endKm));
                            if (stKM < endKm) {
                                missedPunchSubmit();
                            } else {
                                Toast.makeText(Missed_Punch.this, "Should be greater then Started Km", Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        missedPunchSubmit();
                    }
                } else if (misseddateselect.getText().toString().matches("")) {
                    Toast.makeText(Missed_Punch.this, "Enter Shite time", Toast.LENGTH_SHORT).show();
                } else if (reasonMP.getText().toString().matches("")) {
                    Toast.makeText(Missed_Punch.this, "Enter Remarks", Toast.LENGTH_SHORT).show();
                }

            }
        });

        BusToValue();
        callApi();


        if (sharedpreferences.contains("misseddateselect")) {
            misseddateselect.setText(sharedpreferences.getString("misseddateselect", ""));
        }
        if (sharedpreferences.contains("checkIn")) {
            checkIn.setText(sharedpreferences.getString("checkIn", ""));
        }
        if (sharedpreferences.contains("checkOutTime")) {
            checkOutTime.setText(sharedpreferences.getString("checkOutTime", ""));
        }

        if (sharedpreferences.contains("shiftType")) {
            shiftType.setText(""+sharedpreferences.getString("shiftType", "").replaceAll("]", "").replaceAll("\\[", ""));
        }


        if (sharedpreferences.contains("Share_to_id")) {
            StrToCode = sharedpreferences.getString("Share_to_id", "");
            Log.v("Text_To_ID", StrToCode);
        }

        if (sharedpreferences.contains("Share_to")) {
            TOKM = sharedpreferences.getString("Share_to", "");
            TextToPlace.setText(TOKM);
            Log.v("Text_To_Place", TextToPlace.getText().toString());
        }

        if (sharedpreferences.contains("Share_Mot")) {
            strMod = sharedpreferences.getString("Share_Mot", "");
            TextModeTravel.setText(strMod);
            Log.e("COnvert", "imageConvert");

        }
        if (sharedpreferences.contains("Share_km")) {
            strKm = sharedpreferences.getString("Share_km", "");
            TextStartedKm.setText(strKm);
            Log.e("COnvert", "imageConvert");
        }

        if (sharedpreferences.contains("Share_Img")) {
            ImageStart = sharedpreferences.getString("Share_Img", "");
            Glide.with(getApplicationContext())
                    .load(ImageStart)
                    .into(StartedKmImage);
            Log.v("ImageStart", ImageStart);
        }


        if (sharedpreferences.contains("SharedImage")) {
            StartedImage = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + StartedImage);
            if (StartedImage != null && !StartedImage.isEmpty() && !StartedImage.equals("null")) {
                //StartedKmImage.setImageURI(Uri.parse(StartedImage));
            }
        }
        if (sharedpreferences.contains("SharedImages")) {
            EndedImage = sharedpreferences.getString("SharedImages", "");
            Log.e("Privacypolicy", "Checking" + EndedImage);
            EndedKmImage.setImageURI(Uri.parse(EndedImage));
            imageConvert = EndedImage.substring(7);
            Log.e("COnvert", EndedImage.substring(7));
            Log.e("COnvert", imageConvert);
            getMulipart(imageConvert, 0);

        }
        if (sharedpreferences.contains("StartedKM")) {
            StartedKm = sharedpreferences.getString("StartedKM", "");
            Log.e("Privacypolicy", "STARTRD      " + StartedKm);
        }

        if (sharedpreferences.contains("flagCode")) {
            visbleMOde = sharedpreferences.getString("flagCode", "");
            if (visbleMOde.equalsIgnoreCase("1")) {
                linMode.setVisibility(View.VISIBLE);
            } else {
                linMode.setVisibility(View.GONE);

            }
        }

        if (!misseddateselect.getText().toString().equalsIgnoreCase("")) {
            if (visbleMOde.equalsIgnoreCase("1")) {
                linMode.setVisibility(View.VISIBLE);

            } else {


                linMode.setVisibility(View.GONE);
            }
        }


        if (sharedpreferences.contains("Closing")) {
            CLOSINGKM = sharedpreferences.getString("Closing", "");
            Log.e("Privacypolicy", "Checking" + CLOSINGKM);
            if (!CLOSINGKM.equals("")) {
                EndedEditText.setText(CLOSINGKM);
            }
        }


        if (sharedpreferences.contains("Share_reason")) {
            EdtReasn = sharedpreferences.getString("Share_reason", "");
            reasonMP.setText(EdtReasn);
        }

        EndedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EndedEditText.getText().toString() != null && !EndedEditText.getText().toString().isEmpty() && !EndedEditText.getText().toString().equals("null")) {

                    try {
                        stKM = Integer.valueOf(StartedKm);
                    } catch (NumberFormatException ex) { // handle your exception

                    }
                    if (!EndedEditText.getText().toString().equals("")) {

                        try {
                            endKm = Integer.parseInt(EndedEditText.getText().toString());
                        } catch (NumberFormatException ex) { // handle your exception

                        }
                    }
                    Log.e("STARTED_KM", String.valueOf(endKm));
                    if (stKM < endKm) {
                        Log.e("STARTED_KM", "GREATER");
                    } else {
                        Log.e("STARTED_KM", "Not GREATER");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (!EndedImage.matches("")) {
            EndedKmImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                    intent.putExtra("ImageUrl", EndedImage);
                    startActivity(intent);
                }
            });

            if (!ImageStart.matches("")) {
                StartedKmImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                        intent.putExtra("ImageUrl", ImageStart);
                        startActivity(intent);

                    }
                });
            }
        }


        PersonalKmEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                totalPM = Integer.valueOf((EndedEditText.getText().toString())) - Integer.valueOf((TextStartedKm.getText().toString()));
                Log.v("TOTAL_KM_INSIDe", String.valueOf(totalPM));

                if (totalPM > 0) {
                    PersonalKmEdit.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, totalPM)});
                }
            }
        });


        takeEndedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPermission cameraPermission = new CameraPermission(Missed_Punch.this, getApplicationContext());
                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {
                    Log.v("PERMISSION", "PERMISSION");
                    Log.v("ImageStart_ONCLICK", ImageStart);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Closing", EndedEditText.getText().toString());
                    editor.putString("Share_to", TextToPlace.getText().toString());
                    editor.putString("Share_Mot", TextModeTravel.getText().toString());
                    editor.putString("Share_km", TextStartedKm.getText().toString());
                    editor.putString("Share_Img", ImageStart);
                    editor.putString("Share_to_id", StrToCode);
                    editor.putString("Share_reason", reasonMP.getText().toString());
                    editor.putString("misseddateselect", misseddateselect.getText().toString());
                    editor.putString("checkIn", checkIn.getText().toString());
                    editor.putString("checkOutTime", checkOutTime.getText().toString());
                    editor.putString("shiftType", shiftType.getText().toString());
                    editor.putString("flagCode", visbleMOde);


                    editor.commit();

                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                            Photo_Name = FileName;
                            imageConvert = fullPath;
                            EndedImage = "file://" + fullPath;
                            EndedKmImage.setImageBitmap(image);
                        }
                    });
                    Intent intent = new Intent(Missed_Punch.this, AllowancCapture.class);
                    intent.putExtra("allowance", "Missed");
                    startActivity(intent);
                }
            }
        });


        linToPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(Missed_Punch.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });


    }


    public void leaveTypeMethod() {

        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.missedPunch(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e("RESPONSE_LOG", response.body().toString());
                GetJsonData(new Gson().toJson(response.body()), "0");


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        Log.e("DATE_AND_TIME", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        DateNTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

        TimePickerDialog timePickerDialog = new TimePickerDialog(Missed_Punch.this, Missed_Punch.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();


    }

    @Override
    public void onTimeSet(TimePicker tp, int hour, int sMinute) {

        DateNTime = DateNTime + " " + hour + ":" + sMinute;
        Log.e("DATE_AND_TIME", DateNTime);
        Log.e("DATE_AND_TIME", hour + ":" + sMinute);
        checkOutTime.setText(DateNTime);
        missedCheckOut = DateNTime;
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 10) {
            TextToPlace.setText(myDataset.get(position).getName());
            toPlace = myDataset.get(position).getName();
            StrToCode = myDataset.get(position).getId();
            Log.e("STRTOCOD", StrToCode);

        } else if (type == 1) {

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("Closing");
            editor.remove("Share_to");
            editor.remove("Share_Mot");
            editor.remove("Share_km");
            editor.remove("Share_Img");
            editor.remove("Share_to_id");
            editor.remove("Share_reason");
            editor.remove("misseddateselect");
            editor.remove("checkIn");
            editor.remove("checkOutTime");
            editor.remove("shiftType");
            editor.remove("flagCode");
            editor.commit();


            EndedEditText.setText("");
            EndedKmImage.setImageResource(0);
            PersonalKmEdit.setText("");

            missedDates = myDataset.get(position).getId();
            missedShift = myDataset.get(position).getName();
            missedCHeckin = myDataset.get(position).getFlag();
            checkIn.setText(myDataset.get(position).getFlag());
            shiftType.setText(""+myDataset.get(position).getName().replaceAll("]", "").replaceAll("\\[", ""));
            misseddateselect.setText(myDataset.get(position).getId());
            checkOutTime.setText(myDataset.get(position).getAddress());
            missedCheckOut = myDataset.get(position).getAddress();
            count = myDataset.get(position).getPho();
            visbleMOde = myDataset.get(position).getCont();

            Log.v("VISIBLE_COUNT", visbleMOde);

            if (visbleMOde.equalsIgnoreCase("1")) {

                linMode.setVisibility(View.VISIBLE);

            } else {


                linMode.setVisibility(View.GONE);
            }

        }
    }

    /*Submit Missed punch*/
    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                MissedDate = jsonObject1.optString("name");
                String shift = jsonObject1.optString("name1");
                String Checkin_Time = jsonObject1.optString("Checkin_Time");
                String COutTime = jsonObject1.optString("COutTime");
                String ModeCount = jsonObject1.optString("mode_count");

                String visbleMOde = jsonObject1.optString("Aflag");
/*

                checkIn.setText(Checkin_Time);
                shiftType.setText(shift);
                misseddateselect.setText(MissedDate);
                checkOutTime.setText(COutTime);

*/
                Log.v("visbleMOdevisbleMOde", visbleMOde);
                Model_Pojo = new Common_Model(shift, MissedDate, Checkin_Time, COutTime, ModeCount, visbleMOde);
                missed_punch.add(Model_Pojo);
                if (missedDates != null) {
                    if (missedDates.equalsIgnoreCase(Model_Pojo.getId())) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove("Closing");
                        editor.remove("Share_to");
                        editor.remove("Share_Mot");
                        editor.remove("Share_km");
                        editor.remove("Share_Img");
                        editor.remove("Share_to_id");
                        editor.remove("Share_reason");
                        editor.remove("misseddateselect");
                        editor.remove("checkIn");
                        editor.remove("checkOutTime");
                        editor.remove("shiftType");
                        editor.remove("flagCode");
                        editor.commit();


                        EndedEditText.setText("");
                        EndedKmImage.setImageResource(0);
                        PersonalKmEdit.setText("");

                        missedDates = Model_Pojo.getId();
                        missedShift = Model_Pojo.getName();
                        missedCHeckin = Model_Pojo.getFlag();
                        checkIn.setText(Model_Pojo.getFlag());
                        shiftType.setText(""+Model_Pojo.getName().replaceAll("]", "").replaceAll("\\[", ""));
                        misseddateselect.setText(Model_Pojo.getId());
                        checkOutTime.setText(Model_Pojo.getAddress());
                        missedCheckOut = Model_Pojo.getAddress();
                        count = Model_Pojo.getPho();
                        visbleMOde = Model_Pojo.getCont();

                        Log.v("VISIBLE_COUNT", Model_Pojo.getCont());

                        if (visbleMOde.equalsIgnoreCase("1")) {
                            linMode.setVisibility(View.VISIBLE);
                        } else {
                            linMode.setVisibility(View.GONE);
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void missedPunchSubmit() {


        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", imageConvert);
        mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
        mIntent.putExtra("FileName", Photo_Name);
        mIntent.putExtra("Mode", "Travel");
        FileUploadService.enqueueWork(this, mIntent);

        JSONObject jsonleaveType = new JSONObject();
        JSONObject jsonleaveTypeS = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {

            jsonleaveType.put("missed_date", misseddateselect.getText().toString());
            jsonleaveType.put("Shift_Name", shiftType.getText().toString());
            jsonleaveType.put("checkouttime", missedCheckOut);
            jsonleaveType.put("checkinTime", missedCHeckin);
            jsonleaveType.put("reason", reasonMP.getText().toString());
            jsonleaveType.put("km", EndedEditText.getText().toString());
            jsonleaveType.put("pkm", personalKM);
            jsonleaveType.put("mod", "11");
            jsonleaveType.put("sf", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jsonleaveType.put("div", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jsonleaveType.put("url", Photo_Name);
            jsonleaveType.put("from", "");
            jsonleaveType.put("to", TextToPlace.getText().toString());
            jsonleaveType.put("to_code", StrToCode);
            jsonleaveType.put("fare", "");
            jsonleaveType.put("aflag", visbleMOde);
            jsonleaveTypeS.put("MissedPunchEntry", jsonleaveType);
            jsonArray1.put(jsonleaveTypeS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String leaveCap1 = jsonArray1.toString();
        Log.v("Missed_Punch_Data", leaveCap1);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Name);
        Log.e("SF_Name", Shared_Common_Pref.Div_Code);
        Log.e("SF_Name", Shared_Common_Pref.Sf_Code);
        Log.e("SF_Name", Shared_Common_Pref.StateCode);


        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = service.SubmitmissedPunch(Shared_Common_Pref.Sf_Name, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "MGR", leaveCap1);
        Log.e("TOTAL_REQUEST", call.request().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObjecta = response.body();
                Log.e("TOTAL_REPOSNEaaa", String.valueOf(jsonObjecta));
                String Msg = jsonObjecta.get("Msg").getAsString();
                if (!Msg.equalsIgnoreCase("")) {
                    AlertDialogBox.showDialog(Missed_Punch.this, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if (jsonObjecta.get("MsgID").getAsString().equalsIgnoreCase("2")) {
                                Intent mIntent = new Intent(Missed_Punch.this, Leave_Request.class);
                                mIntent.putExtra("EDt", missedDates);
                                startActivity(mIntent);
                                finish();
                            } else if (jsonObjecta.get("success").getAsBoolean() == true)
                                startActivity(new Intent(Missed_Punch.this, Leave_Dashboard.class));//openHome();

                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ErrorSubmit", t.getMessage());
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Missed_Punch.this, Leave_Dashboard.class));
                    finish();
                }
            });

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.misseddatelayout:
                customDialog = new CustomListViewDialog(Missed_Punch.this, missed_punch, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;

        }
    }


    public void BusToValue() {

        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("divisionCode", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getBusTo(jj.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("name"));
                    String townName = String.valueOf(jsonObject.get("ODFlag"));
                    name = name.replaceAll("^[\"']+|[\"']+$", "");
                    id = id.replaceAll("^[\"']+|[\"']+$", "");
                    mCommon_model_spinner = new Common_Model(id, name, "");
                    modelRetailDetails.add(mCommon_model_spinner);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
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
        Intent Dashboard = new Intent(Missed_Punch.this, Dashboard_Two.class);
        Dashboard.putExtra("Mode", "CIN");
        startActivity(Dashboard);
    }

    public void callApi() {

        try {
            if (missedDates == null) return;
            JSONObject jj = new JSONObject();

            jj.put("div", Shared_Common_Pref.Div_Code);
            jj.put("sf", Shared_Common_Pref.Sf_Code);
            jj.put("rSF", Shared_Common_Pref.Sf_Code);
            jj.put("State_Code", Shared_Common_Pref.StateCode);
            jj.put("Activity_Date", missedDates);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getStartKmDetails(jj.toString());


            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {

                            Log.v("print_upload_file_true", "ggg" + response.body());
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData.toString());
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("StartDetails");
                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject json_oo = jsnArValue.getJSONObject(i);
                                TextModeTravel.setText(json_oo.getString("MOT_Name"));
                                TextStartedKm.setText(json_oo.getString("Start_Km"));
                                Glide.with(getApplicationContext())
                                        .load(json_oo.getString("start_Photo"))
                                        .into(StartedKmImage);
                                maxKM = json_oo.getInt("Maxkm");
                                Hq = json_oo.getString("dailyAllowance");
                                StratKm = Integer.valueOf(json_oo.getString("Start_Km"));
                                ImageStart = json_oo.getString("start_Photo");
                                StrToCode = json_oo.getString("To_Place_Id");
                                TextToPlace.setText(json_oo.getString("To_Place"));

                                TotalKm = StratKm + maxKM;

                                Log.v("START_KM", String.valueOf(StratKm));
                                Log.v("ToTAL_KM", String.valueOf(TotalKm));
                                if (!json_oo.getString("start_Photo").matches("")) {
                                    StartedKmImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                                            try {
                                                intent.putExtra("ImageUrl", json_oo.getString("start_Photo"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }
    }

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field(UserDetails.getString("Sfcode", ""));
        CallApiImage(values, imgg, x);
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));
        return xx;
    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("full_profile", path);
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        Log.v("full_profile", yy + "");
        return yy;
    }

    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = apiInterface.uploadkmimg(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());

                try {
                    if (response.isSuccessful()) {
                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            Photo_Name = js.getString("url");
                            Log.v("printing_dynamic_cou", js.getString("url"));
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }
}