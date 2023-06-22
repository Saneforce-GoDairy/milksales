package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ERTParent {
    @SerializedName("data")
    @Expose
    private List<ERTChild> data = null;

    private String subCategoryName;

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public List<ERTChild> getData() {
        return data;
    }


    public ERTParent(String subCategoryName, List<ERTChild> menus) {
        this.subCategoryName = subCategoryName;
        this.data = menus;
    }

    public void setData(List<ERTChild> data) {
        this.data = data;
    }
}
