package com.saneforce.godairy.procurement.adapter;

import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.saneforce.godairy.Model_Class.ProcMaintenanceReport;
import com.saneforce.godairy.databinding.ModelMaintenanceReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MaintenanceReportAdapter extends RecyclerView.Adapter<MaintenanceReportAdapter.ViewHolder>{
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