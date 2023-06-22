package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.CancelOrderModel;

import java.util.ArrayList;

public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.ViewHolder> {
    ArrayList<CancelOrderModel> list;
    Context context;

    CancelOrderAdapter.ItemSelected itemSelected;

    public CancelOrderAdapter(ArrayList<CancelOrderModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setItemSelected(CancelOrderAdapter.ItemSelected itemSelected) {
        this.itemSelected = itemSelected;
    }

    @NonNull
    @Override
    public CancelOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CancelOrderAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CancelOrderAdapter.ViewHolder holder, int position) {
        CancelOrderModel model = list.get(position);

        holder.title.setText(model.getRemark());

        holder.itemView.setOnClickListener(v -> {
            if (itemSelected != null) {
                itemSelected.onItemSelected(model, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemSelected {
        void onItemSelected(CancelOrderModel model, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name);
        }
    }
}
