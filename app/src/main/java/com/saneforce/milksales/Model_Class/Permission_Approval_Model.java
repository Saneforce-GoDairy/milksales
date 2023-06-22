package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permission_Approval_Model {
    @SerializedName("Sl_no")
    @Expose
    private String slNo;
    @SerializedName("SF_Mobile")
    @Expose
    private String sFMobile;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Permissiondate")
    @Expose
    private String permissiondate;
    @SerializedName("FromTime")
    @Expose
    private String fromTime;
    @SerializedName("Applieddate")
    @Expose
    private String applieddate;
    @SerializedName("ToTime")
    @Expose
    private String toTime;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Noof_hours")
    @Expose
    private String noofHours;
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
    @SerializedName("Submission_date")
    @Expose
    private String submissionDate;
    @SerializedName("Approval_Flag")
    @Expose
    private Integer approvalFlag;
    @SerializedName("Rejected_Reason")
    @Expose
    private String rejectedReason;
    @SerializedName("Pstatus_Sf")
    @Expose
    private String pstatusSf;

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
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

    public String getPermissiondate() {
        return permissiondate;
    }

    public void setPermissiondate(String permissiondate) {
        this.permissiondate = permissiondate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getApplieddate() {
        return applieddate;
    }

    public void setApplieddate(String applieddate) {
        this.applieddate = applieddate;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNoofHours() {
        return noofHours;
    }

    public void setNoofHours(String noofHours) {
        this.noofHours = noofHours;
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

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Integer getApprovalFlag() {
        return approvalFlag;
    }

    public void setApprovalFlag(Integer approvalFlag) {
        this.approvalFlag = approvalFlag;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getPstatusSf() {
        return pstatusSf;
    }

    public void setPstatusSf(String pstatusSf) {
        this.pstatusSf = pstatusSf;
    }

}
