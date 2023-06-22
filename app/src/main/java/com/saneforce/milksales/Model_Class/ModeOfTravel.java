package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModeOfTravel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("StEndNeed")
    @Expose
    private Integer stEndNeed;

    @SerializedName("Attachment")
    @Expose
    private String Attachemnt;


    @SerializedName("Alw_Eligibilty")
    @Expose
    private String Alw_Eligibilty;

    @SerializedName("Max_Allowance")
    @Expose
    private int Max_Allowance;


    @SerializedName("DriverNeed")
    @Expose
    private Integer driverNeed;

    @SerializedName("Eligible")
    @Expose
    private Integer Eligible;

    public String getAttachemnt() {
        return Attachemnt;
    }

    public int getMax_Allowance() {
        return Max_Allowance;
    }
    public void setAttachemnt(String attachemnt) {
        Attachemnt = attachemnt;
    }

    public void setMax_Allowance(int max_Allowance) {
        Max_Allowance = max_Allowance;
    }

    public void setEligible(Integer eligible) {
        Eligible = eligible;
    }
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

    public Integer getStEndNeed() {
        return stEndNeed;
    }

    public void setStEndNeed(Integer stEndNeed) {
        this.stEndNeed = stEndNeed;
    }

    public Integer getDriverNeed() {
        return driverNeed;
    }

    public Integer getEligible() {
        return Eligible;
    }
    public void setDriverNeed(Integer driverNeed) {
        this.driverNeed = driverNeed;
    }

    public String getAlw_Eligibilty() {
        return Alw_Eligibilty;
    }

    public void setAlw_Eligibilty(String alw_Eligibilty) {
        Alw_Eligibilty = alw_Eligibilty;
    }
}
