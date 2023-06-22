package com.saneforce.milksales.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.AllowancCapture;
import com.saneforce.milksales.Activity_Hap.CustomListViewDialog;
import com.saneforce.milksales.Common_Class.CameraPermission;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.Model_Class.ModeOfTravel;
import com.saneforce.milksales.R;

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

public class FuleEntryActivity extends AppCompatActivity implements Master_Interface {
    String CheckInfo = "CheckInDetail",  UserInfo = "MyPrefs";
    SharedPreferences UserDetails, CheckInDetails;
    LinearLayout spnrModTrv,spnrModTyp,imgFrom,imgTo;
    TextView txModTrv,txModTyp;
    ImageView startImg,endImg;
    Gson gson;
    Type userType;

    List<ModeOfTravel> modelOfTravel;
    List<Common_Model> lstTrvType = new ArrayList<>();
    List<Common_Model> lstType = new ArrayList<>();
    List<Common_Model> lstLocations = new ArrayList<>();

    CustomListViewDialog customDialog;
    ApiInterface apiInterface;
    String sSFCode,sDiv,sState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fule_entry);

        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        sDiv=UserDetails.getString("Divcode","");
        sSFCode=UserDetails.getString("Sfcode","");
        sState=UserDetails.getString("State_Code","");

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        gson = new Gson();

        lstTrvType.clear();
        loadModofTravel();
        //loadLocations();
        Common_Model item1 = new Common_Model("Start", "1");
        lstType.add(item1);
        Common_Model item2 = new Common_Model("Start", "1");
        lstType.add(item2);

        spnrModTrv = findViewById(R.id.fule_MT_spnr);
        txModTrv=findViewById(R.id.fule_MT);
        spnrModTyp = findViewById(R.id.fule_Typ_spnr);
        txModTyp=findViewById(R.id.fule_Typ);

        imgFrom = findViewById(R.id.skm_pic);
        imgTo=findViewById(R.id.ekm_pic);
        startImg = findViewById(R.id.skm_img);
        endImg=findViewById(R.id.ekm_img);

        spnrModTrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(FuleEntryActivity.this, lstTrvType, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                //dynamicMode();
            }
        });

        imgFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cameraPermission = new CameraPermission(FuleEntryActivity.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                } else {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImagePick(Bitmap image, String FileName) {
                            startImg.setImageBitmap(image);
                        }
                    });
                    Intent intent = new Intent(FuleEntryActivity.this, AllowancCapture.class);
                    startActivity(intent);

                }
            }
        });
        imgTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cameraPermission = new CameraPermission(FuleEntryActivity.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                } else {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImagePick(Bitmap image, String FileName) {
                            endImg.setImageBitmap(image);
                        }
                    });
                    Intent intent = new Intent(FuleEntryActivity.this, AllowancCapture.class);
                    startActivity(intent);

                }
            }
        });
    }
    public void loadModofTravel() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", sDiv);
        QueryString.put("sfCode", sSFCode);
        QueryString.put("rSF", sSFCode);
        QueryString.put("State_Code", sState);
        String commonLeaveType = "{\"tableName\":\"getmodeoftravel\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                userType = new TypeToken<ArrayList<ModeOfTravel>>() {
                }.getType();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    Common_Model item = new Common_Model(id, name, modeId, driverMode);
                    lstTrvType.add(item);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }
    public void loadLocations() {
        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", sSFCode);
            jj.put("divisionCode", sDiv);
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
                    Common_Model item = new Common_Model(id, name, "");
                    lstLocations.add(item);
                }
                Common_Model itmOth = new Common_Model("-1","Other Location","");
                lstLocations.add(itmOth);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
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
                try {
                    if (response.isSuccessful()) {
                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            //imageServer = js.getString("url");
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
        switch (type){
            case 1:
                txModTrv.setText(myDataset.get(position).getName());
                break;
            case 2:
                txModTyp.setText(myDataset.get(position).getName());
                break;

        }
        customDialog.hide();
    }
}