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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private static final String ARG_COUNT = "TAG_OrderDetailFragment";
    private TextView tvOrderId, tvOrderStatus, tvTotalPrice, tvName, tvPhone, tvAddress;// tvProductName, tvProductPrice;
    //    private TextView tvOrderIdText, tvOrderDetStatusText, tvTotalPriceText, tvReceiverTitle, tvOrderDNameT, tvOrderDPhoneT, tvOrderDetailAddressT; need not to
//    private ImageView ivOrderProductPic;//add later
    private RecyclerView rvOrderDetProList;
    private List<OrderDetail> orderDetailList;
    private Activity activity;
    private Integer counter;
    private CommonTask ordersGetAllTask;

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
    }//ok

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null){
            counter = getArguments().getInt(ARG_COUNT);
        }
    }//ok

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }//ok

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderDetailList = getOrderDetailList();
        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvOrderStatus = view.findViewById(R.id.tvOrderStatus);
        tvName = view.findViewById(R.id.tvName);//=Receiver in DB
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);

        rvOrderDetProList = view.findViewById(R.id.rvOrderDetProList);
        rvOrderDetProList.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderDetProList.setAdapter(new OrderDetailAdapter(getContext(),orderDetailList));

    }

    private class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.PageViewHolder>{
        Context context;
        List<OrderDetail> orderDetailList;
        public OrderDetailAdapter (Context context, List<OrderDetail> orderDetailList){
            this.context = context;
            this.orderDetailList = orderDetailList;
        }//ok

        @Override
        public int getItemCount() {
            return orderDetailList.size();
        }//ok

        private class PageViewHolder extends RecyclerView.ViewHolder {
            //           TextView tvOrderId, tvOrderStatus, tvTotalPrice, tvName, tvPhoneNum, tvAddress;
            TextView tvProductName, tvProductPrice;
            TextView tvProductPriceT;
            ImageView ivOrderProductPic;

            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
//                tvProductPriceT = itemView.findViewById(R.id.tvProductPriceT);
                ivOrderProductPic = itemView.findViewById(R.id.ivOrderProductPic);
//                need not to be shown here
//                tvOrderId = itemView.findViewById(R.id.tvOrderId);
//                tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
//                tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
//                tvName = itemView.findViewById(R.id.tvName);
//                tvPhoneNum = itemView.findViewById(R.id.tvPhone);
//                tvAddress = itemView.findViewById(R.id.tvAddress);
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
            final OrderDetail orderDetail = orderDetailList.get(position);
//            holder.tvOrderId.setText(orderDetail.getOrderId());
//            holder.tvOrderStatus.setText(orderDetail.getOrderStatus());
//            holder.tvTotalPrice.setText(orderDetail.getTotalPrice());//create class: orderDetail,(check), fix orderDetail follow the DB
//            holder.tvName.setText(orderDetail.getReceiver());
//            holder.tvPhoneNum.setText(orderDetail.getPhone());
//            holder.tvAddress.setText(orderDetail.getAddress());
            holder.tvProductName.setText(orderDetail.getProductName());
            holder.tvProductPrice.setText(orderDetail.getBuyPrice());
//            holder.ivOrderProductPic.setImageResource(); //get image from db, product

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //navigation to productDetail
                    Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_productDetailFragment);//follow the main name of productDet
                }
            });
        } //need to be fixed!!

    }


    private List<OrderDetail> getOrderDetailList(){
        List<OrderDetail> orderDetailList = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Orders_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            ordersGetAllTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = ordersGetAllTask.execute().get();
                Type listType = new TypeToken<List<Orders>>() {
                }.getType();
                orderDetailList = new Gson().fromJson(jsonIn, listType);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("--------------",orderDetailList+"");
        return orderDetailList;
    }//not yet
}