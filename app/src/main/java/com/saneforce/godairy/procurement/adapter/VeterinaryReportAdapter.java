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
import com.saneforce.godairy.Model_Class.ProcVeterinaryReport;
import com.saneforce.godairy.databinding.ModelVeterinaryReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class VeterinaryReportAdapter extends RecyclerView.Adapter<VeterinaryReportAdapter.ViewHolder>{
    private final List<ProcVeterinaryReport> veterinaryReportList;
    private final Context context;

    public VeterinaryReportAdapter(Context context, List<ProcVeterinaryReport> veterinaryReportList) {
        this.veterinaryReportList = veterinaryReportList;
        this.context = context;
    }

    @NonNull
    @Override
    public VeterinaryReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelVeterinaryReportBinding binding = ModelVeterinaryReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VeterinaryReportAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(veterinaryReportList.get(position).getCompany());
        holder.binding.txtPlant.setText(veterinaryReportList.get(position).getPlant());
        holder.binding.txtCenterName.setText(veterinaryReportList.get(position).getCenter_name());
        holder.binding.txtFarmerName.setText(veterinaryReportList.get(position).getFarmer_code());
        holder.binding.txtServiceTypeName.setText(veterinaryReportList.get(position).getService_type());
        holder.binding.txtProductType.setText(veterinaryReportList.get(position).getProduct_type());
        holder.binding.txtSeedSale.setText(veterinaryReportList.get(position).getSeed_sale());
        holder.binding.txtMineralMixture.setText(veterinaryReportList.get(position).getMineral_mixture());
        holder.binding.txtFodderSettsSale.setText(veterinaryReportList.get(position).getFodder_setts_sales());
        holder.binding.txtCattleFeedOrder.setText(veterinaryReportList.get(position).getCattle_feed_order());
        holder.binding.txtTeatDip.setText(veterinaryReportList.get(position).getTeat_dip());
        holder.binding.txtEvm.setText(veterinaryReportList.get(position).getEvm());
        holder.binding.txtCaseTpe.setText(veterinaryReportList.get(position).getCase_type());
        holder.binding.txtIdentifiedFarmersCount.setText(veterinaryReportList.get(position).getIdent_farmers_count());
        holder.binding.txtEnrolledFarmers.setText(veterinaryReportList.get(position).getTeat_dip());
        holder.binding.txtFarmersInducted.setText(veterinaryReportList.get(position).getInducted_farmers());
        String upToNCharacters = veterinaryReportList.get(position).getCreated_date().substring(0, Math.min(veterinaryReportList.get(position).getCreated_date().length(), 10));
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

        String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Procurement_images/";
        Log.e("proc_img_url", BASE_URL_PROCUREMENT_IMG);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getService_type_img())
                .into(holder.binding.serviceTypeImg);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getEvm_img())
                .into(holder.binding.evmImg);

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

        holder.binding.serviceTypeImg.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getService_type_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Service type");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.evmImg.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + veterinaryReportList.get(position).getEvm_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Emergency treatment/EVM");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return veterinaryReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ModelVeterinaryReportBinding binding;

        public ViewHolder(ModelVeterinaryReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}