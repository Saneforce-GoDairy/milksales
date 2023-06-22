package com.saneforce.milksales.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category_Universe_Modal {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ColorFlag")
    @Expose
    private String ColorFlag;

    @SerializedName("counterValue")
    @Expose
    private String value;

    public Category_Universe_Modal(String id, String name, String divisionCode, String catImage, String sampleQty, String ColorFlag) {
        this.id = id;
        this.name = name;
        this.divisionCode = divisionCode;
        this.catImage = catImage;
        this.sampleQty = sampleQty;
        this.ColorFlag= ColorFlag;
    }


    public Category_Universe_Modal(String id, String name, String value){
        this.name = name;
        this.value = value;
        this.id = id;

    }

    @SerializedName("Division_Code")
    @Expose
    private String divisionCode;
    @SerializedName("Cat_Image")
    @Expose
    private String catImage;

    public void setColorFlag(String colorFlag) {
        ColorFlag = colorFlag;
    }

    public String getColorFlag() {
        return ColorFlag;
    }

    @SerializedName("sampleQty")
    @Expose
    private String sampleQty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getSampleQty() {
        return sampleQty;
    }

    public void setSampleQty(String sampleQty) {
        this.sampleQty = sampleQty;
    }

}
