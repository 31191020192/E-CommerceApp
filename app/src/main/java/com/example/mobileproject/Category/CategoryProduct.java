package com.example.mobileproject.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Cart.Cart;
import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.Home.OnItemClickListener;
import com.example.mobileproject.Home.SearchAdapter;
import com.example.mobileproject.Product.ProductDetail;
import com.example.mobileproject.Product.ProductModel;
import com.example.mobileproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryProduct extends AppCompatActivity implements OnItemClickListener {
    TextView txtNameCategory, txtNotedProgressCP;
    GridView gv_catePro;
    ProgressBar progressBarCP;
    ImageButton btnViewCartCP, imgCloseCS;
    Toolbar toolbarCatePro;
    List<ProductModel> itemList;
    RecyclerView rcv_searchInCate;
    SearchView sv_cate_search;

    String cateID = "";
    int progressStatus = 0;
    SearchAdapter searchAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);

        txtNameCategory = findViewById(R.id.txtNameCategory);
        txtNotedProgressCP = findViewById(R.id.txtNotedProgressCP);
        gv_catePro = findViewById(R.id.gv_catePro);
        progressBarCP = findViewById(R.id.progressBarCP);
        toolbarCatePro = findViewById(R.id.toolbarCatePro);
        btnViewCartCP = findViewById(R.id.btnViewCartCP);
        rcv_searchInCate = findViewById(R.id.rcv_searchInCate);
        sv_cate_search = findViewById(R.id.sv_cate_search);
        imgCloseCS = findViewById(R.id.imgCloseCS);

        setSupportActionBar(toolbarCatePro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarCatePro.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent cateProAct = getIntent();
        cateID = cateProAct.getStringExtra("cateID");
        String nameCate = cateProAct.getStringExtra("nameCate");

        sv_cate_search.setQueryHint("Tìm sản phẩm trong "+nameCate);
        txtNameCategory.setText(nameCate);
        btnViewCartCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewCart = new Intent(CategoryProduct.this, Cart.class);
                startActivity(viewCart);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        itemList = new ArrayList<>();
        databaseReference.orderByChild("proCate").equalTo(cateID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot listItem : snapshot.getChildren()) {
                    ProductModel item = listItem.getValue(ProductModel.class);
                    itemList.add(new ProductModel(item.getProId(), item.getProName(), item.getProPrice(), item.getProImg(), item.getProCate()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 35) {
                    progressStatus++;
                    SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBarCP.setProgress(progressStatus);
                        }
                    });
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBarCP.setVisibility(View.INVISIBLE);
                        txtNotedProgressCP.setVisibility(View.INVISIBLE);
                        gv_catePro = findViewById(R.id.gv_catePro);
                        CategoryProductAdapter categoryProductAdapter = new CategoryProductAdapter(CategoryProduct.this, R.layout.item_in_category, itemList);
                        gv_catePro.setAdapter(categoryProductAdapter);

                        rcv_searchInCate = findViewById(R.id.rcv_searchInCate);
                        sv_cate_search = findViewById(R.id.sv_cate_search);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoryProduct.this);
                        rcv_searchInCate.setLayoutManager(linearLayoutManager);

                        searchAdapter = new SearchAdapter(itemList, CategoryProduct.this, CategoryProduct.this);
                        rcv_searchInCate.setAdapter(searchAdapter);

                        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(CategoryProduct.this, DividerItemDecoration.VERTICAL);
                        rcv_searchInCate.addItemDecoration(itemDecoration);
                    }
                });
            }
        }).start();

        sv_cate_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rcv_searchInCate.setVisibility(View.VISIBLE);
                gv_catePro.setVisibility(View.INVISIBLE);
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                rcv_searchInCate.setVisibility(View.VISIBLE);
                gv_catePro.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        imgCloseCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcv_searchInCate.setVisibility(View.INVISIBLE);
                gv_catePro.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(ProductModel pro) {
        Intent productDetailAct = new Intent(CategoryProduct.this, ProductDetail.class);
        productDetailAct.putExtra("proId", pro.getProId());
        productDetailAct.putExtra("proCate", pro.getProCate());
        startActivity(productDetailAct);
    }
}