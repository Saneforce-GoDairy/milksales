package com.saneforce.milksales.Activity_Hap;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
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
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.Common_Class.CameraPermission;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.LocationEvents;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.Camera.CameraActivity;
import com.saneforce.milksales.common.LocationFinder;
import com.saneforce.milksales.common.LocationReceiver;
import com.saneforce.milksales.common.SANGPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllowancCapture extends AppCompatActivity implements CameraActivity.CameraPreviewListener {
    private static final String TAG = "AllowanceImageCapture";
    private CameraActivity fragment;

    Button button;
    TextureView textureView;
    ImageView btnFlash,btnWBal;
    ImageView btnSwchCam;
    ListView lstFlashMode,lstWBalance;
    LinearLayout lstModalFlash,lstModalWBal;
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
    String sStatus,mMode, WrkType, onDutyPlcID, onDutyPlcNm, vstPurpose, UserInfo = "MyPrefs", imagvalue = "", mypreference = "mypref", PlaceId = "", PlaceName = "";
    com.saneforce.milksales.Common_Class.Common_Class common_class;

    public static final String sCheckInDetail = "CheckInDetail";
    public static final String sUserDetail = "MyPrefs";

    Button btnRtPrv, btnOkPrv;

    private SANGPSTracker mLUService;
    private boolean mBound = false;
    private LocationReceiver myReceiver;
    Location mlocation;
    String UKey="";

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
        common_class = new com.saneforce.milksales.Common_Class.Common_Class(this);

        new LocationFinder(getApplication(), new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                mlocation = location;
            }
        });
        UKey=UserDetails.getString("Sfcode", "")+"-"+(new Date().getTime());
        Bundle params = getIntent().getExtras();
        try {
            mMode = params.getString("Mode");

        } catch (Exception e) {
            e.printStackTrace();
        }

        CameraPermission cameraPermission = new CameraPermission(AllowancCapture.this, getApplicationContext());
        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        }
        else {
            Log.v("PERMISSION", "PERMISSION");
//            StartSelfiCamera();
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics ();
            display.getMetrics(outMetrics);

            picHeight = outMetrics.heightPixels;
            picWidth  = outMetrics.widthPixels;
            float density  = getResources().getDisplayMetrics().density;
            dpHeight = (int)(outMetrics.heightPixels / density);
            dpWidth  = (int) (outMetrics.widthPixels / density)+1;
            startCamera(0,0,dpWidth,dpHeight,"back",false,false,false,"1",false,false,true);

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
                fragment.takePicture(dpWidth, dpHeight, 100);//takePicture();
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
                WBModes=getSupportedWhiteBalanceModes();

                ArrayAdapter simpleWBAdapter = new ArrayAdapter<String>(AllowancCapture.this, android.R.layout.simple_list_item_1, WBModes);
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
                int bright=(int)((Float.parseFloat(String.valueOf(progress)) / 100) * 40) - 20;
                Log.d("Brightness",String.valueOf(bright));
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
            Log.d(TAG,"Camera already started");
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

    public static void setOnImagePickListener(OnImagePickListener mImagePickListener) {
        imagePickListener = mImagePickListener;
    }
    @Override
    public void onPictureTaken(String originalPicture, String picData) {
        //File imgFile=new File(originalPicture);
        FrameLayout preview= findViewById(R.id.preview);
        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        file = new File(originalPicture);
        imagePath=originalPicture;
        imageFileName=originalPicture.substring(originalPicture.lastIndexOf("/")+1);
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
        Log.d(TAG,"Back button Pressed...");
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

    private void CloseImgPreview() {
        vwPreview = findViewById(R.id.ImgPreview);
        vwPreview.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }
    private void saveImgPreview() {

        RelativeLayout vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        vwPreview.setVisibility(View.GONE);
        imgPreview.setImageURI(Uri.fromFile(file));
        button.setVisibility(View.GONE);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));
        vwPreview.setBackground(drawableBitmap);

        String PathFile = String.valueOf(file);
        Bitmap bitmap = BitmapFactory.decodeFile(PathFile);
        imgPreview.setImageBitmap(bitmap);

        imagePickListener.OnImageURIPick(bitmap,  imageFileName,PathFile.replace("file://",""));
        finish();

        /*if (mMode.equals("One")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImage", Uri.fromFile(file).toString());
            editor.commit();
            // startActivity(new Intent(AllowancCapture.this, AllowanceActivity.class));
            finish();

        } else if (mMode.equals("three")) {

            String filePath = String.valueOf(file);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imgPreview.setImageBitmap(bitmap);

            imagePickListener.OnImageURIPick(bitmap,  imageFileName,filePath.replace("file://",""));
            finish();


        } else if (mMode.equals("Two")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImages", Uri.fromFile(file).toString());
            editor.commit();
            finish();


        } else if (mMode.equalsIgnoreCase("Missed")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SharedImages", Uri.fromFile(file).toString());
            editor.commit();
            finish();
        }else{

            String filePath = String.valueOf(file);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imgPreview.setImageBitmap(bitmap);

            imagePickListener.OnImagePick(bitmap,  imageFileName);
            finish();
        }*/

/*

        vwPreview = findViewById(R.id.ImgPreview);
        ImageView imgPreview = findViewById(R.id.imgPreviewImg);
        BitmapDrawable drawableBitmap = new BitmapDrawable(String.valueOf(Uri.fromFile(file)));

        vwPreview.setBackground(drawableBitmap);
        String filePath = String.valueOf(file);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        imgPreview.setImageBitmap(bitmap);

        Intent mIntent = new Intent(this, FileUploadService.class);
        mIntent.putExtra("mFilePath", String.valueOf(file));
        mIntent.putExtra("SF", UserDetails.getString("Sfcode",""));
        mIntent.putExtra("FileName", imageFileName);
        mIntent.putExtra("Mode", (mMode.equalsIgnoreCase("PF") ? "PROF" : "ATTN"));
        FileUploadService.enqueueWork(this, mIntent);
        Log.e("Image_Capture", Uri.fromFile(file).toString());
        Log.e("Image_Capture", "IAMGE     " + bitmap);
        if (mMode.equalsIgnoreCase("PF")) {
            imagePickListener.OnImagePick(bitmap, imageFileName);
            finish();
        } else {
            mProgress = new ProgressDialog(this);
            String titleId = "Submiting";
            mProgress.setTitle(titleId);
            mProgress.setMessage("Preparing Please Wait...");
            mProgress.show();
            if(mlocation!=null){
                mProgress.setMessage("Submiting Please Wait...");
                vwPreview.setVisibility(View.GONE);
                // imgPreview.setImageURI(Uri.fromFile(file));
                button.setVisibility(View.GONE);
                saveCheckIn();
            }else {
                new LocationFinder(getApplication(), new LocationEvents() {
                    @Override
                    public void OnLocationRecived(Location location) {
                        mlocation=location;
                        mProgress.setMessage("Submiting Please Wait...");
                        vwPreview.setVisibility(View.GONE);
                        // imgPreview.setImageURI(Uri.fromFile(file));
                        button.setVisibility(View.GONE);
                        saveCheckIn();
                    }
                });
            }
        }*/
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
        CallApiImage(UserDetails.getString("Sfcode",""), imgg);
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

            if(mMode.equalsIgnoreCase("onduty")) {
                CheckInInf.put("PlcNm", PlaceName);
                CheckInInf.put("PlcID", PlaceId);
            }else{
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


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private boolean setExposureCompensation(int exposureCompensation) {


        Camera camera = fragment.getCamera();
        Camera.Parameters params = camera.getParameters();

        int minExposureCompensation = camera.getParameters().getMinExposureCompensation();
        int maxExposureCompensation = camera.getParameters().getMaxExposureCompensation();

        if ( minExposureCompensation == 0 && maxExposureCompensation == 0) {
            Log.d("Cam Error","Can't set Exposure");
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
        Camera camera=fragment.getCamera();
        Camera.Parameters params = camera.getParameters();

        List<String> supportedWhiteBalanceModes;
        supportedWhiteBalanceModes = params.getSupportedWhiteBalance();

        JSONArray jsonWhiteBalanceModes = new JSONArray();
        String[] lstModes = new String[supportedWhiteBalanceModes.size()];
        if (camera.getParameters().isAutoWhiteBalanceLockSupported()) {
            jsonWhiteBalanceModes.put(new String("lock"));
        }
        if (supportedWhiteBalanceModes != null) {
            for (int i=0; i<supportedWhiteBalanceModes.size(); i++) {
                jsonWhiteBalanceModes.put(new String(supportedWhiteBalanceModes.get(i)));
                lstModes[i]=supportedWhiteBalanceModes.get(i);
            }
        }

        // callbackContext.success(jsonWhiteBalanceModes);
        return lstModes;
    }
    private boolean setWhiteBalanceMode(String whiteBalanceMode) {
        Camera.Parameters params = fragment.getCamera().getParameters();

        if (whiteBalanceMode.equals("lock")) {
            if (fragment.getCamera().getParameters().isAutoWhiteBalanceLockSupported()) {
                params.setAutoWhiteBalanceLock(true);
                fragment.setCameraParameters(params);
            } else {
                Log.e("Cam Error","White balance lock not supported");
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
            Log.e("Cam Error","White balance parameter not supported");
        }

        return true;
    }
}