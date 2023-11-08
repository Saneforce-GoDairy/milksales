package com.saneforce.godairy.SFA_Model_Class;

import java.util.ArrayList;

public class SalesReturnProductModel {
    String Product_Code, Product_Name, Sale_Erp_Code, UOM;
    double MRP, Price, invAmount, invTax, Margin, retAmount, retTax;
    int invQty, Con_Fac, retQty;
    ArrayList<TaxModel> taxList;

    public SalesReturnProductModel(String product_Code, String product_Name, String sale_Erp_Code, String UOM, double MRP, double price, double invAmount, double invTax, double margin, double retAmount, double retTax, int invQty, int con_Fac, int retQty, ArrayList<TaxModel> taxList) {
        Product_Code = product_Code;
        Product_Name = product_Name;
        Sale_Erp_Code = sale_Erp_Code;
        this.UOM = UOM;
        this.MRP = MRP;
        Price = price;
        this.invAmount = invAmount;
        this.invTax = invTax;
        Margin = margin;
        this.retAmount = retAmount;
        this.retTax = retTax;
        this.invQty = invQty;
        Con_Fac = con_Fac;
        this.retQty = retQty;
        this.taxList = taxList;
    }

    public String getProduct_Code() {
        return Product_Code;
    }

    public void setProduct_Code(String product_Code) {
        Product_Code = product_Code;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getSale_Erp_Code() {
        return Sale_Erp_Code;
    }

    public void setSale_Erp_Code(String sale_Erp_Code) {
        Sale_Erp_Code = sale_Erp_Code;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(double invAmount) {
        this.invAmount = invAmount;
    }

    public double getInvTax() {
        return invTax;
    }

    public void setInvTax(double invTax) {
        this.invTax = invTax;
    }

    public double getMargin() {
        return Margin;
    }

    public void setMargin(double margin) {
        Margin = margin;
    }

    public double getRetAmount() {
        return retAmount;
    }

    public void setRetAmount(double retAmount) {
        this.retAmount = retAmount;
    }

    public double getRetTax() {
        return retTax;
    }

    public void setRetTax(double retTax) {
        this.retTax = retTax;
    }

    public int getInvQty() {
        return invQty;
    }

    public void setInvQty(int invQty) {
        this.invQty = invQty;
    }

    public int getCon_Fac() {
        return Con_Fac;
    }

    public void setCon_Fac(int con_Fac) {
        Con_Fac = con_Fac;
    }

    public int getRetQty() {
        return retQty;
    }

    public void setRetQty(int retQty) {
        this.retQty = retQty;
    }

    public ArrayList<TaxModel> getTaxList() {
        return taxList;
    }

    public void setTaxList(ArrayList<TaxModel> taxList) {
        this.taxList = taxList;
    }
}