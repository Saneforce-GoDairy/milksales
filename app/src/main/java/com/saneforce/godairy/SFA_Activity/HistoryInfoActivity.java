package com.saneforce.godairy.SFA_Activity;


import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.HistorySalesInfoAdapter;
import com.saneforce.godairy.SFA_Model_Class.MonthFormat;
import com.saneforce.godairy.SFA_Model_Class.MonthYearPickerDialog;
import com.saneforce.godairy.SFA_Model_Class.MonthYearPickerDialogFragment;
import com.saneforce.godairy.SFA_Model_Class.OutletReport_View_Modal;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HistoryInfoActivity extends AppCompatActivity implements View.OnClickListener,
        UpdateResponseUI {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static TextView tvStartDate, tvEndDate;

    Common_Class common_class;
    ImageView ivToolbarHome, ivMnthSelect;
    Shared_Common_Pref shared_common_pref;
    DatePickerDialog fromDatePickerDialog;
    String date = "";

    String response = "";
    String TAG = "HistoryInfoActivity";
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();

    RecyclerView rv;
    TextView tvSales, tvOutstanding, tvOutletName;
    LinearLayout llHistoryParent;

    public static String stDate = "", endDate = "";
    Button btnToday, btnYesterday, btnCurrentMnth, btnSelectDate;
    private int currentYear;
    private int yearSelected;
    private int monthSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_info);
        init();

        tvOutletName.setText(shared_common_pref.getvalue(Constants.Distributor_name));
        tvStartDate.setText(Common_Class.GetDatewothouttime());
        tvEndDate.setText(Common_Class.GetDatewothouttime());
        stDate = tvStartDate.getText().toString();
        endDate = tvEndDate.getText().toString();

        common_class.gotoHomeScreen(this, ivToolbarHome);

        common_class.getDb_310Data(Constants.HistoryData, this);

        btnToday.setTypeface(null, Typeface.BOLD);
        btnToday.setTextColor(getResources().getColor(R.color.white));

        btnYesterday.setTypeface(null, Typeface.NORMAL);
        btnYesterday.setTextColor(getResources().getColor(R.color.a50white));
        btnCurrentMnth.setTypeface(null, Typeface.NORMAL);
        btnCurrentMnth.setTextColor(getResources().getColor(R.color.a50white));
        common_class.getDb_310Data(Constants.OUTSTANDING, this);


    }


    void init() {
        tvOutletName = findViewById(R.id.retailername);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        ivToolbarHome = (ImageView) findViewById(R.id.toolbar_home);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        rv = findViewById(R.id.rvHistory);
        tvSales = findViewById(R.id.tvSales);
        llHistoryParent = findViewById(R.id.llHistoryParent);
        btnToday = findViewById(R.id.btnToday);
        btnYesterday = findViewById(R.id.btnYesterday);
        btnCurrentMnth = findViewById(R.id.btnCurrentMnth);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        ivMnthSelect = findViewById(R.id.ivMnthSelecter);
        tvOutstanding = findViewById(R.id.tvOutstanding);

        btnToday.setOnClickListener(this);
        btnYesterday.setOnClickListener(this);
        btnCurrentMnth.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvSales.setOnClickListener(this);
        ivMnthSelect.setOnClickListener(this);


        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        yearSelected = currentYear;
        monthSelected = calendar.get(Calendar.MONTH);


    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrderInfoFragment("Orders"), "Order");
        adapter.addFragment(new InvoiceInfoFragment("Invoice"), "Invoice");
        adapter.addFragment(new OrderInvoiceInfoFragment("Order vs Invoice"), "Order vs Invoice");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMnthSelecter:
//                displayMonthYearPickerDialogFragment(
//                        false,
//                        false
//                );
                btnToday.setTypeface(null, Typeface.NORMAL);
                btnToday.setTextColor(getResources().getColor(R.color.a50white));

                btnYesterday.setTypeface(null, Typeface.NORMAL);
                btnYesterday.setTextColor(getResources().getColor(R.color.a50white));
                btnCurrentMnth.setTypeface(null, Typeface.BOLD);
                btnCurrentMnth.setTextColor(getResources().getColor(R.color.white));

                findViewById(R.id.llCustomDate).setVisibility(View.VISIBLE);
                tvStartDate.setText(stDate);
                tvEndDate.setText(endDate);
                btnSelectDate.setText(stDate + " TO " + endDate);
                break;

            case R.id.tvSales:
                llHistoryParent.setVisibility(View.GONE);
                findViewById(R.id.llCustomDate).setVisibility(View.GONE);
                findViewById(R.id.llSalesParent).setVisibility(View.VISIBLE);
                tvSales.setVisibility(View.GONE);
                stDate = Common_Class.GetDatewothouttime();
                endDate = Common_Class.GetDatewothouttime();
                common_class.getDb_310Data(Constants.HistoryData, this);
                break;
            case R.id.tvStartDate:
                selectDate(1);


                break;
            case R.id.tvEndDate:
                selectDate(2);

                break;
            case R.id.btnToday:
                findViewById(R.id.llCustomDate).setVisibility(View.GONE);

                btnToday.setTypeface(null, Typeface.BOLD);
                btnToday.setTextColor(getResources().getColor(R.color.white));

                btnYesterday.setTypeface(null, Typeface.NORMAL);
                btnYesterday.setTextColor(getResources().getColor(R.color.a50white));
                btnCurrentMnth.setTypeface(null, Typeface.NORMAL);
                btnCurrentMnth.setTextColor(getResources().getColor(R.color.a50white));

                stDate = Common_Class.GetDatewothouttime();
                endDate = Common_Class.GetDatewothouttime();
                common_class.getDb_310Data(Constants.HistoryData, this);
                btnSelectDate.setText("Today");
                break;
            case R.id.btnYesterday:

                findViewById(R.id.llCustomDate).setVisibility(View.GONE);

                btnToday.setTypeface(null, Typeface.NORMAL);
                btnToday.setTextColor(getResources().getColor(R.color.a50white));
                btnYesterday.setTypeface(null, Typeface.BOLD);
                btnYesterday.setTextColor(getResources().getColor(R.color.white));
                btnCurrentMnth.setTypeface(null, Typeface.NORMAL);
                btnCurrentMnth.setTextColor(getResources().getColor(R.color.a50white));

                stDate = getYesterdayDateString();
                endDate = getYesterdayDateString();
                common_class.getDb_310Data(Constants.HistoryData, this);
                btnSelectDate.setText("Yesterday");
                break;
            case R.id.btnCurrentMnth:
                btnToday.setTypeface(null, Typeface.NORMAL);
                btnToday.setTextColor(getResources().getColor(R.color.a50white));

                btnYesterday.setTypeface(null, Typeface.NORMAL);
                btnYesterday.setTextColor(getResources().getColor(R.color.a50white));
                btnCurrentMnth.setTypeface(null, Typeface.BOLD);
                btnCurrentMnth.setTextColor(getResources().getColor(R.color.white));
                findViewById(R.id.llCustomDate).setVisibility(View.VISIBLE);

                tvStartDate.setText(stDate);
                tvEndDate.setText(endDate);

//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.DAY_OF_MONTH, 1);
//                stDate = dateFormat.format(cal.getTime());
//                endDate = Common_Class.GetDatewothouttime();
//                common_class.getDb_310Data(Constants.HistoryData, this);
                // btnSelectDate.setText("Current Month");

                break;

        }
    }

    private MonthYearPickerDialogFragment createDialog(boolean customTitle) {
        return MonthYearPickerDialogFragment
                .getInstance(monthSelected,
                        yearSelected,
                        "",
                        MonthFormat.SHORT);
    }

    private MonthYearPickerDialogFragment createDialogWithRanges(boolean customTitle) {
        final int minYear = 2010;
        final int maxYear = currentYear;
        final int maxMoth = 11;
        final int minMoth = 0;
        final int minDay = 1;
        final int maxDay = 31;
        long minDate;
        long maxDate;

        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(minYear, minMoth, minDay);
        minDate = calendar.getTimeInMillis();

        calendar.clear();
        calendar.set(maxYear, maxMoth, maxDay);
        maxDate = calendar.getTimeInMillis();

        return MonthYearPickerDialogFragment
                .getInstance(monthSelected,
                        yearSelected,
                        minDate,
                        maxDate,
                        "",
                        MonthFormat.SHORT);
    }

    private void displayMonthYearPickerDialogFragment(boolean withRanges,
                                                      boolean customTitle) {
        MonthYearPickerDialogFragment dialogFragment = withRanges ?
                createDialogWithRanges(customTitle) :
                createDialog(customTitle);

        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                monthSelected = monthOfYear;
                yearSelected = year;
                //  updateViews();
                stDate = yearSelected + "-" + (monthSelected + 1) + "-" + "01";
                endDate = yearSelected + "-" + (monthSelected + 1) + "-" + "28";
                common_class.getDb_310Data(Constants.HistoryData, HistoryInfoActivity.this);
                String month = new DateFormatSymbols().getMonths()[monthSelected];

                btnSelectDate.setText(String.format("%s / %s", month, yearSelected));
            }
        });

        dialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            JSONObject jsonObject = new JSONObject(apiDataResponse);

            switch (key) {
                case Constants.HistoryData:
                    if (jsonObject.getBoolean("success")) {
                        shared_common_pref.save(Constants.HistoryData, apiDataResponse);

                    } else {
                        shared_common_pref.clear_pref(Constants.HistoryData);

                    }

                    if (llHistoryParent.getVisibility() == View.VISIBLE) {
                        tabLayout.setupWithViewPager(viewPager);
                        setupViewPager(viewPager);
                    } else {
                        setAdapter();
                    }
                    break;
                case Constants.OUTSTANDING:
                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            tvOutstanding.setText(CurrencySymbol + " " + new DecimalFormat("##0.00").format(
                                    jsonArray.getJSONObject(i).getDouble("Outstanding")));

                        }

                    } else {

                        tvOutstanding.setText(CurrencySymbol + " " + 0.00);
                    }
                    break;
            }


        } catch (Exception e) {

        }

    }

    void setAdapter() {
        try {
            FilterOrderList.clear();
            String strHistory = shared_common_pref.getvalue(Constants.HistoryData);


            JSONObject invoiceObj = new JSONObject(strHistory);


            //  if (invoiceObj.getBoolean("success")) {
            JSONArray jsonArray = invoiceObj.getJSONArray("Invoice");

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int pm = 0; pm < jsonArray.length(); pm++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(pm);


                    List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();


                    if (!jsonObject1.getString("Status").equals("No Order")) {
                        JSONArray detailsArray = jsonObject1.getJSONArray("Details");


                        for (int da = 0; da < detailsArray.length(); da++) {

                            JSONObject daObj = detailsArray.getJSONObject(da);


                            product_details_modalArrayList.add(new Product_Details_Modal(daObj.getString("Product_Code"),
                                    daObj.getString("Product_Name"), "", Integer.parseInt(daObj.getString("Quantity")), ""));

                        }
                    }
                    FilterOrderList.add(new OutletReport_View_Modal(0, jsonObject1.getString("InvoiceID"), "",
                            jsonObject1.getString("ListedDr_Name"),
                            jsonObject1.getString("Date"), (jsonObject1.getDouble("Order_Value")),
                            jsonObject1.getString("Status"), product_details_modalArrayList));


                }


            }


            HistorySalesInfoAdapter historyInfoAdapter = new HistorySalesInfoAdapter(this, FilterOrderList, R.layout.history_sales_adapter_layout,
                    2, new AdapterOnClick() {
                @Override
                public void onIntentClick(int pos) {


                }
            });

            rv.setAdapter(historyInfoAdapter);
        } catch (Exception e) {

        }
    }


    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String plantime = dateFormat.format(cal.getTime());
        return plantime;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    void selectDate(int val) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(HistoryInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(),HistoryInfoActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();
                        btnSelectDate.setText(stDate + " TO " + endDate);
                        common_class.getDb_310Data(Constants.HistoryData, HistoryInfoActivity.this);
                    } else
                        common_class.showMsg(HistoryInfoActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date,HistoryInfoActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        btnSelectDate.setText(stDate + " TO " + endDate);
                        common_class.getDb_310Data(Constants.HistoryData, HistoryInfoActivity.this);

                    } else
                        common_class.showMsg(HistoryInfoActivity.this, "Please select valid date");

                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (llHistoryParent.getVisibility() == View.VISIBLE) {
                finish();
            } else {
                llHistoryParent.setVisibility(View.VISIBLE);
                findViewById(R.id.llCustomDate).setVisibility(View.VISIBLE);
                findViewById(R.id.llSalesParent).setVisibility(View.GONE);
                tvSales.setVisibility(View.VISIBLE);

            }


            return true;
        }
        return false;
    }
}