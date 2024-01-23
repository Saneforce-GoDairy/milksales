package com.saneforce.godairy.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Activity_Hap.Day_Report_Activity;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.UpdateResponseUI;

public class DayReportAdapter extends AppCompatActivity implements UpdateResponseUI {


    public DayReportAdapter(Day_Report_Activity dayReportActivity, AdapterOnClick adapterOnClick) {
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
