package com.saneforce.godairy.SFA_Model_Class;

public class ModelApproveOutlets {
    String customerName, customerID, customerMobile, customerAddress, listedDrCode;

    public ModelApproveOutlets(String customerName, String customerID, String customerMobile, String customerAddress, String listedDrCode) {
        this.customerName = customerName;
        this.customerID = customerID;
        this.customerMobile = customerMobile;
        this.customerAddress = customerAddress;
        this.listedDrCode = listedDrCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getListedDrCode() {
        return listedDrCode;
    }

    public void setListedDrCode(String listedDrCode) {
        this.listedDrCode = listedDrCode;
    }
}
