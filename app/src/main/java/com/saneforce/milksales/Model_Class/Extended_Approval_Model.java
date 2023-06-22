package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Extended_Approval_Model {


    @SerializedName("Shiftnames")
    @Expose
    private String shiftnames;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("sf_Designation_Short_Name")
    @Expose
    private String sfDesignationShortName;
    @SerializedName("SF_Mobile")
    @Expose
    private String sFMobile;
    @SerializedName("Sl_No")
    @Expose
    private Integer slNo;
    @SerializedName("S_Time")
    @Expose
    private String sTime;
    @SerializedName("Entrydate")
    @Expose
    private String entrydate;
    @SerializedName("E_Time")
    @Expose
    private String eTime;
    @SerializedName("NumberofH")
    @Expose
    private String numberofH;
    @SerializedName("Sf_name")
    @Expose
    private String sfName;
    @SerializedName("checkin")
    @Expose
    private String checkin;
    @SerializedName("checkout")
    @Expose
    private String checkout;
    @SerializedName("Start_Date")
    @Expose
    private String startDate;
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

    public String getSfDesignationShortName() {
        return sfDesignationShortName;
    }

    public void setSfDesignationShortName(String sfDesignationShortName) {
        this.sfDesignationShortName = sfDesignationShortName;
    }

    public String getSFMobile() {
        return sFMobile;
    }

    public void setSFMobile(String sFMobile) {
        this.sFMobile = sFMobile;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public String getSTime() {
        return sTime;
    }

    public void setSTime(String sTime) {
        this.sTime = sTime;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }

    public String getETime() {
        return eTime;
    }

    public void setETime(String eTime) {
        this.eTime = eTime;
    }

    public String getNumberofH() {
        return numberofH;
    }

    public void setNumberofH(String numberofH) {
        this.numberofH = numberofH;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
