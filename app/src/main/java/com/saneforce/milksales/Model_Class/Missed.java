package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Missed {

    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Sl_No")
    @Expose
    private Integer slNo;
    @SerializedName("mobilenumber")
    @Expose
    private String mobilenumber;
    @SerializedName("Shift_Name")
    @Expose
    private String shiftName;
    @SerializedName("Checkin_Time")
    @Expose
    private String checkinTime;
    @SerializedName("Sf_name")
    @Expose
    private String sfName;
    @SerializedName("Checkout_Tme")
    @Expose
    private String checkoutTme;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Missed_punch_date")
    @Expose
    private String missedPunchDate;
    @SerializedName("AppliedDate")
    @Expose
    private String appliedDate;
    @SerializedName("Reporting_To_SF")
    @Expose
    private String reportingToSF;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("HQ")
    @Expose
    private String hQ;
    @SerializedName("EmpCode")
    @Expose
    private String empCode;

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public String getCheckoutTme() {
        return checkoutTme;
    }

    public void setCheckoutTme(String checkoutTme) {
        this.checkoutTme = checkoutTme;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMissedPunchDate() {
        return missedPunchDate;
    }

    public void setMissedPunchDate(String missedPunchDate) {
        this.missedPunchDate = missedPunchDate;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getReportingToSF() {
        return reportingToSF;
    }

    public void setReportingToSF(String reportingToSF) {
        this.reportingToSF = reportingToSF;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getHQ() {
        return hQ;
    }

    public void setHQ(String hQ) {
        this.hQ = hQ;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }
}
