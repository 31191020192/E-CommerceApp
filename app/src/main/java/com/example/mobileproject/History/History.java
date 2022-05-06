package com.example.mobileproject.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.User.SignIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    HistoryAdapter historyAdapter;
    ListView listItemPurchase;
    TextView txtInfoH, txtNotedProgressHis;
    Toolbar toolbarHistory;
    TextView btnSignoutH;
    ArrayList<OrderInfoUserModel> orderArr = new ArrayList<>(); //Mảng để hiển thị item order
    ArrayList<String> orderOfUserArr; //Mảng lưu tất cả id order của 1 KH
    boolean flag;
    ProgressBar progressBar;
    int progressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent viewHistory = getIntent(); //Nhận từ User Activity
        String name = viewHistory.getStringExtra("name");
        String userId = viewHistory.getStringExtra("userId");

        listItemPurchase = findViewById(R.id.listItemPurchase);
        txtInfoH = findViewById(R.id.txtInfoH);
        progressBar = findViewById(R.id.progressBar);
        toolbarHistory = findViewById(R.id.toolbarHistory);
        txtNotedProgressHis = findViewById(R.id.txtNotedProgressHis);

        setSupportActionBar(toolbarHistory);
        btnSignoutH = findViewById(R.id.btnSignoutH);

        txtInfoH.setText(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarHistory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignoutH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn có muốn đăng xuất không?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        MainActivity.userId = null; //Hủy phiên đăng nhập
                        Intent signInIntent = new Intent(History.this, SignIn.class);
                        startActivity(signInIntent);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference("Order");
        DatabaseReference dDatabase2 = FirebaseDatabase.getInstance().getReference("OrderInfoUser");
        Query queryOrderId = dDatabase.orderByChild("userId").equalTo(userId);
        queryOrderId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderOfUserArr = new ArrayList<>();
                for (DataSnapshot orderQuery : snapshot.getChildren()) {
                    OrderHistoryModel order = orderQuery.getValue(OrderHistoryModel.class);
                    if (order != null) {
                        if (orderOfUserArr.contains(order.getOrderId()) == false) {
                            orderOfUserArr.add(order.getOrderId());
                            for (String i : orderOfUserArr) {
                                Query queryOrder = dDatabase2.orderByChild("orderId").equalTo(i);
                                queryOrder.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot orderItem : snapshot.getChildren()) {
                                            OrderInfoUserModel orderInfo = orderItem.getValue(OrderInfoUserModel.class);
                                            for (OrderInfoUserModel item : orderArr) {
                                                if (item.getOrderId() == orderInfo.getOrderId()) {
                                                    flag = true; //Biến flag để nhận biết mã đơn hàng tồn tại trong mảng chưa
                                                }
                                            }
                                            if (flag == false) {
                                                orderArr.add(new OrderInfoUserModel(orderInfo.getOrderId(), orderInfo.getOrderReceiver(),
                                                        orderInfo.getOrderAddress(), orderInfo.getTotal(), orderInfo.getTime()));
                                            }
                                            flag = false;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 45) {
                    progressStatus++;
                    SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        txtNotedProgressHis.setVisibility(View.INVISIBLE);
                        historyAdapter = new HistoryAdapter(History.this, orderArr);
                        listItemPurchase.setAdapter(historyAdapter);

                        listItemPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent historyDetailAct = new Intent(History.this, HistoryDetail.class);
                                String orderId = getOrderIdFromArr(orderArr, position);
                                historyDetailAct.putExtra("orderId", orderId);
                                startActivity(historyDetailAct);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    //Tìm vị trí của mã đơn hàng trong mảng đơn hàng
    private String getOrderIdFromArr(ArrayList<OrderInfoUserModel> orderArr, int position) {
        int count = 0;
        String orderId = "";
        for (OrderInfoUserModel i : orderArr) {
            if (count == position) {
                orderId = i.getOrderId();
                break;
            }
            count++;
        }
        return orderId;
    }
}

