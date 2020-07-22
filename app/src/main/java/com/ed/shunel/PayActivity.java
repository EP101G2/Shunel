package com.ed.shunel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ed.shunel.Task.ApiUtil;
import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.Order_Main;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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

public class PayActivity extends AppCompatActivity {

    private static final String TAG = "PayActivity";
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 101;
    private Activity activity;
    private TPDGooglePay tpdGooglePay;
    private RelativeLayout btBuy;
    private TextView tvResult;
    private TextView tvPaymentInfo;
    private PaymentData paymentData;
    private Button btConfirm;
    private TextView tvtotalAmountTV;

    private Order_Main order_main;
    private CommonTask orderMainFindID;
    private String total;
    private String name;
    private String phone;
    private CommonTask chageOrder;
    private String getOrder_main;
    private int requestCode;
    private int resultCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);



        Intent intent = getIntent();
        total = intent.getStringExtra("total");
        name=intent.getStringExtra("name");
        phone=intent.getStringExtra("phone");
        getOrder_main= intent.getStringExtra("orderId");
        Log.i(TAG, "-----------------------------------orderID" +getOrder_main);




        handleViews();


        findViews();
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();

        // 使用TPDSetup設定環境。每個設定值出處參看strings.xml
        TPDSetup.initInstance(getApplicationContext(),
                Integer.parseInt(getString(R.string.TapPay_AppID)),
                getString(R.string.TapPay_AppKey),
                TPDServerType.Sandbox);


        prepareGooglePay();
    }


    //要獲取的值  就是這個引數的值
//    @Override
//    public void SendMessageValue(String strValue) {
//        // TODO Auto-generated method stub
////        tv1.setText(strValue);
//    }

    private void findViews() {

        btBuy = findViewById(R.id.btnBuy);
        tvPaymentInfo = findViewById(R.id.tvPayment);
        btConfirm = findViewById(R.id.btnConfirm);
        tvResult = findViewById(R.id.tvR);
        tvtotalAmountTV = findViewById(R.id.tvtotalAmountTV);

    }

    private void initData() {

        order_main = getData();


    }

    private Order_Main getData() {




        return null;
    }

    private void setSystemServices() {
    }

    private void setLinstener() {


        tvtotalAmountTV.setText("Total amount :" + total + "元");

        btBuy.setEnabled(false);
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳出user資訊視窗讓user確認，確認後會呼叫onActivityResult()
                tpdGooglePay.requestPayment(TransactionInfo.newBuilder()
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        // 消費總金額
                        .setTotalPrice(total)
                        // 設定幣別
                        .setCurrencyCode("TWD")
                        .build(), LOAD_PAYMENT_DATA_REQUEST_CODE);
            }
        });

        btConfirm.setEnabled(false);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeOrderStatus();
            }
        });


    }

    public void prepareGooglePay() {
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

        tpdGooglePay = new TPDGooglePay(this, tpdMerchant, tpdConsumer);
        // 檢查user裝置是否支援Google Pay
        tpdGooglePay.isGooglePayAvailable(new TPDGooglePayListener() {
            @Override
            public void onReadyToPayChecked(boolean isReadyToPay, String msg) {
                Log.d(TAG, "Pay with Google availability : " + isReadyToPay);
                if (isReadyToPay) {
                    btBuy.setEnabled(true);
                } else {
                    tvResult.setText(R.string.textCannotUseGPay);
                }
            }
        });
    }


    private void handleViews() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    btConfirm.setEnabled(true);
                    // 取得支付資訊
                    paymentData = PaymentData.getFromIntent(data);
                    if (paymentData != null) {
                        showPaymentInfo(paymentData);
                        Log.i("1111111","111111111");
                        getPrimeFromTapPay(paymentData);
                        Log.i("2222222","222222222");
                        changeOrderStatus();

                    }

                    break;
                case Activity.RESULT_CANCELED:
                    btConfirm.setEnabled(false);
                    tvResult.setText(R.string.textCanceled);
                    break;
                case AutoResolveHelper.RESULT_ERROR:
                    btConfirm.setEnabled(false);
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    if (status != null) {
                        String text = "status code: " + status.getStatusCode() +
                                " , message: " + status.getStatusMessage();
                        Log.d(TAG, text);
                        tvResult.setText(text);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void changeOrderStatus() {
        //待測試

        if (Common.networkConnected(this)) {

            String url = Common.URL_SERVER + "Orders_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "changeOrderStatus");
            jsonObject.addProperty("orderID",Integer.valueOf(getOrder_main));
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
            Common.showToast(this, R.string.textNoNetwork);
        }





    }

    private void showPaymentInfo(PaymentData paymentData) {
        try {
            JSONObject paymentDataJO = new JSONObject(paymentData.toJson());
            String cardDescription = paymentDataJO.getJSONObject("paymentMethodData").getString
                    ("description");
            tvPaymentInfo.setText(cardDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPrimeFromTapPay(PaymentData paymentData) {
        showProgressDialog();
//        name = Common.getPreherences(activity).getString("id", "");
//        phone = Common.getPreherences(activity).getString("phone", "");


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


                        Log.d(TAG, name + phone);
                        tvResult.setText(text);
                    }
                },
                new TPDTokenFailureCallback() {
                    @Override
                    public void onFailure(int status, String reportMsg) {
                        hideProgressDialog();
                        String text = "TapPay getPrime failed. status: " + status + ", message: " + reportMsg;
                        Log.d(TAG, text);
                        tvResult.setText(text);
                    }
                });
//        changeOrderStatus();

    }


    public ProgressDialog mProgressDialog;

    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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


}
