package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvalaibilityHours {
    @SerializedName("tknHrS")
    @Expose
    private String tknHrS;

    public String getTknHrS() {
        return tknHrS;
    }

    public void setTknHrS(String tknHrS) {
        this.tknHrS = tknHrS;
    }
}
