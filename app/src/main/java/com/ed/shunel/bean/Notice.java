package com.ed.shunel.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice implements Serializable {
    private int Notice_ID;
    private String Notice_Content;
    private String Notice_Title;
    private Timestamp Notice_time;
    private int NOTICE_CATEGORY_ID;
    private int CATEGORY_MESSAGE_ID;


    public Notice(int notice_ID, String notice_Content, String notice_Title, Timestamp notice_time, int NOTICE_CATEGORY_ID, int CATEGORY_MESSAGE_ID) {
        Notice_ID = notice_ID;
        Notice_Content = notice_Content;
        Notice_Title = notice_Title;
        Notice_time = notice_time;
        this.NOTICE_CATEGORY_ID = NOTICE_CATEGORY_ID;
        this.CATEGORY_MESSAGE_ID = CATEGORY_MESSAGE_ID;
    }

    public int getNotice_ID() {
        return Notice_ID;
    }

    public void setNotice_ID(int notice_ID) {
        Notice_ID = notice_ID;
    }

    public String getNotice_Title() {
        return Notice_Title;
    }

    public void setNotice_Title(String notice_Title) {
        Notice_Title = notice_Title;
    }

    public String getNotice_Content() {
        return Notice_Content;
    }

    public void setNotice_Content(String notice_Content) {
        Notice_Content = notice_Content;
    }

    public Timestamp getNotice_time() {
        return Notice_time;
    }

    public void setNotice_time(Timestamp notice_time) {
        Notice_time = notice_time;
    }

    public int getNOTICE_CATEGORY_ID() {
        return NOTICE_CATEGORY_ID;
    }

    public void setNOTICE_CATEGORY_ID(int NOTICE_CATEGORY_ID) {
        this.NOTICE_CATEGORY_ID = NOTICE_CATEGORY_ID;
    }

    public int getCATEGORY_MESSAGE_ID() {
        return CATEGORY_MESSAGE_ID;
    }

    public void setCATEGORY_MESSAGE_ID(int CATEGORY_MESSAGE_ID) {
        this.CATEGORY_MESSAGE_ID = CATEGORY_MESSAGE_ID;
    }
}
