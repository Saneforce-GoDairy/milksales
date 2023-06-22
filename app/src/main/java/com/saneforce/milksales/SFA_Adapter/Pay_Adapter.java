package com.saneforce.milksales.SFA_Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class Pay_Adapter extends RecyclerView.Adapter<Pay_Adapter.MyViewHolder> {
    Context context;
    private JSONArray arr;
    private int rowLayout;


    public Pay_Adapter(JSONArray arr, int rowLayout, Context context) {
        this.arr = arr;
        this.rowLayout = rowLayout;
        this.context = context;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {


            JSONObject obj = arr.getJSONObject(position);

            try {
                holder.tvSku.setText("" + (obj.getString("PName")));
            } catch (Exception e) {

            }

            holder.tvSalQty.setText(String.valueOf("" + obj.getInt("Dr")).toString().trim());

            holder.tvStkQty.setText("" + (obj.getInt("Cr")));


        } catch (Exception e) {
            Log.e("adapterProduct: ", e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return arr.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSku, tvStkQty, tvSalQty;


        public MyViewHolder(View view) {
            super(view);
            tvSku = view.findViewById(R.id.tvSku);
            tvStkQty = view.findViewById(R.id.tvStockQty);
            tvSalQty = view.findViewById(R.id.tvSalQty);

        }
    }


}
