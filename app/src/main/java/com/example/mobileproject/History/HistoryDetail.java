package com.example.mobileproject.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.Product.ProductModel;
import com.example.mobileproject.R;
import com.example.mobileproject.User.SignIn;
import com.example.mobileproject.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryDetail extends AppCompatActivity {
    HistoryDetailAdapter historyDetailAdapter;
    ListView listItemDetail;
    TextView txtInfoHD, orderIdHD;
    Toolbar toolbarHistoryDetail;
    TextView btnSignoutHD, txtNotedProgressHD;
    ArrayList<OrderHistoryModel> itemPurchaseArr;
    int itemQuantity;
    String proId;
    ProgressBar progressBarHD;
    int progressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        listItemDetail = findViewById(R.id.listItemDetail);
        txtInfoHD = findViewById(R.id.txtInfoHD);
        toolbarHistoryDetail = findViewById(R.id.toolbarHistoryDetail);
        btnSignoutHD = findViewById(R.id.btnSignoutHD);
        progressBarHD = findViewById(R.id.progressBarHD);
        txtNotedProgressHD = findViewById(R.id.txtNotedProgressHD);
        orderIdHD = findViewById(R.id.orderIdHD);

        Intent historyDetailAct = getIntent(); //Nhận từ History
        String orderId = historyDetailAct.getStringExtra("orderId");
        orderIdHD.setText("DH#"+orderId);
        txtInfoHD.setText(User.name);

        setSupportActionBar(toolbarHistoryDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarHistoryDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignoutHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryDetail.this);
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn có muốn đăng xuất không?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        MainActivity.userId = null; //Hủy phiên đăng nhập
                        Intent signInIntent = new Intent(HistoryDetail.this, SignIn.class);
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        Query query = databaseReference.orderByChild("orderId").equalTo(orderId);
        itemPurchaseArr = new ArrayList<>();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderQuery : snapshot.getChildren()) {
                    OrderHistoryModel order = orderQuery.getValue(OrderHistoryModel.class);
                    proId = order.getProId();
                    itemQuantity = order.getProQuantity();

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Products");
                    Query query2 = databaseReference2.orderByChild("proId").equalTo(proId);
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot proQuery : snapshot.getChildren()) {
                                ProductModel pro = proQuery.getValue(ProductModel.class);
                                itemPurchaseArr.add(new OrderHistoryModel(pro.getProImg(), pro.getProName(), pro.getProPrice(), itemQuantity));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 25) {
                    progressStatus++;
                    SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBarHD.setProgress(progressStatus);
                        }
                    });
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBarHD.setVisibility(View.INVISIBLE);
                        txtNotedProgressHD.setVisibility(View.INVISIBLE);
                        historyDetailAdapter = new HistoryDetailAdapter(HistoryDetail.this, itemPurchaseArr);
                        listItemDetail.setAdapter(historyDetailAdapter);
                    }
                });
            }
        }).start();
    }
}