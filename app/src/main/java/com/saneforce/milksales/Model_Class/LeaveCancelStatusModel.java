package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveCancelStatusModel {
    @SerializedName("SFNm")
    @Expose
    private String SFNm;
    @SerializedName("sf_code")
    @Expose
    private String sfCode;
    @SerializedName("Leave_Id")
    @Expose
    private Integer leaveId;
    @SerializedName("showflag")
    @Expose
    private Integer showflag;
    @SerializedName("Created_Date")
    @Expose
    private String createdDate;
    @SerializedName("cancelreason")
    @Expose
    private String cancelreason;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Leave_Type")
    @Expose
    private String leaveType;
    @SerializedName("From_Date")
    @Expose
    private String fromDate;
    @SerializedName("LastUpdt_Date")
    @Expose
    private String lastUpdtDate;
    @SerializedName("To_Date")
    @Expose
    private String toDate;
    @SerializedName("No_of_Days")
    @Expose
    private String noOfDays;
    @SerializedName("LStatus")
    @Expose
    private String lStatus;
    @SerializedName("StusClr")
    @Expose
    private String stusClr;
    @SerializedName("Rejected_Reason")
    @Expose
    private Object rejectedReason;
    @SerializedName("Leavecancel_Id")
    @Expose
    private Integer leavecancelId;

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

    public Integer getShowflag() {
        return showflag;
    }

    public void setShowflag(Integer showflag) {
        this.showflag = showflag;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getLastUpdtDate() {
        return lastUpdtDate;
    }

    public String getSFNm() {
        return SFNm;
    }

    public void setSFNm(String SFNm) {
        this.SFNm = SFNm;
    }

    public void setLastUpdtDate(String lastUpdtDate) {
        this.lastUpdtDate = lastUpdtDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getLStatus() {
        return lStatus;
    }

    public void setLStatus(String lStatus) {
        this.lStatus = lStatus;
    }

    public String getStusClr() {
        return stusClr;
    }

    public void setStusClr(String stusClr) {
        this.stusClr = stusClr;
    }

    public Object getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(Object rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public Integer getLeavecancelId() {
        return leavecancelId;
    }

    public void setLeavecancelId(Integer leavecancelId) {
        this.leavecancelId = leavecancelId;
    }

}
