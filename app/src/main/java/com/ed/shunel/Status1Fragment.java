package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Order_Detail;
import com.ed.shunel.bean.Order_Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Status1Fragment extends Fragment {
    private static final String TAG = "---Orders, status1---";
    private Activity activity;
    private SwipeRefreshLayout srlOdStatus1;
    private RecyclerView rvOdLStatus1;
    List<Order_Main> orderMainList;
    //    Order_Main orderMain;
    private CommonTask orderListGetTask;

    public Status1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        get data
        orderMainList = getOrders();
//        showOrders(getOrders());

//        set recycler view
        rvOdLStatus1 = view.findViewById(R.id.rvOdLStatus1);
        rvOdLStatus1.setLayoutManager(new LinearLayoutManager(activity));
        rvOdLStatus1.setAdapter(new Status1Fragment.OrderMainAdapter1(getContext(), orderMainList));

//        set swipeRefreshLayout
        srlOdStatus1 = view.findViewById(R.id.srlOdStatus1);
        srlOdStatus1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //讀取的圈圈 動畫
                srlOdStatus1.setRefreshing(true);
                showOrders(getOrders());//method in next paragraph
                //直到讀取完 結束
                srlOdStatus1.setRefreshing(false);
            }
        });
    }

    private List<Order_Main> getOrders() {
        List<Order_Main> orderMainList = new ArrayList<>();

        try {
            if (Common.networkConnected(activity)) {
//                get data from orders servlet
                String url = Common.URL_SERVER + "Orders_Servlet";
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //add dates in layout
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrderMains");
                jsonObject.addProperty("Account_ID", Common.getPreherences(activity).getString("id", "id"));
//                jsonObject.addProperty("Account_ID", orderMain.getAccount_ID()); //get account id from preference?
                jsonObject.addProperty("status", 0);//get status 0 and 1
                jsonObject.addProperty("status1", 1);//get status 0 and 1
                String jsonOut = jsonObject.toString();
                Log.e("jsonOut","這是jsonOut"+jsonOut);
                orderListGetTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = orderListGetTask.execute().get();
                    Type listType = new TypeToken<List<Order_Main>>() {
                    }.getType();
                    orderMainList = gson.fromJson(jsonIn, listType);
                    Log.e(TAG, jsonIn);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return orderMainList;
    }

    private void showOrders(List<Order_Main> orderMainList) {
        //        Log.e(TAG, "orderMainList: "+orderMainList); //get success
        try{
            if (orderMainList == null || orderMainList.isEmpty()) {
                Common.showToast(activity, R.string.textnofound);
            }
            Status1Fragment.OrderMainAdapter1 orderMainAdapter = (Status1Fragment.OrderMainAdapter1) rvOdLStatus1.getAdapter();
//            nullPointerException //no mind?
            // 如果spotAdapter不存在就建立新的，否則續用舊有的
            if (orderMainAdapter == null) {
                rvOdLStatus1.setAdapter(new Status1Fragment.OrderMainAdapter1(activity, orderMainList));
            } else {
                orderMainAdapter.setOrders(getOrders());//get new
                orderMainAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    ---Adapter---
    private class OrderMainAdapter1 extends RecyclerView.Adapter<Status1Fragment.OrderMainAdapter1.PageViewHolder> {
        //        initialize
        private LayoutInflater layoutInflater;
        Context context;
        List<Order_Main> orderMainList;
//        Order_Detail orderDetail;
//        private ImageTask orderDetProdImgTask;
//        private int imageSize;

        public OrderMainAdapter1(Context context, List<Order_Main> orderMainList) {
            this.context = context;
            this.orderMainList = orderMainList;
            layoutInflater = LayoutInflater.from(context);
//            imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;

        }

        @NonNull
        @Override
        public Status1Fragment.OrderMainAdapter1.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_orderlist_view, parent, false);
            return new Status1Fragment.OrderMainAdapter1.PageViewHolder(view);
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            TextView tvOrderId, tvOrderDate, tvOrderStatus;
//            ImageView ivOrderProductPic;
            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
                tvOrderId = itemView.findViewById(R.id.tvOrderId);
                tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
//                ivOrderProductPic = itemView.findViewById(R.id.ivOrderProductPic);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull Status1Fragment.OrderMainAdapter1.PageViewHolder holder, int position) {
            final Order_Main orderMain = orderMainList.get(position);
            holder.tvOrderId.setText(String.valueOf(orderMain.getOrder_ID()));
            holder.tvOrderStatus.setText(orderStatusText(orderMain.getOrder_Main_Order_Status()));
            holder.tvOrderDate.setText(orderMain.getOrder_Main_Order_Date().toString());

//            try {
//                String url = Common.URL_SERVER + "Prouct_Servlet";
//                int productIdOM = orderDetail.getProduct_ID();
//                Log.e(TAG, "productId: "+productIdOM);
//                orderDetProdImgTask = new ImageTask(url, productIdOM, imageSize, holder.ivOrderProductPic);
//                orderDetProdImgTask.execute();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            Log.e(TAG,"--onBindViewHolder-->"+orderMain.getOrder_ID());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Order", orderMain);
                    Navigation.findNavController(v).navigate(R.id.action_status1Fragment_to_orderDetailFragment2, bundle);
                    Log.e(TAG, "bundeled: "+orderMain);
                }
            });
        }

        private String orderStatusText(int status) {
            String statusText = "";
//            Log.e(TAG, "status"+status);
            if (status == 0) {
                statusText = "未付款";
            } else if (status == 1) {
                statusText = "未出貨";
            }
            return statusText;
        }

        @Override
        public int getItemCount() {
            try {
                if (orderMainList != null) {
                    Log.e(TAG,"itemCount:"+orderMainList.size());
                    return orderMainList == null ? 0 : orderMainList.size();
                }
            }catch (Exception e){
                Log.e(TAG,"null list");
            }
            return orderMainList == null ? 0 : orderMainList.size();
        }

        public void setOrders(List<Order_Main> orderMainList) {
            this.orderMainList = orderMainList;
        }
    }
}