package com.ed.shunel.bean;

import java.io.Serializable;
import java.sql.Timestamp;

    public class Promotion implements Serializable {

        private int Promotion_ID;
        private String Promotion_Name;
        private int Product_ID;
        private int Promotion_Price;
        private Timestamp Promotion_Date_Start;
        private Timestamp Promotion_Date_End;


//        public Promotion(int promotion_ID, String promotion_Name, int prouct_ID, int promotion_Price, Timestamp promotion_Date_Start, Timestamp promotion_Date_End) {
//            Promotion_ID = promotion_ID;
//            Promotion_Name = promotion_Name;
//            Prouct_ID = prouct_ID;
//            Promotion_Price = promotion_Price;
//            Promotion_Date_Start = promotion_Date_Start;
//            Promotion_Date_End = promotion_Date_End;
//        }


        public Promotion(int promotion_ID, String promotion_Name, int prouct_ID, int promotion_Price) {
            Promotion_ID = promotion_ID;
            Promotion_Name = promotion_Name;
            Product_ID = prouct_ID;
            Promotion_Price = promotion_Price;
        }

        public int getPromotion_ID() {
            return Promotion_ID;
        }

        public void setPromotion_ID(int promotion_ID) {
            Promotion_ID = promotion_ID;
        }

        public String getPromotion_Name() {
            return Promotion_Name;
        }

        public void setPromotion_Name(String promotion_Name) {
            Promotion_Name = promotion_Name;
        }

        public int getProuct_ID() {
            return Product_ID;
        }

        public void setProuct_ID(int prouct_ID) {
            Product_ID = prouct_ID;
        }

        public int getPromotion_Price() {
            return Promotion_Price;
        }

        public void setPromotion_Price(int promotion_Price) {
            Promotion_Price = promotion_Price;
        }

        public Timestamp getPromotion_Date_Start() {
            return Promotion_Date_Start;
        }

        public void setPromotion_Date_Start(Timestamp promotion_Date_Start) {
            Promotion_Date_Start = promotion_Date_Start;
        }

        public Timestamp getPromotion_Date_End() {
            return Promotion_Date_End;
        }

        public void setPromotion_Date_End(Timestamp promotion_Date_End) {
            Promotion_Date_End = promotion_Date_End;
        }
    }