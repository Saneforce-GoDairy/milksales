package com.saneforce.godairy.procurement.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.databinding.ModelFarmerCreaReportBinding;
import com.saneforce.godairy.databinding.ModelMilkCollBinding;
import com.saneforce.godairy.procurement.reports.MilkCollViewActivity;
import com.saneforce.godairy.procurement.reports.model.Farmer;
import com.saneforce.godairy.procurement.reports.model.MilkCollection;

import java.util.ArrayList;
import java.util.List;

public class MilkCollectionAdapter extends RecyclerView.Adapter<MilkCollectionAdapter.ViewHolder>{
    private List<MilkCollection> milkCollectionList;
    private final Context context;

    public MilkCollectionAdapter(List<MilkCollection> milkCollectionList, Context context) {
        this.milkCollectionList = milkCollectionList;
        this.context = context;
    }

    public void filterList(ArrayList<MilkCollection> filterlist) {
        milkCollectionList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MilkCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelMilkCollBinding binding = ModelMilkCollBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MilkCollectionAdapter.ViewHolder holder, int position) {
        String customerName = milkCollectionList.get(position).getCustomerName();
        String customerNo = milkCollectionList.get(position).getCustomerNo();
        String date = milkCollectionList.get(position).getDate();
        String session = milkCollectionList.get(position).getSession();
        String milkType = milkCollectionList.get(position).getMilkType();
        String cans = milkCollectionList.get(position).getNoOfCans();
        String milkWeight = milkCollectionList.get(position).getMilkWeight();
        String totalMilkQty = milkCollectionList.get(position).getMilkTotalQty();
        String sampleNo = milkCollectionList.get(position).getMilkSampleNo();
        String fat = milkCollectionList.get(position).getMilkFat();
        String snf = milkCollectionList.get(position).getMilkSnf();
        String clr = milkCollectionList.get(position).getMilkClr();

        String milkRate = milkCollectionList.get(position).getMilkRate();
        String totalAmount = milkCollectionList.get(position).getTotalAmount();

        if (!customerName.isEmpty()) {
            holder.binding.firstLetter.setText(customerName.substring(0,1).toUpperCase());
            holder.binding.txtName.setText(customerName);
        }else {
            holder.binding.firstLetter.setText("E");
            holder.binding.txtName.setText("Error! No Name");
        }

        holder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, MilkCollViewActivity.class);
                intent.putExtra("customer_name", customerName);
                intent.putExtra("customer_no", customerNo);
                intent.putExtra("date", date);
                intent.putExtra("session", session);
                intent.putExtra("milk_type", milkType);
                intent.putExtra("cans", cans);
                intent.putExtra("milk_weight", milkWeight);
                intent.putExtra("total_milk_qty", totalMilkQty);
                intent.putExtra("milk_sample_no", sampleNo);
                intent.putExtra("fat", fat);
                intent.putExtra("snf", snf);
                intent.putExtra("clr", clr);
                intent.putExtra("milk_rate", milkRate);
                intent.putExtra("total_amount", totalAmount);
                context.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return milkCollectionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelMilkCollBinding binding;

        public ViewHolder(ModelMilkCollBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
