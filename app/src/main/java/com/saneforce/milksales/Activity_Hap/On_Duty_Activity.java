package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.CameraPermission;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.Model_Class.ModeOfTravel;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.DatabaseHandler;
import com.saneforce.milksales.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class On_Duty_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface {

    private static String Tag = "HAP_Check-In";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;

    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";

    CardView ModeTravel, BusCardTo, cardHapLoaction, CardOthPlc;
    Button haplocationbutton, otherlocationbutton, submitbutton, closebutton, exitclose, btn_submit;
    EditText purposeofvisitedittext, ondutyedittext, StartKm, onDutyFrom, EditRemarks, txtOthPlc;
    LinearLayout ModeOfTravel, haplocationtext, purposeofvisittext, ondutylocations, linearBus, lincheck,
            BikeMode, BusMode, ReasonPhoto, ProofImage, vwHlyDyEntry;
    TextView TextMode, TextToAddress, dailyAllowance, selecthaplocationss;
    CheckBox chkHlyDyFlg;
    ImageView capture_img, attachedImage;
    Button SubmitValue;
    Common_Model mCommon_model_spinner;

    String imageURI = "", modeVal = "", StartedKM = "", FromKm = "", ToKm = "", Fare = "";
    Boolean HAPLocMode = false;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();

    CustomListViewDialog customDialog;
    Common_Model Model_Pojo;
    Common_Class common_class;

    int flag;
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    List<com.saneforce.milksales.Model_Class.ModeOfTravel> modelOfTravel;

    String mode, hapLocid;
    Gson gson;

    /*AllowanceActivity*/
    ApiInterface apiInterface;
    DatabaseHandler db;

    String SF_code = "", div = "", SFType = "";
    Shared_Common_Pref mShared_common_pref;


    String checking = "";
    String count = "";

    Shared_Common_Pref shared_common_pref;

    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    public static final String MOC = "ModeOfCount";

    Type userType;
    List<Common_Model> modelTravelType = new ArrayList<>();
    String startEnd = "", ModeTravelType = "";
    String modeId = "";

    CheckBox driverAllowance;
    LinearLayout linCheckdriver;
    String strHapLocation = "", strVisitPurpose = "";
    String imageConvert = "", imageServer = "";
    String DriverNeed = "false", DriverMode = "", strDailyAllowance = "", StrID = "";

    CardView CardDailyAllowance;
    private ArrayList<String> travelTypeList;
    String driverAllowanceBoolean = "", StrToCode = "", STRCode = "";

    CheckBox cbReturnHQ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on__duty_);

        shared_common_pref = new Shared_Common_Pref(this);

        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        common_class = new Common_Class(this);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        SFType = UserDetails.getString("State_Code", "");

        db = new DatabaseHandler(this);
        try {
            JSONArray HAPLoca = db.getMasterData("HAPLocations");
            for (int li = 0; li < HAPLoca.length(); li++) {
                JSONObject jItem = HAPLoca.getJSONObject(li);
                Common_Model item = new Common_Model(jItem.getString("id"), jItem.getString("name"), jItem);
                modelRetailDetails.add(item);
                String flag = jItem.optString("ODFlag");
                if (flag.equals("1")) {
                    getfieldforcehqlist.add(item);
                }
            }
            Common_Model itemOth = new Common_Model("-1", "Other Location", "");
            modelRetailDetails.add(itemOth);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dynamicMode();
        //   GetfieldforceHq();
        OrderType();

        lincheck = findViewById(R.id.lin_mode);
        driverAllowance = findViewById(R.id.da_driver_allowance);
        linCheckdriver = findViewById(R.id.lin_check_driver);

        chkHlyDyFlg = findViewById(R.id.chkHlyDyFlg);

        dailyAllowance = findViewById(R.id.text_daily_allowance);
        linearBus = findViewById(R.id.lin_bus);

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
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Dashboard.class);
            }
        });

        gson = new Gson();
        haplocationtext = findViewById(R.id.haplocationtext);
        purposeofvisittext = findViewById(R.id.purposeofvisittext);
        otherlocationbutton = findViewById(R.id.otherlocationbutton);
        haplocationbutton = findViewById(R.id.haplocationbutton);
        ondutylocations = findViewById(R.id.ondutylocations);
        submitbutton = findViewById(R.id.submitbutton);
        selecthaplocationss = findViewById(R.id.selecthaplocationss);
        purposeofvisitedittext = findViewById(R.id.purposeofvisitedittext);
        ondutyedittext = findViewById(R.id.ondutyedittext);
        closebutton = findViewById(R.id.closebutton);
        exitclose = findViewById(R.id.exitclose);
        ModeOfTravel = findViewById(R.id.mode_of_travel);
        cardHapLoaction = findViewById(R.id.card_hap_loaction);
        vwHlyDyEntry = findViewById(R.id.vwHlyDyEntry);

        CardOthPlc = findViewById(R.id.CardOthPlc);
        txtOthPlc = findViewById(R.id.txtOthPlc);

        TextMode = findViewById(R.id.txt_mode);
        TextToAddress = findViewById(R.id.on_duty_to);
        onDutyFrom = findViewById(R.id.on_duty_from);
        StartKm = findViewById(R.id.on_duty_start);
        CardDailyAllowance = findViewById(R.id.card_daily_allowance);

        ModeTravel = findViewById(R.id.card_travel_modes);
        BikeMode = findViewById(R.id.bike_modes);
        BusMode = findViewById(R.id.bus_modes);
        ReasonPhoto = findViewById(R.id.reason_photos);

        BusCardTo = findViewById(R.id.card_bus_modes);

        ProofImage = findViewById(R.id.proof_pics);
        attachedImage = findViewById(R.id.capture_imgs);
        EditRemarks = findViewById(R.id.edt_rmk);
        SubmitValue = findViewById(R.id.btn_submit);

        cardHapLoaction.setOnClickListener(this);
        otherlocationbutton.setOnClickListener(this);
        haplocationbutton.setOnClickListener(this);


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HAPLocMode == true && selecthaplocationss.getText().toString().matches("")) {
                    Toast.makeText(On_Duty_Activity.this, "Select the Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (HAPLocMode == false && ondutyedittext.getText().toString().matches("")) {
                    Toast.makeText(On_Duty_Activity.this, "Enter  Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (purposeofvisitedittext.getText().toString().matches("")) {
                    Toast.makeText(On_Duty_Activity.this, "Enter Visit purpose", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextMode.getText().toString().matches("")) {
                    Toast.makeText(On_Duty_Activity.this, "Enter Mode", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dailyAllowance.getText().toString().matches("")) {
                    Toast.makeText(On_Duty_Activity.this, "Enter Daily Allowance", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!cbReturnHQ.isChecked() && onDutyFrom.getText().toString().matches("")) {
                    Toast.makeText(On_Duty_Activity.this, "Enter From", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (startEnd.equals("1") || count.equals("1")) {
                    if (!(dailyAllowance.getText().toString().equalsIgnoreCase("HQ"))) {
                        if (StrToCode.equalsIgnoreCase("")) {
                            Toast.makeText(On_Duty_Activity.this, "Select the To Location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (StrToCode.equalsIgnoreCase("-1") && txtOthPlc.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(On_Duty_Activity.this, "Enter Other To Location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (StartKm.getText().toString().matches("")) {
                        Toast.makeText(On_Duty_Activity.this, "Enter Start Km", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (imageURI.matches("")) {
                        Toast.makeText(On_Duty_Activity.this, "Choose Start Photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                submitData();
            }
        });
        closebutton.setOnClickListener(this);
        exitclose.setOnClickListener(this);

        CardDailyAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(On_Duty_Activity.this, listOrderType, 100);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        driverAllowance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DriverNeed = "true";
                } else {
                    DriverNeed = "false";
                }
            }
        });
        /*Allowance Activity*/
        capture_img = findViewById(R.id.capture_img);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        btn_submit = findViewById(R.id.btn_submit);
        mShared_common_pref = new Shared_Common_Pref(this);
        ModeTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(On_Duty_Activity.this, modelTravelType, 8);
                customDialog.setCanceledOnTouchOutside(false);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        BusCardTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(On_Duty_Activity.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        ProofImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cameraPermission = new CameraPermission(On_Duty_Activity.this, getApplicationContext());
                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraPermission.requestPermission();
                    }
                } else {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                            //getMulipart(fullPath, 0);
                            imageServer = FileName;
                            imageURI = fullPath;
                            attachedImage.setImageBitmap(image);
                        }
                    });
                    Intent intent = new Intent(On_Duty_Activity.this, AllowancCapture.class);
                    intent.putExtra("allowance", "three");
                    startActivity(intent);
                }

            }
        });

        strHapLocation = selecthaplocationss.getText().toString();

        attachedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                intent.putExtra("ImageUrl", imageURI);
                startActivity(intent);
            }
        });
        lincheck.setVisibility(View.VISIBLE);

        cbReturnHQ = findViewById(R.id.cbReturnHQ);
        cbReturnHQ.setVisibility(View.VISIBLE);

        cbReturnHQ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.cvFromPlace).setVisibility(View.GONE);
                    findViewById(R.id.tvToLabel).setVisibility(View.GONE);
                    TextToAddress.setHint("From Place");

                } else {
                    findViewById(R.id.cvFromPlace).setVisibility(View.VISIBLE);
                    findViewById(R.id.tvToLabel).setVisibility(View.VISIBLE);
                    TextToAddress.setHint("To Place");

                }
            }
        });
    }

    public void OrderType() {
        travelTypeList = new ArrayList<>();
        travelTypeList.add("HQ");
        travelTypeList.add("EXQ");
        travelTypeList.add("Out Station");
        for (int i = 0; i < travelTypeList.size(); i++) {
            String id = String.valueOf(travelTypeList.get(i));
            String name = travelTypeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
    }

    public void dynamicMode() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", div);
        QueryString.put("sfCode", SF_code);
        QueryString.put("rSF", SF_code);
        QueryString.put("State_Code", SFType);
        String commonLeaveType = "{\"tableName\":\"getmodeoftravel\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                modelTravelType.clear();
                userType = new TypeToken<ArrayList<ModeOfTravel>>() {
                }.getType();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    Model_Pojo = new Common_Model(id, name, modeId, driverMode);
                    modelTravelType.add(Model_Pojo);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.otherlocationbutton:
                flag = 1;
                HAPLocMode = false;
                vwHlyDyEntry.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                submitbutton.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.VISIBLE);
                purposeofvisitedittext.setText("");
                selecthaplocationss.setText("");
                ondutyedittext.setText("");
                strHapLocation = "";


                break;

            case R.id.haplocationbutton:
                HAPLocMode = true;

                vwHlyDyEntry.setVisibility(View.VISIBLE);
                ondutyedittext.setText("");
                selecthaplocationss.setText("");
                purposeofvisitedittext.setText("");
                haplocationtext.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                submitbutton.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                otherlocationbutton.setVisibility(View.GONE);
                break;

            case R.id.closebutton:
                vwHlyDyEntry.setVisibility(View.GONE);
                haplocationtext.setVisibility(View.GONE);
                purposeofvisittext.setVisibility(View.GONE);
                submitbutton.setVisibility(View.GONE);
                closebutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.GONE);
                break;
            case R.id.exitclose:
                startActivity(new Intent(this, Dashboard.class));
                break;
            case R.id.card_hap_loaction:
                customDialog = new CustomListViewDialog(On_Duty_Activity.this, getfieldforcehqlist, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                Log.e("On_Duty_Mode", "On_Duty_Mode");
                break;
        }
    }


    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {
            TextMode.setText(myDataset.get(position).getName());
            startEnd = myDataset.get(position).getId();
            count = myDataset.get(position).getId();
            modeId = myDataset.get(position).getFlag();
            DriverMode = myDataset.get(position).getCheckouttime();

            if (startEnd.equals("0")) {
                mode = "11";
                BikeMode.setVisibility(View.GONE);
                BusMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.GONE);
                StartKm.setText("");
                onDutyFrom.setText("");
                TextToAddress.setText("");
            } else {
                mode = "12";
                BikeMode.setVisibility(View.VISIBLE);
                BusMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);

                StartKm.setText("");
                onDutyFrom.setText("");
                TextToAddress.setText("");
            }
            attachedImage.setImageResource(0);
            StartKm.setText("");
            if (attachedImage.getDrawable() == null) {
                Log.e("Image_Draw_able", "Null_Image_View");
            } else {
                Log.e("Image_Draw_able", "Not_Null_Image_View");
            }
            Log.e("IMAGE_URI", imageURI);


            if (DriverMode.equals("1")) {
                linCheckdriver.setVisibility(View.VISIBLE);
            } else {
                linCheckdriver.setVisibility(View.GONE);
            }

            DriverNeed = "false";
            driverAllowance.setChecked(false);

        } else if (type == 10) {
            TextToAddress.setText(myDataset.get(position).getName());
            StrToCode = myDataset.get(position).getId();

            CardOthPlc.setVisibility(View.GONE);
            txtOthPlc.setVisibility(View.GONE);
            if (StrToCode.equalsIgnoreCase("-1")) {
                CardOthPlc.setVisibility(View.VISIBLE);
                txtOthPlc.setVisibility(View.VISIBLE);
            }
        } else if (type == 1) {
            selecthaplocationss.setText(myDataset.get(position).getName());
            hapLocid = String.valueOf(myDataset.get(position).getId());

        } else if (type == 100) {
            String TrTyp = myDataset.get(position).getName();
            dailyAllowance.setText(TrTyp);
            if (TrTyp.equals("HQ")) {
                linearBus.setVisibility(View.GONE);
            } else {
                linearBus.setVisibility(View.VISIBLE);
            }
            TextToAddress.setText("");
        }
    }

    public void GetfieldforceHq() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.getFieldForce_HQ(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                GetJsonData(new Gson().toJson(response.body()), "0");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("ODFlag");

                if (flag.equals("1")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    getfieldforcehqlist.add(Model_Pojo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    /*common_class.CommonIntentwithFinish(Dashboard.class);*/
                    On_Duty_Activity.super.onBackPressed();
                }
            });


    @Override
    public void onBackPressed() {

    }

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field(SF_code);
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
                File file;
                file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(file);
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
                            imageServer = js.getString("url");
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

    public void submitData() {
        try {
            JSONObject jj = new JSONObject();
            jj.put("hap_location", selecthaplocationss.getText().toString());
            jj.put("visit_purpose", purposeofvisitedittext.getText().toString());
            jj.put("other_loaction", ondutyedittext.getText().toString());
            jj.put("mode_allowance", "OnDuty");
            jj.put("km", StartKm.getText().toString());
            jj.put("rmk", StartKm.getText().toString());
            jj.put("mode_name", TextMode.getText().toString());
            jj.put("mod", modeId);
            jj.put("sf", SF_code);
            jj.put("sf_type", UserDetails.getString("Sf_Type", ""));
            jj.put("returnHQ", cbReturnHQ.isChecked() ? 1 : 0);

            jj.put("div", div);
            jj.put("StEndNeed", startEnd);
            jj.put("url", imageServer);
            jj.put("from", onDutyFrom.getText().toString());
            flag = (HAPLocMode == true) ? 0 : 1;
            jj.put("ODFlag", String.valueOf(flag));
            if (StrToCode.equalsIgnoreCase("-1")) {
                jj.put("to", txtOthPlc.getText().toString());
            } else {
                jj.put("to", TextToAddress.getText().toString());
            }
            if (flag == 1) {
                jj.put("onDutyPlcNm", ondutyedittext.getText().toString());
                jj.put("onDutyPlcID", "0");
            } else {
                jj.put("onDutyPlcNm", selecthaplocationss.getText().toString());
                jj.put("onDutyPlcID", hapLocid);
            }
            jj.put("to_code", StrToCode);
            jj.put("dailyAllowance", dailyAllowance.getText().toString());
            jj.put("driverAllowance", DriverNeed);
            jj.put("HolidayFlag", (chkHlyDyFlg.isChecked()) ? "1" : "0");
            jj.put("vstPurpose", purposeofvisitedittext.getText().toString());
            if (!imageServer.equalsIgnoreCase("")) {
                Intent mIntent = new Intent(this, FileUploadService.class);
                mIntent.putExtra("mFilePath", imageURI);
                mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
                mIntent.putExtra("FileName", imageServer);
                mIntent.putExtra("Mode", "Travel");
                FileUploadService.enqueueWork(this, mIntent);
            }
            //saveAllowance
            Intent intent = new Intent(getApplicationContext(), Checkin.class);
            Bundle extras = new Bundle();
            extras.putString("Mode", "onduty");
            extras.putString("data", jj.toString());

            flag = (HAPLocMode == true) ? 0 : 1;

            extras.putString("ODFlag", String.valueOf(flag));
            extras.putString("Mode", "onduty");
            if (flag == 1) {
                extras.putString("onDutyPlcNm", ondutyedittext.getText().toString());
                extras.putString("onDutyPlcID", "0");
                extras.putString("onDuty", "abc");
            } else {
                extras.putString("onDutyPlcNm", selecthaplocationss.getText().toString());
                extras.putString("onDutyPlcID", hapLocid);

                extras.putString("onDuty", "cba");
            }
            extras.putString("HolidayFlag", (chkHlyDyFlg.isChecked()) ? "1" : "0");
            extras.putString("vstPurpose", purposeofvisitedittext.getText().toString());
            intent.putExtras(extras);
            startActivity(intent);


        } catch (Exception e) {
        }
    }

    /*public void submitData() {
        try {
            JSONObject jj = new JSONObject();
            jj.put("hap_location", selecthaplocationss.getText().toString());
            jj.put("visit_purpose", purposeofvisitedittext.getText().toString());
            jj.put("other_loaction", ondutyedittext.getText().toString());
            jj.put("mode_allowance", "OnDuty");
            jj.put("km", StartKm.getText().toString());
            jj.put("rmk", StartKm.getText().toString());
            jj.put("mode_name", TextMode.getText().toString());
            jj.put("mod", modeId);
            jj.put("sf", SF_code);
            jj.put("div", div);
            jj.put("StEndNeed", startEnd);
            jj.put("url", imageServer);
            jj.put("from", onDutyFrom.getText().toString());
            if(StrToCode.equalsIgnoreCase("-1")){
                jj.put("to", txtOthPlc.getText().toString());
            }else{
                jj.put("to", TextToAddress.getText().toString());
            }
            jj.put("to_code", StrToCode);
            jj.put("dailyAllowance", dailyAllowance.getText().toString());
            jj.put("driverAllowance", DriverNeed);
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;
            Callto = apiInterface.saveAllowance(jj.toString());

            Log.v("ONDUTY_TEXXT_REQ", Callto.request().toString());

            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.v("ONDUTY_TEXXT_RES", Callto.request().toString());
                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            if (js.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(On_Duty_Activity.this, " Submitted successfully ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Checkin.class);
                                Bundle extras = new Bundle();
                                flag=(HAPLocMode==true)?0:1;

                                extras.putString("ODFlag", String.valueOf(flag));
                                extras.putString("Mode", "onduty");
                                if (flag == 1) {
                                    extras.putString("onDutyPlcNm", ondutyedittext.getText().toString());
                                    extras.putString("onDutyPlcID", "0");
                                    extras.putString("onDuty", "abc");
                                } else {
                                    extras.putString("onDutyPlcNm", selecthaplocationss.getText().toString());
                                    extras.putString("onDutyPlcID", hapLocid);
                                    extras.putString("onDuty", "cba");
                                }
                                extras.putString("HolidayFlag", (chkHlyDyFlg.isChecked()) ? "1" : "0");
                                extras.putString("vstPurpose", purposeofvisitedittext.getText().toString());
                                intent.putExtras(extras);

                                shared_common_pref.save(Shared_Common_Pref.DAMode, true);

                                mLUService = new SANGPSTracker(On_Duty_Activity.this);
                                myReceiver = new LocationReceiver();
                                bindService(new Intent(On_Duty_Activity.this, SANGPSTracker.class), mServiceConection,
                                        Context.BIND_AUTO_CREATE);
                                LocalBroadcastManager.getInstance(On_Duty_Activity.this).registerReceiver(myReceiver,
                                        new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
                                mLUService.requestLocationUpdates();
                                startActivity(intent);
                            } else
                                Toast.makeText(On_Duty_Activity.this, " Cannot submitted the data ", Toast.LENGTH_SHORT).show();
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
*/
    @Override
    protected void onResume() {
        super.onResume();
        /*Log.v("LOG_IN_LOCATION", "ONRESTART");
        checking = String.valueOf(getIntent().getSerializableExtra("CHECKING"));
        Log.v("CHECKING_DATA", checking);

        if (sharedpreferences.contains("SharedImage")) {
            imageURI = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + imageURI);

            imageConvert = imageURI.substring(7);
            Log.e("COnvert", imageURI.substring(7));
            Log.e("COnvert", imageConvert);
            getMulipart(imageConvert, 0);
            attachedImage.setImageURI(Uri.parse(imageURI));
            Log.e("IMAGE_URI", imageURI);
        }
        */

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}