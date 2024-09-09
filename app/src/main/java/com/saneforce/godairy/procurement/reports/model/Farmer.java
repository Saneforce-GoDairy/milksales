package com.saneforce.godairy.procurement.reports.model;

public class Farmer {
    private String id, farmer_name , farmer_photo, farmer_mobile, state, district, town, coll_center;
    private String farmerCategory, address, pincode, city, email, incentive_amt, cartage_amt;

    public String getFarmerCategory() {
        return farmerCategory;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIncentive_amt() {
        return incentive_amt;
    }

    public void setIncentive_amt(String incentive_amt) {
        this.incentive_amt = incentive_amt;
    }

    public String getCartage_amt() {
        return cartage_amt;
    }

    public void setCartage_amt(String cartage_amt) {
        this.cartage_amt = cartage_amt;
    }

    public void setFarmerCategory(String farmerCategory) {
        this.farmerCategory = farmerCategory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getColl_center() {
        return coll_center;
    }

    public void setColl_center(String coll_center) {
        this.coll_center = coll_center;
    }

    public String getFarmer_name() {
        return farmer_name;
    }

    public void setFarmer_name(String farmer_name) {
        this.farmer_name = farmer_name;
    }

    public String getFarmer_mobile() {
        return farmer_mobile;
    }

    public void setFarmer_mobile(String farmer_mobile) {
        this.farmer_mobile = farmer_mobile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFarmer_photo(String farmer_photo) {
        this.farmer_photo = farmer_photo;
    }

    public String getId() {
        return id;
    }

    public String getFarmer_photo() {
        return farmer_photo;
    }
}
