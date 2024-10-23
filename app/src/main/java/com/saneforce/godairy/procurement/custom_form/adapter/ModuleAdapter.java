package com.saneforce.godairy.procurement.custom_form.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.databinding.ModelCustomFormBinding;
import com.saneforce.godairy.procurement.custom_form.CustomFormMainActivity;
import com.saneforce.godairy.procurement.custom_form.model.ModuleList;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
    List<ModuleList> moduleLists;
    Context context;
    int isPrimary;

    public ModuleAdapter(Context context, List<ModuleList> moduleLists, int isPrimary) {
        this.moduleLists = moduleLists;
        this.context = context;
        this.isPrimary = isPrimary;
    }

    @NonNull
    @Override
    public ModuleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelCustomFormBinding binding1 = ModelCustomFormBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding1);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleAdapter.ViewHolder holder, int position) {
        ModuleList moduleList = moduleLists.get(position);

        holder.binding.nameText.setText(moduleList.getModuleName());

        holder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mModuleId = moduleList.getModuleId();
                String debug = "";

                Intent intent = new Intent(context, CustomFormMainActivity.class);
                intent.putExtra("title", moduleList.getModuleName());
                intent.putExtra("moduleId",moduleList.getModuleId());
                intent.putExtra("moduleName",moduleList.getModuleName());
                intent.putExtra("isPrimary",isPrimary);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ModelCustomFormBinding binding;

        public ViewHolder(ModelCustomFormBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}