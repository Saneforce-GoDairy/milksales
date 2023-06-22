package com.saneforce.milksales.SFA_Activity;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.Dashboard;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.ViewReport;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.Outlet_Report_View_Adapter;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class Outlet_Report_View extends AppCompatActivity {
    TextView toolHeader, txtTotalValue, txtProductDate, Outlet_Name;
    ImageView imgBack;
    EditText edtFromDate, edtToDate;
    String fromDateString, dateTime, toDateString, SF_CODE;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Outlet_Report_View_Adapter mReportViewAdapter;
    RecyclerView mReportList;
    ArrayList<Float> mArrayList;
    List<OutletReport_View_Modal> OutletReport_View_Modal;
    Type userType;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet__report__view);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        Outlet_Name = findViewById(R.id.Outlet_Name);
        Outlet_Name.setText("" + Shared_Common_Pref.OutletName);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        gson = new Gson();
    //    ((MyApplication) getApplication()).getNetComponent().inject(this);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);
        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));


            }
        });

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        mArrayList = new ArrayList<>();
        txtTotalValue = (TextView) findViewById(R.id.total_value);
        edtFromDate = findViewById(R.id.from_picker);
        edtToDate = findViewById(R.id.to_picker);
        txtTotalValue.setText("0");
        DateFormat df = new SimpleDateFormat("yyyy-MM-d");
        Calendar calobj = Calendar.getInstance();
        dateTime = df.format(calobj.getTime());
        System.out.println("Date_and_Time" + dateTime);
        edtFromDate.setText(dateTime);
        edtToDate.setText(dateTime);
        Log.e("DATE_FROM", dateTime);
        fromDateString = dateTime;
        toDateString = dateTime;

        ViewDateReport();

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Outlet_Report_View.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                edtFromDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                                Log.e("DATE_FROM", edtFromDate.getText().toString());

                                ViewDateReport();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        edtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Outlet_Report_View.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                toDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                edtToDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                Log.e("DATE_FROM", edtToDate.getText().toString());
                                ViewDateReport();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        mReportList = (RecyclerView) findViewById(R.id.report_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mReportList.setLayoutManager(layoutManager);
    }


    public void ViewDateReport() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code.replace(",", ""));
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("fromdate", fromDateString);
        QueryString.put("todate", toDateString);
        QueryString.put("Outlet_Code", Shared_Common_Pref.OutletCode);
        Log.e("Report_ValuesMap", QueryString.toString());
        Call<Object> call = service.GetRouteObject(QueryString, "{\"tableName\":\"GetOutletViewReport\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAster_Product_Details", response.body() + "");
                System.out.println("GetOutletView" + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                }.getType();
                OutletReport_View_Modal = gson.fromJson(new Gson().toJson(response.body()), userType);
                if (OutletReport_View_Modal.size() == 0) {
                    Toast.makeText(Outlet_Report_View.this, "Order Not Available!", Toast.LENGTH_SHORT).show();
                }
                Double c = 0.0;
                for (OutletReport_View_Modal ml : OutletReport_View_Modal) {
                    c += ml.getOrderValue();
                }

                txtTotalValue.setText("" + c);
                System.out.println("Product_Details_Size" + OutletReport_View_Modal.size());

                mReportViewAdapter = new Outlet_Report_View_Adapter(Outlet_Report_View.this, OutletReport_View_Modal, new ViewReport() {
                    @Override
                    public void reportCliick(String productId, String orderDate) {
                        Intent intnet = new Intent(Outlet_Report_View.this, Outet_Report_Details.class);
                        intnet.putExtra("Order_ID", productId);
                        intnet.putExtra("OrderDate", orderDate);

                        startActivity(intnet);
                    }
                });

                mReportList.setAdapter(mReportViewAdapter);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    onSuperBackPressed();
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }
}