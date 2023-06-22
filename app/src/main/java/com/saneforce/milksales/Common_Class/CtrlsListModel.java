package com.saneforce.milksales.Common_Class;

import android.widget.EditText;

import java.util.List;

public class CtrlsListModel {

    private List<Ctrls> CtrlsList;
    private String AttachNeed;

    public CtrlsListModel(List<Ctrls> ctrlsList, String attachNeed) {
        CtrlsList = ctrlsList;
        AttachNeed = attachNeed;
    }

    public List<Ctrls> getCtrlsList() {
        return CtrlsList;
    }

    public String getAttachNeed() {
        return AttachNeed;
    }

    public static class Ctrls{
        private String TxtLabel;
        private EditText TxtValue;

        public Ctrls(String txtLabel, EditText txtValue) {
            TxtLabel = txtLabel;
            TxtValue = txtValue;
        }

        public String getTxtLabel() {
            return TxtLabel;
        }

        public EditText getTxtValue() {
            return TxtValue;
        }
    }

}
