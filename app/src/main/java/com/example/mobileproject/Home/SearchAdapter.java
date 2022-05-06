package com.example.mobileproject.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.Cart.Cart;
import com.example.mobileproject.Product.ProductModel;
import com.example.mobileproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private List<ProductModel> listPro;
    private List<ProductModel> listProOld;
    private OnItemClickListener listener;
    Context context;

    public SearchAdapter(List<ProductModel> listPro, Context context, OnItemClickListener listener) {
        this.listPro = listPro;
        this.listProOld = listPro;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        ProductModel pro = listPro.get(position);
        if (pro == null){
        }

        holder.bindData(listPro.get(position));
    }

    @Override
    public int getItemCount() {
        if (listPro != null){
            return listPro.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    listPro = listProOld;
                }
                else {
                    List<ProductModel> list = new ArrayList<>();
                    for (ProductModel product: listProOld){
                        if (product.getProName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(product);
                        }
                    }

                    listPro = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listPro;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listPro = (List<ProductModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private ProductModel product;
        private ImageView imgISearch;
        private TextView txtNameISearch, txtPriceISearch;
        private Button btnAddISearch;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imgISearch = itemView.findViewById(R.id.imgISearch);
            txtNameISearch = itemView.findViewById(R.id.txtNameISearch);
            txtPriceISearch = itemView.findViewById(R.id.txtPriceISearch);
            btnAddISearch = itemView.findViewById(R.id.btnAddISearch);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
        }

        private void bindData(ProductModel product){
            this.product = product;
            Picasso.get().load(product.getProImg()).resize(imgISearch.getWidth(), 150).into(imgISearch);
            txtNameISearch.setText(product.getProName());
            txtPriceISearch.setText(product.getProPrice() +"đ");
            btnAddISearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cart.AddToCart(product.getProId(), 1);
                    Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
