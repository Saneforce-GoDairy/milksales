package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class rvVanStockview  extends RecyclerView.Adapter<rvVanStockview.MyViewHolder>{
    JSONArray jLists;
    int RowLayout;
    Context context;

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
            JSONObject jItem=jLists.getJSONObject(position);
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
//            }
        } catch (Exception e) {
            Log.v("RouteAdapter: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return jLists.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDt, tvLoadAmt, tvUnLoadAmt, tvTotVanSalQty, tvTotStkQty;
        RecyclerView rvVanSales;
        LinearLayout parent_layout;

        public MyViewHolder(View view) {
            super(view);
            try {
                parent_layout = view.findViewById(R.id.parent_layout);

                rvVanSales = itemView.findViewById(R.id.rvVanSal);
                tvDt = itemView.findViewById(R.id.tvVSPayDate);

                tvTotVanSalQty = itemView.findViewById(R.id.tvTotSalQty);
                tvTotStkQty = itemView.findViewById(R.id.tvTotLoadQty);


            } catch (Exception e) {
                Log.e("RouteAdapter:holder ", e.getMessage());
            }
        }
    }
}
