package com.example.mobileproject.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {
    EditText txtUsernameSignin, txtPasswordSignin;
    TextView btnBackHome;
    Button btnLogin, btnSignup;
    String usernameQuery, passwordQuery, nameQuery, phoneQuery, emailQuery, addressQuery, idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtUsernameSignin = findViewById(R.id.txtUsernameSignin);
        txtPasswordSignin = findViewById(R.id.txtPasswordSignin);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnBackHome = findViewById(R.id.btnBackHome);
        DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference("User");

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeAct = new Intent(SignIn.this, MainActivity.class);
                startActivity(homeAct);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsernameSignin.getText().toString().trim();
                String password = txtPasswordSignin.getText().toString().trim();
                if(username.isEmpty()){
                    txtUsernameSignin.setError("Nhập tài khoản");
                }
                else {
                    if (password.isEmpty()){
                        txtPasswordSignin.setError("Nhập mật khẩu");
                    }
                    else {
                        Query query = dDatabase.orderByChild("userName").equalTo(username);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, HashMap> snapValue = (HashMap<String, HashMap>) snapshot.getValue();
                                if (snapValue != null){
                                    for (String key : snapValue.keySet()){
                                        idUser = key;
                                    }
                                    for (DataSnapshot userQuery : snapshot.getChildren()) {
                                        UserModel user = userQuery.getValue(UserModel.class);
                                        usernameQuery = user.getUserName();
                                        passwordQuery = user.getPassword();
                                        nameQuery = user.getName();
                                        phoneQuery = user.getPhone();
                                        emailQuery = user.getEmail();
                                        addressQuery = user.getAddress();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Xác nhận đăng nhập?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                if (SignUp.MD5(password).equals(passwordQuery)) {
                                    MainActivity.userId = idUser;
                                    Intent userIntent = new Intent(SignIn.this, User.class);
                                    userIntent.putExtra("idUser", idUser);
                                    userIntent.putExtra("passwordQuery", passwordQuery);
                                    userIntent.putExtra("usernameQuery", usernameQuery);
                                    userIntent.putExtra("nameQuery", nameQuery);
                                    userIntent.putExtra("phoneQuery", phoneQuery);
                                    userIntent.putExtra("emailQuery", emailQuery);
                                    userIntent.putExtra("addressQuery", addressQuery);
                                    startActivity(userIntent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                                    builder.setTitle("Thông báo");
                                    builder.setMessage("Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản và mật khẩu.");
                                    builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int position) {

                                        }
                                    });
                                    builder.show();
                                }
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpAct = new Intent(SignIn.this, SignUp.class);
                startActivity(signUpAct);
            }
        });
    }
}