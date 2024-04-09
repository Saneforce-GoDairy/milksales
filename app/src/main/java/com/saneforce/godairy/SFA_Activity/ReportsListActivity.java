package com.saneforce.godairy.SFA_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

//import com.saneforce.godairy.Activity_Hap.DayReportActivity;
import com.saneforce.godairy.Activity_Hap.Day_Report_Activity;
import com.saneforce.godairy.Activity_Hap.Monthly_Report_Activity;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.ReportsLIstAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_reports_list);
            shared_common_pref = new Shared_Common_Pref(this);
            recyclerView = findViewById(R.id.rvReportsList);
            common_class = new Common_Class(this);

            ImageView backView = findViewById(R.id.imag_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            JSONObject obj1 = new JSONObject();
            obj1.put("name", "Outlet Reports");
            obj1.put("des", "app");


            JSONObject obj2 = new JSONObject();
            obj2.put("name", "Primary Order");
            obj2.put("des", "web");

            JSONObject obj3 = new JSONObject();
            obj3.put("name", "Sales Summary");
            obj3.put("des", "web");
            JSONObject obj4 = new JSONObject();
            obj4.put("name", "New Outlet Follow up");
            obj4.put("des", "web");
            JSONObject obj5 = new JSONObject();
            obj5.put("name", "QPS Follow up");
            obj5.put("des", "web");
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(obj1);
            jsonArray.put(obj2);
            jsonArray.put(obj3);
            jsonArray.put(obj4);
            jsonArray.put(obj5);
            if(shared_common_pref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.CHECKIN_TYPE)){
                JSONObject obj6 = new JSONObject();
                obj6.put("name", "DAY REPORTS");
                obj6.put("des", "web");
                jsonArray.put(obj6);
                JSONObject obj7 = new JSONObject();
                obj7.put("name", "MONTHLY REPORTS");
                obj7.put("des", "web");
                jsonArray.put(obj7);
            }


            recyclerView.setAdapter(new ReportsLIstAdapter(this, jsonArray, R.layout.adapter_report_list, new AdapterOnClick() {
                @Override
                public void onIntentClick(JSONObject obj, int position) {
                    try {
                        if (obj.getString("name").equalsIgnoreCase("Outlet Reports")) {
                            Intent intent = new Intent(getApplicationContext(), Reports_Outler_Name.class);
                            startActivity(intent);
                        }else if (obj.getString("name").equalsIgnoreCase("Primary Order")){
                            Intent intent = new Intent(ReportsListActivity.this, PrimaryOrderReportActivity.class);
                            startActivity(intent);
                        } else if (obj.getString("name").equalsIgnoreCase("Day Reports")) {
                            Intent intent = new Intent(ReportsListActivity.this, Day_Report_Activity.class);
                            startActivity(intent);
                        } else if (obj.getString("name").equalsIgnoreCase("MONTHLY REPORTS")) {
                            Intent intent = new Intent(ReportsListActivity.this, Monthly_Report_Activity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ReportsListActivity.this, obj.getString("name") + ": Not assigned", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ReportsListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }));
        } catch (Exception e) {

        }

    }

}