package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.Activity_Hap.Leave_Request.CheckInfo;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.Tp_Approval_FF_Modal;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.adapters.Tp_Approval_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TP_Approval_Details extends AppCompatActivity {
    private Gson gson;
    private Type userType;
    private List<Tp_Approval_FF_Modal> Tp_Approval_Model;
    private List<Tp_Approval_FF_Modal> filteredList= new ArrayList<>();;
    private RecyclerView recyclerView;
    private TextView tvName;
    private Common_Class common_class;
    private Toolbar toolbar;
    private  String name="",SF_code = "",currentDate="",reportingSF="";
    public static TP_Approval_Details tpDetails;
    private SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    private Shared_Common_Pref sharedCommonPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp_approval_details);

        tpDetails = this;
        sharedCommonPref = new Shared_Common_Pref(this);
        gson = new Gson();

        recyclerView = findViewById(R.id.tp_approval_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvName=findViewById(R.id.tpApprovalName);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        name = getIntent().getStringExtra("FieldForceName");
        tvName.setText(name);

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtHelp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Help_Activity.class)));
        txtErt.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ERT.class)));
        txtPlaySlip.setOnClickListener(v -> {});
        imgHome.setOnClickListener(v -> {
            SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);

            Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
            if (CheckIn == true) {
                Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                Dashboard.putExtra("Mode", "CIN");
                startActivity(Dashboard);
            } else
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
        });

        gettp_Details();

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();

        ImageView backView = findViewById(R.id.imag_back);
        Button approve = findViewById(R.id.btnPjpApprove);
        Button reject = findViewById(R.id.btnPjpReject);

        backView.setOnClickListener(v -> mOnBackPressedDispatcher.onBackPressed());

        approve.setOnClickListener(v -> {
            try {
                submitData(1,"");
            } catch (Exception ignored) {
            }
        });

        reject.setOnClickListener(v -> {
            final AppCompatActivity activity=(AppCompatActivity)v.getContext();
            final AlertDialog alertDialog=new AlertDialog.Builder(activity).create();
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(HAPApp.Title);
            builder.setMessage("Do you confirm to Reject PJP Approval?");
            final View customLayout
                    = TP_Approval_Details.tpDetails.getLayoutInflater().inflate(R.layout.ta_reject_popup, null);
            builder.setView(customLayout);

            builder.setPositiveButton("Yes", (dialog, which) -> {
                EditText editText = customLayout.findViewById(R.id.editText);

                if (editText.getText().toString().equalsIgnoreCase("")) {
                    common_class.showMsg(TP_Approval_Details.this, "Please Enter the Reason");
                } else {
                    dialog.dismiss();
                    submitData(2,editText.getText().toString());
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> alertDialog.dismiss());
            builder.show();
        });
    }

    private void submitData(int flag, String reason) {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("rSF", UserDetails.getString("Sfcode", ""));
            jObj.put("Confirmed_Date",currentDate);
            JSONArray jArr = new JSONArray();
            for (int i = 0; i < filteredList.size(); i++) {
                JSONObject obj1 = new JSONObject();
                obj1.put("Date", filteredList.get(i).getDate());
                obj1.put("Confirmed", flag);
                obj1.put("sfcode", filteredList.get(i).getSF_Code());
                obj1.put("Rejection_Reason", reason);
                jArr.put(obj1);
            }
            jObj.accumulate("PjpApprovedData" , jArr);
            Log.d("savePjpApproval", "ghkj" + jObj.toString());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> responseBodyCall = apiInterface.pjpApprove(SF_code,Shared_Common_Pref.Sf_Code, currentDate, jObj.toString());
            responseBodyCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.e("JSON_VALUES", response.body().toString());
                            TP_Approval_Details.tpDetails.finish();
                            if (flag == 1) {
                                Toast.makeText(TP_Approval_Details.this, "PJP Approved Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TP_Approval_Details.this, "PJP Rejected  Successfully", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(TP_Approval_Details.this, TP_Approval_Details.class);
                            finish();
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.v("error", e.toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("SUBMIT_VALUE", "ERROR");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(() -> TP_Approval_Details.super.onBackPressed());

    @Override
    public void onBackPressed() {
    }

    public void gettp_Details() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Object> mCall = apiInterface.GetPJPApproval(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "vwtplist", routemaster);
        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("GetTPDetailsList", response.body().toString());
                userType = new TypeToken<ArrayList<Tp_Approval_FF_Modal>>() {
                }.getType();
                Tp_Approval_Model = gson.fromJson(new Gson().toJson(response.body()), userType);

                if (Tp_Approval_Model != null && Tp_Approval_Model.size() > 0) {
                    for (Tp_Approval_FF_Modal filterlist : Tp_Approval_Model) {
                        if (name != null && name.equalsIgnoreCase(filterlist.getFieldForceName())) {
                            filteredList.add(filterlist);
                        }
                    }
                }

                Log.d("wwwwe","filteredList"+ filteredList.toString());
                recyclerView.setAdapter(new Tp_Approval_Adapter(filteredList, R.layout.tp_details_layout, getApplicationContext()
                        , new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int Name) {
                        Intent intent = new Intent(TP_Approval_Details.this, TP_Approval_Status.class);
                        intent.putExtra("FieldForceName",filteredList.get(Name).getFieldForceName());
                        intent.putExtra("date", filteredList.get(Name).getDate());
                        intent.putExtra("SFCode", filteredList.get(Name).getSfCode());
                        intent.putExtra("Work_Type", filteredList.get(Name).getWorktypeName());
                        intent.putExtra("remarks", filteredList.get(Name).getRemarks());
                        intent.putExtra("month",filteredList.get(Name).getMonthnameexample());
                        intent.putExtra("HQ",filteredList.get(Name).getHQName());
                        intent.putExtra("designation",filteredList.get(Name).getDesignation());
                        intent.putExtra("rejectionReason",filteredList.get(Name).getRejectionReason());
                        intent.putExtra("confirmed",filteredList.get(Name).getConfirmed());
                        intent.putExtra("confirmedDate",filteredList.get(Name).getConfirmedDate());
                        startActivity(intent);
                    }
                }));
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }
}
