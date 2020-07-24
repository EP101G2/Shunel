package com.ed.shunel;

import java.io.Serializable;

public class Orders implements Serializable {
//    private String Account_ID;
    private int orderId;
    public static int orderStatus;
    private int imageId;

    public Orders(int orderId, int orderStatus, int imageId){
//        this.Account_ID = Account_ID;
        this.imageId = imageId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

//    public String getAccount_ID() {
//        return Account_ID;
//    }
//
//    public void setAccount_ID(String account_ID) {
//        Account_ID = account_ID;
//    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;//made static for inner method to use?
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
