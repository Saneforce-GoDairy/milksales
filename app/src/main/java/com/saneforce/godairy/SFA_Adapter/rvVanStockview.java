package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class rvVanStockview  extends RecyclerView.Adapter<rvVanStockview.MyViewHolder>{
    JSONArray jLists;
    int RowLayout;
    Context context;
    NumberFormat formatter = new DecimalFormat("##0.00");
    public rvVanStockview(JSONArray jList, int rowLayout, Context mcontext){
        jLists=jList;
        RowLayout=rowLayout;
        context = mcontext;
    }

    @Override
    public rvVanStockview.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(RowLayout, parent, false);
        return new rvVanStockview.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rvVanStockview.MyViewHolder holder, int position) {
        try{
           /* JSONObject jItem=jLists.getJSONObject(position);
            try {
                double salAmt = 0;
                int totStk = 0;
                int totSal = 0;

                holder.tvDt.setText("Date : " + jItem.getString("Dt"));
                //holder.tvLoadAmt.setText(CurrencySymbol+" " + formatter.format(getIntent().getDoubleExtra("stkLoadAmt", 0)));
                JSONArray arr = jItem.getJSONArray("Details");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    totStk += obj.getInt("Cr");
                    totSal += obj.getInt("Dr");

                }
                holder.tvTotVanSalQty.setText("" + totSal);
                holder.tvTotStkQty.setText("" + totStk);


                //  tvUnLoadAmt.setText(CurrencySymbol+" " + formatter.format(getIntent().getDoubleExtra("stkLoadAmt", 0) - salAmt));
                holder.rvVanSales.setAdapter(new Pay_Adapter(arr, R.layout.adapter_vansales_stockview, context));

            } catch (Exception e) {
                Log.v("adap:", e.getMessage());
            }
//            holder.tvDate.setText(jItem.getString("LedgDate"));
//            holder.tvDebit.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(jItem.getDouble("Debit")));
//            holder.tvCredit.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(jItem.getDouble("Credit")));
//            holder.tvBal.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(jItem.getDouble("Balance")));
//            if(jItem.getDouble("Balance")>=0){
//                holder.tvBal.setTextColor(context.getResources().getColor(R.color.greentext));
//            }else{
//                holder.tvBal.setTextColor(context.getResources().getColor(R.color.color_red));
//            }*/

            JSONObject jItem=jLists.getJSONObject(position);
            try {

                int totStk = 0;
                int totSal = 0;
                int totUnload=0;
                int totTopUp=0;
                double totLoadAmt=0;
                double totTopupAmt=0;
                double totSalAmt = 0;
                double totUnLoadAmt=0;

                holder.tvDt.setText("Date : " + jItem.getString("Dt"));
                //holder.tvLoadAmt.setText(CurrencySymbol+" " + formatter.format(getIntent().getDoubleExtra("stkLoadAmt", 0)));
                JSONArray arr = jItem.getJSONArray("Details");
                JSONArray arrData=jItem.getJSONArray("VanData");

                if(arrData.length()>0){
                    holder.ll_vanData.setVisibility(View.VISIBLE);
                    JSONObject objNew = arrData.getJSONObject(0);
                    holder.tvsalesManName.setText(""+objNew.getString("salesManName"));
                    holder.tvVehicleNo.setText(""+objNew.getString("vehNo"));
                    double srtKm=objNew.getDouble("srtKm");
                    double endKm=objNew.getDouble("endKm");
                    if(srtKm>0) {holder.tvSrtKm.setText("" +srtKm );}else{holder.tvSrtKm.setText("-");}
                    if(endKm>0) {
                        holder.tvEndKm.setText("" +endKm );
                        holder.tvCheckOutDate.setText(""+objNew.getString("vanDate"));
                        holder.tvCheckOutTime.setText(""+objNew.getString("checkoutTime"));
                    }else{
                        holder.tvEndKm.setText("-");
                        holder.tvCheckOutDate.setText("-");
                        holder.tvCheckOutTime.setText("-");
                    }
                    holder.tvCheckInDate.setText(""+objNew.getString("vanDate"));
                    holder.tvCheckInTime.setText(""+objNew.getString("checkInTime"));
                    if(endKm==0){
                        holder.tvTotalKm.setText("Closing Not Done");
                    }else{
                        double totkm=endKm-srtKm;
                        if(totkm<0) {
                            holder.tvTotalKm.setText("Closing Not Done");
                        }else {
                            holder.tvTotalKm.setText("" + totkm);
                        }
                    }
                }else{
                    holder.ll_vanData.setVisibility(View.GONE);
                }

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    totStk += obj.getInt("loadCr");
                    totSal += obj.getInt("salDr");
                    totTopUp += obj.getInt("topupCr");
                    totUnload += obj.getInt("unloadDr");
                    totLoadAmt+=obj.getDouble("CrAmt");
                    totTopupAmt+=obj.getDouble("tCrAmt");
                    totSalAmt+=obj.getDouble("DrAmt");
                    totUnLoadAmt+=obj.getDouble("eDrAmt");



                }
                holder.tvTotVanSalQty.setText("" + totSal);
                holder.tvTotStkQty.setText("" + totStk);
                holder.tvTotTopUpQty.setText(""+totTopUp);
                holder.tvTotUnLoadQty.setText(""+totUnload);
                holder.tvTotStkVal.setText(""+ formatter.format(totLoadAmt));
                holder.tvTotTopUpVal.setText(""+ formatter.format(totTopupAmt));
                holder.tvTotVanSalVal.setText(""+ formatter.format(totSalAmt));
                holder.tvTotUnLoadVal.setText(""+ formatter.format(totUnLoadAmt));


                //  tvUnLoadAmt.setText(CurrencySymbol+" " + formatter.format(getIntent().getDoubleExtra("stkLoadAmt", 0) - salAmt));
                holder.rvVanSales.setAdapter(new Pay_Adapter(arr, R.layout.adapter_vansales_stockview, context));

            } catch (Exception e) {
                Log.v("adap:", e.getMessage());
            }
        } catch (Exception e) {
            Log.v("RouteAdapter: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return jLists.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /* TextView tvDt, tvLoadAmt, tvUnLoadAmt, tvTotVanSalQty, tvTotStkQty;
        RecyclerView rvVanSales;
        LinearLayout parent_layout;*/
        TextView tvDt, tvLoadAmt, tvUnLoadAmt, tvTotVanSalQty, tvTotStkQty,tvTotTopUpQty,tvTotUnLoadQty;//tvTotLoad;
        TextView tvTotVanSalVal, tvTotStkVal,tvTotTopUpVal,tvTotUnLoadVal;
        TextView tvsalesManName,tvVehicleNo,tvSrtKm,tvCheckInDate,tvCheckInTime,tvEndKm,tvCheckOutDate,tvCheckOutTime,tvTotalKm;
        RecyclerView rvVanSales;
        LinearLayout parent_layout,ll_vanData;

        public MyViewHolder(View view) {
            super(view);
            try {
              /*  parent_layout = view.findViewById(R.id.parent_layout);

                rvVanSales = itemView.findViewById(R.id.rvVanSal);
                tvDt = itemView.findViewById(R.id.tvVSPayDate);

                tvTotVanSalQty = itemView.findViewById(R.id.tvTotSalQty);
                tvTotStkQty = itemView.findViewById(R.id.tvTotLoadQty);*/
                parent_layout = view.findViewById(R.id.parent_layout);

                rvVanSales = itemView.findViewById(R.id.rvVanSal);
                tvDt = itemView.findViewById(R.id.tvVSPayDate);

                tvTotVanSalQty = itemView.findViewById(R.id.tvTotSalQty);
                tvTotStkQty = itemView.findViewById(R.id.tvTotLoadQty);
                tvTotTopUpQty = itemView.findViewById(R.id.tvTotTopUpQty);
                tvTotUnLoadQty = itemView.findViewById(R.id.tvTotUnLoadQty);
                tvTotVanSalVal = itemView.findViewById(R.id.tvTotSalVal);
                tvTotStkVal = itemView.findViewById(R.id.tvTotLoadVal);
                tvTotTopUpVal = itemView.findViewById(R.id.tvTotTopUpVal);
                tvTotUnLoadVal = itemView.findViewById(R.id.tvTotUnLoadVal);
                tvsalesManName = itemView.findViewById(R.id.tvsalesManName);
                tvVehicleNo=itemView.findViewById(R.id.tvVehicleNo);
                tvSrtKm = itemView.findViewById(R.id.tvSrtKm);
                tvCheckInDate= itemView.findViewById(R.id.tvCheckInDate);
                tvCheckInTime = itemView.findViewById(R.id.tvCheckInTime);
                tvEndKm= itemView.findViewById(R.id.tvEndKm);
                tvCheckOutDate= itemView.findViewById(R.id.tvCheckOutDate);
                tvCheckOutTime= itemView.findViewById(R.id.tvCheckOutTime);
                tvTotalKm= itemView.findViewById(R.id.tvTotalKm);
                ll_vanData=itemView.findViewById(R.id.ll_vanData);


            } catch (Exception e) {
                Log.e("RouteAdapter:holder ", e.getMessage());
            }
        }
    }
}
