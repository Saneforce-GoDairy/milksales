package com.saneforce.milksales.Status_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissedPunch_Status_Model {
    @SerializedName("SFNm")
    @Expose
    private String SFNm;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Shift_Name")
    @Expose
    private String shiftName;
    @SerializedName("Rejectdate")
    @Expose
    private String rejectdate;
    @SerializedName("Missed_punch_date")
    @Expose
    private String missedPunchDate;
    @SerializedName("Missed_punch_Flag")
    @Expose
    private Integer missedPunchFlag;
    @SerializedName("Submission_date")
    @Expose
    private String submissionDate;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Checkin_Time")
    @Expose
    private String checkinTime;
    @SerializedName("Checkout_Tme")
    @Expose
    private String checkoutTme;
    @SerializedName("MPStatus")
    @Expose
    private String mPStatus;
    @SerializedName("StusClr")
    @Expose
    private String stusClr;
    @SerializedName("RejectReason")
    @Expose
    private String rejectReason;
    public void setSFNm(String SFNm) {
        this.SFNm = SFNm;
    }
    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Object getRejectdate() {
        return rejectdate;
    }

    public void setRejectdate(String rejectdate) {
        this.rejectdate = rejectdate;
    }

    public String getMissedPunchDate() {
        return missedPunchDate;
    }

    public void setMissedPunchDate(String missedPunchDate) {
        this.missedPunchDate = missedPunchDate;
    }
    public String getSFNm() {
        return SFNm;
    }
    public Integer getMissedPunchFlag() {
        return missedPunchFlag;
    }

    public void setMissedPunchFlag(Integer missedPunchFlag) {
        this.missedPunchFlag = missedPunchFlag;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTme() {
        return checkoutTme;
    }

    public void setCheckoutTme(String checkoutTme) {
        this.checkoutTme = checkoutTme;
    }

    public String getMPStatus() {
        return mPStatus;
    }

    public void setMPStatus(String mPStatus) {
        this.mPStatus = mPStatus;
    }

    public String getStusClr() {
        return stusClr;
    }

    public void setStusClr(String stusClr) {
        this.stusClr = stusClr;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
