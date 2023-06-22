package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Sft_STime")
    @Expose
    private String sftSTime;
    @SerializedName("sft_ETime")
    @Expose
    private String sftETime;

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

    public String getSftSTime() {
        return sftSTime;
    }

    public void setSftSTime(String sftSTime) {
        this.sftSTime = sftSTime;
    }

    public String getSftETime() {
        return sftETime;
    }

    public void setSftETime(String sftETime) {
        this.sftETime = sftETime;
    }
}
