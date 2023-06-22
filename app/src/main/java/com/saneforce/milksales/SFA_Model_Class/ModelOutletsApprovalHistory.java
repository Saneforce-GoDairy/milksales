package com.saneforce.milksales.SFA_Model_Class;

public class ModelOutletsApprovalHistory {
    String Status, Name, Code, Mobile, Address, ApprovedBy, ModifiedOn, Remarks;

    public ModelOutletsApprovalHistory(String status, String name, String code, String mobile, String address, String approvedBy, String modifiedOn, String remarks) {
        Status = status;
        Name = name;
        Code = code;
        Mobile = mobile;
        Address = address;
        ApprovedBy = approvedBy;
        ModifiedOn = modifiedOn;
        Remarks = remarks;
    }

    public String getStatus() {
        return Status;
    }

    public String getName() {
        return Name;
    }

    public String getCode() {
        return Code;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getAddress() {
        return Address;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public String getRemarks() {
        return Remarks;
    }
}
