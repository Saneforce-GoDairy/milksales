package com.saneforce.milksales.SFA_Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import java.util.ArrayList;

public class InshopBrandAdapter extends RecyclerView.Adapter<InshopBrandAdapter.MyViewHolder> {

    private Context context;
    private ArrayList list;

    public InshopBrandAdapter(Context context, ArrayList listItems){
        this.context = context;
        this.list = listItems;
    }

    @NonNull
    @Override
    public InshopBrandAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        v=layoutInflater.inflate(R.layout.inshop_brand_items,parent,false);
        return new MyViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull InshopBrandAdapter.MyViewHolder holder, int position) {
        holder.brand.setText((String) list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView brand;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            brand = itemView.findViewById(R.id.tvInshopBrandname);
        }
    }
}
