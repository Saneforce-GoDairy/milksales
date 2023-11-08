package com.saneforce.godairy.Activity_Hap;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;
import static com.saneforce.godairy.Activity_Hap.Leave_Request.CheckInfo;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
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

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity.Util.UpdateUi;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.Tp_View_Master;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.TpClanderBinding;
import com.saneforce.godairy.fragments.tour_plan.CurrentMonthFragment;
import com.saneforce.godairy.fragments.tour_plan.NextMonthFragment;

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


public class Tp_Calander extends AppCompatActivity implements View.OnClickListener, UpdateUi {
    private TpClanderBinding binding;
    private Button selectedDayMonthYearButton;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "dd-MMM-yyyy");
    public Button btnsubmit;
    public ImageView goback, backarow, imgHome;
    private RelativeLayout prevMonth, nextMonth;
    private GridView calendarView;
    private Tp_Calander.GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year;
    private static final String dateTemplate = "MMMM yyyy";
    private String flag = "abc";
    private ApiInterface apiService;
    private Shared_Common_Pref shared_common_pref;
    private Common_Class common_class;
    private List<Tp_View_Master> Tp_View_Master = new ArrayList<>();
    private Type userType;
    int SelectedMonth;
    private Gson gson;
    private ProgressDialog progressDialog;
    private TextView txtHelp, txtErt, txtPlaySlip, currentMonth, CurrentYear;

    // get current month / next month
    int CM, CY;
    int NM;

    private Context context = this;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TpClanderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        gson = new Gson();

        initVariable();
        initOnClick();
        initAnimation();
        initProgressbar();
        checkApprovalFlag();
        getCurrentMonthAndNextMonth(); // CM , NM
        getCurrentYear(); // yyyy
        loadFragment();

        SelectedMonth = CM;
        currentMonth.setText(common_class.GetMonthname(CM));
        CurrentYear.setText(String.valueOf(year));

        // Load tour plan
        progressDialog.show();
        GetTp_List();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (state == SCROLL_STATE_DRAGGING && binding.viewPager.getCurrentItem() == 0) {
                    binding.viewPager.setUserInputEnabled(false);
                } else {
                    binding.viewPager.setUserInputEnabled(true);
                }
            }
        });
    }

    private void loadFragment() {
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.viewPager.setAdapter(myViewPagerAdapter);
    }

    public static class MyViewPagerAdapter  extends FragmentStateAdapter {
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new CurrentMonthFragment();
                case 1:
                    return new NextMonthFragment();

                default:
                    return new CurrentMonthFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
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

        Log.d("month_", "Current Month :" + CM);
        Log.d("month_", "Next Month :" + NM);
    }

    private void checkApprovalFlag() {
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            btnsubmit.setVisibility(View.GONE);
        } else {
            btnsubmit.setVisibility(View.GONE);
        }
    }

    private void initProgressbar() {
        progressDialog = new ProgressDialog(Tp_Calander.this);
        progressDialog.setMessage("Loading.......");
        progressDialog.setTitle("Tour Plan");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
    }

    private void initAnimation() {
        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
    }

    private void initOnClick() {
        binding.currentMonthBtn1.setOnClickListener(v -> {

            binding.viewPager.setCurrentItem(0);
            currentMonth.setText(common_class.GetMonthname(CM));

            // Primary button enable logic
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthBtn1.setBackgroundDrawable(null);
            } else {
                binding.currentMonthBtn1.setBackground(null);
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthBtn1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
            } else {
                binding.nextMonthBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.monthly_back_arrow_white));
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                binding.nextMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.monthly_back_arrow_white));
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            binding.nextMonthArrowImg.setScaleX(1);
            binding.currentMonthArrowImg.setScaleX(1);

            GetTp_List();
        });
        binding.nextMonthBtn1.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(1);
            currentMonth.setText(common_class.GetMonthname(NM));

                        final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthBtn1.setBackgroundDrawable(null);
            } else {
                binding.nextMonthBtn1.setBackground(null);
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthBtn1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
            } else {
                binding.currentMonthBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
            }

            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.currentMonthArrowImg.setScaleX(1);
            } else {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.currentMonthArrowImg.setScaleX(1);
            }
        });

//        binding.nextMonthBtn1.setOnClickListener(v -> {
//
//            int size1 = tourPlanList.size();
//            if (size1 > 0) {
//                for (int i = 0; i < size1; i++) {
//                    tourPlanList.remove(0);
//                }
//
//                mTpMonthAdapter.notifyItemRangeRemoved(0, size1);
//            }
//
//            list.clear();
//
//            final int sdk = Build.VERSION.SDK_INT;
//            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                binding.currentMonthBtn1.setBackgroundDrawable(null);
//            } else {
//                binding.currentMonthBtn1.setBackground(null);
//            }
//
//            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                binding.nextMonthBtn1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
//            } else {
//                binding.nextMonthBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
//            }
//
//            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
//            } else {
//                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
//            }
//
//            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
//                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
//                binding.currentMonthArrowImg.setScaleX(1);
//            } else {
//                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
//                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
//                binding.currentMonthArrowImg.setScaleX(1);
//            }
//
//            int size = tourPlanList.size();
//            if (size > 0) {
//                for (int i = 0; i < size; i++) {
//                    tourPlanList.remove(0);
//                }
//
//                mTpMonthAdapter.notifyItemRangeRemoved(0, size);
//            }
//
//
//        });

        txtHelp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Help_Activity.class)));
        txtErt.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ERT.class)));
        txtPlaySlip.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PayslipFtp.class)));
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
        goback.setOnClickListener(v -> mOnBackPressedDispatcher.onBackPressed());
        btnsubmit.setOnClickListener(this);
    }

    private void initVariable() {
        txtHelp = findViewById(R.id.toolbar_help);
        imgHome = findViewById(R.id.toolbar_home);
        txtErt = findViewById(R.id.toolbar_ert);
        txtPlaySlip = findViewById(R.id.toolbar_play_slip);
        currentMonth = this.findViewById(R.id.month);
        goback = this.findViewById(R.id.imag_back);
        btnsubmit = findViewById(R.id.btnsubmit);
        calendarView = this.findViewById(R.id.gridcalander);
        CurrentYear = findViewById(R.id.year_in);
    }

    public void GetTp_List() {
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
                    userType = new TypeToken<ArrayList<Tp_View_Master>>() {
                    }.getType();
                    Tp_View_Master = gson.fromJson(new Gson().toJson(response.body()), userType);

                    month = SelectedMonth + 1;

                    adapter = new Tp_Calander.GridCellAdapter(getApplicationContext(), month, year, (ArrayList<com.saneforce.godairy.Model_Class.Tp_View_Master>) Tp_View_Master);
                    adapter.notifyDataSetChanged();
                    calendarView.setAdapter(adapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressDialog.dismiss();
                }

            });
                adapter = new Tp_Calander.GridCellAdapter(getApplicationContext(), SelectedMonth + 1, year, (ArrayList<com.saneforce.godairy.Model_Class.Tp_View_Master>) Tp_View_Master);
                adapter.notifyDataSetChanged();
                calendarView.setAdapter(adapter);
    }

    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new Tp_Calander.GridCellAdapter(getApplicationContext(), month, year, (ArrayList<com.saneforce.godairy.Model_Class.Tp_View_Master>) Tp_View_Master);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(android.text.format.DateFormat.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imag_back:
                common_class.CommonIntentwithFinish(Tp_Month_Select.class);
                break;

            case R.id.btnsubmit:
                int SM = SelectedMonth + 1;
                common_class.GetTP_Result("TourPlanSubmit", "", SM, year);

                break;
        }
    }

    @Override
    public void update(int value, int pos) {

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
        private TextView num_events_per_day;
        private String  curentDateString;
        private Calendar selectedDate;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private SimpleDateFormat df;

        public GridCellAdapter(Context context, int month, int year, ArrayList<Tp_View_Master> Tp_View_Master) {
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
                    Log.e("getCurrentDayOfMonth", i + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
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

                Log.e("tp_calander_", "Day of month :" + list.get(i - 1));
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("tp_calander_", "Day color : " + list.get(i));
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<>();
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

            gridcell = row.findViewById(R.id.date);

            // ACCOUNT FOR SPACING
            String[] day_color = list.get(position).split("-");

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

            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
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
            gridcell.setOnClickListener(v -> {
                String[] day_color1 = list.get(position).split("-");
                String theday1 = day_color1[0];
                String themonth1 = day_color1[2];
                String theyear1 = day_color1[3];
                int month = SelectedMonth + 1;
                String TourMonth = theyear1 + "-" + month + "-" + theday1;
                common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));
            });

            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
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

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    if (Shared_Common_Pref.Tp_Approvalflag.equals("1")) {
                        common_class.CommonIntentwithFinish(Tp_Approval.class);
                    } else {
                        common_class.CommonIntentwithFinish(Dashboard.class);
                    }

                }
            });
}
