package com.saneforce.milksales.Activity_Hap;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity.Util.UpdateUi;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Model_Class.Tp_View_Master;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.TourPlanAdapter;
import com.saneforce.milksales.common.TourPlan;
import com.saneforce.milksales.databinding.ActivityTpMonthSelectBinding;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tp_Month_Select extends AppCompatActivity implements View.OnClickListener, UpdateUi {
    private ActivityTpMonthSelectBinding binding;
    private Context context = this;
    private static final String TAG = "Month Plan";

    private TextView currentMonth, CurrentMoth, NextMonth;
    public Button btnsubmit, selectedDayMonthYearButton;
    public ImageView goback;
    private RelativeLayout prevMonth, nextMonth;

    private GridView calendarView;
    private GridCellAdapter adapter;

    private Calendar _calendar;
    private ProgressDialog nDialog;
    private Type userType;
    private Gson gson;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");


    private static final String dateTemplate = "MMMM yyyy";
    private String flag = "abc";
    private Shared_Common_Pref shared_common_pref;
    private Common_Class common_class;
    private List<Tp_View_Master> Tp_View_Master = new ArrayList<>();

    int CM, CY, NM, SelectedMonth, month, year;

    private ArrayList<TourPlan> tourPlanList;
    private RecyclerView.Adapter mTpMonthAdapter;

    private String mCurrentDateString;
    private int mCurrentDayOfMonth, mCurrentWeekDay;
    private Calendar mCalendar;
    private SimpleDateFormat dateFormat;
    private int daysInMonth;
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int DAY_OFFSET = 1;
    private int currentDayOfMonth;
    private TextView num_events_per_day;
    private HashMap<String, Integer> eventsPerMonthMap;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTpMonthSelectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // load global
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        gson = new Gson();
        common_class.getintentValues("Monthselection");

        // Initialize variable
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);
        calendarView = this.findViewById(R.id.gridcalander);
        CurrentMoth = findViewById(R.id.CurrentMoth);
        NextMonth = findViewById(R.id.NextMonth);
        ImageView backView = findViewById(R.id.imag_back);
        currentMonth = this.findViewById(R.id.title);
        goback = this.findViewById(R.id.imag_back);
        btnsubmit = findViewById(R.id.btnsubmit);

        CurrentMoth.setOnClickListener(this);
        NextMonth.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);

        // Animation button
        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();

        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();




        // New design code
        binding.currentMonthBtn.setOnClickListener(v -> {

//            final int sdk = android.os.Build.VERSION.SDK_INT;
//            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                binding.currentMonthBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_back_btn) );
//            } else {
//                binding.currentMonthBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_back_btn));
//            }
//
//            binding.currentArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.monthly_back_arrow_white));
//            binding.currentArrow.setScaleX(-1);
//
//            // next month
//            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                binding.nextMonthBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_back_btn_white) );
//            } else {
//                binding.nextMonthBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_back_btn_white));
//            }


            SelectedMonth = CM;

            // Initialize calendar
            Calendar calendar = Calendar.getInstance();
            CM = calendar.get(Calendar.MONTH);
            CY = calendar.get(Calendar.YEAR);
            NM = calendar.get(Calendar.MONTH) + 1;

            // Get current month
            String currrentmonth = common_class.GetMonthname(CM) + "   " + CY;

            // Get next month
            String nextmonth = "";
            if (CM == 11) {
                CY = CY + 1;
                nextmonth = common_class.GetMonthname(NM) + "   " + CY;
            } else
                nextmonth = common_class.GetMonthname(NM) + "   " + CY;

            Log.e(TAG, common_class.GetMonthname(calendar.get(Calendar.MONTH)));
            Log.e(TAG, String.valueOf(calendar.get(Calendar.MONTH)));

            // setText
            binding.month.setText(currrentmonth);
            tourPlanList.clear();
            getDateList(CM, year);
        });

        binding.nextMonthBtn.setOnClickListener(v -> {
            //   SelectedMonth = NM;

            // Initialize calendar
            Calendar calendar = Calendar.getInstance();
            CM = calendar.get(Calendar.MONTH);
            CY = calendar.get(Calendar.YEAR);
            NM = calendar.get(Calendar.MONTH) + 1;

            // Get current month
            String currrentmonth = common_class.GetMonthname(CM) + "   " + CY;

            // Get next month
            String nextmonth = "";
            if (CM == 11) {
                CY = CY + 1;
                nextmonth = common_class.GetMonthname(NM) + "   " + CY;
            } else
                nextmonth = common_class.GetMonthname(NM) + "   " + CY;

            Log.e(TAG, common_class.GetMonthname(calendar.get(Calendar.MONTH)));
            Log.e(TAG, String.valueOf(calendar.get(Calendar.MONTH)));

            // setText
            binding.month.setText(nextmonth);
            tourPlanList.clear();
            getDateList(NM, year);
        });

        imgHome.setOnClickListener(v -> {
            SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
            if (CheckIn == true) {
                Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                Dashboard.putExtra("Mode", "CIN");
                startActivity(Dashboard);
            } else
                startActivity(new Intent(getApplicationContext(), Dashboard.class));


        });
        txtHelp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Help_Activity.class)));
        txtErt.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ERT.class)));
        txtPlaySlip.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PayslipFtp.class)));
        backView.setOnClickListener(v -> mOnBackPressedDispatcher.onBackPressed());
        imgHome.setOnClickListener(v -> {
            SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
            if (CheckIn) {
                Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                Dashboard.putExtra("Mode", "CIN");
                startActivity(Dashboard);
            } else
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

        });
        binding.back.setOnClickListener(v -> mOnBackPressedDispatcher.onBackPressed());


        // NextMonth.setText(nextmonth);

        // get "Local" country calendar based date and time
        _calendar = Calendar.getInstance(Locale.getDefault());

        // get current year
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

        // if tp false submit button gone
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            btnsubmit.setVisibility(View.GONE);
        } else {
            btnsubmit.setVisibility(View.GONE);
        }

        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading.......");
        nDialog.setTitle("Tour Plan");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        GetTp_List();
        initRecyclerView();

        SelectedMonth = CM;

        // Initialize calendar
        Calendar calendar = Calendar.getInstance();
        CM = calendar.get(Calendar.MONTH);
        CY = calendar.get(Calendar.YEAR);
        NM = calendar.get(Calendar.MONTH) + 1;

        // Get current month
        String currrentmonth = common_class.GetMonthname(CM) + "   " + CY;

        // Get next month
        String nextmonth = "";
        if (CM == 11) {
            CY = CY + 1;
            nextmonth = common_class.GetMonthname(NM) + "   " + CY;
        } else
            nextmonth = common_class.GetMonthname(NM) + "   " + CY;

        Log.e(TAG, common_class.GetMonthname(calendar.get(Calendar.MONTH)));
        Log.e(TAG, String.valueOf(calendar.get(Calendar.MONTH)));

        binding.month.setText(currrentmonth);
        binding.yearIn.setText(String.valueOf(year));
        tourPlanList.clear();
        getDateList(CM + 1, year);
    }

    private void getDateList(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        mCalendar = (Calendar) calendar.clone();

        dateFormat = new SimpleDateFormat("MMM");
        mCurrentDateString = dateFormat.format(mCalendar.getTime());

        //     holder.mDate.setText(mCurrentDateString);

        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        int currentMonth = month - 1;
        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
        GregorianCalendar cal = new GregorianCalendar(year, currentMonth, 1);

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = year;
            nextYear = year + 1;
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = year - 1;
            nextYear = year;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = year;
            prevYear = year;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        }

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && month == 1) {
            ++daysInMonth;
        }

        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
        }

        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {
            if (CheckTp_View(i).equals("1") || CheckTp_View(i).equals("3")) {
                Log.e("getCurrentDayOfMonth", String.valueOf(i) + "-BLUE" + "-" + mCurrentDateString + "-" + year + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + year);
                if (CheckTp_View(i).equals("1")) {
                    Log.e("PENDING_COLOR", CheckTp_View(i));
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + year);
                } else {
                    Log.e("APPROVED_COLOR", CheckTp_View(i));
                    list.add(String.valueOf(i) + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + year);
                }
                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }*/
            } else {
                list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + year);
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

        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);

        for (String member : list){
            Log.i("Member name: ", member);
            TourPlan tourPlan = new TourPlan();
            tourPlan.setDay_month(member);
            tourPlanList.add(tourPlan);

            mTpMonthAdapter.notifyDataSetChanged();
        }



    }

    private void initRecyclerView() {
        tourPlanList = new ArrayList<>();
        mTpMonthAdapter = new TourPlanAdapter(context, SelectedMonth + 1, year, tourPlanList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        binding.recyclerView.setAdapter(mTpMonthAdapter);
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


    public void GetTp_List() {
        try {
            int SM = SelectedMonth + 1;

            String Tp_Object = "{\"tableName\":\"vwTourPlan\",\"coloumns\":\"[\\\"date\\\",\\\"remarks\\\",\\\"worktype_code\\\",\\\"worktype_name\\\",\\\"RouteCode\\\",\\\"RouteName\\\",\\\"Worked_with_Code\\\",\\\"Worked_with_Name\\\",\\\"JointWork_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            /*
               if approval flag false
                  assign Sf_Code
                  else
                  assign Tp_SFCode
             */
            String Sf_Code;
            if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
                Sf_Code = Shared_Common_Pref.Sf_Code;
            } else {
                Sf_Code = Shared_Common_Pref.Tp_SFCode;
            }

            Log.e(TAG, "Field force code: " + Sf_Code);
            Call<Object> mCall = apiInterface.GettpRespnse(Shared_Common_Pref.Div_Code,
                    Sf_Code, Sf_Code,
                    Shared_Common_Pref.StateCode,
                    String.valueOf(SM),
                    String.valueOf(year),
                    Tp_Object);

            mCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.body() == null){
                        Log.e(TAG, "retrofit response : " + "null");
                        return;
                    }

                    Log.e(TAG, "Get current month TP response retrofit : " + response.body());
                    Log.e(TAG, "TP response view retrofit : " + new Gson().toJson(response.body()));

                    userType = new TypeToken<ArrayList<Tp_View_Master>>() {}.getType();

                    Tp_View_Master = gson.fromJson(new Gson().toJson(response.body()), userType);

                    month = SelectedMonth + 1;

                    adapter.notifyDataSetChanged();

                    adapter = new GridCellAdapter(getApplicationContext(), R.id.date, month, year, (ArrayList<Tp_View_Master>) Tp_View_Master);
                    adapter.notifyDataSetChanged();
                    calendarView.setAdapter(adapter);

                    // new design code
                  //  getDateList(month, year);

                    nDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    nDialog.dismiss();
                }
            });

            try {
                adapter = new GridCellAdapter(getApplicationContext(), R.id.date, SelectedMonth + 1, year, (ArrayList<Tp_View_Master>) Tp_View_Master);
                adapter.notifyDataSetChanged();
                calendarView.setAdapter(adapter);

                // new design code
             //   getDateList(SelectedMonth + 1, year);
            } catch (Exception e) {
                Log.v("TP CALENDER:local", e.getMessage());

            }
        } catch (Exception e) {
            Log.v("TP CALENDER:", e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CurrentMoth:
                common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(CM));
                break;
            case R.id.NextMonth:
                common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(NM));
                break;
        }
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    common_class.CommonIntentwithFinish(Dashboard.class);
                }
            });

    @Override
    public void onBackPressed() {

    }

    @Override
    public void update(int value, int pos) {

    }

    // ///////////////////////////////////////////////////////////////////////////////////////
    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private final Context _context;
        private final List<String> list;
        private String itemvalue, curentDateString;

        private ArrayList<Tp_View_Master> Tp_View_Master;
        private ArrayList<String> items;

        private static final int DAY_OFFSET = 1;
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        private int daysInMonth, firstDay, currentDayOfMonth, currentWeekDay, maxWeeknumber, maxP, calMaxP, mnthlength;

        private TextView gridcell;
        private TextView num_events_per_day;
        private ImageView iv_icon;

        private Calendar selectedDate, month, pmonth, pmonthmaxset;

        private final HashMap<String, Integer> eventsPerMonthMap;
        private SimpleDateFormat df;

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<Tp_View_Master> Tp_View_Master) {
            super();
            this._context = context;
            this.Tp_View_Master = new ArrayList<>();
            this.Tp_View_Master = Tp_View_Master;
            this.list = new ArrayList<>();


            Calendar calendar = Calendar.getInstance();

            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            selectedDate = (Calendar) calendar.clone();

            df = new SimpleDateFormat("MMM");
            curentDateString = df.format(selectedDate.getTime());

            printMonth(month, year);

            // Find Number of Events
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

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.mCurrentDayOfMonth = currentDayOfMonth;
    }

    public void setCurrentWeekDay(int currentWeekDay) {
        this.mCurrentWeekDay = currentWeekDay;
    }

    public int getCurrentWeekDay() {
        return mCurrentWeekDay;
    }
    private int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    private String getMonthAsString(int i) {
        return months[i];
    }
    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        return map;
    }
}