package com.ed.shunel.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Product;
import com.ed.shunel.R;
import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.ImageTask;

import java.util.List;

public class SearchAdapter_Sam extends RecyclerView.Adapter<SearchAdapter_Sam.searchmyviewholder> {
    private Context context;
    private List<Product> products;
    private ImageTask productimageTask;
    private int imageSize;




    public SearchAdapter_Sam(Context context, List<Product> product) {
        this.context = context;
        this.products = product;
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;

    }

    @NonNull
    @Override
    public searchmyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_search, parent, false);
        return new searchmyviewholder(view);

    }


    public class searchmyviewholder extends RecyclerView.ViewHolder {
        private ImageView ivsearch;
        private TextView tvname;
        private TextView tvprice;

        public searchmyviewholder(View view) {
            super(view);
            ivsearch = view.findViewById(R.id.ivsearch);
            tvname = view.findViewById(R.id.tvname);
            tvprice = view.findViewById(R.id.tvprice);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull searchmyviewholder holder, int position) {
        final Product product = products.get(position);
        String url = Common.URL_SERVER + "Prouct_Servlet";
        int id_product = product.getProduct_ID();
        productimageTask = new ImageTask(url, id_product, imageSize, holder.ivsearch);
        productimageTask.execute();
        holder.tvname.setText(products.get(position).getProduct_Name());
        holder.tvprice.setText("$ "+String.valueOf(product.getProduct_Price()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                bundle.putSerializable("number", 2);
                Navigation.findNavController(v).navigate(R.id.productDetailFragment, bundle);
            }
        });



    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public void setProducts(List<Product> product) {
        this.products = product;
    }


}
