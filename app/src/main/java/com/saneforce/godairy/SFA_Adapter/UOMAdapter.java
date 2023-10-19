package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.UOMModel;

import java.util.ArrayList;

public class UOMAdapter extends RecyclerView.Adapter<UOMAdapter.ViewHolder> {
    Context context;
    ArrayList<UOMModel> list;

    ItemSelected itemSelected;

    public void setItemSelected(ItemSelected itemSelected) {
        this.itemSelected = itemSelected;
    }

    public UOMAdapter(Context context, ArrayList<UOMModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UOMAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UOMAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.uom_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UOMAdapter.ViewHolder holder, int position) {
        UOMModel model = list.get(position);

        holder.title.setText(model.getUOM_Nm());
        holder.convFactor.setText("" + model.getCnvQty());

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, convFactor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.uomName);
            convFactor = itemView.findViewById(R.id.convFactor);
        }
    }

    public interface ItemSelected {
        void onItemSelected(UOMModel model, int position);
    }
}
