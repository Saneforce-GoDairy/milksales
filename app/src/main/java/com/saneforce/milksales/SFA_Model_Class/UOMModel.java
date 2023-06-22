package com.saneforce.milksales.SFA_Model_Class;

public class UOMModel {
    String UOM_Nm;
    double CnvQty;

    public UOMModel(String UOM_Nm, double cnvQty) {
        this.UOM_Nm = UOM_Nm;
        CnvQty = cnvQty;
    }

    public String getUOM_Nm() {
        return UOM_Nm;
    }

    public double getCnvQty() {
        return CnvQty;
    }
}
