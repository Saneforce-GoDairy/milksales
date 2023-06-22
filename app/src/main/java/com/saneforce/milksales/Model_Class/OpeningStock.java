package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningStock {


    @SerializedName("cust_Code")
    @Expose
    private String custCode;
    @SerializedName("Product_name")
    @Expose
    private String productName;
    @SerializedName("Product_code")
    @Expose
    private String productCode;
    @SerializedName("Rc")
    @Expose
    private Integer rc;
    @SerializedName("op")
    @Expose
    private Integer op;
    @SerializedName("cl")
    @Expose
    private Integer cl;
    @SerializedName("sale")
    @Expose
    private Integer sale;

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getRc() {
        return rc;
    }

    public void setRc(Integer rc) {
        this.rc = rc;
    }

    public Integer getOp() {
        return op;
    }

    public void setOp(Integer op) {
        this.op = op;
    }

    public Integer getCl() {
        return cl;
    }

    public void setCl(Integer cl) {
        this.cl = cl;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

}
