package com.example.mobileproject.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobileproject.Category.CategoryAdapter;
import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.Home.SearchAdapter;
import com.example.mobileproject.Product.ProductAdapter;
import com.example.mobileproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class SignUp extends AppCompatActivity {
    EditText txtUsernameSignup, txtPasswordSignup, txtName, txtRepeatPassword, txtEmail, txtPhone,
            txtProvince, txtDistrict, txtWard, txtAddress;
    Button btnSignupSU, btnCancelSignup;

    boolean flag1, flag2;
    int progressStatus = 0;
    private Handler mHandler = new Handler();
    static ArrayList<String> usernameArr = new ArrayList<String>();
    static ArrayList<String> phoneArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtUsernameSignup = findViewById(R.id.txtUsernameSignin);
        txtPasswordSignup = findViewById(R.id.txtPasswordSignup);
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtProvince = findViewById(R.id.txtProvince);
        txtDistrict = findViewById(R.id.txtDistrict);
        txtWard = findViewById(R.id.txtWard);
        txtAddress = findViewById(R.id.txtAddress);
        btnSignupSU = findViewById(R.id.btnSignupSU);
        btnCancelSignup = findViewById(R.id.btnCancelSignup);

        btnSignupSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsernameSignup.getText().toString().trim();
                String password = txtPasswordSignup.getText().toString().trim();
                String repeatPassword = txtRepeatPassword.getText().toString().trim();
                String name = txtName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String address = txtAddress.getText().toString().trim() + " Ph?????ng " + txtWard.getText().toString().trim() + " Qu???n "
                        + txtDistrict.getText().toString().trim() + " " + txtProvince.getText().toString().trim();
                String addressCheck = txtAddress.getText().toString().trim() + txtWard.getText().toString().trim()
                        + txtDistrict.getText().toString().trim() + txtProvince.getText().toString().trim();

                flag1 = false;
                flag2 = false;
                checkExistedUsername(username);
                checkExistPhone(phone);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progressStatus < 30) {
                            progressStatus++;
                            SystemClock.sleep(50);
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (checkExistValue(username, password, repeatPassword, name, email, phone, addressCheck)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                    builder.setTitle("Th??ng b??o");
                                    builder.setMessage("????ng k?? th???t b???i. Nh???p thi???u th??ng tin, vui l??ng ki???m tra l???i");
                                    builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int position) {

                                        }
                                    });
                                    builder.show();
                                } else {
                                    if (password.length() <= 6) {
                                        txtPasswordSignup.setError("????? d??i m???t kh???u ph???i l???n h??n 6 k?? t???");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                        builder.setTitle("Th??ng b??o");
                                        builder.setMessage("????ng k?? th???t b???i. ????? d??i m???t kh???u ph???i l???n h??n 6 k?? t???");
                                        builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int position) {

                                            }
                                        });
                                        builder.show();
                                    } else {
                                        if (password.equals(repeatPassword) == false) {
                                            txtRepeatPassword.setError("M???t kh???u nh???p l???i ph???i tr??ng v???i m???t kh???u");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                            builder.setTitle("Th??ng b??o");
                                            builder.setMessage("????ng k?? th???t b???i. M???t kh???u nh???p l???i ph???i tr??ng v???i m???t kh???u");
                                            builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int position) {

                                                }
                                            });
                                            builder.show();
                                        } else {
                                            if (isEmailValid(email) == false) {
                                                txtEmail.setError("Email kh??ng h???p l???");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                builder.setTitle("Th??ng b??o");
                                                builder.setMessage("????ng k?? th???t b???i. Email kh??ng h???p l???");
                                                builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int position) {

                                                    }
                                                });
                                                builder.show();
                                            } else {
                                                if (phone.length() < 10 || phone.length() > 11) {
                                                    txtPhone.setError("S??? ??i???n tho???i kh??ng h???p l???");
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                    builder.setTitle("Th??ng b??o");
                                                    builder.setMessage("????ng k?? th???t b???i. S??? ??i???n tho???i kh??ng h???p l???");
                                                    builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int position) {

                                                        }
                                                    });
                                                    builder.show();
                                                } else {
                                                    if (usernameArr.contains(username)){
                                                        flag1 = true;
                                                    }else flag1 = false;

                                                    if (phoneArr.contains(phone)){
                                                        flag2 = true;
                                                    }else flag2 = false;

                                                    if (flag1 == true) {
                                                        txtUsernameSignup.setError("T??i kho???n ???? t???n t???i");
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                        builder.setTitle("Th??ng b??o");
                                                        builder.setMessage("????ng k?? th???t b???i. T??i kho???n ???? t???n t???i");
                                                        builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int position) {

                                                            }
                                                        });
                                                        builder.show();
                                                    } else {
                                                        if (flag2 == true) {
                                                            txtPhone.setError("S??? ??i???n tho???i n??y ???? ???????c ????ng k?? b???i t??i kho???n kh??c");
                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUp.this);
                                                            builder1.setTitle("Th??ng b??o");
                                                            builder1.setMessage("????ng k?? th???t b???i. S??? ??i???n tho???i n??y ???? ???????c ????ng k?? b???i t??i kho???n kh??c!");
                                                            builder1.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int position) {

                                                                }
                                                            });
                                                            builder1.show();
                                                        } else {
                                                            DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference("User");
                                                            String userId = dDatabase.push().getKey();
                                                            UserModel user = new UserModel(username, MD5(password), name, email, phone, address);
                                                            dDatabase.child(userId).setValue(user);

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setTitle("Th??ng b??o");
                                                            builder.setMessage("????ng k?? th??nh c??ng. Vui l??ng ????ng nh???p l???i");
                                                            builder.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int position) {
                                                                    Intent signInAct = new Intent(SignUp.this, SignIn.class);
                                                                    startActivity(signInAct);
                                                                }
                                                            });
                                                            builder.show();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }).start();

            }
        });

        btnCancelSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setTitle("H???y ????ng k??");
                builder.setMessage("B???n c?? mu???n h???y ????ng k?? v?? tr??? v??? trang ????ng nh???p kh??ng?");
                builder.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        Intent singInAct = new Intent(SignUp.this, SignIn.class);
                        startActivity(singInAct);
                    }
                });
                builder.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //KT c??c tr?????ng c?? d??? li???u ch??a
    private boolean checkExistValue(String username, String password, String repeatPassword, String name,
                                    String email, String phone, String addressCheck) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()
                || addressCheck.isEmpty() || repeatPassword.isEmpty() || name.isEmpty()) {
            return true;
        } else return false;
    }

    //MD5 cho password
    public static String MD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            return convertByteToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertByteToHex(byte[] data) {
        BigInteger number = new BigInteger(1, data);
        String hashtext = number.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    private void checkExistedUsername(String username) {
        DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference();
        Query queryUser = dDatabase.child("User");
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userQuery : snapshot.getChildren()) {
                    UserModel user = userQuery.getValue(UserModel.class);
                    if (usernameArr.contains(user.getUserName()) == false){
                        usernameArr.add(user.getUserName());
                    }
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });
    }

    private void checkExistPhone(String phone) {
        DatabaseReference dDatabase = FirebaseDatabase.getInstance().getReference();
        Query queryPhone = dDatabase.child("User");
        queryPhone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userQuery : snapshot.getChildren()) {
                    UserModel user = userQuery.getValue(UserModel.class);
                    if (phoneArr.contains(user.getPhone()) == false){
                        phoneArr.add(user.getPhone());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}