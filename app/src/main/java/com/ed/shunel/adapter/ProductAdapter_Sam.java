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

import java.util.List;

public class ProductAdapter_Sam extends RecyclerView.Adapter<ProductAdapter_Sam.Myviewholder>{
    private Context context;
    private List<Product> list;

    public ProductAdapter_Sam(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.item_view_product,parent,false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder myviewholder, int position) {

        Product product = list.get(position);
        myviewholder.tvname.setText(product.getProduct_Name());
//        myviewholder.tvPrice.setText(product.getProduct_Price());
        myviewholder.ivcardIMG.setImageResource(product.getImg());

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
        return list.size();
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
