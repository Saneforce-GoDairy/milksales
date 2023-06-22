package com.saneforce.milksales.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tp_Approval_Model implements Serializable {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("SF_Code")
    @Expose
    private String sFCode;
    @SerializedName("worktype_code")
    @Expose
    private Double worktypeCode;
    @SerializedName("worktype_name")
    @Expose
    private String worktypeName;
    @SerializedName("RouteCode")
    @Expose
    private String routeCode;
    @SerializedName("RouteName")
    @Expose
    private String routeName;
    @SerializedName("Worked_with_Code")
    @Expose
    private String workedWithCode;
    @SerializedName("Worked_with_Name")
    @Expose
    private String workedWithName;
    @SerializedName("Confirmed")
    @Expose
    private Double confirmed;
    @SerializedName("HQ_Code")
    @Expose
    private String hQCode;
    @SerializedName("HQ_Name")
    @Expose
    private String hQName;
    @SerializedName("JointWork_Name")
    @Expose
    private String jointWorkName;
    @SerializedName("JointworkCode")
    @Expose
    private String jointworkCode;
    @SerializedName("Tmonth")
    @Expose
    private Double tmonth;
    @SerializedName("Tyear")
    @Expose
    private Double tyear;
    @SerializedName("submit_status")
    @Expose
    private Double submitStatus;
    @SerializedName("dayofplan")
    @Expose
    private Double dayofplan;
    @SerializedName("Worktype_Flag")
    @Expose
    private String worktypeFlag;
    @SerializedName("TourHQ_Name")
    @Expose
    private String tourHQName;
    @SerializedName("Start_date")
    @Expose
    private String startDate;
    @SerializedName("DeptType")
    @Expose
    private String deptType;
    @SerializedName("FieldForceName")
    @Expose
    private String fieldForceName;
    @SerializedName("SF_Mobile")
    @Expose
    private String sFMobile;
    @SerializedName("Reporting_To_SF")
    @Expose
    private String reportingToSF;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("HQ")
    @Expose
    private String hQ;
    @SerializedName("EmpCode")
    @Expose
    private String empCode;
    @SerializedName("Hq_Id")
    @Expose
    private String hqId;
    @SerializedName("Chilling_Id")
    @Expose
    private String chillingId;
    @SerializedName("CCentre_Name")
    @Expose
    private String cCentreName;
    @SerializedName("SHift_Type_Id")
    @Expose
    private String sHiftTypeId;
    @SerializedName("Typename")
    @Expose
    private String typename;
    @SerializedName("Fromdate")
    @Expose
    private String fromdate;
    @SerializedName("Todate")
    @Expose
    private String todate;



    @SerializedName("MOT")
    @Expose
    private String MOT;
    @SerializedName("DA_Type")
    @Expose
    private String DA_Type;
    @SerializedName("Driver_Allow")
    @Expose
    private String Driver_Allow;
    @SerializedName("From_Place")
    @Expose
    private String From_Place;
    @SerializedName("To_Place")
    @Expose
    private String To_Place;


    public String getMOT() {
        return MOT;
    }

    public void setMOT(String MOT) {
        this.MOT = MOT;
    }

    public String getDA_Type() {
        return DA_Type;
    }

    public void setDA_Type(String DA_Type) {
        this.DA_Type = DA_Type;
    }

    public String getDriver_Allow() {
        return Driver_Allow;
    }

    public void setDriver_Allow(String driver_Allow) {
        Driver_Allow = driver_Allow;
    }

    public String getFrom_Place() {
        return From_Place;
    }

    public void setFrom_Place(String from_Place) {
        From_Place = from_Place;
    }

    public String getTo_Place() {
        return To_Place;
    }

    public void setTo_Place(String to_Place) {
        To_Place = to_Place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSFCode() {
        return sFCode;
    }

    public void setSFCode(String sFCode) {
        this.sFCode = sFCode;
    }

    public Double getWorktypeCode() {
        return worktypeCode;
    }

    public void setWorktypeCode(Double worktypeCode) {
        this.worktypeCode = worktypeCode;
    }

    public String getWorktypeName() {
        return worktypeName;
    }

    public void setWorktypeName(String worktypeName) {
        this.worktypeName = worktypeName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getWorkedWithCode() {
        return workedWithCode;
    }

    public void setWorkedWithCode(String workedWithCode) {
        this.workedWithCode = workedWithCode;
    }

    public String getWorkedWithName() {
        return workedWithName;
    }

    public void setWorkedWithName(String workedWithName) {
        this.workedWithName = workedWithName;
    }

    public Double getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Double confirmed) {
        this.confirmed = confirmed;
    }

    public String getHQCode() {
        return hQCode;
    }

    public void setHQCode(String hQCode) {
        this.hQCode = hQCode;
    }

    public String getHQName() {
        return hQName;
    }

    public void setHQName(String hQName) {
        this.hQName = hQName;
    }

    public String getJointWorkName() {
        return jointWorkName;
    }

    public void setJointWorkName(String jointWorkName) {
        this.jointWorkName = jointWorkName;
    }

    public String getJointworkCode() {
        return jointworkCode;
    }

    public void setJointworkCode(String jointworkCode) {
        this.jointworkCode = jointworkCode;
    }

    public Double getTmonth() {
        return tmonth;
    }

    public void setTmonth(Double tmonth) {
        this.tmonth = tmonth;
    }

    public Double getTyear() {
        return tyear;
    }

    public void setTyear(Double tyear) {
        this.tyear = tyear;
    }

    public Double getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(Double submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Double getDayofplan() {
        return dayofplan;
    }

    public void setDayofplan(Double dayofplan) {
        this.dayofplan = dayofplan;
    }

    public String getWorktypeFlag() {
        return worktypeFlag;
    }

    public void setWorktypeFlag(String worktypeFlag) {
        this.worktypeFlag = worktypeFlag;
    }

    public String getTourHQName() {
        return tourHQName;
    }

    public void setTourHQName(String tourHQName) {
        this.tourHQName = tourHQName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getFieldForceName() {
        return fieldForceName;
    }

    public void setFieldForceName(String fieldForceName) {
        this.fieldForceName = fieldForceName;
    }

    public String getSFMobile() {
        return sFMobile;
    }

    public void setSFMobile(String sFMobile) {
        this.sFMobile = sFMobile;
    }

    public String getReportingToSF() {
        return reportingToSF;
    }

    public void setReportingToSF(String reportingToSF) {
        this.reportingToSF = reportingToSF;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getHQ() {
        return hQ;
    }

    public void setHQ(String hQ) {
        this.hQ = hQ;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getHqId() {
        return hqId;
    }

    public void setHqId(String hqId) {
        this.hqId = hqId;
    }

    public String getChillingId() {
        return chillingId;
    }

    public void setChillingId(String chillingId) {
        this.chillingId = chillingId;
    }

    public String getCCentreName() {
        return cCentreName;
    }

    public void setCCentreName(String cCentreName) {
        this.cCentreName = cCentreName;
    }

    public String getSHiftTypeId() {
        return sHiftTypeId;
    }

    public void setSHiftTypeId(String sHiftTypeId) {
        this.sHiftTypeId = sHiftTypeId;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

}