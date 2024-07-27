package com.saneforce.godairy.procurement.adapter;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.saneforce.godairy.Model_Class.ProcFarmerCreaReport;
import com.saneforce.godairy.databinding.ModelFarmerCreaReportBinding;
import com.saneforce.godairy.procurement.AgentUpdateActivity;
import com.saneforce.godairy.procurement.FarmerUpdateActivity;
import com.saneforce.godairy.procurement.ImageViewActivity;
import com.saneforce.godairy.procurement.reports.model.Agent;
import com.saneforce.godairy.procurement.reports.model.Farmer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FarmerListAdapter extends RecyclerView.Adapter<FarmerListAdapter.ViewHolder>{
    private List<Farmer> farmerList;
    private  final Context context;

    public FarmerListAdapter(Context context , List<Farmer> farmerList) {
        this.farmerList = farmerList;
        this.context = context;
    }

    public void filterList(ArrayList<Farmer> filterlist) {
        farmerList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FarmerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelFarmerCreaReportBinding binding = ModelFarmerCreaReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerListAdapter.ViewHolder holder, int position) {
        String farmerName = farmerList.get(position).getFarmer_name();

        if (!farmerName.isEmpty()) {
            holder.binding.firstLetter.setText(farmerName.substring(0,1).toUpperCase());
            holder.binding.txtName.setText(farmerName);
        }else {
            holder.binding.firstLetter.setText("E");
            holder.binding.txtName.setText("Error! No Name");
        }

        holder.binding.txtPhone.setText(farmerList.get(position).getFarmer_mobile());

        holder.binding.createSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, FarmerUpdateActivity.class);
                intent.putExtra("id", farmerList.get(position).getId());
                intent.putExtra("farmer_name", farmerList.get(position).getFarmer_name());
                intent.putExtra("farmer_photo", farmerList.get(position).getFarmer_photo());
                intent.putExtra("state", farmerList.get(position).getState());
                intent.putExtra("district", farmerList.get(position).getDistrict());
                intent.putExtra("mobile", farmerList.get(position).getFarmer_mobile());
                intent.putExtra("town", farmerList.get(position).getTown());
                intent.putExtra("coll_center", farmerList.get(position).getColl_center());
                intent.putExtra("fa_category", farmerList.get(position).getFarmerCategory());
                intent.putExtra("addr", farmerList.get(position).getAddress());
                intent.putExtra("pin_code", farmerList.get(position).getPincode());
                intent.putExtra("city", farmerList.get(position).getCity());
                intent.putExtra("email", farmerList.get(position).getEmail());
                intent.putExtra("incentive_amt", farmerList.get(position).getIncentive_amt());
                intent.putExtra("cartage_amt", farmerList.get(position).getCartage_amt());
//                intent.putExtra("form_id", "1"); // id 1 for edit and updation of data
                context.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return farmerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelFarmerCreaReportBinding binding;

        public ViewHolder(ModelFarmerCreaReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}