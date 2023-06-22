package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Product_Cat_Code")
    @Expose
    private Integer productCatCode;
    @SerializedName("row_num")
    @Expose
    private String rowNum;
    @SerializedName("Product_Sale_Unit")
    @Expose
    private String productSaleUnit;
    @SerializedName("product_unit")
    @Expose
    private String productUnit;
    @SerializedName("Unit_code")
    @Expose
    private String unitCode;
    @SerializedName("Default_UOMQty")
    @Expose
    private Integer defaultUOMQty;
    @SerializedName("Default_UOM")
    @Expose
    private Integer defaultUOM;
    private int mQuantity;


    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }


    public void addToQuantity() {
        this.mQuantity += 1;
    }

    public void removeFromQuantity() {
        if (this.mQuantity > 0) {
            this.mQuantity -= 1;
        }
    }

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }


    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getProductSaleUnit() {
        return productSaleUnit;
    }

    public void setProductSaleUnit(String productSaleUnit) {
        this.productSaleUnit = productSaleUnit;
    }

    public Integer getDefaultUOMQty() {
        return defaultUOMQty;
    }

    public void setDefaultUOMQty(Integer defaultUOMQty) {
        this.defaultUOMQty = defaultUOMQty;
    }

    public Integer getDefaultUOM() {
        return defaultUOM;
    }

    public void setDefaultUOM(Integer defaultUOM) {
        this.defaultUOM = defaultUOM;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductCatCode() {
        return productCatCode;
    }

    public void setProductCatCode(Integer productCatCode) {
        this.productCatCode = productCatCode;
    }
}
