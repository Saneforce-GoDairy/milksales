package com.saneforce.godairy.procurement.custom_form.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.databinding.ModelCustomFormModuleReportBinding;
import com.saneforce.godairy.procurement.custom_form.CustomFormDetailsViewActivity;
import com.saneforce.godairy.procurement.custom_form.CustomFormReportDetailsActivity;
import com.saneforce.godairy.procurement.custom_form.model.ModuleList;

import java.util.List;

public class ModuleReportsAdapter extends RecyclerView.Adapter<ModuleReportsAdapter.ViewHolder> {
    List<ModuleList> moduleLists;
    Context context;

    public ModuleReportsAdapter(Context context, List<ModuleList> moduleLists) {
        this.moduleLists = moduleLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ModuleReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelCustomFormModuleReportBinding binding1 = ModelCustomFormModuleReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ModuleReportsAdapter.ViewHolder(binding1);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleReportsAdapter.ViewHolder holder, int position) {
        ModuleList moduleList = moduleLists.get(position);

        holder.binding.nameText.setText(moduleList.getModuleName());

        holder.binding.container.setOnClickListener(v -> {
            String mModuleId = moduleList.getModuleId();
            Intent intent = new Intent(context, CustomFormReportDetailsActivity.class);
            intent.putExtra("title", moduleList.getModuleName());
            intent.putExtra("module_id", moduleList.getModuleId());
            intent.putExtra("moduleName", moduleList.getModuleName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return moduleLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ModelCustomFormModuleReportBinding binding;

        public ViewHolder(ModelCustomFormModuleReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}