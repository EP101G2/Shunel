package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Order_Main;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//fragment of OrderListMain, with rvOrderList
//    change into view pager
public class OrderListFragment extends Fragment{
    private static final String TAG = "-OrderListFragment-";
    private Activity activity;
    private Integer counter;
    private ImageButton btNotYetDelivered, btDelivered, btReceived, btCanceled, btRefounded;
    private Button btBack;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageTask orderImageTask;
    private User_Account userAccount;
    List<Order_Main> orderListMain;
    RecyclerView rvOrderList;
    private CommonTask ordersListGetTask;

    public OrderListFragment(){
//        Required empty public constructor
    }

    public static OrderListFragment newInstance(Integer counter){
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt(TAG, counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null){
            counter = getArguments().getInt(TAG);
        }
    }
    //  main return
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_order_list, container, false);
    }

//    get the bundle with accountId from Member Fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        bring accountId from bundle?
        rvOrderList = view.findViewById(R.id.rvOrderList);
//        rvOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrderList.setAdapter(new OrderListAdapter(getContext(), orderListMain));
        rvOrderList.setLayoutManager(new LinearLayoutManager(activity));

        orderListMain = getOrders();
        showOrders(orderListMain);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutOrder);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //讀取的圈圈 動畫
                swipeRefreshLayout.setRefreshing(true);
                showOrders(getOrders());
                //直到讀取完 結束
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btNotYetDelivered = view.findViewById(R.id.btNotYetDelivered);
        btDelivered = view.findViewById(R.id.btDelivered);
        btReceived = view.findViewById(R.id.btReceived);
        btCanceled = view.findViewById(R.id.btCanceled);
        btRefounded = view.findViewById(R.id.btRefounded);
//        set up filter when click image (change to btn??)
        btNotYetDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
                adapter.getFilter().filter("0"); //?? setting the key: orderstatus.case0
            }
        });
        btDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
                adapter.getFilter().filter("1"); //?? setting the key: orderstatus.case1
            }
        });
        btReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
                adapter.getFilter().filter("2"); //?? setting the key: orderstatus.case2
            }
        });
        btCanceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
                adapter.getFilter().filter("3"); //?? setting the key: orderstatus.case3
            }
        });
        btRefounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
                adapter.getFilter().filter("4"); //?? setting the key: orderstatus.case4
            }
        });

//        click on arrow left and go back to the previous page
        Button btBack = view.findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 效果與手機上的Back按鍵相同
                Navigation.findNavController(v).popBackStack();
            }
        });//ok

    }

    private List<Order_Main> getOrders() {
        List<Order_Main> orderListMain = new ArrayList<>();
        try {
            if (Common.networkConnected(activity)) {
//                get data from orders servlet
                String url = Common.URL_SERVER + "Orders_Servlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrderMainShort");
                jsonObject.addProperty("Account_ID", Common.getPreherences(activity).getString("Account_ID", "defValue")); //for testing
                String jsonOut = jsonObject.toString();
                ordersListGetTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = ordersListGetTask.execute().get();
                    Type listType = new TypeToken<List<Order_Main>>() {
                    }.getType();
                    orderListMain = new Gson().fromJson(jsonIn, listType);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return orderListMain;
    }

    private void showOrders(List<Order_Main> orderListMain) {
        if (orderListMain == null || orderListMain.isEmpty()) {
            Common.showToast(activity, R.string.textnofound);
        }
        OrderListAdapter orderListAdapter = (OrderListAdapter) rvOrderList.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (orderListAdapter == null) {
            rvOrderList.setAdapter(new OrderListAdapter(activity, orderListMain));
        } else {
            orderListAdapter.setOrders(orderListMain);
            orderListAdapter.notifyDataSetChanged();
        }
    }
    //    adapter for orderList
    private class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.PageViewHolder> {//implements Filterable
        private LayoutInflater layoutInflater;
        private int imageSize;
        Context context;
        List<Order_Main> orderListMain;
        List<Order_Main> sortedOrderList = new ArrayList<>();
        StatusFilter statusFilter;//initialise a filter

        public OrderListAdapter(Context context, List<Order_Main> orderListMain) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.orderListMain = orderListMain;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setOrders(List<Order_Main> orderListMain) {
            this.orderListMain = orderListMain;
        }

        //    inner class PageViewHolder for the holding of recycler view
        class PageViewHolder extends RecyclerView.ViewHolder {
            ImageView ivOrderProductPic;
            TextView tvOrderId, tvOrderStatus, tvOrderIdText,tvOrderStatusText;
            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                ivOrderProductPic = itemView.findViewById(R.id.ivOrderProductPic);
                tvOrderIdText = itemView.findViewById(R.id.tvOrderIdText);
                tvOrderId = itemView.findViewById(R.id.tvOrderId);
                tvOrderStatusText = itemView.findViewById(R.id.tvOrderStatusText);
                tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            }
        }//ok

        public int getItemCount(){
            try {
                if (orderListMain != null) {
                    Log.e(TAG,"itemCount:"+orderListMain.size());
                    return orderListMain == null ? 0 : orderListMain.size();
                }
            }catch (Exception e){
                Log.e(TAG,"null list");
            }
            return orderListMain == null ? 0 : orderListMain.size();
        }//ok orderListMain == null ? 0 : orderListMain.size();

        @NonNull
        @Override
        public OrderListAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_orderlist_view, parent, false);
            return new OrderListAdapter.PageViewHolder(view);
        }//ok

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
            final Order_Main orderMain = orderListMain.get(position);

            holder.tvOrderId.setText(String.valueOf(orderMain.getOrder_ID()));
            holder.tvOrderStatus.setText(orderStatusText(orderMain.getOrder_Main_Order_Status()));//apply method orderStatusText

//            fake pic for testing
            holder.ivOrderProductPic.setImageResource(R.drawable.photos_pink);
//            insert pic here
            

            holder.itemView.setOnClickListener(new View.OnClickListener() {//nevigation: go to OrderDetailFragment
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();//bring orderId and status and pic
                    bundle.putSerializable("orders", orderMain);
                    Navigation.findNavController(v).navigate(R.id.action_orderListFragment2_to_orderDetailFragment, bundle);
                }
            });
        }//ok
//setting text for order status
        private String orderStatusText(int status) {
            String statusText = "";
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

        //    set up filter
//        @Override
        public Filter getFilter() { //create the method: getFilter; when no statusFilter found, create a new one
            if (statusFilter == null){
                statusFilter = new OrderListAdapter.StatusFilter();
            }
            return statusFilter;
        }

        public class StatusFilter extends Filter{ //the StatusFilter extends Filter, with two methods
            //                        public void getOrderStatus(Order_Main orders){
//                int orderStatus = orders.getOrderStatus();
//            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
//            constraint: represent the "orderStatus" from method adapter.getFilter().filter(orderStatus)
//            status filter is the condition word of the filter
//            the method "performFiltering()" is to do the filtering of the data(orderList)
            public FilterResults performFiltering(CharSequence constraint) {
//                filter by orderStatus. if orderStatus = 0
                List<Order_Main> sortedOrdersList = new ArrayList<>();
                if (constraint == null){
                    sortedOrdersList.addAll(orderListMain);
                }else {
                    for (Order_Main orderMain : orderListMain){
                        if (Objects.equals(orderMain.getOrder_Main_Order_Status(), constraint)){//Objects.equals(orderMain.getOrderStatus(), constraint)
                            sortedOrdersList.add(orderMain);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = orderListMain.size();
                filterResults.values = orderListMain;
                return filterResults;
            }

            @Override
            public void publishResults(CharSequence constraint, FilterResults filterResults) {
               try {
                   sortedOrderList.clear();
                   sortedOrderList.addAll((List)filterResults.values);
                   notifyDataSetChanged();
               }catch (Exception e){
                   Log.e(TAG, e.toString());
               }
            }
        }//to be fixed
    }
}