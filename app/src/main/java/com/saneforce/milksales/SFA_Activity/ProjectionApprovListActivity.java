package com.saneforce.milksales.SFA_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.ProjectionApprovListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProjectionApprovListActivity extends AppCompatActivity {
    RecyclerView rvApproval;
    TextView tvStatus, tvHeadText;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_projection_approval_list);

            common_class=new Common_Class(this);
            rvApproval = findViewById(R.id.rvOutletApprov);
            tvStatus = findViewById(R.id.tvOutletSta);
            tvHeadText = findViewById(R.id.tvHeadText);

            tvHeadText.setText("Product Projection Approval");

            JSONObject student1 = new JSONObject();
            student1.put("name", "Abinaya Store");
            student1.put("qty", "600");
            student1.put("date", "2022-02-16");


            JSONObject student2 = new JSONObject();
            student2.put("name", "Mahalakshmi Store");
            student2.put("qty", "1000");
            student2.put("date", "2022-02-17");

            JSONObject student3 = new JSONObject();
            student3.put("name", "Mahalakshmi Store");
            student3.put("qty", "700");
            student3.put("date", "2022-02-17");


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(student1);
            jsonArray.put(student2);
            jsonArray.put(student3);

            ProjectionApprovListAdapter adapter = new ProjectionApprovListAdapter(this, jsonArray, R.layout.adapter_projection_approval);
            rvApproval.setAdapter(adapter);

            tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overridePendingTransition(R.anim.in, R.anim.out);
                    startActivity(new Intent(getApplicationContext(), Outlet_Status_Activity.class));
                }
            });

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


        } catch (Exception e) {
            Log.v("OutletApprovalActivity:", e.getMessage());
        }

    }
}
