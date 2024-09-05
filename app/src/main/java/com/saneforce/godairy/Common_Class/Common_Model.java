package com.saneforce.godairy.Common_Class;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class Common_Model {
    private String CusSubGrpErp;
    private String Latlong;
    private String catName, catId, subCatName, subCatId;
    private int icon;
    private String DivERP;
    private String ERP_Code;
    private String QPS_Code;
    private String QPS_Name;
    private int Total_Ltrs;
    private int Per_Day_Ltrs;
    private int CnvQty;

    private String Days_Period;
    private String retGst;
    private String disFssai, Out_stand;
    private String disGst;

    public String getDisGst() {
        return disGst;
    }

    public void setDisGst(String disGst) {
        this.disGst = disGst;
    }

    public String getRetGst() {
        return retGst;
    }

    public void setRetGst(String retGst) {
        this.retGst = retGst;
    }

    public String getOut_stand() {
        return Out_stand;
    }

    public void setOut_stand(String out_stand) {
        Out_stand = out_stand;
    }

    public String getDisFssai() {
        return disFssai;
    }

    public void setDisFssai(String disFssai) {
        this.disFssai = disFssai;
    }

    public int getTotal_Ltrs() {
        return Total_Ltrs;
    }

    public void setTotal_Ltrs(int total_Ltrs) {
        Total_Ltrs = total_Ltrs;
    }

    public int getPer_Day_Ltrs() {
        return Per_Day_Ltrs;
    }

    public void setPer_Day_Ltrs(int per_Day_Ltrs) {
        Per_Day_Ltrs = per_Day_Ltrs;
    }

    public String getDays_Period() {
        return Days_Period;
    }

    public void setDays_Period(String days_Period) {
        Days_Period = days_Period;
    }

    private String name;
    private String id;
    private String flag;

    private JsonObject jsonObject;
    private JSONObject JSONobject;

    private String address;
    private String phone;

    String checkouttime;
    boolean ExpNeed;
    boolean isSelected;
    private Integer Pho;
    private String cont;
    private Integer MaxDays;


    public Common_Model(String name, String id, String flag, String address, String phone) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.phone = phone;

    }

    public Common_Model(String name, String id, String flag, String address, Integer phone) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.Pho = phone;
    }

    public Common_Model(String name, String id, String flag, String address, String phone, String cont) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.phone = phone;
        this.cont = cont;

    }

    public String getLatlong() {
        return Latlong;
    }

    public void setLatlong(String latlong) {
        Latlong = latlong;
    }

    public Common_Model(String name, String id, String flag, String address, String phone, String cont, String DivERP,String disGst,String disFssai, String Latlong, String CusSubGrpErp) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.phone = phone;
        this.cont = cont;
        this.DivERP = DivERP;
        this.Latlong = Latlong;
        this.CusSubGrpErp = CusSubGrpErp;
        this.disGst=disGst;
        this.disFssai=disFssai;
    }

    public Common_Model(String name, String id, String flag, String address, String phone, String cont, String DivERP,String disGst,String disFssai, String Latlong, String CusSubGrpErp, String Out_stand) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.phone = phone;
        this.cont = cont;
        this.DivERP = DivERP;
        this.Latlong = Latlong;
        this.CusSubGrpErp = CusSubGrpErp;
        this.disGst=disGst;
        this.disFssai=disFssai;
        this.Out_stand = Out_stand;
    }

    public Common_Model(String id, String name, String flag, String checkouttime, Boolean ExpNeed) {
        this.id = id;
        this.name = name;
        this.flag = flag;
        this.checkouttime = checkouttime;
        this.ExpNeed = ExpNeed;
    }

    public Common_Model(String id, String name, JsonObject jsonObject) {
        this.id = id;
        this.name = name;
        this.jsonObject = jsonObject;
    }

    public Common_Model(String id, String name, JSONObject jsonObject) {
        this.id = id;
        this.name = name;
        this.JSONobject = jsonObject;
    }

    public Common_Model(String id, String name, JSONObject jsonObject, Integer position) {
        this.id = id;
        this.name = name;
        this.JSONobject = jsonObject;
        this.Pho = position;
    }

    public Common_Model(String id, String name, String flag, String checkouttime) {
        this.id = id;
        this.name = name;
        this.flag = flag;
        this.checkouttime = checkouttime;
    }



    public Common_Model(String name, String id, boolean isSelected) {
        this.name = name;
        this.id = id;
        this.isSelected = isSelected;
    }

    public Common_Model(String name, String id, boolean isSelected, String catName, String catId, String subCatName, String subCatId) {
        this.name = name;
        this.id = id;
        this.isSelected = isSelected;
        this.catName = catName;
        this.catId = catId;
        this.subCatName = subCatName;
        this.subCatId = subCatId;
    }

    public Common_Model(String name, String id, int CnvQty) {
        this.name = name;
        this.id = id;
        this.CnvQty = CnvQty;
    }

    public Common_Model(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Common_Model(String id, String name, String flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }


    public Common_Model(String name, int Total_Ltrs, String QPS_Name, String QPS_Code) {
        this.name = name;
        this.QPS_Name = QPS_Name;
        this.Total_Ltrs = Total_Ltrs;
        this.QPS_Code = QPS_Code;
    }

    public Common_Model(String id, String name, String flag, Integer MaxDays) {
        this.id = id;
        this.name = name;
        this.flag = flag;
        this.MaxDays = MaxDays;
    }

    public Common_Model(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Common_Model(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getFlag() {
        return flag;
    }


    public Integer getMaxDays() {
        return MaxDays;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public boolean getExpNeed() {
        return ExpNeed;
    }

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

    public Integer getPho() {
        return Pho;
    }

    public void setPho(Integer pho) {
        Pho = pho;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public JSONObject getJSONObject() {
        return JSONobject;
    }

    public String getQPS_Name() {
        return QPS_Name;
    }

    public void setQPS_Name(String QPS_Name) {
        this.QPS_Name = QPS_Name;
    }

    public String getQPS_Code() {
        return QPS_Code;
    }

    public void setQPS_Code(String QPS_Code) {
        this.QPS_Code = QPS_Code;
    }

    public int getCnvQty() {
        return CnvQty;
    }

    public void setCnvQty(int cnvQty) {
        CnvQty = cnvQty;
    }

    public String getDivERP() {
        return DivERP;
    }

    public void setDivERP(String divERP) {
        DivERP = divERP;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getCusSubGrpErp() {
        return CusSubGrpErp;
    }

    public void setCusSubGrpErp(String cusSubGrpErp) {
        CusSubGrpErp = cusSubGrpErp;
    }
}
