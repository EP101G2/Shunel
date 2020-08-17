package com.ed.shunel.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Promotion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PromotionAdapter_Sam extends RecyclerView.Adapter<PromotionAdapter_Sam.Myviewholder>{
    private Context context;
    private List<Promotion> promotion ;
    private int imageSize;
    private ImageTask productimageTask;
    private CommonTask productGetAllTask;
    private List<Product> products;

    public PromotionAdapter_Sam(Context context, List<Promotion> promotion) {
        this.context = context;
        this.promotion = promotion;
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_promotion, parent, false);
        return new PromotionAdapter_Sam.Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        final Promotion promotionProduct = promotion.get(position);
        products = getProduct();
        final Product product = products.get(position);

        String url = Common.URL_SERVER + "Prouct_Servlet";
       int id_product = promotionProduct.getProduct_ID();
        productimageTask = new ImageTask(url, id_product, imageSize, holder.ivCardIMG);
        productimageTask.execute();
        holder.tvName.setText(promotionProduct.getProduct_Name());
        holder.tvOldPrice.setText("$"+promotionProduct.getProduct_Price());
        holder.tvNewPrice.setText("$"+promotionProduct.getPromotion_Price());
        holder.tvDate.setText(promotionProduct.getDate_End().toString().substring(0,10));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("promotion", promotionProduct);
                //bundle.putSerializable("product",  product);
                Navigation.findNavController(v).navigate(R.id.productDetailFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotion == null ? 0 : promotion.size();

    }


    public void setpromotionProducts(List<Promotion> promotion) {
        this.promotion = promotion;
    }


    class Myviewholder extends RecyclerView.ViewHolder {
        private ImageView ivCardIMG, ivTop;
        private TextView tvName, tvOldPrice,tvNewPrice,tvDate;


        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            ivCardIMG = itemView.findViewById(R.id.ivCardIMG);
            tvName = itemView.findViewById(R.id.tvName);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvNewPrice = itemView.findViewById(R.id.tvNewPrice);
            tvDate = itemView.findViewById(R.id.tvDate);


        }
    }




    private List<Product> getProduct() {
        List<Product> products = null;
        if (Common.networkConnected((Activity)context)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getSaleProduct");
            productGetAllTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = productGetAllTask.execute().get();
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                products = new Gson().fromJson(jsonIn, listType);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(context, R.string.textNoNetwork);
        }
        Log.e("--------------",products+"");
        return products;
    }


}
