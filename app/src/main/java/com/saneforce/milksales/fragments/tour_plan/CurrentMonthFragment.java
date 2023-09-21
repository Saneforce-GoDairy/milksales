package com.saneforce.milksales.fragments.tour_plan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.Tp_Calander;
import com.saneforce.milksales.Activity_Hap.Tp_Mydayplan;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Model_Class.Tp_View_Master;
import com.saneforce.milksales.R;
import com.saneforce.milksales.databinding.FragmentCurrentMonthBinding;
import com.saneforce.milksales.databinding.FragmentTodayBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

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
        GetTp_List();

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

    public void GetTp_List() {
        try {
            Log.e("ERROR_CONTROL", String.valueOf(SelectedMonth));
            int SM = SelectedMonth + 1;
            String Tp_Object = "{\"tableName\":\"vwTourPlan\",\"coloumns\":\"[\\\"date\\\",\\\"remarks\\\",\\\"worktype_code\\\",\\\"worktype_name\\\",\\\"RouteCode\\\",\\\"RouteName\\\",\\\"Worked_with_Code\\\",\\\"Worked_with_Name\\\",\\\"JointWork_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String Sf_Code = "";
            if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
                Sf_Code = Shared_Common_Pref.Sf_Code;
            } else {
                Sf_Code = Shared_Common_Pref.Tp_SFCode;
            }
            Log.e("FIELDFORCE_SF", Sf_Code);
            Call<Object> mCall = apiInterface.GettpRespnse(Shared_Common_Pref.Div_Code, Sf_Code, Sf_Code, Shared_Common_Pref.StateCode, String.valueOf(SM), String.valueOf(year), Tp_Object);
            mCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    // locationList=response.body();
                    Log.e("GetCurrentMonth_Values", String.valueOf(response.body().toString()));
                    Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                    userType = new TypeToken<ArrayList<Tp_View_Master>>() {
                    }.getType();
                    Tp_View_Master = gson.fromJson(new Gson().toJson(response.body()), userType);
                    month = SelectedMonth + 1;
                    adapter = new GridCellAdapter(getContext(), R.id.date, month, year, (ArrayList<Tp_View_Master>) Tp_View_Master);
                    adapter.notifyDataSetChanged();
                    binding.gridcalander.setAdapter(adapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });

            try {
                adapter = new GridCellAdapter(getActivity(), R.id.date, SelectedMonth + 1, year, (ArrayList<com.saneforce.milksales.Model_Class.Tp_View_Master>) Tp_View_Master);
                adapter.notifyDataSetChanged();
                binding.gridcalander.setAdapter(adapter);
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {
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
        String  curentDateString;
        private Calendar selectedDate;
        private final HashMap<String, Integer> eventsPerMonthMap;
        SimpleDateFormat df;

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


            // ACCOUNT FOR SPACING

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
                // iv_icon.setVisibility(View.VISIBLE);
                gridcell.setTextColor(getResources().getColor(R.color.Pending_yellow));
                //gridcell.setBackgroundResource(R.drawable.grid_dateshape);
            }
            gridcell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] day_color = list.get(position).split("-");
                    Log.e("THE_DAY_COLOR", String.valueOf(day_color[0]));
                    String theday = day_color[0];
                    String themonth = day_color[2];
                    String theyear = day_color[3];
                    int month = SelectedMonth + 1;
                    String TourMonth = theyear + "-" + month + "-" + theday;
                    Log.e("Grid_Selected_Date", theday + "-" + themonth + "-" + theyear + day_color[1]);
                    common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));

                }
            });

            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            //selectedDayMonthYearButton.setText("Selected: " + date_month_year);
            Log.e("Selected date", date_month_year);
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);

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