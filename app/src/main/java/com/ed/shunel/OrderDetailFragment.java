package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
//import com.ed.shunel.bean.Order_Detail;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Order_Detail;
import com.ed.shunel.bean.Order_Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//1. rv orderDetail, 2. nav main to product detail
public class OrderDetailFragment extends Fragment {
    private static final String TAG = "-OrderDetailFragment-";
    private TextView tvOrderIdDet, tvOrderStatus, tvTotalPrice, tvName, tvPhone, tvAddress;// tvProductName, tvProductPrice;
    //    private TextView tvOrderIdText, tvOrderDetStatusText, tvTotalPriceText, tvReceiverTitle, tvOrderDNameT, tvOrderDPhoneT, tvOrderDetailAddressT; need not to
//    private ImageView ivOrderProductPic;//add later
    private RecyclerView rvOrderDetProList;
    private List<Order_Detail> orderDetailList;
    private Order_Main orderMain;
    private Activity activity;
    private Integer counter;
    private CommonTask ordersDetGetTask;
    private int productId;

    public OrderDetailFragment(){
//        Required empty public constructor
    }

//    public static OrderDetailFragment newInstance(Integer counter) {
//        OrderDetailFragment fragment = new OrderDetailFragment();
//        Bundle args = new Bundle();
//        args.putInt(TAG, counter);
//        fragment.setArguments(args);
//        return fragment;
//    }//ok

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null){
            counter = getArguments().getInt(TAG);
        }
    }//ok

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }//ok

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvOrderIdDet = view.findViewById(R.id.tvOrderIdDet);
        tvOrderStatus = view.findViewById(R.id.tvOrderStatusDet);
        tvName = view.findViewById(R.id.tvNameDet);//=Receiver in DB
        tvPhone = view.findViewById(R.id.tvPhoneDet);
        tvAddress = view.findViewById(R.id.tvAddressDet);
        tvTotalPrice = view.findViewById(R.id.tvTotalPriceDet);
//        bundle bring orderId, orderStatus, from OrderListFrag
        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("Order") == null) {
            Common.showToast(activity, R.string.textnofound);
            navController.popBackStack();
            return;
        }
        orderMain = (Order_Main) bundle.getSerializable("Order");

        Log.e(TAG, "bundleGet"+orderMain.getOrder_ID()+","+orderMain.getOrder_Main_Order_Status()+","+orderMain.getOrder_Main_Total_Price()+","+orderMain.getOrder_Main_Receiver());

        showOrderDetails(orderMain);

        orderDetailList = getOrderDetailList();
        rvOrderDetProList = view.findViewById(R.id.rvOrderDetProList);
        rvOrderDetProList.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderDetProList.setAdapter(new OrderDetailAdapter(getContext(),orderDetailList)); //rvAdapter
    }

    private List<Order_Detail> getOrderDetailList(){
        List<Order_Detail> orderDetailList = new ArrayList<>();
        try {
            int orderId = Integer.parseInt(tvOrderIdDet.getText().toString());
            Log.e(TAG, "getOrderIdFromTvOrderIdDet: "+orderId);
            if (Common.networkConnected(activity)) {
//                get data from orders servlet
                String url = Common.URL_SERVER + "Orders_Servlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrderedProducts");
                jsonObject.addProperty("order_Id", orderId);
                String jsonOut = jsonObject.toString();
                ordersDetGetTask = new CommonTask(url, jsonOut);
                Log.e(TAG, "getOrderedProducts: out -> "+jsonOut);
                try {
                    String jsonIn = ordersDetGetTask.execute().get();
                    Type listType = new TypeToken<List<Order_Detail>>() {
                    }.getType();
                    orderDetailList = new Gson().fromJson(jsonIn, listType);
                    for (Order_Detail od:orderDetailList
                         ) {
                        productId = od.getorderDetProductId();
                        Log.e(TAG, "productid get: "+productId);
                    }
                    Log.e(TAG, "getOrderedProducts: in -> "+jsonIn);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderDetailList;
    }

    private void showOrderDetails(Order_Main orderMain) {
        int id = orderMain.getOrder_ID();

        tvOrderIdDet.setText(String.valueOf(id));
        String orderIdStr = tvOrderIdDet.getText().toString();
        Log.e(TAG, "showOrderDetail-orderId: "+orderIdStr);

        tvOrderStatus.setText(orderStatusText(orderMain.getOrder_Main_Order_Status()));
        tvTotalPrice.setText(String.valueOf(orderMain.getOrder_Main_Total_Price()));

        tvName.setText(orderMain.getOrder_Main_Receiver());
        tvPhone.setText(orderMain.getOrder_Main_Phone());
        tvAddress.setText(orderMain.getOrder_Main_Address());

////        get receiver name, phone, address by orderId from server(not working, can't see message)
//        String url = Common.URL_SERVER + "Orders_Servlet";
//        final Gson gson = new Gson();
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "getOrderMain");
//        jsonObject.addProperty("orderID", new Gson().toJson(id));
//        ordersDetGetTask = new CommonTask(url, jsonObject.toString());
//
//        try {
//            String jsonIn = ordersDetGetTask.execute().get();
//            Log.e(TAG, jsonIn);
//
////            JsonObject jsonObjectRec = gson.fromJson(jsonIn, JsonObject.class);
////            final String result = jsonObjectRec.get("orderMain").getAsString();
//            Order_Main orderMainRec = gson.fromJson(jsonIn, Order_Main.class);
//
//            tvName.setText(orderMainRec.getOrder_Main_Receiver());
//            tvPhone.setText(orderMainRec.getOrder_Main_Phone());
//            tvAddress.setText(orderMainRec.getOrder_Main_Address());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private String orderStatusText(int status) {
        String statusText = "";
//            Log.e(TAG, "status"+status);
        if (status == 0){
            statusText = "未付款";
        }else if (status == 1){
            statusText = "未出貨";
        }else if (status == 2){
            statusText = "已出貨";
        }else if (status == 3){
            statusText = "已送達";
        }else if (status == 4){
            statusText = "已取消";
        }else if (status == 5){
            statusText = "已退貨";
        }
        return statusText;
    }

    //adapter
    private class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.PageViewHolder>{
        Context context;
        List<Order_Detail> orderDetailList;
        ImageTask orderDetProdImgTask;
        private int imageSize;
//        private LayoutInflater inflater;

    public OrderDetailAdapter (Context context, List<Order_Detail> orderDetailList){
        this.context = context;
        this.orderDetailList = orderDetailList;
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 100;
//        inflater = LayoutInflater.from(context);
    }//ok

        @Override
        public int getItemCount() {
            try {
                if (orderDetailList != null) {
                    Log.e(TAG,"itemCount:"+orderDetailList.size());
                    return orderDetailList == null ? 0 : orderDetailList.size();
                }
            }catch (Exception e){
                Log.e(TAG,"null list");
            }
            return orderDetailList == null ? 0 : orderDetailList.size();
        }//ok

        public void setOrderedProduct(List<Order_Detail> orderDetailList) {
            this.orderDetailList = orderDetailList;
            Log.e(TAG, "orderDetList"+orderDetailList);
        }

        private class PageViewHolder extends RecyclerView.ViewHolder {
            //           TextView tvOrderId, tvOrderStatus, tvTotalPrice, tvName, tvPhoneNum, tvAddress;
            TextView tvProductName, tvProductPrice;
            ImageView ivOrderProductPicDet;

            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductNameOD);
                tvProductPrice = itemView.findViewById(R.id.tvProductPriceOD);
                ivOrderProductPicDet = itemView.findViewById(R.id.ivOrderProductPicDet);
            }
        }

        @NonNull
        @Override
        public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_orderdetail_view, parent, false);
            return new PageViewHolder(view);
        }//ok

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) { //after added orderId & orderStatus to orderDetail(checked), modify this section
            final Order_Detail orderDetail = orderDetailList.get(position);
            try {
                String url = Common.URL_SERVER + "Prouct_Servlet";
                int productIdOD = orderDetail.getorderDetProductId();
                orderDetProdImgTask = new ImageTask(url, productIdOD, imageSize,holder.ivOrderProductPicDet);
                orderDetProdImgTask.execute();
//                holder.ivOrderProductPicDet.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }


            holder.tvProductName.setText(orderDetail.getProduct_Name());
            holder.tvProductPrice.setText("$" + orderDetail.getOrder_Detail_Buy_Price());
//            holder.ivOrderProductPicDet.setImageResource(R.drawable.photos_pink);

//            get product pic through product ID
            Log.e(TAG, "orderDetail's id: "+orderDetail.getorderDetProductId());



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //navigation to productDetail
//                    Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_productDetailFragment);//follow the main name of productDet
                }
            });
        } //need to be fixed!!

    }


}