package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Model_Class.CommonModelWithOneString;

import java.util.ArrayList;

public class CommonAdapterForDateRange extends RecyclerView.Adapter<CommonAdapterForDateRange.ViewHolder> {
    Context context;
    ArrayList<CommonModelWithOneString> list;

    ItemSelect itemSelect;

    public CommonAdapterForDateRange(Context context, ArrayList<CommonModelWithOneString> list) {
        this.context = context;
        this.list = list;
    }

    public void setItemSelect(ItemSelect itemSelect) {
        this.itemSelect = itemSelect;
    }

    @NonNull
    @Override
    public CommonAdapterForDateRange.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommonAdapterForDateRange.ViewHolder(LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonAdapterForDateRange.ViewHolder holder, int position) {
        CommonModelWithOneString model = list.get(position);

        holder.date.setText(model.getDate());
        holder.itemView.setOnClickListener(v -> {
            if (itemSelect != null) {
                itemSelect.onItemSelected(model.getDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemSelect {
        void onItemSelected(String selectedDate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Name);
        }
    }
}
