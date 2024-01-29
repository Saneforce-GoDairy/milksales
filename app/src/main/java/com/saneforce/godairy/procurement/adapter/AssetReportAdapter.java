package com.saneforce.godairy.procurement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Model_Class.ProcAssetReport;
import com.saneforce.godairy.databinding.ModelAssetReportBinding;
import java.util.List;

public class AssetReportAdapter extends RecyclerView.Adapter<AssetReportAdapter.ViewHolder>{
    private final List<ProcAssetReport> assetReportList;
    private  final Context context;

    public AssetReportAdapter(Context context , List<ProcAssetReport> assetReportList) {
        this.assetReportList = assetReportList;
        this.context = context;
    }


    @NonNull
    @Override
    public AssetReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelAssetReportBinding binding = ModelAssetReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(assetReportList.get(position).getCompany());
        holder.binding.txtPlant.setText(assetReportList.get(position).getPlant());
        holder.binding.txtAssetType.setText(assetReportList.get(position).getAsset_type());
        holder.binding.txtComments.setText(assetReportList.get(position).getComments());
        String upToNCharacters = assetReportList.get(position).getCreated_dt().substring(0, Math.min(assetReportList.get(position).getCreated_dt().length(), 10));
        holder.binding.txtDate.setText(upToNCharacters);
    }

    @Override
    public int getItemCount() {
        return assetReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelAssetReportBinding binding;

        public ViewHolder(ModelAssetReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
