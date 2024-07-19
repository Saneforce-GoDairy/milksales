package com.saneforce.godairy.procurement.adapter;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
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
import com.saneforce.godairy.procurement.ImageViewActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FarmerCreaReportAdapter extends RecyclerView.Adapter<FarmerCreaReportAdapter.ViewHolder>{
    private final List<ProcFarmerCreaReport> farmerCreaReportList;
    private  final Context context;

    public FarmerCreaReportAdapter(Context context , List<ProcFarmerCreaReport> farmerCreaReportList) {
        this.farmerCreaReportList = farmerCreaReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public FarmerCreaReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelFarmerCreaReportBinding binding = ModelFarmerCreaReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerCreaReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtVillageCenter.setText(farmerCreaReportList.get(position).getCenter());
        holder.binding.txtFarmerCategory.setText(farmerCreaReportList.get(position).getFarmer_category());
        holder.binding.txtFarmerName.setText(farmerCreaReportList.get(position).getFarmer_name());
        holder.binding.txtAddress.setText(farmerCreaReportList.get(position).getAddress());
        holder.binding.txtPhoneNo.setText(farmerCreaReportList.get(position).getPhone_number());
        holder.binding.txtPinCode.setText(farmerCreaReportList.get(position).getPin_code());
        holder.binding.txtNoOfAniCow.setText(farmerCreaReportList.get(position).getNo_of_ani_cow());
        holder.binding.txtNoOfAniBuffalo.setText(farmerCreaReportList.get(position).getNo_of_ani_buffalo());
        holder.binding.txtMilkAvailCow.setText(farmerCreaReportList.get(position).getMilk_avail_lttr_cow());
        holder.binding.txtMilkAvailBaffalo.setText(farmerCreaReportList.get(position).getMilk_avail_lttr_buffalo());
        holder.binding.txtMilkSupplyCompany.setText(farmerCreaReportList.get(position).getMilk_supply_company());
        holder.binding.txtInterestedForSupply.setText(farmerCreaReportList.get(position).getInterested_for_supply());
        String upToNCharacters = farmerCreaReportList.get(position).getCreated_dt().substring(0, Math.min(farmerCreaReportList.get(position).getCreated_dt().length(), 10));
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

        String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Procurement/Proc_Photos/";
        Log.e("proc_img_url", BASE_URL_PROCUREMENT_IMG);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + farmerCreaReportList.get(position).getFarmer_img())
                .into(holder.binding.imgFarmer);

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

        holder.binding.imgFarmer.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + farmerCreaReportList.get(position).getFarmer_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Farmer image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return farmerCreaReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelFarmerCreaReportBinding binding;

        public ViewHolder(ModelFarmerCreaReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}