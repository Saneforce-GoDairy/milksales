package com.saneforce.milksales.Model_Class;

public class DashboardParticulars {
    //2021-03-19 00
    String Name, thisMonth, lastmonth, percentage,Sl_NO;

    public DashboardParticulars(String Sl_NO, String Name, String thisMonth, String lastmonth, String percentage) {
        this.Sl_NO = Sl_NO;
        this.Name = Name;
        this.thisMonth = thisMonth;
        this.lastmonth = lastmonth;
        this.percentage = percentage;
    }

    public String getSl_NO() {
        return Sl_NO;
    }

    public void setSl_NO(String listName) {
        this.Sl_NO = listName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(String thisMonth) {
        this.thisMonth = thisMonth;
    }

    public String getLastmonth() {
        return lastmonth;
    }

    public void setLastmonth(String lastmonth) {
        this.lastmonth = lastmonth;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
