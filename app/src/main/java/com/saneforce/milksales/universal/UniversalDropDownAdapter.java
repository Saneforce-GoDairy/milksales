package com.saneforce.milksales.universal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;

public class UniversalDropDownAdapter extends RecyclerView.Adapter<UniversalDropDownAdapter.ViewHolder> {
    Context context;
    JSONArray array;
    OnItemClick onItemClick;

    public UniversalDropDownAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
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
            holder.title.setText(array.getJSONObject(holder.getBindingAdapterPosition()).getString("title"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) {
                onItemClick.onClick(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name);
        }
    }

    public interface OnItemClick {
        void onClick(int position);
    }
}
