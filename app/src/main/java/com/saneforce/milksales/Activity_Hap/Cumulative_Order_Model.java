package com.saneforce.milksales.Activity_Hap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cumulative_Order_Model {
    @SerializedName("value")
    @Expose
    private int value;
    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("existing")
    @Expose
    private Integer existing;

    @SerializedName("newCustomer")
    @Expose
    private Integer newCustomer;

    @SerializedName("TotalLtrs")
    @Expose
    private Integer totalLtrs;
    @SerializedName("NewOrdersLtrs")
    @Expose
    private Integer newOrderLtrs;

    @SerializedName("totalCurd")
    @Expose
    private int totalCurd;
    @SerializedName("desc")
    @Expose
    private int totalMilkProduct;


    public Cumulative_Order_Model(String desc, Integer existing, Integer newCustomer, Integer totalLtrs, Integer newOrderLtrs, int totalCurd, int totalMilkProduct) {
        this.desc = desc;
        this.existing = existing;
        this.newCustomer = newCustomer;
        this.totalLtrs = totalLtrs;
        this.newOrderLtrs = newOrderLtrs;
        this.totalCurd = totalCurd;
        this.totalMilkProduct = totalMilkProduct;
    }

    public Cumulative_Order_Model(String desc, int value) {
        this.desc = desc;
        this.value = value;
    }

    public Integer getNewOrderLtrs() {
        return newOrderLtrs;
    }

    public void setNewOrderLtrs(Integer newOrderLtrs) {
        this.newOrderLtrs = newOrderLtrs;
    }

    public Integer getTotalLtrs() {
        return totalLtrs;
    }

    public void setTotalLtrs(Integer totalLtrs) {
        this.totalLtrs = totalLtrs;
    }

    public Integer getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(Integer newCustomer) {
        this.newCustomer = newCustomer;
    }

    public Integer getExisting() {
        return existing;
    }

    public void setExisting(Integer existing) {
        this.existing = existing;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
