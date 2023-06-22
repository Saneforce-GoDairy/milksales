package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class Lead_Adapter extends RecyclerView.Adapter<Lead_Adapter.MyViewHolder> {
    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView outletname, Outlet_Code, Productcategory, Productnamedate, outletAddress, invoice;
        LinearLayout parent_layout;

        public MyViewHolder(View view) {
            super(view);
            outletname = view.findViewById(R.id.outletname);
            Outlet_Code = view.findViewById(R.id.Outlet_Code);
            Productcategory = view.findViewById(R.id.Productcategory);
            Productnamedate = view.findViewById(R.id.Productnamedate);
            outletAddress = view.findViewById(R.id.outletAddress);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public Lead_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Lead_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Lead_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Lead_Adapter.MyViewHolder holder, int position) {
        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);
        holder.outletname.setText("" + Retailer_Modal_List.getName().toUpperCase());
        holder.Outlet_Code.setText("Outlet Code:" + "\t\t" + Retailer_Modal_List.getId());
        holder.Productcategory.setText("PRODUCT:" + "\t\t" + "FRESH");
        holder.Productnamedate.setText("" + Retailer_Modal_List.getId());
        holder.outletAddress.setText("" + Retailer_Modal_List.getListedDrAddress1());
       // Log.e("LastUpdt_Date_Server", Retailer_Modal_List.getName().toUpperCase() + ":" + Retailer_Modal_List.getLastUpdt_Date());
        //Log.e("LastUpdt_Date_Mobile", Retailer_Modal_List.getName().toUpperCase() + ":" + Common_Class.GetDatewothouttime());
        if (Retailer_Modal_List.getLastUpdt_Date() != null && Retailer_Modal_List.getLastUpdt_Date().equals(Common_Class.GetDatewothouttime())   ) {
            holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
        }
    }
    @Override
    public int getItemCount() {
        return Retailer_Modal_Listitem.size();
    }
}
