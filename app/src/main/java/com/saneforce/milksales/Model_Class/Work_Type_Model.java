package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Work_Type_Model {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ETabs")
    @Expose
    private String eTabs;
    @SerializedName("FWFlg")
    @Expose
    private String fWFlg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getETabs() {
        return eTabs;
    }

    public void setETabs(String eTabs) {
        this.eTabs = eTabs;
    }

    public String getFWFlg() {
        return fWFlg;
    }

    public void setFWFlg(String fWFlg) {
        this.fWFlg = fWFlg;
    }


}
