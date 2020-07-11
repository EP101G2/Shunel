package com.ed.shunel.bean;

import java.sql.Timestamp;

public class Order_Main {

    //宣告區
    private int Order_ID; //PK
    private String Account_ID; //FK
    private int Order_Main_Total_Price;
    private String Order_Main_Recriver;
    private String Order_Main_Address;
    private String order_Main_Phone;
    private Timestamp Order_Main_Order_Date;
    private int Order_Main_Order_Status;
    private Timestamp Order_Main_Modify_Date;

//    public Order_Main(int order_ID, String account_ID, int order_Main_Total_Price, String order_Main_Recriver, String order_Main_Address, String order_Main_Phone, int order_Main_Order_Status) {
//        Order_ID = order_ID;
//        Account_ID = account_ID;
//        Order_Main_Total_Price = order_Main_Total_Price;
//        Order_Main_Recriver = order_Main_Recriver;
//        Order_Main_Address = order_Main_Address;
//        this.order_Main_Phone = order_Main_Phone;
//        Order_Main_Order_Status = order_Main_Order_Status;
//    }
public Order_Main( String account_ID, int order_Main_Total_Price, String order_Main_Recriver, String order_Main_Address, String order_Main_Phone, int order_Main_Order_Status) {
//        Order_ID = order_ID;
        Account_ID = account_ID;
        Order_Main_Total_Price = order_Main_Total_Price;
        Order_Main_Recriver = order_Main_Recriver;
        Order_Main_Address = order_Main_Address;
        this.order_Main_Phone = order_Main_Phone;
        Order_Main_Order_Status = order_Main_Order_Status;
    }

    //    Order_Main(int order_ID, String account_ID, int order_Main_Total_Price, String order_Main_Recriver,
//               String order_Main_Address, String order_Main_Phone, Timestamp order_Main_Order_Date,
//               int order_Main_Order_Status, Timestamp order_Main_Modify_Date) {
//        super();
//        Order_ID = order_ID;
//        Account_ID = account_ID;
//        Order_Main_Total_Price = order_Main_Total_Price;
//        Order_Main_Recriver = order_Main_Recriver;
//        Order_Main_Address = order_Main_Address;
//        this.order_Main_Phone = order_Main_Phone;
//        Order_Main_Order_Date = order_Main_Order_Date;
//        Order_Main_Order_Status = order_Main_Order_Status;
//        Order_Main_Modify_Date = order_Main_Modify_Date;
//    }



    public int getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(int order_ID) {
        Order_ID = order_ID;
    }

    public String getAccount_ID() {
        return Account_ID;
    }
    public void setAccount_ID(String account_ID) {
        Account_ID = account_ID;
    }

    public int getOrder_Main_Total_Price() {
        return Order_Main_Total_Price;
    }

    public void setOrder_Main_Total_Price(int order_Main_Total_Price) {
        Order_Main_Total_Price = order_Main_Total_Price;
    }

    public String getOrder_Main_Recriver() {
        return Order_Main_Recriver;
    }

    public void setOrder_Main_Recriver(String order_Main_Recriver) {
        Order_Main_Recriver = order_Main_Recriver;
    }

    public String getOrder_Main_Address() {
        return Order_Main_Address;
    }

    public void setOrder_Main_Address(String order_Main_Address) {
        Order_Main_Address = order_Main_Address;
    }

    public String getOrder_Main_Phone() {
        return order_Main_Phone;
    }

    public void setOrder_Main_Phone(String order_Main_Phone) {
        this.order_Main_Phone = order_Main_Phone;
    }

    public Timestamp getOrder_Main_Order_Date() {
        return Order_Main_Order_Date;
    }

    public void setOrder_Main_Order_Date(Timestamp order_Main_Order_Date) {
        Order_Main_Order_Date = order_Main_Order_Date;
    }

    public int getOrder_Main_Order_Status() {
        return Order_Main_Order_Status;
    }

    public void setOrder_Main_Order_Status(int order_Main_Order_Status) {
        Order_Main_Order_Status = order_Main_Order_Status;
    }

    public Timestamp getOrder_Main_Modify_Date() {
        return Order_Main_Modify_Date;
    }

    public void setOrder_Main_Modify_Date(Timestamp order_Main_Modify_Date) {
        Order_Main_Modify_Date = order_Main_Modify_Date;
    }
}
