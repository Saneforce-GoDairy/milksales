package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateResult {

    @SerializedName("Trans_POrd_No")
    @Expose
    private String transPOrdNo;
    @SerializedName("Trans_Sl_No")
    @Expose
    private String transSlNo;
    @SerializedName("Product_Code")
    @Expose
    private String productCode;
    @SerializedName("Product_Name")
    @Expose
    private String productName;
    @SerializedName("CQty")
    @Expose
    private String cQty;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("Rate")
    @Expose
    private String rate;
    @SerializedName("Order_Flag")
    @Expose
    private Integer orderFlag;
    @SerializedName("Division_Code")
    @Expose
    private Integer divisionCode;
    @SerializedName("Cl_bal")
    @Expose
    private String clBal;
    @SerializedName("Free")
    @Expose
    private Integer free;
    @SerializedName("discount_price")
    @Expose
    private Integer discountPrice;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("Offer_ProductNm")
    @Expose
    private String offerProductNm;
    @SerializedName("Offer_ProductCd")
    @Expose
    private String offerProductCd;
    @SerializedName("Offer_Product_Unit")
    @Expose
    private String offerProductUnit;
    @SerializedName("Product_Unit_Name")
    @Expose
    private String productUnitName;
    @SerializedName("PQty")
    @Expose
    private Integer pQty;

    public String getTransPOrdNo() {
        return transPOrdNo;
    }

    public void setTransPOrdNo(String transPOrdNo) {
        this.transPOrdNo = transPOrdNo;
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

    public String getCQty() {
        return cQty;
    }

    public void setCQty(String cQty) {
        this.cQty = cQty;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Integer getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(Integer orderFlag) {
        this.orderFlag = orderFlag;
    }

    public Integer getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(Integer divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getClBal() {
        return clBal;
    }

    public void setClBal(String clBal) {
        this.clBal = clBal;
    }

    public Integer getFree() {
        return free;
    }

    public void setFree(Integer free) {
        this.free = free;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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

    public String getOfferProductUnit() {
        return offerProductUnit;
    }

    public void setOfferProductUnit(String offerProductUnit) {
        this.offerProductUnit = offerProductUnit;
    }

    public String getProductUnitName() {
        return productUnitName;
    }

    public void setProductUnitName(String productUnitName) {
        this.productUnitName = productUnitName;
    }

    public Integer getPQty() {
        return pQty;
    }

    public void setPQty(Integer pQty) {
        this.pQty = pQty;
    }
}
