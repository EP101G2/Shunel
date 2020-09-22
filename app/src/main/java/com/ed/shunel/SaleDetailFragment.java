package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Notice;
import com.ed.shunel.bean.Promotion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaleDetailFragment extends Fragment {
    private Activity activity;
    private RecyclerView rvSaleDetail;
    private TextView tvPromotionFNT, tvPromotionFND;
    private List<Promotion> promotion;
    private CommonTask noticeGetAllTask;
    private SaleAdapter SaleAdapter;
    private Notice notice;
    private String saleTitle, saleDetail;
    private ImageTask imageTask;
    private Product product;
    private int product_ID, promotionPrice;
    ImageView ivProductMini;
    TextView tvNoticeT;
    TextView tvNoticeD;
    CardView cvProduct;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Common.getPreherences(activity).edit().remove("noticeTitle").apply();
        Common.getPreherences(activity).edit().remove("noticeDetail").apply();
        Common.getPreherences(activity).edit().remove("product_ID").apply();

        //清除放在偏好設定的通知標題跟內文

        return inflater.inflate(R.layout.fragment_sale_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.flag = 0;
        Bundle bundle = getArguments();
        if (bundle != null) {
            saleTitle = bundle.getString("noticeTitle");
            saleDetail = bundle.getString("noticeDetail");
            product_ID = bundle.getInt("product_ID");
        }
        initData();
        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */

        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();
//        Bundle bundle = getArguments();

    }

    private void findViews(View view) {


        tvPromotionFNT = view.findViewById(R.id.tvPromotionFNT);
        tvPromotionFND = view.findViewById(R.id.tvPromotionFND);
        rvSaleDetail = view.findViewById(R.id.rvSaleDetail);
        cvProduct = view.findViewById(R.id.cvProduct);
        rvSaleDetail.setLayoutManager(new LinearLayoutManager(activity));
        rvSaleDetail.setAdapter(new SaleAdapter(activity, promotion));
        ivProductMini = view.findViewById(R.id.ivProductMini);
        tvNoticeT = view.findViewById(R.id.tvNoticeT);
        tvNoticeD = view.findViewById(R.id.tvNoticeD);

        if (product_ID != 0) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
//            Log.e("555555","product_id:"+product_id);
            int imageSize = getResources().getDisplayMetrics().widthPixels / 5;
//            Bitmap bitmap = null;
            try {
                imageTask = (ImageTask) new ImageTask(url, product_ID, imageSize, ivProductMini).execute();

            } catch (Exception e) {
//                Log.e(TAG, e.toString());
                e.printStackTrace();
            }


            cvProduct.setVisibility(View.VISIBLE);
            rvSaleDetail.setVisibility(View.GONE);
            tvNoticeT.setText(product.getProduct_Name());
            tvNoticeD.setText(product.getProduct_Color());


        }
    }


    private void initData() {
        if (product_ID != 0) {

            product = getProductData();

        } else {
            promotion = getPromotionDate();
        }


    }


    private void setSystemServices() {

    }


    private void setLinstener() {
        tvPromotionFNT.setText(saleTitle);
        tvPromotionFND.setText(saleDetail);
        cvProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("product.getProduct_Status()", "" + product.getProduct_Status());


                if (product.getProduct_Status() == 2) {
                    product.setProduct_Price(getPromotionPrice());
                    Log.e("getPromotionPrice", "getPromotionPrice" + getPromotionPrice());
                }

                Bundle bundle = new Bundle();

                bundle.putSerializable("product", product);
                bundle.putInt("number", 2);


                Navigation.findNavController(v).navigate(R.id.action_saleDetailFragment_to_productDetailFragment, bundle);

            }
        });
    }

    private int getPromotionPrice() {

        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Promotion_Servlet";
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getPromotionPrice");
            jsonObject.addProperty("PRODUCT_Id", product_ID);
            String jsonOut = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String JsonIn = noticeGetAllTask.execute().get();
                promotionPrice = Integer.parseInt(JsonIn);
                Log.e(" promotionPrice", "promotionPrice" + promotionPrice);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("product", "product:" + product.getProduct_Price());
        return promotionPrice;
    }

    private Product getProductData() {
        product = null;

        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findById");
            jsonObject.addProperty("PRODUCT_Id", product_ID);
            String jsonOut = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String JsonIn = noticeGetAllTask.execute().get();

                product = gson.fromJson(JsonIn, Product.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }

        return product;
    }


    private List<Promotion> getPromotionDate() {


        Log.i("---123---", "-----------12323");
        List<Promotion> promotions = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Promotion_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getPromotionAll");
            String jsonOut = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = noticeGetAllTask.execute().get();
                Type listType = new TypeToken<List<Promotion>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                參考Android web範例：jsonex
                promotions = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return promotions;
    }


    private class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.Myviewholder> {
        Context context;
        List<Promotion> promotionList;

        public SaleAdapter(Context context, List<Promotion> promotions) {
            this.context = context;
            this.promotionList = promotions;

        }

        @NonNull
        @Override
        public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_view_notice, parent, false);
            return new Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
            Log.e("test", "測試");
            final Promotion promotion = promotionList.get(position);

//            Log.e(TAG,"Name"+promotion.getPromotion_Name());
//            Log.e(TAG,"Promotion"+promotion.getPromotion_ID());
//            Log.e(TAG,"Price"+promotion.getPromotion_Price());
//            Log.e(TAG,"ID"+promotion.getProuct_ID());
            String url = Common.URL_SERVER + "Prouct_Servlet";
            int product_id = promotion.getProuct_ID();
//            Log.e("555555","product_id:"+product_id);
            int imageSize = getResources().getDisplayMetrics().widthPixels / 5;
//            Bitmap bitmap = null;
            try {
                imageTask = (ImageTask) new ImageTask(url, product_id, imageSize, holder.ivProductMini).execute();

            } catch (Exception e) {
//                Log.e(TAG, e.toString());
                e.printStackTrace();
            }

//            if (bitmap != null) {
//                holder.ivProductMini.setImageBitmap(bitmap);
//            } else {
//                holder.ivProductMini.setImageResource(R.drawable.no_image);
//            }
            holder.tvNoticeT.setText(promotion.getPromotion_Name());
            holder.tvNoticeD.setText(String.valueOf(promotion.getPromotion_Price()));

            final Product product = new Product(promotion.getProuct_ID(), promotion.getPromotion_Price());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("product", product);
                    bundle.putInt("number", 4);
                    Navigation.findNavController(v).navigate(R.id.action_saleDetailFragment_to_productDetailFragment, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            Log.e("promotionList", "promotionList");
            return promotionList == null ? 0 : promotionList.size();

        }


        private class Myviewholder extends RecyclerView.ViewHolder {
            ImageView ivProductMini;
            TextView tvNoticeT;
            TextView tvNoticeD;


            public Myviewholder(View view) {
                super(view);
                ivProductMini = view.findViewById(R.id.ivProductMini);
                tvNoticeT = view.findViewById(R.id.tvNoticeT);
                tvNoticeD = view.findViewById(R.id.tvNoticeD);

            }
        }
    }
}
