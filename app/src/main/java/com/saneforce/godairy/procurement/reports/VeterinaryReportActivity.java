package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_VETERINARY_REPORT;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcVeterinaryReport;
import com.saneforce.godairy.databinding.ActivityVeterinaryReportBinding;
import com.saneforce.godairy.databinding.ModelVeterinaryReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import com.saneforce.godairy.procurement.adapter.VeterinaryReportAdapter;

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

public class VeterinaryReportActivity extends AppCompatActivity {
    private ActivityVeterinaryReportBinding binding;
    private final Context context = this;
    private List<ProcVeterinaryReport> veterinaryReportList;
    private VeterinaryReportAdapter veterinaryReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVeterinaryReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        veterinaryReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getVeterinaryReport(PROCUREMENT_GET_VETERINARY_REPORT);

        call.enqueue(new Callback<>() {
            @Override
            @SuppressLint("NotifyDataSetChanged")
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(GONE);
                    String  veterinaryList;
                    try {
                        veterinaryList = response.body().string();

                        if (veterinaryList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(veterinaryList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcVeterinaryReport veterinaryReport = new ProcVeterinaryReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                veterinaryReport.setCompany(object.getString("company"));
                                veterinaryReport.setPlant(object.getString("plant"));
                                veterinaryReport.setCenter_name(object.getString("center_name"));
                                veterinaryReport.setFarmer_code(object.getString("farmer_name"));
                                veterinaryReport.setService_type(object.getString("service_type"));
                                veterinaryReport.setService_type_img(object.getString("service_type_image"));
                                veterinaryReport.setProduct_type(object.getString("product_type"));
                                veterinaryReport.setSeed_sale(object.getString("seed_sale"));
                                veterinaryReport.setMineral_mixture(object.getString("mineral_mixture"));
                                veterinaryReport.setFodder_setts_sales(object.getString("fodder_setts_sale_kg"));
                                veterinaryReport.setCattle_feed_order(object.getString("cattle_feed_order_kg"));
                                veterinaryReport.setTeat_dip(object.getString("teat_dip_cup"));
                                veterinaryReport.setEvm(object.getString("evm_treatment"));
                                veterinaryReport.setEvm_img(object.getString("evm_image"));
                                veterinaryReport.setCase_type(object.getString("case_type"));
                                veterinaryReport.setIdent_farmers_count(object.getString("identified_farmer_count"));
                                veterinaryReport.setEnrolled_farmers(object.getString("farmer_enrolled"));
                                veterinaryReport.setInducted_farmers(object.getString("farmer_inducted"));
                                veterinaryReport.setCreated_date(object.getString("created_dt"));
                                veterinaryReportList.add(veterinaryReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            veterinaryReportAdapter = new VeterinaryReportAdapter(context, veterinaryReportList);
                            binding.recyclerView.setAdapter(veterinaryReportAdapter);
                            veterinaryReportAdapter.notifyDataSetChanged();
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