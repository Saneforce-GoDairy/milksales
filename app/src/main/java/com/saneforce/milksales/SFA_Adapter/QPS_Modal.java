package com.saneforce.milksales.SFA_Adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QPS_Modal {


    private String p_id;
    @SerializedName("name")
    @Expose
    private String name;

    private String sku;
    private double price;
    @SerializedName("Amount")
    @Expose
    private Double Amount;
    @SerializedName("Qty")
    @Expose
    private Integer Qty;

    @SerializedName("Scheme")
    @Expose
    private String scheme;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Integer getQty() {
        return Qty;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public QPS_Modal(String sNo, String requestNo, String gift, String bookingDate, String duration, String receivedDate, String Status,
                     List<String> fileUrls) {
        this.sNo = sNo;
        this.requestNo = requestNo;
        this.gift = gift;
        this.bookingDate = bookingDate;
        this.duration = duration;
        this.receivedDate = receivedDate;
        this.Status = Status;
        this.fileUrls = fileUrls;
    }


    public QPS_Modal(String p_id, String name, String sku, double price, int Qty, double amount, String scheme) {
        this.p_id = p_id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.Qty = Qty;
        this.scheme = scheme;
        this.Amount = amount;
    }


    @SerializedName("id")
    @Expose
    private String sNo;

    private List<String> fileUrls;


    @SerializedName("requestNo")
    @Expose
    private String requestNo;
    @SerializedName("gift")
    @Expose
    private String gift;
    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;
    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("Status")
    @Expose
    private String Status;


    public QPS_Modal(String filePath, String fileName, String fileKey) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileKey = fileKey;
    }

    //            holder.receivedDate.setText("" + itm.getString("Received_Date"));
//            holder.status.setText("" + itm.getString("POP_Status"));
//            holder.materialName.setText("" + itm.getString("POP_ID"));
    //    String images = itm.getString("Images");


    public QPS_Modal(String name, String Status, String receivedDate, String fileName, String p_id, String sNo) {
        this.name = name;
        this.Status = Status;
        this.receivedDate = receivedDate;
        this.fileName = fileName;
        this.p_id = p_id;
        this.sNo = sNo;
    }

    private String filePath;
    private String fileName;
    private String fileKey;


    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    @SerializedName("receivedDate")
    @Expose
    private String receivedDate;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }
}
