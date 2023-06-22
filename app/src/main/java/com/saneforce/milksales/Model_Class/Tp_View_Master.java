package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tp_View_Master {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("SF_Code")
    @Expose
    private String sFCode;
    @SerializedName("worktype_code")
    @Expose
    private Integer worktypeCode;
    @SerializedName("worktype_name")
    @Expose
    private String worktypeName;
    @SerializedName("RouteCode")
    @Expose
    private String routeCode;
    @SerializedName("RouteName")
    @Expose
    private String routeName;
    @SerializedName("Worked_with_Code")
    @Expose
    private String workedWithCode;
    @SerializedName("Worked_with_Name")
    @Expose
    private String workedWithName;
    @SerializedName("Confirmed")
    @Expose
    private Integer confirmed;
    @SerializedName("HQ_Code")
    @Expose
    private String hQCode;
    @SerializedName("HQ_Name")
    @Expose
    private String hQName;
    @SerializedName("JointWork_Name")
    @Expose
    private String jointWorkName;
    @SerializedName("JointworkCode")
    @Expose
    private String jointworkCode;
    @SerializedName("Tmonth")
    @Expose
    private Integer tmonth;
    @SerializedName("Tyear")
    @Expose
    private Integer tyear;
    @SerializedName("submit_status")
    @Expose
    private String submitStatus;
    @SerializedName("dayofplan")
    @Expose
    private Integer dayofcout;
    @Expose
    private String todate;
    @SerializedName("MOT")
    @Expose
    private String mOT;
    @SerializedName("DA_Type")
    @Expose
    private String dAType;
    @SerializedName("Driver_Allow")
    @Expose
    private String driverAllow;

    public String getmOT() {
        return mOT;
    }

    public void setmOT(String mOT) {
        this.mOT = mOT;
    }

    public String getdAType() {
        return dAType;
    }

    public void setdAType(String dAType) {
        this.dAType = dAType;
    }

    public String getDriverAllow() {
        return driverAllow;
    }

    public void setDriverAllow(String driverAllow) {
        this.driverAllow = driverAllow;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    @SerializedName("From_Place")
    @Expose
    private String fromPlace;
    @SerializedName("To_Place")
    @Expose
    private String toPlace;
    public Integer getDayofcout() {
        return dayofcout;
    }
    public void setDayofcout(Integer date) {
        this.dayofcout = date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSFCode() {
        return sFCode;
    }

    public void setSFCode(String sFCode) {
        this.sFCode = sFCode;
    }

    public Integer getWorktypeCode() {
        return worktypeCode;
    }

    public void setWorktypeCode(Integer worktypeCode) {
        this.worktypeCode = worktypeCode;
    }

    public String getWorktypeName() {
        return worktypeName;
    }

    public void setWorktypeName(String worktypeName) {
        this.worktypeName = worktypeName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getWorkedWithCode() {
        return workedWithCode;
    }

    public void setWorkedWithCode(String workedWithCode) {
        this.workedWithCode = workedWithCode;
    }

    public String getWorkedWithName() {
        return workedWithName;
    }

    public void setWorkedWithName(String workedWithName) {
        this.workedWithName = workedWithName;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public String getHQCode() {
        return hQCode;
    }

    public void setHQCode(String hQCode) {
        this.hQCode = hQCode;
    }

    public String getHQName() {
        return hQName;
    }

    public void setHQName(String hQName) {
        this.hQName = hQName;
    }

    public String getJointWorkName() {
        return jointWorkName;
    }

    public void setJointWorkName(String jointWorkName) {
        this.jointWorkName = jointWorkName;
    }

    public String getJointworkCode() {
        return jointworkCode;
    }

    public void setJointworkCode(String jointworkCode) {
        this.jointworkCode = jointworkCode;
    }

    public Integer getTmonth() {
        return tmonth;
    }

    public void setTmonth(Integer tmonth) {
        this.tmonth = tmonth;
    }

    public Integer getTyear() {
        return tyear;
    }

    public void setTyear(Integer tyear) {
        this.tyear = tyear;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }



}
