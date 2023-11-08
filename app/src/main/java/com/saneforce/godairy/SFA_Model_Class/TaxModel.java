package com.saneforce.godairy.SFA_Model_Class;

public class TaxModel {
    String taxName, taxCode;
    double taxAmt, taxVal;

    public TaxModel(String taxName, String taxCode, double taxAmt, double taxVal) {
        this.taxName = taxName;
        this.taxCode = taxCode;
        this.taxAmt = taxAmt;
        this.taxVal = taxVal;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(double taxAmt) {
        this.taxAmt = taxAmt;
    }

    public double getTaxVal() {
        return taxVal;
    }

    public void setTaxVal(double taxVal) {
        this.taxVal = taxVal;
    }
}
