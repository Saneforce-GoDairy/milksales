package com.saneforce.godairy.SFA_Model_Class;

public class OutletGeoTagInfoModel {
    String code, name, address, lat, lng, mobile;

    public OutletGeoTagInfoModel(String code, String name, String address, String lat, String lng, String mobile) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
