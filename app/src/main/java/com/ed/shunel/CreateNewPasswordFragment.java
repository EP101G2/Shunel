package com.ed.shunel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;


public class CreateNewPasswordFragment extends Fragment {
    private Activity activity;
    private EditText etNewPasswordForget, etRetypeNewPasswordForget;
    private Button btConfirm, btCancel;
    private String password,phone;
    private User_Account user_account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_password, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNewPasswordForget = view.findViewById(R.id.etNewPasswordForget);
        etRetypeNewPasswordForget = view.findViewById(R.id.etRetypeNewPasswordForget);
        btCancel = view.findViewById(R.id.btCancel);
        btConfirm = view.findViewById(R.id.btConfirm);
        final NavController navController = Navigation.findNavController(view);


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (etNewPasswordForget.getText().toString().equals(etRetypeNewPasswordForget.getText().toString())) {
                    password = etNewPasswordForget.getText().toString();
                   Bundle bundle=getArguments();
                   phone=bundle.getString("phonenumber");
//                    user_account = new User_Account();
//                    user_account.setAccount_Password(password);
//                    user_account.setAccount_ID(Common.getPreherences(activity).getString("id", ""));
                    if (Common.networkConnected(activity)) {
                        String url = Common.URL_SERVER + "User_Account_Servlet";                           //connect servlet(eclipse)
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "UpdateNewPw");
                        jsonObject.addProperty("phone", phone);
                        jsonObject.addProperty("password", password);
                        int count = 0;
                        try {
                            String result = new CommonTask(url, jsonObject.toString()).execute().get();
                            count = Integer.parseInt(result);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (count == 0) {
                            Common.showToast(activity, R.string.textUpdateFail);
                        } else {
                            Common.showToast(activity, R.string.textUpdateSuccess);
                            Common.getPreherences(activity).edit().putString("password", password).apply();

                            bundle.putSerializable("user", user_account);


                            Common.getPreherences(activity).getString("phone",phone);

                            Navigation.findNavController(v).navigate(R.id.action_createNewPasswordFragment_to_login_Fragment);

                        }

                    } else {
                        Common.showToast(activity, R.string.textNoNetwork);
                    }
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 回前一個Fragment */
                navController.popBackStack();
            }
        });

    }
}
