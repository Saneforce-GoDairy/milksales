package com.saneforce.godairy.Activity_Hap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity.AllowanceActivityTwo;
import com.saneforce.godairy.BuildConfig;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.CameraPermission;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.common.AlmReceiver;
import com.saneforce.godairy.common.Camera.CameraActivity;
import com.saneforce.godairy.common.FileUploadService;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.common.LocationReceiver;
import com.saneforce.godairy.common.SANGPSTracker;

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

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageCapture extends AppCompatActivity implements CameraActivity.CameraPreviewListener {
    private static final String TAG = "ImageCapture";
    private CameraActivity fragment;

    Button button;
    TextureView textureView;
    ImageView btnFlash, btnWBal;
    ImageView btnSwchCam;
    ListView lstFlashMode, lstWBalance;
    LinearLayout lstModalFlash, lstModalWBal;
    SeekBar skBarBright;
    String imagePath;
    String imageFileName;
    private ProgressDialog mProgress;
    public static RelativeLayout vwPreview;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    static OnImagePickListener imagePickListener;
    Camera mCamera;
    int mCamId = 1;
    String[] flashModes = {"OFF", "Auto", "ON", "Torch"};
    String[] WBModes;
    private File file;
    SurfaceView preview;
    SurfaceHolder mHolder;
    private int noOfCameras;
    SharedPreferences sharedpreferences;
    JSONObject CheckInInf;
    Shared_Common_Pref mShared_common_pref;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    Common_Class DT = new Common_Class();
    String VistPurpose = "";
    String sStatus, mMode, mModeRetailorCapture, WrkType, onDutyPlcID, onDutyPlcNm, vstPurpose, UserInfo = "MyPrefs", imagvalue = "", mypreference = "mypref", PlaceId = "", PlaceName = "";
    com.saneforce.godairy.Common_Class.Common_Class common_class;

    public static final String sCheckInDetail = "CheckInDetail";
    public static final String sUserDetail = "MyPrefs";

    Button btnRtPrv, btnOkPrv;

    private SANGPSTracker mLUService;
    private boolean mBound = false;
    private LocationReceiver myReceiver;
    Location mlocation;
    String UKey = "";

    int dpHeight, dpWidth;
    int picHeight, picWidth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        mShared_common_pref = new Shared_Common_Pref(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        CheckInInf = new JSONObject();
        CheckInDetails = getSharedPreferences(sCheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(sUserDetail, Context.MODE_PRIVATE);
        common_class = new com.saneforce.godairy.Common_Class.Common_Class(this);

        new LocationFinder(getApplication(), new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                mlocation = location;
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

        CameraPermission cameraPermission = new CameraPermission(ImageCapture.this, getApplicationContext());
        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
//            StartSelfiCamera();
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            picHeight = outMetrics.heightPixels;
            picWidth = outMetrics.widthPixels;
            float density = getResources().getDisplayMetrics().density;
            dpHeight = (int) (outMetrics.heightPixels / density);
            dpWidth = (int) (outMetrics.widthPixels / density) + 1;
            startCamera(0, 0, dpWidth, dpHeight, "front", false, false, false, "1", false, false, true);

        }

        textureView = (TextureView) findViewById(R.id.ImagePreview);
        button = (Button) findViewById(R.id.button_capture);
        btnRtPrv = (Button) findViewById(R.id.btnRtPrv);
        btnOkPrv = (Button) findViewById(R.id.btnOkPrv);
        btnFlash = (ImageView) findViewById(R.id.button_flash);
        btnWBal = (ImageView) findViewById(R.id.button_WBalance);
        btnSwchCam = (ImageView) findViewById(R.id.button_switchCam);
        lstModalFlash = (LinearLayout) findViewById(R.id.lstMFlash);
        lstModalWBal = (LinearLayout) findViewById(R.id.lstMWBalance);
        lstFlashMode = (ListView) findViewById(R.id.lstFlashMode);
        lstWBalance = (ListView) findViewById(R.id.lstWBalance);

        ArrayAdapter simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, flashModes);
        lstFlashMode.setAdapter(simpleAdapter);//sets the adapter for listView

        //perform listView item click event
        lstFlashMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lstModalFlash.setVisibility(View.GONE);
            }
        });
        //  if (mCamId == 0) {
        lstWBalance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lstModalWBal.setVisibility(View.GONE);
                setWhiteBalanceMode(WBModes[i]);
            }
        });
        //      }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.takePicture(dpWidth, dpHeight, 80);//takePicture();
            }
        });
        btnSwchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.switchCamera();
 /*               CameraPermission cameraPermission = new CameraPermission(ImageCapture.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {
                    Log.v("PERMISSION", "PERMISSION");

                    mCamId = (mCamId == 1) ? 0 : 1;
                    StartSelfiCamera();

                }*/


            }
        });
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lstModalFlash.setVisibility(View.VISIBLE);
            }
        });
        btnWBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WBModes = getSupportedWhiteBalanceModes();

                ArrayAdapter simpleWBAdapter = new ArrayAdapter<String>(ImageCapture.this, android.R.layout.simple_list_item_1, WBModes);
                lstWBalance.setAdapter(simpleWBAdapter);//sets the adapter for listView

                lstModalWBal.setVisibility(View.VISIBLE);
            }
        });
        btnRtPrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CloseImgPreview();
            }
        });
        btnOkPrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImgPreview();
            }
        });
        skBarBright = (SeekBar) findViewById(R.id.skBarBright);
        skBarBright.setProgress(50);
        skBarBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int bright = (int) ((Float.parseFloat(String.valueOf(progress)) / 100) * 40) - 20;
                Log.d("Brightness", String.valueOf(bright));
                setExposureCompensation(bright);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private boolean startCamera(int x, int y, int width, int height, String defaultCamera, Boolean tapToTakePicture, Boolean dragEnabled, final Boolean toBack, String alpha, boolean tapFocus, boolean disableExifHeaderStripping, boolean storeToFile) {
        Log.d(TAG, "start camera action");

        if (fragment != null) {
            Log.d(TAG, "Camera already started");
            return true;
        }

        final float opacity = Float.parseFloat(alpha);

        fragment = new CameraActivity();
        fragment.setEventListener(this);
        fragment.defaultCamera = defaultCamera;
        fragment.tapToTakePicture = tapToTakePicture;
        fragment.dragEnabled = dragEnabled;
        fragment.tapToFocus = tapFocus;
        fragment.disableExifHeaderStripping = disableExifHeaderStripping;
        fragment.storeToFile = storeToFile;
        fragment.toBack = toBack;

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // offset
        int computedX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, metrics);
        int computedY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y, metrics);

        // size
        int computedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, metrics);
        int computedHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, metrics);

        fragment.setRect(computedX, computedY, computedWidth, computedHeight);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //create or update the layout params for the container view
                FrameLayout containerView = (FrameLayout) findViewById(R.id.preview);
                //add the fragment to the container
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(containerView.getId(), fragment);
                fragmentTransaction.commit();
            }
        });

        return true;
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

    public static void setOnImagePickListener(OnImagePickListener mImagePickListener) {
        imagePickListener = mImagePickListener;
    }

    @Override
    public void onPictureTaken(String originalPicture, String picData) {
        //File imgFile=new File(originalPicture);
        FrameLayout preview = findViewById(R.id.preview);
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        file = new File(originalPicture);
        imagePath = originalPicture;
        imageFileName = originalPicture.substring(originalPicture.lastIndexOf("/") + 1);
        imgPreview.setImageURI(Uri.fromFile(file));
        vwPreview.setVisibility(View.VISIBLE);
        imgPreview.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
    }

    @Override
    public void onPictureTakenError(String message) {

    }

    @Override
    public void onSnapshotTaken(String originalPicture) {

    }

    @Override
    public void onSnapshotTakenError(String message) {

    }

    @Override
    public void onFocusSet(int pointX, int pointY) {

    }

    @Override
    public void onFocusSetError(String message) {

    }

    @Override
    public void onBackButton() {
        Log.d(TAG, "Back button Pressed...");
    }

    @Override
    public void onCameraStarted() {

    }

    @Override
    public void onStartRecordVideo() {

    }

    @Override
    public void onStartRecordVideoError(String message) {

    }

    @Override
    public void onStopRecordVideo(String file) {

    }

    @Override
    public void onStopRecordVideoError(String error) {

    }

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

    public int setPhotoOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public static Bitmap applyMatrix(Bitmap source, Matrix matrix) {
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void ShowImgPreview() {
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageURI(Uri.fromFile(file));
        button.setVisibility(View.GONE);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));

        vwPreview.setBackground(drawableBitmap);

        Log.v("CAMERA_FOCUS_Preview", String.valueOf(mCamId));

        if (mCamId == 1) {
            imgPreview.setRotation((float) -90.0);
        } else if (mCamId == 2) {
            imgPreview.setRotation((float) 90.0);
        } else {
            imgPreview.setRotation((float) 270.0);
        }

    }

    private void CloseImgPreview() {
        vwPreview = findViewById(R.id.ImgPreview);
        vwPreview.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }

    private void saveImgPreview() {
        try {


            vwPreview = findViewById(R.id.ImgPreview);
            ImageView imgPreview = findViewById(R.id.imgPreviewImg);
            BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));

            vwPreview.setBackground(drawableBitmap);
            String filePath = String.valueOf(file);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imgPreview.setImageBitmap(bitmap);

            if (mModeRetailorCapture == null) {

                Intent mIntent = new Intent(this, FileUploadService.class);
                mIntent.putExtra("mFilePath", String.valueOf(file));
                mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
                mIntent.putExtra("FileName", imageFileName);
                mIntent.putExtra("Mode", (mMode.equalsIgnoreCase("PF") ? "PROF" : "ATTN"));
                FileUploadService.enqueueWork(this, mIntent);
                Log.e("Image_Capture", Uri.fromFile(file).toString());
                Log.e("Image_Capture", "IAMGE     " + bitmap);
            }

            if (mModeRetailorCapture != null && mModeRetailorCapture.equals("NewRetailor")) {
//                Intent mIntent = new Intent(this, AddNewRetailer.class);
//                mIntent.putExtra("mFilePath", Uri.fromFile(file).toString());
//                startActivity(mIntent);

                mShared_common_pref.save(Constants.Retailor_FilePath, Uri.fromFile(file).toString());

                finish();
            } else if (mMode.equalsIgnoreCase("PF")) {
                Log.e(TAG, "Capture profile image");
                imagePickListener.OnImagePick(bitmap, imageFileName);
                String debug = "test";
                finish();
            } else {
                mProgress = new ProgressDialog(this);
                String titleId = "Submiting";
                mProgress.setTitle(titleId);
                mProgress.setMessage("Preparing Please Wait...");
                mProgress.show();
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
                        mlocation = location;
                        mProgress.setMessage("Submiting Please Wait...");
                        vwPreview.setVisibility(View.GONE);
                        // imgPreview.setImageURI(Uri.fromFile(file));
                        button.setVisibility(View.GONE);
                        saveCheckIn();
                    }
                });
                //}
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
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

            Location location = mlocation;//Common_Class.location;//locationFinder.getLocation();
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
            double lat = 0, lng = 0;
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            CheckInInf.put("lat", lat);
            CheckInInf.put("long", lng);
            CheckInInf.put("Lattitude", lat);
            CheckInInf.put("Langitude", lng);

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

            Log.e("Image_Capture", imagePath);
            Log.e("Image_Capture", imageFileName);


            if (mMode.equalsIgnoreCase("CIN") || mMode.equalsIgnoreCase("onduty") || mMode.equalsIgnoreCase("holidayentry")) {

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
                            mProgress.dismiss();
                            sStatus = itm.get("success").getAsString();
                            if (sStatus.equalsIgnoreCase("true")) {
                                SharedPreferences.Editor editor = CheckInDetails.edit();
                                try {
                                    if (mMode.equalsIgnoreCase("CIN")) {
                                        editor.putString("Shift_Selected_Id", CheckInInf.getString("Shift_Selected_Id"));
                                        editor.putString("Shift_Name", CheckInInf.getString("Shift_Name"));
                                        editor.putString("ShiftStart", CheckInInf.getString("ShiftStart"));
                                        editor.putString("ShiftEnd", CheckInInf.getString("ShiftEnd"));
                                        editor.putString("ShiftCutOff", CheckInInf.getString("ShiftCutOff"));

                                        long AlrmTime = DT.getDate(CheckInInf.getString("ShiftEnd")).getTime();
//                                        sendAlarmNotify(1001, AlrmTime, HAPApp.Title, "Check-Out Alert !.");
                                    }

                                    if (mMode.equalsIgnoreCase("ONDuty")) {
                                        mShared_common_pref.save(Shared_Common_Pref.DAMode, true);

                                        mLUService = new SANGPSTracker(ImageCapture.this);
                                        myReceiver = new LocationReceiver();
                                        bindService(new Intent(ImageCapture.this, SANGPSTracker.class), mServiceConection,
                                                Context.BIND_AUTO_CREATE);
                                        LocalBroadcastManager.getInstance(ImageCapture.this).registerReceiver(myReceiver,
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            String mMessage = "Your Check-In Submitted Successfully";
                            try {
                                mMessage = itm.get("Msg").getAsString();
                            } catch (Exception e) {
                            }

                            AlertDialogBox.showDialog(ImageCapture.this, HAPApp.Title, String.valueOf(Html.fromHtml(mMessage)), "Yes", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    if (sStatus.equalsIgnoreCase("true")) {
                                        Intent Dashboard = new Intent(ImageCapture.this, Dashboard_Two.class);

                                        Dashboard.putExtra("Mode", "CIN");
                                        ImageCapture.this.startActivity(Dashboard);
                                    }
                                    ((AppCompatActivity) ImageCapture.this).finish();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        mProgress.dismiss();
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
                        mProgress.dismiss();
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

                            AlertDialog alertDialog = new AlertDialog.Builder(ImageCapture.this)
                                    .setTitle(HAPApp.Title)
                                    .setMessage(Html.fromHtml(mMessage))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent Dashboard = new Intent(ImageCapture.this, Dashboard_Two.class);
                                            Dashboard.putExtra("Mode", "extended");
                                            ImageCapture.this.startActivity(Dashboard);
                                            ((AppCompatActivity) ImageCapture.this).finish();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        mProgress.dismiss();
                        Log.d("HAP_receive", "");
                    }
                });
            }
            else {
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
                        mProgress.dismiss();
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

                            Intent playIntent = new Intent(ImageCapture.this, SANGPSTracker.class);
                            stopService(playIntent);

                            JsonObject itm = response.body().getAsJsonObject();
                            String mMessage = "Your Check-Out Submitted Successfully<br><br>Check in Time  : " + CheckInDetails.getString("FTime", "") + "<br>" +
                                    "Check Out Time : " + CTime;

                            try {
                                mMessage = itm.get("Msg").getAsString();
                            } catch (Exception e) {
                            }

                            AlertDialogBox.showDialog(ImageCapture.this, HAPApp.Title, String.valueOf(Html.fromHtml(mMessage)), "Ok", "", false, new AlertBox() {
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
                                            common_class.clearLocData(ImageCapture.this);

                                            mShared_common_pref.clear_pref(Constants.DB_TWO_GET_MREPORTS);
                                            mShared_common_pref.clear_pref(Constants.DB_TWO_GET_DYREPORTS);
                                            mShared_common_pref.clear_pref(Constants.DB_TWO_GET_NOTIFY);
                                            mShared_common_pref.clear_pref(Constants.LOGIN_DATA);

                                            finishAffinity();
                                            if (response.body().size() > 0) {
                                                Intent takePhoto = new Intent(ImageCapture.this, AllowanceActivityTwo.class);
                                                takePhoto.putExtra("Mode", "COUT");
                                                startActivity(takePhoto);
                                            } else {

                                                Intent Dashboard = new Intent(ImageCapture.this, Login.class);
                                                startActivity(Dashboard);

                                                ((AppCompatActivity) ImageCapture.this).finish();
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
                        mProgress.dismiss();
                        Log.d("HAP_receive", "");
                    }
                });
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

    private void setDefaultCameraId(String cam) {
        noOfCameras = Camera.getNumberOfCameras();
        int facing = cam.equalsIgnoreCase("front") ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        Log.v("CAMERA_FOCUS", String.valueOf(facing));
        mCamId = facing;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < noOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                //mCamId = i;
            }
        }
    }

    private boolean setExposureCompensation(int exposureCompensation) {

        Camera camera = fragment.getCamera();
        Camera.Parameters params = camera.getParameters();

        int minExposureCompensation = camera.getParameters().getMinExposureCompensation();
        int maxExposureCompensation = camera.getParameters().getMaxExposureCompensation();

        if (minExposureCompensation == 0 && maxExposureCompensation == 0) {
            Log.d("Cam Error", "Can't set Exposure");
        } else {
            if (exposureCompensation < minExposureCompensation) {
                exposureCompensation = minExposureCompensation;
            } else if (exposureCompensation > maxExposureCompensation) {
                exposureCompensation = maxExposureCompensation;
            }
            params.setExposureCompensation(exposureCompensation);
            camera.setParameters(params);
        }

        return true;
    }

    private String[] getSupportedWhiteBalanceModes() {
        Camera camera = fragment.getCamera();
        Camera.Parameters params = camera.getParameters();

        List<String> supportedWhiteBalanceModes;
        supportedWhiteBalanceModes = params.getSupportedWhiteBalance();

        JSONArray jsonWhiteBalanceModes = new JSONArray();
        String[] lstModes = new String[supportedWhiteBalanceModes.size()];
        if (camera.getParameters().isAutoWhiteBalanceLockSupported()) {
            jsonWhiteBalanceModes.put(new String("lock"));
        }
        if (supportedWhiteBalanceModes != null) {
            for (int i = 0; i < supportedWhiteBalanceModes.size(); i++) {
                jsonWhiteBalanceModes.put(new String(supportedWhiteBalanceModes.get(i)));
                lstModes[i] = supportedWhiteBalanceModes.get(i);
            }
        }

        // callbackContext.success(jsonWhiteBalanceModes);
        return lstModes;
    }

    private boolean getWhiteBalanceMode() {
        Camera camera = fragment.getCamera();
        Camera.Parameters params = camera.getParameters();

        String whiteBalanceMode;

        if (camera.getParameters().isAutoWhiteBalanceLockSupported()) {
            if (camera.getParameters().getAutoWhiteBalanceLock()) {
                whiteBalanceMode = "lock";
            } else {
                whiteBalanceMode = camera.getParameters().getWhiteBalance();
            }
            ;
        } else {
            whiteBalanceMode = camera.getParameters().getWhiteBalance();
        }
        if (whiteBalanceMode != null) {
            //callbackContext.success(whiteBalanceMode);
        } else {
            Log.e("Cam Error", "White balance mode not supported");
        }

        return true;
    }

    private boolean setWhiteBalanceMode(String whiteBalanceMode) {
        Camera.Parameters params = fragment.getCamera().getParameters();

        if (whiteBalanceMode.equals("lock")) {
            if (fragment.getCamera().getParameters().isAutoWhiteBalanceLockSupported()) {
                params.setAutoWhiteBalanceLock(true);
                fragment.setCameraParameters(params);
            } else {
                Log.e("Cam Error", "White balance lock not supported");
            }
        } else if (whiteBalanceMode.equals("auto") ||
                whiteBalanceMode.equals("incandescent") ||
                whiteBalanceMode.equals("cloudy-daylight") ||
                whiteBalanceMode.equals("daylight") ||
                whiteBalanceMode.equals("fluorescent") ||
                whiteBalanceMode.equals("shade") ||
                whiteBalanceMode.equals("twilight") ||
                whiteBalanceMode.equals("warm-fluorescent")) {
            params.setWhiteBalance(whiteBalanceMode);
            fragment.setCameraParameters(params);
        } else {
            Log.e("Cam Error", "White balance parameter not supported");
        }

        return true;
    }

    public void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCamId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(-rotation);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
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
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
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
}