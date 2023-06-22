package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.Outlet_Report_Product_Details_Modal;

import java.util.List;

public class Outlet_Report_ProductDetails_Adapter extends RecyclerView.Adapter<Outlet_Report_ProductDetails_Adapter.MyViewHolder> {
    Context context;
    List<Outlet_Report_Product_Details_Modal> mDate;

    public Outlet_Report_ProductDetails_Adapter(Context context, List<Outlet_Report_Product_Details_Modal> mDate) {
        this.context = context;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public Outlet_Report_ProductDetails_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.report_product_details_view, null, false);
        return new Outlet_Report_ProductDetails_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Outlet_Report_ProductDetails_Adapter.MyViewHolder holder, int position) {
        holder.txtName.setText(" " + mDate.get(position).getProductName());
        holder.txtQty.setText(" " + mDate.get(position).getQty());
        holder.txtRate.setText(" " + mDate.get(position).getRate());
        holder.txtTotal.setText(" " + mDate.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQty, txtRate, txtTotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtQty = itemView.findViewById(R.id.txt_qty);
            txtRate = itemView.findViewById(R.id.txt_rate);
            txtTotal = itemView.findViewById(R.id.txt_total);
        }
    }
}
