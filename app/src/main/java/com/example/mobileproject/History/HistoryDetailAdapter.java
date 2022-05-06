package com.example.mobileproject.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryDetailAdapter extends BaseAdapter {
    Context context;
    ArrayList<OrderHistoryModel> itemPurchaseArr;

    public HistoryDetailAdapter(Context context, ArrayList<OrderHistoryModel> itemPurchaseArr){
        this.context = context;
        this.itemPurchaseArr = itemPurchaseArr;
    }

    @Override
    public int getCount() {
        return itemPurchaseArr.size();
    }

    @Override
    public Object getItem(int i) {
        return itemPurchaseArr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        ImageView imgItemPurchase;
        TextView txtPurchaseItemName, txtPurchaseItemPrice, txtPurchaseItemQuantity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_history, null);
            viewHolder.imgItemPurchase = convertView.findViewById(R.id.imgItemPurchase);
            viewHolder.txtPurchaseItemName = convertView.findViewById(R.id.txtPurchaseItemName);
            viewHolder.txtPurchaseItemPrice = convertView.findViewById(R.id.txtPurchaseItemPrice);
            viewHolder.txtPurchaseItemQuantity = convertView.findViewById(R.id.txtPurchaseItemQuantity);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderHistoryModel order = (OrderHistoryModel) getItem(position);
        Picasso.get().load(order.getProImg()).resize(viewHolder.imgItemPurchase.getWidth(), 100).into(viewHolder.imgItemPurchase);
        viewHolder.txtPurchaseItemName.setText(order.getProName());
        viewHolder.txtPurchaseItemPrice.setText(order.getProPrice() +"đ");
        viewHolder.txtPurchaseItemQuantity.setText("SL: "  +order.getProQuantity());
        return convertView;
    }
}
