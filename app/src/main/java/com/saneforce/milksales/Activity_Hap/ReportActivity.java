package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.ViewReport;
import com.saneforce.milksales.Model_Class.ReportDataList;
import com.saneforce.milksales.Model_Class.ReportModel;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.ReportViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    TextView toolHeader, txtTotalValue, txtProductDate;
    ImageView imgBack;
    EditText edtFromDate,edtToDate;
    String fromDateString, dateTime, toDateString, SF_CODE;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ReportViewAdapter mReportViewAdapter;
    RecyclerView mReportList;
    ArrayList<Float> mArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
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
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
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
                startActivity(new Intent(getApplicationContext(), OrderDashBoard.class));

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
        @SuppressLint("WrongConstant")
        SharedPreferences sh
                = getSharedPreferences("MyPrefs",
                MODE_APPEND);
        SF_CODE = sh.getString("Sf_Code", "");

        Log.e("SF_CODE", SF_CODE);
        //  fromBtn = (Button) findViewById(R.id.from_picker);
        edtFromDate = findViewById(R.id.from_picker);
        edtToDate =  findViewById(R.id.to_picker);

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


                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
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

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.e("FROMDATA", "" + fromDateString);
        Log.e("TODATA", "" + toDateString);
        Call<ReportDataList> responseBodyCall = apiInterface.reportValues("27", fromDateString, toDateString);
        responseBodyCall.enqueue(new Callback<ReportDataList>() {
            @Override
            public void onResponse(Call<ReportDataList> call, Response<ReportDataList> response) {

                ReportDataList mReportActivities = response.body();

                List<ReportModel> mDReportModels = mReportActivities.getData();
                for (int i = 0; i < mDReportModels.size(); i++) {
                    Log.e("data", mDReportModels.get(i).getOrderDate());
                    mArrayList.add(Float.valueOf((mDReportModels.get(i).getOrderValue())));
                }
                Float intSum = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    intSum = Float.valueOf(mArrayList.stream()
                            .mapToLong(Float::longValue)
                            .sum());
                }

                txtTotalValue.setText("" + intSum);

                mReportViewAdapter = new ReportViewAdapter(ReportActivity.this, mDReportModels, new ViewReport() {
                    @Override
                    public void reportCliick(String productId, String orderDate) {

                        Intent intnet = new Intent(ReportActivity.this, ViewReportActivity.class);

                        intnet.putExtra("ProductID", productId);
                        intnet.putExtra("OrderDate", orderDate);
                        startActivity(intnet);

                        Log.e("ProdutId", productId);
                        Log.e("OrderDate", orderDate);
                    }
                });

                mReportList.setAdapter(mReportViewAdapter);
            }

            @Override
            public void onFailure(Call<ReportDataList> call, Throwable t) {

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

