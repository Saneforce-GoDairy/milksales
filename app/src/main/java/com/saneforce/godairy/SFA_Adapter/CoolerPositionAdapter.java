package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.CoolerPositionModel;

import java.util.ArrayList;

public class CoolerPositionAdapter extends RecyclerView.Adapter<CoolerPositionAdapter.ViewHolder> {
    ArrayList<CoolerPositionModel> list;
    Context context;

    CoolerPositionAdapter.ItemSelected itemSelected;

    public CoolerPositionAdapter(ArrayList<CoolerPositionModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setItemSelected(CoolerPositionAdapter.ItemSelected itemSelected) {
        this.itemSelected = itemSelected;
    }

    @NonNull
    @Override
    public CoolerPositionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CoolerPositionAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CoolerPositionAdapter.ViewHolder holder, int position) {
        CoolerPositionModel model = list.get(position);

        holder.title.setText(model.getName());

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
        void onItemSelected(CoolerPositionModel model, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name);
        }
    }
}
