package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Onduty_Approval_Model {


    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("duty_id")
    @Expose
    private String dutyId;
    @SerializedName("ondutytype")
    @Expose
    private String ondutytype;
    @SerializedName("SF_Mobile")
    @Expose
    private String sFMobile;
    @SerializedName("checkin")
    @Expose
    private String checkin;
    @SerializedName("checkout")
    @Expose
    private String checkout;
    @SerializedName("Start_Time")
    @Expose
    private String startTime;
    @SerializedName("End_Time")
    @Expose
    private String endTime;
    @SerializedName("FieldForceName")
    @Expose
    private String fieldForceName;
    @SerializedName("ODLocName")
    @Expose
    private String oDLocName;
    @SerializedName("login_date")
    @Expose
    private String loginDate;
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
    @SerializedName("Rmks")
    @Expose
    private String rmks;

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getOndutytype() {
        return ondutytype;
    }

    public void setOndutytype(String ondutytype) {
        this.ondutytype = ondutytype;
    }

    public String getSFMobile() {
        return sFMobile;
    }

    public void setSFMobile(String sFMobile) {
        this.sFMobile = sFMobile;
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

    public String getFieldForceName() {
        return fieldForceName;
    }

    public void setFieldForceName(String fieldForceName) {
        this.fieldForceName = fieldForceName;
    }

    public String getODLocName() {
        return oDLocName;
    }

    public void setODLocName(String oDLocName) {
        this.oDLocName = oDLocName;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
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

    public String getRmks() {
        return rmks;
    }

    public void setRmks(String rmks) {
        this.rmks = rmks;
    }
}
