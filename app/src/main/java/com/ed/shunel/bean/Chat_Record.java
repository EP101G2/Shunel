package com.ed.shunel.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Chat_Record implements Serializable {

    int ID;
    int CHAT_NO;
    Timestamp CHAT_DATE;
    String CHAT_MSG;
    int FLAG;
    int CHAT_IMAGE;
    String CHAT_SENDER;
    String CHAT_RECRIVER;

    public Chat_Record(int ID, int CHAT_NO, Timestamp CHAT_DATE, String CHAT_MSG, int FLAG, int CHAT_IMAGE, String CHAT_SENDER, String CHAT_RECRIVER) {
        this.ID = ID;
        this.CHAT_NO = CHAT_NO;
        this.CHAT_DATE = CHAT_DATE;
        this.CHAT_MSG = CHAT_MSG;
        this.FLAG = FLAG;
        this.CHAT_IMAGE = CHAT_IMAGE;
        this.CHAT_SENDER = CHAT_SENDER;
        this.CHAT_RECRIVER = CHAT_RECRIVER;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCHAT_NO() {
        return CHAT_NO;
    }

    public void setCHAT_NO(int CHAT_NO) {
        this.CHAT_NO = CHAT_NO;
    }

    public Timestamp getCHAT_DATE() {
        return CHAT_DATE;
    }

    public void setCHAT_DATE(Timestamp CHAT_DATE) {
        this.CHAT_DATE = CHAT_DATE;
    }

    public String getCHAT_MSG() {
        return CHAT_MSG;
    }

    public void setCHAT_MSG(String CHAT_MSG) {
        this.CHAT_MSG = CHAT_MSG;
    }

    public int getFLAG() {
        return FLAG;
    }

    public void setFLAG(int FLAG) {
        this.FLAG = FLAG;
    }

    public int getCHAT_IMAGE() {
        return CHAT_IMAGE;
    }

    public void setCHAT_IMAGE(int CHAT_IMAGE) {
        this.CHAT_IMAGE = CHAT_IMAGE;
    }

    public String getCHAT_SENDER() {
        return CHAT_SENDER;
    }

    public void setCHAT_SENDER(String CHAT_SENDER) {
        this.CHAT_SENDER = CHAT_SENDER;
    }

    public String getCHAT_RECRIVER() {
        return CHAT_RECRIVER;
    }

    public void setCHAT_RECRIVER(String CHAT_RECRIVER) {
        this.CHAT_RECRIVER = CHAT_RECRIVER;
    }
}
