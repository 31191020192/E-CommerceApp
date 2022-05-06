package com.example.mobileproject.History;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.mobileproject.Cart.Cart;
import com.example.mobileproject.Cart.CartModel;
import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderHistory {
    ArrayList<String> orderIdArr = new ArrayList<>();

    public void Order(ArrayList<CartModel> arrCart, Context context) {
        DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference("Order");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = now + "";
        String resCurrent = stringDateProcess(time);

        DatabaseReference dDatabase2 = FirebaseDatabase.getInstance().getReference("OrderInfoUser");
        dDatabase2.orderByChild("orderId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot listOrderId : snapshot.getChildren()){
                    OrderInfoUserModel order = listOrderId.getValue(OrderInfoUserModel.class);
                    if (orderIdArr.contains(order.getOrderId()) == false){
                        orderIdArr.add(order.getOrderId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Đặt hàng thành công");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                int random_orderId;
                do {
                    random_orderId = (int)Math.floor(Math.random()*(300000-100000+1)+100000);
                }
                while (orderIdArr.contains(random_orderId));
                for (CartModel listCart : arrCart) {
                    String orderId = dDatabase.push().getKey();
                    OrderHistoryModel order = new OrderHistoryModel(random_orderId + "", listCart.getProId(), listCart.getProQuantity(), MainActivity.userId, resCurrent);
                    dDatabase.child(orderId).setValue(order);
                }

                String orderTotalId = dDatabase2.push().getKey();
                OrderInfoUserModel order = new OrderInfoUserModel(random_orderId + "", User.name, User.address, Cart.total+ "", resCurrent);
                dDatabase2.child(orderTotalId).setValue(order);
            }
        });
        builder.show();

    }

    //Xử lý định dạng ngày tháng
    private String stringDateProcess(String current) {
        String res = current.substring(0,10) + " " + current.substring(11,19);
        return res;
    }
}