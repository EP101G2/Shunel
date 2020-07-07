package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Locale;


public class LoginFragment extends Fragment {
    private final static String TAG = "TAG_LoginFragment";
    private Activity activity;
    private EditText etTypeId, etTypePassword;
    private Button btLogin, btFacebook, btGoogle;
    private TextView tvRegisterNow, tvForgetPassword, tvMessage;
    private SharedPreferences preferences;
    private CommonTask loginTask;
    private String id, password;

    final String INITIALIZED = "initialized";
    Boolean user_first;
    private View view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();


//
//        AppEventsLogger.activateApp(getA);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//move the title
//
//        setting=getSharedPreferences("setting", 0);//開啟Preferences,名稱為setting,如果存在則開啟它,否則建立新的Preferences
//
//        user_first = setting.getBoolean("FIRST",true);//取得相應的值,如果沒有該值,說明還未寫入,用true作為預設值
//
//        if(user_first){
//            //第一次登入,正常載入
//            setContentView(R.layout.activity_sign_up);
//
//        }else{
//
////如果不是第一次登入,直接跳轉到下一個介面
//            Intent intent=new Intent(this, activity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        setting.edit().putBoolean("FIRST", false).commit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity.setTitle(R.string.textLogin);

        return inflater.inflate(R.layout.fragment_login, container, false);

    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTypeId = view.findViewById(R.id.etTypePhonenumber);
        etTypePassword = view.findViewById(R.id.etTypePassword);
        btLogin = view.findViewById(R.id.btLogin);
        btFacebook = view.findViewById(R.id.btFacebook);
        btGoogle = view.findViewById(R.id.btGoogle);
        tvRegisterNow = view.findViewById(R.id.tvRegisterNow);
        tvForgetPassword = view.findViewById(R.id.tvForgetPassword);
        tvMessage = view.findViewById(R.id.tvMessage);

        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                id = etTypeId.getText().toString();
                password = etTypePassword.getText().toString();

                if (networkConnected()) {
                    String url = Common.URL_SERVER + "Uesr_Account_Servlet";                           //connect servlet(eclipse)
                    Gson gson = new Gson();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getLogin");
                    jsonObject.addProperty("id", id);
                    jsonObject.addProperty("password", password);
                    loginTask = new CommonTask(url, jsonObject.toString());
                    String jsonIn = "";
                    try {
                        jsonIn = loginTask.execute().get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    JsonObject jsonObject2 = gson.fromJson(jsonIn, JsonObject.class);
                    String result = jsonObject2.get("result").getAsString();
                    switch (result) {
                        case "fail":

                            Toast.makeText(activity, "失敗", Toast.LENGTH_SHORT).show();

                        case "success":
                            String userJstr = jsonObject2.get("user").getAsString();
                            User_Account user_account = gson.fromJson(userJstr, User_Account.class);
                            savePreferences();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("User", user_account);
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_memberFragment);
                            break;
                    }
                } else {
                    showToast(activity, R.string.textNoNetwork);
                }
            }
        });
    }

//        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
//        loadPreferences();


    // 檢查是否有網路連線
    private boolean networkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // API 23支援getActiveNetwork()
                Network network = connectivityManager.getActiveNetwork();
                // API 21支援getNetworkCapabilities()
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    String msg = String.format(Locale.getDefault(),
                            "TRANSPORT_WIFI: %b%nTRANSPORT_CELLULAR: %b%nTRANSPORT_ETHERNET: %b%n",
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                    Log.d(TAG, msg);
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                // API 29將NetworkInfo列為deprecated
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    private void savePreferences() {

        preferences.edit()
                .putString("id", id)
                .putString("password", password)
                .apply();
    }
}


//                if (number.isEmpty()) {
//                    etTypePhonenumber.setError(getString(R.string.textPhonenumberEmpty));
//                }
//                if (!password.isEmpty()) {
//                } else {
//                    etTypePassword.setError(getString(R.string.textPasswordEmpty));
//                }
//                if (number.isEmpty() || password.isEmpty()) {
//                    tvMessage.setText(R.string.textPhonenumberOrPasswordEmpty);
//                    return;
//                }
//                tvMessage.setText("");
//
//
//                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_memberFragment, bundle);
//            }
//        });
//    }
//
//    public void signUp(View view) {
//        phonenum1 = ((EditText) findViewById(R.id.et_phonenum1))
//                .getText().toString();
//        phonenum2= ((EditText) findViewById(R.id.et_phonenum2))
//                .getText().toString();
//        //whether input is phone number
//        if (!Patterns.PHONE.matcher(phonenum1).matches()) {
//            Toast.makeText(this, R.string.invalid_phonenum, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //whether the two numbers are the same
//        if (!phonenum1.equals(((EditText) findViewById(R.id.et_phonenum2))
//                .getText().toString())) {
//            Toast.makeText(this, R.string.inconsistent_phonenum,
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent=new Intent(this, MainActivity.class);
//        intent.putExtra("PHONE_NUM", phonenum2);
//        startActivity(intent);
//        finish();
//    }
//}

