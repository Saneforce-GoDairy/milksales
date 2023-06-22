package com.saneforce.milksales.SFA_Model_Class;

public class CancelOrderModel {
    int id;
    String remark;

    public CancelOrderModel(int id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
