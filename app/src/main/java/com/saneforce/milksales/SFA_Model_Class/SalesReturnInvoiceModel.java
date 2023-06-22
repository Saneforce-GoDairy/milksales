package com.saneforce.milksales.SFA_Model_Class;

public class SalesReturnInvoiceModel {
    String invoice, date, value;

    public SalesReturnInvoiceModel(String invoice, String date, String value) {
        this.invoice = invoice;
        this.date = date;
        this.value = value;
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
}
