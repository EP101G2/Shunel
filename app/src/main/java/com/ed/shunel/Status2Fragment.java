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
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.Order_Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Status2Fragment extends Fragment {

    private static final String TAG = "---Orders, status2---";
    private Activity activity;
    private SwipeRefreshLayout srlOdStatus2;
    private RecyclerView rvOdLStatus2;
    List<Order_Main> orderMainList;
    //    Order_Main orderMain;
    private CommonTask orderListGetTask;

    public Status2Fragment() {
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
        return inflater.inflate(R.layout.fragment_status2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        get data
        orderMainList = getOrders();
//        showOrders(getOrders());

//        set recycler view
        rvOdLStatus2 = view.findViewById(R.id.rvOdLStatus2);
        rvOdLStatus2.setLayoutManager(new LinearLayoutManager(activity));
        rvOdLStatus2.setAdapter(new Status2Fragment.OrderMainAdapter2(getContext(), orderMainList));

//        set swipeRefreshLayout
        srlOdStatus2 = view.findViewById(R.id.srlOdStatus2);
        srlOdStatus2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //讀取的圈圈 動畫
                srlOdStatus2.setRefreshing(true);
                showOrders(getOrders());//method in next paragraph
                //直到讀取完 結束
                srlOdStatus2.setRefreshing(false);
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
                jsonObject.addProperty("status", 2);
                String jsonOut = jsonObject.toString();
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
            Status2Fragment.OrderMainAdapter2 orderMainAdapter = (Status2Fragment.OrderMainAdapter2) rvOdLStatus2.getAdapter();
//            nullPointerException //no mind?
            // 如果spotAdapter不存在就建立新的，否則續用舊有的
            if (orderMainAdapter == null) {
                rvOdLStatus2.setAdapter(new Status2Fragment.OrderMainAdapter2(activity, orderMainList));
            } else {
                orderMainAdapter.setOrders(getOrders());//get new
                orderMainAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    ---Adapter---
    private class OrderMainAdapter2 extends RecyclerView.Adapter<Status2Fragment.OrderMainAdapter2.PageViewHolder> {
        //        initialize
        private LayoutInflater layoutInflater;
        Context context;
        List<Order_Main> orderMainList;

        public OrderMainAdapter2(Context context, List<Order_Main> orderMainList) {
            this.context = context;
            this.orderMainList = orderMainList;
            layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public Status2Fragment.OrderMainAdapter2.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_orderlist_view, parent, false);
            return new Status2Fragment.OrderMainAdapter2.PageViewHolder(view);
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            TextView tvOrderId, tvOrderDate, tvOrderStatus;
            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
                tvOrderId = itemView.findViewById(R.id.tvOrderId);
                tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull Status2Fragment.OrderMainAdapter2.PageViewHolder holder, int position) {
            final Order_Main orderMain = orderMainList.get(position);
            holder.tvOrderId.setText(String.valueOf(orderMain.getOrder_ID()));
            holder.tvOrderStatus.setText(R.string.textDelivered);
            holder.tvOrderDate.setText(orderMain.getOrder_Main_Order_Date().toString());

            Log.e(TAG,"--onBindViewHolder-->"+orderMain.getOrder_ID());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Order", orderMain);
                    Navigation.findNavController(v).navigate(R.id.action_status2Fragment_to_orderDetailFragment2, bundle);
                }
            });
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