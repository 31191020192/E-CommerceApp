package com.example.mobileproject.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobileproject.Cart.Cart;
import com.example.mobileproject.Product.ProductDetail;
import com.example.mobileproject.Product.ProductModel;
import com.example.mobileproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryProductAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List objects;

    public Object getItem(int i) {
        return objects.get(i);
    }

    public CategoryProductAdapter(@NonNull Context context, int layoutResourceId, @NonNull List objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.txtNamePro = row.findViewById(R.id.txtNamePro);
            holder.txtPricePro = row.findViewById(R.id.txtPricePro);
            holder.imgPro = row.findViewById(R.id.imgPro);
            holder.btnAddPro = row.findViewById(R.id.btnAddPro);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ProductModel item = (ProductModel) getItem(position);
        holder.txtPricePro.setText(item.getProPrice() + "đ");
        holder.txtNamePro.setText(item.getProName());
        Picasso.get().load(item.getProImg()).resize(400, 390).into(holder.imgPro);
        holder.imgPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetailAct = new Intent(context, ProductDetail.class);
                productDetailAct.putExtra("proId", item.getProId());
                productDetailAct.putExtra("proCate", item.getProCate());
                context.startActivity(productDetailAct);
            }
        });

        holder.btnAddPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart.AddToCart(item.getProId(), 1);
                Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
        return row;
    }

    class ViewHolder {
        TextView txtPricePro, txtNamePro;
        ImageButton imgPro;
        Button btnAddPro;
    }
}
