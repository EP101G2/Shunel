package com.ed.shunel;

import com.ed.shunel.bean.Shopping_Cart;

import java.io.Serializable;
import java.util.List;

public class Shopping_Cart_List implements Serializable {

    private List<Shopping_Cart> cart;


    public Shopping_Cart_List(List<Shopping_Cart> cart) {
        this.cart = cart;
    }

    public List<Shopping_Cart> getCart() {
        return cart;
    }

    public void setCart(List<Shopping_Cart> cart) {
        this.cart = cart;
    }
}
