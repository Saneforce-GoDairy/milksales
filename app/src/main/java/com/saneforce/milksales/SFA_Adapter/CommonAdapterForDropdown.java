package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.CommonModelForDropDown;

import java.util.ArrayList;

public class CommonAdapterForDropdown extends RecyclerView.Adapter<CommonAdapterForDropdown.ViewHolder> {
    ArrayList<CommonModelForDropDown> list;
    Context context;

    SelectItem selectItem;

    public void setSelectItem(SelectItem selectItem) {
        this.selectItem = selectItem;
    }

    public CommonAdapterForDropdown(ArrayList<CommonModelForDropDown> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommonModelForDropDown model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (selectItem != null) {
                selectItem.onItemSelected(model, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name);
        }
    }

    public interface SelectItem {
        void onItemSelected(CommonModelForDropDown model, int position);
    }
}
