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
import android.widget.SearchView;
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


public class Status4Fragment extends Fragment {

    private static final String TAG = "---Orders, status4---";
    private Activity activity;
    private SwipeRefreshLayout srlOdStatus4;
    private RecyclerView rvOdLStatus4;
    private SearchView svOrderStatus4;
    List<Order_Main> orderMainList;
    //    Order_Main orderMain;
    private CommonTask orderListGetTask;

    public Status4Fragment() {
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
        return inflater.inflate(R.layout.fragment_status4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        get data
        orderMainList = getOrders();
//        showOrders(getOrders());

//        set recycler view
        rvOdLStatus4 = view.findViewById(R.id.rvOdLStatus4);
        rvOdLStatus4.setLayoutManager(new LinearLayoutManager(activity));
        rvOdLStatus4.setAdapter(new Status4Fragment.OrderMainAdapter4(getContext(), orderMainList));

//        set swipeRefreshLayout
        srlOdStatus4 = view.findViewById(R.id.srlOdStatus4);
        srlOdStatus4.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //讀取的圈圈 動畫
                srlOdStatus4.setRefreshing(true);
                showOrders(getOrders());//method in next paragraph
                //直到讀取完 結束
                srlOdStatus4.setRefreshing(false);
            }
        });

//        setting search view
        svOrderStatus4 = view.findViewById(R.id.svOrderStatus4);
        svOrderStatus4.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Status4Fragment.OrderMainAdapter4 adapter = (Status4Fragment.OrderMainAdapter4) rvOdLStatus4.getAdapter(); //強迫子型recyclerview.getadapter轉型成父型friendadapter
                try{
                    if (adapter != null) { //先檢查是否為空值：空值會執行錯誤
                        // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                        if (newText.isEmpty()) { //空字串“” =/ 空值 null ＝/ 空白字串"/s"; 空值呼叫方法會造成執行錯誤：nullpointer exception
                            adapter.setOrders(orderMainList);
                        } else {
                            List<Order_Main> searchOrders = new ArrayList<>();
                            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                            for (Order_Main orderMain : orderMainList) { //get searched data from origin data
//                            search by account id
                                if (orderMain.getAccount_ID().toUpperCase().contains(newText.toUpperCase())) { //ignore upper/lower case: all input change into upper case
                                    searchOrders.add(orderMain);
                                }
//                            search by orderId
                                else if (orderMain.getOrder_ID() == Integer.parseInt(newText)) { //turn newtext into int and compare to orderid
                                    searchOrders.add(orderMain);
                                }
                            }
                            adapter.setOrders(searchOrders);
                        }
                        adapter.notifyDataSetChanged(); //重刷畫面
                        return true;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
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
                jsonObject.addProperty("status", 4);
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
            Status4Fragment.OrderMainAdapter4 orderMainAdapter = (Status4Fragment.OrderMainAdapter4) rvOdLStatus4.getAdapter();
//            nullPointerException //no mind?
            // 如果spotAdapter不存在就建立新的，否則續用舊有的
            if (orderMainAdapter == null) {
                rvOdLStatus4.setAdapter(new Status4Fragment.OrderMainAdapter4(activity, orderMainList));
            } else {
                orderMainAdapter.setOrders(getOrders());//get new
                orderMainAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    ---Adapter---
    private class OrderMainAdapter4 extends RecyclerView.Adapter<Status4Fragment.OrderMainAdapter4.PageViewHolder> {
        //        initialize
        private LayoutInflater layoutInflater;
        Context context;
        List<Order_Main> orderMainList;

        public OrderMainAdapter4(Context context, List<Order_Main> orderMainList) {
            this.context = context;
            this.orderMainList = orderMainList;
            layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public Status4Fragment.OrderMainAdapter4.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_orderlist_view, parent, false);
            return new Status4Fragment.OrderMainAdapter4.PageViewHolder(view);
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
        public void onBindViewHolder(@NonNull Status4Fragment.OrderMainAdapter4.PageViewHolder holder, int position) {
            final Order_Main orderMain = orderMainList.get(position);
            holder.tvOrderId.setText(String.valueOf(orderMain.getOrder_ID()));
            holder.tvOrderStatus.setText(R.string.textCanceled);
            holder.tvOrderDate.setText(orderMain.getOrder_Main_Order_Date().toString());

            Log.e(TAG,"--onBindViewHolder-->"+orderMain.getOrder_ID());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Order", orderMain);
                    Navigation.findNavController(v).navigate(R.id.action_status4Fragment_to_orderDetailFragment2, bundle);
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