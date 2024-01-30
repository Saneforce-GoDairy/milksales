package com.saneforce.godairy.procurement.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Model_Class.ProcExistingAgentReport;
import com.saneforce.godairy.databinding.ModelExistingReportBinding;

import java.util.List;

public class ExistingAgentReportAdapter extends RecyclerView.Adapter<ExistingAgentReportAdapter.ViewHolder>{
    private final List<ProcExistingAgentReport> existingAgentReportList;
    private  final Context context;

    public ExistingAgentReportAdapter(Context context , List<ProcExistingAgentReport> existingAgentReportList) {
        this.existingAgentReportList = existingAgentReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExistingAgentReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelExistingReportBinding binding = ModelExistingReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExistingAgentReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(existingAgentReportList.get(position).getCompany());
        String upToNCharacters = existingAgentReportList.get(position).getCreated_dt().substring(0, Math.min(existingAgentReportList.get(position).getCreated_dt().length(), 10));
        holder.binding.txtDate.setText(upToNCharacters);
        holder.binding.txtAgentVisitType.setText(existingAgentReportList.get(position).getAgent());
        holder.binding.txtTotalMilkAvail.setText(existingAgentReportList.get(position).getTotal_milk_availability());
        holder.binding.txtOurCompanyLtrs.setText(existingAgentReportList.get(position).getOur_company_ltrs());
        holder.binding.txtCompetitorRate.setText(existingAgentReportList.get(position).getCompetitor_rate());
        holder.binding.txtOurCompanyRate.setText(existingAgentReportList.get(position).getOur_company_rate());
        holder.binding.txtDemand.setText(existingAgentReportList.get(position).getDemand());
        holder.binding.txtSupplyStartDt.setText(existingAgentReportList.get(position).getSupply_start_dt());

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
        return existingAgentReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelExistingReportBinding binding;

        public ViewHolder(ModelExistingReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}