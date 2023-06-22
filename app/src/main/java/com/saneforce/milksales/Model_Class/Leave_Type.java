package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leave_Type {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Leave_SName")
    @Expose
    private String leaveSName;
    @SerializedName("COffType")
    @Expose
    private Integer COffType;
    @SerializedName("MaxDays")
    @Expose
    private Integer MaxDays;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCOffType() {
        return COffType;
    }
    public Integer getMaxDays() {
        return MaxDays;
    }


    public String getLeaveSName() {
        return leaveSName;
    }

    public void setLeaveSName(String leaveSName) {
        this.leaveSName = leaveSName;
    }
}
