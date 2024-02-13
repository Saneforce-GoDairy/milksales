package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.OutletGeoTagInfoModel;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OutletGeoTagInfoAdapter extends RecyclerView.Adapter<OutletGeoTagInfoAdapter.ViewHolder> {
    Context context;
    ArrayList<OutletGeoTagInfoModel> list;
    AssistantClass assistantClass;

    public OutletGeoTagInfoAdapter(Context context, ArrayList<OutletGeoTagInfoModel> list) {
        this.context = context;
        this.list = list;
        assistantClass = new AssistantClass(context);
    }

    @NonNull
    @Override
    public OutletGeoTagInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutletGeoTagInfoAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_outlet_geo_tag_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OutletGeoTagInfoAdapter.ViewHolder holder, int position) {
        holder.code.setText(list.get(holder.getBindingAdapterPosition()).getCode());
        holder.name.setText(list.get(holder.getBindingAdapterPosition()).getName());
        holder.address.setText(list.get(holder.getBindingAdapterPosition()).getAddress());
        holder.mobile.setText(list.get(holder.getBindingAdapterPosition()).getMobile());
        if (list.get(holder.getBindingAdapterPosition()).getLat().trim().equals("0") || list.get(holder.getBindingAdapterPosition()).getLng().trim().equals("0") || list.get(holder.getBindingAdapterPosition()).getLat().isEmpty() || list.get(holder.getBindingAdapterPosition()).getLng().isEmpty()) {
            holder.location.setImageResource(R.drawable.ic_location_off);
        } else {
            holder.location.setImageResource(R.drawable.ic_location);
        }
        holder.mobile.setOnClickListener(view -> {
            assistantClass.showAlertDialog("", "Are you sure you want to make call?", true, "Yes", "No", new AlertDialogClickListener() {
                @Override
                public void onPositiveButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                    assistantClass.makeCall(list.get(holder.getBindingAdapterPosition()).getMobile());
                }

                @Override
                public void onNegativeButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView code, name, address, mobile;
        ImageView location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            address = itemView.findViewById(R.id.address);
            location = itemView.findViewById(R.id.location);
        }
    }
}
