package com.saneforce.milksales.Model_Class;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("Qry")
    @Expose
    private String qry;
    @SerializedName("BlockMsg")
    @Expose
    private String blockMsg;

    @SerializedName("CInData")
    @Expose
    private JsonArray CInData;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getQry() {
        return qry;
    }

    public void setQry(String qry) {
        this.qry = qry;
    }

    public String getBlockMsg() {
        return blockMsg;
    }

    public void setBlockMsg(String blockMsg) {
        this.blockMsg = blockMsg;
    }
    public JsonArray getCInData() {
        return CInData;
    }

    public void setCInData(JsonArray CInData) {
        this.CInData = CInData;
    }
}
