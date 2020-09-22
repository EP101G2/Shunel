package com.ed.shunel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ed.shunel.Task.ApiUtil;
import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
//import com.ed.shunel.bean.Order_Detail;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Order_Detail;
import com.ed.shunel.bean.Order_Main;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tech.cherri.tpdirect.api.TPDCardInfo;
import tech.cherri.tpdirect.api.TPDConsumer;
import tech.cherri.tpdirect.api.TPDGooglePay;
import tech.cherri.tpdirect.api.TPDMerchant;
import tech.cherri.tpdirect.api.TPDServerType;
import tech.cherri.tpdirect.api.TPDSetup;
import tech.cherri.tpdirect.callback.TPDGooglePayListener;
import tech.cherri.tpdirect.callback.TPDTokenFailureCallback;
import tech.cherri.tpdirect.callback.TPDTokenSuccessCallback;

import static com.ed.shunel.Task.Common.CARD_TYPES;

//1. rv orderDetail, 2. nav main to product detail
public class OrderDetailFragment extends Fragment {
    private static final String TAG = "-OrderDetailFragment-";
    private TextView tvOrderIdDet, tvOrderStatus, tvTotalPrice, tvName, tvPhone, tvAddress;// tvProductName, tvProductPrice;
    private RecyclerView rvOrderDetProList;
    private List<Order_Detail> orderDetailList;
    private Order_Main orderMain;
    private Activity activity;
    private Integer counter;
    private CommonTask ordersDetGetTask;
    private int productId;

    /*Jack*/
    private boolean statusBoolean = false;
    private String status;
    private ImageView btnOrderBuy;
    private TPDGooglePay tpdGooglePay;
    private PaymentData paymentData;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 101;
    private int requestCode;
    private int resultCode;
    String id = "";
    String name = "";
    String phone = "";
    String address = "";
    String price = "";
    int orderFinalID = 0;
    private CommonTask chageOrder;

    /*Jack*/
    public OrderDetailFragment() {
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
        if (getArguments() != null) {
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
        /*Jack*/
        btnOrderBuy = view.findViewById(R.id.btnOrderBuy);

        TPDSetup.initInstance(getActivity(),
                Integer.parseInt(getString(R.string.TapPay_AppID)),
                getString(R.string.TapPay_AppKey),
                TPDServerType.Sandbox);

        prepareGooglePay();
        /*Jack*/


        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("Order") == null) {
            Common.showToast(activity, R.string.textnofound);
            navController.popBackStack();
            return;
        }
        orderMain = (Order_Main) bundle.getSerializable("Order");

        Log.e(TAG, "bundleGet" + orderMain.getOrder_ID() + "," + orderMain.getOrder_Main_Order_Status() + "," + orderMain.getOrder_Main_Total_Price() + "," + orderMain.getOrder_Main_Receiver());

        showOrderDetails(orderMain);

        orderDetailList = getOrderDetailList();
        rvOrderDetProList = view.findViewById(R.id.rvOrderDetProList);
        rvOrderDetProList.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderDetProList.setAdapter(new OrderDetailAdapter(getContext(), orderDetailList)); //rvAdapter

        if (orderMain.getOrder_Main_Order_Status() != 0) {
            btnOrderBuy.setVisibility(View.GONE);
        }

        btnOrderBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tpdGooglePay.requestPayment(TransactionInfo.newBuilder()
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        // 消費總金額
                        .setTotalPrice("10")
                        // 設定幣別
                        .setCurrencyCode("TWD")
                        .build(), LOAD_PAYMENT_DATA_REQUEST_CODE);
            }
        });

    }

    private void prepareGooglePay() {

        TPDMerchant tpdMerchant = new TPDMerchant();
        // 設定商店名稱
        tpdMerchant.setMerchantName(getString(R.string.TapPay_MerchantName));
        // 設定允許的信用卡種類
        tpdMerchant.setSupportedNetworks(CARD_TYPES);
        // 設定客戶填寫項目
        TPDConsumer tpdConsumer = new TPDConsumer();
        // 不需要電話號碼
        tpdConsumer.setPhoneNumberRequired(false);
        // 不需要運送地址
        tpdConsumer.setShippingAddressRequired(false);
        // 不需要Email
        tpdConsumer.setEmailRequired(false);

        tpdGooglePay = new TPDGooglePay(activity, tpdMerchant, tpdConsumer);
        // 檢查user裝置是否支援Google Pay
        tpdGooglePay.isGooglePayAvailable(new TPDGooglePayListener() {
            @Override
            public void onReadyToPayChecked(boolean isReadyToPay, String msg) {
                Log.d(TAG, "Pay with Google availability : " + isReadyToPay);
                if (isReadyToPay) {
//                    btBuy.setEnabled(true);
                } else {
//                    tvResult.setText(R.string.textCannotUseGPay);
                }
            }
        });

    }

    private List<Order_Detail> getOrderDetailList() {
        List<Order_Detail> orderDetailList = new ArrayList<>();
        try {
            int orderId = Integer.parseInt(tvOrderIdDet.getText().toString());
            Log.e(TAG, "getOrderIdFromTvOrderIdDet: " + orderId);
            if (Common.networkConnected(activity)) {
//                get data from orders servlet
                String url = Common.URL_SERVER + "Orders_Servlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrderedProducts");
                jsonObject.addProperty("order_Id", orderId);
                String jsonOut = jsonObject.toString();
                ordersDetGetTask = new CommonTask(url, jsonOut);
                Log.e(TAG, "getOrderedProducts: out -> " + jsonOut);
                try {
                    String jsonIn = ordersDetGetTask.execute().get();
                    Type listType = new TypeToken<List<Order_Detail>>() {
                    }.getType();
                    orderDetailList = new Gson().fromJson(jsonIn, listType);
                    for (Order_Detail od : orderDetailList
                    ) {
                        productId = od.getorderDetProductId();
                        Log.e(TAG, "productid get: " + productId);
                    }
                    Log.e(TAG, "getOrderedProducts: in -> " + jsonIn);
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

        /*Jack*/
        orderFinalID = orderMain.getOrder_ID();
        status = orderStatusText(orderMain.getOrder_Main_Order_Status());
        name = orderMain.getOrder_Main_Receiver();
        address = orderMain.getOrder_Main_Address();
        price = String.valueOf(orderMain.getOrder_Main_Total_Price());
        /*Jack*/


        tvOrderIdDet.setText(String.valueOf(id));
        String orderIdStr = tvOrderIdDet.getText().toString();
        Log.e(TAG, "showOrderDetail-orderId: " + orderIdStr);

        tvOrderStatus.setText(status);
        tvTotalPrice.setText(String.valueOf(orderMain.getOrder_Main_Total_Price()));

        tvName.setText(orderMain.getOrder_Main_Receiver());
        tvPhone.setText(orderMain.getOrder_Main_Phone());
        tvAddress.setText(orderMain.getOrder_Main_Address());


    }

    private String orderStatusText(int status) {
        String statusText = "";
//            Log.e(TAG, "status"+status);
        if (status == 0) {
            statusText = "未付款";
        } else if (status == 1) {
            statusText = "未出貨";
        } else if (status == 2) {
            statusText = "已出貨";
        } else if (status == 3) {
            statusText = "已送達";
        } else if (status == 4) {
            statusText = "已取消";
        } else if (status == 5) {
            statusText = "已退貨";
        }
        return statusText;
    }

    //adapter
    private class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.PageViewHolder> {
        Context context;
        List<Order_Detail> orderDetailList;
        ImageTask orderDetProdImgTask;
        private int imageSize;
//        private LayoutInflater inflater;

        public OrderDetailAdapter(Context context, List<Order_Detail> orderDetailList) {
            this.context = context;
            this.orderDetailList = orderDetailList;
            imageSize = context.getResources().getDisplayMetrics().widthPixels / 100;
//        inflater = LayoutInflater.from(context);
        }//ok

        @Override
        public int getItemCount() {
            try {
                if (orderDetailList != null) {
                    Log.e(TAG, "itemCount:" + orderDetailList.size());
                    return orderDetailList == null ? 0 : orderDetailList.size();
                }
            } catch (Exception e) {
                Log.e(TAG, "null list");
            }
            return orderDetailList == null ? 0 : orderDetailList.size();
        }//ok

        public void setOrderedProduct(List<Order_Detail> orderDetailList) {
            this.orderDetailList = orderDetailList;
            Log.e(TAG, "orderDetList" + orderDetailList);
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
                orderDetProdImgTask = new ImageTask(url, productIdOD, imageSize, holder.ivOrderProductPicDet);
                orderDetProdImgTask.execute();
//                holder.ivOrderProductPicDet.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }


            holder.tvProductName.setText(orderDetail.getProduct_Name());
            holder.tvProductPrice.setText("$" + orderDetail.getOrder_Detail_Buy_Price());
//            holder.ivOrderProductPicDet.setImageResource(R.drawable.photos_pink);

//            get product pic through product ID
            Log.e(TAG, "orderDetail's id: " + orderDetail.getorderDetProductId());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //navigation to productDetail
//                    if (status.equals("未付款")){
//                        Intent intent= new Intent(activity,BuyerFragment.class);
//                        startActivity(intent);
//                    }

//                    Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_productDetailFragment);//follow the main name of productDet
                }
            });
        } //need to be fixed!!

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        View view = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:

                    // 取得支付資訊
                    paymentData = PaymentData.getFromIntent(data);
                    if (paymentData != null) {
                        showPaymentInfo(paymentData);
                        getPrimeFromTapPay(paymentData);
                        changeOrderStatus(orderMain);
                        Common.showToast(activity, "交易成功");
                    }
                    break;
                case Activity.RESULT_CANCELED:
//                    btConfirm.setEnabled(false);
//                    tvResult.setText(R.string.textCanceled);
                    Common.showToast(activity, "交易失敗");
                    break;
                case AutoResolveHelper.RESULT_ERROR:
//                    btConfirm.setEnabled(false);
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    if (status != null) {
                        String text = "status code: " + status.getStatusCode() +
                                " , message: " + status.getStatusMessage();
                        Log.d(TAG, text);
//                        tvResult.setText(text);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void showPaymentInfo(PaymentData paymentData) {

        try {
            JSONObject paymentDataJO = new JSONObject(paymentData.toJson());
            String cardDescription = paymentDataJO.getJSONObject("paymentMethodData").getString
                    ("description");
//            tvPaymentInfo.setText(cardDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getPrimeFromTapPay(PaymentData paymentData) {
        showProgressDialog();
        /* 呼叫getPrime()只將支付資料提交給TapPay以取得prime (代替卡片的一次性字串，此字串的時效為 30 秒)，
            參看https://docs.tappaysdk.com/google-pay/zh/reference.html#prime */
        tpdGooglePay.getPrime(
                paymentData,
                new TPDTokenSuccessCallback() {
                    @Override
                    /* 一般而言，手機提交支付、信用卡資料給TapPay後，TapPay會將信用卡等資訊送至Bank確認是否合法，
                           Bank會回一個暫時編號給TapPay方便後續支付確認，TapPay儲存該編號後再回一個自編prime給手機，
                           手機再傳給server，server再呼叫payByPrime方法提交給TapPay，以確認這筆訂單，
                           此時server就可發訊息告訴user訂單成立。
                           參看圖示 https://docs.tappaysdk.com/google-pay/zh/home.html#home 向下捲動即可看到 */
                    public void onSuccess(String prime, TPDCardInfo tpdCardInfo) {
                        hideProgressDialog();

                        String text = "Your prime is " + prime
                                + "\n\nUse below cURL to proceed the payment : \n"
                                /* 手機得到prime後，一般會傳給商家server端再呼叫payByPrime方法提交給TapPay，以確認這筆訂單
                                   現在為了方便，手機直接提交給TapPay */
                                + ApiUtil.generatePayByPrimeCURLForSandBox(prime,
                                getString(R.string.TapPay_PartnerKey),
                                getString(R.string.TapPay_MerchantID), price, name, phone);

                    }
                },
                new TPDTokenFailureCallback() {
                    @Override
                    public void onFailure(int status, String reportMsg) {
                        hideProgressDialog();
                        String text = "TapPay getPrime failed. status: " + status + ", message: " + reportMsg;
                        Log.d(TAG, text);
//                        tvResult.setText(text);
                    }
                });
    }

    public ProgressDialog mProgressDialog;

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();


    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void changeOrderStatus(Order_Main oM) {
        //待測試

        if (Common.networkConnected(activity)) {

            String url = Common.URL_SERVER + "Orders_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "changeOrderStatus");
            jsonObject.addProperty("OrderID", Integer.valueOf(oM.getOrder_ID()));
            jsonObject.addProperty("oM",new Gson().toJson(oM));
            chageOrder = new CommonTask(url, jsonObject.toString());

            Log.i(TAG, chageOrder.toString());
            try {
                String jsonIn = chageOrder.execute().get();
//                jsonObject = new Gson().fromJson(jsonIn,JsonObject.class);
                Log.i(TAG, jsonObject.toString());

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }


    }

}