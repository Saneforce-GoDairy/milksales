package com.milksales.godairy.SFA_Model_Class;

public class ComplementaryInvoiceHistoryModel {
    String invoice, type, dateTime, value;

    public ComplementaryInvoiceHistoryModel(String invoice, String type, String dateTime, String value) {
        this.invoice = invoice;
        this.type = type;
        this.dateTime = dateTime;
        this.value = value;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
