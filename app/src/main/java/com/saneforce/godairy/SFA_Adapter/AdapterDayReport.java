package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;

import org.json.JSONArray;

public class AdapterDayReport extends RecyclerView.Adapter<AdapterDayReport.ViewHolder> {
    Context context;
    JSONArray array;

    public AdapterDayReport(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AdapterDayReport.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterDayReport.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_day_report, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDayReport.ViewHolder holder, int position) {
        holder.emp_name.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("title"));
        holder.work_type.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("workType"));
        holder.emp_id_deg.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("empId") + " & " + array.optJSONObject(holder.getBindingAdapterPosition()).optString("designationName"));
        holder.submit_time.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("submittedOn"));
        holder.visited_distributor.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistVisitedCount"));
        holder.visited_outlet.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetVisitedCount"));
        holder.distOrderTaken.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistOrderCount"));
        holder.outletOrderTaken.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetOrderCount"));
        holder.count_distributor.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistInvoiceCount"));
        holder.count_outlet.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetInvoiceCount"));
        holder.ordered_distributor.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistOrderAmt"));
        holder.ordered_outlet.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetOrderAmt"));
        holder.invoiced_distributor.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistInvoiceAmt"));
        holder.invoiced_outlet.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetInvoiceAmt"));
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emp_name, work_type, emp_id_deg, submit_time, visited_distributor, visited_outlet, distOrderTaken, outletOrderTaken, count_distributor, count_outlet, ordered_distributor, ordered_outlet, invoiced_distributor, invoiced_outlet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emp_name = itemView.findViewById(R.id.emp_name);
            work_type = itemView.findViewById(R.id.work_type);
            emp_id_deg = itemView.findViewById(R.id.emp_id_deg);
            submit_time = itemView.findViewById(R.id.submit_time);
            visited_distributor = itemView.findViewById(R.id.visited_distributor);
            visited_outlet = itemView.findViewById(R.id.visited_outlet);
            distOrderTaken = itemView.findViewById(R.id.distOrderTaken);
            outletOrderTaken = itemView.findViewById(R.id.outletOrderTaken);
            count_distributor = itemView.findViewById(R.id.count_distributor);
            count_outlet = itemView.findViewById(R.id.count_outlet);
            ordered_distributor = itemView.findViewById(R.id.ordered_distributor);
            ordered_outlet = itemView.findViewById(R.id.ordered_outlet);
            invoiced_distributor = itemView.findViewById(R.id.invoiced_distributor);
            invoiced_outlet = itemView.findViewById(R.id.invoiced_outlet);
        }
    }
}