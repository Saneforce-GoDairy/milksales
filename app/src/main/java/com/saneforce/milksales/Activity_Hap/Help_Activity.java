package com.saneforce.milksales.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.On_ItemCLick_Listner;

import com.saneforce.milksales.Model_Class.Help_Model;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.Help_Adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Help_Activity extends AppCompatActivity implements View.OnClickListener {

    List<Help_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView, questionandanswerrecycler;
    Type userType;
    ImageView Closebutton;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    TextView title;
    LinearLayout qanda, onlyquestion;
    String titletext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_);

        recyclerView = findViewById(R.id.helpstatus);
        questionandanswerrecycler = findViewById(R.id.questionandanswerrecycler);
        title = findViewById(R.id.title);
        qanda = findViewById(R.id.qanda);
        title.setOnClickListener(this);
        qanda.setVisibility(View.GONE);
        onlyquestion = findViewById(R.id.onlyquestion);
        Closebutton = findViewById(R.id.Closebutton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionandanswerrecycler.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        getHelp();
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        Closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

    public void getHelp() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Object> mCall = apiInterface.GetTPObject(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "GetHelpList", routemaster);

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                //Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<Help_Model>>() {
                }.getType();
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);
                Log.e("Leave_Adapter", String.valueOf(approvalList));
                recyclerView.setAdapter(new Help_Adapter(approvalList, R.layout.add_helplistitem, getApplicationContext(), 1, new On_ItemCLick_Listner() {
                    @Override
                    public void onIntentClick(int Name) {
                        qanda.setVisibility(View.VISIBLE);
                        onlyquestion.setVisibility(View.GONE);
                        getHelpqA(approvalList.get(Name).getId());
                        titletext = approvalList.get(Name).getText();
                    }
                }));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    public void getHelpqA(String Id) {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/helpdet");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("id", Id);

        Call<Object> mCall = apiInterface.Get_Object(QueryString, routemaster);

        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // locationList=response.body();
                Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<Help_Model>>() {
                }.getType();
                Log.e("TEXT_VALUES", String.valueOf(titletext));

                title.setText(titletext);
                approvalList = gson.fromJson(new Gson().toJson(response.body()), userType);
                Log.e("Leave_Adapter", String.valueOf(approvalList));
                questionandanswerrecycler.setAdapter(new Help_Adapter(approvalList, R.layout.add_helplistitem, getApplicationContext(), 2, new On_ItemCLick_Listner() {
                    @Override
                    public void onIntentClick(int Name) {

                    }
                }));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Help_Activity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                qanda.setVisibility(View.GONE);
                onlyquestion.setVisibility(View.VISIBLE);
                break;
        }
    }
}