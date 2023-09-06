package com.saneforce.milksales.session;

public class User {
    String myDayPlan, checkInEnabled, checkInTimeStamp;

    public void setMyDayPlan(String myDayPlan) {
        this.myDayPlan = myDayPlan;
    }

    public String getMyDayPlan() {
        return myDayPlan;
    }

    public void setCheckInEnabled(String checkInEnabled) {
        this.checkInEnabled = checkInEnabled;
    }

    public String getCheckInEnabled() {
        return checkInEnabled;
    }

    public void setCheckInTimeStamp(String checkInTimeStamp) {
        this.checkInTimeStamp = checkInTimeStamp;
    }

    public String getCheckInTimeStamp() {
        return checkInTimeStamp;
    }
}
