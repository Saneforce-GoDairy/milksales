package com.saneforce.godairy.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayReport {
    @SerializedName("emp_name")
    @Expose
    private String empname;
    @SerializedName("work_type")
    @Expose
    private String worktype;
    @SerializedName("emp_id_deg")
    @Expose
    private String empiddeg;
    @SerializedName("submit_time")
    @Expose
    private String submittime;
    @SerializedName("visited_distributor")
    @Expose
    private String visiteddistributor;
    @SerializedName("visited_outlet")
    @Expose
    private Integer visitedoutlet;
    @SerializedName("order_distributor")
    @Expose
    private String orderdistributor;
    @SerializedName("order_outlet")
    @Expose
    private Integer orderoutlet;
    @SerializedName("count_distributor")
    @Expose
    private Integer countdistributor;
    @SerializedName("count_outlet")
    @Expose
    private String countoutlet;
    @SerializedName("ordered_distributor")
    @Expose
    private Integer ordereddistributor;
    @SerializedName("ordered_outlet")
    @Expose
    private Integer orderedoutlet;
    @SerializedName("invoiced_distributor")
    @Expose
    private Integer invoiceddistributor;

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getWorktype() {
        return worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }

    public String getEmpiddeg() {
        return empiddeg;
    }

    public void setEmpiddeg(String empiddeg) {
        this.empiddeg = empiddeg;
    }

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }

    public String getVisiteddistributor() {
        return visiteddistributor;
    }

    public void setVisiteddistributor(String visiteddistributor) {
        this.visiteddistributor = visiteddistributor;
    }

    public Integer getVisitedoutlet() {
        return visitedoutlet;
    }

    public void setVisitedoutlet(Integer visitedoutlet) {
        this.visitedoutlet = visitedoutlet;
    }

    public String getOrderdistributor() {
        return orderdistributor;
    }

    public void setOrderdistributor(String orderdistributor) {
        this.orderdistributor = orderdistributor;
    }

    public Integer getOrderoutlet() {
        return orderoutlet;
    }

    public void setOrderoutlet(Integer orderoutlet) {
        this.orderoutlet = orderoutlet;
    }

    public Integer getCountdistributor() {
        return countdistributor;
    }

    public void setCountdistributor(Integer countdistributor) {
        this.countdistributor = countdistributor;
    }

    public String getCountoutlet() {
        return countoutlet;
    }

    public void setCountoutlet(String countoutlet) {
        this.countoutlet = countoutlet;
    }

    public Integer getOrdereddistributor() {
        return ordereddistributor;
    }

    public void setOrdereddistributor(Integer ordereddistributor) {
        this.ordereddistributor = ordereddistributor;
    }

    public Integer getOrderedoutlet() {
        return orderedoutlet;
    }

    public void setOrderedoutlet(Integer orderedoutlet) {
        this.orderedoutlet = orderedoutlet;
    }

    public Integer getInvoiceddistributor() {
        return invoiceddistributor;
    }

    public void setInvoiceddistributor(Integer invoiceddistributor) {
        this.invoiceddistributor = invoiceddistributor;
    }

    public String getInvoicedoutlet() {
        return invoicedoutlet;
    }

    public void setInvoicedoutlet(String invoicedoutlet) {
        this.invoicedoutlet = invoicedoutlet;
    }

    @SerializedName("invoiced_outlet")
    @Expose
    private String invoicedoutlet;



}
