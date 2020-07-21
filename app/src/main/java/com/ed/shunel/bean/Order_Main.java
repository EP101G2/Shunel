package com.ed.shunel.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Order_Main implements Serializable {

    //宣告區
    private int order_ID; //PK
    private String account_ID; //FK
    private int order_Main_Total_Price;
    private String order_Main_Receiver;
    private String order_Main_Address;
    private String order_Main_Phone;
    private Timestamp Order_Main_Order_Date;
    private int order_Main_Order_Status;
    private Timestamp Order_Main_Modify_Date;

public Order_Main(int order_ID, String account_ID, int order_Main_Total_Price, String order_Main_Receiver, String order_Main_Address, String order_Main_Phone,Timestamp order_Main_Order_Date, int order_Main_Order_Status, Timestamp order_Main_Modify_Date) {
    super();
    this.order_ID = order_ID;
    this.account_ID = account_ID;
    this.order_Main_Total_Price = order_Main_Total_Price;
    this.order_Main_Receiver = order_Main_Receiver;
    this.order_Main_Address = order_Main_Address;
    this.order_Main_Phone = order_Main_Phone;
    this.Order_Main_Order_Date = order_Main_Order_Date;
    this.order_Main_Order_Status = order_Main_Order_Status;
    this.Order_Main_Modify_Date = order_Main_Modify_Date;
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
        return order_ID;
    }

    public void setOrder_ID(int order_ID) {
        this.order_ID = order_ID;
    }

    public String getAccount_ID() {
        return account_ID;
    }
    public void setAccount_ID(String account_ID) {
        this.account_ID = account_ID;
    }

    public int getOrder_Main_Total_Price() {
        return order_Main_Total_Price;
    }

    public void setOrder_Main_Total_Price(int order_Main_Total_Price) {
        this.order_Main_Total_Price = order_Main_Total_Price;
    }

    public String getOrder_Main_Receiver() {
        return order_Main_Receiver;
    }

    public void setOrder_Main_Receiver(String order_Main_Receiver) {
        this.order_Main_Receiver = order_Main_Receiver;
    }

    public String getOrder_Main_Address() {
        return order_Main_Address;
    }

    public void setOrder_Main_Address(String order_Main_Address) {
        this.order_Main_Address = order_Main_Address;
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
        return order_Main_Order_Status;
    }

    public void setOrder_Main_Order_Status(int order_Main_Order_Status) {
        this.order_Main_Order_Status = order_Main_Order_Status;
    }

    public Timestamp getOrder_Main_Modify_Date() {
        return Order_Main_Modify_Date;
    }

    public void setOrder_Main_Modify_Date(Timestamp order_Main_Modify_Date) {
        Order_Main_Modify_Date = order_Main_Modify_Date;
    }
}
