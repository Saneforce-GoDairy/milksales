package com.saneforce.godairy.Model_Class;

public class ProcMaintenIssue {
    private String company, plant, equipment, repair_type, active_flag, created_dt, repair_type_img;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPlant() {
        return plant;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getRepair_type() {
        return repair_type;
    }

    public String getActive_flag() {
        return active_flag;
    }

    public String getCreated_dt() {
        return created_dt;
    }

    public String getRepair_type_img() {
        return repair_type_img;
    }

    public void setActive_flag(String active_flag) {
        this.active_flag = active_flag;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public void setRepair_type(String repair_type) {
        this.repair_type = repair_type;
    }

    public void setRepair_type_img(String repair_type_img) {
        this.repair_type_img = repair_type_img;
    }
}
