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
import com.saneforce.godairy.Model_Class.ProcAITReport;
import com.saneforce.godairy.databinding.ModelAitReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AITReportListAdapter extends RecyclerView.Adapter<AITReportListAdapter.ViewHolder>{
    private final List<ProcAITReport> aitReportList;
    private final Context context;

    public AITReportListAdapter(Context context, List<ProcAITReport> aitReportList) {
        this.aitReportList = aitReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public AITReportListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelAitReportBinding binding = ModelAitReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AITReportListAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(aitReportList.get(position).getCompany());
        holder.binding.txtPlant.setText(aitReportList.get(position).getPlant());
        holder.binding.txtFarmerName.setText(aitReportList.get(position).getFarmer_name());
        holder.binding.txtCenterName.setText(aitReportList.get(position).getCenter_name());
        holder.binding.txtBreedName.setText(aitReportList.get(position).getBreed_name());
        String upToNCharacters = aitReportList.get(position).getDate().substring(0, Math.min(aitReportList.get(position).getDate().length(), 10));
        holder.binding.txtDate.setText(upToNCharacters);
        holder.binding.txtNoOfAi.setText(aitReportList.get(position).getNo_of_ai());
        holder.binding.txtBullNos.setText(aitReportList.get(position).getBull_nos());
        holder.binding.txtPdVerification.setText(aitReportList.get(position).getPd_verification());
        holder.binding.txtCalfbirthVerification.setText(aitReportList.get(position).getCalfbirth_verification());
        holder.binding.txtMineralMixSale.setText(aitReportList.get(position).getMineral_mixture_sale());
        holder.binding.txtSeedSales.setText(aitReportList.get(position).getSeed_sales());

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
                .load(BASE_URL_PROCUREMENT_IMG + aitReportList.get(position).getBreed_image())
                .into(holder.binding.breedImg);

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

        holder.binding.breedImg.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + aitReportList.get(position).getBreed_image();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Breed image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return aitReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ModelAitReportBinding binding;

        public ViewHolder(ModelAitReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
