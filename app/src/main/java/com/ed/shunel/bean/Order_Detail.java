package com.ed.shunel.bean;

public class Order_Detail {

    //宣告區
    private int order_ID;
    private int order_Detail_Amount;
    private int Prount_ID;
    private int order_Detail_Buy_Price;
    private String color;
    private int imageId;

    public Order_Detail(int order_ID, int order_Detail_Amount, int prount_ID, int order_Detail_Buy_Price,
                        String color, int imageId) {
        super();
        this.order_ID = order_ID;
        this.order_Detail_Amount = order_Detail_Amount;
        this.Prount_ID = prount_ID;
        this.order_Detail_Buy_Price = order_Detail_Buy_Price;
        this.color = color;
        this.imageId = imageId;
    }
    //建構子


    public int getOrder_ID() {
        return order_ID;
    }

    public void setOrder_ID(int order_ID) {
        this.order_ID = order_ID;
    }

    public int getOrder_Detail_Amount() {
        return order_Detail_Amount;
    }

    public void setOrder_Detail_Amount(int order_Detail_Amount) {
        this.order_Detail_Amount = order_Detail_Amount;
    }

    public int getPrount_ID() {
        return Prount_ID;
    }

    public void setPrount_ID(int prount_ID) {
        Prount_ID = prount_ID;
    }

    public int getOrder_Detail_Buy_Price() {
        return order_Detail_Buy_Price;
    }

    public void setOrder_Detail_Buy_Price(int order_Detail_Buy_Price) {
        this.order_Detail_Buy_Price = order_Detail_Buy_Price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
