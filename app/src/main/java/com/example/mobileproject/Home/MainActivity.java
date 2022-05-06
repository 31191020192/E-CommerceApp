package com.example.mobileproject.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileproject.Cart.Cart;
import com.example.mobileproject.Cart.CartModel;
import com.example.mobileproject.Category.CategoryAdapter;
import com.example.mobileproject.Category.CategoryModel;
import com.example.mobileproject.Product.ProductAdapter;
import com.example.mobileproject.Product.ProductDetail;
import com.example.mobileproject.Product.ProductModel;
import com.example.mobileproject.R;
import com.example.mobileproject.User.SignIn;
import com.example.mobileproject.User.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    GridView gridCate, gv_home_product;
    ImageView btnMenu, iv_home_banner;
    TextView btnSignInMain, dmnb, sp;
    ImageButton btnCart, imgClose;
    RecyclerView rcv_search;
    SearchView sv_home_search;

    List<CategoryModel> cateList = new ArrayList<>();
    ;
    List<ProductModel> itemList = new ArrayList<>();

    public static ArrayList<CartModel> arrayCart = new ArrayList<CartModel>();
    public static String userId;
    public static String name, phone, address, username, password, email;
    int progressStatus = 0;
    private Handler mHandler = new Handler();
    private SearchAdapter searchAdapter;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridCate = findViewById(R.id.gridCate);
        gv_home_product = findViewById(R.id.gv_home_product);
        btnMenu = findViewById(R.id.btnMenu);
        mDrawerLayout = findViewById(R.id.dl_home);
        btnCart = findViewById(R.id.btnCart);
        btnSignInMain = findViewById(R.id.btnSignInMain);
        rcv_search = findViewById(R.id.rcv_search);
        sv_home_search = findViewById(R.id.sv_home_search);
        iv_home_banner = findViewById(R.id.iv_home_banner);
        dmnb = findViewById(R.id.dmnb);
        sp = findViewById(R.id.sp);
        imgClose = findViewById(R.id.imgClose);
        Intent homeAct = getIntent();

        sv_home_search.setQueryHint("Nhập sản phẩm bạn cần tìm");

        if (MainActivity.userId == null) {
            btnSignInMain.setText("Đăng nhập");
            btnSignInMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signInAct = new Intent(MainActivity.this, SignIn.class);
                    startActivity(signInAct);
                }
            });
        } else {
            btnSignInMain.setText("Quản lý tài khoản");
            btnSignInMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userAct = new Intent(MainActivity.this, User.class);
                    startActivity(userAct);
                }
            });
        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewCartAct = new Intent(MainActivity.this, Cart.class);
                startActivity(viewCartAct);
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcv_search.setVisibility(View.INVISIBLE);
                iv_home_banner.setVisibility(View.VISIBLE);
                dmnb.setVisibility(View.VISIBLE);
                gridCate.setVisibility(View.VISIBLE);
                sp.setVisibility(View.VISIBLE);
                gv_home_product.setVisibility(View.VISIBLE);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot listCate : snapshot.getChildren()) {
                    CategoryModel cate = listCate.getValue(CategoryModel.class);
                    cateList.add(new CategoryModel(cate.getCateID(), cate.getCateName(), cate.getIcon()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot listItem : snapshot.getChildren()) {
                    ProductModel item = listItem.getValue(ProductModel.class);
                    itemList.add(new ProductModel(item.getProId(), item.getProName(),
                            item.getProPrice(), item.getProImg(), item.getProCate()));
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
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        gridCate = findViewById(R.id.gridCate);
                        CategoryAdapter cateAdapter = new CategoryAdapter(MainActivity.this, R.layout.item_category, cateList);
                        gridCate.setAdapter(cateAdapter);

                        gv_home_product = findViewById(R.id.gv_home_product);
                        ProductAdapter productAdapter = new ProductAdapter(MainActivity.this, R.layout.item, itemList);
                        gv_home_product.setAdapter(productAdapter);

                        rcv_search = findViewById(R.id.rcv_search);
                        sv_home_search = findViewById(R.id.sv_home_search);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        rcv_search.setLayoutManager(linearLayoutManager);

                        searchAdapter = new SearchAdapter(itemList, MainActivity.this, MainActivity.this);
                        rcv_search.setAdapter(searchAdapter);

                        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);
                        rcv_search.addItemDecoration(itemDecoration);
                    }
                });
            }
        }).start();

        sv_home_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rcv_search.setVisibility(View.VISIBLE);
                iv_home_banner.setVisibility(View.INVISIBLE);
                dmnb.setVisibility(View.INVISIBLE);
                gridCate.setVisibility(View.INVISIBLE);
                sp.setVisibility(View.INVISIBLE);
                gv_home_product.setVisibility(View.INVISIBLE);
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                rcv_search.setVisibility(View.VISIBLE);
                iv_home_banner.setVisibility(View.INVISIBLE);
                dmnb.setVisibility(View.INVISIBLE);
                gridCate.setVisibility(View.INVISIBLE);
                sp.setVisibility(View.INVISIBLE);
                gv_home_product.setVisibility(View.INVISIBLE);
                return false;
            }
        });

    }

    @Override
    public void onItemClick(ProductModel pro) {
        Intent productDetailAct = new Intent(MainActivity.this, ProductDetail.class);
        productDetailAct.putExtra("proId", pro.getProId());
        productDetailAct.putExtra("proCate", pro.getProCate());
        startActivity(productDetailAct);
    }
}