package com.saneforce.godairy.Activity_Hap;

import static android.app.PendingIntent.getActivity;
import static com.saneforce.godairy.SFA_Activity.HAPApp.sendOFFlineLocations;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.location.Address;
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
import com.saneforce.godairy.Activity.AllowanceActivityTwo;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.CameraPermission;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.common.AlmReceiver;
import com.saneforce.godairy.common.FileUploadService;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.common.LocationReceiver;
import com.saneforce.godairy.common.SANGPSTracker;
import com.saneforce.godairy.databinding.ActivityCameraxBinding;
import com.saneforce.godairy.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private ActivityCameraxBinding binding;
    private Context context = this;
    private static final String TAG = "ImageCapture";

    private Button button;
    private String imagePath, imageFileName;
    private ProgressDialog mProgress;
    private File file;

    private JSONObject CheckInInf;
    private Shared_Common_Pref mShared_common_pref;
    private SharedPreferences CheckInDetails, UserDetails, sharedpreferences;
    private Common_Class DT = new Common_Class();
    private String VistPurpose = "", UKey = "", DIR, onDutyPlcID, onDutyPlcNm, vstPurpose, PlaceId = "", PlaceName = "";
    private String sStatus, mMode="", mModeRetailorCapture, WrkType, UserInfo = "MyPrefs", imagvalue = "", mypreference = "mypref";
    private com.saneforce.godairy.Common_Class.Common_Class common_class;

    public static final String sCheckInDetail = "CheckInDetail";
    public static final String sUserDetail = "MyPrefs";

    private SANGPSTracker mLUService;
    private boolean mBound = false;
    private LocationReceiver myReceiver;
    private Location mlocation;

    public static final String APP_DATA = "/.saneforce";
    private Camera camera;
    private Bitmap bitmap;
    private Dialog submitProgressDialog, checkInSuccessDialog;
    private String capturedImageName;

    double lat = 0, lng = 0;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraxBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String intentMode = getIntent().getStringExtra("Mode");

        if (intentMode.equals("COUT")){
            binding.headerText.setText("Check Out");
        }

        if (intentMode.equals("EXOUT")){
            binding.headerText.setText("Check Out");
        }

        onClick();
        cameraPermission();
        DIR = getExternalFilesDir("/").getPath() + "/" + ".saneforce/";
        createDirectory();

        mShared_common_pref = new Shared_Common_Pref(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        CheckInInf = new JSONObject();
        CheckInDetails = getSharedPreferences(sCheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(sUserDetail, Context.MODE_PRIVATE);
        common_class = new com.saneforce.godairy.Common_Class.Common_Class(this);

        new LocationFinder(getApplication(), location -> {
            mlocation = location;
            if (location != null) {
                try {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                } catch (Exception e) { }
            }
        });
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
                WrkType = "0";
                if (mMode.equals("onduty")) {
                    WrkType = "1";
                }
                Log.e("Checkin_Mode", mMode);
                String SftId = params.getString("ShiftId");
                if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {
                    if (!(SftId.isEmpty() || SftId.equalsIgnoreCase(""))) {
                        CheckInInf.put("Shift_Selected_Id", SftId);
                        CheckInInf.put("Shift_Name", params.getString("ShiftName"));
                        CheckInInf.put("ShiftStart", params.getString("ShiftStart"));
                        CheckInInf.put("ShiftEnd", params.getString("ShiftEnd"));
                        CheckInInf.put("ShiftCutOff", params.getString("ShiftCutOff"));
                    }
                    CheckInInf.put("App_Version", BuildConfig.VERSION_NAME);
                    CheckInInf.put("WrkType", WrkType);
                    CheckInInf.put("CheckDutyFlag", "0");
                    CheckInInf.put("On_Duty_Flag", WrkType);
                    CheckInInf.put("PlcID", onDutyPlcID);
                    CheckInInf.put("PlcNm", onDutyPlcNm);
                    CheckInInf.put("vstRmks", VistPurpose);
                }

                if (mMode.equalsIgnoreCase("extended")) {
                    if (!(SftId.isEmpty() || SftId.equalsIgnoreCase(""))) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
      //  startCamera(0, 0, dpWidth, dpHeight, "front", false, false, false, "1", false, false, true);

        }
        button = (Button) findViewById(R.id.button_capture);
    }

    private void createDirectory() {
        File dir = getExternalFilesDir(APP_DATA);
        if(!dir.exists()) {
            if (!dir.mkdir()) {
                Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onClick() {
        binding.submit.setOnClickListener(v -> saveImgPreview());
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
    }

    private void saveImgPreview() {
        if(file==null) return;
        imageFileName = file.getName();

        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", String.valueOf(file));
        mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
        mIntent.putExtra("FileName", imageFileName);
        mIntent.putExtra("Mode", (mMode.equalsIgnoreCase("PF") ? "PROF" : "ATTN"));
        FileUploadService.enqueueWork(this, mIntent);

        if (lat == 0 || lng == 0) {
            new LocationFinder(getApplication(), new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    if (location != null) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        saveCheckIn();
                    }
                }
            });
        } else {
            saveCheckIn();
        }


        // New design Not full used previous code

        /*
//                   try {
//
//
//            vwPreview = findViewById(R.id.ImgPreview);
//            ImageView imgPreview = findViewById(R.id.imgPreviewImg);
//            BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));
//
//            vwPreview.setBackground(drawableBitmap);
//            String filePath = String.valueOf(file);
//            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//            imgPreview.setImageBitmap(bitmap);
//
//            if (mModeRetailorCapture == null) {
//
//                Intent mIntent = new Intent(this, FileUploadService.class);
//                mIntent.putExtra("mFilePath", String.valueOf(file));
//                mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
//                mIntent.putExtra("FileName", imageFileName);
//                mIntent.putExtra("Mode", (mMode.equalsIgnoreCase("PF") ? "PROF" : "ATTN"));
//                FileUploadService.enqueueWork(this, mIntent);
//                Log.e("Image_Capture", Uri.fromFile(file).toString());
//                Log.e("Image_Capture", "IAMGE     " + bitmap);
//            }
//
//            if (mModeRetailorCapture != null && mModeRetailorCapture.equals("NewRetailor")) {
////                Intent mIntent = new Intent(this, AddNewRetailer.class);
////                mIntent.putExtra("mFilePath", Uri.fromFile(file).toString());
////                startActivity(mIntent);
//
//                mShared_common_pref.save(Constants.Retailor_FilePath, Uri.fromFile(file).toString());
//
//                finish();
//            } else if (mMode.equalsIgnoreCase("PF")) {
//                imagePickListener.OnImagePick(bitmap, imageFileName);
//                finish();
//            } else {
//                mProgress = new ProgressDialog(this);
//                String titleId = "Submiting";
//                mProgress.setTitle(titleId);
//                mProgress.setMessage("Preparing Please Wait...");
//                mProgress.show();
//                /*if (mlocation != null) {
//                    mProgress.setMessage("Submiting Please Wait...");
//                    vwPreview.setVisibility(View.GONE);
//                    // imgPreview.setImageURI(Uri.fromFile(file));
//                    button.setVisibility(View.GONE);
//                    saveCheckIn();
//                } else {*/
//        new LocationFinder(getApplication(), new LocationEvents() {
//            @Override
//            public void OnLocationRecived(Location location) {
//                mlocation = location;
//                mProgress.setMessage("Submiting Please Wait...");
//                vwPreview.setVisibility(View.GONE);
//                // imgPreview.setImageURI(Uri.fromFile(file));
//                button.setVisibility(View.GONE);
//                saveCheckIn();
//            }
//        });
//        //}
//    }
//} catch (Exception e) {
//        Log.e(TAG, e.getMessage());
//        }

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

        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        String SF_Code=UserDetails.getString("Sfcode","");

        long tsLong = System.currentTimeMillis() / 1000;
        capturedImageName = SF_Code +"_"+Long.toString(tsLong) + ".jpg";

        file = new File(DIR, capturedImageName);
        androidx.camera.core.ImageCapture.OutputFileOptions outputFileOptions = new androidx.camera.core.ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new androidx.camera.core.ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    // Toast.makeText(context, "Image saved at: " + file.getPath(), Toast.LENGTH_SHORT).show();
                    Log.e("image_capture", "Captured image save path :" + file.getPath());
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

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }
    }
//    private void StartSelfiCamera() {
//
//        if (mCamera != null) {
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = null;
//        }
//        preview = (SurfaceView) findViewById(R.id.PREVIEW);
//        mHolder = preview.getHolder();
//        mHolder.addCallback(this);
//        setDefaultCameraId((mCamId == 1) ? "front" : "back");
//        try {
//            mCamera = Camera.open(mCamId);
//            mCamera.setPreviewDisplay(mHolder);
//            setCameraDisplayOrientation();
//            mCamera.startPreview();
//        } catch (IOException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
//            e.printStackTrace();
//        }
//
//        Log.e("mCAmer_id", String.valueOf(mCamId));
//
//    }

//    public static void setOnImagePickListener(OnImagePickListener mImagePickListener) {
//        imagePickListener = mImagePickListener;
//    }

//    @Override
//    public void onPictureTaken(String originalPicture, String picData) {
//        //File imgFile=new File(originalPicture);
//        FrameLayout preview = findViewById(R.id.preview);
//        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
//        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
//        file = new File(originalPicture);
//        imagePath = originalPicture;
//        imageFileName = originalPicture.substring(originalPicture.lastIndexOf("/") + 1);
//        imgPreview.setImageURI(Uri.fromFile(file));
//        vwPreview.setVisibility(View.VISIBLE);
//        imgPreview.setVisibility(View.VISIBLE);
//        button.setVisibility(View.GONE);
//    }


    //    public void takePicture() {
//
//        String usrNm=UserDetails.getString("Sfcode","");
//        long tsLong = System.currentTimeMillis() / 1000;
//        imageFileName = usrNm+"_"+Long.toString(tsLong) + ".jpg";
//        //file  = new File(Environment.getExternalStorageDirectory() + "/"+ts+".jpg");
//        imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + imageFileName;
//        file = new File(imagePath);
//        try {
//            mCamera.takePicture(null, null,
//                    new Camera.PictureCallback() {
//                        @Override
//                        public void onPictureTaken(byte[] bytes, Camera camera) {
//                            Bitmap bm = null;
//                            try {
//                                if (bytes != null) {
//                                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
//                                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
//                                    bm = BitmapFactory.decodeByteArray(bytes, 0, (bytes != null) ? bytes.length : 0);
//
//                                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                                        // Notice that width and height are reversed
//                                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
//                                        int w = scaled.getWidth();
//                                        int h = scaled.getHeight();
//                                        w = bm.getWidth();
//                                        h = bm.getHeight();
//                                        // Setting post rotate to 90
//                                        Matrix mtx = new Matrix();
//
//                                        int CameraEyeValue = setPhotoOrientation(ImageCapture.this, mCamId); // CameraID = 1 : front 0:back
//                                        if (mCamId == 1) { // As Front camera is Mirrored so Fliping the Orientation
//                                            if (CameraEyeValue == 270) {
//                                                mtx.postRotate(90);
//                                            } else if (CameraEyeValue == 90) {
//                                                mtx.postRotate(270);
//                                            }
//                                        } else {
//                                            mtx.postRotate(CameraEyeValue); // CameraEyeValue is default to Display Rotation
//                                        }
//                                        bm = applyMatrix(bm, mtx);
//                                        // bm = Bitmap.createBitmap(bm, 0, 0, w, h, mtx, true);
//                                    } else {// LANDSCAPE MODE
//                                        //No need to reverse width and height
//                                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
//                                        bm = scaled;
//                                    }
//                                }
//
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                                byte[] byteArray = stream.toByteArray();
//
//                                save(byteArray);
//                                ShowImgPreview();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onResume();
//    }



//    private void ShowImgPreview() {
//        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
//        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
//        vwPreview.setVisibility(View.VISIBLE);
//        imgPreview.setImageURI(Uri.fromFile(file));
//        button.setVisibility(View.GONE);
//        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));
//
//        vwPreview.setBackground(drawableBitmap);
//
//        Log.v("CAMERA_FOCUS_Preview", String.valueOf(mCamId));
//
//        if (mCamId == 1) {
//            imgPreview.setRotation((float) -90.0);
//        } else if (mCamId == 2) {
//            imgPreview.setRotation((float) 90.0);
//        } else {
//            imgPreview.setRotation((float) 270.0);
//        }
//
//    }

//    private void CloseImgPreview() {
//        vwPreview = findViewById(R.id.ImgPreview);
//        vwPreview.setVisibility(View.GONE);
//        button.setVisibility(View.VISIBLE);
//    }

//    private void saveImgPreview() {
//        try {
//
//
//            vwPreview = findViewById(R.id.ImgPreview);
//            ImageView imgPreview = findViewById(R.id.imgPreviewImg);
//            BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));
//
//            vwPreview.setBackground(drawableBitmap);
//            String filePath = String.valueOf(file);
//            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//            imgPreview.setImageBitmap(bitmap);
//
//            if (mModeRetailorCapture == null) {
//
//                Intent mIntent = new Intent(this, FileUploadService.class);
//                mIntent.putExtra("mFilePath", String.valueOf(file));
//                mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
//                mIntent.putExtra("FileName", imageFileName);
//                mIntent.putExtra("Mode", (mMode.equalsIgnoreCase("PF") ? "PROF" : "ATTN"));
//                FileUploadService.enqueueWork(this, mIntent);
//                Log.e("Image_Capture", Uri.fromFile(file).toString());
//                Log.e("Image_Capture", "IAMGE     " + bitmap);
//            }
//
//            if (mModeRetailorCapture != null && mModeRetailorCapture.equals("NewRetailor")) {
////                Intent mIntent = new Intent(this, AddNewRetailer.class);
////                mIntent.putExtra("mFilePath", Uri.fromFile(file).toString());
////                startActivity(mIntent);
//
//                mShared_common_pref.save(Constants.Retailor_FilePath, Uri.fromFile(file).toString());
//
//                finish();
//            } else if (mMode.equalsIgnoreCase("PF")) {
//                imagePickListener.OnImagePick(bitmap, imageFileName);
//                finish();
//            } else {
//                mProgress = new ProgressDialog(this);
//                String titleId = "Submiting";
//                mProgress.setTitle(titleId);
//                mProgress.setMessage("Preparing Please Wait...");
//                mProgress.show();
//                /*if (mlocation != null) {
//                    mProgress.setMessage("Submiting Please Wait...");
//                    vwPreview.setVisibility(View.GONE);
//                    // imgPreview.setImageURI(Uri.fromFile(file));
//                    button.setVisibility(View.GONE);
//                    saveCheckIn();
//                } else {*/
//                new LocationFinder(getApplication(), new LocationEvents() {
//                    @Override
//                    public void OnLocationRecived(Location location) {
//                        mlocation = location;
//                        mProgress.setMessage("Submiting Please Wait...");
//                        vwPreview.setVisibility(View.GONE);
//                        // imgPreview.setImageURI(Uri.fromFile(file));
//                        button.setVisibility(View.GONE);
//                        saveCheckIn();
//                    }
//                });
//                //}
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        mlocation = location;
//                        ImageCapture.vwPreview.setVisibility(View.GONE);
//                        // imgPreview.setImageURI(Uri.fromFile(file));
//                        button.setVisibility(View.GONE);
//                        saveCheckIn();
                    } catch (Exception e) {
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
        Log.v("full_profile", path);
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
        } catch (Exception e) {
        }
        Log.v("full_profile", yy + "");
        return yy;
    }

    public void CallApiImage(String values, MultipartBody.Part imgg) {
        Call<ResponseBody> Callto;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Callto = apiInterface.CheckImage(values, imgg);

        Log.v("print_upload_file", Callto.request().toString());
        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());
                //uploading.setText("Uploading "+String.valueOf(count)+"/"+String.valueOf(count_check));

                try {
                    if (response.isSuccessful()) {
                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            imagvalue = js.getString("url");
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

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses != null) {
                    Address address = addresses.get(0);
                    StringBuilder fullAddress = new StringBuilder();
                    if (address != null) {
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            fullAddress.append(address.getAddressLine(i)).append("\n");
                        }
                    }
                    CheckInInf.put("address", fullAddress.toString().trim());
                    CheckInInf.put("locality", address.getLocality().trim());
                    CheckInInf.put("postalCode", address.getPostalCode().trim());
                    Log.e("fullAddress", fullAddress.toString().trim());

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
                        Log.e("CHECK_IN_DETAILS", String.valueOf(paramObject));

                        jsonarray.put(paramObject);


                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                                UserDetails.getString("Divcode", ""),
                                UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());

                        Log.v("PRINT_REQUEST", modelCall.request().toString());

                        modelCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    JsonObject itm = response.body().getAsJsonObject();
                                    Log.e("RESPONSE_FROM_SERVER", String.valueOf(response.body().getAsJsonObject()));
                                    submitProgressDialog.dismiss();
                                    sStatus = itm.get("success").getAsString();
                                    if (sStatus.equalsIgnoreCase("true")) {
                                        SharedPreferences.Editor editor = CheckInDetails.edit();
                                        if (mMode.equalsIgnoreCase("CIN")) {
                                            try {
                                                editor.putString("Shift_Selected_Id", CheckInInf.getString("Shift_Selected_Id"));
                                                editor.putString("Shift_Name", CheckInInf.getString("Shift_Name"));
                                                editor.putString("ShiftStart", CheckInInf.getString("ShiftStart"));
                                                editor.putString("ShiftEnd", CheckInInf.getString("ShiftEnd"));
                                                editor.putString("ShiftCutOff", CheckInInf.getString("ShiftCutOff"));

                                                long AlrmTime = DT.getDate(CheckInInf.getString("ShiftEnd")).getTime();
                                            } catch (Exception ignored) { }
//                                                sendAlarmNotify(1001, AlrmTime, HAPApp.Title, "Check-Out Alert !.");
                                        }

                                        if (mMode.equalsIgnoreCase("ONDuty")) {
                                            mShared_common_pref.save(Shared_Common_Pref.DAMode, true);

                                            mLUService = new SANGPSTracker(CameraxActivity.this);
                                            myReceiver = new LocationReceiver();
                                            bindService(new Intent(CameraxActivity.this, SANGPSTracker.class), mServiceConection,
                                                    Context.BIND_AUTO_CREATE);
                                            LocalBroadcastManager.getInstance(CameraxActivity.this).registerReceiver(myReceiver,
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
                                    }
                                    String mMessage = "Your Check-In Submitted Successfully";
                                    try {
                                        mMessage = itm.get("Msg").getAsString();
                                    } catch (Exception e) {
                                    }

                                    AlertDialogBox.showDialog(CameraxActivity.this, HAPApp.Title, String.valueOf(Html.fromHtml(mMessage)), "Yes", "", false, new AlertBox() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            if (sStatus.equalsIgnoreCase("true")) {
                                                TrackLocation();
                                                Intent Dashboard = new Intent(CameraxActivity.this, Dashboard_Two.class);
                                                Dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                Dashboard.putExtra("Mode", "CIN");
                                                startActivity(Dashboard);
                                            }
                                            ((AppCompatActivity) CameraxActivity.this).finish();
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
                                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("HAP_receive", "");
                            }
                        });
                    }
                    else if (mMode.equalsIgnoreCase("extended")) {
                        JSONArray jsonarray = new JSONArray();
                        JSONObject paramObject = new JSONObject();
                        paramObject.put("extended_entry", CheckInInf);
                        jsonarray.put(paramObject);
                        Log.e("CHECK_IN_DETAILS", String.valueOf(jsonarray));

                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> modelCall = apiInterface.JsonSave("dcr/save",
                                UserDetails.getString("Divcode", ""),
                                UserDetails.getString("Sfcode", ""), "", "", jsonarray.toString());
                        modelCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                submitProgressDialog.dismiss();
                                Log.e("RESPONSE_FROM_SERVER", String.valueOf(response.body().getAsJsonObject()));
                                if (response.isSuccessful()) {
                                    JsonObject itm = response.body().getAsJsonObject();
                                    SharedPreferences.Editor editor = CheckInDetails.edit();

                                    editor.putInt("Type", 1);
                                    editor.putBoolean("CheckIn", true);
                                    editor.apply();

                                    String mMessage = "Your Extended Submitted Successfully";
                                    try {
                                        mMessage = itm.get("Msg").getAsString();


                                    } catch (Exception e) {
                                    }

                                    AlertDialog alertDialog = new AlertDialog.Builder(CameraxActivity.this)
                                            .setTitle(HAPApp.Title)
                                            .setMessage(Html.fromHtml(mMessage))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    TrackLocation();
                                                    Intent Dashboard = new Intent(CameraxActivity.this, Dashboard_Two.class);
                                                    Dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    Dashboard.putExtra("Mode", "extended");
                                                    CameraxActivity.this.startActivity(Dashboard);
                                                    ((AppCompatActivity) CameraxActivity.this).finish();
                                                }
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
                        sendOFFlineLocations();
                        initSubmitProgressDialog("Check out please wait.");
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        String lMode = "get/logouttime";
                        if(mMode.equalsIgnoreCase("EXOUT")) {
                            lMode = "get/Extendlogout";
                        }
                        Call<JsonObject> modelCall = apiInterface.JsonSave(lMode,
                                UserDetails.getString("Divcode", ""),
                                UserDetails.getString("Sfcode", ""), "", "", CheckInInf.toString());
                        modelCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                submitProgressDialog.dismiss();
                                if (response.isSuccessful()) {
                                    Log.e("TOTAL_REPOSNEaaa", String.valueOf(response.body()));
                                    SharedPreferences.Editor loginsp = UserDetails.edit();
                                    loginsp.putBoolean("Login", false);
                                    loginsp.apply();
                                    Boolean Login = UserDetails.getBoolean("Login", false);
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
                                    } catch (Exception e) {
                                    }

                                    AlertDialogBox.showDialog(CameraxActivity.this, HAPApp.Title, String.valueOf(Html.fromHtml(mMessage)), "Ok", "", false, new AlertBox() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {

                                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                            Call<JsonArray> Callto = apiInterface.getDataArrayList("get/CLSExp",
                                                    UserDetails.getString("Divcode", ""),
                                                    UserDetails.getString("Sfcode", ""), CDate);

                                            Log.v("DATE_REQUEST", Callto.request().toString());
                                            Callto.enqueue(new Callback<JsonArray>() {
                                                @Override
                                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
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
                                                        ((AppCompatActivity) CameraxActivity.this).finish();
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<JsonArray> call, Throwable t) {

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
                }
            } catch (Exception e) {
                saveCheckIn();
                // Toast.makeText(context, "Map Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }

//    private void setDefaultCameraId(String cam) {
//        noOfCameras = Camera.getNumberOfCameras();
//        int facing = cam.equalsIgnoreCase("front") ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
//        Log.v("CAMERA_FOCUS", String.valueOf(facing));
//        mCamId = facing;
//
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        for (int i = 0; i < noOfCameras; i++) {
//            Camera.getCameraInfo(i, cameraInfo);
//            if (cameraInfo.facing == facing) {
//                //mCamId = i;
//            }
//        }
//    }

//    private boolean setExposureCompensation(int exposureCompensation) {
//
//
//        Camera camera = fragment.getCamera();
//        Camera.Parameters params = camera.getParameters();
//
//        int minExposureCompensation = camera.getParameters().getMinExposureCompensation();
//        int maxExposureCompensation = camera.getParameters().getMaxExposureCompensation();
//
//        if (minExposureCompensation == 0 && maxExposureCompensation == 0) {
//            Log.d("Cam Error", "Can't set Exposure");
//        } else {
//            if (exposureCompensation < minExposureCompensation) {
//                exposureCompensation = minExposureCompensation;
//            } else if (exposureCompensation > maxExposureCompensation) {
//                exposureCompensation = maxExposureCompensation;
//            }
//            params.setExposureCompensation(exposureCompensation);
//            camera.setParameters(params);
//        }
//
//        return true;
//    }

//    private String[] getSupportedWhiteBalanceModes() {
//        Camera camera = fragment.getCamera();
//        Camera.Parameters params = camera.getParameters();
//
//        List<String> supportedWhiteBalanceModes;
//        supportedWhiteBalanceModes = params.getSupportedWhiteBalance();
//
//        JSONArray jsonWhiteBalanceModes = new JSONArray();
//        String[] lstModes = new String[supportedWhiteBalanceModes.size()];
//        if (camera.getParameters().isAutoWhiteBalanceLockSupported()) {
//            jsonWhiteBalanceModes.put(new String("lock"));
//        }
//        if (supportedWhiteBalanceModes != null) {
//            for (int i = 0; i < supportedWhiteBalanceModes.size(); i++) {
//                jsonWhiteBalanceModes.put(new String(supportedWhiteBalanceModes.get(i)));
//                lstModes[i] = supportedWhiteBalanceModes.get(i);
//            }
//        }
//
//        // callbackContext.success(jsonWhiteBalanceModes);
//        return lstModes;
//    }

//    private boolean getWhiteBalanceMode() {
//        Camera camera = fragment.getCamera();
//        Camera.Parameters params = camera.getParameters();
//
//        String whiteBalanceMode;
//
//        if (camera.getParameters().isAutoWhiteBalanceLockSupported()) {
//            if (camera.getParameters().getAutoWhiteBalanceLock()) {
//                whiteBalanceMode = "lock";
//            } else {
//                whiteBalanceMode = camera.getParameters().getWhiteBalance();
//            }
//            ;
//        } else {
//            whiteBalanceMode = camera.getParameters().getWhiteBalance();
//        }
//        if (whiteBalanceMode != null) {
//            //callbackContext.success(whiteBalanceMode);
//        } else {
//            Log.e("Cam Error", "White balance mode not supported");
//        }
//
//        return true;
//    }
//
//    private boolean setWhiteBalanceMode(String whiteBalanceMode) {
//        Camera.Parameters params = fragment.getCamera().getParameters();
//
//        if (whiteBalanceMode.equals("lock")) {
//            if (fragment.getCamera().getParameters().isAutoWhiteBalanceLockSupported()) {
//                params.setAutoWhiteBalanceLock(true);
//                fragment.setCameraParameters(params);
//            } else {
//                Log.e("Cam Error", "White balance lock not supported");
//            }
//        } else if (whiteBalanceMode.equals("auto") ||
//                whiteBalanceMode.equals("incandescent") ||
//                whiteBalanceMode.equals("cloudy-daylight") ||
//                whiteBalanceMode.equals("daylight") ||
//                whiteBalanceMode.equals("fluorescent") ||
//                whiteBalanceMode.equals("shade") ||
//                whiteBalanceMode.equals("twilight") ||
//                whiteBalanceMode.equals("warm-fluorescent")) {
//            params.setWhiteBalance(whiteBalanceMode);
//            fragment.setCameraParameters(params);
//        } else {
//            Log.e("Cam Error", "White balance parameter not supported");
//        }
//
//        return true;
//    }
//
//    public void setCameraDisplayOrientation() {
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(mCamId, info);
//        int rotation = getWindowManager().getDefaultDisplay().getRotation();
//        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setRotation(-rotation);
//        mCamera.setParameters(parameters);
//        mCamera.setDisplayOrientation(90);
//        mCamera.startPreview();
//
//        int degrees = 0;
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//        }
//    }

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
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_MUTABLE);
        }

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, AlmTm, pIntent);
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void TrackLocation() {
        mLUService = new SANGPSTracker(getApplicationContext());
        if (!isMyServiceRunning(SANGPSTracker.class)) {
            try {
                Intent playIntent = new Intent(CameraxActivity.this, SANGPSTracker.class);
                bindService(playIntent, mServiceConection, Context.BIND_AUTO_CREATE);
                mLUService.requestLocationUpdates();
                LocalBroadcastManager.getInstance(this).registerReceiver(new LocationReceiver(), new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
            } catch (Exception ignored) { }
        }
    }
}