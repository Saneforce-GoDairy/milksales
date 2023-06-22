package com.saneforce.milksales.Status_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Onduty_Status_Model {
    @SerializedName("SFNm")
    @Expose
    private String SFNm;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("WrkType")
    @Expose
    private Integer wrkType;
    @SerializedName("login_date")
    @Expose
    private String loginDate;
    @SerializedName("ondutytype")
    @Expose
    private String ondutytype;
    @SerializedName("Approveddate")
    @Expose
    private String approveddate;
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


    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public void setSFNm(String SFNm) {
        this.SFNm = SFNm;
    }

    public Integer getWrkType() {
        return wrkType;
    }

    public void setWrkType(Integer wrkType) {
        this.wrkType = wrkType;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public String getSFNm() {
        return SFNm;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getOndutytype() {
        return ondutytype;
    }


    public void setOndutytype(String ondutytype) {
        this.ondutytype = ondutytype;
    }

    public String getApproveddate() {
        return approveddate;
    }

    public void setApproveddate(String approveddate) {
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
