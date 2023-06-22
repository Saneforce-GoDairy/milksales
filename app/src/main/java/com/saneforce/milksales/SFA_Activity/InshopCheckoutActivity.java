package com.saneforce.milksales.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity_Hap.AllowancCapture;
import com.saneforce.milksales.Activity_Hap.ProductImageView;
import com.saneforce.milksales.Common_Class.CameraPermission;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.InshopModel;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.milksales.SFA_Model_Class.TimeUtils;

import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InshopCheckoutActivity extends AppCompatActivity {

    TextView checkoutTunTime, checkinTime, rName,tvDate,tvCheckout;
    final Handler handler = new Handler();
    Button checkout;
    String checkoutTime;
    ImageView attachedImage,back;
    CardView attach;
    ApiInterface apiInterface;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;
    int Sl_no=0;
    String SF_code = "", div = "", State_Code = "", date="",UserInfo = "MyPrefs",imageSet = "", imageServer = "",imageConvert = "",
            retailerName="",SFName="",coutTime="";
    String Date= "";
    String time="";
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    ArrayList<Retailer_Modal_List> retailerList = new ArrayList<>();
    ArrayList<InshopModel> checkInList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_checkout);

        sharedCommonPref = new Shared_Common_Pref(InshopCheckoutActivity.this);

        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        common_class = new Common_Class(this);

        checkoutTunTime = findViewById(R.id.tvCheckoutRunTime);
        checkinTime = findViewById(R.id.tvCheckinTime);
        rName = findViewById(R.id.ischeckoutRetName);
        tvDate = findViewById(R.id.iscoutDate);
        checkout = findViewById(R.id.btnInshopCheckout);
        tvCheckout = findViewById(R.id.tvCheckoutTime);
        attachedImage = findViewById(R.id.cout_attachedImage);
        attach = findViewById(R.id.galleryCard);
        back = findViewById(R.id.cout_back);

        rName.setText(InshopCheckinActivity.getName());
        checkinTime.setText(InshopCheckinActivity.getCheckinTime());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        SFName = UserDetails.getString("SfName","");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = TimeUtils.getCurrentTime(TimeUtils.FORMAT1);
        tvDate.setText(date);
        coutTime = TimeUtils.getCurrentTime(TimeUtils.FORMAT);
        handler.postDelayed(new Runnable() {
            public void run() {
                checkoutTunTime.setText(Common_Class.GetRunTime());
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        getCheckinStatus();

        checkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkoutTime = checkoutTunTime.getText().toString().trim();
                Log.v("checkoutTime",checkoutTime);

                int count=0;
                for(int i=0;i<checkInList.size();i++) {
                    if (checkInList.size()!=0 &&checkInList.get(i).getCflag()==1) {
                        count++;
                        Sl_no=checkInList.get(i).getSlno();
                    }
                }
                if(count>0){
                    checkoutData(Sl_no);
                }else {
                    rName.setVisibility(View.GONE);
                    checkinTime.setVisibility(View.GONE);
                    Toast.makeText(InshopCheckoutActivity.this,"Nothing to Check Out",Toast.LENGTH_SHORT).show();
                }
            }

           /* @Override
            public void onClick(View view) {
                checkoutTime = checkoutTunTime.getText().toString().trim();
                Log.v("checkoutTime",checkoutTime);

//                checkoutData();

//                Toast.makeText(InshopCheckoutActivity.this,"Checkout Successfully",Toast.LENGTH_SHORT).show();
            }*/
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPermission cameraPermission = new CameraPermission(InshopCheckoutActivity.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                } else {

                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {

                            UploadPhoto(fullPath, UserDetails.getString("Sfcode", ""), FileName, "Travel", image);

                        }
                    });
                    Intent intent = new Intent(InshopCheckoutActivity.this, AllowancCapture.class);
                    intent.putExtra("allowance", "Two");
                    startActivity(intent);

                }

            }
        });

        attachedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                intent.putExtra("ImageUrl", imageSet);
                startActivity(intent);
            }
        });

    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
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
        return yy;
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

                                    imageServer=FileName;
                                    imageConvert = path;
                                    imageSet = "file://" + path;
                                    attachedImage.setImageBitmap(image);
                                    attachedImage.setVisibility(View.VISIBLE);

                                }


                                common_class.ProgressdialogShow(0, "");

                                common_class.showMsg(InshopCheckoutActivity.this, "File uploading successful ");
                            } else {
                                common_class.ProgressdialogShow(0, "");
                                common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");
                            }
                        } else {

                            common_class.ProgressdialogShow(0, "");
                            common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");

                        }

                    } catch (Exception e) {
                        common_class.ProgressdialogShow(0, "");
                        common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    common_class.ProgressdialogShow(0, "");
                    common_class.showMsg(InshopCheckoutActivity.this, "Failed.Try Again...");

                    Log.e("SEND_IMAGE_Response", "ERROR");
                }
            });


        } catch (Exception e) {
            Log.e("TAClaim:", e.getMessage());
        }
    }

    private void checkoutData(int sl_no) {

        JsonArray jsonArray = new JsonArray();
        JsonObject activityReportAppObject = new JsonObject();
        activityReportAppObject.addProperty("c_flag",0);
        activityReportAppObject.addProperty("CheckoutTime","'" + coutTime+ "'");
        activityReportAppObject.addProperty("Sl_No","'"+sl_no+"'");
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.add("Activity_Check_Out_App", activityReportAppObject);
        jsonArray.add(jsonObject1);

        JsonObject jsonObject5 = new JsonObject();

        JsonArray jsonArray1 = new JsonArray();
        for (int i = 0; i < 1; i++) {
            JsonObject activityEventCapturesObject = new JsonObject();
            activityEventCapturesObject.addProperty("imgurl", "'" + imageServer + "'");
            jsonArray1.add(activityEventCapturesObject);

        }

        jsonObject5.add("Activity_Event_Captures", jsonArray1);

        jsonArray.add(jsonObject5);

        Log.d("OutletCheckInActivity","orderlist"+jsonArray.toString());
        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = request.inshopSave(div, SF_code,"dcr/save", jsonArray.toString() );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    if (responseBody != null) {
                        try {
                            JSONObject jsonArray = new JSONObject(responseBody);
                            Log.v("jsonvalues....",responseBody);
                            if (jsonArray.getBoolean("success")) {
                                if(jsonArray.has("msg")){

                                    Toast.makeText(InshopCheckoutActivity.this, jsonArray.getString("msg"), Toast.LENGTH_LONG).show();
                                }else
                                    Toast.makeText(InshopCheckoutActivity.this, "CheckOut Entry Submitted Successfully", Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                if(jsonArray.has("msg")){
                                    Toast.makeText(InshopCheckoutActivity.this, jsonArray.getString("msg"), Toast.LENGTH_LONG).show();
                                }else
                                    Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Exception 1 " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else
                        Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Exception 2 " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                Log.d("Error", "" + t.getMessage());
                Toast.makeText(getApplicationContext(), "Failure : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCheckinStatus() {
        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = request.getInshopRetailer("get/checkInList",
                div,
                SF_code,
                SF_code,
                Shared_Common_Pref.StateCode,
                date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    if (responseBody != null &&!responseBody.isEmpty()) {
                        checkInList.clear();
                        Log.v("responseList",responseBody);
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            Log.d("CheckOutActivity", "jsonArray" + jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                InshopModel checkInModel=new InshopModel(jsonObject.getInt("Sl_No"),jsonObject.getString("Sf_Code"),jsonObject.getString("Retailer_Code"),jsonObject.getString("eKey"));

                                String name = jsonObject.getString("Retailer_Name");
                                rName.setText(name);

                                if(jsonObject.has("CIn_Time")){
                                    String string= jsonObject.getString("CIn_Time");
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(string);
                                        if(jsonObject1.has("date")){
                                            InshopModel cInTime=new InshopModel(jsonObject1.getString("date"));
                                            checkInModel.setCintime(String.valueOf(cInTime));
                                            String  s=jsonObject1.getString("date");


                                            try {
                                                String ss=" ";
                                                time = s.substring(s.indexOf(ss),s.length()).trim();
                                                checkinTime.setText(time);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                String ss=" ";
                                                Date = s.substring(0,s.indexOf(ss)).trim();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            checkInModel.setCintime(time);
                                            checkInModel.setEntrydate(jsonObject1.getString("date"));



                                        };

                                    }catch (JSONException err){
                                        Log.d("Error", err.toString());
                                    }
                                }
                                if(jsonObject.has("C_Flag")){
                                    checkInModel.setCflag(jsonObject.getInt("C_Flag"));
                                }
                                checkInList.add(checkInModel);

                            }
                            int count=0;
                            if(checkInList.size()!=0) {
                                for (int k = 0; k < checkInList.size(); k++) {
                                    if (checkInList.get(k).getCflag() == 1) {
//                                        tv_cIn_name.setVisibility(View.VISIBLE);
//                                        tv_status.setVisibility(View.VISIBLE);
//                                        tv_date.setVisibility(View.GONE);
//                                        ll_order.setVisibility(View.VISIBLE);
//
//                                        checkedinLay.setVisibility(View.VISIBLE);
//                                        checkinLay.setVisibility(View.GONE);
                                        getRetailerName();
                                        rName.setVisibility(View.VISIBLE);
                                        checkinTime.setVisibility(View.VISIBLE);
//                                        rName.setVisibility(View.GONE);
//                                        checkinTime.setVisibility(View.GONE);
//                                        tv_cIn_name.setText(retailerLabelName + ": " + retailerName);
//                                        // tv_date.setText("Date: "+Date);
//                                        tv_tl_date.setVisibility(View.VISIBLE);
//                                        tv_tl_date.setText(Date);
//                                        ll_event_captures.setVisibility(View.GONE);
//                                        ll_retailer.setVisibility(View.GONE);
//                                        ll_head.setVisibility(View.VISIBLE);
//                                        btn_save.setVisibility(View.GONE);
//                                        textClock.setVisibility(View.GONE);
//                                        c_time.setVisibility(View.VISIBLE);
//                                        c_time.setText(checkInList.get(k).getTime());
//                                        checkedinTime.setText(checkInList.get(k).getCintime());
//                                        checkedinTime.setVisibility(View.VISIBLE);


                                    }else{
                                        rName.setVisibility(View.GONE);
                                        checkinTime.setVisibility(View.GONE);

                                    }
                                }
//
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void getRetailerName() {
        for (int i=0;i<checkInList.size();i++){
            for (int j=0;j<retailerList.size();j++){
                if((checkInList.get(i).getRetailercode()).equals(retailerList.get(j).getMobileNumber())){
                    retailerName =retailerList.get(j).getName();
                    //retailer_code=retailerList.get(j).getMobileNumber();
                    Log.d("checkInActivityrName","name"+retailerName);
                }

            }
        }
    }

}



