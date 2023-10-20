package com.milksales.godairy.SFA_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Activity_Hap.SFA_Activity;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.UpdateResponseUI;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.DashboardInfoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class DashboardInfoActivity extends AppCompatActivity implements View.OnClickListener,
        UpdateResponseUI {

    RecyclerView rvDashboard;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    TextView tvDistributor, tvHeaderName;
    private String sts;
    ImageView ivToolbarHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_info);
        init();
        tvDistributor.setText(shared_common_pref.getvalue(Constants.Distributor_name));
        if (sts == null)
            tvHeaderName.setText(getIntent().getStringExtra("type") + " Info");
        else
            tvHeaderName.setText("No order Info");


        common_class.getDb_310Data(Constants.SALES_SUMMARY, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_home:
                common_class.CommonIntentwithoutFinish(SFA_Activity.class);
                break;
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {

                JSONObject jsonObject = new JSONObject(apiDataResponse);
                Log.v("SALES_SUMMA:", apiDataResponse);

                switch (key) {
                    case Constants.SALES_SUMMARY:
                        if (jsonObject.getBoolean("success")) {
                            setAdapter(apiDataResponse);
                        }
                        break;


                }
            }

        } catch (Exception e) {

        }

    }

    void init() {
        tvHeaderName = findViewById(R.id.headtext);
        rvDashboard = findViewById(R.id.rvDashboard);
        tvDistributor = findViewById(R.id.tvDistributer);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        sts = getIntent().getStringExtra("status");
        ivToolbarHome = findViewById(R.id.toolbar_home);

        ivToolbarHome.setOnClickListener(this);
    }


    void setAdapter(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getBoolean("success")) {
                JSONArray jsonArr = jsonObject.getJSONArray("data");

                Log.e("status", "SQL: " + jsonObject.getString("sql"));

                tvDistributor.setText(jsonArr.getJSONObject(0).getString("Sf_Name"));
                rvDashboard.setAdapter(new DashboardInfoAdapter(this, jsonArr, R.layout.db_type_info_adapter_layout));
            }

        } catch (Exception e) {
            Log.e("HistoryorderFrag:", e.getMessage());

        }
    }


}