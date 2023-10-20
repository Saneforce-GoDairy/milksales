package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Model_Class.Product_Details_Modal;

import java.text.DecimalFormat;
import java.util.List;

public class Print_Invoice_Adapter extends RecyclerView.Adapter<Print_Invoice_Adapter.MyViewHolder> {
    Context context;
    List<Product_Details_Modal> mDate;
    String flag="";

    public Print_Invoice_Adapter(Context context, List<Product_Details_Modal> mDate,String flag) {
        this.flag=flag;
        this.context = context;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public Print_Invoice_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.print_invoice_recyclerview, null, false);
        return new Print_Invoice_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Print_Invoice_Adapter.MyViewHolder holder, int position) {
        try {
            Product_Details_Modal pm=mDate.get(position);
            holder.productname.setText("" + pm.getName());
            holder.productqty.setText("" + pm.getQty());
            holder.productUOM.setText("" + pm.getUnitCode());
            holder.productrate.setText("" + new DecimalFormat("##0.00").format(pm.getRate()));
            holder.producttotal.setText("" + new DecimalFormat("##0.00").format(pm.getAmount()));

            if(flag.equalsIgnoreCase("PROJECTION")){
                holder.llUom.setVisibility(View.GONE);
                holder.llPrice.setVisibility(View.GONE);
                holder.llTot.setVisibility(View.GONE);
            }


//            holder.productname.setText("" + mDate.getJSONObject(position).getString("Product_Name"));
//            holder.productqty.setText("" + mDate.getJSONObject(position).getInt("Quantity"));
//            holder.productUOM.setText("" + mDate.getJSONObject(position).getString("UOM"));
//            holder.productrate.setText("" + new DecimalFormat("##0.00").format(mDate.getJSONObject(position).getDouble("Rate")));
//            holder.producttotal.setText("" + new DecimalFormat("##0.00").format(mDate.getJSONObject(position).getDouble("value")));
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productname, productqty, productrate, producttotal, productUOM;
        LinearLayout llUom,llPrice,llTot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.productname);
            productqty = itemView.findViewById(R.id.productqty);
            productrate = itemView.findViewById(R.id.productrate);
            producttotal = itemView.findViewById(R.id.producttotal);
            productUOM = itemView.findViewById(R.id.productUom);

            llUom=itemView.findViewById(R.id.llUOM);
            llPrice=itemView.findViewById(R.id.llPrice);
            llTot=itemView.findViewById(R.id.llTot);

        }
    }
}