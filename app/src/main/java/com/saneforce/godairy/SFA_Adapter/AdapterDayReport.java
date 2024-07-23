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
import org.json.JSONObject;

public class AdapterDayReport extends RecyclerView.Adapter<AdapterDayReport.ViewHolder> {
    Context context;
    JSONArray array;

    OnItemClick onItemClick;

    public AdapterDayReport(Context context, JSONArray array, OnItemClick onItemClick) {
        this.context = context;
        this.array = array;
        this.onItemClick = onItemClick;
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
        String empIdDeg = array.optJSONObject(holder.getBindingAdapterPosition()).optString("empId") + " & " + array.optJSONObject(holder.getBindingAdapterPosition()).optString("designationName");
        holder.emp_id_deg.setText(empIdDeg);
        holder.submit_time.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("submittedOn"));
        holder.visited_distributor.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistVisitedCount"));
        holder.visited_outlet.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetVisitedCount"));
        holder.distOrderTaken.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistOrderCount"));
        holder.distOrderTaken.setOnClickListener(v -> onItemClick.onDistOrderCountClick(holder.getBindingAdapterPosition(), array.optJSONObject(holder.getBindingAdapterPosition())));
        holder.outletOrderTaken.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetOrderCount"));
        holder.outletOrderTaken.setOnClickListener(v -> onItemClick.onRetOrderCountClick(holder.getBindingAdapterPosition(), array.optJSONObject(holder.getBindingAdapterPosition())));
        holder.count_distributor.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("DistInvoiceCount"));
        holder.count_distributor.setOnClickListener(v -> onItemClick.onDistInvoiceCountClick(holder.getBindingAdapterPosition(), array.optJSONObject(holder.getBindingAdapterPosition())));
        holder.count_outlet.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("RetInvoiceCount"));
        holder.count_outlet.setOnClickListener(v -> onItemClick.onRetInvoiceCountClick(holder.getBindingAdapterPosition(), array.optJSONObject(holder.getBindingAdapterPosition())));
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

    public interface OnItemClick {
        void onDistOrderCountClick(int position, JSONObject object);
        void onRetOrderCountClick(int position, JSONObject object);
        void onDistInvoiceCountClick(int position, JSONObject object);
        void onRetInvoiceCountClick(int position, JSONObject object);
    }
}
