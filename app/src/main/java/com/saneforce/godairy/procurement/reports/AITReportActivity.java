package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_AIT_REPORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcAITReport;
import com.saneforce.godairy.databinding.ActivityAitreportBinding;
import com.saneforce.godairy.procurement.adapter.AITReportListAdapter;

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

public class AITReportActivity extends AppCompatActivity {
    private ActivityAitreportBinding binding;
    private final Context context = this;
    private List<ProcAITReport> procAITReportList;
    private AITReportListAdapter aitReportListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAitreportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        procAITReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAITReport(PROCUREMENT_GET_AIT_REPORT);

        call.enqueue(new Callback<>() {
            @Override
            @SuppressLint("NotifyDataSetChanged")
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(GONE);
                    String  aitList;
                    try {
                        aitList = response.body().string();

                        if (aitList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(aitList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcAITReport procAITReport = new ProcAITReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                procAITReport.setCompany(object.getString("company"));
                                procAITReport.setPlant(object.getString("plant"));
                                procAITReport.setFarmer_name(object.getString("farmer_name_code"));
                                procAITReport.setCenter_name(object.getString("center_name"));
                                procAITReport.setBreed_name(object.getString("breed_name"));
                                procAITReport.setDate(object.getString("created_dt"));
                                procAITReport.setBreed_image(object.getString("breed_image"));
                                procAITReport.setNo_of_ai(object.getString("service_type_ai"));
                                procAITReport.setBull_nos(object.getString("service_type2"));
                                procAITReport.setPd_verification(object.getString("pd_verification"));
                                procAITReport.setCalfbirth_verification(object.getString("calfbirth_verification"));
                                procAITReport.setMineral_mixture_sale(object.getString("mineral_mixture_kg"));
                                procAITReport.setSeed_sales(object.getString("seed_sales"));
                                procAITReportList.add(procAITReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            aitReportListAdapter = new AITReportListAdapter(context, procAITReportList);
                            binding.recyclerView.setAdapter(aitReportListAdapter);
                            aitReportListAdapter.notifyDataSetChanged();
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