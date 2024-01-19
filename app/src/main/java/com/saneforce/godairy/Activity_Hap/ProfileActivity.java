package com.saneforce.godairy.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.saneforce.godairy.R;

public class ProfileActivity extends AppCompatActivity {
    public static final String UserDetail = "MyPrefs";
    ImageView mLogout, mCamera;
    private final Context context = this;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences userDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        TextView tvUserName = findViewById(R.id.user_name);
        TextView txtDesignation = findViewById(R.id.designation);
        TextView txtMobile = findViewById(R.id.mobile);
        TextView txtReportingTo = findViewById(R.id.reporting);
        mLogout = findViewById(R.id.btn_logout);
        LinearLayout changePasswordLayout = findViewById(R.id.change_password_layout);
        mCamera = findViewById(R.id.btn_camera);

        changePasswordLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChangePasswordActivity.class);
            startActivity(intent);
        });


        mCamera.setOnClickListener(view -> {
            Intent intent = new Intent(context, ImageCapture.class);
            startActivity(intent);
        });

        String sUName = userDetails.getString("SfName", "");
        String SFDesig = userDetails.getString("SFDesig", "");
        String SFRptName = userDetails.getString("SFRptName","");
        String SFMobile = userDetails.getString("SFMobile","");

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
