package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_FARMER_CREATION_REPORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcFarmerCreaReport;
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityFarmerCreationReportBinding;
import com.saneforce.godairy.databinding.ModelFarmerCreaReportBinding;
import com.saneforce.godairy.databinding.ModelQualityReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import com.saneforce.godairy.procurement.adapter.FarmerCreaReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerCreationReportActivity extends AppCompatActivity {
    private ActivityFarmerCreationReportBinding binding;
    private final Context context = this;
    private List<ProcFarmerCreaReport> farmerCreaReportList;
    private FarmerCreaReportAdapter farmerCreaReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerCreationReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        farmerCreaReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getFarmerCreationReport(PROCUREMENT_GET_FARMER_CREATION_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String farmerCreationList;
                    try {
                        farmerCreationList = response.body().string();

                        if (farmerCreationList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(farmerCreationList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcFarmerCreaReport farmerCreaReport = new ProcFarmerCreaReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                farmerCreaReport.setCenter(object.getString("center_name"));
                                farmerCreaReport.setFarmer_category(object.getString("farmer_gategory"));
                                farmerCreaReport.setFarmer_name(object.getString("farmer_name"));
                                farmerCreaReport.setFarmer_img(object.getString("farmer_image"));
                                farmerCreaReport.setAddress(object.getString("farmer_address"));
                                farmerCreaReport.setPhone_number(object.getString("phone_number"));
                                farmerCreaReport.setPin_code(object.getString("pin_code"));
                                farmerCreaReport.setNo_of_ani_cow(object.getString("cow_total"));
                                farmerCreaReport.setNo_of_ani_buffalo(object.getString("buffalo_total"));
                                farmerCreaReport.setMilk_avail_lttr_cow(object.getString("cow_milk_availability_ltrs"));
                                farmerCreaReport.setMilk_avail_lttr_buffalo(object.getString("buffalo_milk_availability_ltrs"));
                                farmerCreaReport.setMilk_supply_company(object.getString("milk_supply_company"));
                                farmerCreaReport.setInterested_for_supply(object.getString("interested_supply"));
                                farmerCreaReport.setCreated_dt(object.getString("created_dt"));
                                farmerCreaReportList.add(farmerCreaReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            farmerCreaReportAdapter = new FarmerCreaReportAdapter(context, farmerCreaReportList);
                            binding.recyclerView.setAdapter(farmerCreaReportAdapter);
                            farmerCreaReportAdapter.notifyDataSetChanged();
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