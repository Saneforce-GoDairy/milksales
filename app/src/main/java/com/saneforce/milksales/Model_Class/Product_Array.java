package com.saneforce.milksales.Model_Class;

public class Product_Array {
    String productcode;
    String productname;
    Integer productqty;
    Integer sampleqty;
    Integer productRate;
    String catImage;
    String catName;
    String ProductUnit;

    public Product_Array(String productcode, String productname, Integer productqty, Integer sampleqty, Integer productRate, String catImage, String catName, String ProductUnit) {
        this.productcode = productcode;
        this.productname = productname;
        this.productqty = productqty;
        this.sampleqty = sampleqty;
        this.productRate = productRate;
        this.catImage = catImage;
        this.catName = catName;
        this.ProductUnit=ProductUnit;

    }

    public String getProductUnit() {
        return ProductUnit;
    }

    public void setProductUnit(String productUnit) {
        ProductUnit = productUnit;
    }

    public Product_Array(String productcode, String productname, Integer productqty, Integer sampleqty, Integer productRate) {
        this.productcode = productcode;
        this.productname = productname;
        this.productqty = productqty;
        this.sampleqty = sampleqty;
        this.productRate = productRate;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public Integer getProductRate() {
        return productRate;
    }

    public void setProductRate(Integer productRate) {
        this.productRate = productRate;
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

    public Integer getSampleqty() {
        return sampleqty;
    }

    public void setSampleqty(Integer sampleqty) {
        this.sampleqty = sampleqty;
    }
}
