package com.saneforce.milksales.SFA_Adapter;

import static com.saneforce.milksales.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class DashboardInfoAdapter extends RecyclerView.Adapter<DashboardInfoAdapter.MyViewHolder> {
    Context context;
    JSONArray mDate;
    private View listItem;
    int rowlayout;

    String DistName = "";
    Common_Class common_class;

    public DashboardInfoAdapter(Context context, JSONArray mDate, int rowlayout) {
        this.context = context;
        this.mDate = mDate;
        this.rowlayout = rowlayout;
        common_class = new Common_Class(context);

    }


    @NonNull
    @Override
    public DashboardInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new DashboardInfoAdapter.MyViewHolder(listItem);
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
    public void onBindViewHolder(DashboardInfoAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject mObj = mDate.getJSONObject(position);
            holder.tvOutletName.setText(mObj.getString("OutletName"));

            JSONArray arr = mObj.getJSONArray("Products");
            String pro_name = "";
            for (int i = 0; i < arr.length(); i++) {
                JSONObject proObj = arr.getJSONObject(i);
                if (i + 1 == arr.length())
                    pro_name = pro_name + proObj.getString("Product_Name") + " x " + proObj.getInt("Quantity");
                else
                    pro_name = pro_name + proObj.getString("Product_Name") + " x " + proObj.getInt("Quantity") + ", ";

            }

            if (pro_name.equals(""))
                holder.tvProductName.setVisibility(View.GONE);
            else
                holder.tvProductName.setVisibility(View.VISIBLE);
//            if (DistName.contains(mObj.getString("FranchiseName")))
//                holder.tvDistName.setVisibility(View.GONE);
//            else {
            // DistName = DistName + mObj.getString("FranchiseName");

            holder.tvDistName.setText(mObj.getString("FranchiseName"));
            //}

            if (Shared_Common_Pref.SALES_MODE.equals("noorder")) {
                holder.tvStatus.setText(mObj.getString("Remarks"));
                holder.tvAmount.setVisibility(View.GONE);
                holder.tvId.setVisibility(View.GONE);
            } else {
                holder.tvStatus.setVisibility(View.GONE);
                holder.tvAmount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(mObj.getDouble("TransactionAmt")));
                holder.tvId.setText(mObj.getString("TransactionNo"));
            }

            holder.tvDistAdd.setText("" + mObj.getString("Address"));
            holder.tvDistPh.setText("" + mObj.getString("Mobile"));
            holder.tvDateTime.setText("" + mObj.getString("Date_Time"));
            holder.tvProductName.setText(pro_name);
            holder.llDistCall.setVisibility(View.VISIBLE);
            if (Common_Class.isNullOrEmpty(holder.tvDistPh.getText().toString()))
                holder.llDistCall.setVisibility(View.GONE);

            holder.llDistCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        common_class.showCalDialog(context, "Do you want to Call this Franchise?", holder.tvDistPh.getText().toString().replaceAll(",", ""));
                    } catch (Exception e) {
                        Log.v("Call:Outlet:", e.getMessage());
                    }
                }
            });


        } catch (Exception e) {
            Log.e("History_Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDate.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvStatus, tvAmount, tvOutletName, tvDistName, tvDistAdd, tvDistPh, tvDateTime, tvProductName;
        ImageView ivStatus;
        LinearLayout llDistCall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutletName = itemView.findViewById(R.id.retailername);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvDistName = itemView.findViewById(R.id.tvDistributer);
            tvDistAdd = itemView.findViewById(R.id.tvDistAdd);
            tvDistPh = itemView.findViewById(R.id.txMobile);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            llDistCall = itemView.findViewById(R.id.btnCallMob);


        }
    }
}