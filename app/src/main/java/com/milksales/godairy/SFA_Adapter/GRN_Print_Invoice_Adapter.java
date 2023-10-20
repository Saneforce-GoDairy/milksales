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
import com.milksales.godairy.SFA_Model_Class.OutletReport_View_Modal;

import java.text.DecimalFormat;
import java.util.List;

public class GRN_Print_Invoice_Adapter extends RecyclerView.Adapter<GRN_Print_Invoice_Adapter.MyViewHolder> {
    Context context;
    List<OutletReport_View_Modal> mDate;
    String flag="";

    public GRN_Print_Invoice_Adapter(Context context, List<OutletReport_View_Modal> mDate) {
        this.flag=flag;
        this.context = context;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public GRN_Print_Invoice_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.grn_print_invoice_recyclerview, null, false);
        return new GRN_Print_Invoice_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(GRN_Print_Invoice_Adapter.MyViewHolder holder, int position) {
        holder.productname.setText(mDate.get(position).getProductName());
        holder.productqty.setText(mDate.get(position).getBilledQty());
        holder.productrate.setText(new DecimalFormat("##0.00").format (mDate.get(position).getRate())+"");
        holder.productUOM.setText(mDate.get(position).getPunit());
        holder.producttotal.setText(mDate.get(position).gettValue());

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
            productname = itemView.findViewById(R.id.grnproductname);
            productqty = itemView.findViewById(R.id.grnproductqty);
            productrate = itemView.findViewById(R.id.grnproductrate);
            producttotal = itemView.findViewById(R.id.grn_producttotal);
            productUOM = itemView.findViewById(R.id.grnproductUom);

            llUom=itemView.findViewById(R.id.llUOM);
            llPrice=itemView.findViewById(R.id.llPrice);
            llTot=itemView.findViewById(R.id.llTot);

        }
    }
}