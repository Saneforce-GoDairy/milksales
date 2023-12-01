package com.saneforce.godairy.Model_Class;

import com.google.gson.annotations.SerializedName;

public class PrimaryNoOrderList {
    @SerializedName("id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
