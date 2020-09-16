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
import android.widget.SearchView;
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
    private SearchView svOrderStatus1;
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
//        setting search view
        svOrderStatus1 = view.findViewById(R.id.svOrderStatus1);
        svOrderStatus1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                OrderMainAdapter1 adapter = (OrderMainAdapter1) rvOdLStatus1.getAdapter(); //強迫子型recyclerview.getadapter轉型成父型friendadapter
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
        Type listType;
        try {
            if (Common.networkConnected(activity)) {
//                get data from orders servlet
                String url = Common.URL_SERVER + "Orders_Servlet";

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); //add dates in layout
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrderMains");
                jsonObject.addProperty("Account_ID", Common.getPreherences(activity).getString("id", "id"));
                jsonObject.addProperty("status", 0);//get status 0 and 1
                jsonObject.addProperty("status1", 1);
                String jsonOut = jsonObject.toString();

                orderListGetTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = orderListGetTask.execute().get();
//                    Log.e(TAG,"getOrders: "+jsonIn);
                           listType = new TypeToken<List<Order_Main>>() {
                           }.getType();
                           orderMainList = gson.fromJson(jsonIn, listType);
//                    Log.e(TAG,"getOrders: " +orderMainList);
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

            Log.e(TAG,"--onBindViewHolder--> id: "+orderMain.getOrder_ID());
            Log.e(TAG,"--onBindViewHolder--> status: "+orderMain.getOrder_Main_Order_Status());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Order", orderMain);
                    Navigation.findNavController(v).navigate(R.id.action_status1Fragment_to_orderDetailFragment2, bundle);
//                    Log.e(TAG, "bundel: "+orderMain);
                }
            });
        }

        private String orderStatusText(int status) {
            String statusText = "";
            Log.e(TAG, "status這是111111"+status);
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