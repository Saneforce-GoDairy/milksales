package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_VETERINARY_REPORT;

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
import com.saneforce.godairy.Model_Class.ProcAITReport;
import com.saneforce.godairy.Model_Class.ProcVeterinaryReport;
import com.saneforce.godairy.databinding.ActivityVeterinaryReportBinding;
import com.saneforce.godairy.databinding.ModelVeterinaryReportBinding;
import com.saneforce.godairy.databinding.ShiftListItemBinding;
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
        Call<ResponseBody> call = apiInterface.getAITReport(PROCUREMENT_GET_VETERINARY_REPORT);

        call.enqueue(new Callback<>() {
            @Override
            @SuppressLint("NotifyDataSetChanged")
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout2.setVisibility(GONE);
                    String  veterinaryList;

                    try {
                        veterinaryList = response.body().string();

                        JSONArray jsonArray = new JSONArray(veterinaryList);

                        for (int i = 0; i<jsonArray.length(); i++) {
                            ProcVeterinaryReport veterinaryReport = new ProcVeterinaryReport();
                            JSONObject object = jsonArray.getJSONObject(i);
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
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    public static class VeterinaryReportAdapter extends RecyclerView.Adapter<VeterinaryReportAdapter.ViewHolder>{
        private final List<ProcVeterinaryReport> veterinaryReportList;
        private final Context context;

        public VeterinaryReportAdapter(Context context, List<ProcVeterinaryReport> veterinaryReportList) {
            this.veterinaryReportList = veterinaryReportList;
            this.context = context;
        }

        @NonNull
        @Override
        public VeterinaryReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ModelVeterinaryReportBinding binding = ModelVeterinaryReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull VeterinaryReportAdapter.ViewHolder holder, int position) {
            holder.binding.txtCompanyName.setText(veterinaryReportList.get(position).getCompany());
            holder.binding.txtPlant.setText(veterinaryReportList.get(position).getPlant());
            holder.binding.txtCenterName.setText(veterinaryReportList.get(position).getCenter_name());
            holder.binding.txtFarmerName.setText(veterinaryReportList.get(position).getFarmer_code());
            holder.binding.txtServiceTypeName.setText(veterinaryReportList.get(position).getService_type());
            holder.binding.txtProductType.setText(veterinaryReportList.get(position).getProduct_type());
            holder.binding.txtSeedSale.setText(veterinaryReportList.get(position).getSeed_sale());
            holder.binding.txtMineralMixture.setText(veterinaryReportList.get(position).getMineral_mixture());
            holder.binding.txtFodderSettsSale.setText(veterinaryReportList.get(position).getFodder_setts_sales());
            holder.binding.txtCattleFeedOrder.setText(veterinaryReportList.get(position).getCattle_feed_order());
            holder.binding.txtTeatDip.setText(veterinaryReportList.get(position).getTeat_dip());
            holder.binding.txtEvm.setText(veterinaryReportList.get(position).getEvm());
            holder.binding.txtCaseTpe.setText(veterinaryReportList.get(position).getCase_type());
            holder.binding.txtIdentifiedFarmersCount.setText(veterinaryReportList.get(position).getIdent_farmers_count());
            holder.binding.txtEnrolledFarmers.setText(veterinaryReportList.get(position).getTeat_dip());
            holder.binding.txtFarmersInducted.setText(veterinaryReportList.get(position).getInducted_farmers());
            String upToNCharacters = veterinaryReportList.get(position).getCreated_date().substring(0, Math.min(veterinaryReportList.get(position).getCreated_date().length(), 10));
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
                    .load(BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getService_type_img())
                    .into(holder.binding.serviceTypeImg);

            Glide.with(context)
                    .load(BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getEvm_img())
                    .into(holder.binding.evmImg);

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

            holder.binding.serviceTypeImg.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getService_type_img();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Service type");
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });

            holder.binding.evmImg.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getEvm_img();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Emergency treatment/EVM");
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return veterinaryReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ModelVeterinaryReportBinding binding;

            public ViewHolder(ModelVeterinaryReportBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}