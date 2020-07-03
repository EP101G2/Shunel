package com.ed.shunel;

import java.io.Serializable;
import java.sql.Timestamp;

public class Product implements Serializable {


    private static final long serialVersionUID = 1L;
    //宣告區
    private int product_ID;
    private String product_Name;
    private String product_Color;
    private int product_Price;
    private String product_Ditail;
    private int product_Category_ID;
    private int product_Status;
    private Timestamp product_MODIFY_DATE;


    //建構區


    public Product(int product_ID, String product_Name, String product_Color, int product_Price, String product_Ditail, int product_Category_ID, int product_Status) {
        this.product_ID = product_ID;
        this.product_Name = product_Name;
        this.product_Color = product_Color;
        this.product_Price = product_Price;
        this.product_Ditail = product_Ditail;
        this.product_Category_ID = product_Category_ID;
        this.product_Status = product_Status;
    }

    public Product(int product_ID, String product_Name, String product_Color, int product_Price, String product_Ditail,
                   int product_Category_ID, int product_Status, Timestamp product_MODIFY_DATE) {
        super();
        this.product_ID = product_ID;
        this.product_Name = product_Name;
        this.product_Color = product_Color;
        this.product_Price = product_Price;
        this.product_Ditail = product_Ditail;
        this.product_Category_ID = product_Category_ID;
        this.product_Status = product_Status;
        this.product_MODIFY_DATE = product_MODIFY_DATE;
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


    public String getProduct_Color() {
        return product_Color;
    }


    public void setProduct_Color(String product_Color) {
        this.product_Color = product_Color;
    }


    public int getProduct_Price() {
        return product_Price;
    }


    public void setProduct_Price(int product_Price) {
        this.product_Price = product_Price;
    }


    public String getProduct_Ditail() {
        return product_Ditail;
    }


    public void setProduct_Ditail(String product_Ditail) {
        this.product_Ditail = product_Ditail;
    }


    public int getProduct_Category_ID() {
        return product_Category_ID;
    }


    public void setProduct_Category_ID(int product_Category_ID) {
        this.product_Category_ID = product_Category_ID;
    }


    public int getProduct_Status() {
        return product_Status;
    }


    public void setProduct_Status(int product_Status) {
        this.product_Status = product_Status;
    }


    public Timestamp getProduct_MODIFY_DATE() {
        return product_MODIFY_DATE;
    }


    public void setProduct_MODIFY_DATE(Timestamp product_MODIFY_DATE) {
        this.product_MODIFY_DATE = product_MODIFY_DATE;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }






}
