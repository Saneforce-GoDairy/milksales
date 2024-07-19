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
import com.saneforce.godairy.Model_Class.ProcCollectionCeReport;
import com.saneforce.godairy.databinding.ModelCollCenterReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CollectionCeReportAdapter extends RecyclerView.Adapter<CollectionCeReportAdapter.ViewHolder>{
    private final List<ProcCollectionCeReport> collectionCeReportList;
    private  final Context context;

    public CollectionCeReportAdapter(Context context , List<ProcCollectionCeReport> collectionCeReportList) {
        this.collectionCeReportList = collectionCeReportList;
        this.context = context;
    }


    @NonNull
    @Override
    public CollectionCeReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelCollCenterReportBinding binding = ModelCollCenterReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionCeReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(collectionCeReportList.get(position).getCompany());
        holder.binding.txtPlant.setText(collectionCeReportList.get(position).getPlant());
        String upToNCharacters = collectionCeReportList.get(position).getCreated_dt().substring(0, Math.min(collectionCeReportList.get(position).getCreated_dt().length(), 10));
        holder.binding.txtDate.setText(upToNCharacters);
        holder.binding.txtSapCenterCode.setText(collectionCeReportList.get(position).getSap_center_code());
        holder.binding.txtSapCenterName.setText(collectionCeReportList.get(position).getSap_center_name());
        holder.binding.txtCenterAddrs.setText(collectionCeReportList.get(position).getCenter_addr());
        holder.binding.txtLactalisLpd.setText(collectionCeReportList.get(position).getLocatlis_lpd());
        holder.binding.txtNoOfFarmersEnroled.setText(collectionCeReportList.get(position).getFarmers_enrolled());
        holder.binding.txtCompetitorLpd1.setText(collectionCeReportList.get(position).getCompetitor_lpd());
        holder.binding.txtCompetitorLpd2.setText(collectionCeReportList.get(position).getCompetitor_lpd2());

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
                .load(BASE_URL_PROCUREMENT_IMG + collectionCeReportList.get(position).getCollection_ce_image())
                .into(holder.binding.imgSapCenter);

        holder.binding.imgSapCenter.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + collectionCeReportList.get(position).getCollection_ce_image();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Collection center");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

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
        return collectionCeReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelCollCenterReportBinding binding;

        public ViewHolder(ModelCollCenterReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}