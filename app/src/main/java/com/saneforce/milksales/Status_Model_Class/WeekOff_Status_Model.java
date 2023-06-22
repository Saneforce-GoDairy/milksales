package com.saneforce.milksales.Status_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeekOff_Status_Model {
    @SerializedName("SFNm")
    @Expose
    private String sFNm;
    @SerializedName("WrkTyp")
    @Expose
    private String wrkTyp;
    @SerializedName("wkDate")
    @Expose
    private String wkDate;
    @SerializedName("wkDt")
    @Expose
    private String wkDt;
    @SerializedName("DtNm")
    @Expose
    private String dtNm;
    @SerializedName("Rmks")
    @Expose
    private String rmks;
    @SerializedName("sbmtOn")
    @Expose
    private String sbmtOn;
    public void setSFNm(String SFNm) {
        this.sFNm = SFNm;
    }
    public String getWrkTyp() {
        return wrkTyp;
    }

    public void setWrkTyp(String wrkTyp) {
        this.wrkTyp = wrkTyp;
    }

    public String getWkDate() {
        return wkDate;
    }

    public void setWkDate(String wkDate) {
        this.wkDate = wkDate;
    }

    public String getWkDt() {
        return wkDt;
    }

    public void setWkDt(String wkDt) {
        this.wkDt = wkDt;
    }

    public String getDtNm() {
        return dtNm;
    }

    public void setDtNm(String dtNm) {
        this.dtNm = dtNm;
    }

    public String getRmks() {
        return rmks;
    }
    public String getSFNm() {
        return sFNm;
    }
    public void setRmks(String rmks) {
        this.rmks = rmks;
    }

    public String getSbmtOn() {
        return sbmtOn;
    }

    public void setSbmtOn(String sbmtOn) {
        this.sbmtOn = sbmtOn;
    }

}
