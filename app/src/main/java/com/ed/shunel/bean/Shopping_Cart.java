package com.ed.shunel.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Shopping_Cart implements Serializable{

    // 宣告區
    private String account_ID; // PK FK
    private int product_ID; // PK FK
    private String product_Name;
    private int amount;
    private String color;
    private int price;
    private Timestamp MODIFY_DATE;

    public Shopping_Cart(String account_ID, int product_ID, String product_Name, int amount, String color, int price,
                         Timestamp mODIFY_DATE) {
        super();
        this.account_ID = account_ID;
        this.product_ID = product_ID;
        this.product_Name = product_Name;
        this.amount = amount;
        this.color = color;
        this.price = price;
        MODIFY_DATE = mODIFY_DATE;
    }

    public String getAccount_ID() {
        return account_ID;
    }

    public void setAccount_ID(String account_ID) {
        this.account_ID = account_ID;
    }

    public int getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(int product_ID) {
        this.product_ID = product_ID;
    }

    public String getProduct_Name() {
        return product_Name;
    }

    public void setProduct_Name(String product_Name) {
        this.product_Name = product_Name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getMODIFY_DATE() {
        return MODIFY_DATE;
    }

    public void setMODIFY_DATE(Timestamp mODIFY_DATE) {
        MODIFY_DATE = mODIFY_DATE;
    }
}
