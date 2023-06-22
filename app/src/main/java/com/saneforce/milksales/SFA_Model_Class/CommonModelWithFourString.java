package com.saneforce.milksales.SFA_Model_Class;

public class CommonModelWithFourString {
    String id, title, regionReference, routeReference;

    public CommonModelWithFourString(String id, String title, String regionReference, String routeReference) {
        this.id = id;
        this.title = title;
        this.regionReference = regionReference;
        this.routeReference = routeReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegionReference() {
        return regionReference;
    }

    public void setRegionReference(String regionReference) {
        this.regionReference = regionReference;
    }

    public String getRouteReference() {
        return routeReference;
    }

    public void setRouteReference(String routeReference) {
        this.routeReference = routeReference;
    }
}
