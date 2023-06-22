package com.saneforce.milksales.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outlet_Report_Product_Details_Modal {
    @SerializedName("Trans_Order_No")
    @Expose
    private String transOrderNo;
    @SerializedName("Trans_Sl_No")
    @Expose
    private String transSlNo;
    @SerializedName("Product_Code")
    @Expose
    private String productCode;
    @SerializedName("Product_Name")
    @Expose
    private String productName;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;
    @SerializedName("value")
    @Expose
    private Double value;
    @SerializedName("Rate")
    @Expose
    private Double rate;
    @SerializedName("net_weight")
    @Expose
    private Double netWeight;
    @SerializedName("free")
    @Expose
    private Double free;
    @SerializedName("discount_price")
    @Expose
    private Double discountPrice;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("rateMode")
    @Expose
    private String rateMode;
    @SerializedName("DistDisc")
    @Expose
    private Double distDisc;
    @SerializedName("DistPicRate")
    @Expose
    private Double distPicRate;
    @SerializedName("MfgDt")
    @Expose
    private String mfgDt;
    @SerializedName("ClStock")
    @Expose
    private Double clStock;
    @SerializedName("PromoVal")
    @Expose
    private Double promoVal;
    @SerializedName("Div_ID")
    @Expose
    private Double divID;
    @SerializedName("Offer_ProductNm")
    @Expose
    private String offerProductNm;
    @SerializedName("Offer_ProductCd")
    @Expose
    private String offerProductCd;
    @SerializedName("CClStock")
    @Expose
    private String cClStock;
    @SerializedName("New_Qty")
    @Expose
    private Double newQty;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("off_pro_unit")
    @Expose
    private String offProUnit;
    @SerializedName("umo_unit")
    @Expose
    private Double umoUnit;
    @SerializedName("qty")
    @Expose
    private Double qty;

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getTransSlNo() {
        return transSlNo;
    }

    public void setTransSlNo(String transSlNo) {
        this.transSlNo = transSlNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getFree() {
        return free;
    }

    public void setFree(Double free) {
        this.free = free;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getRateMode() {
        return rateMode;
    }

    public void setRateMode(String rateMode) {
        this.rateMode = rateMode;
    }

    public Double getDistDisc() {
        return distDisc;
    }

    public void setDistDisc(Double distDisc) {
        this.distDisc = distDisc;
    }

    public Double getDistPicRate() {
        return distPicRate;
    }

    public void setDistPicRate(Double distPicRate) {
        this.distPicRate = distPicRate;
    }

    public String getMfgDt() {
        return mfgDt;
    }

    public void setMfgDt(String mfgDt) {
        this.mfgDt = mfgDt;
    }

    public Double getClStock() {
        return clStock;
    }

    public void setClStock(Double clStock) {
        this.clStock = clStock;
    }

    public Double getPromoVal() {
        return promoVal;
    }

    public void setPromoVal(Double promoVal) {
        this.promoVal = promoVal;
    }

    public Double getDivID() {
        return divID;
    }

    public void setDivID(Double divID) {
        this.divID = divID;
    }

    public String getOfferProductNm() {
        return offerProductNm;
    }

    public void setOfferProductNm(String offerProductNm) {
        this.offerProductNm = offerProductNm;
    }

    public String getOfferProductCd() {
        return offerProductCd;
    }

    public void setOfferProductCd(String offerProductCd) {
        this.offerProductCd = offerProductCd;
    }

    public String getCClStock() {
        return cClStock;
    }

    public void setCClStock(String cClStock) {
        this.cClStock = cClStock;
    }

    public Double getNewQty() {
        return newQty;
    }

    public void setNewQty(Double newQty) {
        this.newQty = newQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOffProUnit() {
        return offProUnit;
    }

    public void setOffProUnit(String offProUnit) {
        this.offProUnit = offProUnit;
    }

    public Double getUmoUnit() {
        return umoUnit;
    }

    public void setUmoUnit(Double umoUnit) {
        this.umoUnit = umoUnit;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
