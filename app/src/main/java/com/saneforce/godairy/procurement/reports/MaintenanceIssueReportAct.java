package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_AIT_REPORT;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_MAINTENANCE_ISSUE_REPORT;

import android.content.Context;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcAITReport;
import com.saneforce.godairy.Model_Class.ProcMaintenIssue;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityMaintenanceIssueReportBinding;
import com.saneforce.godairy.procurement.adapter.AITReportListAdapter;
import com.saneforce.godairy.procurement.adapter.MainIssueReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintenanceIssueReportAct extends AppCompatActivity {
    private ActivityMaintenanceIssueReportBinding binding;
    private final Context context = this;
    List<ProcMaintenIssue> list;
    private MainIssueReportAdapter mainIssueReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaintenanceIssueReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getMaintenanceIssueReport(PROCUREMENT_GET_MAINTENANCE_ISSUE_REPORT);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(GONE);
                    String  maintenIssueList;
                    try {
                        maintenIssueList = response.body().string();

                        if (maintenIssueList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(maintenIssueList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcMaintenIssue procMaintenIssue = new ProcMaintenIssue();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                procMaintenIssue.setCompany(object.getString("company"));
                                procMaintenIssue.setPlant(object.getString("plant"));
                                procMaintenIssue.setEquipment(object.getString("equipment"));
                                procMaintenIssue.setRepair_type(object.getString("repair_type"));
                                procMaintenIssue.setRepair_type_img(object.getString("repair_type_img"));
                                list.add(procMaintenIssue);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            mainIssueReportAdapter = new MainIssueReportAdapter(context, list);
                            binding.recyclerView.setAdapter(mainIssueReportAdapter);
                            mainIssueReportAdapter.notifyDataSetChanged();
                            return;
                        }
                        binding.shimmerLayout.setVisibility(GONE);
                        binding.recyclerView.setVisibility(GONE);
                        binding.noRecords.setVisibility(View.VISIBLE);
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        showError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    private void showError() {
        binding.shimmerLayout.setVisibility(GONE);
        binding.recyclerView.setVisibility(GONE);
        binding.nullError.setVisibility(View.VISIBLE);
        binding.message.setText("Something went wrong!");
    }
}