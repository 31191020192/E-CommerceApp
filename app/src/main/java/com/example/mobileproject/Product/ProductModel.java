package com.example.mobileproject.Product;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String proId;
    private String proName;
    private String proPrice;
    private String proImg;
    private String proDes;
    private String proRate;
    private String proCate;

    public ProductModel(){

    }

    public ProductModel(String proId, String proName, String proPrice, String proImg, String proCate){
        this.proId = proId;
        this.proName = proName;
        this.proPrice = proPrice;
        this.proImg = proImg;
        this.proCate = proCate;
    }
    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public String getProDes() {
        return proDes;
    }

    public void setProDes(String proDes) {
        this.proDes = proDes;
    }

    public String getProRate() {
        return proRate;
    }

    public void setProRate(String proRate) {
        this.proRate = proRate;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProCate() {
        return proCate;
    }

    public void setProCate(String proCate) {
        this.proCate = proCate;
    }
}
