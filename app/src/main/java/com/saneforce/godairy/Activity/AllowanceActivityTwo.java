package com.saneforce.godairy.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity_Hap.CustomListViewDialog;
import com.saneforce.godairy.Activity_Hap.Dashboard;
import com.saneforce.godairy.Activity_Hap.Dashboard_Two;
import com.saneforce.godairy.Activity_Hap.ERT;
import com.saneforce.godairy.Activity_Hap.Help_Activity;
import com.saneforce.godairy.Activity_Hap.Login;
import com.saneforce.godairy.Activity_Hap.Mydayplan_Activity;
import com.saneforce.godairy.Activity_Hap.PayslipFtp;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Common_Class.Util;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllowanceActivityTwo extends AppCompatActivity implements Master_Interface {
    static TransferUtility transferUtility;
    // Reference to the utility class
    static Util util;
    final Handler handler = new Handler();
    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {

                    if (!(ClosingCon.equals("") || ClosingCon.equalsIgnoreCase("null"))) {
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    } else {
                        Intent Dashboard = new Intent(AllowanceActivityTwo.this, Dashboard_Two.class);
                        Dashboard.putExtra("Mode", "CIN");
                        startActivity(Dashboard);
                    }
                    //startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
            });
    TextView TextModeTravel, /*TextStartedKm,*/ /*TextMaxKm,*/ TextToPlace, TextCloseDate, TextDtTrv/*tottrv_km,totclm_km*/;
    CircularProgressButton submitAllowance;
    Integer stKM = 0, endKm = 0, personalKM = 0, StratKm = 0, maxKM = 0, TotalKm = 0, totalPM = 0, StartedKM = 0;
    SharedPreferences CheckInDetails, sharedpreferences, UserDetails;
    Shared_Common_Pref shared_common_pref;
    ApiInterface apiInterface;
    String Photo_Name = "", imageConvert = "", StartedKm = "", EndedImage = "", CheckInfo = "CheckInDetail",
            UserInfo = "MyPrefs", MOT = "ModeOfTravel", Name = "Allowance", mypreference = "mypref", StrToCode = "",
            toPlace = "", ImageStart = "", Hq = "", ClosingCon = "", ClosingDate = "";
    LinearLayout linToPlace, /*takeEndedPhoto,*/ vwlblHead;
    CustomListViewDialog customDialog;
    Common_Model mCommon_model_spinner;
    Common_Class common_class;
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Location mlocation;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance_two);

        util = new Util();
        transferUtility = util.getTransferUtility(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        TextModeTravel = findViewById(R.id.txt_mode_travel);
        vwlblHead = findViewById(R.id.vwlblHead);
        TextDtTrv = findViewById(R.id.DtTrv);
        TextCloseDate = findViewById(R.id.closing_date);
        //TextStartedKm = findViewById(R.id.txt_started_km);
        //TextMaxKm = findViewById(R.id.txt_max);
        //StartedKmImage = findViewById(R.id.img_started_km);
        //EndedEditText = findViewById(R.id.ended_km);
        //EndedKmImage = findViewById(R.id.img_ended_km);
        //takeEndedPhoto = findViewById(R.id.btn_take_photo);
        submitAllowance = findViewById(R.id.submit_allowance);
        //PersonalKmEdit = findViewById(R.id.personal_ended_km);
        linToPlace = findViewById(R.id.lin_to);
        TextToPlace = findViewById(R.id.txt_to);
        //ReasonMode = findViewById(R.id.reason_mode);
        //tottrv_km = findViewById(R.id.tottrv_km);
        //totclm_km = findViewById(R.id.totclm_km);
        shared_common_pref = new Shared_Common_Pref(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        common_class = new Common_Class(this);
/*
        closingIntet.putExtra("Cls_con","cls");
        closingIntet.putExtra("Cls_dte","");*/

        new LocationFinder(getApplication(), new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                mlocation = location;
            }
        });

        ClosingCon = String.valueOf(getIntent().getSerializableExtra("Cls_con"));
        ClosingDate = String.valueOf(getIntent().getSerializableExtra("Cls_dte"));


        if (!(ClosingDate.equals("") || ClosingDate.equalsIgnoreCase("null"))) {
            vwlblHead.setVisibility(View.VISIBLE);
            TextCloseDate.setVisibility(View.VISIBLE);
            TextCloseDate.setText(ClosingDate);
            //takeEndedPhoto.setVisibility(View.GONE);
            callApi(ClosingDate);
        } else {
            vwlblHead.setVisibility(View.GONE);
            TextCloseDate.setVisibility(View.GONE);
            TextDtTrv.setVisibility(View.GONE);
            callApi(Common_Class.GetDate());
        }

        getToolbar();
        BusToValue();


        /*EndedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextModeTravel.getText().toString().equalsIgnoreCase("Two Wheeler")) {
                    EndedEditText.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, StartedKM + maxKM), new InputFilter.LengthFilter(String.valueOf(StartedKM + maxKM).length())});
                } else if (TextModeTravel.getText().toString().equalsIgnoreCase("Four Wheeler")) {
                    EndedEditText.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, StartedKM + maxKM), new InputFilter.LengthFilter(String.valueOf(StartedKM + maxKM).length())});
                }

                if (EndedEditText.getText().toString() != null && !EndedEditText.getText().toString().isEmpty() && !EndedEditText.getText().toString().equals("null")) {
                    endKm = Integer.parseInt(EndedEditText.getText().toString());
                    totalPM = Integer.valueOf((EndedEditText.getText().toString())) - Integer.valueOf((TextStartedKm.getText().toString()));
                    if(totalPM<0) totalPM=0;
                    PersonalKmEdit.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, totalPM)});
                    String sPerKM=PersonalKmEdit.getText().toString();
                    if(sPerKM.equals("")) sPerKM="0";
                    int perPM = Integer.valueOf(sPerKM);
                    if(perPM<0) perPM=0;
                    //tottrv_km.setText(String.valueOf(totalPM));
                    int clmKM=totalPM-perPM;
                    if(clmKM<0) clmKM=0;
                    //totclm_km.setText(String.valueOf(clmKM));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        PersonalKmEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EndedEditText.getText().toString() != null && !EndedEditText.getText().toString().isEmpty() && !EndedEditText.getText().toString().equals("null")) {
                    endKm = Integer.parseInt(EndedEditText.getText().toString());
                    totalPM = Integer.valueOf((EndedEditText.getText().toString())) - Integer.valueOf((TextStartedKm.getText().toString()));
                    if(totalPM<0) totalPM=0;
                    String sPerKM=PersonalKmEdit.getText().toString();

                    if(sPerKM.equals("")) sPerKM="0";
                    int perPM = Integer.valueOf(sPerKM);
                    if(perPM<0) perPM=0;
                    //tottrv_km.setText(String.valueOf(totalPM));
                    int clmKM=totalPM-perPM;
                    if(clmKM<0) clmKM=0;
                    //totclm_km.setText(String.valueOf(clmKM));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        }*/


        /*takeEndedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPermission cameraPermission = new CameraPermission(AllowanceActivityTwo.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                } else {

                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
//                            Photo_Name = FileName;
//                            imageConvert=fullPath;
//                            EndedImage="file://"+fullPath;
//                            EndedKmImage.setImageBitmap(image);

                            UploadPhoto(fullPath, UserDetails.getString("Sfcode", ""), FileName, "Travel", image);

                        }
                    });
                    Intent intent = new Intent(AllowanceActivityTwo.this, AllowancCapture.class);
                    intent.putExtra("allowance", "Two");
                    startActivity(intent);

                }

            }
        });*/


        submitAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAllowance.startAnimation();
                common_class.ProgressdialogShow(1, "Validating Please wait...");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mlocation != null) {
                            common_class.ProgressdialogShow(1, "Submitting Please wait...");
                            if (!(ClosingDate.equals("") || ClosingDate.equalsIgnoreCase("null"))) {
                                submitData(ClosingDate);
                            } else {
                                submitData(Common_Class.GetDate());
                            }
                        } else {
                            common_class.ProgressdialogShow(1, "Getting location please wait...");
                            new LocationFinder(getApplication(), new LocationEvents() {
                                @Override
                                public void OnLocationRecived(Location location) {
                                    mlocation = location;
//                                if (!ClosingDate.equals("")) {
                                    common_class.ProgressdialogShow(1, "Submitting Please wait...");
                                    if (!(ClosingDate.equals("") || ClosingDate.equalsIgnoreCase("null"))) {
                                        submitData(ClosingDate);
                                    } else {
                                        submitData(Common_Class.GetDate());
                                    }
                                }
                            });
                        }
                        /*if (imageConvert.matches("")) { //if (EndedImage.matches("") && ((ClosingDate.equals("") || ClosingDate.equalsIgnoreCase("null"))) )
                            Toast.makeText(AllowanceActivityTwo.this, "Choose End photo", Toast.LENGTH_SHORT).show();
                            ResetSubmitBtn(0);
                            return;
                        } else {
                            *//*try {
                                stKM = Integer.valueOf(TextStartedKm.getText().toString());
                            } catch (NumberFormatException ex) { // handle your exception
                            }
                            endKm = Integer.valueOf(EndedEditText.getText().toString());
                            String pkm=PersonalKmEdit.getText().toString();
                            if(pkm.equals("")) pkm="0";
                            personalKM = Integer.valueOf(pkm);*//*

                            *//*if (stKM < endKm) {
                                if (((endKm-StartedKM)-personalKM)>maxKM) {
                                    Toast.makeText(AllowanceActivityTwo.this, "KM Limit is Exceeded", Toast.LENGTH_SHORT).show();
                                    ResetSubmitBtn(0);
                                    return;
                                }

                            } else {
                                ResetSubmitBtn(0);
                                Toast.makeText(AllowanceActivityTwo.this, "Should be greater then Started Km", Toast.LENGTH_SHORT).show();

                            }*//*
                        }*/
                    }
                }, 100);
            }
        });

        linToPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(AllowanceActivityTwo.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

    }

    private void UploadPhoto(String path, String SF, String FileName, String Mode, Bitmap image) {
        try {
            common_class.ProgressdialogShow(1, "");

            MultipartBody.Part imgg;
            if (path != null && (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg"))) {
                imgg = convertimg("file", path);

            } else {
                common_class.ProgressdialogShow(0, "");
                common_class.showMsg(this, "Image file only supported");
                return;
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> mCall = apiInterface.onTAFileUpload(SF, FileName, Mode, imgg);

            Log.e("SEND_IMAGE_SERVER", mCall.request().toString());

            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {


                            JSONObject js = new JSONObject(response.body().string());
                            Log.v("Res", js.toString());

                            if (js.getBoolean("success")) {

                                if (image != null) {
                                    Photo_Name = FileName;
                                    imageConvert = path;
                                    EndedImage = "file://" + path;
                                    //EndedKmImage.setImageBitmap(image);


                                }


                                common_class.ProgressdialogShow(0, "");

                                common_class.showMsg(AllowanceActivityTwo.this, "File uploading successful ");
                            } else {
                                common_class.ProgressdialogShow(0, "");
                                common_class.showMsg(AllowanceActivityTwo.this, "Failed.Try Again...");
                            }
                        } else {

                            common_class.ProgressdialogShow(0, "");
                            common_class.showMsg(AllowanceActivityTwo.this, "Failed.Try Again...");

                        }

                    } catch (Exception e) {
                        common_class.ProgressdialogShow(0, "");
                        common_class.showMsg(AllowanceActivityTwo.this, "Failed.Try Again...");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    common_class.ProgressdialogShow(0, "");
                    common_class.showMsg(AllowanceActivityTwo.this, "Failed.Try Again...");

                    Log.e("SEND_IMAGE_Response", "ERROR");
                }
            });


        } catch (Exception e) {
            Log.e("TAClaim:", e.getMessage());
        }
    }

    /*Submit*/
    public void submitData(String date) {

        //Log.e("PERSONAL_KM", PersonalKmEdit.getText().toString());

        /*if (PersonalKmEdit.getText().toString().equals("")) {

        } else {
            personalKM = Integer.valueOf(PersonalKmEdit.getText().toString());
        }*/

        try {

//            Intent mIntent = new Intent(this, FileUploadService.class);
//            mIntent.putExtra("mFilePath", imageConvert);
//            mIntent.putExtra("SF", UserDetails.getString("Sfcode",""));
//            mIntent.putExtra("FileName", Photo_Name);
//            mIntent.putExtra("Mode", "Travel");
//            FileUploadService.enqueueWork(this, mIntent);

            JSONObject jj = new JSONObject();
            jj.put("km", 0);
            jj.put("rmk", "");
            jj.put("pkm", 0);
            jj.put("mod", "11");
            jj.put("sf", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("div", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("url", Photo_Name);
            jj.put("from", "");
            jj.put("to", "");
            jj.put("to_code", StrToCode);
            jj.put("fare", "");
            jj.put("Activity_Date", date);
            jj.put("location", mlocation.getLatitude() + ":" + mlocation.getLongitude());
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;

            // if (!ClosingDate.equals("")  ) {

            if (!(ClosingDate.equals("") || ClosingDate.equalsIgnoreCase("null"))) {
                Callto = apiInterface.updateAllowance("update/predayallowance", jj.toString());
            } else {
                Callto = apiInterface.updateAllowance("update/allowance", jj.toString());
            }


            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            JSONObject js = new JSONObject(jsonData);
                            if (js.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(AllowanceActivityTwo.this, " Submitted successfully ", Toast.LENGTH_SHORT).show();
                                ResetSubmitBtn(1);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.remove(Name);
                                editor.remove(MOT);
                                editor.remove("SharedImage");
                                editor.remove("Sharedallowance");
                                editor.remove("SharedMode");
                                editor.remove("StartedKM");
                                editor.remove("SharedFromKm");
                                editor.remove("SharedToKm");
                                editor.remove("SharedFare");
                                editor.remove("SharedImages");
                                editor.remove("Closing");
                                shared_common_pref.clear_pref(Shared_Common_Pref.DAMode);

                                //if (!ClosingCon.equals("")) {
                                if (!(ClosingDate.equals("") || ClosingDate.equalsIgnoreCase("null")) && Common_Class.GetDate() != ClosingDate) {
                                    startActivity(new Intent(getApplicationContext(), Mydayplan_Activity.class));
                                } else {
                                    Intent takePhoto = new Intent(AllowanceActivityTwo.this, Login.class);
                                    startActivity(takePhoto);
                                }


                            } else {
                                ResetSubmitBtn(2);
                                Toast.makeText(AllowanceActivityTwo.this, "Cannot submitted the data. Try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ResetSubmitBtn(2);
                            Toast.makeText(AllowanceActivityTwo.this, "Cannot submitted the data. Try again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        ResetSubmitBtn(2);
                        Toast.makeText(AllowanceActivityTwo.this, "Cannot submitted the data. Try again", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ResetSubmitBtn(2);
                    Toast.makeText(AllowanceActivityTwo.this, "Cannot submitted the data. Try again", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            ResetSubmitBtn(0);
            Toast.makeText(AllowanceActivityTwo.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ResetSubmitBtn(int resetMode) {
        common_class.ProgressdialogShow(0, "");
        long dely = 100;
        //if(resetMode!=0) dely=1000;
        if (resetMode == 1) {
            submitAllowance.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            submitAllowance.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                submitAllowance.stopAnimation();
                submitAllowance.revertAnimation();
            }
        }, dely);

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
    }

    @Override
    public void onBackPressed() {

    }

    public void openHome() {

        if (!(ClosingCon.equals("") || ClosingCon.equalsIgnoreCase("null"))) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        } else {
            Intent Dashboard = new Intent(AllowanceActivityTwo.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        }
    }


    public void callApi(String date) {

        try {
            JSONObject jj = new JSONObject();
            jj.put("div", Shared_Common_Pref.Div_Code);
            jj.put("sf", Shared_Common_Pref.Sf_Code);
            jj.put("rSF", Shared_Common_Pref.Sf_Code);
            jj.put("State_Code", Shared_Common_Pref.StateCode);
            jj.put("Activity_Date", date);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getStartKmDetails(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("StartDetails");
                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject json_oo = jsnArValue.getJSONObject(i);
                                TextModeTravel.setText(json_oo.getString("MOT_Name"));
                                //TextStartedKm.setText(json_oo.getString("Start_Km"));
                                /*Glide.with(getApplicationContext())
                                        .load(json_oo.getString("start_Photo"))
                                        .into(StartedKmImage);*/


                                maxKM = json_oo.getInt("Maxkm");
                                Hq = json_oo.getString("dailyAllowance");

                                //TextMaxKm.setText("Maximum km : " + maxKM);
                                //TextMaxKm.setVisibility(View.GONE);
                                StratKm = Integer.valueOf(json_oo.getString("Start_Km"));

                                StartedKM = Integer.valueOf(json_oo.getString("Start_Km"));
                                ImageStart = json_oo.getString("start_Photo");
                                String[] FilNMs = ImageStart.split("/");
                                String[] imgDet = FilNMs[FilNMs.length - 1].split("[.]");
                                //DownloadPhoto(StartedKmImage, imgDet[0], imgDet[1]);

                                StrToCode = json_oo.getString("To_Place_Id");
                                TextToPlace.setText(json_oo.getString("To_Place"));
                                TotalKm = StratKm + maxKM;

                                /* EndedEditText.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(1, TotalKm)});*/

                                /*if (!json_oo.getString("start_Photo").matches("")) {
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
                                }*/
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
                        Log.v("request_data_upload", jsonData);
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

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 10) {
            TextToPlace.setText(myDataset.get(position).getName());
            toPlace = myDataset.get(position).getName();
            StrToCode = myDataset.get(position).getId();
            Log.e("STRTOCOD", StrToCode);
        }
    }


    private void DownloadPhoto(ImageView ImgViewer, String FileName, String FileExt) {
        try {

            final File file = File.createTempFile(FileName, "." + FileExt);

            TransferObserver downloadObserver =
                    transferUtility.download("happic", "TAPhotos/" + FileName + "." + FileExt, file);

            downloadObserver.setTransferListener(new TransferListener() {

                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                        ImgViewer.setImageBitmap(bmp);
                        Toast.makeText(getApplicationContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;

                    //tvFileName.setText("ID:" + id + "|bytesCurrent: " + bytesCurrent + "|bytesTotal: " + bytesTotal + "|" + percentDone + "%");
                }

                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                }

            });
        } catch (Exception e) {
            // Log.e(TAG,e.getMessage());
        }
    }
}