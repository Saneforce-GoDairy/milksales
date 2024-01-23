package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_AIT_REPORT;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcAITReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAitreportBinding;
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
                    binding.shimmerLayout2.setVisibility(GONE);
                    String  aitList;

                    try {
                        aitList = response.body().string();

                        JSONArray jsonArray = new JSONArray(aitList);

                        for (int i = 0; i<jsonArray.length(); i++) {
                            ProcAITReport procAITReport = new ProcAITReport();
                            JSONObject object = jsonArray.getJSONObject(i);
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
                    } catch (IOException | JSONException e) {
                      //  throw new RuntimeException(e);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Load list error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class AITReportListAdapter extends RecyclerView.Adapter<AITReportListAdapter.ViewHolder>{
        private final List<ProcAITReport> aitReportList;
        private final Context context;

        public AITReportListAdapter(Context context, List<ProcAITReport> aitReportList) {
            this.aitReportList = aitReportList;
            this.context = context;
        }

        @NonNull
        @Override
        public AITReportListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_ait_report, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AITReportListAdapter.ViewHolder holder, int position) {
            holder.txtCompanyName.setText(aitReportList.get(position).getCompany());
            holder.txtPlant.setText(aitReportList.get(position).getPlant());
            holder.txtFarmerName.setText(aitReportList.get(position).getFarmer_name());
            holder.txtCenterName.setText(aitReportList.get(position).getCenter_name());
            holder.txtBreedName.setText(aitReportList.get(position).getBreed_name());
            String upToNCharacters = aitReportList.get(position).getDate().substring(0, Math.min(aitReportList.get(position).getDate().length(), 10));
            holder.txtCreatedDt.setText(upToNCharacters);
            holder.txtServiceNoOfAi.setText(aitReportList.get(position).getNo_of_ai());
            holder.txtServiceBullNos.setText(aitReportList.get(position).getBull_nos());
            holder.txtPdVerification.setText(aitReportList.get(position).getPd_verification());
            holder.txtCalfBirthVerification.setText(aitReportList.get(position).getCalfbirth_verification());
            holder.txtMineralMixtureSale.setText(aitReportList.get(position).getMineral_mixture_sale());
            holder.txtSeedSales.setText(aitReportList.get(position).getSeed_sales());

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
                    .load(BASE_URL_PROCUREMENT_IMG + aitReportList.get(position).getBreed_image())
                    .into(holder.imgBreed);

            holder.txtViewDetails.setOnClickListener(v -> {
                holder.viewSecondLayout.setVisibility(View.VISIBLE);
                holder.txtViewDetails.setVisibility(GONE);
                holder.txtViewLess.setVisibility(View.VISIBLE);
            });

            holder.txtViewLess.setOnClickListener(v -> {
                holder.viewSecondLayout.setVisibility(View.GONE);
                holder.txtViewDetails.setVisibility(View.VISIBLE);
                holder.txtViewLess.setVisibility(View.GONE);
            });

            holder.imgBreed.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + aitReportList.get(position).getBreed_image();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Breed image");
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return aitReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtCompanyName,txtPlant, txtFarmerName, txtCenterName, txtBreedName;
            TextView txtViewDetails, txtViewLess, txtCreatedDt, txtMineralMixtureSale, txtSeedSales;
            TextView txtServiceNoOfAi, txtServiceBullNos, txtPdVerification, txtCalfBirthVerification;
            LinearLayout viewSecondLayout;
            ImageView imgBreed;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtCompanyName = itemView.findViewById(R.id.txt_company_name);
                txtPlant = itemView.findViewById(R.id.txt_plant);
                txtCenterName = itemView.findViewById(R.id.txt_center_name);
                txtFarmerName = itemView.findViewById(R.id.txt_farmer_name);
                txtBreedName = itemView.findViewById(R.id.txt_breed_name);
                txtViewDetails = itemView.findViewById(R.id.txt_view_details);
                txtViewLess = itemView.findViewById(R.id.txt_view_less);
                viewSecondLayout = itemView.findViewById(R.id.second_cn);
                txtCreatedDt = itemView.findViewById(R.id.txt_date);
                imgBreed = itemView.findViewById(R.id.breed_img);
                txtServiceNoOfAi = itemView.findViewById(R.id.txt_no_of_ai);
                txtServiceBullNos = itemView.findViewById(R.id.txt_bull_nos);
                txtPdVerification = itemView.findViewById(R.id.txt_pd_verification);
                txtCalfBirthVerification = itemView.findViewById(R.id.txt_calfbirth_verification);
                txtMineralMixtureSale = itemView.findViewById(R.id.txt_mineral_mix_sale);
                txtSeedSales = itemView.findViewById(R.id.txt_seed_sales);
            }
        }
    }
}