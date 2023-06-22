package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockistDetail {
    @SerializedName("stockist_code")
    @Expose
    private String stockistCode;
    @SerializedName("Sum")
    @Expose
    private Integer sum;
    @SerializedName("Avg")
    @Expose
    private Integer avg;
    @SerializedName("LastOrderAmt")
    @Expose
    private Object lastOrderAmt;
    @SerializedName("outStandingAmt")
    @Expose
    private Object outStandingAmt;
    @SerializedName("OrderDetails")
    @Expose
    private List<OrderDetail> orderDetails = null;

    public String getStockistCode() {
        return stockistCode;
    }

    public void setStockistCode(String stockistCode) {
        this.stockistCode = stockistCode;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}