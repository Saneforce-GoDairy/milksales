package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissedPunch {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name1")
    @Expose
    private String name1;
    @SerializedName("End_Time")
    @Expose
    private String endTime;
    @SerializedName("Checkin_Time")
    @Expose
    private String checkinTime;
    @SerializedName("COutTime")
    @Expose
    private String cOutTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCOutTime() {
        return cOutTime;
    }

    public void setCOutTime(String cOutTime) {
        this.cOutTime = cOutTime;
    }

}
