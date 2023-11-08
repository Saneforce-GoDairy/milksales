package com.saneforce.godairy.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tp_Approval_FF_Modal {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("Tour_Date")
    @Expose
    private String tdate;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("Monthnameexample")
    @Expose
    private String monthnameexample;
    @SerializedName("Tmonth")
    @Expose
    private String tmonth;
    @SerializedName("Tour_Month")
    @Expose
    private String tourMonth;

    @SerializedName("SF_Code")
    @Expose
    private String SF_Code;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Reporting_To_SF")
    @Expose
    private String reportingSFCode;
    @SerializedName("Tyear")
    @Expose
    private String tyear;
    @SerializedName("FieldForceName")
    @Expose
    private String fieldForceName;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("EmpCode")
    @Expose
    private String empCode;
    @SerializedName("TourHQ_Name")
    @Expose
    private String hQName;
    @SerializedName("worktype_name")
    @Expose
    private String worktypeName;

    @SerializedName("Rejection_Reason")
    @Expose
    private String rejectionReason;
    @SerializedName("Confirmed")
    @Expose
    private String confirmed;

    @SerializedName("Confirmed_Date")
    @Expose
    private String confirmedDate;


    public String getWorktypeName() {
        return worktypeName;
    }

    public void setWorktypeName(String worktypeName) {
        this.worktypeName = worktypeName;
    }
    public String getHQName() {
        return hQName;
    }

    public void setHQName(String hQName) {
        this.hQName = hQName;
    }
    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getMonthnameexample() {
        return monthnameexample;
    }

    public void setMonthnameexample(String monthnameexample) {
        this.monthnameexample = monthnameexample;
    }

    public String getTmonth() {
        return tmonth;
    }

    public void setTmonth(String tmonth) {
        this.tmonth = tmonth;
    }

    public String getTourMonth() {
        return tourMonth;
    }

    public void setTourMonth(String tourMonth) {
        this.tourMonth = tourMonth;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getTyear() {
        return tyear;
    }

    public void setTyear(String tyear) {
        this.tyear = tyear;
    }

    public String getFieldForceName() {
        return fieldForceName;
    }

    public void setFieldForceName(String fieldForceName) {
        this.fieldForceName = fieldForceName;
    }
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getDate() {
        return date;
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
    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }


    public String getSF_Code() {
        return SF_Code;
    }

    public void setSF_Code(String SF_Code) {
        this.SF_Code = SF_Code;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tp_Approval_FF_Modal)
        {
            Tp_Approval_FF_Modal temp = (Tp_Approval_FF_Modal) obj;
            if(this.fieldForceName.equals(temp.fieldForceName))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.fieldForceName.hashCode());
    }

    public String getReportingSFCode() {
        return reportingSFCode;
    }

    public void setReportingSFCode(String reportingSFCode) {
        this.reportingSFCode = reportingSFCode;
    }
    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }
}
