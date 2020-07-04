package com.ed.shunel.adapter;

import android.content.Context;
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

public class ProductAdapter_Sam extends RecyclerView.Adapter<ProductAdapter_Sam.Myviewholder>{
    private Context context;
    private List<Product> products;
    private ImageTask productimageTask;
    private int imageSize;

    public ProductAdapter_Sam(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.item_view_product,parent,false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder myviewholder, int position) {
        final Product product = products.get(position);
        String url = Common.URL_SERVER + "Shunel_Web";
        int id_product = product.getProduct_ID();
        productimageTask = new ImageTask(url, id_product, imageSize, myviewholder.ivcardIMG);
//        productimageTask.execute();


        myviewholder.tvname.setText(products.get(position).getProduct_Name());
        myviewholder.tvPrice.setText(String.valueOf(product.getProduct_Price()));

//        myviewholder.ivcardIMG.setImageResource(product.getImg());

        myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.productDetailFragment);
            }
        });



    }

    @NonNull


    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public void setProducts(List<Product> product) {
        this.products = product;
    }

    class Myviewholder extends RecyclerView.ViewHolder{
        private ImageView ivcardIMG;
        private TextView tvname,tvPrice;



        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            ivcardIMG = itemView.findViewById(R.id.ivcardIMG);
            tvname = itemView.findViewById(R.id.tvname);
            tvPrice = itemView.findViewById(R.id.tvprice);

        }
    }


}
