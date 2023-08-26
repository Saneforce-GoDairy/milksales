package com.saneforce.milksales.SFA_Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.ChallanActivity;
import com.saneforce.milksales.SFA_Activity.TodayPrimOrdActivity;
import com.saneforce.milksales.SFA_Model_Class.CoolerPositionModel;

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

            String isPaid = mDate.getJSONObject(holder.getBindingAdapterPosition()).optString("isPaid");
            if (isPaid.equalsIgnoreCase("")) {
                holder.payNow.setText("Pay Now");
                holder.payNow.setBackground(context.getResources().getDrawable(R.drawable.app_theme_button));
                holder.payNow.setVisibility(View.VISIBLE);
                holder.payNow.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setMessage("Select a payment method to continue");
                    builder.setPositiveButton("Online", (dialog, which) -> {
                        Toast.makeText(context, "...", Toast.LENGTH_SHORT).show();
                    });
                    builder.setNegativeButton("Offline", (dialog, which) -> {
                        AlertDialog.Builder builders = new AlertDialog.Builder(context);
                        builders.setCancelable(true);
                        builders.setMessage("In offline payment, a challan will be created. You can make payment using this challan at your nearest Axis bank. Do you want to create a challan?");
                        builders.setPositiveButton("Yes", (dialog1, which1) -> CreateChallan(holder.getBindingAdapterPosition()));
                        builders.setNegativeButton("No", (dialog1, which1) -> dialog.dismiss());
                        builders.create().show();
                    });
                    builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.create().show();
                });
            } else if (isPaid.equalsIgnoreCase("paid")) {
                holder.payNow.setText("PAID");
                holder.payNow.setBackground(context.getResources().getDrawable(R.drawable.button_success));
                holder.payNow.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(context, ChallanActivity.class);
                        intent.putExtra("invoice", obj.getString("OrderNo"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                holder.payNow.setText("PENDING");
                holder.payNow.setBackground(context.getResources().getDrawable(R.drawable.button_pending));
                holder.payNow.setOnClickListener(v -> {
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
                    holder.llEdit.setVisibility(View.VISIBLE);
                else
                    holder.llEdit.setVisibility(View.GONE);
            } catch (Exception e) {

            }

            if (mDate.getJSONObject(holder.getBindingAdapterPosition()).optDouble("Order_Value") < mDate.getJSONObject(holder.getBindingAdapterPosition()).optDouble("lastOrderedValue")) {
                holder.lowOrder.setVisibility(View.VISIBLE);
            } else {
                holder.lowOrder.setVisibility(View.GONE);
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

    private void CreateChallan(int bindingAdapterPosition) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating challan");
        progressDialog.show();
        try {
            JSONObject object = mDate.getJSONObject(bindingAdapterPosition);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Map<String, String> params = new HashMap<>();
            params.put("axn", "save_new_offline_trans");
            params.put("invoice", object.getString("OrderNo"));
            Call<ResponseBody> call = apiInterface.getUniversalData(params);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() == null) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String result = response.body().string();
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("success")) {
                                object.put("isPaid", jsonObject.getString("status"));
                                notifyItemChanged(bindingAdapterPosition);
                                Intent intent = new Intent(context, ChallanActivity.class);
                                intent.putExtra("invoice", object.getString("OrderNo"));
                                context.startActivity(intent);
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } catch (JSONException ignored) { }


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
