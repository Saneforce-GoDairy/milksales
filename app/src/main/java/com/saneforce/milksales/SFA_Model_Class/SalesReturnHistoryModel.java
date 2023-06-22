package com.saneforce.milksales.SFA_Model_Class;

public class SalesReturnHistoryModel {
    String returnID, returnDate;
    double returnTotal;

    public SalesReturnHistoryModel(String returnID, String returnDate, double returnTotal) {
        this.returnID = returnID;
        this.returnDate = returnDate;
        this.returnTotal = returnTotal;
    }

    public String getReturnID() {
        return returnID;
    }

    public void setReturnID(String returnID) {
        this.returnID = returnID;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getReturnTotal() {
        return returnTotal;
    }

    public void setReturnTotal(double returnTotal) {
        this.returnTotal = returnTotal;
    }
}
