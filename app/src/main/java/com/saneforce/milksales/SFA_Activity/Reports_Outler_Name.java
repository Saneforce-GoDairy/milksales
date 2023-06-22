package com.saneforce.milksales.SFA_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.Outlet_Info_Adapter;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Reports_Outler_Name extends AppCompatActivity {
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView  textViewname;
    List<com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();

    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__outler__name);
        shared_common_pref = new Shared_Common_Pref(this);
        recyclerView = findViewById(R.id.outletrecyclerview);
        textViewname = findViewById(R.id.textViewname);
        common_class = new Common_Class(this);
        gson = new Gson();
        //GetAllDetails();
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);
        userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
        }.getType();
        String OrdersTable = shared_common_pref.getvalue(Constants.Retailer_OutletList);

        if (!OrdersTable.equals(""))
            Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
            recyclerView.setAdapter(new Outlet_Info_Adapter(Retailer_Modal_List, R.layout.outlet_info_recyclerview,this, "Reports",new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
                Shared_Common_Pref.OutletCode = Retailer_Modal_List.get(position).getId();
                Shared_Common_Pref.OutletName = Retailer_Modal_List.get(position).getName();
                Intent intent = new Intent(getApplicationContext(), Outlet_Report_View.class);
                startActivity(intent);

            }
        }));


    }

}