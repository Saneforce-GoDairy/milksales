package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.SFA_Activity.HAPApp.sendOFFlineLocations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
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
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.view.Window;
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

import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity.AllowanceActivityTwo;
import com.saneforce.godairy.BuildConfig;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.CameraPermission;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.LocationResponse;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.common.FileUploadService;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.common.LocationReceiver;
import com.saneforce.godairy.common.SANGPSTracker;
import com.saneforce.godairy.databinding.ActivityCameraxBinding;

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

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraxActivity extends AppCompatActivity {
    public static final String sCheckInDetail = "CheckInDetail";
    public static final String sUserDetail = "MyPrefs";
    public static final String APP_DATA = "/.saneforce";
    private final Context context = this;
    private final Common_Class DT = new Common_Class();
    double lat = 0.0, lng = 0.0;
    AssistantClass assistantClass;
    int mandatory = 0;
    int cameraFacing = CameraSelector.LENS_FACING_FRONT;
    private ActivityCameraxBinding binding;
    private File file;
    private JSONObject CheckInInf;
    private Shared_Common_Pref mShared_common_pref;
    private SharedPreferences CheckInDetails;
    private SharedPreferences UserDetails;
    private String VistPurpose = "", UKey = "", DIR, onDutyPlcID, onDutyPlcNm, vstPurpose, PlaceId = "", PlaceName = "", imagePath, imageFileName;
    private String mMode = "";
    private String mModeRetailorCapture;
    private String imagvalue = "";
    private String capturedImageName, Ekey;
    private com.saneforce.godairy.Common_Class.Common_Class common_class;
    private SANGPSTracker mLUService;
    private boolean mBound = false;
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
    private LocationReceiver myReceiver;
    private Location mlocation;
    private Camera camera;
    private Bitmap bitmap;
    private final ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                startCamera(cameraFacing);
            }
        }
    });
    private Dialog submitProgressDialog;

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
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraxBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        assistantClass = new AssistantClass(context);
        String intentMode = getIntent().getStringExtra("Mode");

        if (intentMode.equals("COUT") || intentMode.equals("EXOUT")) {
            binding.headerText.setText("Check Out");
        } else if (intentMode.equalsIgnoreCase("secondaryEventCapture") || intentMode.equalsIgnoreCase("primaryEventCapture")) {
            binding.headerText.setText("Event Capture");
            mandatory = getIntent().getIntExtra("mandatory", 0);
            if (mandatory == 1) {
                binding.back.setVisibility(View.GONE);
            }
            binding.submit.setVisibility(View.GONE);
            binding.top2.setVisibility(View.INVISIBLE);
            binding.submit.setText("Proceed to order entry");
        }

        if (Shared_Common_Pref.Outletlat != null) {
            lat = Shared_Common_Pref.Outletlat;
        }
        if (Shared_Common_Pref.Outletlat != null) {
            lng = Shared_Common_Pref.Outletlong;
        }
        Log.e("jhdbjfsbhf", "" + lat);
        Log.e("jhdbjfsbhf", "" + lng);

        onClick();
        cameraPermission();
        DIR = getExternalFilesDir("/").getPath() + "/" + ".saneforce/";
        createDirectory();

        mShared_common_pref = new Shared_Common_Pref(this);
        String mypreference = "mypref";
        SharedPreferences sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        CheckInInf = new JSONObject();
        CheckInDetails = getSharedPreferences(sCheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(sUserDetail, Context.MODE_PRIVATE);
        common_class = new com.saneforce.godairy.Common_Class.Common_Class(this);

        UKey = UserDetails.getString("Sfcode", "") + "-" + (new Date().getTime());
        Bundle params = getIntent().getExtras();
        try {
            mMode = params.getString("Mode");
            mModeRetailorCapture = params.getString("RetailorCapture");

            String exData = params.getString("data", "");
            if (!(exData.equalsIgnoreCase("") || exData.equalsIgnoreCase("null"))) {
                CheckInInf = new JSONObject(exData);
            }

            if (mMode != null && !mMode.equalsIgnoreCase("PF")) {
                CheckInInf.put("Mode", mMode);
                CheckInInf.put("Divcode", UserDetails.getString("Divcode", ""));
                CheckInInf.put("sfCode", UserDetails.getString("Sfcode", ""));
                String wrkType = "0";
                if (mMode.equals("onduty")) {
                    wrkType = "1";
                }

                String SftId = params.getString("ShiftId");
                if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {
                    if (!SftId.isEmpty()) {
                        CheckInInf.put("Shift_Selected_Id", SftId);
                        CheckInInf.put("Shift_Name", params.getString("ShiftName"));
                        CheckInInf.put("ShiftStart", params.getString("ShiftStart"));
                        CheckInInf.put("ShiftEnd", params.getString("ShiftEnd"));
                        CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                    }
                    CheckInInf.put("App_Version", BuildConfig.VERSION_NAME);
                    CheckInInf.put("WrkType", wrkType);
                    CheckInInf.put("CheckDutyFlag", "0");
                    CheckInInf.put("On_Duty_Flag", wrkType);
                    CheckInInf.put("PlcID", onDutyPlcID);
                    CheckInInf.put("PlcNm", onDutyPlcNm);
                    CheckInInf.put("vstRmks", VistPurpose);
                }

                if (mMode.equalsIgnoreCase("extended")) {
                    if (!SftId.isEmpty()) {
                        @SuppressLint("SimpleDateFormat") DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        CheckInInf.put("Shift_Selected_Id", SftId);
                        CheckInInf.put("Shift_Name", params.getString("ShiftName"));
                        CheckInInf.put("ShiftStart", params.getString("ShiftStart"));
                        CheckInInf.put("ShiftEnd", params.getString("ShiftEnd"));
                        CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                        CheckInInf.put("App_Version", BuildConfig.VERSION_NAME);
                        CheckInInf.put("Ekey", "EK" + UserDetails.getString("Sfcode", "") + dfw.format(calobjw.getTime()).hashCode());
                        CheckInInf.put("update", "0");
                        CheckInInf.put("WrkType", "0");
                        CheckInInf.put("CheckDutyFlag", "0");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CameraPermission cameraPermission = new CameraPermission(CameraxActivity.this, getApplicationContext());
        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
        } else {
            Log.v("PERMISSION", "PERMISSION");
        }
        GetEkey();
    }

    private void GetEkey() {
        @SuppressLint("SimpleDateFormat") DateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calander = Calendar.getInstance();
        Ekey = "EK" + Shared_Common_Pref.Sf_Code + dateformet.format(calander.getTime()).hashCode();
    }

    private void createDirectory() {
        File dir = getExternalFilesDir(APP_DATA);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onClick() {
        binding.submit.setOnClickListener(v -> {
            if (validateInputs()) {
                saveImgPreview();
            }
        });
        binding.cameraxRightControls.setOnClickListener(v -> {
            startCamera(cameraFacing);
            binding.imageView.setVisibility(View.GONE);
            binding.submit.setVisibility(View.GONE);
            binding.cameraxRightControls.setVisibility(View.GONE);

            binding.cameraxLeftControls.setVisibility(View.VISIBLE);
            binding.cameraPreview.setVisibility(View.VISIBLE);
            binding.seekBarChangeBrightness.setVisibility(View.VISIBLE);
            binding.cameraxClickLayout.setVisibility(View.VISIBLE);
            binding.cameraFunctionContainer.setVisibility(View.VISIBLE);
        });
        binding.back.setOnClickListener(v -> onBackPressed());
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
    }

    private boolean validateInputs() {
        if ("".equals(mMode)){
            showError("Unable check in. try again");
            return false;
        }
        if (file == null){
            showError("Unable access check in image. try again");
            return false;
        }
        return true;
    }

    private void showError(String s) {
        Snackbar snack = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), s, Snackbar.LENGTH_SHORT);
        snack.setDuration(Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        TextView tvAction = view.findViewById(com.google.android.material.R.id.snackbar_action);
        tvAction.setTextSize(16);
        tvAction.setTextColor(Color.WHITE);
        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.warning));
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.warning));
        snack.show();
    }

    private void saveImgPreview() {
        if (file == null) return;
        imageFileName = file.getName();
        String fullPath = String.valueOf(file);

        if (mMode.equalsIgnoreCase("secondaryEventCapture")) {
            com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, imageFileName, "SecondaryEventCapture", new com.saneforce.godairy.Common_Class.Common_Class.ImageUploadListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Image captured successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("eventCaptureImageName", imageFileName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFail() {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (mMode.equalsIgnoreCase("primaryEventCapture")) {
            com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, imageFileName, "PrimaryEventCapture", new com.saneforce.godairy.Common_Class.Common_Class.ImageUploadListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "Image captured successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("eventCaptureImageName", imageFileName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFail() {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", String.valueOf(file));
            mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
            mIntent.putExtra("FileName", imageFileName);
            mIntent.putExtra("Mode", (mMode.equalsIgnoreCase("PF") ? "PROF" : "ATTN"));
            FileUploadService.enqueueWork(this, mIntent);

            assistantClass.showProgressDialog("Getting location...", false);
            assistantClass.getLocation(new LocationResponse() {
                @Override
                public void onSuccess(double _lat, double _lng) {
                    assistantClass.dismissProgressDialog();
                    lat = _lat;
                    lng = _lng;
                    saveCheckIn();
                }

                @Override
                public void onFailure() {
                    assistantClass.dismissProgressDialog();
                    assistantClass.showAlertDialogWithDismiss("Can't fetch your location. Please try again...");
                    showError("Location error. try again");
                }
            });
        }

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

    public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(binding.cameraPreview.getWidth(), binding.cameraPreview.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                androidx.camera.core.ImageCapture imageCapture = new androidx.camera.core.ImageCapture.Builder()
                        .setCaptureMode(androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
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

    public void takePicture(androidx.camera.core.ImageCapture imageCapture) {

        String userInfo = "MyPrefs";
        UserDetails = getSharedPreferences(userInfo, Context.MODE_PRIVATE);
        String SF_Code = UserDetails.getString("Sfcode", "");

        long tsLong = System.currentTimeMillis() / 1000;
        capturedImageName = SF_Code + "_" + Long.toString(tsLong) + ".jpg";

        file = new File(DIR, capturedImageName);
        androidx.camera.core.ImageCapture.OutputFileOptions outputFileOptions = new androidx.camera.core.ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new androidx.camera.core.ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    file = new File(DIR, capturedImageName);
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
                    binding.cameraFunctionContainer.setVisibility(View.GONE);
                    binding.cameraxLeftControls.setVisibility(View.GONE);
                    binding.cameraxClickLayout.setVisibility(View.GONE);


                    binding.submit.setVisibility(View.VISIBLE);
                    binding.imageView.setVisibility(View.VISIBLE);
                    binding.cameraxCustomController.setVisibility(View.VISIBLE);
                    binding.cameraxRightControls.setVisibility(View.VISIBLE);
                    binding.cameraPreview.setVisibility(View.INVISIBLE);
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

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        mlocation = location;
                    } catch (Exception ignored) {
                    }
                }
            });
        }
    }

    public void getMulipart(String path) {
        MultipartBody.Part imgg = convertimg("file", path);
        CallApiImage(UserDetails.getString("Sfcode", ""), imgg);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        try {
            if (!TextUtils.isEmpty(path)) {
                File file;
                file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(file);
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getPath(), requestBody);
            }
        } catch (Exception ignored) {
        }
        return yy;
    }

    public void CallApiImage(String values, MultipartBody.Part imgg) {
        Call<ResponseBody> Callto;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Callto = apiInterface.CheckImage(values, imgg);
        Callto.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String jsonData;
                        jsonData = response.body().string();
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            imagvalue = js.getString("url");
                        }
                    }

                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }

    private void saveCheckIn() {
        try {
            String CTime = DT.GetDateTime(getApplicationContext(), "HH:mm:ss");
            String CDate = DT.GetDateTime(getApplicationContext(), "yyyy-MM-dd");
            if (mMode.equalsIgnoreCase("onduty")) {
                PlaceName = CheckInInf.getString("onDutyPlcNm");
                PlaceId = CheckInInf.getString("onDutyPlcID");
                if (CheckInInf.has("vstPurpose"))
                    VistPurpose = CheckInInf.getString("vstPurpose");
            }
            CheckInInf.put("eDate", CDate + " " + CTime);
            CheckInInf.put("eTime", CTime);
            CheckInInf.put("UKey", UKey);
            CheckInInf.put("lat", lat);
            CheckInInf.put("long", lng);
            CheckInInf.put("Lattitude", lat);
            CheckInInf.put("Langitude", lng);

            try {
                if (mMode.equalsIgnoreCase("onduty")) {
                    CheckInInf.put("PlcNm", PlaceName);
                    CheckInInf.put("PlcID", PlaceId);
                } else {
                    CheckInInf.put("PlcNm", "");
                    CheckInInf.put("PlcID", "");
                }
                if (mMode.equalsIgnoreCase("holidayentry"))
                    CheckInInf.put("On_Duty_Flag", "1");
                else
                    CheckInInf.put("On_Duty_Flag", "0");

                CheckInInf.put("iimgSrc", imagePath);
                CheckInInf.put("slfy", imageFileName);
                CheckInInf.put("Rmks", vstPurpose);
                CheckInInf.put("vstRmks", VistPurpose);

                if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {
                    initSubmitProgressDialog("Check in please wait.");
                    JSONArray jsonarray = new JSONArray();
                    JSONObject paramObject = new JSONObject();
                    paramObject.put("TP_Attendance", CheckInInf);
                    jsonarray.put(paramObject);

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save", Ekey,
                            UserDetails.getString("Divcode", ""),
                            UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());

                    modelCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                submitProgressDialog.dismiss();

                                JsonObject jsonObject = response.body().getAsJsonObject();
                                String mStatus = jsonObject.get("success").getAsString();

                                if (mStatus.isEmpty()){
                                    showError("Unable to check in. try again");
                                    return;
                                }

                                if (mStatus.equalsIgnoreCase("true")) {
                                    SharedPreferences.Editor editor = CheckInDetails.edit();
                                    if (mMode.equalsIgnoreCase("CIN")) {
                                        try {
                                            editor.putString("Shift_Selected_Id", CheckInInf.getString("Shift_Selected_Id"));
                                            editor.putString("Shift_Name", CheckInInf.getString("Shift_Name"));
                                            editor.putString("ShiftStart", CheckInInf.getString("ShiftStart"));
                                            editor.putString("ShiftEnd", CheckInInf.getString("ShiftEnd"));
                                            editor.putString("ShiftCutOff", CheckInInf.getString("ShiftCutOff"));

                                            if (mMode.equalsIgnoreCase("ONDuty")) {
                                                mShared_common_pref.save(Shared_Common_Pref.DAMode, true);

                                                mLUService = new SANGPSTracker(context);
                                                myReceiver = new LocationReceiver();
                                                bindService(new Intent(context, SANGPSTracker.class), mServiceConection,
                                                        Context.BIND_AUTO_CREATE);
                                                LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver,
                                                        new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
                                                mLUService.requestLocationUpdates();
                                            }

                                            if (CheckInDetails.getString("FTime", "").equalsIgnoreCase(""))
                                                editor.putString("FTime", CTime);
                                            editor.putString("Logintime", CTime);

                                            if (mMode.equalsIgnoreCase("onduty"))
                                                editor.putString("On_Duty_Flag", "1");
                                            else
                                                editor.putString("On_Duty_Flag", "0");
                                            editor.putInt("Type", 0);
                                            editor.putBoolean("CheckIn", true);
                                            editor.apply();

                                            String mMessage = "Your Check-In Submitted Successfully";
                                            try {
                                                mMessage = jsonObject.get("Msg").getAsString();
                                            } catch (Exception ignored) {

                                            }

                                            AlertDialogBox.showDialog(CameraxActivity.this, HAPApp.Title, String.valueOf(Html.fromHtml(mMessage)), "Yes", "", false, new AlertBox() {
                                                @Override
                                                public void PositiveMethod(DialogInterface dialog, int id) {
                                                    if (mStatus.equalsIgnoreCase("true")) {
                                                        TrackLocation();
                                                        Intent Dashboard = new Intent(context, Dashboard_Two.class);
                                                        Dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        Dashboard.putExtra("Mode", "CIN");
                                                        startActivity(Dashboard);
                                                    }
                                                    CameraxActivity.this.finish();
                                                }

                                                @Override
                                                public void NegativeMethod(DialogInterface dialog, int id) {

                                                }
                                            });
                                        } catch (JSONException e) {
                                           // throw new RuntimeException(e);
                                            showError("Unable to check in. try again");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                            submitProgressDialog.dismiss();
                            Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (mMode.equalsIgnoreCase("extended")) {
                    JSONArray jsonarray = new JSONArray();
                    JSONObject paramObject = new JSONObject();
                    paramObject.put("extended_entry", CheckInInf);
                    jsonarray.put(paramObject);

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save", Ekey,
                            UserDetails.getString("Divcode", ""),
                            UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());
                    modelCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                            if (submitProgressDialog != null) {
                                submitProgressDialog.dismiss();
                            }
                            if (response.isSuccessful()) {
                                JsonObject itm = response.body().getAsJsonObject();
                                SharedPreferences.Editor editor = CheckInDetails.edit();
                                editor.putInt("Type", 1);
                                editor.putBoolean("CheckIn", true);
                                editor.apply();
                                String mMessage = "Your Extended Shift Submitted Successfully";
                                try {
                                    mMessage = itm.get("Msg").getAsString();
                                } catch (Exception ignored) {
                                }

                                new AlertDialog.Builder(CameraxActivity.this)
                                        .setTitle(HAPApp.Title)
                                        .setMessage(Html.fromHtml(mMessage))
                                        .setPositiveButton("OK", (dialogInterface, i) -> {
                                            TrackLocation();
                                            Intent Dashboard = new Intent(CameraxActivity.this, Dashboard_Two.class);
                                            Dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Dashboard.putExtra("Mode", "extended");
                                            CameraxActivity.this.startActivity(Dashboard);
                                            CameraxActivity.this.finish();
                                        })
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                            submitProgressDialog.dismiss();
                            Log.d("HAP_receive", "");
                        }
                    });
                } else {
                    sendOFFlineLocations();
                    initSubmitProgressDialog("Check out please wait.");
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    String lMode = "get/logouttime";
                    if (mMode.equalsIgnoreCase("EXOUT")) {
                        lMode = "get/Extendlogout";
                    }
                    Call<JsonObject> modelCall = apiInterface.JsonSave(lMode, Ekey,
                            UserDetails.getString("Divcode", ""),
                            UserDetails.getString("Sfcode", ""), "", "", CheckInInf.toString());
                    modelCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                            submitProgressDialog.dismiss();
                            if (response.isSuccessful()) {
                                Log.e("TOTAL_REPOSNEaaa", String.valueOf(response.body()));
                                SharedPreferences.Editor loginsp = UserDetails.edit();
                                loginsp.putBoolean("Login", false);
                                loginsp.apply();
                                SharedPreferences.Editor editor = CheckInDetails.edit();
                                editor.putString("Logintime", "");
                                editor.putBoolean("CheckIn", false);
                                editor.apply();
                                mShared_common_pref.clear_pref(Shared_Common_Pref.DAMode);

                                Intent playIntent = new Intent(CameraxActivity.this, SANGPSTracker.class);
                                stopService(playIntent);

                                JsonObject itm = response.body().getAsJsonObject();
                                String mMessage = "Your Check-Out Submitted Successfully<br><br>Check in Time  : " + CheckInDetails.getString("FTime", "") + "<br>" +
                                        "Check Out Time : " + CTime;

                                try {
                                    mMessage = itm.get("Msg").getAsString();
                                } catch (Exception ignored) {
                                }

                                AlertDialogBox.showDialog(CameraxActivity.this, HAPApp.Title, String.valueOf(Html.fromHtml(mMessage)), "Ok", "", false, new AlertBox() {
                                    @Override
                                    public void PositiveMethod(DialogInterface dialog, int id) {

                                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                        Call<JsonArray> Callto = apiInterface.getDataArrayList("get/CLSExp",
                                                UserDetails.getString("Divcode", ""),
                                                UserDetails.getString("Sfcode", ""), CDate);

                                        Callto.enqueue(new Callback<>() {
                                            @Override
                                            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                                                common_class.clearLocData(CameraxActivity.this);
                                                mShared_common_pref.clear_pref(Constants.DB_TWO_GET_MREPORTS);
                                                mShared_common_pref.clear_pref(Constants.DB_TWO_GET_DYREPORTS);
                                                mShared_common_pref.clear_pref(Constants.DB_TWO_GET_NOTIFY);
                                                mShared_common_pref.clear_pref(Constants.LOGIN_DATA);
                                                finishAffinity();
                                                if (response.body().size() > 0) {
                                                    Intent takePhoto = new Intent(CameraxActivity.this, AllowanceActivityTwo.class);
                                                    takePhoto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    takePhoto.putExtra("Mode", "COUT");
                                                    startActivity(takePhoto);
                                                } else {
                                                    Intent Dashboard = new Intent(CameraxActivity.this, Login.class);
                                                    Dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(Dashboard);
                                                    CameraxActivity.this.finish();
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
                        public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                            submitProgressDialog.dismiss();
                        }
                    });
                }
//                }
            } catch (Exception ignored) {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
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

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SANGPSTracker.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void TrackLocation() {
        mLUService = new SANGPSTracker(getApplicationContext());
        if (!isMyServiceRunning()) {
            try {
                Intent playIntent = new Intent(CameraxActivity.this, SANGPSTracker.class);
                bindService(playIntent, mServiceConection, Context.BIND_AUTO_CREATE);
                mLUService.requestLocationUpdates();
                LocalBroadcastManager.getInstance(this).registerReceiver(new LocationReceiver(), new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shared_Common_Pref.Outletlat = 0.0;
        Shared_Common_Pref.Outletlong = 0.0;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("mandatory")) {
            if (mandatory != 1) {
                assistantClass.showAlertDialog("", "Do you want to continue without photo capture?", true, "CANCEL", "OK", new AlertDialogClickListener() {
                    @Override
                    public void onPositiveButtonClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeButtonClick(DialogInterface dialog) {
                        finish();
                    }
                });
            }
        } else {
            super.onBackPressed();
        }
    }
}