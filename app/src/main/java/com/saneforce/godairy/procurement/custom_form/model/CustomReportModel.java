package com.saneforce.godairy.procurement.custom_form.model;

public class CustomReportModel {

    String EntryId;
    String id;
    private int type;

    public CustomReportModel(String EntryId, String id, int type) {
        this.EntryId = EntryId;
        this.id = id;
        this.type = type;
    }

    public CustomReportModel() {
        this.EntryId = EntryId;
        this.id = id;
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEntryId(String entryId) {
        EntryId = entryId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getEntryId() {
        return EntryId;
    }

    public int getType() {
        return type;
    }
}
