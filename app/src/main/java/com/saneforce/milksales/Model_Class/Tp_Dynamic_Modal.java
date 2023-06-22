package com.saneforce.milksales.Model_Class;

import com.saneforce.milksales.Common_Class.Common_Model;

import java.util.ArrayList;

public class Tp_Dynamic_Modal {
    String Fld_ID,Fld_Name,Fld_Values,  Fld_Type,Fld_Src_Name,Fld_Src_Field,Fld_Symbol,Fld_Mandatory,Active_flag,Control_id,Target_Form,Filter_Text,Filter_Value,Field_Col;

    Integer Fld_Length;
    ArrayList<Common_Model> a_list=new ArrayList<>();
    public Tp_Dynamic_Modal(String fld_ID, String fld_Name,String fld_Values, String fld_Type, String fld_Src_Name, String fld_Src_Field, Integer fld_Length, String fld_Symbol, String fld_Mandatory, String active_flag, String control_id, String target_Form, String filter_Text, String filter_Value, String field_Col, ArrayList<Common_Model>Aa_list) {
        Fld_ID = fld_ID;
        Fld_Name = fld_Name;
        Fld_Values=fld_Values;
        Fld_Type = fld_Type;
        Fld_Src_Name = fld_Src_Name;
        Fld_Src_Field = fld_Src_Field;
        Fld_Length = fld_Length;
        Fld_Symbol = fld_Symbol;
        Fld_Mandatory = fld_Mandatory;
        Active_flag = active_flag;
        Control_id = control_id;
        Target_Form = target_Form;
        Filter_Text = filter_Text;
        Filter_Value = filter_Value;
        Field_Col = field_Col;
        a_list = Aa_list;
    }

    public String getFld_ID() {
        return Fld_ID;
    }

    public void setFld_ID(String fld_ID) {
        Fld_ID = fld_ID;
    }

    public String getFld_Name() {
        return Fld_Name;
    }

    public void setFld_Name(String fld_Name) {
        Fld_Name = fld_Name;
    }

    public String getFld_Values() {
        return Fld_Values;
    }

    public void setFld_Values(String fld_Values) {
        Fld_Values = fld_Values;
    }

    public String getFld_Type() {
        return Fld_Type;
    }

    public void setFld_Type(String fld_Type) {
        Fld_Type = fld_Type;
    }

    public String getFld_Src_Name() {
        return Fld_Src_Name;
    }

    public void setFld_Src_Name(String fld_Src_Name) {
        Fld_Src_Name = fld_Src_Name;
    }

    public String getFld_Src_Field() {
        return Fld_Src_Field;
    }

    public void setFld_Src_Field(String fld_Src_Field) {
        Fld_Src_Field = fld_Src_Field;
    }

    public Integer getFld_Length() {
        return Fld_Length;
    }

    public void setFld_Length(Integer fld_Length) {
        Fld_Length = fld_Length;
    }

    public String getFld_Symbol() {
        return Fld_Symbol;
    }

    public void setFld_Symbol(String fld_Symbol) {
        Fld_Symbol = fld_Symbol;
    }

    public String getFld_Mandatory() {
        return Fld_Mandatory;
    }

    public void setFld_Mandatory(String fld_Mandatory) {
        Fld_Mandatory = fld_Mandatory;
    }

    public String getActive_flag() {
        return Active_flag;
    }

    public void setActive_flag(String active_flag) {
        Active_flag = active_flag;
    }

    public String getControl_id() {
        return Control_id;
    }

    public void setControl_id(String control_id) {
        Control_id = control_id;
    }

    public String getTarget_Form() {
        return Target_Form;
    }

    public void setTarget_Form(String target_Form) {
        Target_Form = target_Form;
    }

    public String getFilter_Text() {
        return Filter_Text;
    }

    public void setFilter_Text(String filter_Text) {
        Filter_Text = filter_Text;
    }

    public String getFilter_Value() {
        return Filter_Value;
    }

    public void setFilter_Value(String filter_Value) {
        Filter_Value = filter_Value;
    }

    public String getField_Col() {
        return Field_Col;
    }

    public void setField_Col(String field_Col) {
        Field_Col = field_Col;
    }

    public ArrayList<Common_Model> getA_list() {
        return a_list;
    }

    public void setA_list(ArrayList<Common_Model> a_list) {
        this.a_list = a_list;
    }
}
