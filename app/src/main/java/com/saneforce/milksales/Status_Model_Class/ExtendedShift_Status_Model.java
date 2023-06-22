package com.saneforce.milksales.Status_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendedShift_Status_Model {
    @SerializedName("SFNm")
    @Expose
    private String SFNm;
    @SerializedName("Shiftnames")
    @Expose
    private String shiftnames;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("WrkType")
    @Expose
    private Integer wrkType;
    @SerializedName("Approveddate")
    @Expose
    private String approveddate;
    @SerializedName("Total_Numbers")
    @Expose
    private String totalNumbers;
    @SerializedName("Submission_date")
    @Expose
    private String submissionDate;
    @SerializedName("STime")
    @Expose
    private String sTime;
    @SerializedName("ETime")
    @Expose
    private String eTime;
    @SerializedName("checkin")
    @Expose
    private String checkin;
    @SerializedName("NumberofH")
    @Expose
    private String numberofH;
    @SerializedName("checkout")
    @Expose
    private String checkout;
    @SerializedName("EStatus")
    @Expose
    private String eStatus;
    @SerializedName("StusClr")
    @Expose
    private String stusClr;
    @SerializedName("Reject_reason")
    @Expose
    private Object rejectReason;

    public void setSFNm(String SFNm) {
        this.SFNm = SFNm;
    }

    public String getShiftnames() {
        return shiftnames;
    }

    public void setShiftnames(String shiftnames) {
        this.shiftnames = shiftnames;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public Integer getWrkType() {
        return wrkType;
    }

    public void setWrkType(Integer wrkType) {
        this.wrkType = wrkType;
    }

    public String getApproveddate() {
        return approveddate;
    }

    public void setApproveddate(String approveddate) {
        this.approveddate = approveddate;
    }

    public String getTotalNumbers() {
        return totalNumbers;
    }

    public void setTotalNumbers(String totalNumbers) {
        this.totalNumbers = totalNumbers;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getSTime() {
        return sTime;
    }

    public void setSTime(String sTime) {
        this.sTime = sTime;
    }

    public String getETime() {
        return eTime;
    }

    public String getSFNm() {
        return SFNm;
    }

    public void setETime(String eTime) {
        this.eTime = eTime;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getNumberofH() {
        return numberofH;
    }

    public void setNumberofH(String numberofH) {
        this.numberofH = numberofH;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getEStatus() {
        return eStatus;
    }

    public void setEStatus(String eStatus) {
        this.eStatus = eStatus;
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
