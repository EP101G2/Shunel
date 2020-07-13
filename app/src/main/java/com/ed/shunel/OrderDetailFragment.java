package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//1. rv orderDetail, 2. nav main to product detail
public class OrderDetailFragment extends Fragment {
    private static final String TAG = "TAG_OrderDetailFragment";
    private static final String ARG_COUNT = "param1";
    private TextView tvOrderId, tvOrderStatus, tvTotalPrice, tvName, tvPhoneNum, tvAddress;
    private RecyclerView rvOrderDetProList;
    private List<Orders> orderDetail;
    private List<Product> products;//in the main project
    private Activity activity;
    private Integer counter;
//    product ID
//    private commonTask orderGetAllTask;
//    private ImageTask orderImageTask;

//    public OrderDetailFragment() {
//        // Required empty public constructor
//    }
//    // TODO: Rename and change types and number of parameters

    public OrderDetailFragment(){
//        Required empty public constructor
    }

    public static OrderDetailFragment newInstance(Integer counter) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null){
            counter = getArguments().getInt(ARG_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderDetail = getOrderDetail();

        rvOrderDetProList = view.findViewById(R.id.rvOrderDetProList);
        rvOrderDetProList.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderDetProList.setAdapter(new OrderDetailAdapter());

    }

    private class OrderDetailAdapter extends RecyclerView.Adapter<PageViewHolder>{
        Context context;
        List<Orders> orderDetList;

        @NonNull
        @Override
        public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return orderDetail.size();
        }
    }

    private class PageViewHolder extends RecyclerView.ViewHolder {
        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private List<Orders> getOrderDetail(){
        List<Orders> ordersList = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "OrderList_Servlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            ordersGetAllTask = new CommonTask(url, jsonObject.toString());
//            try {
//                String jsonIn = ordersGetAllTask.execute().get();
//                Type listType = new TypeToken<List<Product>>() {
//                }.getType();
//                products = new Gson().fromJson(jsonIn, listType);
//
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//        }
//        Log.e("--------------",ordersList+"");
        return ordersList;
    }//not yet
}