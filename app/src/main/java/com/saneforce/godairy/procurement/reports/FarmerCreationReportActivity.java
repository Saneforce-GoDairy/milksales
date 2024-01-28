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
                    binding.shimmerLayout2.setVisibility(GONE);
                    String farmerCreationList;
                    try {
                        farmerCreationList = response.body().string();
                        JSONArray jsonArray = new JSONArray(farmerCreationList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProcFarmerCreaReport farmerCreaReport = new ProcFarmerCreaReport();
                            JSONObject object = jsonArray.getJSONObject(i);
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
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        Toast.makeText(context, "List load error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class FarmerCreaReportAdapter extends RecyclerView.Adapter<FarmerCreaReportAdapter.ViewHolder>{
        private final List<ProcFarmerCreaReport> farmerCreaReportList;
        private  final Context context;

        public FarmerCreaReportAdapter(Context context , List<ProcFarmerCreaReport> farmerCreaReportList) {
            this.farmerCreaReportList = farmerCreaReportList;
            this.context = context;
        }

        @NonNull
        @Override
        public FarmerCreaReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ModelFarmerCreaReportBinding binding = ModelFarmerCreaReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FarmerCreaReportAdapter.ViewHolder holder, int position) {
            holder.binding.txtVillageCenter.setText(farmerCreaReportList.get(position).getCenter());
            holder.binding.txtFarmerCategory.setText(farmerCreaReportList.get(position).getFarmer_category());
            holder.binding.txtFarmerName.setText(farmerCreaReportList.get(position).getFarmer_name());
            holder.binding.txtAddress.setText(farmerCreaReportList.get(position).getAddress());
            holder.binding.txtPhoneNo.setText(farmerCreaReportList.get(position).getPhone_number());
            holder.binding.txtPinCode.setText(farmerCreaReportList.get(position).getPin_code());
            holder.binding.txtNoOfAniCow.setText(farmerCreaReportList.get(position).getNo_of_ani_cow());
            holder.binding.txtNoOfAniBuffalo.setText(farmerCreaReportList.get(position).getNo_of_ani_buffalo());
            holder.binding.txtMilkAvailCow.setText(farmerCreaReportList.get(position).getMilk_avail_lttr_cow());
            holder.binding.txtMilkAvailBaffalo.setText(farmerCreaReportList.get(position).getMilk_avail_lttr_buffalo());
            holder.binding.txtMilkSupplyCompany.setText(farmerCreaReportList.get(position).getMilk_supply_company());
            holder.binding.txtInterestedForSupply.setText(farmerCreaReportList.get(position).getInterested_for_supply());
            String upToNCharacters = farmerCreaReportList.get(position).getCreated_dt().substring(0, Math.min(farmerCreaReportList.get(position).getCreated_dt().length(), 10));
            holder.binding.txtDate.setText(upToNCharacters);

              /*
               below logic used for access procurement images folder ( its wrks dev and live )
             */
            URL url = null;
            try {
                url = new URL(BASE_URL);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Procurement_images/";
            Log.e("proc_img_url", BASE_URL_PROCUREMENT_IMG);

            Glide.with(context)
                    .load(BASE_URL_PROCUREMENT_IMG + farmerCreaReportList.get(position).getFarmer_img())
                    .into(holder.binding.imgFarmer);

            holder.binding.txtViewDetails.setOnClickListener(v -> {
                holder.binding.secondCn.setVisibility(View.VISIBLE);
                holder.binding.txtViewDetails.setVisibility(GONE);
                holder.binding.txtViewLess.setVisibility(View.VISIBLE);
            });

            holder.binding.txtViewLess.setOnClickListener(v -> {
                holder.binding.secondCn.setVisibility(View.GONE);
                holder.binding.txtViewDetails.setVisibility(View.VISIBLE);
                holder.binding.txtViewLess.setVisibility(View.GONE);
            });

            holder.binding.imgFarmer.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + farmerCreaReportList.get(position).getFarmer_img();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Farmer image");
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return farmerCreaReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ModelFarmerCreaReportBinding binding;

            public ViewHolder(ModelFarmerCreaReportBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}