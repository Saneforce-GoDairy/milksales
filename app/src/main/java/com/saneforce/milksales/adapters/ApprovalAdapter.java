package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.R;

import java.text.DecimalFormat;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.ViewHolder> {
    private static final String TAG = "ShiftList";
    private JsonArray mlist = new JsonArray();
    private Context mContext;
    //static onPayslipItemClick payClick;
    public ApprovalAdapter(JsonArray mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ApprovalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ledger_layout, parent, false);
        ApprovalAdapter.ViewHolder holder = new ApprovalAdapter.ViewHolder(view);
        return holder;

    }
   /* public static void SetPayOnClickListener(onPayslipItemClick mPayClick){
        payClick=mPayClick;
    }*/
    @Override
    public void onBindViewHolder(@NonNull ApprovalAdapter.ViewHolder holder, int position) {

        JsonObject itm = null;
        try {
            itm = mlist.get(position).getAsJsonObject();
            holder.date.setText(itm.get("date").getAsString());
            foodGrid adFdtyp=new foodGrid(itm.get("FoodDet").getAsJsonArray(),mContext);
            holder.fdGrid.setAdapter(adFdtyp);
            holder.amount.setText("Rs. "+ new DecimalFormat("##0.00").format(Double.valueOf(itm.get("amount").getAsString())));

//            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    JsonObject itm = null;
//                    try {
//                        itm = mlist.get(position).getAsJsonObject();
//                        payClick.onClick(itm);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,amount;
        GridView fdGrid;
        LinearLayout parentLayout;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_date);
            fdGrid = itemView.findViewById(R.id.grdViewFdet);
            amount = itemView.findViewById(R.id.Amount);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}