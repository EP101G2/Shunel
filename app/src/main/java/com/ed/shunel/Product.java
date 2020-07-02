package com.ed.shunel;

import java.io.Serializable;
import java.sql.Timestamp;

public class Product implements Serializable {


    private int prouct_ID;
    private String prouct_Name;
    private String prouct_Color;
    private int prouct_Price;
    private String prouct_Ditail;
    private int prouct_Category_ID;
    private int prouct_Status;
    private Timestamp prouct_MODIFY_DATE;

    private int img;
    //建構區


    public int getImg() {
        return img;
    }

    public Product(String prouct_Name, int prouct_Price, int img) {
        this.prouct_Name = prouct_Name;
        this.prouct_Price = prouct_Price;
        this.img = img;
    }



    public Product(int prouct_ID, String prouct_Name, String prouct_Color, int prouct_Price, String prouct_Ditail,
                   int prouct_Category_ID, int prouct_Status, Timestamp prouct_MODIFY_DATE) {
        super();
        this.prouct_ID = prouct_ID;
        this.prouct_Name = prouct_Name;
        this.prouct_Color = prouct_Color;
        this.prouct_Price = prouct_Price;
        this.prouct_Ditail = prouct_Ditail;
        this.prouct_Category_ID = prouct_Category_ID;
        this.prouct_Status = prouct_Status;
        this.prouct_MODIFY_DATE = prouct_MODIFY_DATE;
    }



    public Product(int prouct_ID) {
        super();
        this.prouct_ID = prouct_ID;
    }



    //方法區
    public int getProduct_ID() {
        return prouct_ID;
    }

    public void setProduct_ID(int prouct_ID) {
        this.prouct_ID = prouct_ID;
    }

    public String getProduct_Name() {
        return prouct_Name;
    }

    public void setProduct_Name(String prouct_Name) {
        this.prouct_Name = prouct_Name;
    }

    public String getProduct_Color() {
        return prouct_Color;
    }

    public void setProduct_Color(String prouct_Color) {
        this.prouct_Color = prouct_Color;
    }

    public int getProduct_Price() {
        return prouct_Price;
    }

    public void setProduct_Price(int prouct_Price) {
        this.prouct_Price = prouct_Price;
    }

    public String getProduct_Ditail() {
        return prouct_Ditail;
    }

    public void setProduct_Ditail(String prouct_Ditail) {
        this.prouct_Ditail = prouct_Ditail;
    }

    public int getProduct_Category_ID() {
        return prouct_Category_ID;
    }

    public void setProduct_Category_ID(int prouct_Category_ID) {
        this.prouct_Category_ID = prouct_Category_ID;
    }

    public int getProduct_Status() {
        return prouct_Status;
    }

    public void setProduct_Status(int prouct_Status) {
        this.prouct_Status = prouct_Status;
    }

    public Timestamp getProduct_MODIFY_DATE() {
        return prouct_MODIFY_DATE;
    }

    public void setProduct_MODIFY_DATE(Timestamp prouct_MODIFY_DATE) {
        this.prouct_MODIFY_DATE = prouct_MODIFY_DATE;
    }

}
