package com.saneforce.godairy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Activity_Hap.DayReportActivity;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Model_Class.DayReport;
import com.saneforce.godairy.R;

import java.util.List;

public class DayReportAdapter extends RecyclerView.Adapter<DayReportAdapter.MyViewHolder> {
    List<DayReport> dayreportlist ;
    AdapterOnClick adapterOnClick;
    int itemDayReport;
    Context applicationContext;


    public DayReportAdapter(DayReportActivity dayReportActivity, AdapterOnClick adapterOnClick) {

    }

    public DayReportAdapter(List<DayReport> dayreportlist, int itemDayReport, Context applicationContext, AdapterOnClick adapterOnClick) {
        this.dayreportlist = dayreportlist;
        this.itemDayReport = itemDayReport;
        this.adapterOnClick = adapterOnClick;
        this.applicationContext = applicationContext;

    }


    @NonNull
    @Override
    public DayReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_day_report, null, false);
        return new DayReportAdapter.MyViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull DayReportAdapter.MyViewHolder holder, int position) {
        /*  holder.tv_emp_name.setText(" " + mDate.get(position).getEmpName());*/
        holder.tv_emp_name.setText(" " + dayreportlist.get(position).getEmpname());
        holder.tv_work_type.setText("" + dayreportlist.get(position).getWorktype());
        holder.tv_emp_id_deg.setText("" + dayreportlist.get(position).getEmpiddeg());
        holder.tv_submit_time.setText("" + dayreportlist.get(position).getSubmittime());
        holder.tv_visited_distributor.setText("" + dayreportlist.get(position).getVisiteddistributor());
        holder.tv_visited_outlet.setText("" + dayreportlist.get(position).getVisitedoutlet());
        holder.tv_order_distributor.setText("" + dayreportlist.get(position).getOrderdistributor());
        holder.tv_count_distributor.setText("" + dayreportlist.get(position).getCountdistributor());
        holder.tv_order_outlet.setText("" + dayreportlist.get(position).getOrderoutlet());
        holder.tv_count_outlet.setText("" + dayreportlist.get(position).getCountoutlet());
        holder.tv_ordered_distributor.setText("" + dayreportlist.get(position).getOrdereddistributor());
        holder.tv_ordered_outlet.setText("" + dayreportlist.get(position).getOrderedoutlet());
        holder.tv_invoiced_distributor.setText("" + dayreportlist.get(position).getInvoiceddistributor());
        holder.tv_invoiced_outlet.setText("" + dayreportlist.get(position).getInvoicedoutlet());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_emp_name, tv_work_type, tv_emp_id_deg, tv_submit_time, tv_visited_distributor, tv_visited_outlet,
                tv_order_distributor, tv_order_outlet, tv_count_distributor, tv_count_outlet, tv_ordered_distributor,
                tv_ordered_outlet, tv_invoiced_distributor, tv_invoiced_outlet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_emp_name = (TextView) itemView.findViewById(R.id.emp_name);
            tv_work_type = (TextView) itemView.findViewById(R.id.work_type);
            tv_emp_id_deg = (TextView) itemView.findViewById(R.id.emp_id_deg);
            tv_submit_time = (TextView) itemView.findViewById(R.id.submit_time);
            tv_visited_distributor = (TextView) itemView.findViewById(R.id.visited_distributor);
            tv_visited_outlet = (TextView) itemView.findViewById(R.id.visited_outlet);
          //  tv_order_distributor = (TextView) itemView.findViewById(R.id.order_distributor);
           // tv_order_outlet = (TextView) itemView.findViewById(R.id.order_outlet);

            tv_count_distributor = (TextView) itemView.findViewById(R.id.count_distributor);
            tv_count_outlet = (TextView) itemView.findViewById(R.id.count_outlet);
            tv_ordered_distributor = (TextView) itemView.findViewById(R.id.ordered_distributor);
            tv_ordered_outlet = (TextView) itemView.findViewById(R.id.ordered_outlet);

            tv_invoiced_distributor = (TextView) itemView.findViewById(R.id.invoiced_distributor);
            tv_invoiced_outlet = (TextView) itemView.findViewById(R.id.invoiced_outlet);


        }
    }
}

