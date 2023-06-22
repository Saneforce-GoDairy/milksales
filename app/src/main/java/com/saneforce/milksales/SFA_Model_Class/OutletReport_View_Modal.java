package com.saneforce.milksales.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OutletReport_View_Modal {

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    @SerializedName("GRN_Date1")
    @Expose
    private String grnDate = "";

    private  String name;
    @SerializedName("No_Of_items")
    @Expose
    private int No_Of_items;
    @SerializedName("slno")
    @Expose
    private String slno = "";
    @SerializedName("OrderNo")
    @Expose
    private String orderNo = "";

    public String getBatch_No() {
        return batch_No;
    }

    public void setBatch_No(String batch_No) {
        this.batch_No = batch_No;
    }

    @SerializedName("batch_no")
    @Expose
    private String batch_No = "";

    public String getProdDetails() {
        return prodDetails;
    }

    public void setProdDetails(String prodDetails) {
        this.prodDetails = prodDetails;
    }

    public String getProdUom() {
        return prodUom;
    }

    public void setProdUom(String prodUom) {
        this.prodUom = prodUom;
    }

    public String getPunit() {
        return punit;
    }

    public void setPunit(String punit) {
        this.punit = punit;
    }

    @SerializedName("product_unit")
    @Expose
    private String punit = "";

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    @SerializedName("UOM")
    @Expose
    private String unitCode = "";


    @SerializedName("uom_name")
    @Expose
    private String prodUom = "";

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    @SerializedName("Unit_code")
    @Expose
    private String uName = "";

    public String getProdTotal() {
        return prodTotal;
    }

    public void setProdTotal(String prodTotal) {
        this.prodTotal = prodTotal;
    }

    @SerializedName("Gross_Value")
    @Expose
    private String prodTotal = "";

    @SerializedName("PDetails")
    @Expose
    private String prodDetails = "";

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    @SerializedName("Price")
    @Expose
    private String prodPrice = "";

    public String getProdQnty() {
        return prodQnty;
    }

    public void setProdQnty(String prodQnty) {
        this.prodQnty = prodQnty;
    }

    @SerializedName("POQTY")
    @Expose
    private String prodQnty = "";

    public String getDamaged() {
        return damaged;
    }

    public void setDamaged(String damaged) {
        this.damaged = damaged;
    }

    @SerializedName("Damaged")
    @Expose
    private String damaged = "";


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @SerializedName("Product_Name")
    @Expose
    private String productName = "";
    @SerializedName("Product_code")
    @Expose
    private String productCode = "";
    @SerializedName("manufactor_date")
    @Expose
    private String manufDate = "";
    @SerializedName("exp_date")
    @Expose
    private String expDate = "";

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getManufDate() {
        return manufDate;
    }

    public void setManufDate(String manufDate) {
        this.manufDate = manufDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    @SerializedName("MRP")
    @Expose
    private String mrp = "";

    public String getBilledQty() {
        return billedQty;
    }

    public void setBilledQty(String billedQty) {
        this.billedQty = billedQty;
    }

    @SerializedName("billed_qty")
    @Expose
    private String billedQty = "";

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @SerializedName("Rate")
    @Expose
    private Double rate = 0.0;

    public Double getTaxVal() {
        return taxVal;
    }

    public void setTaxVal(Double taxVal) {
        this.taxVal = taxVal;
    }

    @SerializedName("total_tax_val")
    @Expose
    private Double taxVal = 0.0;

    public void setProductDetailsModal(List<OutletReport_View_Modal> productDetailsModal) {
        this.productDetailsModal = productDetailsModal;
    }

    public String gettValue() {
        return tValue;
    }

    public void settValue(String tValue) {
        this.tValue = tValue;
    }

    @SerializedName("Net_value")
    @Expose
    private String tValue = "";

    public int getDeliveredQty() {
        return deliveredQty;
    }

    public void setDeliveredQty(int deliveredQty) {
        this.deliveredQty = deliveredQty;
    }

    @SerializedName("deliver_qty")
    @Expose
    private int deliveredQty;


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @SerializedName("Product_Image")
    @Expose
    private String productImage ;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @SerializedName("sap_batch_no")
    @Expose
    private String batchNo = "";


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @SerializedName("flg")
    @Expose
    private String flag = "";

    public String getNetValue() {
        return netValue;
    }

    public void setNetValue(String netValue) {
        this.netValue = netValue;
    }

    @SerializedName("Net_Value")
    @Expose
    private String netValue ="" ;

    public String getGrnNo() {
        return grnNo;
    }

    public void setGrnNo(String grnNo) {
        this.grnNo = grnNo;
    }

    @SerializedName("GRN_No")
    @Expose
    private String grnNo = "";

    public Double getGrnTotal() {
        return grnTotal;
    }

    public void setGrnTotal(Double grnTotal) {
        this.grnTotal = grnTotal;
    }

    @SerializedName("Net_Tot_Value")
    @Expose
    private Double grnTotal = 0.0;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @SerializedName("Net_Tot_Goods")
    @Expose
    private Double total = 0.0;

    public Double getGrnTax() {
        return grnTax;
    }

    public void setGrnTax(Double grnTax) {
        this.grnTax = grnTax;
    }

    @SerializedName("Net_Tot_Tax")
    @Expose
    private Double grnTax = 0.0;

    public String getSuppName() {
        return suppName;
    }

    public void setSuppName(String suppName) {
        this.suppName = suppName;
    }

    @SerializedName("Supp_Name")
    @Expose
    private String suppName = "";



    public String getPono() {
        return pono;
    }

    public void setPono(String pono) {
        this.pono = pono;
    }

    @SerializedName("Po_No")
    @Expose
    private String pono = "";

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    @SerializedName("Billing_Date")
    @Expose
    private String billingDate = "";


    public String getBillingId() {
        return billingId;
    }

    public void setBillingId(String billingId) {
        this.billingId = billingId;
    }

    @SerializedName("Billing_Document")
    @Expose
    private String billingId = "";

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    @SerializedName("Sales_Document")
    @Expose
    private String salesId = "";

    @SerializedName("Qty_deleivered")
    @Expose
    private Double qnty = 0.0;

    public String getSubdiv() {
        return subdiv;
    }

    public void setSubdiv(String subdiv) {
        this.subdiv = subdiv;
    }

    @SerializedName("subdivision_code")
    @Expose
    private String subdiv = "";

    public Double getQnty() {
        return qnty;
    }

    public void setQnty(Double qnty) {
        this.qnty = qnty;
    }


    public int getTot_qty() {
        return tot_qty;
    }

    public void setTot_qty(int tot_qty) {
        this.tot_qty = tot_qty;
    }

    @SerializedName("Total_Qty")
    @Expose
    private int tot_qty ;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @SerializedName("Amount")
    @Expose
    private Double amount = 0.0;

    public Double getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(Double tax_amount) {
        this.tax_amount = tax_amount;
    }

    @SerializedName("Tax_Amount")
    @Expose
    private Double tax_amount = 0.0;

    @SerializedName("Stockist_Code")
    @Expose
    private String stockistCode = "";
    @SerializedName("Trans_Sl_No")
    @Expose
    private String transSlNo = "";
    @SerializedName("Outlet_Code")
    @Expose
    private String outletCode = "";
    @SerializedName("sf_code")
    @Expose
    private String sfCode = "";
    @SerializedName("Order_Date")
    @Expose
    private String orderDate = "";
    @SerializedName("Order_Value")
    @Expose
    private Double orderValue = 0.0;
    @SerializedName("Invoice_Flag")
    @Expose
    private String Invoice_Flag = "";
    @SerializedName("invoicevalues")
    @Expose
    private String invoicevalues = "";
    @SerializedName("NetAmount")
    @Expose
    private String NetAmount = "";
    @SerializedName("Discount_Amount")
    @Expose
    private String Discount_Amount = "";

    @SerializedName("InvoiceID")
    @Expose
    private String InvoiceID = "";

    @SerializedName("InvoiceItems")
    @Expose
    private String InvoiceItems = "";

    @SerializedName("Indent")
    @Expose
    private String Indent = "";

    @SerializedName("Quantity")
    @Expose
    private int Quantity;




    @SerializedName("InvoiceDate")
    @Expose
    private String InvoiceDate = "";

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getInvoiceStatus() {
        return InvoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        InvoiceStatus = invoiceStatus;
    }

    public String getInvoiceAmount() {
        return InvoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        InvoiceAmount = invoiceAmount;
    }

    private List<OutletReport_View_Modal> productDetailsModal = new ArrayList<>();

    @SerializedName("InvoiceStatus")
    @Expose
    private String InvoiceStatus = "";

    @SerializedName("InvoiceAmount")
    @Expose
    private String InvoiceAmount = "";

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    @SerializedName("place_id")
    @Expose
    private String place_id = "ChIJ6fBt_tVnUjoRVxxz1mgBipI";


    public String getNetAmount() {
        return NetAmount;
    }

    public void setNetAmount(String netAmount) {
        NetAmount = netAmount;
    }

    public String getDiscount_Amount() {
        return Discount_Amount;
    }

    public void setDiscount_Amount(String discount_Amount) {
        Discount_Amount = discount_Amount;
    }

    public String getInvoice_Flag() {
        return Invoice_Flag;
    }

    public String getInvoicevalues() {
        return invoicevalues;
    }

    public int getNo_Of_items() {
        return No_Of_items;
    }

    public void setNo_Of_items(int no_Of_items) {
        No_Of_items = no_Of_items;
    }

    public void setInvoicevalues(String invoicevalues) {
        this.invoicevalues = invoicevalues;
    }


    public void setInvoice_Flag(String invoice_Flag) {
        Invoice_Flag = invoice_Flag;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @SerializedName("Status")
    @Expose
    private String Status;

    List<Product_Details_Modal> product_details_modal;

    public List<Product_Details_Modal> getProduct_details_modal() {
        return product_details_modal;
    }

    public void setProduct_details_modal(List<Product_Details_Modal> product_details_modal) {
        this.product_details_modal = product_details_modal;
    }

    public OutletReport_View_Modal(int no_Of_items, String orderNo, String InvoiceID, String outletCode, String orderDate, double orderValue,
                                   String status, List<Product_Details_Modal> product_details_modal) {
        this.outletCode = outletCode;
        this.Status = status;
        this.orderDate = orderDate;
        this.orderNo = orderNo;
        this.InvoiceID = InvoiceID;
        this.orderValue = orderValue;
        this.No_Of_items = no_Of_items;
        this.product_details_modal = product_details_modal;

    }

    public OutletReport_View_Modal(String name, String orderNo, String InvoiceID, String outletCode, String orderDate, double orderValue,
                                   String status) {
        this.outletCode = outletCode;
        this.Status = status;
        this.orderDate = orderDate;
        this.orderNo = orderNo;
        this.InvoiceID = InvoiceID;
        this.orderValue = orderValue;
        this.name = name;

    }

    public OutletReport_View_Modal(String pname,String pcode,String pmrp,String pmanfdate,String pexpdate,String pbilledqty,String pbatchno,String pbillingdate,String pImage,String pUom,String pUomName,Double pTax ){
        this.productName = pname;
        this.productCode = pcode;
        this.mrp = pmrp;
        this.manufDate = pmanfdate;
        this.expDate = pexpdate;
        this.billedQty = pbilledqty;
        this.batchNo = pbatchno;
        this.billingDate = pbillingdate;
        this.productImage = pImage;
        this.punit = pUom;
        this.uName = pUomName;
        this.taxVal = pTax;

    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStockistCode() {
        return stockistCode;
    }

    public void setStockistCode(String stockistCode) {
        this.stockistCode = stockistCode;
    }

    public String getTransSlNo() {
        return transSlNo;
    }

    public void setTransSlNo(String transSlNo) {
        this.transSlNo = transSlNo;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Double orderValue) {
        this.orderValue = orderValue;
    }

    public String getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getInvoiceItems() {
        return InvoiceItems;
    }

    public void setInvoiceItems(String invoiceItems) {
        InvoiceItems = invoiceItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndent() {
        return Indent;
    }

    public void setIndent(String indent) {
        Indent = indent;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

   /* public List<OutletReport_View_Modal> getProductDetailsModal() {
        return productDetailsModal;
    }*/

}

