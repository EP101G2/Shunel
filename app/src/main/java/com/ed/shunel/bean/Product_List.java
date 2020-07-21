package com.ed.shunel.bean;

import com.ed.shunel.Product;

import java.io.Serializable;
import java.util.List;

public class Product_List implements Serializable {

    private List<Product> cart;

    public Product_List(List<Product> cart) {
        this.cart = cart;
    }

    public List<Product> getCart() {
        return cart;
    }

    public void setCart(List<Product> cart) {
        this.cart = cart;
    }
}
