package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Approval {
    @SerializedName("SF_Mobile")
    @Expose
    private String sFMobile;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Leave_Id")
    @Expose
    private Integer leaveId;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Applieddate")
    @Expose
    private String applieddate;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("FieldForceName")
    @Expose
    private String fieldForceName;
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
    @SerializedName("From_Date")
    @Expose
    private String fromDate;
    @SerializedName("To_Date")
    @Expose
    private String toDate;
    @SerializedName("LeaveDays")
    @Expose
    private Integer leaveDays;
    @SerializedName("Division_Code")
    @Expose
    private Integer divisionCode;
    @SerializedName("sf_Designation_Short_Name")
    @Expose
    private String sfDesignationShortName;
    @SerializedName("Leave_Type")
    @Expose
    private Integer leaveType;
    @SerializedName("Leave_Name")
    @Expose
    private String leaveName;

    public Approval(String sFMobile, String sfCode, Integer leaveId, String reason, String applieddate, String address, String fieldForceName, String reportingToSF, String designation, String hQ, String empCode, String fromDate, String toDate, Integer leaveDays, Integer divisionCode, String sfDesignationShortName, Integer leaveType, String leaveName) {
        this.sFMobile = sFMobile;
        this.sfCode = sfCode;
        this.leaveId = leaveId;
        this.reason = reason;
        this.applieddate = applieddate;
        this.address = address;
        this.fieldForceName = fieldForceName;
        this.reportingToSF = reportingToSF;
        this.designation = designation;
        this.hQ = hQ;
        this.empCode = empCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.leaveDays = leaveDays;
        this.divisionCode = divisionCode;
        this.sfDesignationShortName = sfDesignationShortName;
        this.leaveType = leaveType;
        this.leaveName = leaveName;
    }

    public String getSFMobile() {
        return sFMobile;
    }

    public void setSFMobile(String sFMobile) {
        this.sFMobile = sFMobile;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApplieddate() {
        return applieddate;
    }

    public void setApplieddate(String applieddate) {
        this.applieddate = applieddate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFieldForceName() {
        return fieldForceName;
    }

    public void setFieldForceName(String fieldForceName) {
        this.fieldForceName = fieldForceName;
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Integer getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(Integer divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getSfDesignationShortName() {
        return sfDesignationShortName;
    }

    public void setSfDesignationShortName(String sfDesignationShortName) {
        this.sfDesignationShortName = sfDesignationShortName;
    }

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }
}

