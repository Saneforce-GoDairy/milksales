package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Activity.HAPApp;
import com.milksales.godairy.SFA_Model_Class.SalesReturnProductModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesReturnProductAdapterSubmit extends RecyclerView.Adapter<SalesReturnProductAdapterSubmit.ViewHolder> {
    Context context;
    ArrayList<SalesReturnProductModel> list;

    public SalesReturnProductAdapterSubmit(Context context, ArrayList<SalesReturnProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SalesReturnProductAdapterSubmit.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesReturnProductAdapterSubmit.ViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_return_product_item_two, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReturnProductAdapterSubmit.ViewHolder holder, int position) {
        SalesReturnProductModel model = list.get(holder.getBindingAdapterPosition());
        holder.productName.setText(list.get(holder.getBindingAdapterPosition()).getProduct_Name());
        holder.materialCode.setText(list.get(holder.getBindingAdapterPosition()).getSale_Erp_Code());
        String mrp = HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getMRP());
        holder.MRP.setText(mrp);
        String rates = HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(model.getPrice());
        holder.rate.setText(rates);
        holder.invUOM.setText(list.get(holder.getBindingAdapterPosition()).getUOM());
        holder.invQty.setText(String.valueOf(list.get(holder.getBindingAdapterPosition()).getInvQty()));
        holder.retUOM.setText(list.get(holder.getBindingAdapterPosition()).getUOM());
        holder.retQty.setText(String.valueOf(list.get(holder.getBindingAdapterPosition()).getRetQty()));
        String value = HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(list.get(holder.getBindingAdapterPosition()).getRetAmount());
        holder.retAmount.setText(value);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, materialCode, MRP, rate, invUOM, invQty, retUOM, retType, retAmount, retQty;
        ImageView retQtyMinus, retQtyPlus;
        RelativeLayout rl_retType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.ProductName);
            materialCode = itemView.findViewById(R.id.MatCode);
            MRP = itemView.findViewById(R.id.MRP);
            rate = itemView.findViewById(R.id.Rate);
            invUOM = itemView.findViewById(R.id.UOM);
            invQty = itemView.findViewById(R.id.invQty);
            retUOM = itemView.findViewById(R.id.retUOM);
            retQty = itemView.findViewById(R.id.retQty);
            retType = itemView.findViewById(R.id.tv_retType);
            retAmount = itemView.findViewById(R.id.retAmount);
            retQtyMinus = itemView.findViewById(R.id.retQtyMns);
            retQtyPlus = itemView.findViewById(R.id.retQtyPlus);
            rl_retType = itemView.findViewById(R.id.rl_retType);
        }
    }
}
