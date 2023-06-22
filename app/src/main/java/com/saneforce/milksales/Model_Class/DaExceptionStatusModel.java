package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DaExceptionStatusModel {
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Applied_Date")
    @Expose
    private String appliedDate;
    @SerializedName("DA_Date")
    @Expose
    private String dADate;
    @SerializedName("DA_Amt")
    @Expose
    private Double dAAmt;
    @SerializedName("Actual_Time")
    @Expose
    private String actualTime;
    @SerializedName("Early_Late_Time")
    @Expose
    private String earlyLateTime;
    @SerializedName("FMOTName")
    @Expose
    private String fMOTName;
    @SerializedName("TMOTName")
    @Expose
    private String tMOTName;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("RejectReason")
    @Expose
    private String rejectReason;
    @SerializedName("Approve_Date")
    @Expose
    private String approveDate;

    @SerializedName("Approval_Flag")
    @Expose
    private String Approval_Flag;
    @SerializedName("DA_Type")
    @Expose
    private String DA_Type;

    @SerializedName("DA_Url")
    @Expose
    private String DA_Url;

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getDADate() {
        return dADate;
    }

    public void setDADate(String dADate) {
        this.dADate = dADate;
    }

    public Double getDAAmt() {
        return dAAmt;
    }

    public void setDAAmt(Double dAAmt) {
        this.dAAmt = dAAmt;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public String getEarlyLateTime() {
        return earlyLateTime;
    }

    public void setEarlyLateTime(String earlyLateTime) {
        this.earlyLateTime = earlyLateTime;
    }

    public String getFMOTName() {
        return fMOTName;
    }

    public void setFMOTName(String fMOTName) {
        this.fMOTName = fMOTName;
    }

    public String getTMOTName() {
        return tMOTName;
    }

    public void setTMOTName(String tMOTName) {
        this.tMOTName = tMOTName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getdADate() {
        return dADate;
    }

    public void setdADate(String dADate) {
        this.dADate = dADate;
    }

    public Double getdAAmt() {
        return dAAmt;
    }

    public void setdAAmt(Double dAAmt) {
        this.dAAmt = dAAmt;
    }

    public String getfMOTName() {
        return fMOTName;
    }

    public void setfMOTName(String fMOTName) {
        this.fMOTName = fMOTName;
    }

    public String gettMOTName() {
        return tMOTName;
    }

    public void settMOTName(String tMOTName) {
        this.tMOTName = tMOTName;
    }

    public String getApproval_Flag() {
        return Approval_Flag;
    }

    public void setApproval_Flag(String approval_Flag) {
        Approval_Flag = approval_Flag;
    }

    public String getDA_Type() {
        return DA_Type;
    }

    public void setDA_Type(String DA_Type) {
        this.DA_Type = DA_Type;
    }

    public String getDA_Url() {
        return DA_Url;
    }

    public void setDA_Url(String DA_Url) {
        this.DA_Url = DA_Url;
    }
}
