package com.ed.shunel.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Shopping_Cart implements Serializable {

    //宣告區
    private int Account_ID; //PK FK
    private int Product_ID; //PK FK
    private int Shopping_Cart_Amount;
    private Timestamp Shopping_Cart_Modify_Date;


    public Shopping_Cart(int account_ID, int product_ID, int shopping_Cart_Amount) {
        Account_ID = account_ID;
        Product_ID = product_ID;
        Shopping_Cart_Amount = shopping_Cart_Amount;
    }

    public Shopping_Cart(int account_ID, int product_ID, int shopping_Cart_Amount, Timestamp shopping_Cart_Modify_Date) {
        Account_ID = account_ID;
        Product_ID = product_ID;
        Shopping_Cart_Amount = shopping_Cart_Amount;
        Shopping_Cart_Modify_Date = shopping_Cart_Modify_Date;
    }

    public int getAccount_ID() {
        return Account_ID;
    }

    public void setAccount_ID(int account_ID) {
        Account_ID = account_ID;
    }

    public int getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(int product_ID) {
        Product_ID = product_ID;
    }

    public int getShopping_Cart_Amount() {
        return Shopping_Cart_Amount;
    }

    public void setShopping_Cart_Amount(int shopping_Cart_Amount) {
        Shopping_Cart_Amount = shopping_Cart_Amount;
    }

    public Timestamp getShopping_Cart_Modify_Date() {
        return Shopping_Cart_Modify_Date;
    }

    public void setShopping_Cart_Modify_Date(Timestamp shopping_Cart_Modify_Date) {
        Shopping_Cart_Modify_Date = shopping_Cart_Modify_Date;
    }
}
