package com.milksales.godairy.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InshopModel {

    public String getIn_Time() {
        return in_Time;
    }

    public void setIn_Time(String in_Time) {
        this.in_Time = in_Time;
    }

    public String in_Time="";

    @SerializedName("Sl_No")
    @Expose
    private int slno;

    @SerializedName("Sf_Code")
    @Expose
    private String sfcode;

    @SerializedName("Retailer_Code")
    @Expose
    private String retailercode;

    @SerializedName("CIn_Time")
    @Expose
    private String cintime;

    @SerializedName("Entry_Date")
    @Expose
    private String entrydate;

    @SerializedName("C_Flag")
    @Expose
    private int cflag;

    @SerializedName("eKey")
    @Expose
    private String ekey;

    @SerializedName("COut_Time")
    @Expose
    private String couttime;

    @SerializedName("Retailer_Name")
    @Expose
    private String retailername;

    @SerializedName("Sf_Name")
    @Expose
    private String sfname;

    @SerializedName("Entry_Type")
    @Expose
    private String entrytype;

    @SerializedName("Cin_Img_Url")
    @Expose
    private String cinimage;

    @SerializedName("Cout_Img_Url")
    @Expose
    private String coutimage;

    public InshopModel(int sl_no, String sf_code, String retailer_code, String eKey) {
        this.slno=sl_no;
        this.sfcode=sf_code;
        this.retailercode=retailer_code;
        this.ekey=eKey;
    }

    public InshopModel(String date) {
        this.entrydate=date;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getSfcode() {
        return sfcode;
    }

    public void setSfcode(String sfcode) {
        this.sfcode = sfcode;
    }

    public String getRetailercode() {
        return retailercode;
    }

    public void setRetailercode(String retailercode) {
        this.retailercode = retailercode;
    }

    public String getCintime() {
        return cintime;
    }

    public void setCintime(String cintime) {
        this.cintime = cintime;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }

    public int getCflag() {
        return cflag;
    }

    public void setCflag(int cflag) {
        this.cflag = cflag;
    }

    public String getEkey() {
        return ekey;
    }

    public void setEkey(String ekey) {
        this.ekey = ekey;
    }

    public String getCouttime() {
        return couttime;
    }

    public void setCouttime(String couttime) {
        this.couttime = couttime;
    }

    public String getRetailername() {
        return retailername;
    }

    public void setRetailername(String retailername) {
        this.retailername = retailername;
    }

    public String getSfname() {
        return sfname;
    }

    public void setSfname(String sfname) {
        this.sfname = sfname;
    }

    public String getEntrytype() {
        return entrytype;
    }

    public void setEntrytype(String entrytype) {
        this.entrytype = entrytype;
    }

    public String getCinimage() {
        return cinimage;
    }

    public void setCinimage(String cinimage) {
        this.cinimage = cinimage;
    }

    public String getCoutimage() {
        return coutimage;
    }

    public void setCoutimage(String coutimage) {
        this.coutimage = coutimage;
    }








   /* public int sl_No;
    public String sf_Code;
    public String retailer_Code;
    public String eKey;
    public CInTime cIn_Time;
    public EntryDate entry_Date;
    public COutTime cOut_Time;
    public  String cTime;
    public String name="";
    public String cTimeOut;
    public  String time;
    public int c_flag;


    public String in_Time="";
    public  InshopModel(int sl_No,String sf_Code,String retailer_Code,String eKey){
        this.sl_No=sl_No;
        this.sf_Code=sf_Code;
        this.retailer_Code=retailer_Code;
        this.eKey=eKey;

    }

    public int getSl_No() {
        return sl_No;
    }
    public void setSl_No(int sl_No) {
        this.sl_No = sl_No;
    }
    public String getSf_Code() {
        return sf_Code;
    }
    public void setSf_Code(String sf_Code) {
        this.sf_Code = sf_Code;
    }
    public String getRetailer_Code() {
        return retailer_Code;
    }
    public void setRetailer_Code(String retailer_Code) {
        this.retailer_Code = retailer_Code;
    }
    public String geteKey() {
        return eKey;
    }
    public void seteKey(String eKey) {
        this.eKey = eKey;
    }
    public CInTime getcIn_Time() {
        return cIn_Time;
    }

    public void setcIn_Time(CInTime cIn_Time) {
        this.cIn_Time = cIn_Time;
    }



    public EntryDate getEntry_Date() {
        return entry_Date;
    }

    public void setEntry_Date(EntryDate entry_Date) {
        this.entry_Date = entry_Date;
    }



    public COutTime getcOut_Time() {
        return cOut_Time;
    }

    public void setcOut_Time(COutTime cOut_Time) {
        this.cOut_Time = cOut_Time;
    }
    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }
    public int getC_flag() {
        return c_flag;
    }

    public void setC_flag(int c_flag) {
        this.c_flag = c_flag;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getcTimeOut() {
        return cTimeOut;
    }

    public void setcTimeOut(String cTimeOut) {
        this.cTimeOut = cTimeOut;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getIn_Time() {
        return in_Time;
    }

    public void setIn_Time(String in_Time) {
        this.in_Time = in_Time;
    }


    public static class CInTime{
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String date;
        public int timezone_type;
        public String timezone;
        public CInTime(String date){
            this.date=date;
        }
    }
    public static class COutTime{
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
        public String date;
        public int timezone_type;
        public String timezone;
        public COutTime(String date){
            this.date=date;
        }
    }
    public class EntryDate{
        public String date;
        public int timezone_type;
        public String timezone;
    }
*/
}
