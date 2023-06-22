package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductUnitModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Product_Code")
    @Expose
    private String productCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ConQty")
    @Expose
    private Integer conQty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConQty() {
        return conQty;
    }

    public void setConQty(Integer conQty) {
        this.conQty = conQty;
    }

}
