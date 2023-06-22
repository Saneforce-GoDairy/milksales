package com.saneforce.milksales.Activity_Hap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Activity.Leave_Status_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveReasonStatus extends AppCompatActivity {
    private EditText edtReason;
    private Button reasonSend;
    String leaveId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_reason_status);
        edtReason = findViewById(R.id.edt_reason);
        reasonSend = findViewById(R.id.button_send);


        leaveId = String.valueOf(getIntent().getSerializableExtra("LeaveId"));

        reasonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtReason.getText().toString().equalsIgnoreCase("")) {
                    SendtpApproval();
                } else {
                    Toast.makeText(LeaveReasonStatus.this, "Enter Reason", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void SendtpApproval() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "dcr/save");
        QueryString.put("Sf_Code", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("Leave_Id", leaveId);
        QueryString.put("Reason", edtReason.getText().toString());


        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            jsonObject.put("LeaveCancel", sp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());

        Log.v("LEAVE_STATUS",mCall.request().toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String success = jsonObject.getString("success");
                    if (success.equalsIgnoreCase("true")) {

                        startActivity(new Intent(LeaveReasonStatus.this, Leave_Status_Activity.class));
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}