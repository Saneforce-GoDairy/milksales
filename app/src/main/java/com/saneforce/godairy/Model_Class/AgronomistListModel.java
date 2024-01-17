package com.saneforce.godairy.Model_Class;

public class AgronomistListModel {
    private String created_dt;
    private String company;
    private String farmer_name;
    private String center_name;
    private String service_type;
    private String product_type;

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setFarmer_name(String farmer_name) {
        this.farmer_name = farmer_name;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }

    public String getCreated_dt() {
        return created_dt;
    }

    public String getCompany() {
        return company;
    }

    public String getFarmer_name() {
        return farmer_name;
    }

    public String getCenter_name() {
        return center_name;
    }

    public String getService_type() {
        return service_type;
    }

    public String getProduct_type() {
        return product_type;
    }
}
