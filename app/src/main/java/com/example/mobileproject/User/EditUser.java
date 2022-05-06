package com.example.mobileproject.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobileproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUser extends AppCompatActivity {
    EditText txtEditName, txtEditPhone, txtEditEmail, txtEditAddress;
    Button btnEditUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        txtEditName = findViewById(R.id.txtEditName);
        txtEditPhone = findViewById(R.id.txtEditPhone);
        txtEditEmail = findViewById(R.id.txtEditEmail);
        txtEditAddress = findViewById(R.id.txtEditAddress);
        btnEditUser = findViewById(R.id.btnEditUser);

        Intent editUserIntent = getIntent(); //Nhận thông tin từ trang User
        String idUser = editUserIntent.getStringExtra("idUser");
        String username = editUserIntent.getStringExtra("username");
        String password = editUserIntent.getStringExtra("password");
        String name = editUserIntent.getStringExtra("name");
        String phone = editUserIntent.getStringExtra("phone");
        String email = editUserIntent.getStringExtra("email");
        String address = editUserIntent.getStringExtra("address");

        //Hiển thị lên giao diện
        txtEditName.setText(name);
        txtEditPhone.setText(phone);
        txtEditEmail.setText(email);
        txtEditAddress.setText(address);

        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtEditName.getText().toString().trim();
                String phone = txtEditPhone.getText().toString().trim();
                String email = txtEditEmail.getText().toString().trim();
                String address = txtEditAddress.getText().toString().trim();

                if (name.isEmpty()){
                    txtEditName.setError("Tên không được bỏ trống");
                }
                else {
                    if (phone.isEmpty()){
                        txtEditPhone.setError("Số điện thoại không được bỏ trống");
                    }else {
                        if (phone.length()!= 10 ){
                            txtEditPhone.setError("Số điện thoại bao gồm 10 số");
                        }else {
                            if (address.isEmpty()){
                                txtEditAddress.setError("Địa chỉ giao hàng không được bỏ trống");
                            }else {
                                DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference("User");
                                dDatabase.child(idUser).setValue(new UserModel(username, password, name, email, phone, address));
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditUser.this);
                                builder.setTitle("Thông báo");
                                builder.setMessage("Cập nhật thông tin thành công.");
                                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position) {
                                        Intent intent = new Intent(EditUser.this, User.class);
                                        intent.putExtra("name", txtEditName.getText().toString().trim());
                                        intent.putExtra("phone", txtEditPhone.getText().toString().trim());
                                        startActivity(intent);
                                    }
                                });
                                builder.show();
                            }
                        }
                    }
                }
            }
        });
    }
}