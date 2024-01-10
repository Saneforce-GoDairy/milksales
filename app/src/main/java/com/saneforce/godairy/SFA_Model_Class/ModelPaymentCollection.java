package com.saneforce.godairy.SFA_Model_Class;

public class ModelPaymentCollection {
    String invoice, invoiceDate;
    double invoiceAmt, invoicePAmt;
    boolean isChecked;

    public ModelPaymentCollection(String invoice, String invoiceDate, double invoiceAmt, double invoicePAmt, boolean isChecked) {
        this.invoice = invoice;
        this.invoiceDate = invoiceDate;
        this.invoiceAmt = invoiceAmt;
        this.invoicePAmt = invoicePAmt;
        this.isChecked = isChecked;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getInvoiceAmt() {
        return invoiceAmt;
    }

    public void setInvoiceAmt(double invoiceAmt) {
        this.invoiceAmt = invoiceAmt;
    }

    public double getInvoicePAmt() {
        return invoicePAmt;
    }

    public void setInvoicePAmt(double invoicePAmt) {
        this.invoicePAmt = invoicePAmt;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
