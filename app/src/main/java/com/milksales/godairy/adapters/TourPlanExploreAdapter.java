package com.milksales.godairy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Activity_Hap.Tp_Mydayplan;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Model_Class.Tp_View_Master;
import com.milksales.godairy.databinding.TourPlanExploreItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class TourPlanExploreAdapter extends RecyclerView.Adapter<TourPlanExploreAdapter.ViewHolder>{
    private final Context context;
    private final ArrayList<Tp_View_Master> tpViewMasterArrayList;
    private final List<Tp_View_Master> Tp_View_Master = new ArrayList<>();
    private final List<String> list;
    private static final int DAY_OFFSET = 1;
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    private String  curentDateString;
    private Calendar selectedDate;
    private SimpleDateFormat df;
    int selectedPosition = -1;
    int lastSelectedPosition = -1;
    int SelectedMonth;
    private Common_Class common_class;

    @SuppressLint("SimpleDateFormat")
    public TourPlanExploreAdapter(Context context, int month, int year , ArrayList<Tp_View_Master> tpViewMasterArrayList){
        this.context = context;
        this.tpViewMasterArrayList = tpViewMasterArrayList;
        this.list = new ArrayList<>();

        common_class = new Common_Class(context);

        Calendar calendar = Calendar.getInstance();
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        selectedDate = (Calendar) calendar.clone();
        df = new SimpleDateFormat("MMM");
        curentDateString = df.format(selectedDate.getTime());

        SelectedMonth = month;

        printMonth(month, year);
        findNumberOfEventsPerMonth(year, month);
    }

    @NonNull
    @Override
    public TourPlanExploreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TourPlanExploreItemBinding binding = TourPlanExploreItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TourPlanExploreAdapter.ViewHolder holder, int position) {
        if (tpViewMasterArrayList != null){
            Tp_View_Master tpViewMaster = tpViewMasterArrayList.get(position);

            holder.mWorkType.setText(tpViewMaster.getWorktypeName());
            holder.mRemarks.setText(tpViewMaster.getRemarks());
            holder.mDate.setText(tpViewMaster.getDate());
            holder.mCardView.setCardBackgroundColor(Color.parseColor(tpViewMaster.getColor()));
            holder.binding.cardLayout.setOnClickListener(v -> {
                lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getBindingAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);

                String[] day_color1 = list.get(position).split("-");
                String theday1 = day_color1[0];
                String themonth1 = day_color1[2];
                String theyear1 = day_color1[3];
                int month = SelectedMonth + 1;
                String TourMonth = theyear1 + "-" + month + "-" + theday1;
               // common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", tpViewMaster.getDate(), "TourMonth", String.valueOf(month - 1));
                Intent intent = new Intent(context, Tp_Mydayplan.class);
                intent.putExtra("TourDate", tpViewMaster.getDate());
                intent.putExtra("TourMonth", String.valueOf(month - 1));
                context.startActivity(intent);
            });

        }else {
            Toast.makeText(context, "Empty tp arraylist", Toast.LENGTH_SHORT).show();
        }
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
        return tpViewMasterArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TourPlanExploreItemBinding binding;
        private final TextView mWorkType, mRemarks, mDate;
        private final CardView mCardView;

        public ViewHolder(TourPlanExploreItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
            mWorkType = binding.workType;
            mRemarks = binding.remarks;
            mDate = binding.date;
            mCardView = binding.cardLayout;
        }
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    public void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    private int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    private String getMonthAsString(int i) {
        return months[i];
    }

    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        return map;
    }

    public String CheckTp_View(int a) {
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

    @SuppressLint("WrongConstant")
    private void printMonth(int mm, int yy) {
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        int currentMonth = mm - 1;
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);
        // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        }

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
            ++daysInMonth;
        }

        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
        }

        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {
            if (CheckTp_View(i).equals("1") || CheckTp_View(i).equals("3")) {
                Log.e("tp_calander_", "Get current day of month : " + i + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
                if (CheckTp_View(i).equals("1")) {
                    Log.e("tp_calander_", "Prnding color : " + CheckTp_View(i));
                    list.add(i + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    Log.e("tp_calander_", "approved color : " + CheckTp_View(i));
                    list.add(i + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }*/
            } else {
                list.add(i + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
            }
            Log.e("DAY_of_month", String.valueOf(list.get(i - 1)));
        }

        // Leading Month days
        for (int i = 0; i < list.size() % 7; i++) {
            list.add(i + 1 + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
        }
        for (int i = 0; i < list.size(); i++) {
            Log.e("tp_calander_", "Day color : " + list.get(i));
            Log.e("tp_calander_", "Days in a month : " + daysInMonth);
        }
    }
    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }
}