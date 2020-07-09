package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;


public class Register_Fragment extends Fragment {

    private final static String TAG = "TAG_SpotInsertFragment";
    private Activity activity;
    private User_Account user_account;
    private EditText etTypeName,etTypeAccountId, etTypePhonenumber, etTypePassword,etTypeAddress;
    private Button btRegister;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        etTypeName=view.findViewById(R.id.etTypeName);
        etTypeAccountId = view.findViewById(R.id.etTypeAccountId);
        etTypePhonenumber = view.findViewById(R.id.etTypePhonenumber);
        etTypePassword = view.findViewById(R.id.etTypePassword);
        etTypeAddress=view.findViewById(R.id.etTypeAddress);
        btRegister = view.findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "Uesr_Account_Servlet";
                    String status = btRegister.getText().toString();
                    String name =etTypeName.getText().toString();
                    String  id = etTypeAccountId.getText().toString();
                    String phonenumber =etTypePhonenumber.getText().toString();
                    String password =etTypePassword.getText().toString();
                    String address =etTypeAddress.getText().toString();

                    user_account = new User_Account(name,id, phonenumber, password, address);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "Register");
                    jsonObject.addProperty("user", new Gson().toJson(user_account));

                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        int resultType= Integer.parseInt(result);

                        if (resultType==0){
                            Common.showToast(activity, R.string.textInsertFail);
                        }else {
                            Common.showToast(activity, R.string.textInsertSuccess);
//
//
//
//                            String userJstr = jsonObject2.get("user").getAsString();
//                            if(userJstr != null) {
//                                User_Account user_account = gson.fromJson(userJstr, User_Account.class);
//                                savePreferences();
//                                Bundle bundle=new Bundle();

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


    }
    }
