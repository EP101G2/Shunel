package com.ed.shunel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONException;
import org.json.JSONObject;

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
public class DeliveryFragment extends Fragment {

    private static final String TAG = "DeliveryFragment";
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 101;
    private Activity activity;
    private TPDGooglePay tpdGooglePay;
    private RelativeLayout btBuy;
    private TextView tvResult;
    private TextView tvPaymentInfo;
    private PaymentData paymentData;
    private Button btConfirm;

    public DeliveryFragment() {
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
        return inflater.inflate(R.layout.fragment_delivery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleViews(view);


        Log.d(TAG, "SDK version is " + TPDSetup.getVersion());

        // 使用TPDSetup設定環境。每個設定值出處參看strings.xml
        TPDSetup.initInstance(getActivity(),
                Integer.parseInt(getString(R.string.TapPay_AppID)),
                getString(R.string.TapPay_AppKey),
                TPDServerType.Sandbox);




//        TPDSetup.initInstance(getApplicationContext(),
//                Integer.parseInt(getString(R.string.TapPay_AppID)),
//                getString(R.string.TapPay_AppKey),
//                TPDServerType.Sandbox);

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
                    btBuy.setEnabled(true);
                } else {
                    tvResult.setText(R.string.textCannotUseGPay);
                }
            }
        });


    }

    private void handleViews(View view) {


        btBuy = view.findViewById(R.id.btnBuy);
        btBuy.setEnabled(false);
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳出user資訊視窗讓user確認，確認後會呼叫onActivityResult()
                Log.i(TAG,"btbuy1");
                tpdGooglePay.requestPayment(TransactionInfo.newBuilder()
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        // 消費總金額
                        .setTotalPrice("10")
                        // 設定幣別
                        .setCurrencyCode("TWD")
                        .build(), LOAD_PAYMENT_DATA_REQUEST_CODE);
            }
        });

        tvPaymentInfo = view.findViewById(R.id.tvPayment);

        btConfirm = view.findViewById(R.id.btnConfirm);
        btConfirm.setEnabled(false);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrimeFromTapPay(paymentData);
            }
        });


        tvResult = view.findViewById(R.id.tvR);




    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    btConfirm.setEnabled(true);
//                    // 取得支付資訊
//                    paymentData = PaymentData.getFromIntent(data);
//                    if (paymentData != null) {
//                        showPaymentInfo(paymentData);
//                    }
//                    break;
//                case Activity.RESULT_CANCELED:
//                    btConfirm.setEnabled(false);
//                    tvResult.setText(R.string.textCanceled);
//                    break;
//                case AutoResolveHelper.RESULT_ERROR:
//                    btConfirm.setEnabled(false);
//                    Status status = AutoResolveHelper.getStatusFromIntent(data);
//                    if (status != null) {
//                        String text = "status code: " + status.getStatusCode() +
//                                " , message: " + status.getStatusMessage();
//                        Log.d(TAG, text);
//                        tvResult.setText(text);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


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
//
//                        String text = "Your prime is " + prime
//                                + "\n\nUse below cURL to proceed the payment : \n"
//                                /* 手機得到prime後，一般會傳給商家server端再呼叫payByPrime方法提交給TapPay，以確認這筆訂單
//                                   現在為了方便，手機直接提交給TapPay */
//                                + ApiUtil.generatePayByPrimeCURLForSandBox(prime,
//                                getString(R.string.TapPay_PartnerKey),
//                                getString(R.string.TapPay_MerchantID),getString(t));
//                        Log.d(TAG, text);
//                        tvResult.setText(text);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    btConfirm.setEnabled(true);
                    // 取得支付資訊
                    paymentData = PaymentData.getFromIntent(data);
                    if (paymentData != null) {
                        showPaymentInfo(paymentData);
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

}
