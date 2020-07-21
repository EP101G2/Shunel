package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//fragment of OrderListMain, with rvOrderList
public class OrderListFragment extends Fragment{
    private static final String TAG = "TAG";
    private Activity activity;
    private Integer counter;
    private ImageView ivNotYetDelivered, ivDelivered, ivReceived, ivCanceled, ivRefounded;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageTask orderImageTask;
    List<Orders> ordersList;
    RecyclerView rvOrderList;
    private CommonTask ordersListGetAllTask;

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
    //set up for rvOrderList
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvOrderList = view.findViewById(R.id.rvOrderList);
//        rvOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrderList.setAdapter(new OrderListAdapter(getContext(), ordersList));
        rvOrderList.setLayoutManager(new LinearLayoutManager(activity));

        ordersList = getOrders();
        showOrders(ordersList);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutOrder);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //讀取的圈圈 動畫
                swipeRefreshLayout.setRefreshing(true);
                showOrders(ordersList);
                //直到讀取完 結束
                swipeRefreshLayout.setRefreshing(false);
            }
        });
<<<<<<< HEAD

//        imageViews, onClick Listener(select from status)
        ivNotYetDelivered = ivNotYetDelivered.findViewById(R.id.ivNotYetDelivered);
        ivDelivered = ivDelivered.findViewById(R.id.ivDelivered);
        ivReceived = ivReceived.findViewById(R.id.ivReceived);
        ivCanceled = ivCanceled.findViewById(R.id.ivCanceled);
        ivRefounded = ivRefounded.findViewById(R.id.ivRefounded);
=======
//        searchViews, onClick Listener
        ivNotYetDelivered = view.findViewById(R.id.ivNotYetDelivered);
        ivDelivered = view.findViewById(R.id.ivDelivered);
        ivReceived = view.findViewById(R.id.ivReceived);
        ivCanceled = view.findViewById(R.id.ivCanceled);
        ivRefounded = view.findViewById(R.id.ivRefounded);
>>>>>>> 7479e0d5029386defce70bfdf16a1acab0a92e93



//        svNotYetDelivered.setOnSearchClickListener(new SearchView.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
//                adapter.getFilter().filter("0"); //?? setting the key: orderstatus.case0
//            }
//        });
//        svDelivered.setOnSearchClickListener(new SearchView.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
//                adapter.getFilter().filter("1"); //?? setting the key: orderstatus.case1
//            }
//        });
//        svReceived.setOnSearchClickListener(new SearchView.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
//                adapter.getFilter().filter("2"); //?? setting the key: orderstatus.case2
//            }
//        });
//        svCanceled.setOnSearchClickListener(new SearchView.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
//                adapter.getFilter().filter("3"); //?? setting the key: orderstatus.case3
//            }
//        });
//        svRefounded.setOnSearchClickListener(new SearchView.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OrderListFragment.OrderListAdapter adapter = (OrderListFragment.OrderListAdapter)rvOrderList.getAdapter();
//                adapter.getFilter().filter("4"); //?? setting the key: orderstatus.case4
//            }
//        });
    }

    //    adapter for orderList
    private class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.PageViewHolder> {//implements Filterable
        private LayoutInflater layoutInflater;
        private int imageSize;
        Context context;
        List<Orders> ordersList;
        List<Orders> sortedOrderList = new ArrayList<>();
//        OrderListAdapter.StatusFilter statusFilter;//initialise a filter

        public OrderListAdapter(Context context, List<Orders> ordersList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.ordersList = ordersList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }
        //    set up filter
//        @Override
//        public Filter getFilter() { //create the method: getFilter; when no statusFilter found, create a new one
//            if (statusFilter == null){
//                statusFilter = new OrderListAdapter.StatusFilter();
//            }
//            return statusFilter;
//        }

        void setOrders(List<Orders> ordersList) {
            this.ordersList = ordersList;
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
            return ordersList == null ? 0 : ordersList.size();
        }//ok ordersList == null ? 0 : ordersList.size();

        @NonNull
        @Override
        public OrderListAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_orderlist_view, parent, false);
            return new OrderListAdapter.PageViewHolder(view);
        }//ok

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
            final Orders orders = ordersList.get(position);

            String url = Common.URL_SERVER + "Orders_Servlet";
            int id = orders.getOrderId();
            orderImageTask = new ImageTask(url, id, imageSize, holder.ivOrderProductPic);
            orderImageTask.execute();

            holder.ivOrderProductPic.setImageResource(orders.getImageId());
            holder.tvOrderIdText.setText(R.string.textOrderIdText);
            holder.tvOrderId.setText(String.valueOf(orders.getOrderId()));
            holder.tvOrderStatusText.setText(R.string.textOrderStatusText);
            holder.tvOrderStatus.setText(String.valueOf(orders.getOrderStatus()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //nevigation: go to OrderDetailFragment
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orders", orders);
                    Navigation.findNavController(v).navigate(R.id.action_orderListFragment2_to_orderDetailFragment, bundle);
                }
            });
        }//ok



//        public class StatusFilter extends Filter{ //the StatusFilter extends Filter, with two methods
//            //                        public void getOrderStatus(Orders orders){
////                int orderStatus = orders.getOrderStatus();
////            }
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
////            constraint: represent the "orderStatus" from method adapter.getFilter().filter(orderStatus)
////            status filter is the condition word of the filter
////            the method "performFiltering()" is to do the filtering of the data(orderList)
//            public FilterResults performFiltering(CharSequence constraint) {
////                filter by orderStatus. if orderStatus = 0
//                List<Orders> sortedOrdersList = new ArrayList<>();
//                if (constraint == null){
//                    sortedOrdersList.addAll(ordersList);
//                }else {
//                    for (Orders orders : ordersList){
//                        if (Objects.equals(orders.getOrderStatus(), constraint)){//Objects.equals(orders.getOrderStatus(), constraint)
//                            sortedOrdersList.add(orders);
//                        }
//                    }
//                }
////                    switch (chosenOrderStatus) { //not yet completed!!
////                        case R.id.svNotYetDelivered://click on 1st sv and shows only orderList.getOrderStatus(0) //write a method() but use dif parameter to adjust layout(search view ex)
////                            chosenOrderStatus = 0;
////
////                            break;
////                        case R.id.svDelivered://click on 2nd sv and shows only orderList.getOrderStatus(1)
////
////                            break;
////                        case R.id.svReceived://click on 3rd sv and shows only orderList.getOrderStatus(2)
////
////                            break;
////                        case R.id.svCanceled://click on 4th sv and shows only orderList.getOrderStatus(3)
////
////                            break;
////                        case R.id.svRefounded://click on 5th sv and shows only orderList.getOrderStatus(4)
////
////                            break;
////                        default:
////                            sortedOrdersList.addAll(ordersList);
////                    }
//
////                public showByStatus(){
////                    for (Orders orders : ordersList){
////                        if (orders.getOrderStatus().equals(chosenOrderStatus)){
////                            sortedOrderList.add(orders);
////                        }
////                    }
////                    return sortedOrdersList;
////                }//dumped
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.count = ordersList.size();
//                filterResults.values = ordersList;
//                return filterResults;
//            }
//            @Override
//            public void publishResults(CharSequence constraint, FilterResults filterResults) {
//                sortedOrderList.clear();
//                sortedOrderList.addAll((List)filterResults.values);
//                notifyDataSetChanged();
//            }
//        }//probably ok(?
    }

    private List<Orders> getOrders() {
        List<Orders> ordersList = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Orders_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            ordersListGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = ordersListGetAllTask.execute().get();
                Type listType = new TypeToken<List<Orders>>() {
                }.getType();
                ordersList = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return ordersList;
    }

    private void showOrders(List<Orders> ordersList) {
        if (ordersList == null || ordersList.isEmpty()) {
            Common.showToast(activity, R.string.textnofound);
        }
        OrderListAdapter orderListAdapter = (OrderListAdapter) rvOrderList.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (orderListAdapter == null) {
            rvOrderList.setAdapter(new OrderListAdapter(activity, ordersList));
        } else {
            orderListAdapter.setOrders(ordersList);
            orderListAdapter.notifyDataSetChanged();
        }
    }
}