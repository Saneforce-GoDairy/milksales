package com.saneforce.godairy.procurement.adapter;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.saneforce.godairy.Model_Class.ProcMaintenRegularReport;
import com.saneforce.godairy.databinding.ModelMaintenanceReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MaintenRegularReportAdapter extends RecyclerView.Adapter<MaintenRegularReportAdapter.ViewHolder>{
    private final List<ProcMaintenRegularReport> maintenanceReportList;
    private  final Context context;

    public MaintenRegularReportAdapter(Context context , List<ProcMaintenRegularReport> maintenanceReportList) {
        this.maintenanceReportList = maintenanceReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public MaintenRegularReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelMaintenanceReportBinding binding = ModelMaintenanceReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenRegularReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(maintenanceReportList.get(position).getCompany());
        holder.binding.txtPlant.setText(maintenanceReportList.get(position).getPlant());
        String upToNCharacters = maintenanceReportList.get(position).getCreated_dt().substring(0, Math.min(maintenanceReportList.get(position).getCreated_dt().length(), 10));
        holder.binding.txtDate.setText(upToNCharacters);

        holder.binding.txtBmcNoHrsRuns.setText(maintenanceReportList.get(position).getBmc_hrs_running());

               /*
               below logic used for access procurement images folder ( its wrks dev and live )
             */
        URL url = null;
        try {
            url = new URL(BASE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Proc_Photos/";
        Log.e("proc_img_url", BASE_URL_PROCUREMENT_IMG);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getHrs_runs_image())
                .into(holder.binding.noOfHrsRunsImg);

        holder.binding.noOfHrsRunsImgCn.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getHrs_runs_image();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "BMC Hrs Runs image");
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

        holder.binding.txtVolumeColl.setText(maintenanceReportList.get(position).getBmc_volume_coll());
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