package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.AdapterOutletsApprovalHistory;
import com.saneforce.godairy.SFA_Model_Class.ModelOutletsApprovalHistory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovalHistoryActivity extends AppCompatActivity {
    ImageView home;
    TextView outletsCount;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    ArrayList<ModelOutletsApprovalHistory> list = new ArrayList<>();
    AdapterOutletsApprovalHistory adapter;
    Context context = this;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_history);

        home = findViewById(R.id.toolbar_home);
        outletsCount = findViewById(R.id.count);
        recyclerView = findViewById(R.id.recyclerview__ApprovalHistoryActivity);
        progressBar = findViewById(R.id.progressbar_ApprovalHistoryActivity);

        common_class = new Common_Class(context);
        home.setOnClickListener(v -> common_class.gotoHomeScreen(context, v));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(this::StartLoading);
    }

    private void StartLoading() {
        list.clear();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getOutletsApprovalHistory("get_outlets_approval_history");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            JSONArray array = object.getJSONArray("response");
                            for (int i = 0; i < array.length(); i++) {
                                String Status = array.getJSONObject(i).getString("Status");
                                String Name = array.getJSONObject(i).getString("Name");
                                String Code = array.getJSONObject(i).getString("Code");
                                String Mobile = array.getJSONObject(i).getString("Mobile");
                                String Address = array.getJSONObject(i).getString("Address");
                                String ApprovedBy = array.getJSONObject(i).getString("ApprovedBy");
                                String ModifiedOn = array.getJSONObject(i).getString("ModifiedOn");
                                String Remarks = array.getJSONObject(i).getString("Remarks");
                                list.add(new ModelOutletsApprovalHistory(Status, Name, Code, Mobile, Address, ApprovedBy, ModifiedOn, Remarks));
                            }
                            String c = "Results Count\n" + list.size();
                            runOnUiThread(() -> {
                                outletsCount.setText(c);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                adapter = new AdapterOutletsApprovalHistory(list, context);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            });
                        } else {
                            Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error: Response Failed", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}