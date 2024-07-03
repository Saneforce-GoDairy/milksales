package com.saneforce.godairy.Model_Class;

public class ProcMaintenRegularReport {
    private String company, plant, created_dt;
    private String bmc_hrs_running, bmc_volume_coll, cc_hrs_running, cc_volume_coll, ibt_running_hrs, dg_set_running, dg_set_running_img;
    private String power_factor, pipeline_condition, leakage, scale, per_book, physical, etp, hot_water, factory_license_ins;
    private String active_flag, hrs_runs_image, as_per_book_img;

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

    public String getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }

    public void setBmc_hrs_running(String bmc_hrs_running) {
        this.bmc_hrs_running = bmc_hrs_running;
    }

    public String getBmc_hrs_running() {
        return bmc_hrs_running;
    }

    public void setHrs_runs_image(String hrs_runs_image) {
        this.hrs_runs_image = hrs_runs_image;
    }

    public String getHrs_runs_image() {
        return hrs_runs_image;
    }

    public void setBmc_volume_coll(String bmc_volume_coll) {
        this.bmc_volume_coll = bmc_volume_coll;
    }

    public String getBmc_volume_coll() {
        return bmc_volume_coll;
    }
}
