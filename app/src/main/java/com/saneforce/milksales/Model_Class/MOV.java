
package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MOV {

    @SerializedName("MorderSum")
    @Expose
    private Integer morderSum;

    public Integer getMorderSum() {
        return morderSum;
    }

    public void setMorderSum(Integer morderSum) {
        this.morderSum = morderSum;
    }

}
