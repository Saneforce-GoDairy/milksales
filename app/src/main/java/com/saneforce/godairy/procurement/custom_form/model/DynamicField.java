package com.saneforce.godairy.procurement.custom_form.model;

public class DynamicField {
    private String column,data,tag;
    private int mandatory;
    private int fldGrpId;
    private String fldType;
    private String grpTableName;
    private  String fldGrpName;
    private String imgKey;
    private String fldSrcName;
    private String fldSrcFld;
    private int moduleId;
    private String moduleName;

    public DynamicField(String id, String data, String tag, int mandatory){
        this.column = id;
        this.data = data;
        this.tag = tag;
        this.mandatory = mandatory;
    }
    public DynamicField(String fldSrcName,String fldSrcFld){
        this.fldSrcName=fldSrcName;
        this.fldSrcFld=fldSrcFld;

    }

    public DynamicField(int id,String moduleNm){
        this.moduleId=id;
        this.moduleName=moduleNm;

    }

    public  DynamicField(){}
    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getFldGrpId() {return fldGrpId;}

    public void setFldGrpId(int fldGrpId) {this.fldGrpId = fldGrpId;}

    public String getFldType() {return fldType;}

    public void setFldType(String fldType) {this.fldType = fldType;}

    public String getGrpTableName() {return grpTableName;}

    public void setGrpTableName(String grpTableName) {this.grpTableName = grpTableName;}

    public String getFldGrpName() {return fldGrpName;}

    public void setFldGrpName(String fldGrpName) {this.fldGrpName = fldGrpName;}

    public String getImgKey() {return imgKey;}

    public void setImgKey(String imgKey) {this.imgKey = imgKey;}

    public String getFldSrcName() {return fldSrcName;}

    public void setFldSrcName(String fldSrcName) {this.fldSrcName = fldSrcName;}

    public String getFldSrcFld() {return fldSrcFld;}

    public void setFldSrcFld(String fldSrcFld) {this.fldSrcFld = fldSrcFld;}

    public String getModuleName() {return moduleName;}

    public void setModuleName(String moduleName) {this.moduleName = moduleName;}

    public int getModuleId() {return moduleId;}

    public void setModuleId(int moduleId) {this.moduleId = moduleId;}
}
