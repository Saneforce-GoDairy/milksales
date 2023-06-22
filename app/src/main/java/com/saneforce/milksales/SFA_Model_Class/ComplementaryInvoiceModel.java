package com.saneforce.milksales.SFA_Model_Class;

public class ComplementaryInvoiceModel {
    String invoice, date, value;
    boolean status;

    public ComplementaryInvoiceModel(String invoice, String date, String value, boolean status) {
        this.invoice = invoice;
        this.date = date;
        this.value = value;
        this.status = status;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
