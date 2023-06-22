package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {
    @SerializedName("Product_Code")
    @Expose
    private String productCode;
    @SerializedName("Product_Name")
    @Expose
    private String productName;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("value")
    @Expose
    private Integer value;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
