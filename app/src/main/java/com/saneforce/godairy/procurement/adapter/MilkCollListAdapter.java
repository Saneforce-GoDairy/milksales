package com.saneforce.godairy.procurement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.databinding.ModelFarmerCreaReportBinding;
import com.saneforce.godairy.procurement.reports.model.Farmer;
import com.saneforce.godairy.procurement.reports.model.MilkCollection;

import java.util.ArrayList;
import java.util.List;

public class MilkCollListAdapter extends RecyclerView.Adapter<MilkCollListAdapter.ViewHolder> {
    private List<MilkCollection>milkCollectionList;
    private final Context context;

    public MilkCollListAdapter(Context context , List<MilkCollection> milkCollectionList) {
        this.milkCollectionList = milkCollectionList;
        this.context = context;
    }

    public void filterList(ArrayList<MilkCollection> filterlist) {
        milkCollectionList = filterlist;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MilkCollListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelFarmerCreaReportBinding binding = ModelFarmerCreaReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MilkCollListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return milkCollectionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelFarmerCreaReportBinding binding;

        public ViewHolder(ModelFarmerCreaReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
