package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_MAINTENANCE_REPORT;
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
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcMaintenanceReport;
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityMaintenanceReportBinding;
import com.saneforce.godairy.databinding.ModelMaintenanceReportBinding;
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

public class MaintenanceReportActivity extends AppCompatActivity {
    private ActivityMaintenanceReportBinding binding;
    private List<ProcMaintenanceReport> maintenanceReportList;
    private MaintenanceReportAdapter maintenanceReportAdapter;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaintenanceReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        maintenanceReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getMaintenanceReport(PROCUREMENT_GET_MAINTENANCE_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout2.setVisibility(GONE);
                    String maintenanceList;
                    try {
                        maintenanceList = response.body().string();
                        JSONArray jsonArray = new JSONArray(maintenanceList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProcMaintenanceReport maintenanceReport = new ProcMaintenanceReport();
                            JSONObject object = jsonArray.getJSONObject(i);
                            maintenanceReport.setCompany(object.getString("company"));
                            maintenanceReport.setPlant(object.getString("plant"));
                            maintenanceReport.setNo_of_equipment(object.getString("equipment"));
                            maintenanceReport.setRepair_type(object.getString("repair_type"));
                            maintenanceReport.setRepair_img(object.getString("repair_type_img"));
                            maintenanceReport.setCreated_dt(object.getString("created_dt"));

                            maintenanceReportList.add(maintenanceReport);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.recyclerView.setLayoutManager(linearLayoutManager);
                        binding.recyclerView.setHasFixedSize(true);
                        binding.recyclerView.setItemViewCacheSize(20);
                        maintenanceReportAdapter = new MaintenanceReportAdapter(context, maintenanceReportList);
                        binding.recyclerView.setAdapter(maintenanceReportAdapter);
                        maintenanceReportAdapter.notifyDataSetChanged();
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        Toast.makeText(context, "List load error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "List load error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class MaintenanceReportAdapter extends RecyclerView.Adapter<MaintenanceReportAdapter.ViewHolder>{
        private final List<ProcMaintenanceReport> maintenanceReportList;
        private  final Context context;

        public MaintenanceReportAdapter(Context context , List<ProcMaintenanceReport> maintenanceReportList) {
            this.maintenanceReportList = maintenanceReportList;
            this.context = context;
        }

        @NonNull
        @Override
        public MaintenanceReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ModelMaintenanceReportBinding binding = ModelMaintenanceReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MaintenanceReportAdapter.ViewHolder holder, int position) {
            holder.binding.txtCompanyName.setText(maintenanceReportList.get(position).getCompany());
            holder.binding.txtPlant.setText(maintenanceReportList.get(position).getPlant());
            holder.binding.txtNoEquipment.setText(maintenanceReportList.get(position).getNo_of_equipment());
            holder.binding.txtRepairType.setText(maintenanceReportList.get(position).getRepair_type());
            String upToNCharacters = maintenanceReportList.get(position).getCreated_dt().substring(0, Math.min(maintenanceReportList.get(position).getCreated_dt().length(), 10));
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
                    .load(BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getRepair_img())
                    .into(holder.binding.imgRepair);

            holder.binding.imgRepair.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getRepair_img();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Repair image");
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return maintenanceReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ModelMaintenanceReportBinding binding;

            public ViewHolder(ModelMaintenanceReportBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}