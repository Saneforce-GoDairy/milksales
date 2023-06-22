package com.saneforce.milksales.SFA_Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.R;

import java.lang.reflect.Type;

public class SFA_Dashboard extends AppCompatActivity  implements View.OnClickListener {
    Gson gson;
    Type userType;
    Common_Class common_class;
    LinearLayout lin_Distwise, lin_outlettype, lin_productwise, lin_daysummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_f_a__dashboard);
        lin_Distwise = findViewById(R.id.lin_Distwise);
        lin_outlettype = findViewById(R.id.lin_outlettype);
        lin_productwise = findViewById(R.id.lin_productwise);
        lin_daysummary = findViewById(R.id.lin_daysummary);
        common_class = new Common_Class(this);
        gson = new Gson();
        lin_Distwise.setOnClickListener(this);
        lin_outlettype.setOnClickListener(this);
        lin_productwise.setOnClickListener(this);
        lin_daysummary.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_Distwise:
               common_class.CommonIntentwithoutFinishputextratwo(Dashboard_View_Activity.class,"Name","Distributor","Servername","Dashboarddistcount");
                break;
            case R.id.lin_outlettype:
                common_class.CommonIntentwithoutFinishputextratwo(Dashboard_View_Activity.class,"Name","OutLet","Servername","DashOutletC");
                break;
            case R.id.lin_productwise:
                common_class.CommonIntentwithoutFinishputextratwo(Dashboard_View_Activity.class,"Name","ProductWise","Servername","DashProductC");
                break;
            case R.id.lin_daysummary:
                common_class.CommonIntentwithoutFinishputextratwo(Dashboard_View_Activity.class,"Name","DaySummary","Servername","DashDaySummary");
                break;
        }
    }




}