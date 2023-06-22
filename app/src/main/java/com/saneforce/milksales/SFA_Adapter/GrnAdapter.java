package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.GRN_Invoice;
import com.saneforce.milksales.SFA_Activity.GrnPendingActivity;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class GrnAdapter extends RecyclerView.Adapter<GrnAdapter.MyViewHolder> {

    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;
    Shared_Common_Pref sharedCommonPref;

    public GrnAdapter(Context context, List<OutletReport_View_Modal> mDate){
//            , AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
        sharedCommonPref = new Shared_Common_Pref(context);
    }

    @NonNull
    @Override
    public GrnAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.grn_list_recycleritems, null, false);

        return new GrnAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(GrnAdapter.MyViewHolder holder, int position) {

        holder.billingDate.setText("" + mDate.get(position).getBillingDate());
        holder.billingDoc.setText(mDate.get(position).getBillingId());
        holder.salesDoc.setText(mDate.get(position).getSalesId());
        holder.amount.setText("" +new DecimalFormat("##0.00").format ( mDate.get(position).getAmount()));

        if (Objects.equals(mDate.get(position).getFlag(), "1.0")){
            holder.complete.setVisibility(View.VISIBLE);
            holder.pending.setVisibility(View.GONE);

            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, GRN_Invoice.class);
                    Shared_Common_Pref.BillingID=mDate.get(position).getBillingId();
                    Shared_Common_Pref.SalesID=mDate.get(position).getSalesId();
                    Log.e("Bill_NO and Sales_NO", mDate.get(position).getBillingId() + " " + mDate.get(position).getSalesId());

                    sharedCommonPref.save(Constants.FLAG,"GRN");
//                Log.e("Sub_Total", String.valueOf(mDate.get(position).getOrderValue() + ""));
//                intent.putExtra("Order_Values", mDate.get(position).getOrderValue() + "");
//                intent.putExtra("Invoice_Values", mDate.get(position).getInvoicevalues());
                    intent.putExtra("TotQnty", mDate.get(position).getTot_qty()+ "");
                    intent.putExtra("Invoice_Date", mDate.get(position).getBillingDate()+ "");
                    intent.putExtra("Invoice_No", mDate.get(position).getGrnNo()+"");
                    intent.putExtra("NetAmount", new DecimalFormat("##0.00").format ( mDate.get(position).getAmount())+"");
                    intent.putExtra("TaxAmount", new DecimalFormat("##0.00").format ( mDate.get(position).getTax_amount())+"");

                    double x= Double.parseDouble(new DecimalFormat("##0.00").format ( mDate.get(position).getTax_amount()));
                    double y= Double.parseDouble(new DecimalFormat("##0.00").format ( mDate.get(position).getAmount()));

                    double z=x+y;
                intent.putExtra("NetTotal",new DecimalFormat("##0.00").format (z)+"");
//                    intent.putExtra("NetTax",mDate.get(position).getGrnTax()+ "");
//                intent.putExtra("Discount_Amount", mDate.get(position).getDiscount_Amount());
                    context.startActivity(intent);
//                    ((Activity)context).finish();

//                    Log.e("12345678",mDate.get(position).getNetValue().toString());

                }
            });
        }
        else{
            holder.complete.setVisibility(View.GONE);
            holder.pending.setVisibility(View.VISIBLE);

            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GrnPendingActivity.class);

                    Shared_Common_Pref.BillingID=mDate.get(position).getBillingId();
                    Shared_Common_Pref.SalesID=mDate.get(position).getSalesId();
                    Log.e("poiuyt", Shared_Common_Pref.BillingID +  Shared_Common_Pref.SalesID +"    "+mDate.get(position).getGrnDate());
                    intent.putExtra("salesID", mDate.get(position).getSalesId()+ "");
                    intent.putExtra("billingID", mDate.get(position).getBillingId()+ "");
                    intent.putExtra("grnDate",mDate.get(position).getBillingDate()+"");


                    context.startActivity(intent);
                }
            });
        }

        Log.v("wertyuio",mDate.get(position).getFlag());

//        holder.no.setText("" + mDate.get(position).getGrnNo());
//        holder.date.setText("" + mDate.get(position).getGrnDate());
//        holder.pono.setText("" + mDate.get(position).getPono());
//        holder.name.setText("" + mDate.get(position).getSuppName());
//        holder.amount.setText("" +new DecimalFormat("##0.00").format ( mDate.get(position).getGrnTotal()));
//        holder.row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Print_Invoice_Activity.class);
//                sharedCommonPref.save(Constants.FLAG, mDate.get(position).getStatus());
//                Log.e("Sub_Total", String.valueOf(mDate.get(position).getOrderValue() + ""));
//                intent.putExtra("Order_Values", mDate.get(position).getOrderValue() + "");
//                intent.putExtra("Invoice_Values", mDate.get(position).getInvoicevalues());
//                intent.putExtra("No_Of_Items", mDate.get(position).getNo_Of_items());
//                intent.putExtra("Invoice_Date", mDate.get(position).getOrderDate());
//                intent.putExtra("Invoice_No", mDate.get(position).getOrderNo());
//                intent.putExtra("NetAmount", mDate.get(position).getGrnTotal());
//                intent.putExtra("Discount_Amount", mDate.get(position).getDiscount_Amount());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView billingDoc, salesDoc, billingDate, amount,no,date,name,pono;
        LinearLayout complete,pending,row;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            billingDoc = itemView.findViewById(R.id.billingId);
            salesDoc = itemView.findViewById(R.id.salesId);
            billingDate = itemView.findViewById(R.id.billingDate);
            amount = itemView.findViewById(R.id.billingAmount);
            complete = itemView.findViewById(R.id.statusComplete);
            pending = itemView.findViewById(R.id.statusPending);
            row = itemView.findViewById(R.id.row_report);

            /*no = itemView.findViewById(R.id.grnNo);
            date = itemView.findViewById(R.id.grnDate);
            pono = itemView.findViewById(R.id.grnPONO);
            name = itemView.findViewById(R.id.grnSuppName);
            amount = itemView.findViewById(R.id.billingAmount);*/
        }
    }
}

