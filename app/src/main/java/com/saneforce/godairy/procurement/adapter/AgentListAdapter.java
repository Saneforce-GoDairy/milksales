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

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ModelAgentReportMainBinding;
import com.saneforce.godairy.procurement.AgentCreatActivity;
import com.saneforce.godairy.procurement.AgentUpdateActivity;
import com.saneforce.godairy.procurement.reports.model.Agent;

import java.util.ArrayList;
import java.util.List;

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.ViewHolder> {
    private List<Agent> agentList;
    private final Context context;

    public AgentListAdapter(List<Agent> agentList, Context context) {
        this.agentList = agentList;
        this.context = context;
    }

    public void filterList(ArrayList<Agent> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        agentList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModelAgentReportMainBinding binding = ModelAgentReportMainBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentListAdapter.ViewHolder holder, int position) {
        String agentName = agentList.get(position).getAgent_name();

        if (!agentName.isEmpty()) {
            holder.binding.firstLetter.setText(agentName.substring(0,1).toUpperCase());
            holder.binding.txtName.setText(agentName);
        }else {
            holder.binding.firstLetter.setText("E");
            holder.binding.txtName.setText("Error! No Name");
        }

        holder.binding.txtPhone.setText(agentList.get(position).getMobile());

        holder.binding.createSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
               Intent intent = new Intent(context, AgentUpdateActivity.class);
                intent.putExtra("id", agentList.get(position).getId());
               intent.putExtra("agent_name", agentList.get(position).getAgent_name());
               intent.putExtra("agent_photo", agentList.get(position).getAgentImage());
               intent.putExtra("state", agentList.get(position).getState());
               intent.putExtra("district", agentList.get(position).getDistrict());
               intent.putExtra("town", agentList.get(position).getTown());
               intent.putExtra("coll_center", agentList.get(position).getColl_center());
               intent.putExtra("agent_category", agentList.get(position).getAgent_category());
               intent.putExtra("company", agentList.get(position).getCompany());
               intent.putExtra("address", agentList.get(position).getAddress());
               intent.putExtra("pin_code", agentList.get(position).getPin_code());
               intent.putExtra("city", agentList.get(position).getCity());
               intent.putExtra("mobile_no", agentList.get(position).getMobile());
               intent.putExtra("email", agentList.get(position).getEmail());
               intent.putExtra("incentive_amt", agentList.get(position).getIncentive());
               intent.putExtra("cartage_amt", agentList.get(position).getCartage());
               intent.putExtra("form_id", "1"); // id 1 for edit and updation of data
               context.startActivity(intent);
               activity.overridePendingTransition(0, 0);
            }
        });

        /*
        holder.binding.txtState.setText(agentList.get(position).getState());
        holder.binding.txtDistrict.setText(agentList.get(position).getDistrict());
        holder.binding.txtTown.setText(agentList.get(position).getTown());
        holder.binding.txtCollCenter.setText(agentList.get(position).getColl_center());

        holder.binding.txtAgentCategory.setText(agentList.get(position).getAgent_category());
        holder.binding.txtCompany.setText(agentList.get(position).getCompany());
        holder.binding.txtAddress.setText(agentList.get(position).getAddress());
        holder.binding.txtPinCode.setText(agentList.get(position).getPin_code());

        holder.binding.txtCity.setText(agentList.get(position).getCity());
        holder.binding.txtMobileNo.setText(agentList.get(position).getMobile());
        holder.binding.txtIncentive.setText(agentList.get(position).getIncentive());
        holder.binding.txtCartage.setText(agentList.get(position).getCartage());
        holder.binding.txtEmail.setText(agentList.get(position).getEmail());

        URL url = null;
        try {
            url = new URL(BASE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Procurement/Proc_Photos/";

        Glide.with(context)
                .load(BASE_URL_PROCUREMENT_IMG + agentList.get(position).getAgentImage())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.error_image1)
                .into(holder.binding.imgAgent);

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

        holder.binding.agentImgCn.setOnClickListener(v -> {
            String imageUrl = BASE_URL_PROCUREMENT_IMG + agentList.get(position).getAgentImage();
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
            intent.putExtra("event_name", "Agent image");
            intent.putExtra("url", imageUrl); // url not URI
            context.startActivity(intent);
        });

         */

    }

    @Override
    public int getItemCount() {
        return agentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ModelAgentReportMainBinding binding;

        public ViewHolder(ModelAgentReportMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
