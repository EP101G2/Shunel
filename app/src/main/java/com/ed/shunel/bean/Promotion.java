package com.ed.shunel.bean;

import java.io.Serializable;
import java.sql.Timestamp;

    public class Promotion implements Serializable {

        private int Promotion_ID;
        private String product_Name;
        private int Product_ID;
        private int Promotion_Price;
        private int Product_Price;
        private String color;
        private String dital;
        private Timestamp Date_Start;
        private Timestamp Date_End;

        // 促銷消息使用，不包含原先價格
        public Promotion(int promotion_ID, String product_Name, int product_ID, int promotion_Price, Timestamp date_Start,
                         Timestamp date_End) {
            super();
            Promotion_ID = promotion_ID;
            this.product_Name = product_Name;
            Product_ID = product_ID;
            Promotion_Price = promotion_Price;
            Date_Start = date_Start;
            Date_End = date_End;
        }

        public Promotion(int promotion_ID, String product_Name, int product_ID, int promotion_Price, int product_Price,
                         Timestamp date_Start, Timestamp date_End) {
            super();
            Promotion_ID = promotion_ID;
            this.product_Name = product_Name;
            Product_ID = product_ID;
            Promotion_Price = promotion_Price;
            Product_Price = product_Price;
            Date_Start = date_Start;
            Date_End = date_End;
        }

        public Promotion(int promotion_ID, int product_ID, int product_Price, Timestamp date_Start,Timestamp date_End) {
            super();
            Promotion_ID = promotion_ID;
            Product_ID = product_ID;
            Product_Price = product_Price;
            Date_Start = date_Start;
            Date_End = date_End;
        }




        public Promotion(int promotion_ID, String product_Name, int product_ID, int promotion_Price, int product_Price,
                         String color, String dital, Timestamp date_Start, Timestamp date_End) {
            super();
            Promotion_ID = promotion_ID;
            this.product_Name = product_Name;
            Product_ID = product_ID;
            Promotion_Price = promotion_Price;
            Product_Price = product_Price;
            this.color = color;
            this.dital = dital;
            Date_Start = date_Start;
            Date_End = date_End;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getDital() {
            return dital;
        }

        public void setDital(String dital) {
            this.dital = dital;
        }

        public String getProduct_Name() {
            return product_Name;
        }

        public void setProduct_Name(String product_Name) {
            this.product_Name = product_Name;
        }

        public int getProduct_ID() {
            return Product_ID;
        }

        public void setProduct_ID(int product_ID) {
            Product_ID = product_ID;
        }

        public int getProduct_Price() {
            return Product_Price;
        }

        public void setProduct_Price(int product_Price) {
            Product_Price = product_Price;
        }

        public int getPromotion_ID() {
            return Promotion_ID;
        }

        public void setPromotion_ID(int promotion_ID) {
            Promotion_ID = promotion_ID;
        }

        public String getPromotion_Name() {
            return product_Name;
        }

        public void setPromotion_Name(String promotion_Name) {
            product_Name = promotion_Name;
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

        public Timestamp getDate_Start() {
            return Date_Start;
        }

        public void setDate_Start(Timestamp promotion_Date_Start) {
            Date_Start = Date_Start;
        }

        public Timestamp getDate_End() {
            return Date_End;
        }

        public void setDate_End(Timestamp Date_End) {
            Date_End = Date_End;
        }


    }