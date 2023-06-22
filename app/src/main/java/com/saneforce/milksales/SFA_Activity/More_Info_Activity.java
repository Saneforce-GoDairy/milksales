package com.saneforce.milksales.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class More_Info_Activity extends AppCompatActivity implements View.OnClickListener  {
    TextView button1, button2, button3, lastupdatedrecatrd;
    String CM = "", PM = "", PNM = "", CMY = "", PMY = "", PNY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__info_);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        lastupdatedrecatrd = findViewById(R.id.lastupdatedrecatrd);
        button1.setBackground(getResources().getDrawable(R.drawable.more_info_button));
        button1.setTextColor(getResources().getColor(R.color.white));
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        Get_MonthName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                button1.setBackground(getResources().getDrawable(R.drawable.more_info_button));
                button1.setTextColor(getResources().getColor(R.color.white));
                button2.setBackground(getResources().getDrawable(R.drawable.button_grey));
                button2.setTextColor(getResources().getColor(R.color.black));
                button3.setBackground(getResources().getDrawable(R.drawable.button_grey));
                button3.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.button2:
                button2.setBackground(getResources().getDrawable(R.drawable.more_info_button));
                button2.setTextColor(getResources().getColor(R.color.white));
                button1.setBackground(getResources().getDrawable(R.drawable.button_grey));
                button1.setTextColor(getResources().getColor(R.color.black));
                button3.setBackground(getResources().getDrawable(R.drawable.button_grey));
                button3.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.button3:
                button3.setBackground(getResources().getDrawable(R.drawable.more_info_button));
                button3.setTextColor(getResources().getColor(R.color.white));
                button1.setBackground(getResources().getDrawable(R.drawable.button_grey));
                button1.setTextColor(getResources().getColor(R.color.black));
                button2.setBackground(getResources().getDrawable(R.drawable.button_grey));
                button2.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }

    private void Get_MonthName() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "Get/MoreinfomonthName");
        QueryString.put("Sf_code", Shared_Common_Pref.Sf_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("GettodayResult", "response Tp_View: " + jsonObject.getJSONArray("Getmonthname"));
                    JSONArray jsoncc = jsonObject.getJSONArray("Getmonthname");
                    Log.e("LENGTH", String.valueOf(jsoncc.length()));
                    if (jsoncc.length() > 0) {
                        CMY = String.valueOf(jsoncc.getJSONObject(0).get("CM"));
                        PMY = String.valueOf(jsoncc.getJSONObject(0).get("PM"));
                        PNY = String.valueOf(jsoncc.getJSONObject(0).get("PNM"));
                        CM = String.valueOf(jsoncc.getJSONObject(1).get("CM"));
                        PM = String.valueOf(jsoncc.getJSONObject(1).get("PM"));
                        PNM = String.valueOf(jsoncc.getJSONObject(1).get("PNM"));
                        button1.setText(String.valueOf(jsoncc.getJSONObject(2).get("CM")));
                        button2.setText(String.valueOf(jsoncc.getJSONObject(2).get("PM")));
                        button3.setText(String.valueOf(jsoncc.getJSONObject(2).get("PNM")));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}