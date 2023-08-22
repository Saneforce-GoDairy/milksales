package com.saneforce.milksales.Activity_Hap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.saneforce.milksales.BuildConfig;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.LocationFinder;
import com.saneforce.milksales.databinding.ActivityImageCaptureBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import androidx.camera.core.CameraSelector;

public class ImageCaptureActivity extends AppCompatActivity {
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
    private String mSfCodeUkey = "";
    private String VistPurpose = "";
    private String mMode ,mModeRetailorCapture, WrkType, onDutyPlcID, onDutyPlcNm;
    private File file ;
    private String DIR;
    public static final String APP_DATA = "/.saneforce";

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
        binding.captureButton.setOnClickListener(view1 -> {

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
    }

    public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(binding.cameraPreview.getWidth(), binding.cameraPreview.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                binding.captureButton.setOnClickListener(view -> {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else {
                        takePicture(imageCapture);
                    }
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
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                   // Toast.makeText(context, "Image saved at: " + file.getPath(), Toast.LENGTH_SHORT).show();

                    file = new File(DIR, "checkin_image" + ".jpg");
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    binding.imageView.setImageBitmap(bitmap);
                    binding.imageView.setVisibility(View.VISIBLE);

                    binding.cameraPreview.setVisibility(View.INVISIBLE);
                });
                startCamera(cameraFacing);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failed to save: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                startCamera(cameraFacing);
            }
        });
    }


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