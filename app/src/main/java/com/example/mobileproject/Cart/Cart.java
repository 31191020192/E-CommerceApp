package com.example.mobileproject.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.History.OrderHistory;
import com.example.mobileproject.Product.ProductModel;
import com.example.mobileproject.R;
import com.example.mobileproject.User.EditUser;
import com.example.mobileproject.User.SignIn;
import com.example.mobileproject.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Cart extends AppCompatActivity {
    public static ListView listCart;
    static TextView txtTotal, txtTotalFinal, txtEditInfoCart, txtReceiver,
            txtPhoneReceiver, txtAddressReceiverCart;
    public static TextView txtNoted;
    Button btnCharge, btnContinue;
    CartAdapter cartAdapter;
    ImageButton btnDelItem;
    Toolbar toolbarCart;
    public static ImageView imgNoted;
    public static long total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cartAdapter = new CartAdapter(Cart.this, MainActivity.arrayCart);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listCart = findViewById(R.id.listCart);
        txtTotal = findViewById(R.id.txtTotal);
        txtNoted = findViewById(R.id.txtNoted);
        txtTotalFinal = findViewById(R.id.txtTotalFinal);
        btnCharge = findViewById(R.id.btnCharge);
        btnContinue = findViewById(R.id.btnContinue);
        btnDelItem = findViewById(R.id.btnDelItem);
        imgNoted = findViewById(R.id.imgNoted);
        txtEditInfoCart = findViewById(R.id.txtEditInfoCart);
        txtReceiver = findViewById(R.id.txtReceiver);
        txtPhoneReceiver = findViewById(R.id.txtPhoneReceiver);
        txtAddressReceiverCart = findViewById(R.id.txtAddressReceiverCart);
        toolbarCart = findViewById(R.id.toolbarCart);

        if (User.name != null && User.phone != null && User.address != null) {
            txtReceiver.setText(User.name);
            txtPhoneReceiver.setText(User.phone);
            txtAddressReceiverCart.setText(User.address);
            txtEditInfoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editUser = new Intent(Cart.this, EditUser.class);
                    editUser.putExtra("idUser", MainActivity.userId);
                    editUser.putExtra("username", MainActivity.username);
                    editUser.putExtra("password", MainActivity.password);
                    editUser.putExtra("name", MainActivity.name);
                    editUser.putExtra("phone", MainActivity.phone);
                    editUser.putExtra("email", MainActivity.email);
                    editUser.putExtra("address", MainActivity.address);
                    startActivity(editUser);
                }
            });
        } else {
            txtEditInfoCart.setText("????ng nh???p");
            txtEditInfoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signIn = new Intent(Cart.this, SignIn.class);
                    startActivity(signIn);
                }
            });
        }

        setSupportActionBar(toolbarCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarCart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeAct = new Intent(Cart.this, MainActivity.class);
                startActivity(homeAct);
            }
        });

        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.arrayCart.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Gi??? h??ng c???a b???n tr???ng!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                    builder.setTitle("X??c nh???n ?????t h??ng");
                    builder.setMessage("B???n c?? ch???c mu???n ?????t ????n h??ng n??y?");
                    builder.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (MainActivity.userId == null) { //N???u ch??a ????ng nh???p th?? start Signin
                                Intent signin = new Intent(Cart.this, SignIn.class);
                                startActivity(signin);
                            } else {
                                OrderHistory order = new OrderHistory();
                                order.Order(MainActivity.arrayCart, Cart.this);
                            }
                        }
                    });
                    builder.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            }
        });

        cartAdapter = new CartAdapter(Cart.this, MainActivity.arrayCart);
        listCart.setAdapter(cartAdapter);
        checkEmptyCart();
        totalCart();
    }

    public static void AddToCart(String receiveIdPro, int quantity) {
        //T??m id c???a sp => truy v???n r???i add v??o gi??? h??ng
        if (MainActivity.arrayCart.size() > 0) { //N???u ch??a t???n t???i item th?? th??m v??o gi???, ng?????c l???i th?? ch??? th??m s??? l?????ng
            boolean flag = false; //Bi???n KT item t???n t???i
            for (int i = 0; i < MainActivity.arrayCart.size(); i++) {
                if (MainActivity.arrayCart.get(i).getProId().equals(receiveIdPro)) {
                    int oldQuantity = MainActivity.arrayCart.get(i).getProQuantity();
                    String oldPrice = MainActivity.arrayCart.get(i).getProPrice();
                    MainActivity.arrayCart.get(i).setProQuantity(MainActivity.arrayCart.get(i).getProQuantity() + quantity);
                    MainActivity.arrayCart.get(i).setProPrice(((MainActivity.arrayCart.get(i).getProQuantity()
                            * Long.parseLong(oldPrice)) / oldQuantity) + "");
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dRef = fDatabase.getReference("Products");
                Query query = dRef.orderByChild("proId").equalTo(receiveIdPro);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot listPro : snapshot.getChildren()) {
                            ProductModel product = listPro.getValue(ProductModel.class);
                            CartModel item = new CartModel(product.getProId(), product.getProName(), product.getProImg(),
                                    Long.parseLong(product.getProPrice()) * quantity + "", quantity);
                            MainActivity.arrayCart.add(item);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else {
            FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dRef = fDatabase.getReference("Products");
            Query query = dRef.orderByChild("proId").equalTo(receiveIdPro);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot listPro : snapshot.getChildren()) {
                        ProductModel product = listPro.getValue(ProductModel.class);
                        CartModel item = new CartModel(product.getProId(), product.getProName(), product.getProImg(), Long.parseLong(product.getProPrice()) * quantity + "", quantity);
                        MainActivity.arrayCart.add(item);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    //T??nh t???ng gi?? tr??? gi??? h??ng
    public static void totalCart() {
        total = 0;
        for (int i = 0; i < MainActivity.arrayCart.size(); i++) {
            total += Long.parseLong(MainActivity.arrayCart.get(i).getProPrice());
        }
        String totalCart = total + " ??";
        txtTotal.setText(totalCart);
        txtTotalFinal.setText((total + 15000) + " ??");
    }

    private void checkEmptyCart() {
        if (MainActivity.arrayCart.isEmpty()) {
            cartAdapter.notifyDataSetChanged();
            txtNoted.setVisibility(View.VISIBLE);
            imgNoted.setVisibility(View.VISIBLE);
            listCart.setVisibility(View.INVISIBLE);
        } else {
            cartAdapter.notifyDataSetChanged();
            txtNoted.setVisibility(View.INVISIBLE);
            imgNoted.setVisibility(View.INVISIBLE);
            listCart.setVisibility(View.VISIBLE);
        }
    }
}