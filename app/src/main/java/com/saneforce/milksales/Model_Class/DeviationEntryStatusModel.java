package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviationEntryStatusModel {
    public String getSFNm() {
        return SFNm;
    }

    public void setSFNm(String SFNm) {
        this.SFNm = SFNm;
    }

    public String getdStatus() {
        return dStatus;
    }

    public void setdStatus(String dStatus) {
        this.dStatus = dStatus;
    }

    @SerializedName("SFNm")
    @Expose
    private String SFNm;
    @SerializedName("sf_code")
    @Expose
    private String sfCode;
    @SerializedName("Deviation_Id")
    @Expose
    private Integer deviationId;
    @SerializedName("Created_Date")
    @Expose
    private String createdDate;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Deviation_Type")
    @Expose
    private String deviationType;
    @SerializedName("Deviation_Date")
    @Expose
    private String deviationDate;
    @SerializedName("LastUpdt_Date")
    @Expose
    private String lastUpdtDate;
    @SerializedName("DStatus")
    @Expose
    private String dStatus;
    @SerializedName("StusClr")
    @Expose
    private String stusClr;
    @SerializedName("Rejected_Reason")
    @Expose
    private Object rejectedReason;
    @SerializedName("Devi_Active_Flag")
    @Expose
    private Integer deviActiveFlag;

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public Integer getDeviationId() {
        return deviationId;
    }

    public void setDeviationId(Integer deviationId) {
        this.deviationId = deviationId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDeviationType() {
        return deviationType;
    }

    public void setDeviationType(String deviationType) {
        this.deviationType = deviationType;
    }

    public String getDeviationDate() {
        return deviationDate;
    }

    public void setDeviationDate(String deviationDate) {
        this.deviationDate = deviationDate;
    }

    public String getLastUpdtDate() {
        return lastUpdtDate;
    }

    public void setLastUpdtDate(String lastUpdtDate) {
        this.lastUpdtDate = lastUpdtDate;
    }

    public String getDStatus() {
        return dStatus;
    }

    public void setDStatus(String dStatus) {
        this.dStatus = dStatus;
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

    public Integer getDeviActiveFlag() {
        return deviActiveFlag;
    }

    public void setDeviActiveFlag(Integer deviActiveFlag) {
        this.deviActiveFlag = deviActiveFlag;
    }

}
