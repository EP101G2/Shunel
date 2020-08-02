package com.ed.shunel.bean;

import android.graphics.Bitmap;

import java.util.Date;

public class ChatMessage {

    private String type;
    private String sender;
    private String receiver;
    private String message;
    private int chatRoom;
    private String read;
    private String base64;
    private Date date;

    private int id;


    public ChatMessage(String type, String sender, String receiver, String message, int chatRoom) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatRoom = chatRoom;
    }

    public ChatMessage(String type, String sender, String receiver, String message, int chatRoom, int id) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatRoom = chatRoom;
        this.id = id;
    }


    //    public ChatMessage(String sender, String receiver, String message, int chatRoom) {
//        this.sender = sender;
//        this.receiver = receiver;
//        this.message = message;
//        this.chatRoom = chatRoom;
//    }

//    public ChatMessage(String type, String sender, String receiver, String message, int chatRoom) {
//        this.type = type;
//        this.sender = sender;
//        this.receiver = receiver;
//        this.message = message;
//        this.chatRoom = chatRoom;
//    }
//
//
//    public ChatMessage(String type, String sender, String receiver, String message, int chatRoom, String read,
//                       String base64, Date date) {
//        super();
//        this.type = type;
//        this.sender = sender;
//        this.receiver = receiver;
//        this.message = message;
//        this.chatRoom = chatRoom;
//        this.read = read;
//        this.base64 = base64;
//        this.date = date;
//    }




    public ChatMessage() {
        super();
    }




    public String getRead() {
        return read;
    }




    public void setRead(String read) {
        this.read = read;
    }




    public int getChatRoom() {
        return chatRoom;
    }




    public void setChatRoom(int chatRoom) {
        this.chatRoom = chatRoom;
    }








    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBase64() {
        return base64;
    }


    public void setBase64(String base64) {
        this.base64 = base64;
    }


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }
}
