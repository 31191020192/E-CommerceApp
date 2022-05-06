package com.example.mobileproject.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobileproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List objects;

    public Object getItem(int i) {
        return objects.get(i);
    }

    public CategoryAdapter(@NonNull Context context, int layoutResourceId, @NonNull List objects) {
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
            holder.txtNameCate = row.findViewById(R.id.txtNameCate);
            holder.imgCate = row.findViewById(R.id.imgCate);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        CategoryModel item = (CategoryModel) getItem(position);
        holder.txtNameCate.setText(item.getCateName());
        Picasso.get().load(item.getIcon()).resize(holder.imgCate.getWidth(), 195).into(holder.imgCate);
        holder.imgCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cateProAct = new Intent(context, CategoryProduct.class);
                cateProAct.putExtra("cateID", item.getCateID());
                cateProAct.putExtra("nameCate", item.getCateName());
                context.startActivity(cateProAct);
            }
        });
        return row;
    }

    static class ViewHolder {
        TextView txtNameCate;
        ImageButton imgCate;
    }
}
