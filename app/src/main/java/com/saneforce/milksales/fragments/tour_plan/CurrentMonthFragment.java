package com.saneforce.milksales.fragments.tour_plan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.Tp_Mydayplan;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Model_Class.Tp_View_Master;
import com.saneforce.milksales.R;
import com.saneforce.milksales.databinding.CalendarItemBinding;
import com.saneforce.milksales.databinding.FragmentCurrentMonthBinding;
import com.saneforce.milksales.databinding.TourPlanExploreItemBinding;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentMonthFragment extends Fragment {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "dd-MMM-yyyy");
    private FragmentCurrentMonthBinding binding;
    int SelectedMonth;
    int CM, CY;
    int NM;
    private Common_Class common_class;
    private Type userType;
    private Gson gson;
    private List<Tp_View_Master> Tp_View_Master = new ArrayList<>();
    private int month, year;
    private GridCellAdapter adapter;
    private ProgressDialog progressDialog;
    private Calendar _calendar;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.Adapter adapter3;
    private Context context ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentMonthBinding.inflate(inflater, container, false);

        common_class = new Common_Class(getActivity());
        gson = new Gson();

        initProgressbar();
        getCurrentMonthAndNextMonth(); // CM , NM
        getCurrentYear(); // yyyy

        SelectedMonth = CM;

        // Load tour plan
        progressDialog.show();
        GetTpList();

        return binding.getRoot();
    }

    private void getCurrentYear() {
        _calendar = Calendar.getInstance(Locale.getDefault());
        if (SelectedMonth == 12 || SelectedMonth == 0) {
            SelectedMonth = 0;
            if (SelectedMonth == 12) {
                year = _calendar.get(Calendar.YEAR) + 1;
            } else {
                year = _calendar.get(Calendar.YEAR);
            }
        } else {
            year = _calendar.get(Calendar.YEAR);
        }
    }

    private void getCurrentMonthAndNextMonth() {
        Calendar cal = Calendar.getInstance();
        CM = cal.get(Calendar.MONTH);
        CY = cal.get(Calendar.YEAR);
        NM = cal.get(Calendar.MONTH) + 1;
        String currrentmonth = common_class.GetMonthname(CM) + "   " + CY;
        String nextmonth = "";
        if (CM == 11) {
            CY = CY + 1;
            nextmonth = common_class.GetMonthname(NM) + "   " + CY;
        } else
            nextmonth = common_class.GetMonthname(NM) + "   " + CY;
    }

    public void GetTpList() {
        try {
            int SM = SelectedMonth + 1;
            String Tp_Object = "{\"tableName\":\"vwTourPlan\",\"coloumns\":\"[\\\"date\\\",\\\"remarks\\\",\\\"worktype_code\\\",\\\"worktype_name\\\",\\\"RouteCode\\\",\\\"RouteName\\\",\\\"Worked_with_Code\\\",\\\"Worked_with_Name\\\",\\\"JointWork_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String Sf_Code = "";
            if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
                Sf_Code = Shared_Common_Pref.Sf_Code;
            } else {
                Sf_Code = Shared_Common_Pref.Tp_SFCode;
            }

            Call<Object> mCall = apiInterface.GettpRespnse(Shared_Common_Pref.Div_Code, Sf_Code, Sf_Code, Shared_Common_Pref.StateCode, String.valueOf(SM), String.valueOf(year), Tp_Object);
            mCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    progressDialog.dismiss();

                    userType = new TypeToken<ArrayList<Tp_View_Master>>() {
                    }.getType();
                    //------------ Server response
                    Tp_View_Master = gson.fromJson(new Gson().toJson(response.body()), userType);
                    month = SelectedMonth + 1;

                    //------------- Gridview
                    adapter = new GridCellAdapter(getContext(), R.id.date, month, year, (ArrayList<Tp_View_Master>) Tp_View_Master);
                    binding.gridcalander.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    //-------------- RecyclerView
                    adapter2 = new PostAdapter(getActivity(), R.id.date, SelectedMonth + 1, year, (ArrayList<com.saneforce.milksales.Model_Class.Tp_View_Master>) Tp_View_Master);
                    binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
                    binding.recyclerView.setHasFixedSize(true);
                    binding.recyclerView.setItemViewCacheSize(20);
                    binding.recyclerView.setAdapter(adapter2);
                    adapter.notifyDataSetChanged();

                    //--------------- Tour plan RecyclerView Plan explore view
                    adapter3 = new TourPlanExploreAdapter(getActivity(),  R.id.date, SelectedMonth + 1, year,(ArrayList<com.saneforce.milksales.Model_Class.Tp_View_Master>) Tp_View_Master);
                    binding.recyclerviewExplore.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerviewExplore.addItemDecoration(new DividerItemDecoration(requireContext(), 0));
                    binding.recyclerviewExplore.setHasFixedSize(true);
                    binding.recyclerviewExplore.setItemViewCacheSize(20);
                    binding.recyclerviewExplore.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        } catch (Exception ignored) {
        }
    }

    public class TourPlanExploreAdapter extends RecyclerView.Adapter<TourPlanExploreAdapter.ViewHolder>{
        private final Context context;
        private ArrayList<Tp_View_Master> tpViewMasterArrayList;
        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView gridcell;
        private ImageView iv_icon;
        private TextView num_events_per_day;
        private String  curentDateString;
        private Calendar selectedDate;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private SimpleDateFormat df;
        int selectedPosition = -1;
        int lastSelectedPosition = -1;

        public TourPlanExploreAdapter(Context context,int textViewResourceId, int month, int year , ArrayList<Tp_View_Master> tpViewMasterArrayList){
            this.context = context;
            this.tpViewMasterArrayList = tpViewMasterArrayList;
            this.list = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            selectedDate = (Calendar) calendar.clone();
            df = new SimpleDateFormat("MMM");
            curentDateString = df.format(selectedDate.getTime());

            printMonth(month, year);

            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
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
                    common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));
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
            private TourPlanExploreItemBinding binding;
            private TextView mWorkType, mRemarks, mDate;
            private CardView mCardView;

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
                    Log.e("getCurrentDayOfMonth", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
                    if (CheckTp_View(i).equals("1")) {
                        Log.e("PENDING_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        Log.e("APPROVED_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }*/
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }

                Log.e("DAY_of_month", String.valueOf(list.get(i - 1)));
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(i + 1 + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("DAYCOLOR", String.valueOf(list.get(i)));
                Log.e("Days_In_A month", String.valueOf(daysInMonth));
            }
        }
        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }


    }

    public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
        private final Context context;
        private final List<String> list;
        private ArrayList<Tp_View_Master> tpViewMasterArrayList;
        private static final int DAY_OFFSET = 1;
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView gridcell;
        private ImageView iv_icon;
        private TextView num_events_per_day;
        private String  curentDateString;
        private Calendar selectedDate;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private SimpleDateFormat df;
        int selectedPosition = -1;
        int lastSelectedPosition = -1;

        public PostAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<Tp_View_Master> tpViewMasterArrayList) {
            this.context = context;
            this.tpViewMasterArrayList = tpViewMasterArrayList;
            this.list = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            selectedDate = (Calendar) calendar.clone();
            df = new SimpleDateFormat("MMM");
            curentDateString = df.format(selectedDate.getTime());

            printMonth(month, year);

            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        @NonNull
        @Override
        public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CalendarItemBinding binding = CalendarItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

            holder.binding.getRoot().setOnClickListener(v -> {
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
                common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));
            });

            if (selectedPosition == holder.getBindingAdapterPosition()) {
                final int sdk = Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
                } else {
                    holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
                }
                holder.mDate.setTextColor(Color.WHITE);

            } else {
                final int sdk = Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled) );
                } else {
                    holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled));
                }
                holder.mDate.setTextColor(Color.BLACK);
            }

            String[] day_color = list.get(position).split("-");

            Log.e("day_color", String.valueOf(day_color[0]));
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                  //  num_events_per_day = view.findViewById(R.id.num_events_per_day);
                    Integer numEvents = eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            holder.mDate.setText(theday);
            holder.mDate.setTag(theday + "-" + themonth + "-" + theyear);
            if (day_color[1].equals("GREY")) {
                holder.mDate.setTextColor(Color.LTGRAY);
                holder.mDate.setEnabled(false);
            }
            if (day_color[1].equals("GREEN")) {
                holder.mTourPlanCircle.setVisibility(View.VISIBLE);
            }
            if (day_color[1].equals("BLUE")) {
                holder.mDate.setTextColor(getResources().getColor(R.color.Pending_yellow));
            }

           if (tpViewMasterArrayList != null){
               for (int i = 0; tpViewMasterArrayList.size() > i; i++ ){
                   Log.e("nn_", String.valueOf(tpViewMasterArrayList.get(i).getRemarks()));
               }
           }else {
               Toast.makeText(context, "Empty dp arraylist", Toast.LENGTH_SHORT).show();
           }

//            int size = tpViewMasterArrayList.size();
//            Tp_View_Master tp = tpViewMasterArrayList.get(position);
//
//            String mColor = tp.getColor();
//            String mDate = tp.getDate();
//
//            if (mColor != null){
//                holder.mTourPlanCircle.setCardBackgroundColor(Color.parseColor(mColor));
//            }
//
//            Log.e("color_", mColor);
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
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private CalendarItemBinding binding;
            private TextView mDate;
            private RelativeLayout mDateLayout;
            private CardView mTourPlanCircle;

            public ViewHolder(CalendarItemBinding binding)
            {
                super(binding.getRoot());
                this.binding = binding;

                mDate = binding.date;
                mDateLayout = binding.dateLayout;
                mTourPlanCircle = binding.color;

            }
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        private String getMonthAsString(int i) {
            return months[i];
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
                    Log.e("getCurrentDayOfMonth", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
                    if (CheckTp_View(i).equals("1")) {
                        Log.e("PENDING_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        Log.e("APPROVED_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }*/
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }

                Log.e("DAY_of_month", String.valueOf(list.get(i - 1)));
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(i + 1 + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("DAYCOLOR", String.valueOf(list.get(i)));
                Log.e("Days_In_A month", String.valueOf(daysInMonth));
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }
    }

    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private final Context _context;
        private final List<String> list;
        private ArrayList<Tp_View_Master> Tp_View_Master;
        private static final int DAY_OFFSET = 1;
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView gridcell;
        private ImageView iv_icon;
        private TextView num_events_per_day;
        private String  curentDateString;
        private Calendar selectedDate;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private SimpleDateFormat df;

        public GridCellAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<Tp_View_Master> Tp_View_Master) {
            super();
            this._context = context;
            this.Tp_View_Master = Tp_View_Master;

            this.list = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            selectedDate = (Calendar) calendar.clone();
            df = new SimpleDateFormat("MMM");
            curentDateString = df.format(selectedDate.getTime());

            printMonth(month, year);

            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
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
                    Log.e("getCurrentDayOfMonth", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
                    if (CheckTp_View(i).equals("1")) {
                        Log.e("PENDING_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        Log.e("APPROVED_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }*/
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }

                Log.e("DAY_of_month", String.valueOf(list.get(i - 1)));
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("DAYCOLOR", String.valueOf(list.get(i)));
                Log.e("Days_In_A month", String.valueOf(daysInMonth));
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_item, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = row.findViewById(R.id.date);
            iv_icon = row.findViewById(R.id.tp_date_icon);


            // for spacing
            String[] day_color = list.get(position).split("-");

            Log.e("THE_DAY_COLOR", String.valueOf(day_color[0]));
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.e("ALL_DATE", theday + "-" + themonth + "-" + theyear + day_color[1]);
            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(Color.LTGRAY);
                gridcell.setEnabled(false);
            }
            if (day_color[1].equals("GREEN")) {
                gridcell.setTextColor(getResources().getColor(R.color.subExpHeader));
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.Pending_yellow));
            }
            int in = 0;
            gridcell.setOnClickListener(v -> {
                String[] day_color1 = list.get(position).split("-");
                Log.e("THE_DAY_COLOR", String.valueOf(day_color1[0]));
                String theday1 = day_color1[0];
                String themonth1 = day_color1[2];
                String theyear1 = day_color1[3];
                int month = SelectedMonth + 1;
                String TourMonth = theyear1 + "-" + month + "-" + theday1;
                Log.e("Grid_Selected_Date", theday1 + "-" + themonth1 + "-" + theyear1 + day_color1[1]);
                common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));

            });
            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            Log.e("Selected date", date_month_year);
            try {
                dateFormatter.parse(date_month_year);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
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

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }

    private void initProgressbar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.......");
        progressDialog.setTitle("Tour Plan");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
    }
}