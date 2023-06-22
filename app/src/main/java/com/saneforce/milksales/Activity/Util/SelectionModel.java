package com.saneforce.milksales.Activity.Util;

import java.util.ArrayList;

public class SelectionModel {
    String txt;
    boolean isClick;
    String code, value, img_url, tmp_url, cardview_id, user_enter, attachment, max;
    ArrayList<SelectionModel> array = new ArrayList<>();

    String modeTravel, from, to, Fare, header;


    public SelectionModel(String txt, boolean isClick) {
        this.txt = txt;
        this.isClick = isClick;
    }

    public SelectionModel(String txt, boolean isClick, String code) {
        this.txt = txt;
        this.isClick = isClick;
        this.code = code;
    }

    public SelectionModel(String txt, ArrayList<SelectionModel> array) {
        this.txt = txt;
        this.array = array;
    }

    public SelectionModel(String txt, String value, String code, String img_url, String tmp_url) {
        this.txt = txt;
        this.value = value;
        this.code = code;
        this.img_url = img_url;
        this.tmp_url = tmp_url;
    }

    public SelectionModel(String header, String txt, String value, String code, String img_url, ArrayList<SelectionModel> array, String user_enter, String attachment, String max) {
        this.txt = txt;
        this.value = value;
        this.code = code;
        this.img_url = img_url;
        this.array = array;
        this.user_enter = user_enter;
        this.attachment = attachment;
        this.max = max;
        this.header = header;
    }
    public SelectionModel( String txt, String value, String code, String img_url, ArrayList<SelectionModel> array, String user_enter, String attachment, String max) {
        this.txt = txt;
        this.value = value;
        this.code = code;
        this.img_url = img_url;
        this.array = array;
        this.user_enter = user_enter;
        this.attachment = attachment;
        this.max = max;

    }

    public SelectionModel(String txt, String code) {
        this.txt = txt;
        this.code = code;
    }


    public SelectionModel(String modeTravel, String from, String to, String Fare) {
        this.modeTravel = modeTravel;
        this.from = from;
        this.to = to;
        this.Fare = Fare;

    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getModeTravel() {
        return modeTravel;
    }

    public void setModeTravel(String modeTravel) {
        this.modeTravel = modeTravel;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public String getCardview_id() {
        return cardview_id;
    }

    public void setCardview_id(String cardview_id) {
        this.cardview_id = cardview_id;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getUser_enter() {
        return user_enter;
    }

    public void setUser_enter(String user_enter) {
        this.user_enter = user_enter;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public ArrayList<SelectionModel> getArray() {
        return array;
    }

    public void setArray(ArrayList<SelectionModel> array) {
        this.array = array;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTmp_url() {
        return tmp_url;
    }

    public void setTmp_url(String tmp_url) {
        this.tmp_url = tmp_url;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SelectionModel(boolean isClick) {
        this.isClick = isClick;
    }

    @Override
    public boolean equals(Object obj) {
        SelectionModel dt = (SelectionModel) obj;

        if (String.valueOf(dt.isClick).equals(String.valueOf(isClick))) return true;

        return false;
    }
}

