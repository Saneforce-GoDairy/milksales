package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;


import java.util.List;

public class Complete_Order_Adapter extends RecyclerView.Adapter<Complete_Order_Adapter.MyViewHolder> {

    private List<Outlet_Orders_Alldays> Outlet_Orders_Alldaysitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, status, invoicevalue, invoicedate;
        LinearLayout parent_layout;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.retailername);
            parent_layout = view.findViewById(R.id.parent_layout);
            status = view.findViewById(R.id.status);
            invoicevalue = view.findViewById(R.id.invoicevalue);
            invoicedate = view.findViewById(R.id.invoicedate);
        }
    }


    public Complete_Order_Adapter(List<Outlet_Orders_Alldays> Outlet_Orders_Alldaysitem, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Outlet_Orders_Alldaysitem = Outlet_Orders_Alldaysitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public Complete_Order_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Complete_Order_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Complete_Order_Adapter.MyViewHolder holder, int position) {
        Outlet_Orders_Alldays Outlet_Orders_Alldays = Outlet_Orders_Alldaysitem.get(position);
        holder.textviewname.setText("" + Outlet_Orders_Alldays.getOutletName().toUpperCase() + "~" + Outlet_Orders_Alldays.getOutletCode());
        if (Outlet_Orders_Alldays.getStatus() != null) {
            holder.status.setText("Status :" + "\t\t" + Outlet_Orders_Alldays.getStatus().toUpperCase());
        } else {
            holder.status.setText("Status :" + "\t\t" + "");
        }

        holder.invoicevalue.setText("value :" + "\t\t" + Outlet_Orders_Alldays.getInvoicevalues());
        holder.invoicedate.setText("invoice date :" + "\t\t" + Outlet_Orders_Alldays.getOrderDate());


        holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Outlet_Orders_Alldaysitem.size();
    }
}