package com.example.mobileproject.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mobileproject.Home.MainActivity;
import com.example.mobileproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    Context context;
    ArrayList<CartModel> cartArr;

    public CartAdapter(Context context, ArrayList<CartModel> cartArr) {
        this.context = context;
        this.cartArr = cartArr;
    }

    @Override
    public int getCount() {
        return cartArr.size();
    }

    @Override
    public Object getItem(int i) {
        return cartArr.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView txtCartItemName, txtCartItemPrice;
        public ImageView imgItemCart, imgNoted;
        public Button btnMinus, btnPlus;
        public ImageButton btnDelItem;
        public EditText editTxtQuantity;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cart_item, null);
            viewHolder.txtCartItemName = view.findViewById(R.id.txtCartItemName);
            viewHolder.txtCartItemPrice = view.findViewById(R.id.txtCartItemPrice);
            viewHolder.imgItemCart = view.findViewById(R.id.imgItemCart);
            viewHolder.btnMinus = view.findViewById(R.id.btnMinus);
            viewHolder.btnPlus = view.findViewById(R.id.btnPlus);
            viewHolder.btnDelItem = view.findViewById(R.id.btnDelItem);
            viewHolder.editTxtQuantity = view.findViewById(R.id.editTxtQuantity);
            viewHolder.imgNoted = view.findViewById(R.id.imgNoted);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CartModel cart = (CartModel) getItem(i);
        viewHolder.txtCartItemName.setText(cart.getProName());
        viewHolder.txtCartItemPrice.setText(cart.getProPrice() + "đ");
        Picasso.get().load(cart.getProImg()).resize(viewHolder.imgItemCart.getWidth(), 100).into(viewHolder.imgItemCart);
        viewHolder.editTxtQuantity.setText(cart.getProQuantity() + "");

        ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityNew = 1;
                if (finalViewHolder.editTxtQuantity.getText().toString().isEmpty()) {
                    quantityNew = 1;
                } else {
                    quantityNew = Integer.parseInt(finalViewHolder.editTxtQuantity.getText().toString()) + 1;
                }
                int quantityCurrent = MainActivity.arrayCart.get(i).getProQuantity();
                String price = MainActivity.arrayCart.get(i).getProPrice();
                MainActivity.arrayCart.get(i).setProQuantity(quantityNew);
                Long newPrice = (Long.parseLong(price) * quantityNew) / quantityCurrent;
                MainActivity.arrayCart.get(i).setProPrice(String.valueOf(newPrice));
                finalViewHolder.editTxtQuantity.setText(MainActivity.arrayCart.get(i).getProQuantity() + "");
                finalViewHolder.txtCartItemPrice.setText(MainActivity.arrayCart.get(i).getProPrice() +"đ");
                Cart.totalCart();
            }
        });
        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityNew = 1;
                if (finalViewHolder.editTxtQuantity.getText().toString().isEmpty()) {
                    quantityNew = 1;
                } else {
                    int quantity = Integer.parseInt(finalViewHolder.editTxtQuantity.getText().toString());
                    if (quantity > 1) {
                        quantityNew = Integer.parseInt(finalViewHolder.editTxtQuantity.getText().toString()) - 1;
                    }
                    int quantityCurrent = MainActivity.arrayCart.get(i).getProQuantity();
                    String price = MainActivity.arrayCart.get(i).getProPrice();
                    MainActivity.arrayCart.get(i).setProQuantity(quantityNew);
                    Long newPrice = (Long.parseLong(price) * quantityNew) / quantityCurrent;
                    MainActivity.arrayCart.get(i).setProPrice(String.valueOf(newPrice));
                    finalViewHolder.editTxtQuantity.setText(MainActivity.arrayCart.get(i).getProQuantity() + "");
                    finalViewHolder.txtCartItemPrice.setText(MainActivity.arrayCart.get(i).getProPrice() + "đ");
                    Cart.totalCart();
                }
            }
        });

        viewHolder.btnDelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Bạn có chắc muốn xóa món hàng này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        MainActivity.arrayCart.remove(i);
                        notifyDataSetChanged();
                        Cart.totalCart();
                        if(MainActivity.arrayCart.size() == 0){
                            Cart.txtNoted.setVisibility(View.VISIBLE);
                           Cart.listCart.setVisibility(View.INVISIBLE);
                           Cart.imgNoted.setVisibility(View.VISIBLE);
                           notifyDataSetChanged();
                        }else {
                            Cart.txtNoted.setVisibility(View.INVISIBLE);
                            Cart.imgNoted.setVisibility(View.INVISIBLE);
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
