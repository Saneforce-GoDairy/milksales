package com.saneforce.godairy.SFA_Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.ChallanActivity;
import com.saneforce.godairy.SFA_Activity.TodayPrimOrdActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrimaryOrder_History_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    JSONArray mDate;
    AdapterOnClick mAdapterOnClick;

    private static final int VIEW_TYPE_ONE = 1;
    private static final int VIEW_TYPE_TWO = 2;


    public PrimaryOrder_History_Adapter(Context context, JSONArray mDate, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (mDate.getJSONObject(position).optString("Status").equalsIgnoreCase("order")) {
                return VIEW_TYPE_ONE;
            }
        } catch (JSONException ignored) { }
        return VIEW_TYPE_TWO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ONE) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.primaryorder_history_recyclerview, parent, false));
        }
        return new MyViewHolder2(LayoutInflater.from(context).inflate(R.layout.model_no_order_reason_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TWO:
                ((MyViewHolder2) holder).text.setText(mDate.optJSONObject(holder.getBindingAdapterPosition()).optString("distribute_name"));
                ((MyViewHolder2) holder).txtReason.setText(mDate.optJSONObject(holder.getBindingAdapterPosition()).optString("reason"));
                ((MyViewHolder2) holder).txtDate.setText(mDate.optJSONObject(holder.getBindingAdapterPosition()).optString("date_time"));
                return;
        }

        try {
            JSONObject obj = mDate.getJSONObject(holder.getBindingAdapterPosition());
            ((MyViewHolder) holder).txtOrderDate.setText("" + obj.getString("Order_Date"));
            ((MyViewHolder) holder).txtOrderID.setText(obj.getString("OrderNo"));
            ((MyViewHolder) holder).txtValue.setText("" + new DecimalFormat("##0.00").format(Double.parseDouble(obj.getString("Order_Value"))));
            ((MyViewHolder) holder).Itemcountinvoice.setText(obj.getString("Status"));

            String isPaid = mDate.getJSONObject(((MyViewHolder) holder).getBindingAdapterPosition()).optString("isPaid");
            if (isPaid.equalsIgnoreCase("")) {
                ((MyViewHolder) holder).payNow.setText("Pay Now");
                ((MyViewHolder) holder).payNow.setBackground(context.getResources().getDrawable(R.drawable.app_theme_button));
                ((MyViewHolder) holder).payNow.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).payNow.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setMessage("Select a payment method to continue");
                    builder.setPositiveButton("Online", (dialog, which) -> {
                        Toast.makeText(context, "...", Toast.LENGTH_SHORT).show();
                    });
                    builder.setNegativeButton("Offline", (dialog, which) -> {

                    });
                    builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.create().show();
                });
            } else if (isPaid.equalsIgnoreCase("paid")) {
                ((MyViewHolder) holder).payNow.setText("PAID");
                ((MyViewHolder) holder).payNow.setBackground(context.getResources().getDrawable(R.drawable.button_success));
                ((MyViewHolder) holder).payNow.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(context, ChallanActivity.class);
                        intent.putExtra("invoice", obj.getString("OrderNo"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                ((MyViewHolder) holder).payNow.setText("PENDING");
                ((MyViewHolder) holder).payNow.setBackground(context.getResources().getDrawable(R.drawable.button_pending));
                ((MyViewHolder) holder).payNow.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(context, ChallanActivity.class);
                        intent.putExtra("invoice", obj.getString("OrderNo"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            try {
                if (obj.getInt("editmode") == 0)
                    ((MyViewHolder) holder).llEdit.setVisibility(View.VISIBLE);
                else
                    ((MyViewHolder) holder).llEdit.setVisibility(View.GONE);
            } catch (Exception e) {

            }

            if (mDate.getJSONObject(((MyViewHolder) holder).getBindingAdapterPosition()).optDouble("Order_Value") < mDate.getJSONObject(holder.getBindingAdapterPosition()).optDouble("lastOrderedValue")) {
                ((MyViewHolder) holder).lowOrder.setVisibility(View.VISIBLE);
            } else {
                ((MyViewHolder) holder).lowOrder.setVisibility(View.GONE);
            }


            ((MyViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterOnClick.onIntentClick(holder.getBindingAdapterPosition());
                }
            });

            ((MyViewHolder) holder).llEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mAdapterOnClick.onEditOrder(mDate.getJSONObject(holder.getBindingAdapterPosition()).getString("Trans_Sl_No"),
                                mDate.getJSONObject(holder.getBindingAdapterPosition()).getString("cutoff_time"),
                                mDate.getJSONObject(holder.getBindingAdapterPosition()).getString("category_type"));
                    } catch (Exception e) {

                    }

                }
            });

            ((MyViewHolder) holder).tvCutoff.setText("Cutoff Time:" + obj.getString("cutoff_time"));

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

    public static class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView text, txtReason, txtDate;
        CardView layout;

        public MyViewHolder2(View view) {
            super(view);
            text = view.findViewById(R.id.txt_name);
            layout = view.findViewById(R.id.layout);
            txtReason = view.findViewById(R.id.txt_reason);
            txtDate = view.findViewById(R.id.txt_date);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderID, txtValue, Itemcountinvoice, tvCutoff, payNow;
        LinearLayout linearLayout, llEdit;
        View lowOrder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);
            llEdit = itemView.findViewById(R.id.llEdit);
            tvCutoff = itemView.findViewById(R.id.tvCutOffTime);
            lowOrder = itemView.findViewById(R.id.lowOrder);
            payNow = itemView.findViewById(R.id.payNow);
        }
    }
}
