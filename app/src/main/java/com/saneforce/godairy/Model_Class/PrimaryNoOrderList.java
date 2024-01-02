package com.saneforce.godairy.Model_Class;

import com.google.gson.annotations.SerializedName;

public class PrimaryNoOrderList {
    private String id;
    private String sfCode;
    private String reason;
    private String dateTime;
    private String erpCode;
    private Object distributeName;
    private Object lat;
    private Object lan;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSfCode() {
        return sfCode;
    }
    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getErpCode() {
        return erpCode;
    }
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }
    public Object getDistributeName() {
        return distributeName;
    }
    public void setDistributeName(Object distributeName) {
        this.distributeName = distributeName;
    }
    public Object getLat() {
        return lat;
    }
    public void setLat(Object lat) {
        this.lat = lat;
    }
    public Object getLan() {
        return lan;
    }
    public void setLan(Object lan) {
        this.lan = lan;
    }
}
