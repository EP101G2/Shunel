package com.ed.shunel;

import android.app.Activity;
import android.content.SharedPreferences;
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


public class ModifyPasswordFragment extends Fragment {
    private Activity activity;
    private final static String TAG = "TAG_SpotUpdateFragment";
    private EditText etOldPassword, etNewPassword, etReTypeNewPassword;
    private Button btCancel, btConfirm;
    private SharedPreferences sharedPreferences;
    private String password;
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
        return inflater.inflate(R.layout.fragment_modify_password, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        etOldPassword = view.findViewById(R.id.etOldPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etReTypeNewPassword = view.findViewById(R.id.etReTypeNewPassword);
        btCancel = view.findViewById(R.id.btCancel);
        btConfirm = view.findViewById(R.id.btConfirm);
        final NavController navController = Navigation.findNavController(view);


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = Common.getPreherences(activity).getString("password", "");
                if (etOldPassword.getText().toString().equals(password)) {








                    if (etNewPassword.getText().toString().equals(etReTypeNewPassword.getText().toString())) {

                        password = etNewPassword.getText().toString();
                        user_account = new User_Account();
                        user_account.setAccount_Password(password);
                        user_account.setAccount_ID(Common.getPreherences(activity).getString("id",""));
                        if (Common.networkConnected(activity)) {
                            String url = Common.URL_SERVER + "User_Account_Servlet";                           //connect servlet(eclipse)
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("action", "UpdatePw");
                            jsonObject.addProperty("user", new Gson().toJson(user_account));
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
                                Common.getPreherences(activity).edit().putString("password",password).apply();
                                 Bundle bundle=new Bundle();
                                bundle.putSerializable("user", user_account);
                                Navigation.findNavController(v).navigate(R.id.action_modifyPasswordFragment_to_memberFragment,bundle);
                            }

                        }else {Common.showToast(activity, R.string.textNoNetwork);}
                    }

                } else {
                    Common.showToast(activity, R.string.textWrongOldPassword);
                    return;
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
