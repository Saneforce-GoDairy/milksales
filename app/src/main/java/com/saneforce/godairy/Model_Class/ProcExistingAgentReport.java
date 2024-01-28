package com.saneforce.godairy.Model_Class;

public class ProcExistingAgentReport {
    private String agent;

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTotal_milk_availability() {
        return total_milk_availability;
    }

    public void setTotal_milk_availability(String total_milk_availability) {
        this.total_milk_availability = total_milk_availability;
    }

    public String getOur_company_ltrs() {
        return our_company_ltrs;
    }

    public void setOur_company_ltrs(String our_company_ltrs) {
        this.our_company_ltrs = our_company_ltrs;
    }

    public String getCompetitor_rate() {
        return competitor_rate;
    }

    public void setCompetitor_rate(String competitor_rate) {
        this.competitor_rate = competitor_rate;
    }

    public String getOur_company_rate() {
        return our_company_rate;
    }

    public void setOur_company_rate(String our_company_rate) {
        this.our_company_rate = our_company_rate;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getSupply_start_dt() {
        return supply_start_dt;
    }

    public void setSupply_start_dt(String supply_start_dt) {
        this.supply_start_dt = supply_start_dt;
    }

    public String getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }

    private String company;
    private String total_milk_availability;
    private String our_company_ltrs;
    private String competitor_rate;
    private String our_company_rate;
    private String demand, supply_start_dt, created_dt;
}
