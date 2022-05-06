package com.example.mobileproject.Cart;

import java.io.Serializable;

public class CartModel implements Serializable {
    private String proId;
    private String proName;
    private String proImg;
    private String proPrice;
    private int proQuantity;

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }

    public CartModel(String proId, String proName, String proImg, String proPrice, int proQuantity){
        this.proId = proId;
        this.proName = proName;
        this.proImg = proImg;
        this.proPrice = proPrice;
        this.proQuantity = proQuantity;
    }

    public int getProQuantity() {
        return proQuantity;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }
}
