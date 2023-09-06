package com.saneforce.milksales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Model_Class.Tp_View_Master;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.TourPlan;
import com.saneforce.milksales.databinding.CalendarItemBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourPlanAdapter extends RecyclerView.Adapter<TourPlanAdapter.ViewHolder>{
    private static final String TAG = "Month Plan Adapter";
    private final Context context;
    private final List<TourPlan> tourlist;

    private final int month;
    private final int year;
    private String mMonthNumeric;

    int selectedPosition = -1;
    int lastSelectedPosition = -1;

    public TourPlanAdapter (Context context, int month, int year, ArrayList<TourPlan> tourlist)
    {
        this.context = context;
        this.tourlist = tourlist;
        this.month = month;
        this.year = year;
    }

    @NonNull
    @Override
    public TourPlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CalendarItemBinding binding = CalendarItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if (selectedPosition == holder.getBindingAdapterPosition()) {
            holder.binding.colorGrey.setVisibility(View.GONE);
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
            } else {
                holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
            }

            holder.mMonth.setTextColor(Color.BLACK);
            holder.mDate.setTextColor(Color.WHITE);

        } else {
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled) );
            } else {
                holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled));
            }
            holder.mMonth.setTextColor(Color.GRAY);
            holder.mDate.setTextColor(Color.GRAY);
        }


        TourPlan tourPlan = tourlist.get(position);
        String[] date = tourPlan.getDay_month().split("-");

        holder.mDate.setText(date[0]);

        if (date[1].equals("GREY")) {
          //  holder.mDate.setTextColor(Color.LTGRAY);
          //  holder.mDate.setEnabled(false);
            holder.binding.colorGrey.setVisibility(View.VISIBLE);
        }
        if (date[1].equals("GREEN")) {
            holder.mDate.setTextColor(ContextCompat.getColor(context, R.color.subExpHeader));
        }
        if (date[1].equals("BLUE")) {
            // iv_icon.setVisibility(View.VISIBLE);
            holder.mDate.setTextColor(ContextCompat.getColor(context, R.color.Pending_yellow));
            //gridcell.setBackgroundResource(R.drawable.grid_dateshape);
        }

        // below code get day name in text format
        if (date[2].equals("January")){
            Log.e(TAG, "This is August");
            mMonthNumeric = "01";
        }

        if (date[2].equals("February")){
            mMonthNumeric = "02";
        }

        if (date[2].equals("March")){
            mMonthNumeric = "03";
        }

        if (date[2].equals("April")){
            mMonthNumeric = "04";
        }

        if (date[2].equals("May")){
            mMonthNumeric = "05";
        }

        if (date[2].equals("June")){
            mMonthNumeric = "06";
        }

        if (date[2].equals("July")){
            mMonthNumeric = "07";
        }

        if (date[2].equals("August")){
            mMonthNumeric = "08";
        }

        if (date[2].equals("September")){
            mMonthNumeric = "09";
        }

        if (date[2].equals("October")){
            mMonthNumeric = "10";
        }

        if (date[2].equals("November")){
            mMonthNumeric = "11";
        }

        if (date[2].equals("December")){
            mMonthNumeric = "12";
        }

        String mFinalDate = date[0] + "-" + mMonthNumeric + "-" + date[3]; // dd-MM-yyyy

        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = null;
        try {
            date1 = inFormat.parse(mFinalDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String goal = outFormat.format(date1);


          holder.mMonth.setText(goal);

        holder.binding.getRoot().setOnClickListener(v -> {
            lastSelectedPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectedPosition);
        });

//        holder.mDateLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (date[1].equals("GREY")) {
//                    holder.mDate.setTextColor(Color.LTGRAY);
//                    Log.e(TAG, "This is gray");
//                }
//
//                if (date[1].equals("WHITE")){
//                    Log.e(TAG, "This is not gray");
//                    holder.mDate.setEnabled(true);
//                    holder.mDate.setTextColor(Color.WHITE);
//                }
//            }
//        });
    }

    public String CheckTp_View(int a) {
        ArrayList<Tp_View_Master> Tp_View_Master = new ArrayList<>();

        String bflag = "0";
        if (Tp_View_Master != null) {

            for (int i = 0; Tp_View_Master.size() > i; i++) {
                if (a == Tp_View_Master.get(i).getDayofcout()) {
                    Log.v("SUBMIT_STATUS", String.valueOf(Tp_View_Master.get(i).getSubmitStatus() + "DAY" + Tp_View_Master.get(i).getDayofcout()));
                    if (String.valueOf(Tp_View_Master.get(i).getSubmitStatus()).equals("3")) {
                        bflag = "3";
                    } else {
                        bflag = "1";
                    }

                }
            }
        }
        return bflag;
    }


    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public int getItemCount() {
        return tourlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CalendarItemBinding binding;
        TextView mDate, mMonth;
        RelativeLayout mLayout, mDateLayout;
        public ViewHolder(CalendarItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
            mDate = binding.date;
            mMonth = binding.month;
            mLayout = binding.layout1;
            mDateLayout = binding.dateLayout;
        }
    }
}

