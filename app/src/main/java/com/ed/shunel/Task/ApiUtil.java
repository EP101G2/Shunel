package com.ed.shunel.Task;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


//第三方支付
public class ApiUtil {
    private static final String TAG = "TAG_ApiUtil";



    // 開啟MyTask (AsyncTask) 將交易資訊送至TapPay測試區
//    public static String generatePayByPrimeCURLForSandBox(String prime, String partnerKey, String merchantId ,String totalPrice,String name,String phone ,String address) {
        public static String generatePayByPrimeCURLForSandBox(String prime, String partnerKey, String merchantId,String totalPrice,String name,String phone) {
        JSONObject paymentJO = new JSONObject();

        try {
            paymentJO.put("partner_key", partnerKey);
            paymentJO.put("prime", prime);
            paymentJO.put("merchant_id", merchantId);
            paymentJO.put("amount", totalPrice);
            paymentJO.put("currency", "TWD");
            paymentJO.put("order_number", "SN0001");
            paymentJO.put("details", "");
            JSONObject cardHolderJO = new JSONObject();
            cardHolderJO.put("phone_number","123");
            cardHolderJO.put("name", "123");
            cardHolderJO.put("email", "123");

            paymentJO.put("cardholder", cardHolderJO);
            Log.d(TAG, "paymentJO: " + paymentJO.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // TapPay測試區網址
        String url = Common.TAPPAY_DOMAIN_SANDBOX + Common.TAPPAY_PAY_BY_PRIME_URL;
        GoogleTask myTask = new GoogleTask(url, paymentJO.toString(), partnerKey);
        String result = "";
        try {
            result = myTask.execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

}
