package com.saneforce.milksales.SFA_Adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outlet_Orders_Alldays {

    @SerializedName("slno")
    @Expose
    private String slno;
    @SerializedName("Order_Date")
    @Expose
    private String orderDate;
    @SerializedName("Outlet_Name")
    @Expose
    private String outletName;
    @SerializedName("NetAmount")
    @Expose
    private String netAmount;
    @SerializedName("Discount_Amount")
    @Expose
    private String discountAmount;
    @SerializedName("Order_Value")
    @Expose
    private Double orderValue;
    @SerializedName("invoicevalues")
    @Expose
    private Double invoicevalues;
    @SerializedName("Invoice_Flag")
    @Expose
    private String invoiceFlag;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Outlet_Code")
    @Expose
    private String outletCode;
    @SerializedName("sf_code")
    @Expose
    private String sfCode;
    @SerializedName("Territory_Code")
    @Expose
    private String Territory_Code;

    public String getTerritory_Code() {
        return Territory_Code;
    }

    public void setTerritory_Code(String territory_Code) {
        Territory_Code = territory_Code;
    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Double orderValue) {
        this.orderValue = orderValue;
    }

    public Double getInvoicevalues() {
        return invoicevalues;
    }

    public void setInvoicevalues(Double invoicevalues) {
        this.invoicevalues = invoicevalues;
    }

    public String getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(String invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

}
