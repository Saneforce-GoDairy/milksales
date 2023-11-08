package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.ApproveOutletsActivity;
import com.saneforce.godairy.SFA_Model_Class.ModelPendingOutletsCategory;

import java.util.ArrayList;

public class AdapterPendingOutletsCategory extends RecyclerView.Adapter<AdapterPendingOutletsCategory.ViewHolder> {

    ArrayList<ModelPendingOutletsCategory> list;
    Context context;

    public AdapterPendingOutletsCategory(ArrayList<ModelPendingOutletsCategory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterPendingOutletsCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_outlets_category, parent, false);
        return new AdapterPendingOutletsCategory.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPendingOutletsCategory.ViewHolder holder, int position) {
        ModelPendingOutletsCategory model = list.get(position);
        holder.sfName.setText(model.getSfName());
        holder.stockistName.setText(model.getStockistName());
        holder.count.setText("Outlets: " + model.getListedDrCount());

        holder.open.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApproveOutletsActivity.class);
            intent.putExtra("stockistCode", model.getStockistCode());
            intent.putExtra("sfCode", model.getSfCode());
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApproveOutletsActivity.class);
            intent.putExtra("stockistCode", model.getStockistCode());
            intent.putExtra("sfCode", model.getSfCode());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sfName, stockistName, count, open;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sfName = itemView.findViewById(R.id.sf_name);
            stockistName = itemView.findViewById(R.id.stockist_name);
            count = itemView.findViewById(R.id.outlets_count);
            open = itemView.findViewById(R.id.open);
        }
    }
}
