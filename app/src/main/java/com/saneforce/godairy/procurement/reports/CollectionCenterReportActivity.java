package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_COLLECTION_CENTER_REPORT;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_QUALITY_REPORT;

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
import com.saneforce.godairy.Model_Class.ProcCollectionCeReport;
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityCollectionCenterReportBinding;
import com.saneforce.godairy.databinding.ModelCollCenterReportBinding;
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

public class CollectionCenterReportActivity extends AppCompatActivity {
    private ActivityCollectionCenterReportBinding binding;
    private final Context context = this;
    private List<ProcCollectionCeReport> collectionCeReportList;
    private CollectionCeReportAdapter collectionCeReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionCenterReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        collectionCeReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getCollectionCenterReport(PROCUREMENT_GET_COLLECTION_CENTER_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout2.setVisibility(GONE);
                    String collectionCenterList;
                    try {
                        collectionCenterList = response.body().string();
                        JSONArray jsonArray = new JSONArray(collectionCenterList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProcCollectionCeReport collectionCeReport = new ProcCollectionCeReport();
                            JSONObject object = jsonArray.getJSONObject(i);
                            collectionCeReport.setCompany(object.getString("company"));
                            collectionCeReport.setPlant(object.getString("plant"));
                            collectionCeReport.setCreated_dt(object.getString("created_dt"));
                            collectionCeReport.setCollection_ce_image(object.getString("coll_center_image"));
                            collectionCeReport.setSap_center_code(object.getString("sap_center_code"));
                            collectionCeReport.setSap_center_name(object.getString("sap_center_name"));
                            collectionCeReport.setCenter_addr(object.getString("center_address"));
                            collectionCeReport.setLocatlis_lpd(object.getString("lactlis_potential_lpd"));
                            collectionCeReport.setFarmers_enrolled(object.getString("no_enrolled_farmers"));
                            collectionCeReport.setCompetitor_lpd(object.getString("competitor1"));
                            collectionCeReport.setCompetitor_lpd2(object.getString("competitor1_txt"));
                            collectionCeReportList.add(collectionCeReport);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.recyclerView.setLayoutManager(linearLayoutManager);
                        binding.recyclerView.setHasFixedSize(true);
                        binding.recyclerView.setItemViewCacheSize(20);
                        collectionCeReportAdapter = new CollectionCeReportAdapter(context, collectionCeReportList);
                        binding.recyclerView.setAdapter(collectionCeReportAdapter);
                        collectionCeReportAdapter.notifyDataSetChanged();
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

    public static class CollectionCeReportAdapter extends RecyclerView.Adapter<CollectionCeReportAdapter.ViewHolder>{
        private final List<ProcCollectionCeReport> collectionCeReportList;
        private  final Context context;

        public CollectionCeReportAdapter(Context context , List<ProcCollectionCeReport> collectionCeReportList) {
            this.collectionCeReportList = collectionCeReportList;
            this.context = context;
        }


        @NonNull
        @Override
        public CollectionCeReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ModelCollCenterReportBinding binding = ModelCollCenterReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CollectionCeReportAdapter.ViewHolder holder, int position) {
            holder.binding.txtCompanyName.setText(collectionCeReportList.get(position).getCompany());
            holder.binding.txtPlant.setText(collectionCeReportList.get(position).getPlant());
            String upToNCharacters = collectionCeReportList.get(position).getCreated_dt().substring(0, Math.min(collectionCeReportList.get(position).getCreated_dt().length(), 10));
            holder.binding.txtDate.setText(upToNCharacters);
            holder.binding.txtSapCenterCode.setText(collectionCeReportList.get(position).getSap_center_code());
            holder.binding.txtSapCenterName.setText(collectionCeReportList.get(position).getSap_center_name());
            holder.binding.txtCenterAddrs.setText(collectionCeReportList.get(position).getCenter_addr());
            holder.binding.txtLactalisLpd.setText(collectionCeReportList.get(position).getLocatlis_lpd());
            holder.binding.txtNoOfFarmersEnroled.setText(collectionCeReportList.get(position).getFarmers_enrolled());
            holder.binding.txtCompetitorLpd1.setText(collectionCeReportList.get(position).getCompetitor_lpd());
            holder.binding.txtCompetitorLpd2.setText(collectionCeReportList.get(position).getCompetitor_lpd2());

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
                    .load(BASE_URL_PROCUREMENT_IMG + collectionCeReportList.get(position).getCollection_ce_image())
                    .into(holder.binding.imgSapCenter);

            holder.binding.imgSapCenter.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + collectionCeReportList.get(position).getCollection_ce_image();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Collection center");
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });

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
        }

        @Override
        public int getItemCount() {
            return collectionCeReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ModelCollCenterReportBinding binding;

            public ViewHolder(ModelCollCenterReportBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}