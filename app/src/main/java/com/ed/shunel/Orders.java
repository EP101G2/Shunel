package com.ed.shunel;

public class Orders {
    private int orderId;
    private static int orderStatus;
    private int imageId;

    public Orders(int imageId, int orderId, int orderStatus){
        this.imageId = imageId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

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
