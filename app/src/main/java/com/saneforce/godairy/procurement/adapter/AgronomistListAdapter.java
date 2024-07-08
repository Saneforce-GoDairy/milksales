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
import com.saneforce.godairy.Model_Class.ProcAgronoListModel;
import com.saneforce.godairy.databinding.ModelAgronomistReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AgronomistListAdapter extends  RecyclerView.Adapter<AgronomistListAdapter.ViewHolder>{
    private final List<ProcAgronoListModel> agronomistListModel;
    private final Context context;

    public AgronomistListAdapter(Context context, List<ProcAgronoListModel> agronomistListModel) {
        this.context = context;
        this.agronomistListModel = agronomistListModel;
    }

    @NonNull
    @Override
    public AgronomistListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelAgronomistReportBinding binding = ModelAgronomistReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AgronomistListAdapter.ViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(agronomistListModel.get(position).getCompany());
        holder.binding.txtFarmerName.setText(agronomistListModel.get(position).getFarmer_name());
        String upToNCharacters = agronomistListModel.get(position).getCreated_dt().substring(0, Math.min(agronomistListModel.get(position).getCreated_dt().length(), 10));
        holder.binding.txtDate.setText(upToNCharacters);
        holder.binding.txtCenterName.setText(agronomistListModel.get(position).getCenter_name());
        holder.binding.txtServiceType.setText(agronomistListModel.get(position).getService_type());
        holder.binding.txtProductType.setText(agronomistListModel.get(position).getProduct_type());
        holder.binding.txtPlantName.setText(agronomistListModel.get(position).getPlant_name());
        holder.binding.txtTeatDip.setText(agronomistListModel.get(position).getTeat_dip());
        holder.binding.txtFodderDevAcres.setText(agronomistListModel.get(position).getFodder_dev());
        holder.binding.txtFarmersEnrolled.setText(agronomistListModel.get(position).getFarmers_enrolled());

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
                .load(BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFarmers_meeting_img())
                .into(holder.binding.farmersMeetingImg);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getCsr_img())
                .into(holder.binding.csrActivityImg);

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFodder_acres_img())
                .into(holder.binding.fodderAcresImg);

        holder.binding.farmersMeetingImg.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFarmers_meeting_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Farmers meeting"); // This is url ( not URI )
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.csrActivityImg.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getCsr_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "CSR Activity"); // This is url ( not URI )
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

        holder.binding.fodderAcresImg.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFodder_acres_img();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Fodder dev acres"); // This is url ( not URI )
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
    public long getItemId(int position){
        return position;
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public int getItemCount() {
        return agronomistListModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ModelAgronomistReportBinding binding;

        public ViewHolder(ModelAgronomistReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
