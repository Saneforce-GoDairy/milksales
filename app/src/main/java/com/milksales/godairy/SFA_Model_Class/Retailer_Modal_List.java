package com.milksales.godairy.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Retailer_Modal_List {
    public Retailer_Modal_List(String cust_Code, String mnth, double others, double othersVal, double curd, double curdVal, double milk, double milkVal) {
        Cust_Code = cust_Code;
        Mnth = mnth;
        Others = others;
        OthersVal = othersVal;
        Curd = curd;
        CurdVal = curdVal;
        Milk = milk;
        MilkVal = milkVal;
    }


    public String getCust_Code() {
        return Cust_Code;
    }

    public void setCust_Code(String cust_Code) {
        Cust_Code = cust_Code;
    }

    public String getMnth() {
        return Mnth;
    }

    public void setMnth(String mnth) {
        Mnth = mnth;
    }

    public double getOthers() {
        return Others;
    }

    public void setOthers(double others) {
        Others = others;
    }

    public double getMilk() {
        return Milk;
    }

    public void setMilk(double milk) {
        Milk = milk;
    }

    @SerializedName("Cust_Code")
    @Expose
    private String Cust_Code;

    public ArrayList<CateSpecList> getCategoryList() {
        return CategoryList;
    }


    @SerializedName("outlet_type_Details")
    @Expose
    private ArrayList<CateSpecList> CategoryList;

    @SerializedName("deposit_amount")
    @Expose
    private String deposit_amount;
    @SerializedName("expected_sales_value")
    @Expose
    private String expected_sales_value;
    @SerializedName("Freezer_status")
    @Expose
    private String Freezer_status;
    @SerializedName("Freezer_Tag_no")
    @Expose
    private String Freezer_Tag_no;
    @SerializedName("Pan_No")
    @Expose
    private String Pan_No;
    @SerializedName("FssiNo")
    @Expose
    private String FssiNo;
    @SerializedName("freezer_capacity")
    @Expose
    private String freezer_capacity;
    @SerializedName("freezer_required")
    @Expose
    private String freezer_required;
    @SerializedName("freezer_attachments")
    @Expose
    private String freezer_attachments;
    @SerializedName("Freezer_make")
    @Expose
    private String Freezer_make;
    @SerializedName("Mnth")
    @Expose
    private String Mnth;

    @SerializedName("Others")
    @Expose
    private double Others;

    @SerializedName("OthersVal")
    @Expose
    private double OthersVal;

    public String getDoctor_Active_flag() {
        return Doctor_Active_flag;
    }

    @SerializedName("Doctor_Active_flag")
    @Expose
    private String Doctor_Active_flag;

    @SerializedName("Curd")
    @Expose
    private double Curd;


    @SerializedName("CurdVal")
    @Expose
    private double CurdVal;

    @SerializedName("ClosedRmks")
    @Expose
    private String ClosedRemarks;

    @SerializedName("OrderFlg")
    @Expose
    private int OrderFlg;


    @SerializedName("Milk")
    @Expose
    private double Milk;


    @SerializedName("MilkVal")
    @Expose
    private double MilkVal;


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("Type")
    @Expose
    private String Type;

    @SerializedName("DelivType")
    @Expose
    private String DelivType;

    @SerializedName("ERP_Code")
    @Expose
    private String ERP_Code;

    @SerializedName("CustomerCode")
    @Expose
    private String CustomerCode;

    @SerializedName("districtname")
    @Expose
    private String districtname;


    public Retailer_Modal_List getRetailer_modal_list() {
        return retailer_modal_list;
    }

    public void setRetailer_modal_list(Retailer_Modal_List retailer_modal_list) {
        this.retailer_modal_list = retailer_modal_list;
    }

    public double getProductQty() {
        return productQty;
    }

    public void setProductQty(double productQty) {
        this.productQty = productQty;
    }

    public double getProductRate() {
        return productRate;
    }

    public void setProductRate(double productRate) {
        this.productRate = productRate;
    }

    Retailer_Modal_List retailer_modal_list;


    public Retailer_Modal_List(String cust_Code, String mnth, List<Retailer_Modal_List> retailer_modal_lists) {
        this.Cust_Code = cust_Code;
        this.Mnth = mnth;
        this.retailer_modal_list = (Retailer_Modal_List) retailer_modal_lists;
    }


    public Retailer_Modal_List(String productName, List<Retailer_Modal_List> retailer_modal_lists) {
        this.productName = productName;
        this.retailer_modal_list = (Retailer_Modal_List) retailer_modal_lists;

    }


    public Retailer_Modal_List(double productQty, double productRate) {
        this.productQty = productQty;
        this.productRate = productRate;

    }


    public String getProductName() {
        return productName;
    }

    public Retailer_Modal_List(String productName, double productQty, double productRate) {
        this.productName = productName;
        this.productQty = productQty;
        this.productRate = productRate;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @SerializedName("productName")
    @Expose
    private String productName;


    @SerializedName("productQty")
    @Expose
    private double productQty;


    @SerializedName("productRate")
    @Expose
    private double productRate;


    public String getStatusname() {
        return Statusname;
    }

    public void setStatusname(String statusname) {
        Statusname = statusname;
    }





    public String getValuesinv() {
        return Valuesinv;
    }

    public void setValuesinv(String values) {
        Valuesinv = values;
    }

    public String getInvoiceValues() {
        return InvoiceValues;
    }

    public void setInvoiceValues(String invoiceValues) {
        InvoiceValues = invoiceValues;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }


    public void set_long(String _long) {
        this._long = _long;
    }
    public String getType() {
        return Type;
    }
    public void setType(String type) {
        Type = type;
    }
    public String getDelivType() {
        return DelivType;
    }
    public String getFreezer_status() {
        return Freezer_status;
    }
    public void setFreezer_status(String freezer_status) {
        Freezer_status = freezer_status;
    }
    public String getFreezer_Tag_no() {
        return Freezer_Tag_no;
    }
    public String getPan_No() {
        return Pan_No;
    }
    public String getFssiNo() {
        return FssiNo;
    }
    public String getFreezer_capacity() {
        return freezer_capacity;
    }
    public String getFreezer_attachments() {
        return freezer_attachments;
    }
    public String getFreezer_make() {
        return Freezer_make;
    }
    public String getDeposit_amount() {
        return deposit_amount;
    }
    public String getFreezer_required() {
        return freezer_required;
    }
    public void setFreezer_required(String freezer_required) {
        this.freezer_required = freezer_required;
    }

    public String getExpected_sales_value() {
        return expected_sales_value;
    }

    public Retailer_Modal_List(String cust_Code, double others, double othersVal, double curd, double curdVal, double milk, double milkVal) {
        Cust_Code = cust_Code;
        Others = others;
        OthersVal = othersVal;
        Curd = curd;
        CurdVal = curdVal;
        Milk = milk;
        MilkVal = milkVal;

    }


    public Retailer_Modal_List(String id, String name, String statusname, String Valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode) {
        this.id = id;
        this.name = name;
        Statusname = statusname;
        Valuesinv = Valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
    }

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("Distributor_Code")
    @Expose
    private String DistCode;

    public String getOwner_Name() {
        return Owner_Name;
    }


    public void setOwner_Name(String owner_Name) {
        Owner_Name = owner_Name;
    }

    public Retailer_Modal_List(String id, String name, String owner_Name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String listedDr_Email, String cityname, String compititor_Name, String compititor_Id, String lastUpdt_Date, String hatsanavail_Switch, String hatsanCategory_Switch, String reason_category, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        Owner_Name = owner_Name;
        Statusname = statusname;
        Valuesinv = valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
        Invoice_Flag = invoice_Flag;
        Hatsun_AvailablityId = hatsun_AvailablityId;
        ListedDr_Email = listedDr_Email;
        this.cityname = cityname;
        Compititor_Name = compititor_Name;
        Compititor_Id = compititor_Id;
        LastUpdt_Date = lastUpdt_Date;
        Hatsanavail_Switch = hatsanavail_Switch;
        HatsanCategory_Switch = hatsanCategory_Switch;
        this.reason_category = reason_category;
        Category_Universe_Id = category_Universe_Id;
    }

    @SerializedName("Owner_Name")
    @Expose
    private String Owner_Name;
    @SerializedName("StateCode")
    @Expose
    private String StateCode;
    @SerializedName("Statusname")
    @Expose
    private String Statusname;
    @SerializedName("Valuesinv")
    @Expose
    private String Valuesinv;
    @SerializedName("InvoiceValues")
    @Expose
    private String InvoiceValues;
    @SerializedName("InvoiceDate")
    @Expose
    private String InvoiceDate;
    @SerializedName("town_code")
    @Expose
    private String townCode;
    @SerializedName("town_name")
    @Expose
    private String townName;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("addrs")
    @Expose
    private String addrs;
    @SerializedName("ListedDr_Address1")
    @Expose
    private String listedDrAddress1;
    @SerializedName("ListedDr_Sl_No")
    @Expose
    private Object listedDrSlNo;
    @SerializedName("Mobile_Number")
    @Expose
    private String mobileNumber;
    @SerializedName("Primary_No")
    @Expose
    private String Primary_No;
    @SerializedName("Secondary_No")
    @Expose
    private String Secondary_No;
    @SerializedName("Doc_cat_code")
    @Expose
    private Integer docCatCode;
    @SerializedName("ContactPersion")
    @Expose
    private String contactPersion;
    @SerializedName("Doc_Special_Code")
    @Expose
    private Integer docSpecialCode;
    @SerializedName("Invoice_Flag")
    @Expose
    private String Invoice_Flag;
    @SerializedName("Hatsun_AvailablityId")
    @Expose
    private String Hatsun_AvailablityId;
    @SerializedName("ListedDr_Email")
    @Expose
    private String ListedDr_Email;
    @SerializedName("cityname")
    @Expose
    private String cityname;
    @SerializedName("Compititor_Name")
    @Expose
    private String Compititor_Name;

    @SerializedName("Compititor_Id")
    @Expose
    private String Compititor_Id;
    @SerializedName("LastUpdt_Date")
    @Expose
    private String LastUpdt_Date;
    @SerializedName("Hatsanavail_Switch")
    @Expose
    private String Hatsanavail_Switch;
    @SerializedName("HatsanCategory_Switch")
    @Expose
    private String HatsanCategory_Switch;


    @SerializedName("place_id")
    @Expose
    private String place_id = "ChIJ6fBt_tVnUjoRVxxz1mgBipI";
    @SerializedName("Category")
    @Expose
    private String Category;
    @SerializedName("Speciality")
    @Expose
    private String Speciality;
    @SerializedName("Class")
    @Expose
    private String Class;
    @SerializedName("Imagename")
    @Expose
    private String Imagename;

    public Retailer_Modal_List(String id, String name, String owner_Name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String listedDr_Email, String cityname, String compititor_Name, String compititor_Id, String lastUpdt_Date, String hatsanavail_Switch, String hatsanCategory_Switch, String pin_code, String gst, String reason_category, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        Owner_Name = owner_Name;
        Statusname = statusname;
        Valuesinv = valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
        Invoice_Flag = invoice_Flag;
        Hatsun_AvailablityId = hatsun_AvailablityId;
        ListedDr_Email = listedDr_Email;
        this.cityname = cityname;
        Compititor_Name = compititor_Name;
        Compititor_Id = compititor_Id;
        LastUpdt_Date = lastUpdt_Date;
        Hatsanavail_Switch = hatsanavail_Switch;
        HatsanCategory_Switch = hatsanCategory_Switch;
        this.pin_code = pin_code;
        this.gst = gst;
        this.reason_category = reason_category;
        Category_Universe_Id = category_Universe_Id;
    }

    @SerializedName("pin_code")
    @Expose
    private String pin_code;

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    @SerializedName("gst")
    @Expose
    private String gst;

    public String getHatsanavail_Switch() {
        return Hatsanavail_Switch;
    }

    public void setHatsanavail_Switch(String hatsanavail_Switch) {
        Hatsanavail_Switch = hatsanavail_Switch;
    }

    public String getHatsanCategory_Switch() {
        return HatsanCategory_Switch;
    }

    public void setHatsanCategory_Switch(String hatsanCategory_Switch) {
        HatsanCategory_Switch = hatsanCategory_Switch;
    }

    public String getReason_category() {
        return reason_category;
    }

    public void setReason_category(String reason_category) {
        this.reason_category = reason_category;
    }

    public String getDistCode() {
        return DistCode;
    }

    public void setDistCode(String distCode) {
        DistCode = distCode;
    }

    @SerializedName("reason_category")
    @Expose
    private String reason_category;

    public String getLastUpdt_Date() {
        return LastUpdt_Date;
    }

    public void setLastUpdt_Date(String lastUpdt_Date) {
        LastUpdt_Date = lastUpdt_Date;
    }

    public Retailer_Modal_List(String id, String name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String listedDr_Email, String cityname, String compititor_Name, String compititor_Id, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        Statusname = statusname;
        Valuesinv = valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
        Invoice_Flag = invoice_Flag;
        Hatsun_AvailablityId = hatsun_AvailablityId;
        ListedDr_Email = listedDr_Email;
        this.cityname = cityname;
        Compititor_Name = compititor_Name;
        Compititor_Id = compititor_Id;
        Category_Universe_Id = category_Universe_Id;
    }

    public String getCompititor_Name() {
        return Compititor_Name;
    }

    public void setCompititor_Name(String compititor_Name) {
        Compititor_Name = compititor_Name;
    }

    public String getCompititor_Id() {
        return Compititor_Id;
    }

    public void setCompititor_Id(String compititor_Id) {
        Compititor_Id = compititor_Id;
    }

    public String getListedDr_Email() {
        return ListedDr_Email;
    }

    public void setListedDr_Email(String listedDr_Email) {
        ListedDr_Email = listedDr_Email;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getHatsun_AvailablityId() {
        return Hatsun_AvailablityId;
    }

    public void setHatsun_AvailablityId(String hatsun_AvailablityId) {
        Hatsun_AvailablityId = hatsun_AvailablityId;
    }

    public String getCategory_Universe_Id() {
        return Category_Universe_Id;
    }

    public void setCategory_Universe_Id(String category_Universe_Id) {
        Category_Universe_Id = category_Universe_Id;
    }

    public Retailer_Modal_List(String id, String name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        this.Statusname = statusname;
        this.Valuesinv = valuesinv;
        this.InvoiceValues = invoiceValues;
        this.InvoiceDate = invoiceDate;
        this.Invoice_Flag = invoice_Flag;
        this.Hatsun_AvailablityId = hatsun_AvailablityId;
        this.Category_Universe_Id = category_Universe_Id;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
    }

    @SerializedName("Category_Universe_Id")
    @Expose
    private String Category_Universe_Id;

    public String getInvoice_Flag() {
        return Invoice_Flag;
    }

    public void setInvoice_Flag(String invoice_Flag) {
        Invoice_Flag = invoice_Flag;
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

    public String getTownCode() {
        return townCode;
    }
    public String getTownName() {
        return townName;
    }
    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getAddrs() {
        return addrs;
    }

    public void setAddrs(String addrs) {
        this.addrs = addrs;
    }

    public String getListedDrAddress1() {
        return listedDrAddress1;
    }

    public void setListedDrAddress1(String listedDrAddress1) {
        this.listedDrAddress1 = listedDrAddress1;
    }

    public Object getListedDrSlNo() {
        return listedDrSlNo;
    }

    public void setListedDrSlNo(Object listedDrSlNo) {
        this.listedDrSlNo = listedDrSlNo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getDocCatCode() {
        return docCatCode;
    }

    public void setDocCatCode(Integer docCatCode) {
        this.docCatCode = docCatCode;
    }

    public String getContactPersion() {
        return contactPersion;
    }

    public void setContactPersion(String contactPersion) {
        this.contactPersion = contactPersion;
    }

    public Integer getDocSpecialCode() {
        return docSpecialCode;
    }

    public void setDocSpecialCode(Integer docSpecialCode) {
        this.docSpecialCode = docSpecialCode;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }


    public String getERP_Code() {
        return ERP_Code;
    }

    public void setERP_Code(String ERP_Code) {
        this.ERP_Code = ERP_Code;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getOutletClass() {
        return Class;
    }

    public void setOutletClass(String aClass) {
        Class = aClass;
    }

    public int getOrderFlg() {
        return OrderFlg;
    }

    public void setOrderFlg(int orderFlg) {
        OrderFlg = orderFlg;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getImagename() {
        return Imagename;
    }

    public void setImagename(String imagename) {
        Imagename = imagename;
    }

    public String getSecondary_No() {
        return Secondary_No;
    }

    public void setSecondary_No(String secondary_No) {
        Secondary_No = secondary_No;
    }

    public String getPrimary_No() {
        return Primary_No;
    }

    public void setPrimary_No(String primary_No) {
        Primary_No = primary_No;
    }
    public String getClosedRemarks() {
        return ClosedRemarks;
    }
    public void setClosedRemarks(String closedRemarks) {
        ClosedRemarks = closedRemarks;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public class CateSpecList{
        @SerializedName("OutletCode")
        @Expose
        public String OutletCode;
        @SerializedName("OutletCat_Type")
        @Expose
        public String OutletCat_Type;
        @SerializedName("Category_Code")
        @Expose
        public String Category_Code;
        @SerializedName("Category_Name")
        @Expose
        public String Category_Name;
        @SerializedName("Sub_Category_Code")
        @Expose
        public String Sub_Category_Code;
        @SerializedName("Sub_Category_Name")
        @Expose
        public String Sub_Category_Name;
    }
}
