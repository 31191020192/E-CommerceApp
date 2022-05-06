package com.example.mobileproject.History;

import java.io.Serializable;

public class OrderInfoUserModel implements Serializable {
    private String orderId;
    private String time;
    private String total;
    private String orderReceiver;
    private String orderAddress;

    public OrderInfoUserModel(String orderId, String orderReceiver, String orderAddress, String total, String time){
        this.orderId = orderId;
        this.orderReceiver = orderReceiver;
        this.orderAddress = orderAddress;
        this.total = total;
        this.time = time;
    }

    public OrderInfoUserModel(){

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrderReceiver() {
        return orderReceiver;
    }

    public void setOrderReceiver(String orderReceiver) {
        this.orderReceiver = orderReceiver;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }
}
