package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Model_Class.CommonModelWithFourString;

import java.util.ArrayList;

public class CommonAdapterForDropdownWithFilter extends RecyclerView.Adapter<CommonAdapterForDropdownWithFilter.ViewHolder> {
    ArrayList<CommonModelWithFourString> list;
    Context context;

    SelectItem selectItem;

    public void setSelectItem(SelectItem selectItem) {
        this.selectItem = selectItem;
    }

    public CommonAdapterForDropdownWithFilter(ArrayList<CommonModelWithFourString> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.common_list_item_with_two_textview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommonModelWithFourString model = list.get(position);
        holder.id.setText("[" + model.getId() + "]");
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
        TextView title, id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name);
            id = itemView.findViewById(R.id.ID);
        }
    }

    public interface SelectItem {
        void onItemSelected(CommonModelWithFourString model, int position);
    }
}
