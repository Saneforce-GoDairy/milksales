package com.saneforce.milksales.SFA_Model_Class;

public class CommonModelWithThreeString {
    String id, title, reference;

    public CommonModelWithThreeString(String id, String title, String reference) {
        this.id = id;
        this.title = title;
        this.reference = reference;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
