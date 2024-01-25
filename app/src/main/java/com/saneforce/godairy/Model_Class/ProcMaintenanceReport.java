package com.saneforce.godairy.Model_Class;

public class ProcMaintenanceReport {
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getNo_of_equipment() {
        return no_of_equipment;
    }

    public void setNo_of_equipment(String no_of_equipment) {
        this.no_of_equipment = no_of_equipment;
    }

    public String getRepair_type() {
        return repair_type;
    }

    public void setRepair_type(String repair_type) {
        this.repair_type = repair_type;
    }

    public String getRepair_img() {
        return repair_img;
    }

    public void setRepair_img(String repair_img) {
        this.repair_img = repair_img;
    }

    private String company, plant, no_of_equipment, repair_type, repair_img, created_dt;

    public String getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }
}
