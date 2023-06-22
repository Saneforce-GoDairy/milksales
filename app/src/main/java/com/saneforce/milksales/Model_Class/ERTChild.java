package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ERTChild {


    @SerializedName("sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("SF_Mobile")
    @Expose
    private String sFMobile;
    @SerializedName("sf_desgn")
    @Expose
    private String sfDesgn;
    @SerializedName("ERTCatNm")
    @Expose
    private String eRTCatNm;
    @SerializedName("cat_code")
    @Expose
    private Integer catCode;
    @SerializedName("Desig")
    @Expose
    private String desig;
    @SerializedName("Profile_Pic")
    @Expose
    private String profilePic;



    public ERTChild(String menuid,String categoryid,String name,String price,String type)
    {
        this.desig = menuid;
        this.sFMobile =categoryid;
        this.name = name;
        this.sFMobile = price;
        this.profilePic = type;
    }


    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSFMobile() {
        return sFMobile;
    }

    public void setSFMobile(String sFMobile) {
        this.sFMobile = sFMobile;
    }

    public String getSfDesgn() {
        return sfDesgn;
    }

    public void setSfDesgn(String sfDesgn) {
        this.sfDesgn = sfDesgn;
    }

    public String getERTCatNm() {
        return eRTCatNm;
    }

    public void setERTCatNm(String eRTCatNm) {
        this.eRTCatNm = eRTCatNm;
    }

    public Integer getCatCode() {
        return catCode;
    }

    public void setCatCode(Integer catCode) {
        this.catCode = catCode;
    }

    public String getDesig() {
        return desig;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
