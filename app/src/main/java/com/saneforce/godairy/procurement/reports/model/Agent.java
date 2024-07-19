package com.saneforce.godairy.procurement.reports.model;

public class Agent {
    private String agentImage, state, district, town, coll_center, agent_category,company,  address, pin_code;
    private String city, mobile, incentive, cartage, email, agent_name;

    public void setAgentImage(String agentImage) {
        this.agentImage = agentImage;
    }

    public String getAgentImage() {
        return agentImage;
    }

    public void setColl_center(String coll_center) {
        this.coll_center = coll_center;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getColl_center() {
        return coll_center;
    }

    public String getDistrict() {
        return district;
    }

    public String getState() {
        return state;
    }

    public String getTown() {
        return town;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAgent_category(String agent_category) {
        this.agent_category = agent_category;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }

    public String getAgent_category() {
        return agent_category;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setCartage(String cartage) {
        this.cartage = cartage;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCartage() {
        return cartage;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getIncentive() {
        return incentive;
    }

    public String getMobile() {
        return mobile;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getAgent_name() {
        return agent_name;
    }
}
