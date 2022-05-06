package com.example.mobileproject.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mobileproject.R;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<OrderInfoUserModel> orderArr;
    public static String orderId;

    public HistoryAdapter(Context context, ArrayList<OrderInfoUserModel> OrderArr){
        this.context = context;
        this.orderArr = OrderArr;
    }

    @Override
    public int getCount() {
        return orderArr.size();
    }

    @Override
    public Object getItem(int i) {
        return orderArr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView txtOrderId, txtOrderTime, txtInfoReceiver, txtAddressReceiver, txtTotalBill;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.info_history, null);
            viewHolder.txtOrderId = convertView.findViewById(R.id.txtOrderId);
            viewHolder.txtOrderTime = convertView.findViewById(R.id.txtOrderTime);
            viewHolder.txtInfoReceiver = convertView.findViewById(R.id.txtInfoReceiver);
            viewHolder.txtAddressReceiver = convertView.findViewById(R.id.txtAddressReceiver);
            viewHolder.txtTotalBill = convertView.findViewById(R.id.txtTotalBill);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderInfoUserModel order = (OrderInfoUserModel) getItem(position);
        viewHolder.txtOrderId.setText("DH#"+order.getOrderId());
        orderId = order.getOrderId();
        viewHolder.txtOrderTime.setText(order.getTime());
        viewHolder.txtInfoReceiver.setText(order.getOrderReceiver());
        viewHolder.txtAddressReceiver.setText(order.getOrderAddress());
        viewHolder.txtTotalBill.setText((Integer.parseInt(order.getTotal()) + 15000) +"đ");
        return convertView;
    }
}
