package com.saneforce.godairy.universal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;

public class UniversalDropDownAdapter extends RecyclerView.Adapter<UniversalDropDownAdapter.ViewHolder> implements Filterable {
    Context context;
    JSONArray array, filteredArray;
    OnItemClick onItemClick;

    public UniversalDropDownAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
        try {
            this.filteredArray = new JSONArray(array.toString());
        } catch (JSONException ignored) { }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public UniversalDropDownAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UniversalDropDownAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UniversalDropDownAdapter.ViewHolder holder, int position) {
        try {
            holder.title.setText(filteredArray.getJSONObject(holder.getBindingAdapterPosition()).getString("title"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) {
                onItemClick.onClick(holder.getBindingAdapterPosition(), filteredArray);
            }
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
                String query = constraint.toString().toLowerCase();
                JSONArray tempArray = new JSONArray();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        String myTitle = array.getJSONObject(i).getString("title");
                        if (myTitle.toLowerCase().contains(query)) {
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
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name);
        }
    }

    public interface OnItemClick {
        void onClick(int position, JSONArray arrayList);
    }
}
