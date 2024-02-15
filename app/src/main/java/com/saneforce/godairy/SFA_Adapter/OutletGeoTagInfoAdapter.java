package com.saneforce.godairy.SFA_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Activity.GeoTagActivity;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.assistantClass.AssistantClass;

import org.json.JSONArray;
import org.json.JSONException;

public class OutletGeoTagInfoAdapter extends RecyclerView.Adapter<OutletGeoTagInfoAdapter.ViewHolder> implements Filterable {
    Context context;
    JSONArray array, filteredArray;
    AssistantClass assistantClass;

    public OutletGeoTagInfoAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
        assistantClass = new AssistantClass(context);
        try {
            this.filteredArray = new JSONArray(array.toString());
        } catch (JSONException ignored) { }
    }

    @NonNull
    @Override
    public OutletGeoTagInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutletGeoTagInfoAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_outlet_geo_tag_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OutletGeoTagInfoAdapter.ViewHolder holder, int position) {
        holder.code.setText(filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("code"));
        holder.name.setText(filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("name"));
        holder.address.setText(filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("address"));
        holder.mobile.setText(filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("mobile"));
        if (filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("lat").trim().equals("0") || filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("lng").trim().equals("0") || filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("lat").isEmpty() || filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("lng").isEmpty()) {
            holder.location.setImageResource(R.drawable.ic_location_off);
        } else {
            holder.location.setImageResource(R.drawable.ic_location);
        }
        holder.mobile.setOnClickListener(view -> {
            assistantClass.showAlertDialog("", "Are you sure you want to make call?", true, "Yes", "No", new AlertDialogClickListener() {
                @Override
                public void onPositiveButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                    assistantClass.makeCall(filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("mobile"));
                }

                @Override
                public void onNegativeButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        });
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, GeoTagActivity.class);
            intent.putExtra("outletId", filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("code"));
            intent.putExtra("outletName", filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("name"));
            intent.putExtra("outletLat", filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("lat"));
            intent.putExtra("outletLng", filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("lng"));
            intent.putExtra("outletAddress", filteredArray.optJSONObject(holder.getBindingAdapterPosition()).optString("address"));
            ((Activity) context).startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return filteredArray.length();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                JSONArray tempArray = new JSONArray();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        String myTitle = array.getJSONObject(i).getString("name");
                        String Code = array.getJSONObject(i).getString("code");
                        if (myTitle.toLowerCase().contains(query) || Code.toLowerCase().contains(query)) {
                            tempArray.put(array.getJSONObject(i));
                        }
                    } catch (JSONException ignored) { }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = tempArray;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredArray = (JSONArray) results.values;
                notifyDataSetChanged();
            }
        };
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
