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
import com.saneforce.milksales.SFA_Activity.GRN_Print_Invoice_Activity;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;

import java.util.List;

public class GrnHistoryAdapter extends RecyclerView.Adapter<GrnHistoryAdapter.MyViewHolder> {

    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;
    Shared_Common_Pref sharedCommonPref;

    public GrnHistoryAdapter(Context context, List<OutletReport_View_Modal> mDate){
//            , AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
        sharedCommonPref = new Shared_Common_Pref(context);
    }

    @NonNull
    @Override
    public GrnHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.grn_history_recycleritems, null, false);

        return new GrnHistoryAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.no.setText("" + mDate.get(position).getGrnNo());
        holder.date.setText("" + mDate.get(position).getGrnDate());
        holder.pono.setText("" + mDate.get(position).getPono());
        holder.name.setText("" + mDate.get(position).getSuppName());
        holder.amount.setText(""+mDate.get(position).getGrnTotal());

        mDate.get(position).setGrnNo( mDate.get(position).getGrnNo());


        //holder.amount.setText("" +new DecimalFormat("##0.00").format ( mDate.get(position).getGrnTotal()));
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TRANS_SLNO", mDate.get(position).getTransSlNo());
                Shared_Common_Pref.TransSlNo = mDate.get(position).getTransSlNo();
                Shared_Common_Pref.Invoicetoorder = "1";
                Intent intent = new Intent(context, GRN_Print_Invoice_Activity.class);
                sharedCommonPref.save(Constants.FLAG,"GRN");
//                Log.e("Sub_Total", String.valueOf(mDate.get(position).getOrderValue() + ""));
//                intent.putExtra("Order_Values", mDate.get(position).getOrderValue() + "");
//                intent.putExtra("Invoice_Values", mDate.get(position).getInvoicevalues());
                intent.putExtra("No_Of_Items", mDate.get(position).getSlno()+ "");
                intent.putExtra("Invoice_Date", mDate.get(position).getGrnDate()+ "");
                intent.putExtra("Invoice_No", mDate.get(position).getGrnNo()+ "");
                intent.putExtra("NetAmount", mDate.get(position).getGrnTotal()+ "");
                intent.putExtra("Net_Tot_Amount",mDate.get(position).getTotal()+"");
                intent.putExtra("NetTax",mDate.get(position).getGrnTax()+ "");
//                intent.putExtra("Discount_Amount", mDate.get(position).getDiscount_Amount());
                context.startActivity(intent);
            }
        });

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

          /*
            billingDoc = itemView.findViewById(R.id.billingId);
            salesDoc = itemView.findViewById(R.id.salesId);
            billingDate = itemView.findViewById(R.id.billingDate);
            amount = itemView.findViewById(R.id.billingAmount);
            complete = itemView.findViewById(R.id.statusComplete);
            pending = itemView.findViewById(R.id.statusPending);*/
            row = itemView.findViewById(R.id.row_report);

            no = itemView.findViewById(R.id.grnNo);
            date = itemView.findViewById(R.id.grnDate);
            pono = itemView.findViewById(R.id.grnPONO);
            name = itemView.findViewById(R.id.grnSuppName);
            amount = itemView.findViewById(R.id.billingAmount);
        }
    }
}
