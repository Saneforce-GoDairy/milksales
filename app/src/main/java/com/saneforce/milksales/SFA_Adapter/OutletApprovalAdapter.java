package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Activity_Hap.AddNewRetailer;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class OutletApprovalAdapter extends RecyclerView.Adapter<OutletApprovalAdapter.MyViewHolder> {
    Context context;
    JSONArray mArr;

    private View listItem;
    int rowlayout;


    public OutletApprovalAdapter(Context context, JSONArray mArr, int rowlayout) {
        this.context = context;
        this.mArr = mArr;
        this.rowlayout = rowlayout;


    }


    @NonNull
    @Override
    public OutletApprovalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new OutletApprovalAdapter.MyViewHolder(listItem);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(OutletApprovalAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvDate.setText("" + obj.getString("date"));
            holder.tvOutletName.setText("" + obj.getString("name"));
            holder.tvOwnerName.setText("" + obj.getString("ho"));
            holder.tvAdd.setText("" + obj.getString("des"));

            holder.tvView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddNewRetailer.class);
                    intent.putExtra("approval", "status");
//                    if (!Shared_Common_Pref.OutletCode.equalsIgnoreCase("OutletCode")) {
//                        Shared_Common_Pref.Editoutletflag = "1";
//                        //  Shared_Common_Pref.OutletCode = String.valueOf(Retailer_Modal_ListFilter.get(position).getId());
//                        intent.putExtra("OutletCode", Shared_Common_Pref.OutletCode);
//                        intent.putExtra("OutletName", Shared_Common_Pref.OutletName);
//                        intent.putExtra("OutletAddress", Shared_Common_Pref.OutletAddress);
//                        intent.putExtra("OutletMobile", "9876543210");
//                        intent.putExtra("OutletRoute", Shared_Common_Pref.Route_name);
//                    }

                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("OutletApprovalAdapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mArr.length();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvOwnerName, tvOutletName, tvAdd, tvPhone;
        TextView tvView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutletName = itemView.findViewById(R.id.tvOutletName);
            tvDate = itemView.findViewById(R.id.tvCreateDate);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            tvAdd = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvView = itemView.findViewById(R.id.tvView);

        }
    }
}