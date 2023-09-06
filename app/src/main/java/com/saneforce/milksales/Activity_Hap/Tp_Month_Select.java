package com.saneforce.milksales.Activity_Hap;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;
import static com.saneforce.milksales.Common_Class.Common_Class.addquote;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity.Util.UpdateUi;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Joint_Work_Listner;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.MVP.Main_Model;
import com.saneforce.milksales.Model_Class.ModeOfTravel;
import com.saneforce.milksales.Model_Class.Tp_Dynamic_Modal;
import com.saneforce.milksales.Model_Class.Tp_View_Master;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.Joint_Work_Adapter;

import com.saneforce.milksales.common.DatabaseHandler;
import com.saneforce.milksales.common.TourPlan;
import com.saneforce.milksales.databinding.ActivityTpMonthSelectBinding;
import com.saneforce.milksales.databinding.CalendarItemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tp_Month_Select extends AppCompatActivity implements View.OnClickListener, UpdateUi , Master_Interface {
    private ActivityTpMonthSelectBinding binding;
    private final Context context = this;
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
    List<Common_Model> Jointworklistview = new ArrayList<>();
    String worktype_id;

    EditText edt_remarks;
    String Fieldworkflag = "";
    TextView worktype_text;
    String mWorkType;
    String modeId = "";
    String STRCode = "";
    String toId = "";
    String modeVal = "";
    boolean ExpNeed = false;

    List<Common_Model> worktypelist = new ArrayList<>();
    Common_Model Model_Pojo;
    TextView text_tour_plancount;
    Joint_Work_Adapter adapter2;

    List<Common_Model> Route_Masterlist = new ArrayList<>();
    List<Common_Model> FRoute_Master = new ArrayList<>();
    LinearLayout worktypelayout, distributors_layout, route_layout;
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    List<Common_Model> ChillingCenter_List = new ArrayList<>();
    List<Common_Model> Shift_Typelist = new ArrayList<>();
    List<Common_Model> Savejointwork = new ArrayList<>();

    private Main_Model.presenter presenter;
    android.app.DatePickerDialog DatePickerDialog;
    TimePickerDialog timePickerDialog;
    ArrayList<Tp_Dynamic_Modal> Tp_dynamicArraylist = new ArrayList<>();

    EditText empidedittext;
    String Worktype_Button = "", hqid, shifttypeid, Chilling_Id;
    Button submitbutton, GetEmpId;
    CustomListViewDialog customDialog;
    ProgressBar progressbar;

    TextView  Sf_name, distributor_text, route_text, hq_text, shift_type, chilling_text, Remarkscaption;
    TextView tourdate, txtOthPlc;
    LinearLayout BusTo, jointwork_layout, hqlayout, shiftypelayout, Procrumentlayout, chillinglayout;
    RecyclerView jointwork_recycler;
    CardView ModeTravel, card_Toplace, CardDailyAllowance, card_from, CardOthPlc;
    EditText BusFrom, reason;

    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    String DM = "", DriverNeed = "false", DriverMode = "", modeTypeVale = "", mode = "", imageURI = "",  StartedKM = "", FromKm = "", ToKm = "", Fare = "", strDailyAllowance = "", strDriverAllowance = "", StToEnd = "", StrID = "";
    private ArrayList<String> travelTypeList;
    CheckBox driverAllowance;
    List<ModeOfTravel> modelOfTravel;
    List<Common_Model> modelTravelType = new ArrayList<>();
    TextView TextMode, TextToAddress, dailyAllowance;
    LinearLayout linCheckdriver, vwExpTravel;
    List<Common_Model> listOrderType = new ArrayList<>();
    Common_Model mCommon_model_spinner;

    String startEnd = "";
    LinearLayout MdeTraval, DailyAll, frmPlace, ToPlace, Approvereject, rejectonly;
    int jcountglobal = 0;
    LinearLayout Dynamictpview;
    RecyclerView dynamicrecyclerview;
    ArrayList<Tp_Dynamic_Modal> dynamicarray = new ArrayList<>();
    DynamicViewAdapter dynamicadapter;
    Button tpapprovebutton, tp_rejectsave, tpreject;

    DatabaseHandler db;
    String TourMonth;
    String theday = "";


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


        binding.currentMonthBtn1.setOnClickListener(v -> {

            // Primary button enable logic
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthBtn1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
            } else {
                binding.currentMonthBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            // Secondary button disable logic logic
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthBtn1.setBackgroundDrawable(null);
            } else {
                binding.nextMonthBtn1.setBackground(null);
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.monthly_back_arrow_white));
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                binding.nextMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.monthly_back_arrow_white));
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            binding.nextMonthArrowImg.setScaleX(1);
            binding.currentMonthArrowImg.setScaleX(1);

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
        binding.nextMonthBtn1.setOnClickListener(v -> {

            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthBtn1.setBackgroundDrawable(null);
            } else {
                binding.currentMonthBtn1.setBackground(null);
            }

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthBtn1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
            } else {
                binding.nextMonthBtn1.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
            }

            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                binding.nextMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.currentMonthArrowImg.setScaleX(1);
            } else {
                binding.currentMonthArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tp_current_month_enabled_arrow));
                binding.currentMonthArrowImg.setColorFilter(ContextCompat.getColor(context, R.color.monthly_arrow), android.graphics.PorterDuff.Mode.SRC_IN);
                binding.currentMonthArrowImg.setScaleX(1);
            }


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
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.mainContainer2.setVisibility(View.VISIBLE);
                binding.submit.setText("SUBMIT");

                if (theday.equals("")){
                    Toast.makeText(context, "Please select date", Toast.LENGTH_SHORT).show();
                    return;
                }

                String mWorkName = binding.workType.getSelectedItem().toString();
                if (mWorkName.equals("Select")){
                    Toast.makeText(context, "Please select work type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (vali()) {
                    Tp_Submit("0");
                }
            }
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

        /*
           Load current month
         */

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

        // Previous code hide xml
        progressbar = findViewById(R.id.progressbar);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        Approvereject = findViewById(R.id.Approvereject);
        rejectonly = findViewById(R.id.rejectonly);
        tp_rejectsave = findViewById(R.id.tp_rejectsave);
        tpreject = findViewById(R.id.tpreject);
        Sf_name = findViewById(R.id.Sf_name);
        tpapprovebutton = findViewById(R.id.tpapprovebutton);
        reason = findViewById(R.id.reason);
        edt_remarks = findViewById(R.id.edt_remarks);
        Dynamictpview = findViewById(R.id.Dynamictpview);
        vwExpTravel = findViewById(R.id.vwExpTravel);
        CardOthPlc = findViewById(R.id.CardOthPlc);
        txtOthPlc = findViewById(R.id.txtOthPlc);

        dynamicrecyclerview = findViewById(R.id.dynamicrecyclerview);
        dynamicrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        tourdate = findViewById(R.id.tourdate);

//        String[] TP_Dt = TourMonth.split("-");
//        tourdate.setText(TP_Dt[2] + "/" + TP_Dt[1] + "/" + TP_Dt[0]);

        route_text = findViewById(R.id.route_text);
        worktypelayout = findViewById(R.id.worktypelayout);
        distributors_layout = findViewById(R.id.distributors_layout);
        ModeTravel = findViewById(R.id.card_travel_mode);
        card_Toplace = findViewById(R.id.card_Toplace);
        Remarkscaption = findViewById(R.id.remarkscaption);
        chilling_text = findViewById(R.id.chilling_text);
        Procrumentlayout = findViewById(R.id.Procrumentlayout);
        hq_text = findViewById(R.id.hq_text);
        shift_type = findViewById(R.id.shift_type);
        route_layout = findViewById(R.id.route_layout);
        submitbutton = findViewById(R.id.submitbutton);
        worktype_text = findViewById(R.id.worktype_text);
        distributor_text = findViewById(R.id.distributor_text);
        text_tour_plancount = findViewById(R.id.text_tour_plancount);
        text_tour_plancount.setText("0");

        loadWorkTypes();

        jointwork_layout = findViewById(R.id.jointwork_layout);
        jointwork_recycler = findViewById(R.id.jointwork_recycler);
        jointwork_recycler.setLayoutManager(new LinearLayoutManager(this));

        // joint_work_Recyclerview = findViewById(R.id.joint_work_listlt);
        MdeTraval = findViewById(R.id.mode_of_travel);
        DailyAll = findViewById(R.id.lin_daily);
        frmPlace = findViewById(R.id.lin_from);
        ToPlace = findViewById(R.id.lin_to_place);
        GetEmpId = findViewById(R.id.GetEmpId);
        empidedittext = (EditText) findViewById(R.id.empidedittext);
        BusTo = findViewById(R.id.lin_to_place);
        GetEmpId.setOnClickListener(this);
        tpapprovebutton.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        card_Toplace.setOnClickListener(this);
        BusFrom = findViewById(R.id.edt_frm);
        TextMode = findViewById(R.id.txt_mode);
        TextToAddress = findViewById(R.id.edt_to);
        CardDailyAllowance = findViewById(R.id.card_daily_allowance);
        dailyAllowance = findViewById(R.id.text_daily_allowance);
        driverAllowance = findViewById(R.id.da_driver_allowance);
        linCheckdriver = findViewById(R.id.lin_check_driver);

        tpreject.setOnClickListener(this);
        tp_rejectsave.setOnClickListener(this);
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            submitbutton.setText("Submit");
            submitbutton.setVisibility(View.VISIBLE);
            Sf_name.setVisibility(View.GONE);
            Approvereject.setVisibility(View.GONE);
        } else {
            submitbutton.setText("Edit");
            submitbutton.setVisibility(View.GONE);
            Sf_name.setText("" + Shared_Common_Pref.Tp_Sf_name);
            Sf_name.setVisibility(View.VISIBLE);
            Approvereject.setVisibility(View.VISIBLE);

        }

        Get_MydayPlan(common_class.getintentValues("TourDate"));
    }

//    @Override
//    public void setDataToRouteObject(Object noticeArrayList, int position) {
//        if (position == 0) {
//            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
//            GetJsonData(new Gson().toJson(noticeArrayList), "0");
//        } else if (position == 1) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "1");
//        } else if (position == 2) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "2");
//        } else if (position == 3) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "3");
//        } else if (position == 4) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "4");
//        } else if (position == 5) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "5");
//            //Get_MydayPlan(common_class.getintentValues("TourDate"));
//        } else {
//            GetJsonData(new Gson().toJson(noticeArrayList), "6");
//            common_class.ProgressdialogShow(1, "Day plan");
//            Get_MydayPlan(common_class.getintentValues("TourDate"));
//        }
//
//    }

//    @Override
//    public void onResponseFailure(Throwable throwable) {
//
//    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        Log.e("LogWorktype", String.valueOf(type));
        if (type == -1) {
            worktype_text.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Log.e("FIELD_WORK", myDataset.get(position).getFlag());
            Log.e("Button_Access", myDataset.get(position).getCheckouttime());
            Fieldworkflag = myDataset.get(position).getFlag();
            Worktype_Button = myDataset.get(position).getCheckouttime();
            ExpNeed = myDataset.get(position).getExpNeed();
            Log.e("LogWorktype", String.valueOf(myDataset.get(position).getId()));
            jointwork_layout.setVisibility(View.GONE);
            Jointworklistview.clear();
            GetTp_Worktype_Fields(Worktype_Button);
            Log.e("FIELD_Dept_Type", Shared_Common_Pref.Dept_Type);
            vwExpTravel.setVisibility(View.VISIBLE);
            if (ExpNeed == false) {
                vwExpTravel.setVisibility(View.GONE);
            }
        } else if (type == 7) {
            BusFrom.setText(myDataset.get(position).getName());
            shifttypeid = myDataset.get(position).getId();
        } else if (type == 102) {
            TextToAddress.setText(myDataset.get(position).getName());
            toId = myDataset.get(position).getId();
            CardOthPlc.setVisibility(View.GONE);
            if (toId.equalsIgnoreCase("-1")) {
                CardOthPlc.setVisibility(View.VISIBLE);
            }
        } else if (type == 100) {
            TextMode.setText(myDataset.get(position).getName());
            DriverMode = myDataset.get(position).getCheckouttime();
            modeId = myDataset.get(position).getFlag();
            Log.e("Dash_Mode_Count", DriverMode);
            startEnd = myDataset.get(position).getId();
            if (startEnd.equals("0")) {
                mode = "11";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                BusFrom.setText("");
                TextToAddress.setText("");
            } else {
                mode = "12";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                BusFrom.setText("");
                TextToAddress.setText("");
            }
            if (DriverMode.equals("1")) {
                linCheckdriver.setVisibility(View.VISIBLE);
            } else {
                linCheckdriver.setVisibility(View.GONE);
            }
            DriverNeed = "";
            driverAllowance.setChecked(false);
        } else if (type == 10) {
            TextToAddress.setText(myDataset.get(position).getName());
        } else if (type == 101) {
            String TrTyp = myDataset.get(position).getName();
            dailyAllowance.setText(TrTyp);
            if (TrTyp.equals("HQ")) {
                BusTo.setVisibility(View.GONE);
            } else {
                BusTo.setVisibility(View.VISIBLE);
            }
            TextToAddress.setText("");
        } else {
            Log.e("Selectedposition", "" + type);
            dynamicarray.get(type).setFilter_Value(myDataset.get(position).getName());
            dynamicarray.get(type).setFilter_Text(myDataset.get(position).getId());
            dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
            dynamicrecyclerview.setAdapter(dynamicadapter);
            dynamicadapter.notifyDataSetChanged();
            dynamicrecyclerview.setItemViewCacheSize(dynamicarray.size());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbutton:
                if (vali()) {
                    Tp_Submit("0");
                }
                break;
            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(Tp_Month_Select.this, worktypelist, -1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                Log.e("Work_Type_List", String.valueOf(worktypelist));
                break;
//            case R.id.chilling_layout:
//                customDialog = new CustomListViewDialog(Tp_Month_Select.this, ChillingCenter_List, 6);
//                Window chillwindow = customDialog.getWindow();
//                chillwindow.setGravity(Gravity.CENTER);
//                chillwindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                customDialog.show();
//                break;
            case R.id.GetEmpId:
                if (empidedittext.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter the EMP_Id", Toast.LENGTH_SHORT).show();
                } else {
                    GetEmpList();
                }
                break;
            case R.id.card_Toplace:
                customDialog = new CustomListViewDialog(Tp_Month_Select.this, getfieldforcehqlist, 102);
                Window chillwindowww = customDialog.getWindow();
                chillwindowww.setGravity(Gravity.CENTER);
                chillwindowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.tpapprovebutton:
                Tp_Submit("1");
                break;
            case R.id.tpreject:
                rejectonly.setVisibility(View.VISIBLE);
                Approvereject.setVisibility(View.INVISIBLE);
                break;
            case R.id.tp_rejectsave:
                if (reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter The Reason", Toast.LENGTH_SHORT).show();
                } else {
                    SendtpApproval("NTPApprovalR", 2);
                }
                break;
        }
    }

    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String ETabs = jsonObject1.optString("ETabs");
                Model_Pojo = new Common_Model(id, name, flag);
                if (type.equals("0")) {
                    String PlInv = jsonObject1.optString("Place_Involved");
                    boolean tExpNeed = (PlInv.equalsIgnoreCase("Y") ? true : false);
                    Model_Pojo = new Common_Model(id, name, flag, ETabs, tExpNeed);
                    worktypelist.add(Model_Pojo);
                    Log.e("WORK_TYPE", String.valueOf(worktypelist));
                } else if (type.equals("1")) {
                    distributor_master.add(Model_Pojo);
                } else if (type.equals("2")) {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                } else if (type.equals("3")) {
                      /*  Model_Pojo = new Common_Model(name + "-" + jsonObject1.optString("desig"), id, false);
                        Jointworklistview.add(Model_Pojo);*/
                } else if (type.equals("4")) {
                    String sid = jsonObject1.optString(("id"));
                    String Odflag = jsonObject1.optString("ODFlag");
                    Model_Pojo = new Common_Model(sid, name, Odflag);
                    getfieldforcehqlist.add(Model_Pojo);
                } else if (type.equals("5")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    Shift_Typelist.add(Model_Pojo);
                } else {
                    Model_Pojo = new Common_Model(id, name, flag);
                    ChillingCenter_List.add(Model_Pojo);
                }

            }
            if (type.equalsIgnoreCase("4")) {
                Model_Pojo = new Common_Model("-1", "Other Location", "");
                getfieldforcehqlist.add(Model_Pojo);
            }
            if (type.equals("3")) {
                jointwork_recycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int po, boolean flag) {
                        Jointworklistview.get(po).setSelected(flag);
                        int jcount = 0;
                        for (int i = 0; Jointworklistview.size() > i; i++) {
                            if (Jointworklistview.get(i).isSelected()) {
                                jcount = jcount + 1;
                            }

                        }
                        text_tour_plancount.setText(String.valueOf(jcount));
                    }
                }));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Tp_Submit(String Submit_Flag) {
        if(worktype_id==null){
            Toast.makeText(this, "Work Type Not Selected", Toast.LENGTH_SHORT).show();
            return;
        }
        Savejointwork = Jointworklistview;
        Log.e("Savejointwork_SIZE", String.valueOf(Savejointwork.size()));
        String jointwork = "";
        String jointworkname = "";
        for (int ii = 0; ii < Savejointwork.size(); ii++) {
            if (ii != 0) {
                jointwork = jointwork.concat(",");
                jointworkname = jointworkname.concat(",");
            }
            Log.e("JOINT_WORK_SELECT_NAME", Savejointwork.get(ii).getName());
            jointwork = jointwork.concat(Savejointwork.get(ii).getId());
            jointworkname = jointworkname.concat(Savejointwork.get(ii).getName());
        }
        Log.e("JOINT_WORK", jointwork);
        common_class.ProgressdialogShow(1, "Tour  plan");
        Calendar c = Calendar.getInstance();
        String Dcr_Dste = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date());
        JSONArray jsonarr = new JSONArray();
        JSONObject jsonarrplan = new JSONObject();
        String remarks = edt_remarks.getText().toString();
        try {
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("worktype_code", addquote(worktype_id));
            jsonobj.put("Tour_Date", TourMonth);
            jsonobj.put("worktype_name", mWorkType);
            jsonobj.put("Ekey", Common_Class.GetEkey());
            jsonobj.put("objective", addquote(remarks));
            jsonobj.put("Flag", addquote(Fieldworkflag));
            jsonobj.put("Button_Access", Worktype_Button);
            jsonobj.put("MOT", addquote(TextMode.getText().toString()));
            jsonobj.put("DA_Type", addquote(dailyAllowance.getText().toString()));
            jsonobj.put("Driver_Allow", addquote((driverAllowance.isChecked()) ? "1" : "0"));
            jsonobj.put("From_Place", addquote(BusFrom.getText().toString()));
            if (toId.equalsIgnoreCase("-1")) {
                jsonobj.put("To_Place", txtOthPlc.getText());
            } else {
                jsonobj.put("To_Place", addquote(TextToAddress.getText().toString()));
            }
            jsonobj.put("MOT_ID", addquote(modeId));
            jsonobj.put("To_Place_ID", addquote(toId));
            jsonobj.put("Mode_Travel_ID", addquote(startEnd));
            jsonobj.put("worked_with", addquote(jointworkname));
            jsonobj.put("jointWorkCode", addquote(jointwork));
            JSONArray personarray = new JSONArray();
            JSONObject ProductJson_Object;
            for (int z = 0; z < dynamicarray.size(); z++) {
                ProductJson_Object = new JSONObject();
                try {
                    ProductJson_Object.put("Fld_ID", dynamicarray.get(z).getFld_ID());
                    ProductJson_Object.put("Fld_Name", dynamicarray.get(z).getFld_Name());
                    ProductJson_Object.put("Fld_Type", dynamicarray.get(z).getFld_Type());
                    ProductJson_Object.put("Fld_Src_Name", dynamicarray.get(z).getFld_Src_Name());
                    ProductJson_Object.put("Fld_Src_Field", dynamicarray.get(z).getFld_Src_Field());
                    ProductJson_Object.put("Fld_Length", dynamicarray.get(z).getFld_Length());
                    ProductJson_Object.put("Fld_Symbol", dynamicarray.get(z).getFld_Symbol());
                    ProductJson_Object.put("Fld_Mandatory", dynamicarray.get(z).getFld_Mandatory());
                    ProductJson_Object.put("Active_flag", dynamicarray.get(z).getActive_flag());
                    ProductJson_Object.put("Control_id", dynamicarray.get(z).getControl_id());
                    ProductJson_Object.put("Target_Form", dynamicarray.get(z).getTarget_Form());
                    ProductJson_Object.put("Filter_Text", dynamicarray.get(z).getFilter_Text());
                    ProductJson_Object.put("Filter_Value", dynamicarray.get(z).getFilter_Value());
                    ProductJson_Object.put("Field_Col", dynamicarray.get(z).getField_Col());
                    if (dynamicarray.get(z).getFld_Symbol().equals("D")) {
                        jsonobj.put("Worked_with_Code", dynamicarray.get(z).getFilter_Text());
                        jsonobj.put("Worked_with_Name", dynamicarray.get(z).getFilter_Value());
                    } else if (dynamicarray.get(z).getFld_Symbol().equals("R")) {
                        jsonobj.put("RouteCode", dynamicarray.get(z).getFilter_Text());
                        jsonobj.put("RouteName", dynamicarray.get(z).getFilter_Value());
                    }
                    personarray.put(ProductJson_Object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonarrplan.put("Tour_Plan", jsonobj);
            jsonarrplan.put("Tp_DynamicValues", personarray);
            jsonarr.put(jsonarrplan);
            String Sf_Code;
            if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
                Sf_Code = Shared_Common_Pref.Sf_Code;
            } else {
                Sf_Code = Shared_Common_Pref.Tp_SFCode;
            }
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("sfCode", Sf_Code);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            QueryString.put("Approval_MGR", Shared_Common_Pref.Tp_Approvalflag);
            QueryString.put("desig", "MGR");
            Log.e("QueryString", String.valueOf(QueryString));
            Log.e("QueryString_SF", Sf_Code);
            Log.e("QueryString_DV", Shared_Common_Pref.Div_Code);
            Log.e("QueryString_Sc", Shared_Common_Pref.StateCode);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Object> Callto = apiInterface.Tb_Mydayplannew(QueryString, jsonarr.toString());
            Callto.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.e("RESPONSE_FROM_SERVER", response.body().toString());
                    common_class.ProgressdialogShow(2, "Tour  plan");
                    if (response.code() == 200 || response.code() == 201) {
                        common_class.GetTP_Result("TourPlanSubmit", "", common_class.getintentValues("SubmitMonth"), common_class.getintentValues("TourYear"));

                        if (Submit_Flag.equals("1")) {
                            SendtpApproval("NTPApproval", 1);
                        } else {
                          //  common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(common_class.getintentValues("TourMonth")));
                            finish();
                            Toast.makeText(Tp_Month_Select.this, "Work Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    common_class.ProgressdialogShow(2, "Tour  plan");
                    Log.e("Reponse TAG", "onFailure : " + t.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Get_MydayPlan(String tourDate) {
        String Sf_Code = "";
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            Sf_Code = Shared_Common_Pref.Sf_Code;
        } else {
            Sf_Code = Shared_Common_Pref.Tp_SFCode;
        }
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "Get/Tp_dayplan");
        QueryString.put("Sf_code", Sf_Code);
        QueryString.put("Date", tourDate);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        Log.e("Log_TpQuerySTring", QueryString.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("GettodayResult", "response Tp_View: " + jsonObject.getJSONArray("GettodayResult"));
                    Log.e("DynamicViewes", "response Tp_View: " + jsonObject.getJSONArray("DynamicViews"));
                    JSONArray jsoncc = jsonObject.getJSONArray("GettodayResult");
                    Log.e("LENGTH", String.valueOf(jsoncc.length()));
//                    jointwork_layout.setVisibility(View.GONE);
                    Jointworklistview.clear();
                    if (jsoncc.length() > 0) {
                        worktype_id = String.valueOf(jsoncc.getJSONObject(0).get("worktype_code"));
                        edt_remarks.setText(String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                        Fieldworkflag = String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag"));
                        worktype_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                        modeId = String.valueOf(jsoncc.getJSONObject(0).get("Mot_ID"));
                        STRCode = String.valueOf(jsoncc.getJSONObject(0).get("To_Place_ID"));
                        toId = String.valueOf(jsoncc.getJSONObject(0).get("To_Place_ID"));
                        modeVal = String.valueOf(jsoncc.getJSONObject(0).get("Mode_Travel_Id"));
                        ExpNeed = false;
                        for (int ij = 0; ij < worktypelist.size(); ij++) {
                            if (worktype_id.equalsIgnoreCase(worktypelist.get(ij).getId())) {
                                ExpNeed = worktypelist.get(ij).getExpNeed();
                            }
                        }

                        vwExpTravel.setVisibility(View.VISIBLE);
                        if (ExpNeed == false) {
                            vwExpTravel.setVisibility(View.GONE);
                        }

                        String Jointworkcode = String.valueOf(jsoncc.getJSONObject(0).get("JointworkCode"));
                        String JointWork_Name = String.valueOf(jsoncc.getJSONObject(0).get("JointWork_Name"));
                        String[] arrOfStr = Jointworkcode.split(",");
                        String[] arrOfname = JointWork_Name.split(",");
                        //Model_Pojo = new Common_Model(arrOfStr.get("Sf_Name").getAsString() + "-" + EmpDet.get("sf_Designation_Short_Name").getAsString(), EmpDet.get("Sf_Code").getAsString(), false);
                        //Log.e("JOINTWORKCode", String.valueOf(arrOfStr.length));
                        if (!Jointworkcode.equals("")) {
                            Jointworklistview.clear();
                            for (int ik = 0; arrOfStr.length > ik; ik++) {
                                Model_Pojo = new Common_Model(arrOfname[ik], arrOfStr[ik], false);
                                Jointworklistview.add(Model_Pojo);
                            }
                            if (Jointworklistview.size() > 0) {

                                  jointwork_layout.setVisibility(View.VISIBLE);

                                text_tour_plancount.setText(String.valueOf(arrOfStr.length));
                                adapter2 = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                                    @Override
                                    public void onIntentClick(int position, boolean flag) {
                                        Jointworklistview.remove(position);
                                        text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                                        adapter2.notifyDataSetChanged();
                                    }
                                });
                                jointwork_recycler.setAdapter(adapter2);
                            }
                        }

                        TextMode.setText(modeTypeVale);
                        TextMode.setText(String.valueOf(jsoncc.getJSONObject(0).get("MOT")));
                        BusFrom.setText(String.valueOf(jsoncc.getJSONObject(0).get("From_Place")));
                        TextToAddress.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                        dailyAllowance.setText(String.valueOf(jsoncc.getJSONObject(0).get("DA_Type")));
                        CardOthPlc.setVisibility(View.GONE);
                        if (toId.equalsIgnoreCase("-1")) {
                            CardOthPlc.setVisibility(View.VISIBLE);
                            TextToAddress.setText("Other Location");
                            txtOthPlc.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                        }
                        if (jsoncc.getJSONObject(0).get("DA_Type").equals("HQ")) {
                            BusTo.setVisibility(View.GONE);
                        } else {
                            BusTo.setVisibility(View.VISIBLE);
                        }
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Driver_Allow")).equals("1")) {
                            linCheckdriver.setVisibility(View.VISIBLE);
                            driverAllowance.setChecked(true);
                        } else {
                            linCheckdriver.setVisibility(View.GONE);
                            driverAllowance.setChecked(false);
                        }

                        Tp_dynamicArraylist.clear();
                        //jsonData = response.body();
                        Log.e("response_data", "thiru" + jsonObject.getJSONArray("DynamicViews"));

                        JSONArray jsnArValue = jsonObject.getJSONArray("DynamicViews");
                        Log.v("AfterTpresponse", jsnArValue.toString());
                        for (int i = 0; i < jsnArValue.length(); i++) {
                            JSONObject json_oo = jsnArValue.getJSONObject(i);
                            Log.e("Json_Filed", String.valueOf(json_oo.getJSONArray("inputs")));
                            ArrayList<Common_Model> a_listt = new ArrayList<>();
                            ArrayList<Common_Model> a_list = new ArrayList<>();
                            if (json_oo.getJSONArray("inputs") != null) {
                                JSONArray jarray = json_oo.getJSONArray("inputs");
                                a_listt.clear();
                                String[] txtArray = json_oo.getString("Fld_Src_Field").split(",");
                                Log.e("JOINT_WORK", json_oo.getString("Fld_Symbol"));
                                if (jarray != null && jarray.length() > 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        JSONObject jjss = jarray.getJSONObject(m);
                                        Log.v("InsideLoop", jjss.getString(txtArray[1]));
                                        a_listt.add(new Common_Model(jjss.getString(txtArray[1]), jjss.getString(txtArray[0]), false));
                                    }
                                }

                            }

                            Tp_dynamicArraylist.add(new Tp_Dynamic_Modal(json_oo.getString("Fld_ID"), json_oo.getString("Fld_Name"), "", json_oo.getString("Fld_Type"), json_oo.getString("Fld_Src_Name"), json_oo.getString("Fld_Src_Field"), json_oo.getInt("Fld_Length"), json_oo.getString("Fld_Symbol"), json_oo.getString("Fld_Mandatory"), json_oo.getString("Active_flag"), json_oo.getString("Control_id"), json_oo.getString("Target_Form"), json_oo.getString("Filter_Text"), json_oo.getString("Filter_Value"), json_oo.getString("Field_Col"), a_listt));
                        }

                        dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
                        dynamicrecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        //new Tp_Mydayplan.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), 0).notifyDataSetChanged();
                        dynamicrecyclerview.setItemViewCacheSize(jsnArValue.length());


                        if (String.valueOf(jsoncc.getJSONObject(0).get("submit_status")).equals("3")) {
                            submitbutton.setVisibility(View.GONE);
                        }
                    }
                    common_class.ProgressdialogShow(2, "Tour plan");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Tour Plan");
            }
        });
    }

    public class DynamicViewAdapter extends RecyclerView.Adapter<DynamicViewAdapter.MyViewHolder> {

        private int rowLayout;
        private Context context;
        AdapterOnClick mAdapterOnClick;
        private int Categorycolor;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tpcaptions, Textspinnerview;
            EditText edittextid;
            RadioGroup radiogroup;
            LinearLayout worktypelayout;

            public MyViewHolder(View view) {
                super(view);
                tpcaptions = view.findViewById(R.id.tpcaptions);
                Textspinnerview = view.findViewById(R.id.Textspinnerview);
                edittextid = view.findViewById(R.id.edittextid);
                worktypelayout = view.findViewById(R.id.worktypelayout);
                radiogroup = view.findViewById(R.id.radiogroup);
            }
        }

        public DynamicViewAdapter(ArrayList<Tp_Dynamic_Modal> array, int rowLayout, Context context, int Categorycolor) {
            dynamicarray = array;
            this.rowLayout = rowLayout;
            this.context = context;
            this.Categorycolor = Categorycolor;
        }

        @Override
        public DynamicViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new DynamicViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DynamicViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (!dynamicarray.get(position).getFld_Symbol().equals("JW")) {
                holder.tpcaptions.setVisibility(View.VISIBLE);
                holder.tpcaptions.setText(dynamicarray.get(position).getFld_Name());
                String titlecaptions = dynamicarray.get(position).getFld_Name();
                String SEttextvalues = dynamicarray.get(position).getFilter_Value();
                if (dynamicarray.get(position).getControl_id().equals("1") || dynamicarray.get(position).getControl_id().equals("2") || dynamicarray.get(position).getControl_id().equals("3") || dynamicarray.get(position).getControl_id().equals("18") || dynamicarray.get(position).getControl_id().equals("24") || dynamicarray.get(position).getControl_id().equals("24")) {
                    holder.edittextid.setHint("" + titlecaptions);
                    holder.edittextid.setVisibility(View.VISIBLE);
                    holder.edittextid.setText(SEttextvalues);
                    if (dynamicarray.get(position).getControl_id().equals("1")) {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (dynamicarray.get(position).getControl_id().equals("2")) {
                        holder.edittextid.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    } else if (dynamicarray.get(position).getControl_id().equals("3")) {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (dynamicarray.get(position).getControl_id().equals("18")) {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_PHONE);
                    } else if (dynamicarray.get(position).getControl_id().equals("24")) {
                        holder.edittextid.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    } else {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                    // Log.e("THIRUMALAIVASAN", String.valueOf(dynamicarray.get(position).getFld_Length()));
                    int maxLength = dynamicarray.get(position).getFld_Length();
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edittextid.setFilters(FilterArray);
                    holder.edittextid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start,
                                                  int before, int count) {
                            if (!charSequence.toString().equals("")) {
                                dynamicarray.get(position).setFilter_Value(charSequence.toString());
                            } else {
                                dynamicarray.get(position).setFilter_Value("");
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!s.toString().equals("")) {
                                dynamicarray.get(position).setFilter_Value(s.toString());
                            } else {
                                dynamicarray.get(position).setFilter_Value("");
                            }
                        }
                    });
                } else if (dynamicarray.get(position).getControl_id().equals("7") || dynamicarray.get(position).getControl_id().equals("8") || dynamicarray.get(position).getControl_id().equals("11")) {
                    holder.Textspinnerview.setHint("Select The " + titlecaptions);
                    holder.Textspinnerview.setText(SEttextvalues);
                    holder.Textspinnerview.setVisibility(View.VISIBLE);
                    holder.edittextid.setVisibility(View.GONE);
                } else if (dynamicarray.get(position).getControl_id().equals("10")) {
                    Log.e("ROute_Size", String.valueOf(dynamicarray.get(position).getA_list().size()));
                    holder.radiogroup.setVisibility(View.VISIBLE);
                    holder.edittextid.setVisibility(View.GONE);
                    for (int ii = 0; ii < dynamicarray.get(position).getA_list().size(); ii++) {
                        RadioButton rbn = new RadioButton(getApplicationContext());
                        rbn.setId(ii);
                        rbn.setText("" + dynamicarray.get(position).getA_list().get(ii).getName());
                        if (dynamicarray.get(position).getA_list().get(ii).isSelected() || (dynamicarray.get(position).getFilter_Text() != null && dynamicarray.get(position).getFilter_Text() != "" && dynamicarray.get(position).getFilter_Text().equals(dynamicarray.get(position).getA_list().get(ii).getId()))) {
                            rbn.setChecked(true);
                        }
                        holder.radiogroup.addView(rbn);
                    }
                    holder.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // This will get the radiobutton that has changed in its check state
                            RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                            // This puts the value (true/false) into the variable
                            boolean isChecked = checkedRadioButton.isChecked();
                            // If the radiobutton that has changed in check state is now checked...
                            if (isChecked) {
                                dynamicarray.get(position).setFilter_Value(dynamicarray.get(position).getA_list().get(checkedId).getName());
                                dynamicarray.get(position).setFilter_Text(dynamicarray.get(position).getA_list().get(checkedId).getId());
                                dynamicarray.get(position).getA_list().get(checkedId).setSelected(true);

                            }
                        }
                    });

                }
                holder.Textspinnerview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.e("GOOGLE", dynamicarray.get(position).getA_list().toString());
                        if (dynamicarray.get(position).getControl_id().equals("11")) {
                            timePicker(position, dynamicarray.get(position).getA_list());
                        } else if (dynamicarray.get(position).getControl_id().equals("8")) {
                            datePicker(position, dynamicarray.get(position).getA_list());
                        } else
                            openspinnerbox(position, dynamicarray.get(position).getA_list());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return dynamicarray.size();
        }
        public void timePicker(int position, ArrayList<Common_Model> ArrayList) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(Tp_Month_Select.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                    dynamicarray.get(position).setFilter_Value(selectedHour + ":" + selectedMinute);
                    dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
                    dynamicrecyclerview.setAdapter(dynamicadapter);
                    dynamicadapter.notifyDataSetChanged();
                    dynamicrecyclerview.setItemViewCacheSize(dynamicarray.size());
                }
            }, hour, minute, true);//Yes 24 hour time
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        }
        public void datePicker(int position, ArrayList<Common_Model> ArrayList) {
            Calendar newCalendar = Calendar.getInstance();
            DatePickerDialog = new DatePickerDialog(Tp_Month_Select.this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    int mnth = monthOfYear + 1;
                    dynamicarray.get(position).setFilter_Value(dayOfMonth + "-" + mnth + "-" + year);
                    dynamicadapter.notifyDataSetChanged();

                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            DatePickerDialog.show();
        }

        public void openspinnerbox(int position, ArrayList<Common_Model> ArrayList) {
            customDialog = new CustomListViewDialog(Tp_Month_Select.this, ArrayList, position);
            Window windowww = customDialog.getWindow();
            windowww.setGravity(Gravity.CENTER);
            windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            customDialog.show();
        }
    }

    public void OrderType() {
        travelTypeList = new ArrayList<>();
        travelTypeList.add("HQ");
        travelTypeList.add("EXQ");
        travelTypeList.add("Out Station");
        for (int i = 0; i < travelTypeList.size(); i++) {
            String id = String.valueOf(travelTypeList.get(i));
            String name = travelTypeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(Tp_Month_Select.this, listOrderType, 101);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }

    public boolean vali() {
        Log.e(TAG, mWorkType);
        if (mWorkType.equalsIgnoreCase("")) {
            Toast.makeText(this, "select the Work Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < dynamicarray.size(); i++) {
            if (dynamicarray.get(i).getFilter_Value() != null && dynamicarray.get(i).getFilter_Value().equals("") && dynamicarray.get(i).getFld_Mandatory().equals("1")) {
                if (dynamicarray.get(i).getFld_Symbol().equals("JW")) {
                    if (Jointworklistview.size() == 0) {
                        Toast.makeText(this, "Required Field " + dynamicarray.get(i).getFld_Name(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "Required Field " + dynamicarray.get(i).getFld_Name(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        if (ExpNeed == true) {
            String sMsg = "";
            if (TextMode.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Select the Mode of travel";
            }
            if (dailyAllowance.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Select the DA Type";
            }
            if (toId.equalsIgnoreCase("-1") && txtOthPlc.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Enter the To Other Place";
            }
            if (!sMsg.equalsIgnoreCase("")) {
                Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public void loadWorkTypes() {
        db = new DatabaseHandler(this);
        try {
            JSONArray HAPLoca = db.getMasterData("HAPWorkTypes");
            if (HAPLoca != null) {
                for (int li = 0; li < HAPLoca.length(); li++) {
                    JSONObject jItem = HAPLoca.getJSONObject(li);
                    String id = String.valueOf(jItem.optInt("id"));
                    String name = jItem.optString("name");
                    String flag = jItem.optString("FWFlg");
                    String ETabs = jItem.optString("ETabs");
                    String PlInv = jItem.optString("Place_Involved");
                    boolean tExpNeed = (PlInv.equalsIgnoreCase("Y") ? true : false);
                    Common_Model item = new Common_Model(id, name, flag, ETabs, tExpNeed);
                    worktypelist.add(item);

                    binding.workType.setPrompt(name);

                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    list.add(name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.workType.setAdapter(adapter);

                    mWorkType = name;
                    worktype_id = id;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SendtpApproval(String Name, int flag) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "dcr/save");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("Start_date", common_class.getintentValues("TourDate"));
        QueryString.put("Mr_Sfcode", Shared_Common_Pref.Tp_SFCode);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            sp.put("Sf_Code", Shared_Common_Pref.Tp_SFCode);
            if (flag == 2) {
                sp.put("reason", common_class.addquote(reason.getText().toString()));
            }
            jsonObject.put(Name, sp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        Log.e("Log_TpQuerySTring", QueryString.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                try {
                    common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(common_class.getintentValues("TourMonth")));
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "TP Approved  Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "TP Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void GetEmpList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> Callto = apiInterface.getDataArrayList("get/Emp_IdName",
                Shared_Common_Pref.Div_Code,
                Shared_Common_Pref.Sf_Code, empidedittext.getText().toString(), "", "DateTime", null);
        Callto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                if (res.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Emp Code  Not Found!", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.e("EMP_ID_Details", String.valueOf(Jointworklistview.size()));
                JsonObject EmpDet = res.get(0).getAsJsonObject();
                Log.e("EMP_ID_Details", String.valueOf(Jointworklistview.indexOf(new Common_Model(EmpDet.get("Sf_Name").getAsString() + "-" + EmpDet.get("sf_Designation_Short_Name").getAsString(), EmpDet.get("Sf_Code").getAsString(), false))));
                Common_Model Model_Pojo = new Common_Model(EmpDet.get("Sf_Name").getAsString() + "-" + EmpDet.get("sf_Designation_Short_Name").getAsString(), EmpDet.get("Sf_Code").getAsString(), false);

                boolean flag = CheckContains(Jointworklistview, EmpDet.get("Sf_Code").getAsString());
                if (flag) {
                    Toast.makeText(getApplicationContext(), "Already Added SF Name!", Toast.LENGTH_LONG).show();
                } else {
                    Jointworklistview.add(Model_Pojo);
                }
                text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                adapter2 = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int position, boolean flag) {
                        Jointworklistview.remove(position);
                        text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                        adapter.notifyDataSetChanged();
                    }
                });
                jointwork_recycler.setAdapter(adapter2);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Error:", "Some Error" + t.getMessage());
            }
        });
    }

    private boolean CheckContains(List<Common_Model> jointworklistview, String Sf_Code) {
        boolean flag = false;
        for (int i = 0; jointworklistview.size() > i; i++) {
            if (jointworklistview.get(i).getId().equals(Sf_Code)) {
                flag = true;
            }

        }
        return flag;
    }

    public void dynamicMode() {
        String Sf_Code;
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            Sf_Code = Shared_Common_Pref.Sf_Code;
        } else {
            Sf_Code = Shared_Common_Pref.Tp_SFCode;
        }
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Sf_Code);
        QueryString.put("rSF", Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        String commonLeaveType = "{\"tableName\":\"getmodeoftravel\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                userType = new TypeToken<ArrayList<ModeOfTravel>>() {
                }.getType();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    Model_Pojo = new Common_Model(id, name, modeId, driverMode);
                    modelTravelType.add(Model_Pojo);
                }
                customDialog = new CustomListViewDialog(Tp_Month_Select.this, modelTravelType, 100);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    public void GetTp_Worktype_Fields(String wflag) {
        common_class.ProgressdialogShow(1, "Tour plan PJP");
        String Sf_Code;
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            Sf_Code = Shared_Common_Pref.Sf_Code;
        } else {
            Sf_Code = Shared_Common_Pref.Tp_SFCode;
        }
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/worktypefields");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Sf_Code);
        QueryString.put("rSF", Sf_Code);
        QueryString.put("Worktype_Code", wflag);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Log.e("QUERY_STRING", QueryString.toString());
        Call<Object> call = service.GettpWorktypeFields(QueryString);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.v("print_upload_file_true", "ggg" + response);
                        String jsonData = null;
                        Tp_dynamicArraylist.clear();
                        Log.v("response_data", new Gson().toJson(response.body()));
                        JSONArray jsnArValue = new JSONArray(new Gson().toJson(response.body()));
                        Log.v("AfterTpresponse", jsnArValue.toString());
                        for (int i = 0; i < jsnArValue.length(); i++) {
                            JSONObject json_oo = jsnArValue.getJSONObject(i);
                            Log.e("Json_Filed", String.valueOf(json_oo.getJSONArray("inputs")));
                            ArrayList<Common_Model> a_listt = new ArrayList<>();
                            ArrayList<Common_Model> a_list = new ArrayList<>();
                            if (json_oo.getJSONArray("inputs") != null) {
                                JSONArray jarray = json_oo.getJSONArray("inputs");
                                a_listt.clear();
                                String[] txtArray = json_oo.getString("Fld_Src_Field").split(",");
                                if (json_oo.getString("Fld_Symbol").equals("JW")) {
                                    jointwork_layout.setVisibility(View.VISIBLE);
                                    Log.e("JOINT_WORK", json_oo.getString("Fld_Symbol"));
                                }
                                // Toast.makeText(Tp_Month_Select.this, "Fld_Src_Field", Toast.LENGTH_SHORT).show();
                                if (jarray != null && jarray.length() > 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        JSONObject jjss = jarray.getJSONObject(m);
                                        Log.v("InsideLoop", jjss.getString(txtArray[1]));
                                        a_listt.add(new Common_Model(jjss.getString(txtArray[1]), jjss.getString(txtArray[0]), false));
                                    }
                                }
                            }
                            Log.e("THIRUMALAI", String.valueOf(a_listt.size()));
                            Tp_dynamicArraylist.add(new Tp_Dynamic_Modal(json_oo.getString("Fld_ID"), json_oo.getString("Fld_Name"), "", json_oo.getString("Fld_Type"), json_oo.getString("Fld_Src_Name"), json_oo.getString("Fld_Src_Field"), json_oo.getInt("Fld_Length"), json_oo.getString("Fld_Symbol"), json_oo.getString("Fld_Mandatory"), json_oo.getString("Active_flag"), json_oo.getString("Control_id"), json_oo.getString("Target_Form"), json_oo.getString("Filter_Text"), json_oo.getString("Filter_Value"), json_oo.getString("Field_Col"), a_listt));
                        }
                        dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
                        dynamicrecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        //new Tp_Mydayplan.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), 0).notifyDataSetChanged();
                        dynamicrecyclerview.setItemViewCacheSize(jsnArValue.length());
                        common_class.ProgressdialogShow(2, "Tour plan PJP");
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Tour plan PJP");
            }
        });
    }

    private void getDateList(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        mCalendar = (Calendar) calendar.clone();

        dateFormat = new SimpleDateFormat("MMM");
        mCurrentDateString = dateFormat.format(mCalendar.getTime());

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
//        for (int i = 0; i < trailingSpaces; i++) {
//            list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
//        }

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
            Log.i(TAG, "Number Of Events Per Month : " + member);
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

                // main
                String[] day_color = list.get(position).split("-");
                Log.e("THE_DAY_COLOR", String.valueOf(day_color[0]));
                theday = day_color[0];
                String themonth = day_color[2];
                String theyear = day_color[3];
                int month = SelectedMonth + 1;
                TourMonth = theyear + "-" + month + "-" + theday;

                String[] TP_Dt = TourMonth.split("-");
                tourdate.setText(TP_Dt[2] + "/" + TP_Dt[1] + "/" + TP_Dt[0]);
                Log.e("Grid_Selected_Date", theday + "-" + themonth + "-" + theyear + day_color[1]);
           //     common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));

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

        public class ViewHolder extends RecyclerView.ViewHolder
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
            } catch (Exception e) {
                Log.v("TP CALENDER:local", e.getMessage());

            }
        } catch (Exception e) {
            Log.v("TP CALENDER:", e.getMessage());
        }
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.CurrentMoth:
//                common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(CM));
//                break;
//            case R.id.NextMonth:
//                common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(NM));
//                break;
//        }
//    }

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