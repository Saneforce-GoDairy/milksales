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

        String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Procurement/Proc_Photos/";
        Log.e("proc_img_url", BASE_URL_PROCUREMENT_IMG);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getHrs_runs_image())
                .into(holder.binding.noOfHrsRunsImg);

        holder.binding.noOfHrsRunsImgCn.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getDg_set_running_img();
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
        holder.binding.txtCcNoOfHrsRuns.setText(maintenanceReportList.get(position).getCc_hrs_running());
        holder.binding.txtCcVolumeColl.setText(maintenanceReportList.get(position).getCc_volume_coll());
        holder.binding.txtCcIbtRunsHrs.setText(maintenanceReportList.get(position).getIbt_running_hrs());
        holder.binding.txtCcDgSet.setText(maintenanceReportList.get(position).getDg_set_running());

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getDg_set_running_img())
                .into(holder.binding.dgSetImg);

        holder.binding.ccDgSetImgCn.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getDg_set_running_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "DG Set Running image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.txtPowerFactor.setText(maintenanceReportList.get(position).getPower_factor());

        holder.binding.txtPipline.setText(maintenanceReportList.get(position).getPipeline_condition());
        holder.binding.txtLeakages.setText(maintenanceReportList.get(position).getLeakage());
        holder.binding.txtPowerFactor.setText(maintenanceReportList.get(position).getPower_factor());
        holder.binding.txtScale.setText(maintenanceReportList.get(position).getScale());

        holder.binding.txtAsPerBook.setText(maintenanceReportList.get(position).getPer_book());
        holder.binding.txtPhysical.setText(maintenanceReportList.get(position).getPhysical());

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getAs_per_book_img())
                .into(holder.binding.asPerBookImg);

        holder.binding.ccAsPerBookImgCn.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + maintenanceReportList.get(position).getAs_per_book_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "As Per Book image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.txtEtp.setText(maintenanceReportList.get(position).getEtp());
        holder.binding.txtHotWater.setText(maintenanceReportList.get(position).getHot_water());
        holder.binding.txtFactoryLicen.setText(maintenanceReportList.get(position).getFactory_license_ins());
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