package com.ed.shunel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.User_Account;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class RegisterFragment extends Fragment {

    private final static String TAG = "TAG_SpotInsertFragment";
    private Activity activity;
    private User_Account user_account;
    private EditText etTypeName, etTypeAccountId, etTypePhonenumber, etTypePassword, etTypeAddress, etReTypePassword;
    private Button btRegister;
    private String token;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        etTypeName = view.findViewById(R.id.etTypeName);
        etTypeAccountId = view.findViewById(R.id.etTypeAccountId);
        etTypePhonenumber = view.findViewById(R.id.etTypePhonenumber);
        etTypePassword = view.findViewById(R.id.etTypePassword);
        etTypeAddress = view.findViewById(R.id.etTypeAddress);
        etReTypePassword = view.findViewById(R.id.etReTypePassword);
        btRegister = view.findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etTypePassword.getText().toString().equals(etReTypePassword.getText().toString())) {
                    Common.showToast(activity, "輸入密碼與再次輸入密碼不吻合");
                    return;
                }


                if (etTypeName.length() == 0) {
                    etTypeName.setError("請輸入中英文");
                } else if (etTypeAccountId.length() == 0) {
                    etTypeAccountId.setError("請輸入15位數內英文或數字");
                } else if (etTypePhonenumber.length() == 0) {
                    etTypePhonenumber.setError("請輸入15位數內數字");
                } else if (etTypePassword.length() == 0) {
                    etTypePassword.setError("請輸入15位數內英文或數字");
                } else if (etReTypePassword.length() == 0) {
                    etReTypePassword.setError("請和密碼輸入相同");
                } else if (etTypeAddress.length() == 0) {
                    etTypeAddress.setError("請輸入15位數內英文或數字之符號");
                }


                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "User_Account_Servlet";
                    String status = btRegister.getText().toString();
                    String name = etTypeName.getText().toString();
                    String id = etTypeAccountId.getText().toString();
                    String phonenumber = etTypePhonenumber.getText().toString();
                    String password = etTypePassword.getText().toString();
                    String address = etTypeAddress.getText().toString();
                    String token = FirebaseInstanceId.getInstance().getToken();
                    Log.e("token","======"+token);
                    user_account = new User_Account(name, id, phonenumber, password, address,token);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "Register");
                    jsonObject.addProperty("user", new Gson().toJson(user_account));

                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();//巷ＳＥＶＥＲ提出註冊請球，彈出的是註冊節過0貨1
                        int resultType = Integer.parseInt(result);

                        if (resultType == 0) {



                            Common.showToast(activity, R.string.textInsertFail);
                        } else {

                            savePreferences();

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);    //當你在使用物件後還有其他動作要執行，補充資料在JAVA-slide-ch0805
                            LayoutInflater inflater = LayoutInflater.from(activity);
                            final View view =inflater.inflate(R.layout.resistersuccess,null);
                                    builder.setView(view);
                                    builder.create().show();

                            Intent intent = new Intent();
                            intent.setClass(activity, MainActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
                            startActivity(intent);

                            activity.finish();//把自己關掉


//
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());


                    }


                } else {
                    Common.showToast(activity, R.string.textNoNetwork);
                }
                /* 回前一個Fragment */
//                navController.popBackStack();
            }
        });

    }


    private void savePreferences() {

        //置入name屬性的字串

        Common.getPreherences(activity).edit().putString("id",etTypeAccountId.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("password",etTypePassword.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("name",etTypeName.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("phone",etTypePhonenumber.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("address",etTypeAddress.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("getToken",FirebaseInstanceId.getInstance().getToken());

        Log.i(TAG, "-------------------------------------------------------------");

    }
}
