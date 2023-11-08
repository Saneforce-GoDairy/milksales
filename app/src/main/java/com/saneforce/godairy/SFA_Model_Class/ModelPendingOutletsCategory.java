package com.saneforce.godairy.SFA_Model_Class;

public class ModelPendingOutletsCategory {

    String sfCode;

    String sfName;

    String stockistCode;

    String stockistName;

    String listedDrCount;

    String designation;

    public ModelPendingOutletsCategory(String sfCode, String sfName, String stockistCode, String stockistName, String listedDrCount, String designation) {
        this.sfCode = sfCode;
        this.sfName = sfName;
        this.stockistCode = stockistCode;
        this.stockistName = stockistName;
        this.listedDrCount = listedDrCount;
        this.designation = designation;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public String getStockistCode() {
        return stockistCode;
    }

    public void setStockistCode(String stockistCode) {
        this.stockistCode = stockistCode;
    }

    public String getStockistName() {
        return stockistName;
    }

    public void setStockistName(String stockistName) {
        this.stockistName = stockistName;
    }

    public String getListedDrCount() {
        return listedDrCount;
    }

    public void setListedDrCount(String listedDrCount) {
        this.listedDrCount = listedDrCount;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
