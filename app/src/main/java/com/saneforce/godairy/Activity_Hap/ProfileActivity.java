package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.Activity_Hap.Leave_Request.CheckInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.R;

public class ProfileActivity extends AppCompatActivity {
    public static final String UserDetail = "MyPrefs";
    ImageView mLogout, mCamera,BackBtn;
    private final Context context = this;
    private Shared_Common_Pref SHARED_PREF;
    String ScrNm;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences userDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        SHARED_PREF=new Shared_Common_Pref(ProfileActivity.this);
        TextView tvUserName = findViewById(R.id.user_name);
        TextView txtDesignation = findViewById(R.id.designation);
        TextView txtErp = findViewById(R.id.ERP_Code);
        TextView txtMobile = findViewById(R.id.mobile);
        TextView txtReportingTo = findViewById(R.id.reporting);
        TextView tv_desig = findViewById(R.id.tv_desig);

        mLogout = findViewById(R.id.btn_logout);
        BackBtn = findViewById(R.id.btn_backBtn);
        LinearLayout changePasswordLayout = findViewById(R.id.change_password_layout);
        mCamera = findViewById(R.id.btn_camera);

        ScrNm = getIntent().getStringExtra("Mode");
        SHARED_PREF.save("profback",ScrNm);

        changePasswordLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChangePasswordActivity.class);
            startActivity(intent);
        });


        mLogout.setOnClickListener(view -> {
            Intent intent = new Intent(context,Login.class);
            startActivity(intent);
        });
        BackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            if(ScrNm.equalsIgnoreCase("3"))
                intent = new Intent(context,SFA_Activity.class);
            else if(ScrNm.equalsIgnoreCase("2"))
                intent = new Intent(getApplicationContext(), Dashboard_Two.class);
            startActivity(intent);
        });

        String sUName = userDetails.getString("SfName", "");
        String SFDesig = userDetails.getString("SFDesig", "");
        String SFRptName = userDetails.getString("SFRptName","");
        String SFMobile = userDetails.getString("SFMobile","");
        if (SHARED_PREF.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
            String SFERP = SHARED_PREF.getvalue(Constants.DistributorERP, "");
            txtErp.setText(SFERP);
            SFMobile = SHARED_PREF.getvalue(Constants.Distributor_phone, "");
            SFDesig = SHARED_PREF.getvalue(Constants.DistributorAdd, "");
            tv_desig.setText("Address");
        }
        txtDesignation.setText(SFDesig);
        tvUserName.setText(sUName);
        txtReportingTo.setText(SFRptName);
        txtMobile.setText(SFMobile);

        mLogout.setOnClickListener(view -> {
            Intent intent = new Intent(context, LoginHome.class);
            startActivity(intent);
        });
    }

    }

