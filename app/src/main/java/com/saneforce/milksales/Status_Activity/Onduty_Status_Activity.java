package com.saneforce.milksales.Status_Activity;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.Dashboard;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Activity_Hap.PayslipFtp;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Adapter.Onduty_Status_Adapter;
import com.saneforce.milksales.Status_Model_Class.Onduty_Status_Model;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Onduty_Status_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    List<Onduty_Status_Model> approvalList;
    List<Common_Model> statusList = new ArrayList<>();
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    Intent i;
    String AMOD = "0";
    RelativeLayout rlStatus;
    TextView tvStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onduty__status_);

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        tvStatus = findViewById(R.id.tvStatus);
        rlStatus = findViewById(R.id.rlStatus);
        rlStatus.setOnClickListener(this);
        txtHelp.setOnClickListener(this);
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
        recyclerView = findViewById(R.id.ondutystatus);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        i = getIntent();
        AMOD = i.getExtras().getString("AMod");
        getleavestatus();
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

    public void getleavestatus() {
        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        common_class.ProgressdialogShow(1, "Onduty Status");
        Call<Object> mCall = apiInterface.GetTPObject1(i.getExtras().getString("AMod"), Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "Getonduty_Status", routemaster);
        mCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    // locationList=response.body();
                    String res = response.body().toString();
                    Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                    Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                    common_class.ProgressdialogShow(2, "Onduty Status");
                    userType = new TypeToken<ArrayList<Onduty_Status_Model>>() {
                    }.getType();
                    approvalList = sortDate(gson.fromJson(new Gson().toJson(response.body()), userType));


                    recyclerView.setAdapter(new Onduty_Status_Adapter(approvalList, R.layout.onduty_status_listitem, Onduty_Status_Activity.this, AMOD));

                    statusList.clear();
                    statusList.add(new Common_Model("ALL"));

                    for (int i = 0; i < approvalList.size(); i++) {
                        boolean isDuplicate = false;
                        for (int s = 0; s < statusList.size(); s++) {
                            if (statusList.get(s).getName().equalsIgnoreCase(approvalList.get(i).getOStatus())) {
                                isDuplicate = true;
                                break;
                            }

                        }

                        if (!isDuplicate)
                            statusList.add(new Common_Model(approvalList.get(i).getOStatus()));

                    }
                } catch (Exception e) {
                    Log.v("Res:Ex", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Permission Status");
            }
        });

    }


    List<Onduty_Status_Model> sortDate(List<Onduty_Status_Model> approvalList) {
        try {
            Collections.sort(approvalList, new Comparator<Onduty_Status_Model>() {
                DateFormat f = new SimpleDateFormat("dd-MM-yyyy");

                @Override
                public int compare(Onduty_Status_Model lhs, Onduty_Status_Model rhs) {
                    try {
                        return f.parse(rhs.getLoginDate()).compareTo(f.parse(lhs.getLoginDate()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
            return approvalList;
        } catch (Exception e) {
            Log.v("Sorting:", e.getMessage());
        }
        return approvalList;
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlStatus:
                common_class.showCommonDialog(statusList, 0, Onduty_Status_Activity.this);
                break;
            case R.id.toolbar_help:
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
                break;
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 0:
                tvStatus.setText(myDataset.get(position).getName());


                if (tvStatus.getText().toString().equalsIgnoreCase("") || tvStatus.getText().toString().equalsIgnoreCase("ALL")) {

                    recyclerView.setAdapter(new Onduty_Status_Adapter(approvalList, R.layout.onduty_status_listitem, Onduty_Status_Activity.this, AMOD));
                } else {
                    List<Onduty_Status_Model> filterList = new ArrayList<>();

                    for (int i = 0; i < approvalList.size(); i++) {
                        if (tvStatus.getText().toString().equalsIgnoreCase(approvalList.get(i).getOStatus()))
                            filterList.add(approvalList.get(i));
                    }

                    recyclerView.setAdapter(new Onduty_Status_Adapter(filterList, R.layout.onduty_status_listitem, Onduty_Status_Activity.this, AMOD));

                }


                break;
        }
    }
}