package com.saneforce.godairy.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Model_Class.DayReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.adapters.DayReportAdapter;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.databinding.ActivityDayReportBinding;
import com.saneforce.godairy.databinding.ActivityTpMydayplanBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Day_Report_Activity extends AppCompatActivity implements UpdateResponseUI {
    private ActivityDayReportBinding binding;
    public  static  String mDate = "";
    private DatePickerDialog datePickerDialog;
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    private  TextView tvDate,tvSFADate,tvdaydate,txtDate,tvempname,tvsalerepname,thempid_desi,tvtime,
            tvvisitdis,tvvisitoutlet,tvorderdist,tvorderoutlet,
            tvordereddist,tvorderedoutlet,tvinvoicedist,tvinvoiceoutlet;

    Button button;
    RecyclerView day_report_recycler,  rvdayreportrecycler;
    DayReportAdapter dayReportAdapter;
    BottomSheetDialog sheetDialog;
    Gson gson;
    Common_Class common_class;
    DatabaseHandler db;
    Shared_Common_Pref shared_common_pref;
    List<DayReport> Dayreport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDayReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = new DatabaseHandler(this);
        gson = new Gson();
        shared_common_pref = new Shared_Common_Pref(Day_Report_Activity.this);
        common_class = new Common_Class(this);

        initViews();
        initAdapter();


        tvdaydate.setText(DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog=new BottomSheetDialog(Day_Report_Activity.this,R.style.AppBottomSheetDialogTheme) ;

                sheetDialog.setContentView(R.layout.day_bottom_sheet);

                sheetDialog.show();


            }
        });
    }

    public void initAdapter() {
      /*  dayReportAdapter = new DayReportAdapter(Day_Report_Activity.this, Dayreport, new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {


            }

        });
        day_report_recycler.setAdapter(dayReportAdapter);*/
    }


    private void initViews() {
        button=findViewById(R.id.button);
        tvSFADate = findViewById(R.id.tvSFADate);
        tvdaydate = findViewById(R.id.txtdayDate);
        tvempname =  findViewById(R.id.emp_name);
        tvsalerepname =  findViewById(R.id.work_type);
        thempid_desi =  findViewById(R.id.emp_id_deg);
        tvtime =  findViewById(R.id.submit_time);
        tvvisitdis =  findViewById(R.id.visited_distributor);
        tvvisitoutlet =  findViewById(R.id.visited_outlet);
        tvorderdist =  findViewById(R.id.order_distributor);
        tvvisitoutlet =  findViewById(R.id.order_outlet);
        tvordereddist =  findViewById(R.id.ordered_distributor);
        tvorderedoutlet =  findViewById(R.id.ordered_outlet);
        tvinvoicedist =  findViewById(R.id.invoiced_distributor);
        tvinvoiceoutlet =  findViewById(R.id.invoiced_outlet);
//        rvdayreportrecycler = findViewById(R.id.dayreportrecycler);

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {

    }

    @Override
    public void onErrorData(String msg) {
        UpdateResponseUI.super.onErrorData(msg);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}



