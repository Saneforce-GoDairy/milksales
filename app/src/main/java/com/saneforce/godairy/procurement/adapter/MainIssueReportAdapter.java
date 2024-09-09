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
import com.saneforce.godairy.Model_Class.ProcMaintenIssue;
import com.saneforce.godairy.databinding.ModelMaintenIssueReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainIssueReportAdapter extends RecyclerView.Adapter<MainIssueReportAdapter.ViewHolder>{
    private final List<ProcMaintenIssue> maintenIssueList;
    private final Context context;

    public MainIssueReportAdapter(Context context, List<ProcMaintenIssue> maintenIssueList) {
        this.maintenIssueList = maintenIssueList;
        this.context = context;
    }

    @NonNull
    @Override
    public MainIssueReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelMaintenIssueReportBinding binding = ModelMaintenIssueReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainIssueReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(maintenIssueList.get(position).getCompany());
        holder.binding.txtPlant.setText(maintenIssueList.get(position).getPlant());
        holder.binding.txtNoEquipment.setText(maintenIssueList.get(position).getEquipment());
        holder.binding.txtRepairType.setText(maintenIssueList.get(position).getRepair_type());

        URL url = null;
        try {
            url = new URL(BASE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Procurement/Proc_Photos/";

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + maintenIssueList.get(position).getRepair_type_img())
                .into(holder.binding.repairImg);

        holder.binding.repairImg.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + maintenIssueList.get(position).getRepair_type_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Repair image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return maintenIssueList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ModelMaintenIssueReportBinding binding;

        public ViewHolder(ModelMaintenIssueReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
