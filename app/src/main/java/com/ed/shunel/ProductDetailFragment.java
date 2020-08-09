package com.ed.shunel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Order_Main;
import com.ed.shunel.bean.Product_List;
import com.ed.shunel.bean.Promotion;
import com.ed.shunel.bean.Shopping_Cart;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment {
    private final static String TAG = "ProductDetailFragment";
    private Activity activity;
    private ImageView iv_Prouduct;
    private ImageView iv_Like;
    private ImageView iv_Shoppcard;
    private TextView tv_Buy;
    private TextView tv_Dital;
    private TextView tvPdName;
    private TextView tvPdPrice;
    private TextView tvColor;
    private Spinner sp_Amount;
    private Product product, productSale;
    private Promotion promotionProduct;
    private Shopping_Cart shopping_cart;
    private User_Account user_account;
    private int select_Amount = 0;
    private CommonTask addTask, like, nowBuy;
    private SharedPreferences sharedPreferences;
    //    private int[] sp= {1,2,3,4,5,6,7,8,9,10};
    private ArrayAdapter<Integer> adapter;
    private List<Integer> list = new ArrayList<Integer>();
    private List<Product> productList = new ArrayList<>();
    String follow;
    JsonObject jsonObject1;
    private int totalPrice = 0;
    private Shopping_Cart_List shopping_cart_list;
    private Product_List product_list;
    private String product_id, promotionPrice;


    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_product_detail, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("===商品詳細頁面的flag===",""+MainActivity.flag);

        sharedPreferences = Common.getPreherences(activity);
        Log.e("TAG", "_____" + sharedPreferences.getString("id", ""));

        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();

        if (Common.networkConnected(activity)) {

            Bundle bundle = getArguments();
            product = (Product) bundle.getSerializable("product");
            promotionProduct = (Promotion) bundle.getSerializable("promotion");

            int product_id = product.getProduct_ID();
            String account_id = Common.getPreherences(activity).getString("id", "");
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "searchLike");
            jsonObject.addProperty("account_id", account_id);
            jsonObject.addProperty("product_id", product_id);
            Log.e("Tag 阿超我老婆", account_id + product_id);
            like = new CommonTask(url, jsonObject.toString());
            try {
                String like = this.like.execute().get();
                jsonObject1 = new Gson().fromJson(like, JsonObject.class);
                follow = jsonObject1.get("follow").getAsString();
                switch (follow) {
                    case "null": //未追蹤
                        iv_Like.setBackground(getResources().getDrawable(R.drawable.heart_empty));
                        break;
                    case "success": //追蹤
                        iv_Like.setBackground(getResources().getDrawable(R.drawable.heart_full));
                        break;
                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    private void findViews(View view) {

        iv_Prouduct = view.findViewById(R.id.ivPt);
        iv_Like = view.findViewById(R.id.iv_Like);
        iv_Shoppcard = view.findViewById(R.id.iv_Shoppcard);
        tv_Buy = view.findViewById(R.id.tv_Buy);
        tv_Dital = view.findViewById(R.id.tv_Dital);
        tvPdName = view.findViewById(R.id.tvPdName);
        tvPdPrice = view.findViewById(R.id.tvPdPrice);
        tvColor = view.findViewById(R.id.tvColor);
        sp_Amount = view.findViewById(R.id.sp_Amount);


        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("product") == null) {
            Common.showToast(activity, R.string.textNoFound);
            navController.popBackStack();
            return;
        }
        product = (Product) bundle.getSerializable("product");
        promotionProduct = (Promotion) bundle.getSerializable("promotion");

//        product_id = bundle.getString("product_id");
//        promotionPrice = bundle.getString("promotionPrice");

        showTest();

//


//        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(activity, layout.simple_spinner_item, sp);

    }

    private void showTest() {

        String url = Common.URL_SERVER + "Prouct_Servlet";
        int id = product.getProduct_ID();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        Bitmap bitmap = null;
//        int price = promotionProduct.getPromotion_Price();
        try {
            bitmap = new ImageTask(url, id, imageSize).execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            iv_Prouduct.setImageBitmap(bitmap);
        } else {
            iv_Prouduct.setImageResource(R.drawable.no_image);
        }

        if (product.getProduct_Name() != null && product.getProduct_Color() != null && product.getProduct_Ditail() != null) {
            tvPdName.setText("商品名稱：" + product.getProduct_Name());

            if (promotionProduct != null) {   //從促銷頁面過來
                tvPdPrice.setText("價格：" + String.valueOf(promotionProduct.getPromotion_Price()));
            } else {
                tvPdPrice.setText("價格：" + String.valueOf(product.getProduct_Price()));
            }
            tv_Dital.setText(product.getProduct_Ditail());
            tvColor.setText("規格：" + product.getProduct_Color());
        } else {

            if (Common.networkConnected(activity)) {
                String url1 = Common.URL_SERVER + "Prouct_Servlet";
                Gson gson = new Gson();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "findById");
                jsonObject.addProperty("PRODUCT_Id", product.getProduct_ID());
                String jsonOutSystem = jsonObject.toString();
                addTask = new CommonTask(url1, jsonOutSystem);
                try {
                    String jsonIn = addTask.execute().get();
                    productSale = gson.fromJson(jsonIn, Product.class);
                    Log.e(TAG, "-----------------------------------" + jsonIn);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);

            }

            tvPdPrice.setText("價格：" + String.valueOf(product.getProduct_Price()));

            //product 是接從promotion bundle過去的值


            tvPdName.setText("商品名稱：" + productSale.getProduct_Name());
            tv_Dital.setText(productSale.getProduct_Ditail());
            tvColor.setText("規格：" + productSale.getProduct_Color());


        }


    }

    private void initData() {

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);

        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<Integer>(activity, android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        sp_Amount.setAdapter(adapter);

    }

    private void setSystemServices() {
    }

    private void setLinstener() {


        sp_Amount.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                /* 将所选mySpinner 的值带入myTextView 中*/
                int selecte = adapter.getItem(arg2);
                select_Amount = selecte;
//                Log.e("---------------", String.valueOf(selecte));
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
//                myTextView.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });


        /*下拉菜单弹出的内容选项触屏事件处理*/
        sp_Amount.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                /**
                 *
                 */
                return false;
            }
        });

        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        sp_Amount.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
            }
        });

        iv_Shoppcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.networkConnected(activity)) {

                    if (Common.getPreherences(activity).getString("id", "").equals("")) {

                        Intent intent = new Intent();
                        intent.setClass(activity, LoginActivity.class);
                        startActivity(intent);
                    } else {


                        Shopping_Cart shopping_cart = null;
                        String account = Common.getPreherences(activity).getString("id", "");
                        Log.i(TAG, "id");
                        String url = Common.URL_SERVER + "Prouct_Servlet";
                        JsonObject jsonObject = new JsonObject();
                       if (promotionProduct != null){
                        shopping_cart = new Shopping_Cart(account, product.getProduct_ID(), product.getProduct_Name(), select_Amount, product.getProduct_Color(), promotionProduct.getPromotion_Price(), product.getProduct_MODIFY_DATE());
                       }else {
                         shopping_cart = new Shopping_Cart(account, product.getProduct_ID(), product.getProduct_Name(), select_Amount, product.getProduct_Color(), product.getProduct_Price(), product.getProduct_MODIFY_DATE());
                       }



                        jsonObject.addProperty("action", "addShop");
                        jsonObject.addProperty("ProductID", new Gson().toJson(shopping_cart));

                        int count = 0;

                        try {
                            addTask = new CommonTask(url, jsonObject.toString());

                            String result = addTask.execute().get();
                            Log.i(TAG, result);
                            count = Integer.parseInt(result);
                            Toast.makeText(activity, "添加購物車成功", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (count == 0) {
                            Toast.makeText(activity, R.string.Fail, Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }


        });


        //追蹤功能
        iv_Like.setOnClickListener(new View.OnClickListener() {

            String set = "";

            @Override
            public void onClick(View v) {
                if (Common.networkConnected(activity)) {
                    if (set.equals("like") || follow.equals("success")) {     //succes表示已經是追蹤了
                        String account_id = Common.getPreherences(activity).getString("id", "");
                        String url = Common.URL_SERVER + "Prouct_Servlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "deleteLike");
                        jsonObject.addProperty("account_id", account_id);
                        jsonObject.addProperty("product_id", product.getProduct_ID());
                        like = new CommonTask(url, jsonObject.toString());
                        try {
                            String rp = like.execute().get();
                            int count = Integer.parseInt(rp);

                            if (count == 1) {
                                iv_Like.setBackground(getResources().getDrawable(R.drawable.heart_empty));
                                Toast.makeText(activity, R.string.deleteFollow, Toast.LENGTH_SHORT).show();
                                set = "unlike";
                                follow = "null";
                            }


                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    } else if (set.equals("unlike") || follow.equals("null")) {                             //未追蹤
                        String account_id = Common.getPreherences(activity).getString("id", "");
                        String url = Common.URL_SERVER + "Prouct_Servlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "insertLike");
                        jsonObject.addProperty("account_id", account_id);
                        jsonObject.addProperty("product_id", product.getProduct_ID());
                        like = new CommonTask(url, jsonObject.toString());

                        try {
                            String rp = like.execute().get();
                            Log.e("=======count=====", rp + "");
                            int count = Integer.parseInt(rp);

                            if (count == 1) {
                                iv_Like.setBackground(getResources().getDrawable(R.drawable.heart_full));
                                Toast.makeText(activity, R.string.insertFollow, Toast.LENGTH_SHORT).show();
                                set = "like";
                                follow = "success";
                            }else{
                                Toast.makeText(activity,R.string.pleselogin,Toast.LENGTH_SHORT).show();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }


                }


            }
        });


//        直接購買
        tv_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.networkConnected(activity)) {

                    if (Common.getPreherences(activity).getString("id", "").equals("")) {

                        Intent intent = new Intent();
                        intent.setClass(activity, LoginActivity.class);
                        startActivity(intent);
                    } else {


                        String account = Common.getPreherences(activity).getString("id", "");
                        String name = Common.getPreherences(activity).getString("name", "");
                        String address = Common.getPreherences(activity).getString("address", "");
                        String phone = Common.getPreherences(activity).getString("phone", "");
                        Log.i(TAG, "id");
//                        product_list.setCart(product);
                        productList.add(product);
                        Product_List pl = new Product_List(productList);


                        Order_Main orderMain = new Order_Main(account, totalPrice, name, address, phone, 0);

//                        Order_Main orderMain = new Order_Main(account,totalPrice,name,address,phone,0);

//                        Order_Detail orderDetail = new Order_Detail()
                        String url = Common.URL_SERVER + "Prouct_Servlet";
                        JsonObject jsonObject = new JsonObject();


                        jsonObject.addProperty("action", "addOrderMain");
//                        jsonObject.addProperty("OrderID", new Gson().toJson(orderMain));
//                        jsonObject.addProperty("OrderDetail", new Gson().toJson(productList));

                        int count = 0;

                        try {
                            nowBuy = new CommonTask(url, jsonObject.toString());
//
                            String result = nowBuy.execute().get();
                            Log.i(TAG, result);
                            count = Integer.parseInt(result);
//                            Toast.makeText(activity, "添加購物車成功", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("nowBuy", pl);
                        bundle.putString("total", String.valueOf(totalPrice));
//                        再補傳送物件
//                        Navigation.findNavController(v).navigate(R.id.action_productDetailFragment_to_buyerFragment,bundle);
                    }


                }
            }
        });


    }
}




