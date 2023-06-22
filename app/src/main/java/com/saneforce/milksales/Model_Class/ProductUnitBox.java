package com.saneforce.milksales.Model_Class;

public class ProductUnitBox {
    String productcode;
    String productname;
    Integer productqty;

    public ProductUnitBox(String productcode, String productname, Integer productqty) {
        this.productcode = productcode;
        this.productname = productname;
        this.productqty = productqty;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Integer getProductqty() {
        return productqty;
    }

    public void setProductqty(Integer productqty) {
        this.productqty = productqty;
    }
}
