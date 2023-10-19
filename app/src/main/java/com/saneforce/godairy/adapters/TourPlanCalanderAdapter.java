package com.saneforce.godairy.adapters;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.CalendarItemBinding;

//public class TourPlanCalanderAdapter extends RecyclerView.Adapter<TourPlanCalanderAdapter.ViewHolder>{
//    private final Context context;
//    private final List<String> list;
//    private ArrayList<Tp_View_Master> tpViewMasterArrayList;
//  //  private final List<Tp_View_Master> Tp_View_Master = new ArrayList<>();
//    private static final int DAY_OFFSET = 1;
//    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
//    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
//    private int daysInMonth;
//    private int currentDayOfMonth;
//    private int currentWeekDay;
//    private TextView num_events_per_day;
//    private String  curentDateString;
//    private Calendar selectedDate;
//    private final HashMap<String, Integer> eventsPerMonthMap;
//    private SimpleDateFormat df;
//    int selectedPosition = -1;
//    int lastSelectedPosition = -1;
//    int SelectedMonth;
//    private Common_Class common_class;
//
//    public TourPlanCalanderAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<Tp_View_Master> tpViewMasterArrayList) {
//        this.context = context;
//        this.tpViewMasterArrayList = tpViewMasterArrayList;
//        this.list = new ArrayList<>();
//
//        common_class = new Common_Class(context);
//
//        Calendar calendar = Calendar.getInstance();
//        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
//        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
//        selectedDate = (Calendar) calendar.clone();
//        df = new SimpleDateFormat("MMM");
//        curentDateString = df.format(selectedDate.getTime());
//
//        SelectedMonth = month;
//
//        printMonth(month, year);
//        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
//    }
//
//    @NonNull
//    @Override
//    public TourPlanCalanderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        CalendarItemBinding binding = CalendarItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        return new ViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TourPlanCalanderAdapter.ViewHolder holder, int position) {
//
//        holder.binding.getRoot().setOnClickListener(v -> {
//            lastSelectedPosition = selectedPosition;
//            selectedPosition = holder.getBindingAdapterPosition();
//            notifyItemChanged(lastSelectedPosition);
//            notifyItemChanged(selectedPosition);
//
//            String[] day_color1 = list.get(position).split("-");
//            String theday1 = day_color1[0];
//            String themonth1 = day_color1[2];
//            String theyear1 = day_color1[3];
//            int month = SelectedMonth + 1;
//            String TourMonth = theyear1 + "-" + month + "-" + theday1;
//            common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));
//        });
//
//        if (selectedPosition == holder.getBindingAdapterPosition()) {
//            final int sdk = Build.VERSION.SDK_INT;
//            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
//            } else {
//                holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
//            }
//            holder.mDate.setTextColor(Color.WHITE);
//
//        } else {
//            final int sdk = Build.VERSION.SDK_INT;
//            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled) );
//            } else {
//                holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled));
//            }
//            holder.mDate.setTextColor(Color.BLACK);
//        }
//
//        String[] day_color = list.get(position).split("-");
//
//        Log.e("day_color", String.valueOf(day_color[0]));
//        String theday = day_color[0];
//        String themonth = day_color[2];
//        String theyear = day_color[3];
//        if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
//            if (eventsPerMonthMap.containsKey(theday)) {
//                //  num_events_per_day = view.findViewById(R.id.num_events_per_day);
//                Integer numEvents = eventsPerMonthMap.get(theday);
//                num_events_per_day.setText(numEvents.toString());
//            }
//        }
//
//        // Set the Day GridCell
//        holder.mDate.setText(theday);
//        holder.mDate.setTag(theday + "-" + themonth + "-" + theyear);
//        if (day_color[1].equals("GREY")) {
//            holder.mDate.setTextColor(Color.LTGRAY);
//            holder.mDate.setEnabled(false);
//        }
//        if (day_color[1].equals("GREEN")) {
//            holder.mTourPlanCircle.setVisibility(View.VISIBLE);
//        }
//        if (day_color[1].equals("BLUE")) {
//            holder.mDate.setTextColor(context.getResources().getColor(R.color.Pending_yellow));
//        }
//
//
//        if (tpViewMasterArrayList != null){
//            for (int i = 0; tpViewMasterArrayList.size() > i; i++ ){
//                Log.e("nn_", String.valueOf(tpViewMasterArrayList.get(i).getRemarks()));
//            }
//        }else {
//            Toast.makeText(context, "Empty dp arraylist", Toast.LENGTH_SHORT).show();
//        }
//
////            int size = tpViewMasterArrayList.size();
////            Tp_View_Master tp = tpViewMasterArrayList.get(position);
////
////            String mColor = tp.getColor();
////            String mDate = tp.getDate();
////
////            if (mColor != null){
////                holder.mTourPlanCircle.setCardBackgroundColor(Color.parseColor(mColor));
////            }
////
////            Log.e("color_", mColor);
//    }
//
//    @Override
//    public long getItemId(int position){
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position){
//        return position;
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder
//    {
//        private CalendarItemBinding binding;
//        private TextView mDate;
//        private RelativeLayout mDateLayout;
//        private CardView mTourPlanCircle;
//
//        public ViewHolder(CalendarItemBinding binding)
//        {
//            super(binding.getRoot());
//            this.binding = binding;
//
//            mDate = binding.date;
//            mDateLayout = binding.dateLayout;
//            mTourPlanCircle = binding.color;
//
//        }
//    }
//
//    private int getNumberOfDaysOfMonth(int i) {
//        return daysOfMonth[i];
//    }
//
//    private void setCurrentDayOfMonth(int currentDayOfMonth) {
//        this.currentDayOfMonth = currentDayOfMonth;
//    }
//
//    public int getCurrentDayOfMonth() {
//        return currentDayOfMonth;
//    }
//
//    public void setCurrentWeekDay(int currentWeekDay) {
//        this.currentWeekDay = currentWeekDay;
//    }
//
//    private String getMonthAsString(int i) {
//        return months[i];
//    }
//
//    public String CheckTp_View(int a) {
//        String bflag = "0";
//         List<Tp_View_Master> Tp_View_Master = new ArrayList<>();
//        if (Tp_View_Master != null) {
//
//
//            for (int i = 0; Tp_View_Master.size() > i; i++) {
//                if (a == Tp_View_Master.get(i).getDayofcout()) {
//                    Log.v("SUBMIT_STATUS", String.valueOf(Tp_View_Master.get(i).getSubmitStatus() + "DAY" + Tp_View_Master.get(i).getDayofcout()));
//                    if (String.valueOf(Tp_View_Master.get(i).getSubmitStatus()).equals("3")) {
//                        bflag = "3";
//                    } else {
//                        bflag = "1";
//                    }
//                }
//            }
//        }
//        return bflag;
//    }
//
//    @SuppressLint("WrongConstant")
//    private void printMonth(int mm, int yy) {
//        int trailingSpaces = 0;
//        int daysInPrevMonth = 0;
//        int prevMonth = 0;
//        int prevYear = 0;
//        int nextMonth = 0;
//        int nextYear = 0;
//
//        int currentMonth = mm - 1;
//        daysInMonth = getNumberOfDaysOfMonth(currentMonth);
//        // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
//        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
//
//        if (currentMonth == 11) {
//            prevMonth = currentMonth - 1;
//            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//            nextMonth = 0;
//            prevYear = yy;
//            nextYear = yy + 1;
//        } else if (currentMonth == 0) {
//            prevMonth = 11;
//            prevYear = yy - 1;
//            nextYear = yy;
//            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//            nextMonth = 1;
//        } else {
//            prevMonth = currentMonth - 1;
//            nextMonth = currentMonth + 1;
//            nextYear = yy;
//            prevYear = yy;
//            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
//        }
//
//        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        trailingSpaces = currentWeekDay;
//
//        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
//            ++daysInMonth;
//        }
//
//        // Trailing Month days
//        for (int i = 0; i < trailingSpaces; i++) {
//            list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
//        }
//
//        // Current Month Days
//        for (int i = 1; i <= daysInMonth; i++) {
//            if (CheckTp_View(i).equals("1") || CheckTp_View(i).equals("3")) {
//                Log.e("getCurrentDayOfMonth", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
//                if (CheckTp_View(i).equals("1")) {
//                    Log.e("PENDING_COLOR", CheckTp_View(i));
//                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
//                } else {
//                    Log.e("APPROVED_COLOR", CheckTp_View(i));
//                    list.add(String.valueOf(i) + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + yy);
//                }
//                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
//                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
//                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
//                    } else {
//                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
//                    }*/
//            } else {
//                list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
//            }
//
//            Log.e("DAY_of_month", String.valueOf(list.get(i - 1)));
//        }
//
//        // Leading Month days
//        for (int i = 0; i < list.size() % 7; i++) {
//            list.add(i + 1 + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
//        }
//        for (int i = 0; i < list.size(); i++) {
//            Log.e("DAYCOLOR", String.valueOf(list.get(i)));
//            Log.e("Days_In_A month", String.valueOf(daysInMonth));
//        }
//    }
//
//    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
//        HashMap<String, Integer> map = new HashMap<String, Integer>();
//        return map;
//    }
//}
