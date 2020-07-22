package com.ed.shunel;

public class OrderDetail {
    private int orderId;
    private int orderStatus;
    private int totalPrice;
    private String receiver;
    private String phone;
    private String address;
    private int productId;
    private String productName;
    private int buyPrice;
    private int productImageId;



    public OrderDetail(int orderId, int orderStatus, int totalPrice, String address, String phone, String receiver, int productId, String productName, int buyPrice, int productImageId){
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.receiver = receiver;
        this.phone = phone;
        this.address = address;
        this.productId = productId;
        this.productName = productName;
        this.buyPrice = buyPrice;
        this.productImageId = productImageId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getProductImageId() {
        return productImageId;
    }

    public void setProductImageId(int productImageId) {
        this.productImageId = productImageId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
