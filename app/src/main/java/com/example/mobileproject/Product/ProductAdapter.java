package com.example.mobileproject.Product;

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
import com.example.mobileproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List objects;

    public Object getItem(int i) {
        return objects.get(i);
    }

    public ProductAdapter(@NonNull Context context, int layoutResourceId, @NonNull List objects) {
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
            holder.txtNameItem = row.findViewById(R.id.txtNameItem);
            holder.txtPriceItem = row.findViewById(R.id.txtPriceItem);
            holder.imgItem = row.findViewById(R.id.imgItem);
            holder.btnAdd = row.findViewById(R.id.btnAdd);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ProductModel item = (ProductModel) getItem(position);
        holder.txtPriceItem.setText(item.getProPrice() + "đ");
        holder.txtNameItem.setText(item.getProName());
        Picasso.get().load(item.getProImg()).resize(380, 380).into(holder.imgItem);
        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetailAct = new Intent(context, ProductDetail.class);
                productDetailAct.putExtra("proId", item.getProId());
                productDetailAct.putExtra("proCate", item.getProCate());
                context.startActivity(productDetailAct);
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart.AddToCart(item.getProId(), 1);
                Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
        return row;
    }

    class ViewHolder {
        TextView txtPriceItem, txtNameItem;
        ImageButton imgItem;
        Button btnAdd;
    }
}
