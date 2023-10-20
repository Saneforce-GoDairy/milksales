package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;

import java.util.ArrayList;

public class ReturnTypeAdapter extends RecyclerView.Adapter<ReturnTypeAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list;

    ItemSelected itemSelected;

    public void setItemSelected(ItemSelected itemSelected) {
        this.itemSelected = itemSelected;
    }

    public ReturnTypeAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReturnTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReturnTypeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.return_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnTypeAdapter.ViewHolder holder, int position) {
        String model = list.get(position);
        holder.returnTypeName.setText(model);
        holder.itemView.setOnClickListener(v -> {
            if (itemSelected != null) {
                itemSelected.onItemSelected(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView returnTypeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            returnTypeName = itemView.findViewById(R.id.returnTypeName);
        }
    }

    public interface ItemSelected {
        void onItemSelected(String data);
    }
}
