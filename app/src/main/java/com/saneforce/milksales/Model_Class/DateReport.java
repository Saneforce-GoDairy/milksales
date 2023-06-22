package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DateReport {

    @SerializedName("Data")
    @Expose
    private List<DateResult> data = null;

    public List<DateResult> getData() {
        return data;
    }

    public void setData(List<DateResult> data) {
        this.data = data;
    }
}
