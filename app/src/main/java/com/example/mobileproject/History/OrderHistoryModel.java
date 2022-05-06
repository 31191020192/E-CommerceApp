package com.example.mobileproject.History;

import java.io.Serializable;

public class OrderHistoryModel implements Serializable {
    private String orderId;
    private String proId;
    private int proQuantity;
    private String userId;
    private String time;
    private String proImg;
    private String proName;
    private String proPrice;

    public OrderHistoryModel(String orderId, String proId,int proQuantity, String userId, String time) {
        this.orderId = orderId;
        this.proId = proId;
        this.proQuantity = proQuantity;
        this.userId = userId;
        this.time = time;
    }

    public OrderHistoryModel(String proImg, String proName, String proPrice, int proQuantity){
        this.proImg = proImg;
        this.proName = proName;
        this.proPrice = proPrice;
        this.proQuantity = proQuantity;
    }

    public OrderHistoryModel(){

    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public int getProQuantity() {
        return proQuantity;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String currentTime) {
        this.time = currentTime;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
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
}
