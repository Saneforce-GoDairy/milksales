package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_MAINTENANCE_REPORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcMaintenanceReport;
import com.saneforce.godairy.databinding.ActivityMaintenanceReportBinding;
import com.saneforce.godairy.procurement.adapter.MaintenanceReportAdapter;

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

public class MaintenanceReportActivity extends AppCompatActivity {
    private ActivityMaintenanceReportBinding binding;
    private List<ProcMaintenanceReport> maintenanceReportList;
    private MaintenanceReportAdapter maintenanceReportAdapter;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaintenanceReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        maintenanceReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getMaintenanceReport(PROCUREMENT_GET_MAINTENANCE_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String maintenanceList;
                    try {
                        maintenanceList = response.body().string();

                        if (maintenanceList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(maintenanceList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcMaintenanceReport maintenanceReport = new ProcMaintenanceReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                maintenanceReport.setCompany(object.getString("company"));
                                maintenanceReport.setPlant(object.getString("plant"));
                                maintenanceReport.setNo_of_equipment(object.getString("equipment"));
                                maintenanceReport.setRepair_type(object.getString("repair_type"));
                                maintenanceReport.setRepair_img(object.getString("repair_type_img"));
                                maintenanceReport.setCreated_dt(object.getString("created_dt"));

                                maintenanceReportList.add(maintenanceReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            maintenanceReportAdapter = new MaintenanceReportAdapter(context, maintenanceReportList);
                            binding.recyclerView.setAdapter(maintenanceReportAdapter);
                            maintenanceReportAdapter.notifyDataSetChanged();
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
                showError();
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