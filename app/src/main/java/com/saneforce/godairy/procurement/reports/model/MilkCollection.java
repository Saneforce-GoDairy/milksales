package com.saneforce.godairy.procurement.reports.model;

public class MilkCollection {
    private String customerNo, date, session;
    private String milkType, noOfCans, milkWeight;
    private String milkTotalQty, milkSampleNo, milkFat, milkSnf, milkClr;
    private String milkRate, totalAmount;

    public String getMilkRate() {
        return milkRate;
    }

    public void setMilkRate(String milkRate) {
        this.milkRate = milkRate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMilkTotalQty() {
        return milkTotalQty;
    }

    public void setMilkTotalQty(String milkTotalQty) {
        this.milkTotalQty = milkTotalQty;
    }

    public String getMilkSampleNo() {
        return milkSampleNo;
    }

    public void setMilkSampleNo(String milkSampleNo) {
        this.milkSampleNo = milkSampleNo;
    }

    public String getMilkFat() {
        return milkFat;
    }

    public void setMilkFat(String milkFat) {
        this.milkFat = milkFat;
    }

    public String getMilkSnf() {
        return milkSnf;
    }

    public void setMilkSnf(String milkSnf) {
        this.milkSnf = milkSnf;
    }

    public void setMilkClr(String milkClr) {
        this.milkClr = milkClr;
    }

    public String getMilkClr() {
        return milkClr;
    }

    public String getMilkType() {
        return milkType;
    }

    public void setMilkType(String milkType) {
        this.milkType = milkType;
    }

    public String getNoOfCans() {
        return noOfCans;
    }

    public void setNoOfCans(String noOfCans) {
        this.noOfCans = noOfCans;
    }

    public String getMilkWeight() {
        return milkWeight;
    }

    public void setMilkWeight(String milkWeight) {
        this.milkWeight = milkWeight;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private String id, name, customerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
