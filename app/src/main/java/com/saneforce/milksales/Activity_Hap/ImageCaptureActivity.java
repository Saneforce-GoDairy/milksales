package com.saneforce.milksales.Activity_Hap;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExposureState;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity.AllowanceActivityTwo;
import com.saneforce.milksales.BuildConfig;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.LocationEvents;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;
import com.saneforce.milksales.common.AlmReceiver;
import com.saneforce.milksales.common.FileUploadService;
import com.saneforce.milksales.common.LocationFinder;
import com.saneforce.milksales.common.LocationReceiver;
import com.saneforce.milksales.common.SANGPSTracker;
import com.saneforce.milksales.databinding.ActivityImageCaptureBinding;
import com.saneforce.milksales.session.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageCaptureActivity extends AppCompatActivity {
    private SessionHandler session;
    private ActivityImageCaptureBinding binding;
    private final Context context = this;
    private final String KEY_MY_PREFE = "mypref";
    public static final String KEY_CHECKIN_DETAILS = "CheckInDetail";
    public static final String KEY_USER_DETAILS = "MyPrefs";
    private Shared_Common_Pref SHARED_COMMON_PREF;
    private SharedPreferences SHARED_PREFERENCES;
    private SharedPreferences CHECKIN_DETAILS;
    private SharedPreferences USER_DETAILS;
    private JSONObject CHECKIN_INFO;
    private final Common_Class COMMON_CLASS = new Common_Class();
    private com.saneforce.milksales.Common_Class.Common_Class COMMON_CLASS2;
    private Location mLocation;
    private String mSfCodeUkey = "", placeName = "", VistPurpose = "", placeId = "";
    private String mMode ,mModeRetailorCapture, WrkType, onDutyPlcID, onDutyPlcNm, imageFileName, vstPurpose;
    private String responseStatus;
    private File file ;
    private String DIR;
    public static final String APP_DATA = "/.saneforce";
    private Camera camera;
    private static OnImagePickListener imagePickListener;
    private Bitmap bitmap;
    private Dialog submitProgressDialog;
    private Dialog checkInSuccessDialog;
    private SANGPSTracker mLUService;
    private LocationReceiver myReceiver;
    private boolean mBound = false;
    private String mTime;

    int cameraFacing = CameraSelector.LENS_FACING_FRONT;
    private final ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                startCamera(cameraFacing);
            }
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageCaptureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        onClick();
        intSharedPref();
        loadJsonObjectCommonClass();
        iniLocationFinder();

        mSfCodeUkey = USER_DETAILS.getString("Sfcode", "") + "-" + (new Date().getTime());

        loadIntent();
        cameraPermission();
        DIR = getExternalFilesDir("/").getPath() + "/" + ".saneforce/";
        createDirectory();
        initSession();
    }

    private void initCheckInSuccessDialog(String time) {
        checkInSuccessDialog = new Dialog(context);
        checkInSuccessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkInSuccessDialog.setContentView(R.layout.model_dialog__checkin_success);
        checkInSuccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkInSuccessDialog.setCancelable(false);

        Button backBtn = checkInSuccessDialog.findViewById(R.id.close);
        TextView mMessage = checkInSuccessDialog.findViewById(R.id.message);
        TextView mCheckInTime = checkInSuccessDialog.findViewById(R.id.check_in_time);
        backBtn.setEnabled(true);
        mMessage.setEnabled(true);
        mCheckInTime.setEnabled(true);

        mCheckInTime.setText(mTime);
        checkInSuccessDialog.show();

        backBtn.setOnClickListener(v -> {
            checkInSuccessDialog.dismiss();
            Intent Dashboard = new Intent(context, CheckInActivity2.class);
            Dashboard.putExtra("Mode", "CIN");
            context.startActivity(Dashboard);
            finish();
        });
    }

    private void initSession() {
        session = new SessionHandler(getApplicationContext());
    }

    private void initSubmitProgressDialog(String messge) {
        submitProgressDialog = new Dialog(context);
        submitProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        submitProgressDialog.setContentView(R.layout.model_dialog_submit_checkin);
        submitProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        submitProgressDialog.setCancelable(false);

        TextView textView = submitProgressDialog.findViewById(R.id.message1);
        textView.setEnabled(true);
        textView.setText(messge);
        submitProgressDialog.show();
    }

    private void createDirectory() {
            File dir = getExternalFilesDir(APP_DATA);
            if(!dir.exists()) {
                if (!dir.mkdir()) {
                    Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }
    }

    private void loadIntent() {
        Bundle params = getIntent().getExtras();
        try {
            if (params != null) {
                mMode = params.getString("Mode");
            }
            if (params != null) {
                mModeRetailorCapture = params.getString("RetailorCapture");
            }

            String exData = null;
            if (params != null) {
                exData = params.getString("data", "");
            }
            if (exData != null && !(exData.equalsIgnoreCase("") || exData.equalsIgnoreCase("null"))) {
                CHECKIN_INFO = new JSONObject(exData);
            }

            if (mMode != null && !mMode.equalsIgnoreCase("PF")) {
                CHECKIN_INFO.put("Mode", mMode);
                CHECKIN_INFO.put("Divcode", USER_DETAILS.getString("Divcode", ""));
                CHECKIN_INFO.put("sfCode", USER_DETAILS.getString("Sfcode", ""));
                WrkType = "0";
                if (mMode.equals("onduty")) {
                    WrkType = "1";
                }
                Log.e("Checkin_Mode", mMode);
                String SftId = null;
                if (params != null) {
                    SftId = params.getString("ShiftId");
                }
                if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {
                    if (SftId != null && !SftId.isEmpty()) {
                        CHECKIN_INFO.put("Shift_Selected_Id", SftId);
                        CHECKIN_INFO.put("Shift_Name", params.getString("ShiftName"));
                        CHECKIN_INFO.put("ShiftStart", params.getString("ShiftStart"));
                        CHECKIN_INFO.put("ShiftEnd", params.getString("ShiftEnd"));
                        CHECKIN_INFO.put("ShiftCutOff", params.getString("ShiftCutOff"));
                    }
                    CHECKIN_INFO.put("App_Version", BuildConfig.VERSION_NAME);
                    CHECKIN_INFO.put("WrkType", WrkType);
                    CHECKIN_INFO.put("CheckDutyFlag", "0");
                    CHECKIN_INFO.put("On_Duty_Flag", WrkType);
                    CHECKIN_INFO.put("PlcID", onDutyPlcID);
                    CHECKIN_INFO.put("PlcNm", onDutyPlcNm);
                    CHECKIN_INFO.put("vstRmks", VistPurpose);
                }

                if (mMode.equalsIgnoreCase("extended")) {
                    if (SftId != null && !SftId.isEmpty()) {
                        @SuppressLint("SimpleDateFormat") DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        CHECKIN_INFO.put("Shift_Selected_Id", SftId);
                        CHECKIN_INFO.put("Shift_Name", params.getString("ShiftName"));
                        CHECKIN_INFO.put("ShiftStart", params.getString("ShiftStart"));
                        CHECKIN_INFO.put("ShiftEnd", params.getString("ShiftEnd"));
                        CHECKIN_INFO.put("ShiftCutOff", params.getString("ShiftCutOff"));
                        CHECKIN_INFO.put("App_Version", BuildConfig.VERSION_NAME);
                        CHECKIN_INFO.put("Ekey", "EK" + USER_DETAILS.getString("Sfcode", "") + dfw.format(calobjw.getTime()).hashCode());
                        CHECKIN_INFO.put("update", "0");
                        CHECKIN_INFO.put("WrkType", "0");
                        CHECKIN_INFO.put("CheckDutyFlag", "0");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void iniLocationFinder() {
        new LocationFinder(getApplication(), location -> mLocation = location);
    }

    private void loadJsonObjectCommonClass() {
        CHECKIN_INFO = new JSONObject();
        COMMON_CLASS2 = new com.saneforce.milksales.Common_Class.Common_Class(this);
    }

    private void intSharedPref() {
        SHARED_COMMON_PREF = new Shared_Common_Pref(context);
        SHARED_PREFERENCES = getSharedPreferences(KEY_MY_PREFE, Context.MODE_PRIVATE);

        CHECKIN_DETAILS = getSharedPreferences(KEY_CHECKIN_DETAILS, Context.MODE_PRIVATE);
        USER_DETAILS = getSharedPreferences(KEY_USER_DETAILS, Context.MODE_PRIVATE);
    }

    private void onClick() {
        binding.retry.setOnClickListener(v -> {
            startCamera(cameraFacing);
            binding.imageView.setVisibility(View.GONE);
            binding.imageOkRetryContainer.setVisibility(View.GONE);
            binding.cameraFunctionContainer.setVisibility(View.VISIBLE);
            binding.cameraPreview.setVisibility(View.VISIBLE);
            binding.seekBarChangeBrightness.setVisibility(View.VISIBLE);
        });
        binding.back.setOnClickListener(v -> finish());
        binding.buttonFlash.setOnClickListener(v -> {
        });
        binding.buttonSwitchCam.setOnClickListener(v -> {
            if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
            } else {
                cameraFacing = CameraSelector.LENS_FACING_BACK;
            }
            startCamera(cameraFacing);
        });
        binding.seekBarChangeBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ExposureState exposureState = camera.getCameraInfo().getExposureState();
                if (!exposureState.isExposureCompensationSupported()) return;

                Range<Integer> range = exposureState.getExposureCompensationRange();
                if (range.contains(progress))
                    camera.getCameraControl().setExposureCompensationIndex(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.submit.setOnClickListener(v -> {
            try {

                imageFileName = file.getName();
                Log.e("file_name", imageFileName);

                if (mModeRetailorCapture == null){
                    Intent intent = new Intent(context, FileUploadService.class);
                    intent.putExtra("mFilePath", String.valueOf(file));
                    intent.putExtra("SF", USER_DETAILS.getString("Sfcode", ""));
                    intent.putExtra("FileName", imageFileName);
                    intent.putExtra("Mode", (mMode.equalsIgnoreCase("PF") ? "PROF" : "ATTN"));
                    FileUploadService.enqueueWork(context, intent);
                }
                if (mModeRetailorCapture != null && mModeRetailorCapture.equals("NewRetailor")) {
//                Intent mIntent = new Intent(this, AddNewRetailer.class);
//                mIntent.putExtra("mFilePath", Uri.fromFile(file).toString());
//                startActivity(mIntent);
                    SHARED_COMMON_PREF.save(Constants.Retailor_FilePath, Uri.fromFile(file).toString());
                    finish();
                } else if (mMode.equalsIgnoreCase("PF")) {
                    imagePickListener.OnImagePick(bitmap, imageFileName);
                    finish();
                } else {
                  //  submitDialog.show();
                /*if (mlocation != null) {
                    mProgress.setMessage("Submiting Please Wait...");
                    vwPreview.setVisibility(View.GONE);
                    // imgPreview.setImageURI(Uri.fromFile(file));
                    button.setVisibility(View.GONE);
                    saveCheckIn();
                } else {*/
                    new LocationFinder(getApplication(), new LocationEvents() {
                        @Override
                        public void OnLocationRecived(Location location) {
                          initSubmitProgressDialog("Check In upload please wait");
//                            mlocation = location;
//                            mProgress.setMessage("Submiting Please Wait...");
//                            vwPreview.setVisibility(View.GONE);
//                            // imgPreview.setImageURI(Uri.fromFile(file));
//                            button.setVisibility(View.GONE);
                            saveCheckIn();
                        }
                    });
                    //}
                }
            }catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        });
    }

    private void saveCheckIn() {
        try {

            Location location = mLocation;//Common_Class.location;//locationFinder.getLocation();
            String CTime = COMMON_CLASS.GetDateTime(getApplicationContext(), "HH:mm:ss");
            String CDate = COMMON_CLASS.GetDateTime(getApplicationContext(), "yyyy-MM-dd");
            if (mMode.equalsIgnoreCase("onduty")) {
                placeName = CHECKIN_INFO.getString("onDutyPlcNm");
                placeId = CHECKIN_INFO.getString("onDutyPlcID");
                if (CHECKIN_INFO.has("vstPurpose"))
                    VistPurpose = CHECKIN_INFO.getString("vstPurpose");
            }
            CHECKIN_INFO.put("eDate", CDate + " " + CTime);
            CHECKIN_INFO.put("eTime", CTime);
            CHECKIN_INFO.put("UKey", mSfCodeUkey);
            double lat = 0, lng = 0;
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            CHECKIN_INFO.put("lat", lat);
            CHECKIN_INFO.put("long", lng);
            CHECKIN_INFO.put("Lattitude", lat);
            CHECKIN_INFO.put("Langitude", lng);

            if (mMode.equalsIgnoreCase("onduty")) {
                CHECKIN_INFO.put("PlcNm", placeName);
                CHECKIN_INFO.put("PlcID", placeId);
            } else {
                CHECKIN_INFO.put("PlcNm", "");
                CHECKIN_INFO.put("PlcID", "");
            }
            if (mMode.equalsIgnoreCase("holidayentry"))
                CHECKIN_INFO.put("On_Duty_Flag", "1");
            else
                CHECKIN_INFO.put("On_Duty_Flag", "0");

            String imagePath = getExternalFilesDir("/").getPath() + "/" + ".saneforce/" + "checkin_image" + ".jpg";

            CHECKIN_INFO.put("iimgSrc", imagePath);
            CHECKIN_INFO.put("slfy", imageFileName);
            CHECKIN_INFO.put("Rmks", vstPurpose);
            CHECKIN_INFO.put("vstRmks", VistPurpose);

            Log.e("Image_Capture", imagePath);
            Log.e("Image_Capture", imageFileName);


            if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {

                JSONArray jsonarray = new JSONArray();
                JSONObject paramObject = new JSONObject();
                paramObject.put("TP_Attendance", CHECKIN_INFO);
                Log.e("CHECK_IN_DETAILS", String.valueOf(paramObject));

                jsonarray.put(paramObject);


                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                        USER_DETAILS.getString("Divcode", ""),
                        USER_DETAILS.getString("Sfcode", ""), "", "", jsonarray.toString());

                Log.v("PRINT_REQUEST", modelCall.request().toString());

                modelCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if (response.isSuccessful()) {

                            JsonObject itm = response.body().getAsJsonObject();
                            Log.e("RESPONSE_FROM_SERVER", String.valueOf(response.body().getAsJsonObject()));
                            submitProgressDialog.dismiss();
                            responseStatus = itm.get("success").getAsString();
                            Log.e("image_capture", "Check in server response : " + responseStatus);
                            if (responseStatus.equalsIgnoreCase("true")) {
                                SharedPreferences.Editor editor = CHECKIN_DETAILS.edit();
                                try {
                                    if (mMode.equalsIgnoreCase("CIN")) {
                                        editor.putString("Shift_Selected_Id", CHECKIN_INFO.getString("Shift_Selected_Id"));
                                        editor.putString("Shift_Name", CHECKIN_INFO.getString("Shift_Name"));
                                        editor.putString("ShiftStart", CHECKIN_INFO.getString("ShiftStart"));
                                        editor.putString("ShiftEnd", CHECKIN_INFO.getString("ShiftEnd"));
                                        editor.putString("ShiftCutOff", CHECKIN_INFO.getString("ShiftCutOff"));

                                        long AlrmTime = COMMON_CLASS.getDate(CHECKIN_INFO.getString("ShiftEnd")).getTime();
                                        sendAlarmNotify(1001, AlrmTime, HAPApp.Title, "Check-Out Alert !.");
                                    }

                                    if (mMode.equalsIgnoreCase("ONDuty")) {
                                        SHARED_COMMON_PREF.save(Shared_Common_Pref.DAMode, true);

                                        mLUService = new SANGPSTracker(context);
                                        myReceiver = new LocationReceiver();
                                        bindService(new Intent(context, SANGPSTracker.class), mServiceConection,
                                                Context.BIND_AUTO_CREATE);
                                        LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver,
                                                new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
                                        mLUService.requestLocationUpdates();
                                    }

                                    if (CHECKIN_DETAILS.getString("FTime", "").equalsIgnoreCase(""))
                                        editor.putString("FTime", CTime);
                                    editor.putString("Logintime", CTime);
                                    if (mMode.equalsIgnoreCase("onduty"))
                                        editor.putString("On_Duty_Flag", "1");
                                    else
                                        editor.putString("On_Duty_Flag", "0");
                                        editor.putInt("Type", 0);
                                        editor.putBoolean("CheckIn", true);
                                        editor.apply();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            String mMessage = "Your Check-In Submitted Successfully";

                            String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            String mTimeStamp  = mDate +" "+mTime;


                            session.setMyCheckIn("true", mTimeStamp);

                            try {
                                mMessage = itm.get("Msg").getAsString();
                            } catch (Exception ignored) {
                            }
                                    if (responseStatus.equalsIgnoreCase("true")) {
                                        initCheckInSuccessDialog(mTime);
                                    }
                                }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        submitProgressDialog.dismiss();
                        Log.d("HAP_receive", "");
                    }
                });
            }
            else if (mMode.equalsIgnoreCase("extended")) {
                JSONArray jsonarray = new JSONArray();
                JSONObject paramObject = new JSONObject();
                paramObject.put("extended_entry", CHECKIN_INFO);
                jsonarray.put(paramObject);
                Log.e("CHECK_IN_DETAILS", String.valueOf(jsonarray));

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                        USER_DETAILS.getString("Divcode", ""),
                        USER_DETAILS.getString("Sfcode", ""), "", "", jsonarray.toString());
                modelCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        submitProgressDialog.dismiss();
                        assert response.body() != null;
                        Log.e("RESPONSE_FROM_SERVER", String.valueOf(response.body().getAsJsonObject()));
                        if (response.isSuccessful()) {
                            JsonObject itm = response.body().getAsJsonObject();
                            SharedPreferences.Editor editor = CHECKIN_DETAILS.edit();
                            editor.putInt("Type", 1);
                            editor.putBoolean("CheckIn", true);
                            editor.apply();

                            String mMessage = "Your Extended Submitted Successfully";
                            try {
                                mMessage = itm.get("Msg").getAsString();


                            } catch (Exception ignored) {
                            }

                            new AlertDialog.Builder(context)
                                    .setTitle(HAPApp.Title)
                                    .setMessage(Html.fromHtml(mMessage))
                                    .setPositiveButton("OK", (dialogInterface, i) -> {
                                        Intent Dashboard = new Intent(context, CheckInActivity2.class);
                                        Dashboard.putExtra("Mode", "extended");
                                        context.startActivity(Dashboard);
                                        ((AppCompatActivity) context).finish();
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        submitProgressDialog.dismiss();
                        Log.d("HAP_receive", "");
                    }
                });
            }
            else {
                submitProgressDialog.dismiss();
                initSubmitProgressDialog("Check Out Please wait");
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                String lMode = "get/logouttime";
                if(mMode.equalsIgnoreCase("EXOUT")) {
                    lMode = "get/Extendlogout";
                }
                Call<JsonObject> modelCall = apiInterface.JsonSave(lMode,
                        USER_DETAILS.getString("Divcode", ""),
                        USER_DETAILS.getString("Sfcode", ""), "", "", CHECKIN_INFO.toString());
                modelCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        submitProgressDialog.dismiss();
                        if (response.isSuccessful()) {
                            Log.e("TOTAL_REPOSNEaaa", String.valueOf(response.body()));
                            SharedPreferences.Editor loginsp = USER_DETAILS.edit();
                            loginsp.putBoolean("Login", false);
                            loginsp.apply();
                            Boolean Login = USER_DETAILS.getBoolean("Login", false);
                            SharedPreferences.Editor editor = CHECKIN_DETAILS.edit();
                            editor.putString("Logintime", "");
                            editor.putBoolean("CheckIn", false);
                            editor.apply();
                            SHARED_COMMON_PREF.clear_pref(Shared_Common_Pref.DAMode);

                            Intent playIntent = new Intent(context, SANGPSTracker.class);
                            stopService(playIntent);

                            JsonObject itm = response.body().getAsJsonObject();
                            String mMessage = "Your Check-Out Submitted Successfully<br><br>Check in Time  : " + CHECKIN_DETAILS.getString("FTime", "") + "<br>" +
                                    "Check Out Time : " + CTime;

                            session.setMyCheckIn("false", "");

                            try {
                                mMessage = itm.get("Msg").getAsString();
                            } catch (Exception e) {
                            }

                            AlertDialogBox.showDialog(context, HAPApp.Title, String.valueOf(Html.fromHtml(mMessage)), "Ok", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {

                                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                    Call<JsonArray> Callto = apiInterface.getDataArrayList("get/CLSExp",
                                            USER_DETAILS.getString("Divcode", ""),
                                            USER_DETAILS.getString("Sfcode", ""), CDate);

                                    Log.v("DATE_REQUEST", Callto.request().toString());
                                    Callto.enqueue(new Callback<>() {
                                        @Override
                                        public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                                            COMMON_CLASS2.clearLocData(ImageCaptureActivity.this);

                                            SHARED_COMMON_PREF.clear_pref(Constants.DB_TWO_GET_MREPORTS);
                                            SHARED_COMMON_PREF.clear_pref(Constants.DB_TWO_GET_DYREPORTS);
                                            SHARED_COMMON_PREF.clear_pref(Constants.DB_TWO_GET_NOTIFY);
                                            SHARED_COMMON_PREF.clear_pref(Constants.LOGIN_DATA);

                                            finishAffinity();
                                            if (response.body().size() > 0) {
                                                Intent takePhoto = new Intent(context, AllowanceActivityTwo.class);
                                                takePhoto.putExtra("Mode", "COUT");
                                                startActivity(takePhoto);
                                            } else {
                                                Intent Dashboard = new Intent(context, Login.class);
                                                startActivity(Dashboard);
                                                ((AppCompatActivity) context).finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                                        }
                                    });
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        submitProgressDialog.dismiss();
                        Log.d("HAP_receive", "");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendAlarmNotify(int AlmID, long AlmTm, String NotifyTitle, String NotifyMsg) {
        /*AlmTm=AlmTm.replaceAll(" ","-").replaceAll("/","-").replaceAll(":","-");
        String[] sDts= AlmTm.split("-");
        Calendar cal = Calendar.getInstance();
        cal.set(sDts[0],sDts[1],sDts[2],sDts[3],sDts[4]);*/

        Intent intent = new Intent(this, AlmReceiver.class);
        intent.putExtra("ID", String.valueOf(AlmID));
        intent.putExtra("Title", NotifyTitle);
        intent.putExtra("Message", NotifyMsg);
        PendingIntent pIntent = null;
        // PendingIntent.getBroadcast(this.getApplicationContext(), AlmID, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pIntent = PendingIntent.getBroadcast
                    (this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pIntent = PendingIntent.getBroadcast
                    (this, 0, intent,  PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, AlmTm, pIntent);
    }

    public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(binding.cameraPreview.getWidth(), binding.cameraPreview.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                ImageCapture imageCapture = new ImageCapture.Builder()
                                 .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                 .setTargetRotation(getWindowManager()
                                 .getDefaultDisplay().getRotation()).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                binding.captureButton.setOnClickListener(view -> {
                        takePicture(imageCapture);
                });

                binding.buttonFlash.setOnClickListener(view -> setFlashIcon(camera));

                preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }


    public void takePicture(ImageCapture imageCapture) {
        file = new File(DIR, "checkin_image" + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                   // Toast.makeText(context, "Image saved at: " + file.getPath(), Toast.LENGTH_SHORT).show();
                    Log.e("image_capture", "Captured image save path :" + file.getPath());
                    file = new File(DIR, "checkin_image" + ".jpg");
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    // automatic screen orientation require Android 13 above API 33
                    // using Exif method
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(file.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap bmRotated = rotateBitmap(bitmap, orientation);

                    binding.imageView.setImageBitmap(bmRotated);

                    binding.imageView.setVisibility(View.VISIBLE);
                    binding.imageOkRetryContainer.setVisibility(View.VISIBLE);

                    binding.cameraPreview.setVisibility(View.INVISIBLE);
                    binding.cameraFunctionContainer.setVisibility(View.INVISIBLE);
                    binding.seekBarChangeBrightness.setVisibility(View.GONE);
                });
                startCamera(cameraFacing);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> Log.e("image_capture", "Captured image save error :" + exception.getMessage()));
                startCamera(cameraFacing);
            }
        });
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private final ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLUService = ((SANGPSTracker.LocationBinder) service).getLocationUpdateService(getApplicationContext());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };

    private void setFlashIcon(Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                camera.getCameraControl().enableTorch(true);
                binding.buttonFlash.setImageResource(R.drawable.camera_flash);
            } else {
                camera.getCameraControl().enableTorch(false);
                binding.buttonFlash.setImageResource(R.drawable.camera_flash);
            }
        } else {
            runOnUiThread(() -> Toast.makeText(context, "Flash is not available currently", Toast.LENGTH_SHORT).show());
        }
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}