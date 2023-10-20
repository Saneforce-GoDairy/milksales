package com.milksales.godairy.SFA_Adapter;

import static com.milksales.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class PayLedger_Adapter extends RecyclerView.Adapter<PayLedger_Adapter.MyViewHolder> {

    Context context;
    JSONArray mArr;

    public PayLedger_Adapter(Context context, JSONArray arr) {
        this.context = context;
        this.mArr = arr;
    }

    @NonNull
    @Override
    public PayLedger_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ledger_layout, parent, false);
        return new PayLedger_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PayLedger_Adapter.MyViewHolder holder, int position) {
        try {
            JSONObject item=mArr.getJSONObject(position);
            holder.txCustNm.setText(item.getString("CustName"));
            holder.txtOBAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(item.getDouble("OBAmt")));
            holder.txtCBAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(item.getDouble("ClAmt")));
            holder.txtRecNo.setVisibility(View.VISIBLE);
            if (item.getJSONArray("Details").length()>0){
                holder.txtRecNo.setVisibility(View.GONE);
            }
            holder.lstLdgrView.setAdapter(new rcylPayledgerAda(item.getJSONArray("Details"),R.layout.rcyl_ledger_detail,context));
            if(item.getDouble("OBAmt")>=0){
                holder.txtOBAmt.setTextColor(context.getResources().getColor(R.color.greentext));
            }else{
                holder.txtOBAmt.setTextColor(context.getResources().getColor(R.color.color_red));
            }
            if(item.getDouble("ClAmt")>=0){
                holder.txtCBAmt.setTextColor(context.getResources().getColor(R.color.greentext));
            }else{
                holder.txtCBAmt.setTextColor(context.getResources().getColor(R.color.color_red));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mArr.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txCustNm,txtOBAmt,txtCBAmt,txtRecNo;
        RecyclerView lstLdgrView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txCustNm=itemView.findViewById(R.id.txtCustNm);
            lstLdgrView=itemView.findViewById(R.id.ryclLedger);
            txtOBAmt=itemView.findViewById(R.id.txtOBAmt);
            txtCBAmt=itemView.findViewById(R.id.txtCBAmt);
            txtRecNo=itemView.findViewById(R.id.txtRecNo);

        }
    }
}
