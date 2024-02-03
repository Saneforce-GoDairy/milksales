package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_ASSET_REPORT;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_QUALITY_REPORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcAssetReport;
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAssetReportBinding;
import com.saneforce.godairy.databinding.ModelAssetReportBinding;
import com.saneforce.godairy.databinding.ModelQualityReportBinding;
import com.saneforce.godairy.procurement.adapter.AssetReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetReportActivity extends AppCompatActivity {
    private ActivityAssetReportBinding binding;
    private final Context context = this;
    private List<ProcAssetReport> assetReportList;
    private AssetReportAdapter assetReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assetReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAssetReport(PROCUREMENT_GET_ASSET_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String assetList = "";
                    try {
                        assetList = response.body().string();
                        if (assetList.isEmpty()){
                            showError();
                            return;
                        }

                       JSONObject jsonObject = new JSONObject(assetList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords){
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcAssetReport assetReport = new ProcAssetReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                assetReport.setCompany(object.getString("company"));
                                assetReport.setPlant(object.getString("plant"));
                                assetReport.setAsset_type(object.getString("asset_type"));
                                assetReport.setComments(object.getString("comments"));
                                assetReport.setCreated_dt(object.getString("created_dt"));
                                assetReportList.add(assetReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            assetReportAdapter = new AssetReportAdapter(context, assetReportList);
                            binding.recyclerView.setAdapter(assetReportAdapter);
                            assetReportAdapter.notifyDataSetChanged();
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