package com.saneforce.godairy.Model_Class;

public class PaymentModel {

    private  String billNo;
    private String billDate;
    private Double billedAmt;
    private Double pendingAmt;
    private  Double  Amt=0.0;
    private  String orderNo;

    public PaymentModel(String bill_no, String bill_date, double billed_amt, double pending_amt, String orderNo) {
        this.billNo=bill_no;
        this.billDate=bill_date;
        this.billedAmt=billed_amt;
        this.pendingAmt=pending_amt;
        this.orderNo=orderNo;
    }
    public PaymentModel(String bill_no, String bill_date, double billed_amt, double pending_amt, double paid_amt) {
        this.billNo=bill_no;
        this.billDate=bill_date;
        this.billedAmt=billed_amt;
        this.pendingAmt=pending_amt;
       this.Amt=paid_amt;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public Double getBilledAmt() {
        return billedAmt;
    }

    public void setBilledAmt(Double billedAmt) {
        this.billedAmt = billedAmt;
    }

    public Double getPendingAmt() {
        return pendingAmt;
    }

    public void setPendingAmt(Double pendingAmt) {
        this.pendingAmt = pendingAmt;
    }

    public Double getAmt() {
        return Amt;
    }

    public void setAmt(Double amt) {
        Amt = amt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }



}
