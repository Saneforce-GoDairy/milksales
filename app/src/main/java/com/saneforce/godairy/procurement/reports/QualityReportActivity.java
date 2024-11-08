package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_QUALITY_REPORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.databinding.ActivityQualityReportBinding;
import com.saneforce.godairy.procurement.adapter.QualityReportAdapter;

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

public class QualityReportActivity extends AppCompatActivity {
    private ActivityQualityReportBinding binding;
    private final Context context = this;
    private List<ProcQualityReport> qualityReportsList;
    private QualityReportAdapter qualityReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQualityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        qualityReportsList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getQualityReport(PROCUREMENT_GET_QUALITY_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String qualityList;
                    try {
                        qualityList = response.body().string();

                        if (qualityList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(qualityList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcQualityReport qualityReport = new ProcQualityReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                qualityReport.setCompany(object.getString("company"));
                                qualityReport.setPlant(object.getString("plant"));
                                qualityReport.setMass_balance(object.getString("mass_balance"));
                                qualityReport.setMilk_collection(object.getString("milk_collection"));
                                qualityReport.setFat_image(object.getString("fat_image"));
                                qualityReport.setSnf_image(object.getString("snf_image"));
                                qualityReport.setMbrt(object.getString("mbrt"));
                                qualityReport.setRejection(object.getString("rejection"));
                                qualityReport.setWithhood_imag(object.getString("vehicle_with_hood_img"));
                                qualityReport.setWithout_hood_image(object.getString("vehicle_without_hood_img"));
                                qualityReport.setSpecial_cleaning(object.getString("spl_cleaning"));
                                qualityReport.setVehicles_rece_withhood(object.getString("vehicle_with_hood"));
                                qualityReport.setVehicles_rece_withouthood(object.getString("vehicle_without_hood"));
                                qualityReport.setRecords_chemicals(object.getString("chemicals"));
                                qualityReport.setRecords_stock(object.getString("stock"));
                                qualityReport.setRecord_milk(object.getString("milk"));
                                qualityReport.setAwareness_prog(object.getString("awareness_program"));
                                qualityReport.setCleaning_eff(object.getString("cleaning_efficiency"));
                                qualityReport.setSamp_calibra_no_fat(object.getString("no_of_fat"));
                                qualityReport.setSamp_calibra_no_snf(object.getString("no_of_snf"));
                                qualityReport.setSamp_calibra_no_weight(object.getString("no_of_weight"));
                                qualityReport.setCreated_dt(object.getString("created_dt"));
                                qualityReportsList.add(qualityReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            qualityReportAdapter = new QualityReportAdapter(context, qualityReportsList);
                            binding.recyclerView.setAdapter(qualityReportAdapter);
                            qualityReportAdapter.notifyDataSetChanged();
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