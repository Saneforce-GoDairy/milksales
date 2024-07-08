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
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.databinding.ModelQualityReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class QualityReportAdapter extends RecyclerView.Adapter<QualityReportAdapter.ViewHolder>{
    private final List<ProcQualityReport> qualityReportList;
    private  final Context context;

    public QualityReportAdapter(Context context , List<ProcQualityReport> qualityReportList) {
        this.qualityReportList = qualityReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public QualityReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelQualityReportBinding binding = ModelQualityReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QualityReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(qualityReportList.get(position).getCompany());
        holder.binding.txtPlant.setText(qualityReportList.get(position).getPlant());
        holder.binding.txtMassBalance.setText(qualityReportList.get(position).getMass_balance());
        holder.binding.txtMilkCollection.setText(qualityReportList.get(position).getMilk_collection());
        holder.binding.txtMbrt.setText(qualityReportList.get(position).getMbrt());
        holder.binding.txtRejection.setText(qualityReportList.get(position).getRejection());
        holder.binding.txtSplCleaning.setText(qualityReportList.get(position).getSpecial_cleaning());
        holder.binding.txtWithhood.setText(qualityReportList.get(position).getVehicles_rece_withhood());
        holder.binding.txtWithouthood.setText(qualityReportList.get(position).getVehicles_rece_withouthood());
        holder.binding.txtChemicals.setText(qualityReportList.get(position).getRecords_chemicals());
        holder.binding.txtStock.setText(qualityReportList.get(position).getRecords_stock());
        holder.binding.txtMilk.setText(qualityReportList.get(position).getRecord_milk());
        holder.binding.txtAwarenesProg.setText(qualityReportList.get(position).getAwareness_prog());
        holder.binding.txtCleaningEff.setText(qualityReportList.get(position).getCleaning_eff());
        holder.binding.txtNoOfFat.setText(qualityReportList.get(position).getSamp_calibra_no_fat());
        holder.binding.txtNoOfSnf.setText(qualityReportList.get(position).getSamp_calibra_no_snf());
        holder.binding.txtNoOfWeight.setText(qualityReportList.get(position).getSamp_calibra_no_weight());
        String upToNCharacters = qualityReportList.get(position).getCreated_dt().substring(0, Math.min(qualityReportList.get(position).getCreated_dt().length(), 10));
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

        String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Proc_Photos/";
        Log.e("proc_img_url", BASE_URL_PROCUREMENT_IMG);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getFat_image())
                .into(holder.binding.imgFat);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getSnf_image())
                .into(holder.binding.imgSnf);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getWithhood_imag())
                .into(holder.binding.imgWithHood);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getWithout_hood_image())
                .into(holder.binding.imgWithoutHood);

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

        holder.binding.imgFat.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getFat_image();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Fat image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.imgSnf.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getSnf_image();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "SNF image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.imgWithHood.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getWithhood_imag();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "With hood");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.imgWithoutHood.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + qualityReportList.get(position).getWithout_hood_image();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Without hood");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return qualityReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelQualityReportBinding binding;

        public ViewHolder(ModelQualityReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
