package com.ed.shunel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Task.ApiUtil;
import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Order_Main;
import com.ed.shunel.bean.Shopping_Cart;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerFragment extends Fragment {

    private final static String TAG = "BuyerFragment";
    private Activity activity;
    private List<Product> productList;
    private List<Shopping_Cart> listdatas;
    private Shopping_Cart_List shopping_cart_list;

    private String total;
    private CommonTask orderMainUpdata;
    private Order_Main oM = null;
    private Product product_list;
    private String orderId;

    /*UI元件*/
    private RecyclerView rv_Product;
    private RadioButton radioButton_Default_Address;
    private RadioButton radioButton_New_Address;
    private TextView tv_Buyer_Name;
    private TextView tv_Buyer_Phone;
    private TextView tv_Buyer_Address;
    private EditText et_Name;
    private EditText et_Phone;
    private EditText et_Address;
    private Button btn_Buyer_Confirm;
    private Button btn_Pagenext;
    private TextView tv_BuyTotal;
    private LinearLayout line_Name;
    private LinearLayout line_Phone;
    private LinearLayout line_Address;


    /*pay*/
    private TPDGooglePay tpdGooglePay;
    private RelativeLayout btBuy;
    private PaymentData paymentData;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 101;
    private int requestCode;
    private int resultCode;
    private CommonTask chageOrder;

    String id = "";
    String name = "";
    String phone = "";
    String address = "";
//    private String getOrder_main;

    public BuyerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();


        TPDSetup.initInstance(getActivity(),
                Integer.parseInt(getString(R.string.TapPay_AppID)),
                getString(R.string.TapPay_AppKey),
                TPDServerType.Sandbox);


        prepareGooglePay();

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

    private void findViews(View view) {

        rv_Product = view.findViewById(R.id.rv_Product);

        tv_Buyer_Name = view.findViewById(R.id.tv_Buyer_Name);
        tv_Buyer_Phone = view.findViewById(R.id.tv_Phone);
        tv_Buyer_Address = view.findViewById(R.id.tv_Address);
        btn_Pagenext = view.findViewById(R.id.btn_Pagenext);
        tv_BuyTotal = view.findViewById(R.id.tv_BuyTotal);
        btBuy = view.findViewById(R.id.btnBuy);



        /*bundle 接收---------------------------------------------------------------------*/
        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("shopcard") == null) {
            Common.showToast(activity, R.string.textNoFound);
            navController.popBackStack();
            return;
        }


        shopping_cart_list = (Shopping_Cart_List) bundle.getSerializable("shopcard");
        total = bundle.getString("total");
        orderId = bundle.getString("orderId");
        /*bundle 接收---------------------------------------------------------------------*/

    }

    private void initData() {


    }

    private void setSystemServices() {
    }

    private void setLinstener() {

        id = Common.getPreherences(activity).getString("id", "");
        name = Common.getPreherences(activity).getString("name", "");
        phone = Common.getPreherences(activity).getString("phone", "");
        address = Common.getPreherences(activity).getString("address", "");
//        Log.e(TAG,id);
//        Log.e(TAG,name);
//        Log.e(TAG,phone);
//        Log.e(TAG,address);
        tv_Buyer_Name.setText(name);
        tv_Buyer_Address.setText(address);
        tv_Buyer_Phone.setText(phone);

        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳出user資訊視窗讓user確認，確認後會呼叫onActivityResult()
                Log.i(TAG, "btbuy1");
                tpdGooglePay.requestPayment(TransactionInfo.newBuilder()
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        // 消費總金額
                        .setTotalPrice("10")
                        // 設定幣別
                        .setCurrencyCode("TWD")
                        .build(), LOAD_PAYMENT_DATA_REQUEST_CODE);

            }
        });

        tv_BuyTotal.setText("總金額：" + total);

        rv_Product.setAdapter(new productAdapter(activity, shopping_cart_list.getCart()));
        rv_Product.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        oM = new Order_Main(id, Integer.parseInt(total), name, address, phone, 0);
        btn_Pagenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Navigation.findNavController(v).navigate(R.id.action_buyerFragment_to_orderListFragment2);


            }
        });


    }

    private class productAdapter extends RecyclerView.Adapter<productAdapter.Myviewholder> {
        Context context;
        List<Shopping_Cart> productList;

        public productAdapter(Context context, List<Shopping_Cart> productList) {
            this.context = context;
            this.productList = productList;

        }

        @NonNull
        @Override
        public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_shoppingcart_itemview, parent, false);
            return new Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
            final Shopping_Cart list = productList.get(position);

            String url = Common.URL_SERVER + "Prouct_Servlet";
            int id = list.getProduct_ID();
            int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
            Bitmap bitmap = null;
            try {
                bitmap = new ImageTask(url, id, imageSize).execute().get();
            } catch (Exception e) {
//                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
            if (bitmap != null) {
                holder.iv_Prouct.setImageBitmap(bitmap);
            } else {
                holder.iv_Prouct.setImageResource(R.drawable.no_image);
            }


            holder.tv_Name.setText(list.getProduct_Name());
            holder.tv_Count.setText("數量：" + String.valueOf(list.getAmount()));
            holder.tv_Price.setText(String.valueOf(list.getPrice() * list.getAmount()));
            holder.checkBox.setVisibility(View.GONE);
            holder.tv_Less.setVisibility(View.GONE);
            holder.tv_Add.setVisibility(View.GONE);


        }

        @Override
        public int getItemCount() {
            return product_list == null ? 0 : productList.size();
        }

        private class Myviewholder extends RecyclerView.ViewHolder {
            ImageView iv_Prouct;
            TextView tv_Name;
            TextView tv_specification;
            TextView tv_Price;
            TextView tv_Add;
            TextView tv_Count;
            TextView tv_Less;
            CheckBox checkBox;


            public Myviewholder(View view) {
                super(view);
                iv_Prouct = view.findViewById(R.id.iv_Prouct);
                tv_Name = view.findViewById(R.id.tv_Name);
                tv_specification = view.findViewById(R.id.tv_specification);
                tv_Price = view.findViewById(R.id.tv_Price);
                tv_Add = view.findViewById(R.id.btAdd);
                tv_Count = view.findViewById(R.id.tv_Count);
                tv_Less = view.findViewById(R.id.btLess);
                checkBox = view.findViewById(R.id.checkBox);

            }
        }
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
//                    btConfirm.setEnabled(true);
                    // 取得支付資訊
                    paymentData = PaymentData.getFromIntent(data);
                    if (paymentData != null) {
                        showPaymentInfo(paymentData);
                        getPrimeFromTapPay(paymentData);
                        changeOrderStatus();
//                        Navigation.findNavController(view).navigate(R.id.action_buyerFragment_to_orderListFragment2);
                        Common.showToast(activity,"交易成功");
                    }
                    break;
                case Activity.RESULT_CANCELED:
//                    btConfirm.setEnabled(false);
//                    tvResult.setText(R.string.textCanceled);
                    Common.showToast(activity,"交易失敗");
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
                                getString(R.string.TapPay_MerchantID), total, name, phone);

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

    private void next(View view) {

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


    private void changeOrderStatus() {//未付款改以付款
        //待測試

        if (Common.networkConnected(activity)) {

            String url = Common.URL_SERVER + "Orders_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "changeOrderStatus");
            jsonObject.addProperty("OrderID",Integer.valueOf(orderId));
            chageOrder= new CommonTask(url, jsonObject.toString());

            Log.i(TAG,chageOrder.toString());
            try {
                String jsonIn = chageOrder.execute().get();
//                jsonObject = new Gson().fromJson(jsonIn,JsonObject.class);
                Log.i(TAG,jsonObject.toString());

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
