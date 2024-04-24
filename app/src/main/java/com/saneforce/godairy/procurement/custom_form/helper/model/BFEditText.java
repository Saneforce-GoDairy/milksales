package com.saneforce.godairy.procurement.custom_form.helper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saneforce.godairy.procurement.custom_form.helper.utils.Constants;

public class BFEditText extends BFView {
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSingleLine() {
        return singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty(Constants.JSON_KEY_DESCRIPTION)
    private String description;

    @JsonProperty(Constants.JSON_KEY_SINGLE_LINE)
    private boolean singleLine;

    @JsonProperty(Constants.JSON_KEY_MODE)
    private int mode;

    @JsonIgnore
    private String text;
}
