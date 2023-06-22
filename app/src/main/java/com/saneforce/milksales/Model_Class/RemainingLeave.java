package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemainingLeave {
    @SerializedName("SFCode")
    @Expose
    private String sFCode;
    @SerializedName("LeaveCode")
    @Expose
    private String leaveCode;
    @SerializedName("LeaveValue")
    @Expose
    private String leaveValue;
    @SerializedName("LeaveAvailability")
    @Expose
    private String leaveAvailability;
    @SerializedName("LeaveTaken")
    @Expose
    private String leaveTaken;
    @SerializedName("Leave_SName")
    @Expose
    private String leaveSName;
    @SerializedName("Leave_Name")
    @Expose
    private String leaveName;

    public String getSFCode() {
        return sFCode;
    }

    public void setSFCode(String sFCode) {
        this.sFCode = sFCode;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getLeaveValue() {
        return leaveValue;
    }

    public void setLeaveValue(String leaveValue) {
        this.leaveValue = leaveValue;
    }

    public String getLeaveAvailability() {
        return leaveAvailability;
    }

    public void setLeaveAvailability(String leaveAvailability) {
        this.leaveAvailability = leaveAvailability;
    }

    public String getLeaveTaken() {
        return leaveTaken;
    }

    public void setLeaveTaken(String leaveTaken) {
        this.leaveTaken = leaveTaken;
    }

    public String getLeaveSName() {
        return leaveSName;
    }

    public void setLeaveSName(String leaveSName) {
        this.leaveSName = leaveSName;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }
}
