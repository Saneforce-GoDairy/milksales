package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Model_Class.RemainingLeave;
import com.saneforce.milksales.R;

import java.util.List;

public class LeaveRemaining extends RecyclerView.Adapter<LeaveRemaining.MyViewHolder> {

    Context context;
    List<RemainingLeave> mDate;
    String produtId, productDate;

    public LeaveRemaining(Context context, List<RemainingLeave> mDate) {
        this.context = context;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_leave_remaining, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.txtType.setText(mDate.get(position).getLeaveSName());
        holder.txtEli.setText(mDate.get(position).getLeaveValue());
        holder.txtTake.setText(mDate.get(position).getLeaveTaken());
        holder.txtAvai.setText(mDate.get(position).getLeaveAvailability());
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtType,txtEli,txtTake,txtAvai;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtType = (TextView) itemView.findViewById(R.id.txt_type);
            txtEli = (TextView) itemView.findViewById(R.id.txt_eligi);
            txtTake = (TextView) itemView.findViewById(R.id.txt_taken);
            txtAvai = (TextView) itemView.findViewById(R.id.txt_availa);


        }
    }
}