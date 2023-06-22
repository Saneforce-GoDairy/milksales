package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deviation_Entr_Modal {

    @SerializedName("SF_Mobile")
    @Expose
    private String sFMobile;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Deviation_Id")
    @Expose
    private Double deviationId;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Applieddate")
    @Expose
    private String applieddate;
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
    @SerializedName("Deviation_Date")
    @Expose
    private String deviationDate;
    @SerializedName("sf_Designation_Short_Name")
    @Expose
    private String sfDesignationShortName;
    @SerializedName("DeviationName")
    @Expose
    private String deviationName;

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

    public Double getDeviationId() {
        return deviationId;
    }

    public void setDeviationId(Double deviationId) {
        this.deviationId = deviationId;
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

    public String getDeviationDate() {
        return deviationDate;
    }

    public void setDeviationDate(String deviationDate) {
        this.deviationDate = deviationDate;
    }

    public String getSfDesignationShortName() {
        return sfDesignationShortName;
    }

    public void setSfDesignationShortName(String sfDesignationShortName) {
        this.sfDesignationShortName = sfDesignationShortName;
    }

    public String getDeviationName() {
        return deviationName;
    }

    public void setDeviationName(String deviationName) {
        this.deviationName = deviationName;
    }
}
