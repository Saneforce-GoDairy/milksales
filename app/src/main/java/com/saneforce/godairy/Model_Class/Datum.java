package com.saneforce.godairy.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {


    @SerializedName("DSM_code")
    @Expose
    private String DSM_Code;
    @SerializedName("DSM_name")
    @Expose
    private String DSM_Name;
    @SerializedName("Town_Code")
    @Expose
    private String Town_Code;
    @SerializedName("Desig_type")
    @Expose
    private String Desig_Type;
    @SerializedName("Salestype")
    @Expose
    private String SalesType;
    @SerializedName("DSM_Email")
    @Expose
    private String DSM_Email;
    @SerializedName("DSM_Phone_no")
    @Expose
    private String DSM_Phone_No;
    @SerializedName("Aadhar_Number")
    @Expose
    private String DSM_Aadhar_Number;
    @SerializedName("BankAccount_Number")
    @Expose
    private String DSM_BankAccount_Number;
    @SerializedName("Stockist_Code")
    @Expose
    private String Stockist_Code;


    @SerializedName("Sf_UserName")
    @Expose
    private String sfUserName;
    @SerializedName("sf_emp_id")
    @Expose
    private String sfEmpId;
    @SerializedName("ERP_Code")
    @Expose
    private String ERP_Code;

    @SerializedName("CusSubGrpErp")
    @Expose
    private String CusSubGrpErp;

    public Datum(String tm) {
        Tm = tm;
    }

    public String getERP_Code() {
        return ERP_Code;
    }

    public void setERP_Code(String ERP_Code) {
        this.ERP_Code = ERP_Code;
    }

    public String getStockist_Address() {
        return Stockist_Address;
    }

    public void setStockist_Address(String stockist_Address) {
        Stockist_Address = stockist_Address;
    }

    @SerializedName("Stockist_Address")
    @Expose
    private String Stockist_Address;
    @SerializedName("DeptName")
    @Expose
    private String deptName;
    @SerializedName("sf_Designation_Short_Name")
    @Expose
    private String sfDesignationShortName;
    @SerializedName("SF_Status")
    @Expose
    private Integer sFStatus;
    @SerializedName("Sf_Name")
    @Expose
    private String sfName;
    @SerializedName("SFMobile")
    @Expose
    private String sfMobile;
    @SerializedName("Sf_Password")
    @Expose
    private String sfPassword;
    @SerializedName("Division_Code")
    @Expose
    private String divisionCode;
    @SerializedName("DisRad")
    @Expose
    private Double disRad;
    @SerializedName("Sf_code")
    @Expose
    private String sfCode;

    @SerializedName("GSTN")
    @Expose
    private String disGSTN;

    @SerializedName("Fssai_No")
    @Expose
    private String disFSSAI;

    @SerializedName("gst")
    @Expose
    private String retGST;

    @SerializedName("SF_Code")
    @Expose
    private String distCode;

    @SerializedName("StockCheck")
    @Expose
    private String StockCheck;

    @SerializedName("CutoffTime")
    @Expose
    private String CutoffTime;

    @SerializedName("SFJoinDate")
    @Expose
    private String SFJoinDate;

    @SerializedName("SFJoinMxDate")
    @Expose
    private String SFJoinMxDate;


    @SerializedName("SFCutoff")
    @Expose
    private String RSMCutOffTime;

    @SerializedName("Tm")
    @Expose
    private String Tm;

    @SerializedName("SlotTime")
    @Expose
    private List<Datum> SlotTime;

    @SerializedName("Stockist_Name")
    @Expose
    private String Stockist_Name;
    @SerializedName("Stockist_Mobile")
    @Expose
    private String Stockist_Mobile;
    @SerializedName("CheckCount")
    @Expose
    private Integer checkCount;
    @SerializedName("loginType")
    @Expose
    private String loginType;
    @SerializedName("Geo_Fencing")
    @Expose
    private Integer geoFencing;
    @SerializedName("SFFType")
    @Expose
    private Integer sFFType;
    @SerializedName("FFType")
    @Expose
    private String ffType;
    @SerializedName("SFDept")
    @Expose
    private String sFDept;
    @SerializedName("DeptType")
    @Expose
    private String deptType;
    @SerializedName("OTFlg")
    @Expose
    private Integer oTFlg;

    @SerializedName("FlightAllowed")
    @Expose
    private Integer FlightAllowed;

    @SerializedName("radius")
    @Expose
    private Double radius;

    public Integer getFreezer_Mandatory() {
        return Freezer_Mandatory;
    }

    public void setFreezer_Mandatory(Integer freezer_Mandatory) {
        Freezer_Mandatory = freezer_Mandatory;
    }

    @SerializedName("SalesReturnImg")

    @Expose
    private Integer Freezer_Mandatory;
    @SerializedName("Freezer_Mandatory")

    @Expose
    private Integer SalesReturnImg;
    @SerializedName("HOLocation")
    @Expose
    private String hOLocation;
    @SerializedName("HQ_Name")
    @Expose
    private String sFHQ;
    @SerializedName("HQID")
    @Expose
    private String sHQID;
    @SerializedName("HQCode")
    @Expose
    private String sHQCode;
    @SerializedName("THrsPerm")
    @Expose
    private int THrsPerm;
    @SerializedName("Profile")
    @Expose
    private String mProfile;
    @SerializedName("ProfPath")
    @Expose
    private String mProfPath;

    @SerializedName("RptCode")
    @Expose
    private String SfRptCode;
    @SerializedName("RptName")
    @Expose
    private String SfRptName;
    @SerializedName("State_Code")
    @Expose
    private String State_Code;

    @SerializedName("DivERP")
    @Expose
    private String DivERP;

    @SerializedName("checkRadius")
    @Expose
    private Integer checkRadius;
//
//    @SerializedName("SFJoinDate")
//    @Expose
//    private String SFJoinDate;
//
//    @SerializedName("SFJoinMxDate")
//    @Expose
//    private String SFJoinMxDate;

    public String getSfUserName() {
        return sfUserName;
    }

    public void setSfUserName(String sfUserName) {
        this.sfUserName = sfUserName;
    }

    public String getSfEmpId() {
        return sfEmpId;
    }

    public void setSfEmpId(String sfEmpId) {
        this.sfEmpId = sfEmpId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSfDesignationShortName() {
        return sfDesignationShortName;
    }

    public void setSfDesignationShortName(String sfDesignationShortName) {
        this.sfDesignationShortName = sfDesignationShortName;
    }

    public String getSFJoinDate() {
        return SFJoinDate;
    }

    public void setSFJoinDate(String SFJoinDate) {
        this.SFJoinDate = SFJoinDate;
    }
    public String getSFJoinMxDate() {
        return SFJoinMxDate;
    }

    public void setSFJoinMxDate(String SFJoinMxDate) {
        this.SFJoinMxDate = SFJoinMxDate;
    }

    public String getSfRptCode() {
        return SfRptCode;
    }

    public String getSfRptName() {
        return SfRptName;
    }

    public Integer getSFStatus() {
        return sFStatus;
    }

    public void setSFStatus(Integer sFStatus) {
        this.sFStatus = sFStatus;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfMobile(String sfMobile) {
        this.sfMobile = sfMobile;
    }

    public String getSFMobile(){
        return sfMobile;
    }
    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public String getSfPassword() {
        return sfPassword;
    }

    public void setSfPassword(String sfPassword) {
        this.sfPassword = sfPassword;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public Double getDisRad() {
        return disRad;
    }

    public void setDisRad(Double disRad) {
        this.disRad = disRad;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public Integer getGeoFencing() {
        return geoFencing;
    }

    public void setGeoFencing(Integer geoFencing) {
        this.geoFencing = geoFencing;
    }
    public String getFFType() {
        return ffType;
    }
    public void setFFType(String sffType){
        this.ffType=sffType;
    }

    public Integer getSFFType() {
        return sFFType;
    }

    public void setSFFType(Integer sFFType) {
        this.sFFType = sFFType;
    }

    public String getSFDept() {
        return sFDept;
    }

    public void setSFDept(String sFDept) {
        this.sFDept = sFDept;
    }

    public String getHQID() {
        return sHQID;
    }

    public void setHQID(String sFHQ) {
        this.sHQID = sHQID;
    }

    public String getHQCode() {
        return sHQCode;
    }

    public void setHQCode(String sFHQ) {
        this.sHQCode = sHQCode;
    }

    public String getsFHQ() {
        return sFHQ;
    }

    public void setsFHQ(String sFHQ) {
        this.sFHQ = sFHQ;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public Integer getFlightAllowed() {
        return FlightAllowed;
    }

    public void setFlightAllowed(Integer flightAllowed) {
        FlightAllowed = flightAllowed;
    }
    public Integer getOTFlg() {
        return oTFlg;
    }

    public void setOTFlg(Integer oTFlg) {
        this.oTFlg = oTFlg;
    }

    public String getHOLocation() {
        return hOLocation;
    }

    public String getProfile() {
        return mProfile;
    }

    public String getProfPath() {
        return mProfPath;
    }

    public void setHOLocation(String hOLocation) {
        this.hOLocation = hOLocation;
    }

    public int getTHrsPerm() {
        return THrsPerm;
    }

    public void setTHrsPerm(int mTHrsPerm) {
        this.THrsPerm = mTHrsPerm;
    }

    public String getStockCheck() {
        return StockCheck;
    }

    public void setStockCheck(String stockCheck) {
        StockCheck = stockCheck;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDistCode() {
        return distCode;
    }

    public String getDisGSTN() {
        return disGSTN;
    }

    public String getDisFSSAI() {
        return disFSSAI;
    }
    public String getRetGST() {
        return retGST;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getStockist_Mobile() {
        return Stockist_Mobile;
    }

    public void setStockist_Mobile(String stockist_Mobile) {
        Stockist_Mobile = stockist_Mobile;
    }

    public String getStockist_Name() {
        return Stockist_Name;
    }

    public void setStockist_Name(String stockist_Name) {
        Stockist_Name = stockist_Name;
    }

    public String getState_Code() {
        return State_Code;
    }

    public void setState_Code(String state_Code) {
        State_Code = state_Code;
    }

    public String getCutoffTime() {
        return CutoffTime;
    }

    public String getRSMCutoffTime() {
        return RSMCutOffTime;
    }


    public void setCutoffTime(String cutoffTime) {
        CutoffTime = cutoffTime;
    }

    public void setRSMCutoffTime(String cutoffTime) {
        RSMCutOffTime = cutoffTime;
    }

    public List<Datum> getSlotTime() {
        return SlotTime;
    }

    public void setSlotTime(List<Datum> slotTime) {
        SlotTime = slotTime;
    }

    public String getTm() {
        return Tm;
    }

    public void setTm(String tm) {
        Tm = tm;
    }

    public String getDivERP() {
        return DivERP;
    }

    public void setDivERP(String divERP) {
        DivERP = divERP;
    }

    public Integer getSalesReturnImg() {
        return SalesReturnImg;
    }

    public void setSalesReturnImg(Integer salesReturnImg) {
        SalesReturnImg = salesReturnImg;
    }

    public String getCusSubGrpErp() {
        return CusSubGrpErp;
    }

    public void setCusSubGrpErp(String cusSubGrpErp) {
        CusSubGrpErp = cusSubGrpErp;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public int getCheckRadius() {
        return checkRadius;
    }


    public void setCheckRadius(Integer checkRadius) {
        this.checkRadius = checkRadius;
    }

    public String getDSM_Code() {
        return DSM_Code;
    }

    public void setDSM_Code(String DSM_Code) {
        this.DSM_Code = DSM_Code;
    }

    public String getDSM_Name() {
        return DSM_Name;
    }

    public void setDSM_Name(String DSM_Name) {
        this.DSM_Name = DSM_Name;
    }

    public String getTown_Code() {
        return Town_Code;
    }

    public void setTown_Code(String town_Code) {
        Town_Code = town_Code;
    }

    public String getDesig_Type() {
        return Desig_Type;
    }

    public void setDesig_Type(String desig_Type) {
        Desig_Type = desig_Type;
    }

    public String getSalesType() {
        return SalesType;
    }

    public void setSalesType(String salesType) {
        SalesType = salesType;
    }

    public String getDSM_Email() {
        return DSM_Email;
    }

    public void setDSM_Email(String DSM_Email) {
        this.DSM_Email = DSM_Email;
    }

    public String getDSM_Phone_No() {
        return DSM_Phone_No;
    }

    public void setDSM_Phone_No(String DSM_Phone_No) {
        this.DSM_Phone_No = DSM_Phone_No;
    }

    public String getDSM_Aadhar_Number() {
        return DSM_Aadhar_Number;
    }

    public void setDSM_Aadhar_Number(String DSM_Aadhar_Number) {
        this.DSM_Aadhar_Number = DSM_Aadhar_Number;
    }

    public String getDSM_BankAccount_Number() {
        return DSM_BankAccount_Number;
    }

    public void setDSM_BankAccount_Number(String DSM_BankAccount_Number) {
        this.DSM_BankAccount_Number = DSM_BankAccount_Number;
    }

    public String getStockist_Code() {
        return Stockist_Code;
    }

    public void setStockist_Code(String stockist_Code) {
        Stockist_Code = stockist_Code;
    }
}