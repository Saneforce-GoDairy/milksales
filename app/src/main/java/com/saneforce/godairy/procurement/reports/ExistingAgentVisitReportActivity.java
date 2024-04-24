package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_EXISTING_AGENT_REPORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcExistingAgentReport;
import com.saneforce.godairy.databinding.ActivityExistingAgentVisitReportBinding;
import com.saneforce.godairy.procurement.adapter.ExistingAgentReportAdapter;

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

public class ExistingAgentVisitReportActivity extends AppCompatActivity {
    private ActivityExistingAgentVisitReportBinding binding;
    private final Context context = this;
    private List<ProcExistingAgentReport> existingAgentReportList;
    private ExistingAgentReportAdapter existingAgentReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExistingAgentVisitReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        existingAgentReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getExistingAgentReport(PROCUREMENT_GET_EXISTING_AGENT_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String existingAgentList;
                    try {
                        existingAgentList = response.body().string();

                        if (existingAgentList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(existingAgentList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcExistingAgentReport existingAgentReport = new ProcExistingAgentReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                existingAgentReport.setCompany(object.getString("company"));
                                existingAgentReport.setAgent(object.getString("visit_agent"));
                                existingAgentReport.setTotal_milk_availability(object.getString("total_milk_available"));
                                existingAgentReport.setOur_company_ltrs(object.getString("our_company_ltrs"));
                                existingAgentReport.setCompetitor_rate(object.getString("competitor_rate"));
                                existingAgentReport.setOur_company_rate(object.getString("our_company_rate"));
                                existingAgentReport.setDemand(object.getString("demand"));
                                existingAgentReport.setSupply_start_dt(object.getString("supply_start_dt"));
                                existingAgentReport.setCreated_dt(object.getString("created_dt"));

                                existingAgentReportList.add(existingAgentReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            existingAgentReportAdapter = new ExistingAgentReportAdapter(context, existingAgentReportList);
                            binding.recyclerView.setAdapter(existingAgentReportAdapter);
                            existingAgentReportAdapter.notifyDataSetChanged();
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