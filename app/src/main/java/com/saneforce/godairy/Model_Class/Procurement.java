package com.saneforce.godairy.Model_Class;

public class Procurement {
    private String stateName, stateCode, DistName, DistCode;
    private String selectionName, selectionCode;

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setDistCode(String distCode) {
        DistCode = distCode;
    }

    public void setDistName(String distName) {
        DistName = distName;
    }

    public String getDistCode() {
        return DistCode;
    }

    public String getDistName() {
        return DistName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    public void setSelectionCode(String selectionCode) {
        this.selectionCode = selectionCode;
    }

    public String getSelectionName() {
        return selectionName;
    }

    public String getSelectionCode() {
        return selectionCode;
    }
}
