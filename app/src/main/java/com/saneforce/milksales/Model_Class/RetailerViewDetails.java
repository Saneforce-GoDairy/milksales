package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetailerViewDetails {
    @SerializedName("SVL")
    @Expose
    private String sVL;
    @SerializedName("ListedDrCode")
    @Expose
    private String listedDrCode;
    @SerializedName("DrCat")
    @Expose
    private String drCat;
    @SerializedName("DrSpl")
    @Expose
    private String drSpl;
    @SerializedName("DrCamp")
    @Expose
    private String drCamp;
    @SerializedName("DrProd")
    @Expose
    private String drProd;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("SlabName")
    @Expose
    private String slabName;
    @SerializedName("GST")
    @Expose
    private String gST;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("Giftenrol_Name")
    @Expose
    private String giftenrolName;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("StockistDetails")
    @Expose
    private List<StockistDetail> stockistDetails = null;
    @SerializedName("OpeningStock")
    @Expose
    private List<OpeningStock> openingStock = null;
    @SerializedName("MOQ")
    @Expose
    private List<Object> mOQ = null;
    @SerializedName("POTENTIAL")
    @Expose
    private List<POTENTIAL> pOTENTIAL = null;
    @SerializedName("LastorderAmount")
    @Expose
    private Integer lastorderAmount;
    @SerializedName("MOV")
    @Expose
    private List<MOV> mOV = null;
    @SerializedName("LVDt")
    @Expose
    private String lVDt;
    @SerializedName("CallFd")
    @Expose
    private String callFd;
    @SerializedName("Rmks")
    @Expose
    private String rmks;
    @SerializedName("ProdSmp")
    @Expose
    private String prodSmp;
    @SerializedName("Prodgvn")
    @Expose
    private String prodgvn;
    @SerializedName("DrGft")
    @Expose
    private String drGft;

    public String getSVL() {
        return sVL;
    }

    public void setSVL(String sVL) {
        this.sVL = sVL;
    }

    public String getListedDrCode() {
        return listedDrCode;
    }

    public void setListedDrCode(String listedDrCode) {
        this.listedDrCode = listedDrCode;
    }

    public String getDrCat() {
        return drCat;
    }

    public void setDrCat(String drCat) {
        this.drCat = drCat;
    }

    public String getDrSpl() {
        return drSpl;
    }

    public void setDrSpl(String drSpl) {
        this.drSpl = drSpl;
    }

    public String getDrCamp() {
        return drCamp;
    }

    public void setDrCamp(String drCamp) {
        this.drCamp = drCamp;
    }

    public String getDrProd() {
        return drProd;
    }

    public void setDrProd(String drProd) {
        this.drProd = drProd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSlabName() {
        return slabName;
    }

    public void setSlabName(String slabName) {
        this.slabName = slabName;
    }

    public String getGST() {
        return gST;
    }

    public void setGST(String gST) {
        this.gST = gST;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGiftenrolName() {
        return giftenrolName;
    }

    public void setGiftenrolName(String giftenrolName) {
        this.giftenrolName = giftenrolName;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<StockistDetail> getStockistDetails() {
        return stockistDetails;
    }

    public void setStockistDetails(List<StockistDetail> stockistDetails) {
        this.stockistDetails = stockistDetails;
    }

    public List<OpeningStock> getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(List<OpeningStock> openingStock) {
        this.openingStock = openingStock;
    }

    public List<Object> getMOQ() {
        return mOQ;
    }

    public void setMOQ(List<Object> mOQ) {
        this.mOQ = mOQ;
    }

    public List<POTENTIAL> getPOTENTIAL() {
        return pOTENTIAL;
    }

    public void setPOTENTIAL(List<POTENTIAL> pOTENTIAL) {
        this.pOTENTIAL = pOTENTIAL;
    }

    public Integer getLastorderAmount() {
        return lastorderAmount;
    }

    public void setLastorderAmount(Integer lastorderAmount) {
        this.lastorderAmount = lastorderAmount;
    }

    public List<MOV> getMOV() {
        return mOV;
    }

    public void setMOV(List<MOV> mOV) {
        this.mOV = mOV;
    }

    public String getLVDt() {
        return lVDt;
    }

    public void setLVDt(String lVDt) {
        this.lVDt = lVDt;
    }

    public String getCallFd() {
        return callFd;
    }

    public void setCallFd(String callFd) {
        this.callFd = callFd;
    }

    public String getRmks() {
        return rmks;
    }

    public void setRmks(String rmks) {
        this.rmks = rmks;
    }

    public String getProdSmp() {
        return prodSmp;
    }

    public void setProdSmp(String prodSmp) {
        this.prodSmp = prodSmp;
    }

    public String getProdgvn() {
        return prodgvn;
    }

    public void setProdgvn(String prodgvn) {
        this.prodgvn = prodgvn;
    }

    public String getDrGft() {
        return drGft;
    }

    public void setDrGft(String drGft) {
        this.drGft = drGft;
    }

}