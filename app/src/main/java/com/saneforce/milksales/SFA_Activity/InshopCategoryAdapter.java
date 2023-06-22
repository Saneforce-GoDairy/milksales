package com.saneforce.milksales.SFA_Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import java.util.ArrayList;

public class InshopCategoryAdapter extends RecyclerView.Adapter<InshopCategoryAdapter.MyViewHolder>{

    private Context context;
    private ArrayList list;

    public InshopCategoryAdapter(Context context, ArrayList listItems){
        this.context = context;
        this.list = listItems;
    }

    @NonNull
    @Override
    public InshopCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        v=layoutInflater.inflate(R.layout.inshop_category_items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InshopCategoryAdapter.MyViewHolder holder, int position) {
        holder.category.setText((String) list.get(position));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                holder.count++;
                holder.qty.setText(""+holder.count);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(holder.count<=0) holder.count=0;

                else
                    holder.count--;
                holder.qty.setText(""+holder.count);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView category,plus,minus,qty;
        int count=0;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.tvInshopCategory);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            qty = itemView.findViewById(R.id.qtyCount);
        }
    }
}
