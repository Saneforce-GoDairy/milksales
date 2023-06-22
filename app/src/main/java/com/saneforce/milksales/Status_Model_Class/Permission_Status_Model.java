package com.saneforce.milksales.Status_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permission_Status_Model {

    @SerializedName("SFNm")
    @Expose
    private String sFNm;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;

    @SerializedName("Permissiondate")
    @Expose
    private String permissiondate;
    @SerializedName("Approval_Flag")
    @Expose
    private Integer approvalFlag;
    @SerializedName("Submission_date")
    @Expose
    private String submissionDate;
    @SerializedName("Created_Date")
    @Expose
    private String createdDate;
    @SerializedName("Approveddate")
    @Expose
    private String approveddate;
    @SerializedName("FromTime")
    @Expose
    private String fromTime;
    @SerializedName("ToTime")
    @Expose
    private String toTime;
    @SerializedName("Reason")
    @Expose
    private String reason;
    @SerializedName("Noof_hours")
    @Expose
    private String noofHours;
    @SerializedName("PStatus")
    @Expose
    private String pStatus;

    @SerializedName("Rejected_Reason")
    @Expose
    private String rejectedReason;
    public void setsFNm(String sfNm) {
        this.sFNm = sfNm;
    }
    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getSFNm() {
        return sFNm;
    }

    public void setSFNm(String sFNm) {
        this.sFNm = sFNm;
    }

    public String getPermissiondate() {
        return permissiondate;
    }

    public void setPermissiondate(String permissiondate) {
        this.permissiondate = permissiondate;
    }

    public Integer getApprovalFlag() {
        return approvalFlag;
    }

    public void setApprovalFlag(Integer approvalFlag) {
        this.approvalFlag = approvalFlag;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getApproveddate() {
        return approveddate;
    }

    public void setApproveddate(String approveddate) {
        this.approveddate = approveddate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
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

    public String getPStatus() {
        return pStatus;
    }

    public void setPStatus(String pStatus) {
        this.pStatus = pStatus;
    }



    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }
}
