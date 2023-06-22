package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayEntryModel {
    @SerializedName("SFNm")
    @Expose
    private String SFNm;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("login_date")
    @Expose
    private HolidayLoginDate loginDate;
    @SerializedName("ondutytype")
    @Expose
    private String ondutytype;
    @SerializedName("Approveddate")
    @Expose
    private Object approveddate;
    @SerializedName("ODLocName")
    @Expose
    private String oDLocName;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Start_Time")
    @Expose
    private String startTime;
    @SerializedName("End_Time")
    @Expose
    private String endTime;
    @SerializedName("checkin")
    @Expose
    private String checkin;
    @SerializedName("checkout")
    @Expose
    private String checkout;
    @SerializedName("Duty_Mode")
    @Expose
    private Integer dutyMode;
    @SerializedName("Submission_date")
    @Expose
    private String submissionDate;
    @SerializedName("Shift_name")
    @Expose
    private String shiftName;
    @SerializedName("OStatus")
    @Expose
    private String oStatus;
    @SerializedName("StusClr")
    @Expose
    private String stusClr;
    @SerializedName("Reject_reason")
    @Expose
    private Object rejectReason;

    @SerializedName("LastUpdt_Date")
    @Expose
    private String lastUpdtDate;
    @SerializedName("Created_Date")
    @Expose
    private String createdDate;

    public String getLastUpdtDate() {
        return lastUpdtDate;
    }

    public void setLastUpdtDate(String lastUpdtDate) {
        this.lastUpdtDate = lastUpdtDate;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public HolidayLoginDate getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(HolidayLoginDate loginDate) {
        this.loginDate = loginDate;
    }

    public String getOndutytype() {
        return ondutytype;
    }

    public void setOndutytype(String ondutytype) {
        this.ondutytype = ondutytype;
    }

    public Object getApproveddate() {
        return approveddate;
    }

    public void setApproveddate(Object approveddate) {
        this.approveddate = approveddate;
    }

    public String getODLocName() {
        return oDLocName;
    }

    public void setODLocName(String oDLocName) {
        this.oDLocName = oDLocName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getSFNm() {
        return SFNm;
    }

    public void setSFNm(String SFNm) {
        this.SFNm = SFNm;
    }

    public Integer getDutyMode() {
        return dutyMode;
    }

    public void setDutyMode(Integer dutyMode) {
        this.dutyMode = dutyMode;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getOStatus() {
        return oStatus;
    }

    public void setOStatus(String oStatus) {
        this.oStatus = oStatus;
    }

    public String getStusClr() {
        return stusClr;
    }

    public void setStusClr(String stusClr) {
        this.stusClr = stusClr;
    }

    public Object getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(Object rejectReason) {
        this.rejectReason = rejectReason;
    }
}
