package com.example.mobileproject.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.mobileproject.Cart.Cart;
import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {
    DatabaseReference dRef;
    FirebaseDatabase fDatabase;
    ProductModel product;
    Button btnDecrease, btnIncrease, btnAddCart;
    ImageButton proImg, otherProImg1, otherProImg2, otherProImg3, btnViewCartPD;
    TextView proNameTxt, rateTxt, priceTxt, descriptionTxt, nameOtherPro1, nameOtherPro2, nameOtherPro3, priceOtherPro1, priceOtherPro2, priceOtherPro3, txtQuantity;
    Intent productDetailAct;
    String keyProId = null;
    String cateName;
    int quantity = 1;
    Toolbar toolbarProDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        proImg = findViewById(R.id.proImg);
        otherProImg1 = findViewById(R.id.otherProImg1);
        otherProImg2 = findViewById(R.id.otherProImg3);
        otherProImg3 = findViewById(R.id.otherProImg2);
        btnViewCartPD = findViewById(R.id.btnViewCartPD);
        btnAddCart = findViewById(R.id.btnAddCart);
        proNameTxt = findViewById(R.id.proNameTxt);
        rateTxt = findViewById(R.id.rateTxt);
        priceTxt = findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        nameOtherPro1 = findViewById(R.id.nameOtherPro1);
        nameOtherPro2 = findViewById(R.id.nameOtherPro2);
        nameOtherPro3 = findViewById(R.id.nameOtherPro3);
        priceOtherPro1 = findViewById(R.id.priceOtherPro1);
        priceOtherPro2 = findViewById(R.id.priceOtherPro2);
        priceOtherPro3 = findViewById(R.id.priceOtherPro3);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        txtQuantity = findViewById(R.id.txtQuantity);

        productDetailAct = getIntent();
        toolbarProDetail = findViewById(R.id.toolbarProDetail);

        setSupportActionBar(toolbarProDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cateName = productDetailAct.getStringExtra("proCate");
        toolbarProDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>=2) {
                    quantity--;
                }
                txtQuantity.setText(String.valueOf(quantity));
            }
        });

        btnViewCartPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewCartIntent= new Intent(ProductDetail.this, Cart.class);
                startActivity(viewCartIntent);
            }
        });

        getProductDetail(proNameTxt, priceTxt, descriptionTxt, rateTxt, proImg);
        getImgSameCategory(otherProImg1, otherProImg2, otherProImg3);
    }

    private void getProductDetail (TextView proName, TextView proPrice,
                                   TextView proDescrip, TextView proRate, ImageButton imgPro) { //Lấy chi tiết sp
        quantity = 1;
        Query query;
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Products");
        if (productDetailAct != null){
            keyProId = productDetailAct.getStringExtra("proId");
            query = dRef.orderByChild("proId").equalTo(keyProId);
        }
        else {
            keyProId = "pm3";
            query = dRef.orderByChild("proId").equalTo(keyProId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot listPro : snapshot.getChildren()) {
                    product = new ProductModel();
                    product = listPro.getValue(ProductModel.class);
                    proName.setText(product.getProName());
                    proPrice.setText(product.getProPrice() + " đ");
                    proDescrip.setText(product.getProDes());
                    proRate.setText(product.getProRate());
                    Picasso.get().load(product.getProImg()).resize(proImg.getWidth(), 600).into(imgPro);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.AddToCart(keyProId, quantity);
                Toast.makeText(ProductDetail.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*Hiển thị DS sp tương tự*/
    private void getImgSameCategory(ImageButton otherProImg1,
                                    ImageButton otherProImg2, ImageButton otherProImg3){
        quantity = 1;
        txtQuantity.setText(quantity + "");
        int[] flag = {0};
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference().child("Products");
        Query query = dRef.orderByChild("proCate").equalTo(cateName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot listPro: snapshot.getChildren()) {
                    product = listPro.getValue(ProductModel.class);
                    if (product.getProId().equals(keyProId)) {
                        continue;
                    } else {
                        switch (flag[0]) {
                            case 0:
                                String proId1 = product.getProId();
                                nameOtherPro1.setText(product.getProName());
                                priceOtherPro1.setText(product.getProPrice() + " đ");
                                Picasso.get().load(product.getProImg()).resize(otherProImg1.getWidth(), 300).into(otherProImg1);
                                otherProImg1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productDetailAct = new Intent(ProductDetail.this, MainActivity.class);
                                        productDetailAct.putExtra("proId", proId1);
                                        getImgSameCategory(otherProImg1, otherProImg2, otherProImg3);
                                    }
                                });
                                break;
                            case 1:
                                String proId2 = product.getProId();
                                nameOtherPro3.setText(product.getProName());
                                priceOtherPro3.setText(product.getProPrice() + " đ");
                                Picasso.get().load(product.getProImg()).resize(otherProImg2.getWidth(), 300).into(otherProImg2);
                                otherProImg2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productDetailAct = new Intent(ProductDetail.this, MainActivity.class);
                                        productDetailAct.putExtra("proId", proId2);
                                        getImgSameCategory(otherProImg1, otherProImg2, otherProImg3);
                                    }
                                });
                                break;
                            case 2:
                                String proId3 = product.getProId();
                                nameOtherPro2.setText(product.getProName());
                                priceOtherPro2.setText(product.getProPrice() + " đ");
                                Picasso.get().load(product.getProImg()).resize(otherProImg3.getWidth(), 300).into(otherProImg3);
                                otherProImg3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        productDetailAct = new Intent(ProductDetail.this, MainActivity.class);
                                        productDetailAct.putExtra("proId", proId3);
                                        getImgSameCategory(otherProImg1, otherProImg2, otherProImg3);
                                    }
                                });
                                break;
                        }
                        flag[0]++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getProductDetail(proNameTxt, priceTxt, descriptionTxt, rateTxt, proImg);
    }
}