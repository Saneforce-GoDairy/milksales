package com.saneforce.milksales.Status_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class View_All_Model {

    @SerializedName("WrkDate")
    @Expose
    private String wrkDate;
    @SerializedName("SFT_Name")
    @Expose
    private Object sFTName;
    @SerializedName("Shft")
    @Expose
    private Object shft;
    @SerializedName("ShftE")
    @Expose
    private Object shftE;
    @SerializedName("AttTm")
    @Expose
    private Object attTm;
    @SerializedName("ET")
    @Expose
    private Object eT;
    @SerializedName("DayStatus")
    @Expose
    private String dayStatus;
    @SerializedName("StusClr")
    @Expose
    private String stusClr;

    @SerializedName("Geoin")
    @Expose
    private String Geoin    ;
    @SerializedName("Geoout")
    @Expose
    private String Geoout;
    @SerializedName("Loc")
    @Expose
    private String Loc;

    @SerializedName("flag")
    @Expose
    private int flag;



    @SerializedName("Attndt")
    @Expose
    private String Attndt;

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getGeoin() {
        return Geoin;
    }

    public void setGeoin(String geoin) {
        Geoin = geoin;
    }

    public String getGeoout() {
        return Geoout;
    }

    public void setGeoout(String geoout) {
        Geoout = geoout;
    }

    public String getWrkDate() {
        return wrkDate;
    }

    public void setWrkDate(String wrkDate) {
        this.wrkDate = wrkDate;
    }

    public Object getSFTName() {
        return sFTName;
    }

    public void setSFTName(Object sFTName) {
        this.sFTName = sFTName;
    }

    public Object getShft() {
        return shft;
    }

    public void setShft(Object shft) {
        this.shft = shft;
    }

    public Object getShftE() {
        return shftE;
    }

    public void setShftE(Object shftE) {
        this.shftE = shftE;
    }

    public Object getAttTm() {
        return attTm;
    }

    public void setAttTm(Object attTm) {
        this.attTm = attTm;
    }

    public Object getET() {
        return eT;
    }

    public void setET(Object eT) {
        this.eT = eT;
    }

    public String getDayStatus() {
        return dayStatus;
    }

    public void setDayStatus(String dayStatus) {
        this.dayStatus = dayStatus;
    }

    public String getStusClr() {
        return stusClr;
    }

    public void setStusClr(String stusClr) {
        this.stusClr = stusClr;
    }

    public String getAttndt() {
        return Attndt;
    }

    public void setAttndt(String attndt) {
        Attndt = attndt;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
