package com.saneforce.godairy.SFA_Adapter;

import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class CatewiseSalesaAdapter extends RecyclerView.Adapter<CatewiseSalesaAdapter.MyViewHolder> {
    JSONArray AryDta;
    private Context context;
    int salRowDetailLayout;
    public CatewiseSalesaAdapter(JSONArray jAryDta,int rowLayout,Context mContext) {
        AryDta=jAryDta;
        context=mContext;
        salRowDetailLayout=rowLayout;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JSONObject itm=AryDta.getJSONObject(position);
            holder.txCatname.setText(itm.getString("name"));

            if(itm.has("Qty")) {
                holder.txQty.setText(itm.getString("Qty"));
                holder.txVal.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(itm.getDouble("Val")));
            }else if(itm.has("Vals")){
                JSONArray itmv=itm.getJSONArray("Vals");
                if(itmv.length()>0) {
                    if(itmv.getJSONObject(0).has("OQty")) {
                        holder.txOQty.setText(itmv.getJSONObject(0).getString("OQty"));
                    }
                    if(itmv.getJSONObject(0).has("NQty")) {
                        holder.txNQty.setText(itmv.getJSONObject(0).getString("NQty"));
                    }
                    holder.txQty.setText(itmv.getJSONObject(0).getString("Qty"));
                    holder.txVal.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(itmv.getJSONObject(0).getDouble("Val")));
                }else{
                    holder.txQty.setText("0");
                    holder.txVal.setText(CurrencySymbol+" 0.00");
                }
            } else{
                holder.txQty.setText("0");
                holder.txVal.setText(CurrencySymbol+" 0.00");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return AryDta.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txCatname,txOQty,txNQty,txQty,txVal;
        LinearLayout parent_layout;
        public MyViewHolder(View view) {
            super(view);
            parent_layout = view.findViewById(R.id.parent_layout);
            txCatname=view.findViewById(R.id.txCateName);
            txOQty=view.findViewById(R.id.tvTodayOQty);
            txNQty=view.findViewById(R.id.tvTodayNOQty);
            txQty=view.findViewById(R.id.tvTodayQty);
            txVal=view.findViewById(R.id.tvTodayVal);
        }
    }
}
