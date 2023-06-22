package com.saneforce.milksales.Activity_Hap;

import static com.saneforce.milksales.Activity_Hap.Approvals.CheckInfo;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Model_Class.Tp_Approval_FF_Modal;
import com.saneforce.milksales.R;

import java.lang.reflect.Type;
import java.util.List;

public class TP_Approval_Status extends AppCompatActivity {

    Gson gson;
    Type userType;
    List<Tp_Approval_FF_Modal> Tp_Approval_Model;

    private RecyclerView recyclerView;
    TextView tvName,tvDate,tvRemarks,tvWorktype,tvHqName,tvReason,tvApproved,tvRejected,tvAppliedDate,tvConfirmedDate,tvStatus;
    Common_Class common_class;
    private Toolbar toolbar;
    private  String name="",date="",remarks="",worktype="",hq="",reason="",confirmed="",appliedDate="",confirmedDate="",SF_code = "";
    LinearLayout reasonLay,approvedDateLay;

    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    Shared_Common_Pref sharedCommonPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp_approval_status);

        gson = new Gson();
        common_class = new Common_Class(this);
//        recyclerView = findViewById(R.id.tp_status_recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        tvName=findViewById(R.id.tpApprovalStatusName);
        tvDate=findViewById(R.id.date);
        tvRemarks=findViewById(R.id.remarks);
        tvWorktype=findViewById(R.id.workType);
        tvHqName=findViewById(R.id.hq);
        tvReason=findViewById(R.id.reason);
        tvApproved=findViewById(R.id.status_approved);
        tvRejected=findViewById(R.id.status_rejected);
        reasonLay=findViewById(R.id.reasonLayout);
        approvedDateLay=findViewById(R.id.approvedDateLay);
        tvConfirmedDate=findViewById(R.id.status_ApprovedDate);
        tvStatus=findViewById(R.id.tvApprovedText);
        tvAppliedDate=findViewById(R.id.appliedDate);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");

        name=getIntent().getStringExtra("FieldForceName");
        tvName.setText(name);
        date=getIntent().getStringExtra("date");
        tvDate.setText(date);
        remarks=getIntent().getStringExtra("remarks");
        tvRemarks.setText(remarks);
        worktype=getIntent().getStringExtra("Work_Type");
        tvWorktype.setText(worktype);
        hq=getIntent().getStringExtra("HQ");
        tvHqName.setText(hq);
        reason=getIntent().getStringExtra("Rejection_Reason");
        tvReason.setText(reason);
        appliedDate=getIntent().getStringExtra("date");
        tvAppliedDate.setText(appliedDate);
        confirmed=getIntent().getStringExtra("Confirmed");
        confirmedDate=getIntent().getStringExtra("ConfirmedDate");
        tvConfirmedDate.setText(confirmedDate);

//        if (confirmed.equals("0")){
//            tvApproved.setVisibility(View.VISIBLE);
//            tvRejected.setVisibility(View.GONE);
//            reasonLay.setVisibility(View.GONE);
//        }
//        else{
//            tvApproved.setVisibility(View.GONE);
//            tvRejected.setVisibility(View.VISIBLE);
//            reasonLay.setVisibility(View.VISIBLE);
//            tvStatus.setText("Rejected : ");
//        }

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));


            }
        });

//        gettp_Details();

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

//    public void gettp_Details() {
//        String routemaster = " {\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//
//        Call<Object> mCall = apiInterface.GetPJPApproval(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, "getworkplanpjp", routemaster);
//        mCall.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                try {
//                    if (response.isSuccessful()) {
//
//                        Log.e("TAG_TP_RESPONSE_status", "response Tp_View: " + new Gson().toJson(response.body()));
//                        userType = new TypeToken<ArrayList<Tp_Approval_FF_Modal>>() {
//                        }.getType();
//                        Tp_Approval_Model = gson.fromJson(new Gson().toJson(response.body()), userType);
//
//                        recyclerView.setAdapter(new Tp_ApprovalStatus_Adapter(Tp_Approval_Model, R.layout.tp_approval_status_layout, getApplicationContext(), new AdapterOnClick() {
//                            @Override
//                            public void onIntentClick(int Name) {
//
//                                Intent intent = new Intent(TP_Approval_Status.this, TP_Approval_Details.class);
//                                intent.putExtra("FieldForceName", Tp_Approval_Model.get(Name).getFieldForceName());
//                                intent.putExtra("ReportingSF", Tp_Approval_Model.get(Name).getReportingSFCode());
//                                intent.putExtra("sfCode", Tp_Approval_Model.get(Name).getSfCode());
//                                intent.putExtra("Emp_Code", Tp_Approval_Model.get(Name).getEmpCode());
//                                intent.putExtra("Work_Type", Tp_Approval_Model.get(Name).getWorktypeName());
//                                intent.putExtra("HQ", Tp_Approval_Model.get(Name).getHQName());
////                        sharedCommonPref.save(Constants.rSFCode,Tp_Approval_Model.get(Name).getReportingSFCode());
////
////                        Log.v("esfcode",Tp_Approval_Model.get(Name).getReportingSFCode());
//
////                      intent.putExtra("Plan_Date", Tp_Approval_Model.get(Name).getStartDate());
//                        /*intent.putExtra("Emp_Code", Tp_Approval_Model.get(Name).getEmpCode());
//                        intent.putExtra("HQ", Tp_Approval_Model.get(Name).getHQ());
//                        intent.putExtra("Designation", Tp_Approval_Model.get(Name).getDesignation());
//                        intent.putExtra("MobileNumber", Tp_Approval_Model.get(Name).getSFMobile());
//                        intent.putExtra("Plan_Date", Tp_Approval_Model.get(Name).getStartDate());
//
//                        intent.putExtra("Route", Tp_Approval_Model.get(Name).getRouteName());
//                        intent.putExtra("Distributor", Tp_Approval_Model.get(Name).getWorkedWithName());
//                        intent.putExtra("Sf_Code", Tp_Approval_Model.get(Name).getSFCode());
//                        intent.putExtra("Remarks", Tp_Approval_Model.get(Name).getRemarks());
//                        intent.putExtra("workedwithname", Tp_Approval_Model.get(Name).getJointWorkName());
//                        intent.putExtra("TPHqname", Tp_Approval_Model.get(Name).getTourHQName());
//                        intent.putExtra("ShiftType", Tp_Approval_Model.get(Name).getTypename());
//                        intent.putExtra("ChillCentreName", Tp_Approval_Model.get(Name).getCCentreName());
//                        intent.putExtra("FromDate", Tp_Approval_Model.get(Name).getFromdate());
//                        intent.putExtra("Worktype_Flag", Tp_Approval_Model.get(Name).getWorktypeFlag());
//                        intent.putExtra("ToDate", Tp_Approval_Model.get(Name).getTodate());
//                        intent.putExtra("DeptType", Tp_Approval_Model.get(Name).getDeptType());
//                        intent.putExtra("MOT", Tp_Approval_Model.get(Name).getMOT());
//                        intent.putExtra("DA_Type", Tp_Approval_Model.get(Name).getDA_Type());
//                        intent.putExtra("Da", Tp_Approval_Model.get(Name).getDriver_Allow());
//                        intent.putExtra("From_Place", Tp_Approval_Model.get(Name).getFrom_Place());
//                        intent.putExtra("To_Place", Tp_Approval_Model.get(Name).getTo_Place());*/
//
//
//                                startActivity(intent);
//
//                                //startActivity(new Intent(this, Tp_Month_Select.class));
//
//                            }
//                        }));
//
//                    }
//                } catch (JsonSyntaxException e) {
//                    e.printStackTrace();
//                }
//            }
//
//                @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                Toast.makeText(TP_Approval_Status.this
//                        ,"response failed",Toast.LENGTH_SHORT).show();
//            }
//
//
//        });
//
//    }





    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    TP_Approval_Status.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }
}
