package com.saneforce.milksales.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.saneforce.milksales.Activity_Hap.SFA_Activity;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.InshopModel;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.milksales.SFA_Model_Class.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InshopActivity extends AppCompatActivity {
    CardView checkin,checkout,inshop;
    ImageView back;
    ArrayList<Retailer_Modal_List> retailerList = new ArrayList<>();

    ArrayList<InshopModel> checkInList=new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;
    String SF_code = "", div = "", State_Code = "", date="", SFName="";
    String retailerName = "";
    String retailer_code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop);

        back = findViewById(R.id.inshop_back);
        checkin = findViewById(R.id.checkinLay);
        checkout = findViewById(R.id.checkoutLay);
        inshop = findViewById(R.id.inshopLay);

        date = TimeUtils.getCurrentTime(TimeUtils.FORMAT1);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        SFName = UserDetails.getString("SfName","");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InshopActivity.this, InshopCheckinActivity.class));

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int key=1;
                checkExist(key);
//                startActivity(new Intent(InshopActivity.this, InshopCheckoutActivity.class));

            }
        });
        inshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int key=2;
                checkExist(key);
//                startActivity(new Intent(InshopActivity.this, InshopOutletActivity.class));

            }
        });
    }

    public void getRetailerName(){
        for (int i=0;i<checkInList.size();i++){
            for (int j=0;j<retailerList.size();j++){
                if((checkInList.get(i).getRetailercode()).equals(retailerList.get(j).getMobileNumber())){
                    retailerName=checkInList.get(j).getRetailername();
                    Log.v("rName",checkInList.get(j).getRetailername());
                    Log.v("rCode",retailer_code);
                    retailer_code=retailerList.get(j).getMobileNumber();
                }

            }
        }
    }
    private void checkExist(int key) {


        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = request.getInshopRetailer("get/checkInList",
                div,
                SF_code,
                SF_code,
                Shared_Common_Pref.StateCode,
                date);
        call.enqueue(new Callback<ResponseBody>(){

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    Log.d("CheckInActivity","responseBody"+responseBody);
                    if (responseBody != null) {
                        checkInList.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody.toString());
                            Log.d("CheckOutActivity","jsonArray"+jsonArray.length());

                            for(int i = 0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                InshopModel checkInModel=new InshopModel(jsonObject.getInt("Sl_No"),jsonObject.getString("Sf_Code"),jsonObject.getString("Retailer_Code"),jsonObject.getString("eKey"));

                                if(jsonObject.has("CIn_Time")){
                                    String string= jsonObject.getString("CIn_Time");
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(string);
                                        if(jsonObject1.has("date")){

                                            InshopModel cInTime=new InshopModel(jsonObject1.getString("date"));
                                            checkInModel.setCintime(cInTime.toString());
                                            checkInModel.setCintime(jsonObject1.getString("date"));
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
                                        getRetailerName();
                                        count++;
                                        if(key==2) {
                                            Intent intent = new Intent(InshopActivity.this, InshopOutletActivity.class);
                                            intent.putExtra("retailer_name", retailerName);
                                            intent.putExtra("retailer_code", retailer_code);
                                            startActivity(intent);
                                        }else{
                                            Intent intent=new Intent(InshopActivity.this, InshopCheckoutActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                                if(count==0){
                                    setAlertBox();
                                }

                            }else{
                                setAlertBox();
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }else{

                        Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error", "" + t.getMessage());
                Toast.makeText(getApplicationContext(), "Failure : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setAlertBox() {
        new AlertDialog.Builder(InshopActivity.this)
                .setTitle("Check-In")
                .setMessage("Do you want to Check-In")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent =new Intent(InshopActivity.this,InshopCheckinActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(InshopActivity.this, SFA_Activity.class));
        super.onBackPressed();
    }
}