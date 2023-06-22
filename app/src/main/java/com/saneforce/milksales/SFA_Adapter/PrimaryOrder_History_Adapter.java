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

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.TodayPrimOrdActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PrimaryOrder_History_Adapter extends RecyclerView.Adapter<PrimaryOrder_History_Adapter.MyViewHolder> {

    Context context;
    JSONArray mDate;
    AdapterOnClick mAdapterOnClick;


    public PrimaryOrder_History_Adapter(Context context, JSONArray mDate, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;

    }

    @NonNull
    @Override
    public PrimaryOrder_History_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.primaryorder_history_recyclerview, null, false);

        return new PrimaryOrder_History_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(PrimaryOrder_History_Adapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mDate.getJSONObject(position);
            holder.txtOrderDate.setText("" + obj.getString("Order_Date"));
            holder.txtOrderID.setText(obj.getString("OrderNo"));
            holder.txtValue.setText("" + new DecimalFormat("##0.00").format(Double.parseDouble(obj.getString("Order_Value"))));
            holder.Itemcountinvoice.setText(obj.getString("Status"));


            try {
                if (obj.getInt("editmode") == 0)
                    holder.llEdit.setVisibility(View.VISIBLE);
                else
                    holder.llEdit.setVisibility(View.GONE);
            } catch (Exception e) {

            }


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterOnClick.onIntentClick(position);
                }
            });

            holder.llEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TodayPrimOrdActivity.mTdPriAct.updateData(mDate.getJSONObject(position).getString("Trans_Sl_No"),
                                mDate.getJSONObject(position).getString("cutoff_time"), mDate.getJSONObject(position).getString("category_type"));
                    } catch (Exception e) {

                    }

                }
            });

            holder.tvCutoff.setText("Cutoff Time:" + obj.getString("cutoff_time"));

        } catch (Exception e) {
            Log.v("primAdapter:", e.getMessage());
        }
    }

    public int isToday(String date) {
        int result = -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            String plantime = sdf.format(c.getTime());
            Date date2 = sdf.parse(plantime);
            result = date1.compareTo(date2);
        } catch (Exception e) {

        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mDate.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderID, txtValue, Itemcountinvoice, tvCutoff;
        LinearLayout linearLayout, llEdit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);
            llEdit = itemView.findViewById(R.id.llEdit);
            tvCutoff = itemView.findViewById(R.id.tvCutOffTime);


        }
    }
}
