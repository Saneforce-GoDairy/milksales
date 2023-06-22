package com.saneforce.milksales.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Model_Class.POSDataList;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.Product_Details_Modal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSViewEntryActivity extends AppCompatActivity{

    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    public static TextView tvStartDate, tvEndDate;
    POSEntryViewAdapter mReportViewAdapter;
    RecyclerView posViewRecycler;
    public static String stDate = "", endDate = "",SF_code="";
    String fromDateString, dateTime, toDateString, SF_CODE;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ArrayList<Float> model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posview_entry);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        Log.v("sfcodeeee",SF_code);

        model = new ArrayList<>();

        tvStartDate = findViewById(R.id.tvPosViewStartDate);
        tvEndDate = findViewById(R.id.tvPosViewEndDate);
        posViewRecycler = findViewById(R.id.posViewRecyclerview);

        DateFormat df = new SimpleDateFormat("yyyy-MM-d");
        Calendar calobj = Calendar.getInstance();
        dateTime = df.format(calobj.getTime());
        System.out.println("Date_and_Time" + dateTime);

        tvStartDate.setText(dateTime);
        tvEndDate.setText(dateTime);

        fromDateString = dateTime;
        toDateString = dateTime;

        ViewDateReport();

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(POSViewEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tvStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                                Log.e("DATE_FROM", tvStartDate.getText().toString());

                                ViewDateReport();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(POSViewEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                toDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tvEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                Log.e("DATE_FROM", tvEndDate.getText().toString());
                                ViewDateReport();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        posViewRecycler.setLayoutManager(layoutManager);
    }

    public void ViewDateReport() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<POSDataList> call = apiInterface.getpos1(SF_code, fromDateString, toDateString);

        call.enqueue(new Callback<POSDataList>() {
            @Override
            public void onResponse(Call<POSDataList> call, Response<POSDataList> response) {


                POSDataList mReportActivities = response.body();
                List<Product_Details_Modal> mDReportModels = mReportActivities.getData();

                Log.e("MdReportModels", String.valueOf(mDReportModels.size()));

                for (int i = 0; i < mDReportModels.size(); i++) {
                    model.add(Float.valueOf(mDReportModels.get(i).getTotal()));
                }

                mReportViewAdapter = new POSEntryViewAdapter(POSViewEntryActivity.this,mDReportModels);
                posViewRecycler.setAdapter(mReportViewAdapter);
            }

            @Override
            public void onFailure(Call<POSDataList> call, Throwable t) {
                Log.e("MdReportModelsErr", t.toString());
            }
        });

    }

}

