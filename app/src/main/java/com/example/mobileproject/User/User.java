package com.example.mobileproject.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.mobileproject.Cart.Cart;
import com.example.mobileproject.History.History;
import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.R;

public class User extends AppCompatActivity {
    TextView txtInfo, txtPhoneInfo;
    ImageButton btnViewCartU;
    Button btnUpdate, btnSignout, btnHistory;
    Toolbar toolbarUser;
    public static String name, phone, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txtInfo = findViewById(R.id.txtInfo);
        txtPhoneInfo = findViewById(R.id.txtPhoneInfo);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnSignout = findViewById(R.id.btnSignout);
        btnHistory = findViewById(R.id.btnHistory);
        toolbarUser = findViewById(R.id.toolbarUser);
        btnViewCartU = findViewById(R.id.btnViewCartU);

        setSupportActionBar(toolbarUser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(User.this, MainActivity.class);
                startActivity(main);
            }
        });

        btnViewCartU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewCart = new Intent(User.this, Cart.class);
                startActivity(viewCart);
            }
        });

        Intent userIntent = getIntent(); //Nhận từ SignIn
        String idUser = userIntent.getStringExtra("idUser");
        String username = userIntent.getStringExtra("usernameQuery");
        String password = userIntent.getStringExtra("passwordQuery");
        name = userIntent.getStringExtra("nameQuery");
        phone = userIntent.getStringExtra("phoneQuery");
        String email = userIntent.getStringExtra("emailQuery");
        address = userIntent.getStringExtra("addressQuery");

        if (name!= null && phone != null && address!= null){
            MainActivity.name = name;
            MainActivity.address = address;
            MainActivity.phone = phone;
            MainActivity.username = username;
            MainActivity.password = password;
            MainActivity.email = email;
        }

        Intent intent = getIntent(); //Nhận từ editUser
        if (intent.getStringExtra("name") != null && intent.getStringExtra("phone") != null){
            MainActivity.name = intent.getStringExtra("name");
            MainActivity.phone = intent.getStringExtra("phone");
        }
        txtInfo.setText("Xin chào " +MainActivity.name +"!");
        txtPhoneInfo.setText(MainActivity.phone);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editUserIntent = new Intent(User.this, EditUser.class);
                editUserIntent.putExtra("idUser", MainActivity.userId);
                editUserIntent.putExtra("username", MainActivity.username);
                editUserIntent.putExtra("password", MainActivity.password);
                editUserIntent.putExtra("name", MainActivity.name);
                editUserIntent.putExtra("phone", MainActivity.phone);
                editUserIntent.putExtra("email", MainActivity.email);
                editUserIntent.putExtra("address", MainActivity.address);
                startActivity(editUserIntent);
            }
        });

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn có muốn đăng xuất không?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        MainActivity.userId = null; //Hủy phiên đăng nhập
                        Intent signInIntent = new Intent(User.this, SignIn.class);
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

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewHistory = new Intent(User.this, History.class);
                viewHistory.putExtra("name", MainActivity.name);
                viewHistory.putExtra("userId", MainActivity.userId);
                startActivity(viewHistory);
            }
        });
    }
}